package com.example.poultrymandi.app.feature.PaperRate.domain.repository

import com.example.poultrymandi.app.feature.PaperRate.domain.model.PaperRate
import kotlinx.coroutines.flow.Flow

/**
 * Interface for Paper Rate repository.
 * Provides read-only access to rate card images.
 */
interface PaperRateRepository {
    /**
     * Fetches real-time stream of paper rates from Firestore.
     */
    fun getPaperRates(): Flow<List<PaperRate>>
}