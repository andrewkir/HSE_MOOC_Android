package ru.andrewkir.hse_mooc.flows.courses.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesMainBinding
import ru.andrewkir.hse_mooc.flows.course.CourseActivity
import ru.andrewkir.hse_mooc.flows.courses.main.adapters.MainCoursesAdapter
import ru.andrewkir.hse_mooc.flows.courses.main.adapters.TrendingCoursesButtonAdapter
import ru.andrewkir.hse_mooc.flows.courses.main.model.TrendingButton
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.CoursesPreview.CoursePreview

class CoursesMainFragment :
    BaseFragment<CoursesMainViewModel, CoursesMainRepository, FragmentCoursesMainBinding>() {

    lateinit var horizontalLinearLayoutManager: LinearLayoutManager
    lateinit var trendingButtonsAdapter: TrendingCoursesButtonAdapter

    lateinit var coursesLinearLayoutManager: LinearLayoutManager
    lateinit var coursesAdapter: MainCoursesAdapter

    override fun provideViewModelClass() = CoursesMainViewModel::class.java

    override fun provideRepository(): CoursesMainRepository {
        return CoursesMainRepository(
            apiProvider.provideApi(
                CoursesApi::class.java,
                requireContext(),
                userPrefsManager.accessToken
            )
        )
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCoursesMainBinding = FragmentCoursesMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTrendingRecycler()
        setupCoursesRecycler()
        setupRefresh()
        subscribeToCourses()
        subscribeToLoading()

        fillData()
    }

    private fun setupTrendingRecycler() {
        trendingButtonsAdapter = TrendingCoursesButtonAdapter(requireContext()) {
            if (bind.featuredCoursesTextView.text != it.name) {
                bind.mainCoursesRecycler.visibility = View.GONE
                viewModel.getTrending(it.link)
                bind.featuredCoursesTextView.text = it.name
            }
        }
        horizontalLinearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        bind.trendindCoursesButtonsRecycler.run {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = horizontalLinearLayoutManager
            adapter = trendingButtonsAdapter
        }
    }

    private fun setupCoursesRecycler() {
        coursesAdapter = MainCoursesAdapter(requireContext()) {
            val intent = Intent(requireContext(), CourseActivity::class.java)
            intent.putExtra("COURSE_ID", it.id)
            startActivity(intent)
        }
        coursesLinearLayoutManager =
            LinearLayoutManager(requireContext())

        bind.mainCoursesRecycler.run {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = coursesLinearLayoutManager
            adapter = coursesAdapter
        }
    }

    private fun setupRefresh() {
        bind.mainSwipeRefresh.setOnRefreshListener {
            //TODO снова получаем рекомендуемые курсы
            coursesAdapter.data = ArrayList()
            bind.featuredCoursesTextView.text = "Рекомендуемые курсы"
            bind.mainSwipeRefresh.isRefreshing = false
        }
    }

    private fun fillData() {
        val data = ArrayList<TrendingButton>()
        for (i in 1..10) {
            data.add(
                TrendingButton(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Kotlin_Icon.svg/768px-Kotlin_Icon.svg.png",
                    "Button $i",
                    "https://api.mooc.ij.je/courses/?pageNumber=$i&pageSize=10"
                )
            )
        }
        trendingButtonsAdapter.data = data
        bind.trendindCoursesButtonsRecycler.visibility = View.VISIBLE
    }

    private fun subscribeToCourses() {
        viewModel.trendingCourses.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                coursesAdapter.data = it
                bind.mainCoursesRecycler.visibility = View.VISIBLE
            }
        })
    }

    private fun subscribeToLoading() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            //bind.mainSwipeRefresh.isRefreshing = it
        })
    }
}