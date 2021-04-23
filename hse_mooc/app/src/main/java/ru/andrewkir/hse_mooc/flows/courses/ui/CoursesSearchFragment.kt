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
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesBinding
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesSearchBinding
import ru.andrewkir.hse_mooc.flows.courses.CoursesRepository
import ru.andrewkir.hse_mooc.flows.courses.CoursesViewModel
import ru.andrewkir.hse_mooc.flows.courses.ui.adapters.SearchCoursesRecyclerAdapter
import ru.andrewkir.hse_mooc.network.api.CoursesApi

class CoursesSearchFragment :
    BaseFragment<CoursesViewModel, CoursesRepository, FragmentCoursesSearchBinding>() {

    private lateinit var recyclerAdapter: SearchCoursesRecyclerAdapter


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

        recyclerAdapter = SearchCoursesRecyclerAdapter(requireContext()) {
            Toast.makeText(requireContext(), it.courseName, Toast.LENGTH_SHORT).show()
        }

        subscribeToCourses()

        bind.searchCoursesRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }

        viewModel.fetchCourses()

        bind.swipeRefresh.run {
            setOnRefreshListener {
                viewModel.fetchCourses()
            }
        }
    }

    private fun fillList(): List<String> {
        val data = mutableListOf<String>()
        (0..30).forEach { i -> data.add("$i element") }
        return data
    }

    private fun subscribeToCourses() {
        viewModel.coursesLiveData.observe(viewLifecycleOwner, Observer {
            bind.swipeRefresh.isRefreshing = false
            recyclerAdapter.data = it.courses
        })
    }
}