package ru.andrewkir.hse_mooc.flows.courses.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.handleApiError
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesSearchBinding
import ru.andrewkir.hse_mooc.flows.courses.search.adapters.SearchCoursesRecyclerAdapter
import ru.andrewkir.hse_mooc.flows.courses.search.adapters.SearchScrollListener
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse


class CoursesSearchFragment :
    BaseFragment<CoursesSearchViewModel, CoursesSearchRepository, FragmentCoursesSearchBinding>() {

    private lateinit var recyclerAdapter: SearchCoursesRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var currentPage = 1
    private var isLoading = true
    private var query = ""

    override fun provideViewModelClass() = CoursesSearchViewModel::class.java

    override fun provideRepository(): CoursesSearchRepository {
        return CoursesSearchRepository(
            apiProvider.provideApi(
                CoursesApi::class.java,
                requireContext(),
                userPrefsManager.obtainAccessToken()
            )
        )
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCoursesSearchBinding =
        FragmentCoursesSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        subscribeToCourses()
        subscribeToError()
        subscribeToLastPage()
        subscribeToCategories()

        if (viewModel.coursesLiveData.value.isNullOrEmpty()) bind.swipeRefresh.isRefreshing = true
        viewModel.initCourses()
        viewModel.getCategories()

        bind.swipeRefresh.run {
            setOnRefreshListener {
                viewModel.refreshCourses()
            }
        }

        bind.searchButton.setOnClickListener {
            linearLayoutManager.smoothScrollToPosition(bind.searchCoursesRecyclerView, null, 0)
            query = bind.searchEditText.text.toString()
            viewModel.initCourses(query)
        }

        bind.expandButton.setOnClickListener {
            if (bind.chipGroup.visibility == View.GONE) bind.chipGroup.visibility = View.VISIBLE
            else bind.chipGroup.visibility = View.GONE
        }
    }

    private fun subscribeToCourses() {
        viewModel.coursesLiveData.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.data = it
            bind.swipeRefresh.isRefreshing = false
            isLoading = false
        })
    }

    private fun subscribeToError() {
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                handleApiError(it) {
                    if (bind.swipeRefresh.isRefreshing) viewModel.refreshCourses()
                    if (isLoading) viewModel.nextPage()
                    viewModel.initCourses(query)
                }
            }
        })
    }

    private fun subscribeToLastPage() {
        viewModel.isLastPageLiveData.observe(viewLifecycleOwner, Observer {
            if (it) recyclerAdapter.removeLoading()
            else recyclerAdapter.addLoading()
        })
    }

    private fun setupRecyclerView() {
        recyclerAdapter = SearchCoursesRecyclerAdapter(requireContext()) {
            Toast.makeText(requireContext(), it.courseName, Toast.LENGTH_SHORT).show()
        }
        linearLayoutManager = LinearLayoutManager(requireContext())

        bind.searchCoursesRecyclerView.run {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = linearLayoutManager
            adapter = recyclerAdapter
            addOnScrollListener(
                object : SearchScrollListener(linearLayoutManager) {
                    override fun loadMoreItems() {
                        isLoading = true
                        viewModel.nextPage()
                    }

                    override fun isLastPage(): Boolean {
                        return false
                    }

                    override fun isLoading(): Boolean {
                        return isLoading
                    }
                }
            )
        }
    }

    private fun subscribeToCategories() {
        viewModel.categoryResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiResponse.OnSuccessResponse -> {
                    for (i in it.value) {
                        val chip = Chip(requireContext())
                        val drawable = ChipDrawable.createFromAttributes(
                            requireContext(),
                            null,
                            0,
                            R.style.CustomChipChoice
                        )
                        chip.setChipDrawable(drawable)
                        chip.text = i.name.ru
                        chip.setOnClickListener {
                            Toast.makeText(requireContext(), i.name.ru, Toast.LENGTH_SHORT).show()
                        }
                        bind.chipGroup.addView(chip)
                    }
                }
            }
        })
    }
}