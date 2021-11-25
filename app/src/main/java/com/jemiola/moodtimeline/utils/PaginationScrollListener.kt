package com.jemiola.moodtimeline.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val pageSize: Int = 10
) : RecyclerView.OnScrollListener() {

    private var pageIndex = pageSize + 1
    var isLastPage = false
    var isLoadingPage = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!isLoadingPage && !isLastPage) {
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems(pageIndex, pageSize)
                pageIndex += pageSize + 1
            }
        }
    }

    abstract fun loadMoreItems(pageIndex: Int, pageSize: Int)
}