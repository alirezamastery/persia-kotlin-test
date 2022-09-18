package com.persia.test.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.persia.test.global.Constants.Companion.PAGE_SIZE
import com.persia.test.global.Constants.Companion.PREFETCH_DISTANCE
import com.persia.test.data.database.PersiaAtlasDatabase
import com.persia.test.data.domain.models.Variant
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.ui.panel.products.variant.list.VariantPagingSource
import com.persia.test.ui.panel.products.variant.list.VariantRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@ExperimentalPagingApi
class VariantRepository @Inject constructor(
    private val database: PersiaAtlasDatabase,
    private val api: PersiaAtlasApiClient
) {

    fun getAllVariants(): Flow<PagingData<Variant>> {
        // val pagingSourceFactory = { database.variantDao().getAllVariants() }
        val pagingSourceFactory = { VariantPagingSource(api) }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            remoteMediator = VariantRemoteMediator(api = api, database = database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}