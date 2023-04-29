package ru.andrewkir.hse_mooc.flows.courses.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.adapters.CoursePreviewRecyclerViewAdapter
import ru.andrewkir.hse_mooc.common.handleApiError
import ru.andrewkir.hse_mooc.data.repositories.CoursesMainRepositoryImpl
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesMainBinding
import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.flows.course.CourseActivity
import ru.andrewkir.hse_mooc.flows.courses.main.adapters.TrendingCoursesButtonAdapter

class CoursesMainFragment :
    BaseFragment<CoursesMainViewModel, CoursesMainRepositoryImpl, FragmentCoursesMainBinding>() {

    lateinit var horizontalLinearLayoutManager: LinearLayoutManager
    lateinit var trendingButtonsAdapter: TrendingCoursesButtonAdapter

    lateinit var coursesLinearLayoutManager: LinearLayoutManager
    lateinit var coursesAdapter: CoursePreviewRecyclerViewAdapter

    override fun provideViewModelClass() = CoursesMainViewModel::class.java

    override fun provideRepository(): CoursesMainRepositoryImpl {
        return CoursesMainRepositoryImpl(
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("MAIN_TRENDING_TITLE", featuredCoursesTitle)
    }

    private var featuredCoursesTitle = "Рекомендуемые курсы"

    private fun restoreSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            bind.featuredCoursesTextView.text = savedInstanceState.getString(
                "MAIN_TRENDING_TITLE",
                "Рекомендуемые курсы"
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreSavedState(savedInstanceState)
        bind.mainSwipeRefresh.isRefreshing = true

        setupContentText()
        setupTrendingRecycler()
        setupCoursesRecycler()
        setupRefresh()
        subscribeToCourses()
        subscribeToLoading()
        subscribeToError()
        subscribeToCompilations()

        if (viewModel.compilations.value.isNullOrEmpty() || viewModel.trendingCourses.value.isNullOrEmpty()) {
            viewModel.init()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupContentText() {
        bind.mainTitleTextView.text = "Добрый день, ${userPrefsManager.username}!"
    }

    private fun setupTrendingRecycler() {
        trendingButtonsAdapter = TrendingCoursesButtonAdapter(requireContext()) {
            if (bind.featuredCoursesTextView.text != it.name.ru) {
                bind.mainCoursesRecycler.visibility = View.GONE
                viewModel.obtainTrendingCourses(it.link)
                bind.featuredCoursesTextView.text = it.name.ru
                featuredCoursesTitle = it.name.ru
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
        coursesAdapter = CoursePreviewRecyclerViewAdapter(requireContext()) {
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
            bind.mainCoursesRecycler.visibility = View.GONE
            viewModel.obtainMainCourses()
            bind.featuredCoursesTextView.text = getString(R.string.profile_featured_courses_text)
        }
    }

    private fun subscribeToCourses() {
        viewModel.trendingCourses.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                coursesAdapter.data = it
                bind.mainCoursesRecycler.visibility = View.VISIBLE
            }
        })
    }

    private fun subscribeToLoading() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            bind.mainSwipeRefresh.isRefreshing = it
        })
    }

    private fun subscribeToError() {
        viewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            handleApiError(it) {
                viewModel.init()
            }
        })
    }

    private fun subscribeToCompilations() {
        viewModel.compilations.observe(viewLifecycleOwner, Observer {
            trendingButtonsAdapter.data = it
            bind.trendindCoursesButtonsRecycler.visibility = View.VISIBLE
        })
    }
}