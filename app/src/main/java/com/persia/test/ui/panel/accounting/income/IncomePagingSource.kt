package com.persia.test.ui.panel.accounting.income

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.persia.test.data.domain.models.Income
import com.persia.test.data.network.PersiaAtlasApiClient
import javax.inject.Inject

class IncomePagingSource : PagingSource<Int, Income>() {

    @Inject
    lateinit var api:PersiaAtlasApiClient

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Income> {
        TODO("Not yet implemented")
    }

    override fun getRefreshKey(state: PagingState<Int, Income>): Int? {
        TODO("Not yet implemented")
    }

}