package com.persia.test.ui.panel.products.variant.list

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.persia.test.global.Constants.Companion.PAGE_SIZE
import com.persia.test.data.domain.models.Variant
import com.persia.test.data.network.PersiaAtlasApiClient
import timber.log.Timber
import kotlin.time.TimedValue


@ExperimentalPagingApi
class VariantPagingSource(
    private val api: PersiaAtlasApiClient
) : PagingSource<Int, Variant>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Variant> {
        val pageNumber = params.key ?: 1
        val previousKey = if (pageNumber == 1) null else pageNumber - 1

        val pageRequest = api.getVariantList(pageNumber = pageNumber, pageSize = PAGE_SIZE)
        pageRequest.exception?.let {
            Timber.e("ERROR! $it")
            return LoadResult.Error(it)
        }
        val nextPage = getPageIndexFromNext(pageRequest.body.next)

        Timber.i("*".repeat(100))
        Timber.i(" previousKey: $previousKey | pageNumber: $pageNumber | nextPage: $nextPage")
        return LoadResult.Page(
            data = pageRequest.body.items.map { variantResponse ->
                variantResponse.asDomainModel()
            },
            prevKey = previousKey,
            nextKey = nextPage
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Variant>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getPageIndexFromNext(next: String?): Int? {
        Timber.i("next: $next")
        val pageSection = "page=([0-9]+)".toRegex().find(next.toString())?.value
        Timber.i("page sec : $pageSection")
        if (pageSection != null) {
            val pageNumber = pageSection.split("page=")[1].toInt()
            Timber.i("number: $pageNumber")
            return pageNumber
        }
        return null
    }
}