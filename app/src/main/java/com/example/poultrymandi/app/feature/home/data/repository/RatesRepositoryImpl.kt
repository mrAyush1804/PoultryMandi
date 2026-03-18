package com.example.poultrymandi.app.feature.home.data.repository

import android.util.Log
import com.example.poultrymandi.app.feature.home.data.model.DataItem
import com.example.poultrymandi.app.feature.home.domain.data.*
import com.example.poultrymandi.app.feature.home.domain.repository.RatesRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RatesRepository {

    // ── DATE SECTION ──────────────────────────────
    // getLast7Days() — aaj se 6 din pehle tak generate karo
    // isAvailable1 field mein "yyyy-MM-dd" store hoga
    // Firestore document ID se match karta hai
    // Today = list ka last item (index 6)
    override fun getLast7Days(): List<DataItem> {
        val calendar = Calendar.getInstance()
        val days = mutableListOf<DataItem>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayLabelFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())

        for (i in 0 until 7) {
            val date = calendar.time
            days.add(
                DataItem(
                    day = calendar.get(Calendar.DAY_OF_MONTH),
                    dayLabel = dayLabelFormat.format(date),
                    month = monthFormat.format(date),
                    year = calendar.get(Calendar.YEAR),
                    isAvailable1 = dateFormat.format(date),
                    isAvailable = true
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return days.reversed()
    }

    // ── STATES SECTION ────────────────────────────
    // Real-time: rates/{date}/states pe addSnapshotListener
    // Admin Firestore update kare → snapshot fire → UI refresh ✅
    // StateDomain(name, cities) map karo
    override fun getRatesForDate(dateString: String): Flow<List<StateDomain>> = callbackFlow {
        val listener = firestore.collection("rates")
            .document(dateString)
            .collection("states")
            .addSnapshotListener { statesSnapshot, error ->
                if (error != null) {
                    Log.e("RatesRepo", "getRatesForDate: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                val states = mutableListOf<StateDomain>()
                val documents = statesSnapshot?.documents ?: emptyList()
                
                if (documents.isEmpty()) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                var pendingTasks = documents.size
                documents.forEach { stateDoc ->
                    val stateName = stateDoc.getString("name") ?: ""
                    
                    // ── CITIES SECTION ────────────────────────────
                    // Har state ke andar cities sub-collection fetch karo (.get())
                    // Fields: city(String), todayPrice(Double), yesterdayPrice(Double)
                    // trend: "UP"|"DOWN"|"STABLE" → PriceTrend enum
                    // unit: "/kg" default
                    stateDoc.reference.collection("cities").get()
                        .addOnSuccessListener { citiesSnapshot ->
                            val cities = citiesSnapshot.documents.mapNotNull { cityDoc ->
                                val cityName = cityDoc.getString("city") ?: return@mapNotNull null
                                val trendStr = cityDoc.getString("trend") ?: "STABLE"
                                
                                // ── RATE UPDATE TIMING ────────────────────────
                                val lastUpdated = cityDoc.getTimestamp("lastUpdatedAt") ?: Timestamp.now()
                                val diffMillis = System.currentTimeMillis() - lastUpdated.toDate().time
                                val diffMinutes = diffMillis / (1000 * 60)
                                val lastUpdatedLabel = when {
                                    diffMinutes < 1 -> "Just now"
                                    diffMinutes < 60 -> "${diffMinutes}m ago"
                                    diffMinutes < 1440 -> "${diffMinutes / 60}h ago"
                                    else -> "${diffMinutes / 1440}d ago"
                                }

                                MarketRateDomain(
                                    city = cityName,
                                    // Broiler
                                    todayPrice = cityDoc.getDouble("todayPrice") ?: 0.0,
                                    yesterdayPrice = cityDoc.getDouble("yesterdayPrice") ?: 0.0,
                                    // Eggs
                                    eggsPrice = cityDoc.getDouble("eggsPrice") ?: 0.0,
                                    eggsYesterday = cityDoc.getDouble("eggsYesterday") ?: 0.0,
                                    // Chicken
                                    chickenPrice = cityDoc.getDouble("chickenPrice") ?: 0.0,
                                    chickenYesterday = cityDoc.getDouble("chickenYesterday") ?: 0.0,
                                    
                                    lastUpdatedAtLabel = lastUpdatedLabel,
                                    trend = try {
                                        PriceTrend.valueOf(trendStr)
                                    } catch (e: Exception) {
                                        PriceTrend.STABLE
                                    },
                                    unit = cityDoc.getString("unit") ?: "/kg"
                                )
                            }
                            states.add(StateDomain(name = stateName, cities = cities))
                            pendingTasks--
                            if (pendingTasks == 0) trySend(states.toList())
                        }
                        .addOnFailureListener { 
                            pendingTasks--
                            if (pendingTasks == 0) trySend(states.toList())
                        }
                }
            }
        awaitClose { listener.remove() }
    }

    // ── COMPANY RATES / DYNAMIC ISLAND ───────────
    // City click pe trigger hota hai
    // Path: companyRates/{date}/{cityId}/details/updates
    // cityId = city name LOWERCASE (e.g. "indore")
    // Real-time listener → DynamicIslandContent ko data deta hai
    override fun getCompanyRatesForCity(
        dateString: String,
        cityId: String
    ): Flow<List<CompanyRateUpdate>> = callbackFlow {

        val cityIdLower = cityId.trim().lowercase().replace(" ", "_")

        Log.d("RatesRepo", "Fetching company rates: companyRates/$dateString/$cityIdLower/details/updates")

        val listener = firestore
            .collection("companyRates")
            .document(dateString)
            .collection(cityIdLower)
            .document("details")
            .collection("updates")
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("RatesRepo", "getCompanyRates error: ${error.message}")
                    trySend(emptyList()) 
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    Log.d("RatesRepo", "No company rates found for $cityIdLower")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val updates = snapshot.documents.mapNotNull { doc ->
                    try {
                        val updatedAt = doc.getTimestamp("updatedAt") 
                            ?: Timestamp.now()
                        val diffMillis = System.currentTimeMillis() - updatedAt.toDate().time
                        val diffMinutes = diffMillis / (1000 * 60)

                        val timestampLabel = when {
                            diffMinutes < 1    -> "Just now"
                            diffMinutes < 60   -> "${diffMinutes}m ago"
                            diffMinutes < 1440 -> "${diffMinutes / 60}h ago"
                            else               -> "${diffMinutes / 1440}d ago"
                        }

                        CompanyRateUpdate(
                            id = doc.id,
                            city = doc.getString("city") ?: cityId,
                            companyName = doc.getString("companyName") ?: "",
                            category = doc.getString("category") ?: "Broiler",
                            variety = doc.getString("variety") ?: "Standard",
                            rate = doc.getDouble("rate") ?: 0.0,
                            timestamp = timestampLabel,
                            isLive = doc.getBoolean("isLive") ?: (diffMinutes < 60)
                        )
                    } catch (e: Exception) {
                        Log.e("RatesRepo", "Map error: ${e.message}")
                        null
                    }
                }

                Log.d("RatesRepo", "Company rates loaded: ${updates.size} items")
                trySend(updates)
            }

        awaitClose { listener.remove() }
    }
}
