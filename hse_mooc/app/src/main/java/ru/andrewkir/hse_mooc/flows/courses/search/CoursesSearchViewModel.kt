package ru.andrewkir.hse_mooc.flows.courses.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.data.repositories.CoursesSearchRepositoryImpl
import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Categories.CategoriesResponse
import ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview.CoursePreview


class CoursesSearchViewModel(
    private val searchRepository: CoursesSearchRepositoryImpl
) : BaseViewModel(searchRepository) {

    private val mutableCourses = arrayListOf<CoursePreview>()
    private val mutableCoursesLiveData: MutableLiveData<List<CoursePreview>> = MutableLiveData()
    val coursesLiveData: LiveData<List<CoursePreview>>
        get() = mutableCoursesLiveData

    val error: MutableLiveData<ApiResponse.OnErrorResponse?> by lazy {
        MutableLiveData<ApiResponse.OnErrorResponse?>()
    }

    val isLastPage: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val categoryResponse: MutableLiveData<ApiResponse<CategoriesResponse>> = MutableLiveData()

    init {
        isLastPage.value = true
    }

    private var page = 1
    private var query = ""
    private var isFirstInit = true
    var categories = mutableSetOf<Int>()

    private fun fetchCourses(query: String, currentPage: Int) {
        if (isLastPage.value!!) return

        error.value = null
        viewModelScope.launch {
            mutableLoading.value = true
            when (val result = searchRepository.getCoursesFromServer(
                query,
                currentPage,
                categories.joinToString(",")
            )) {
                is ApiResponse.OnSuccessResponse -> {
                    error.value = null

                    if (result.value.courses.isEmpty()) {
                        page--
                        isLastPage.value = true
                    }
                    mutableCourses.addAll(result.value.courses)
                    mutableCoursesLiveData.value = mutableCourses
                }
                is ApiResponse.OnErrorResponse -> {
                    page--
                    error.value = result
                }
            }
            mutableLoading.value = false
        }
    }

    fun initCourses() {
        if (isFirstInit) {
            isFirstInit = false
            refreshCourses()
            getCategories()
        }
    }

    fun searchCourses(searchQuery: String, categoriesQuery: Set<Int> = setOf()) {
        if (searchQuery != query || !(categoriesQuery.containsAll(categories) && categories.containsAll(
                categoriesQuery
            ))
        ) {
            query = searchQuery
            categories = categoriesQuery.toMutableSet()

            refreshCourses()
        } else {
            refreshCourses()
        }
    }

    fun nextPage() {
        page++
        fetchCourses(query, page)
    }

    fun refreshCourses(categoriesQuery: Set<Int>? = null) {
        if (categoriesQuery != null) categories = categoriesQuery.toMutableSet()

        error.value = null
        page = 1

        viewModelScope.launch {
            mutableLoading.value = true
            when (val result =
                searchRepository.getCoursesFromServer(query, 1, categories.joinToString(","))) {
                is ApiResponse.OnSuccessResponse -> {
                    mutableCourses.clear()
                    isLastPage.value =
                        result.value.courses.isEmpty() || result.value.courses.size < 30
                    mutableCourses.addAll(result.value.courses)
                    mutableCoursesLiveData.value = mutableCourses
                }
                is ApiResponse.OnErrorResponse -> {
                    error.value = result
                }
            }
            mutableLoading.value = false
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            categoryResponse.value = searchRepository.getCategories()
        }
    }
}