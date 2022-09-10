package com.persia.test.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.persia.test.data.database.PersiaAtlasDatabase
import com.persia.test.data.database.entities.IncomeEntity
import com.persia.test.data.database.entities.asDomainModel
import com.persia.test.data.domain.models.Income
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.network.services.persiaatlas.responses.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class IncomeRepository @Inject constructor(
    private val database: PersiaAtlasDatabase,
    private val api: PersiaAtlasApiClient
) {

    val incomes: LiveData<List<Income>> = Transformations.map(
        database.providePersiaAtlasDao().getIncomes()
    ) {
        it.asDomainModel()
    }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [incomes]
     */
    suspend fun refreshIncomes() {
        withContext(Dispatchers.IO) {
            val response = api.getIncomes()
            Timber.i("income response: $response")
            if (response.isSuccessful) {
                val incomes = response.body.items
                val incomesForDatabase: Array<IncomeEntity> = incomes.map { income ->
                    income.asDatabaseModel()
                }.toTypedArray()
                database.providePersiaAtlasDao().insertIncomes(*incomesForDatabase)
            } else {
                Timber.e("could not get income list from server")
            }
        }
    }
}