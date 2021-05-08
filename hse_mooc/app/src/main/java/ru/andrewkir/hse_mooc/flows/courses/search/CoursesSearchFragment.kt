package ru.andrewkir.hse_mooc.flows.courses.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.handleApiError
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesSearchBinding
import ru.andrewkir.hse_mooc.flows.course.CourseActivity
import ru.andrewkir.hse_mooc.flows.courses.search.adapters.SearchCoursesRecyclerAdapter
import ru.andrewkir.hse_mooc.flows.courses.search.adapters.SearchScrollListener
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse


class CoursesSearchFragment :
    BaseFragment<CoursesSearchViewModel, CoursesSearchRepository, FragmentCoursesSearchBinding>() {

    private lateinit var recyclerAdapter: SearchCoursesRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var isLoading = true
    private var query = ""
    private var lastPage = true

    private var checkedChipsIds = mutableSetOf<Int>()

    override fun provideViewModelClass() = CoursesSearchViewModel::class.java

    override fun provideRepository(): CoursesSearchRepository {
        return CoursesSearchRepository(
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
    ): FragmentCoursesSearchBinding =
        FragmentCoursesSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        subscribeToCourses()
        subscribeToError()
        subscribeToLastPage()
        subscribeToCategories()
        subscribeToLoading()

        viewModel.initCourses()
    }

    private fun subscribeToCourses() {
        viewModel.coursesLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                bind.cantFindImage.visibility = View.VISIBLE
                bind.cantFindText.visibility = View.VISIBLE

                bind.searchCoursesRecyclerView.visibility = View.GONE
            } else {
                bind.cantFindImage.visibility = View.GONE
                bind.cantFindText.visibility = View.GONE

                bind.searchCoursesRecyclerView.visibility = View.VISIBLE
            }
            recyclerAdapter.data = it
            isLoading = false
        })
    }

    private fun subscribeToError() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                handleApiError(it) {
                    if (bind.swipeRefresh.isRefreshing) viewModel.refreshCourses(checkedChipsIds)
                    if (isLoading) viewModel.nextPage()
                    viewModel.searchCourses(query)
                }
            }
        })
    }

    private fun subscribeToLastPage() {
        viewModel.isLastPage.observe(viewLifecycleOwner, Observer {
            lastPage = if (it) {
                recyclerAdapter.removeLoading()
                true
            } else {
                recyclerAdapter.addLoading()
                false
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerAdapter = SearchCoursesRecyclerAdapter(requireContext()) {
            val intent = Intent(requireContext(), CourseActivity::class.java)
            intent.putExtra("COURSE_ID", it.id)
            startActivity(intent)
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
                        return lastPage
                    }

                    override fun isLoading(): Boolean {
                        return isLoading
                    }
                }
            )
        }
    }

    private fun setupListeners() {
        bind.swipeRefresh.run {
            setOnRefreshListener {
                viewModel.refreshCourses(checkedChipsIds)
                if (bind.chipGroup.childCount <= 1) viewModel.getCategories()
            }
        }

        bind.searchButton.setOnClickListener {
            performSearch()
            if (bind.chipGroup.visibility == View.VISIBLE) bind.chipGroup.visibility = View.GONE
        }

        bind.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (bind.chipGroup.visibility == View.VISIBLE) bind.chipGroup.visibility = View.GONE
                performSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        bind.expandButton.setOnClickListener {
            if (!bind.chipGroup.isShown) {
                bind.chipGroup.visibility = View.VISIBLE
                bind.filterCover.visibility = View.VISIBLE
            } else {
                bind.chipGroup.visibility = View.GONE
                bind.filterCover.visibility = View.GONE
            }
        }

        bind.filterCover.setOnClickListener {
            if (bind.chipGroup.isShown) bind.chipGroup.visibility = View.GONE
            bind.filterCover.visibility = View.GONE
        }
    }

    private fun performSearch() {
        bind.searchEditText.clearFocus()
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(bind.searchEditText.windowToken, 0)

        linearLayoutManager.smoothScrollToPosition(bind.searchCoursesRecyclerView, null, 0)

        query = bind.searchEditText.text.toString()
        viewModel.searchCourses(query, checkedChipsIds)
    }

    private fun subscribeToCategories() {
        viewModel.categoryResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiResponse.OnSuccessResponse -> {
                    bind.expandButton.visibility = View.VISIBLE

                    if (viewModel.categories.isNotEmpty()) checkedChipsIds =
                        viewModel.categories.toMutableSet()

                    bind.chipGroup.removeAllViews()
                    val removeAllChip = Chip(requireContext())
                    removeAllChip.id = View.generateViewId()
                    removeAllChip.text = "Сбросить"
                    removeAllChip.setOnClickListener {
                        bind.chipGroup.clearCheck()
                        checkedChipsIds.clear()
                    }
                    bind.chipGroup.addView(removeAllChip)

                    for (category in it.value) {
                        val chip = Chip(requireContext())
                        chip.id = View.generateViewId()
                        val drawable = ChipDrawable.createFromAttributes(
                            requireContext(),
                            null,
                            0,
                            R.style.CustomChipChoice
                        )
                        chip.setChipDrawable(drawable)
                        chip.text = category.name.ru
                        chip.setOnClickListener {
                            if (chip.isChecked) checkedChipsIds.add(category.id)
                            else checkedChipsIds.remove(category.id)
                        }
                        if (checkedChipsIds.contains(category.id)) chip.isChecked = true
                        bind.chipGroup.addView(chip)
                    }
                }
                else -> {
                    bind.expandButton.visibility = View.INVISIBLE
                    bind.chipGroup.removeAllViews()
                }
            }
        })
    }

    private fun subscribeToLoading() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            bind.swipeRefresh.isRefreshing = it
        })
    }
}