package ru.andrewkir.hse_mooc.flows.courses.search.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class SearchScrollListener(private var layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {
    private val PAGE_SIZE = 10

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        layoutManager.apply {
            val visibleItemCount = childCount
            val totalItemCount = itemCount
            val firstVisibleItemPosition = findFirstVisibleItemPosition()
            if (!isLoading() && !isLastPage()) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                    loadMoreItems()
                }
            }
        }
    }
    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}