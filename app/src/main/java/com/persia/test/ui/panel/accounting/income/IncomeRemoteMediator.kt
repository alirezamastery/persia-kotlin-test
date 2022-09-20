package com.persia.test.ui.panel.accounting.income

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.persia.test.global.Constants.Companion.PAGE_SIZE
import com.persia.test.data.database.PersiaAtlasDatabase
import com.persia.test.data.database.entities.IncomeEntity
import com.persia.test.data.database.entities.IncomeRemoteKey
import com.persia.test.domain.models.Income
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.network.services.persiaatlas.responses.asDatabaseModel
import com.persia.test.data.network.services.persiaatlas.responses.asDomainModel
import timber.log.Timber


@ExperimentalPagingApi
class IncomeRemoteMediator(
    private val api: PersiaAtlasApiClient,
    private val database: PersiaAtlasDatabase
) : RemoteMediator<Int, Income>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Income>): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = api.getIncomeList(pageIndex = currentPage, pageSize = PAGE_SIZE)
            Timber.i("mediator | currentPage: $currentPage | body: ${response.body}")
            val endOfPaginationReached = response.body.next == null

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.providePersiaAtlasDao().deleteAllIncomes()
                    database.provideIncomeRemoteKeysDao().clearRemoteKeys()
                }
                val incomes = response.body.items.map { incomeResponse ->
                    incomeResponse.asDomainModel()
                }
                val keys = incomes.map { income ->
                    IncomeRemoteKey(
                        incomeId = income.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                val incomesForDb: Array<IncomeEntity> = response.body.items.map { incomeResponse ->
                    incomeResponse.asDatabaseModel()
                }.toTypedArray()
                database.providePersiaAtlasDao().insertIncome(*incomesForDb)
                database.provideIncomeRemoteKeysDao().insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Income>
    ): IncomeRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.provideIncomeRemoteKeysDao().getRemoteKeysByIncomeId(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Income>
    ): IncomeRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { income ->
                database.provideIncomeRemoteKeysDao().getRemoteKeysByIncomeId(income.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Income>
    ): IncomeRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { income ->
                database.provideIncomeRemoteKeysDao().getRemoteKeysByIncomeId(income.id)
            }
    }
}