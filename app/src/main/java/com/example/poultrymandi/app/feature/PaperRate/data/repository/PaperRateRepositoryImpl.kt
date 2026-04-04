package com.example.poultrymandi.app.feature.PaperRate.data.repository

import android.util.Log
import com.example.poultrymandi.app.feature.PaperRate.domain.model.PaperRate
import com.example.poultrymandi.app.feature.PaperRate.domain.repository.PaperRateRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PaperRateRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : PaperRateRepository {

    companion object {
        private const val TABLE = "paper_rates"
        private const val TAG = "PaperRateRepo"
    }

    override fun getPaperRates(): Flow<List<PaperRate>> = flow {
        while (true) {
            try {
                val list = supabaseClient.postgrest
                    .from(TABLE)
                    .select {
                        order("created_at", Order.DESCENDING)
                    }
                    .decodeList<PaperRate>()

                Log.d(TAG, "Fetched: ${list.size} records")
                emit(list)

            } catch (e: Exception) {
                Log.e(TAG, "Fetch error: ${e.message}", e)
                emit(emptyList())
            }

            delay(30_000L) // 30 sec mein refresh
        }
    }.flowOn(Dispatchers.IO)
}