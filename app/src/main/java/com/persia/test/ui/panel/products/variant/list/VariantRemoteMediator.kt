package com.persia.test.ui.panel.products.variant.list

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.persia.test.global.Constants.Companion.PAGE_SIZE
import com.persia.test.data.database.PersiaAtlasDatabase
import com.persia.test.data.database.entities.VariantEntity
import com.persia.test.data.database.entities.VariantPagingRemoteKeyEntity
import com.persia.test.data.network.PersiaAtlasApiClient
import timber.log.Timber


@ExperimentalPagingApi
class VariantRemoteMediator(
    private val api: PersiaAtlasApiClient,
    private val database: PersiaAtlasDatabase
) : RemoteMediator<Int, VariantEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VariantEntity>
    ): MediatorResult {
        // Timber.i("*".repeat(120))
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
            val response = api.getVariantList(pageNumber = currentPage, pageSize = PAGE_SIZE)
            val endOfPaginationReached = response.body.next == null

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            // Timber.i("loadType: $loadType | page size: ${state.pages.size}")
            // Timber.i("currentPage: $currentPage")
            // Timber.i("isEnd: $endOfPaginationReached | prevPage: $prevPage | nextPage: $nextPage")
            // Timber.i("res previous: ${response.body.previous} | res next: ${response.body.next}")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.variantDao().deleteAllVariants()
                    database.variantRemoteKeysDao().clearRemoteKeys()
                }
                val variants = response.body.items.map { variantResponse ->
                    variantResponse.asDomainModel()
                }
                val keys = variants.map { variant ->
                    VariantPagingRemoteKeyEntity(
                        variantId = variant.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                val variantsForDb: Array<VariantEntity> =
                    response.body.items.map { variantResponse ->
                        variantResponse.asDatabaseModel()
                    }.toTypedArray()
                database.variantDao().insertVariant(*variantsForDb)
                database.variantRemoteKeysDao().insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, VariantEntity>
    ): VariantPagingRemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.variantRemoteKeysDao().getRemoteKeysByVariantId(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, VariantEntity>
    ): VariantPagingRemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { variant ->
                database.variantRemoteKeysDao().getRemoteKeysByVariantId(variant.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, VariantEntity>
    ): VariantPagingRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { variant ->
                database.variantRemoteKeysDao().getRemoteKeysByVariantId(variant.id)
            }
    }
}