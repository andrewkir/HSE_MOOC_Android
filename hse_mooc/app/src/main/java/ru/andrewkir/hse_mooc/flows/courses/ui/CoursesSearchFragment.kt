package ru.andrewkir.hse_mooc.flows.courses.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.handleApiError
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesBinding
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesSearchBinding
import ru.andrewkir.hse_mooc.flows.courses.CoursesRepository
import ru.andrewkir.hse_mooc.flows.courses.CoursesViewModel
import ru.andrewkir.hse_mooc.flows.courses.ui.adapters.SearchCoursesRecyclerAdapter
import ru.andrewkir.hse_mooc.flows.courses.ui.adapters.SearchScrollListener
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.Course

class CoursesSearchFragment :
    BaseFragment<CoursesViewModel, CoursesRepository, FragmentCoursesSearchBinding>() {

    private lateinit var recyclerAdapter: SearchCoursesRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var currentPage = 1
    private var isLoading = true

    override fun provideViewModelClass() = CoursesViewModel::class.java

    override fun provideRepository(): CoursesRepository {
        return CoursesRepository(
            apiProvider.provideApi(
                CoursesApi::class.java,
                requireContext(),
                userPrefsManager.obtainAccessToken(),
                userPrefsManager.obtainRefreshToken()
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

        bind.swipeRefresh.isRefreshing = true

        viewModel.init()

        recyclerAdapter = SearchCoursesRecyclerAdapter(requireContext()) {
            Toast.makeText(requireContext(), it.courseName, Toast.LENGTH_SHORT).show()
        }
        linearLayoutManager = LinearLayoutManager(requireContext())

        subscribeToCourses()
        subscribeToError()

        bind.searchCoursesRecyclerView.run {
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

        bind.swipeRefresh.run {
            setOnRefreshListener {
                viewModel.refreshCourses()
            }
        }
    }

    private fun subscribeToCourses() {
        viewModel.coursesLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) recyclerAdapter.removeLoading()
            recyclerAdapter.addLoading()
            recyclerAdapter.data = it
            bind.swipeRefresh.isRefreshing = false
            isLoading = false
        })
    }

    private fun subscribeToError() {
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                handleApiError(it) {
                    if(bind.swipeRefresh.isRefreshing) viewModel.refreshCourses()
                    if(isLoading) viewModel.nextPage()
                    viewModel.init()
                }
            }
        })
    }
}