package com.ninjafarm.poultrymandi.app.feature.home.domain.repository

import com.ninjafarm.poultrymandi.app.feature.home.data.model.DataItem
import com.ninjafarm.poultrymandi.app.feature.home.domain.data.CompanyRateUpdate
import com.ninjafarm.poultrymandi.app.feature.home.domain.data.StateDomain
import kotlinx.coroutines.flow.Flow

interface RatesRepository {
    fun getRatesForDate(dateString: String): Flow<List<StateDomain>>
    fun getLast7Days(): List<DataItem>
    fun getCompanyRatesForCity(dateString: String, cityId: String): Flow<List<CompanyRateUpdate>>
}
