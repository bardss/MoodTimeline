package com.jemiola.moodtimeline.views.moods.timeline

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val pageSize: Int = 5
) :
    RecyclerView.OnScrollListener() {

    private var nextPage = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems(nextPage, pageSize)
                nextPage += pageSize
            }
        }
    }

    protected abstract fun loadMoreItems(nextPage: Int, pageSize: Int)
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean

}