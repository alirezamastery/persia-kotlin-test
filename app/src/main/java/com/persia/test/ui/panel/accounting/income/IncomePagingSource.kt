package com.persia.test.ui.panel.accounting.income

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.persia.test.Constants.Companion.PAGE_SIZE
import com.persia.test.data.domain.models.Income
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.data.network.services.persiaatlas.responses.asDomainModel
import com.persia.test.data.repository.IncomeRepository
import timber.log.Timber

@ExperimentalPagingApi
class IncomePagingSource(
    val repository: IncomeRepository,
    val api: PersiaAtlasApiClient
) : PagingSource<Int, Income>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Income> {
        val pageNumber = params.key ?: 1
        val previousKey = if (pageNumber == 1) null else pageNumber - 1

        val response = api.getIncomeList(pageIndex = pageNumber, pageSize = PAGE_SIZE)
        val incomeList = response.body.items.map { incomeResponse ->
            incomeResponse.asDomainModel()
        }

        Timber.i("income list in source: $incomeList")

        return LoadResult.Page(
            data = incomeList,
            prevKey = previousKey,
            nextKey = getPageIndexFromNext(response.body.next)
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Income>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Timber.i("refresh key")
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getPageIndexFromNext(next: String?): Int? {
        Timber.i("find next: $next")
        return next?.split("?page=")?.get(1)?.toInt()
    }

}