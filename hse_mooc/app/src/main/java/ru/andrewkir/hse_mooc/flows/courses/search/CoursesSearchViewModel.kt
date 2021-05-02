package ru.andrewkir.hse_mooc.flows.courses.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.Categories.CategoriesResponse
import ru.andrewkir.hse_mooc.network.responses.CoursesPreview.CoursePreview


class CoursesSearchViewModel(
    private val searchRepository: CoursesSearchRepository
) : BaseViewModel(searchRepository) {

    private val mutableCourses = arrayListOf<CoursePreview>()
    private val mutableCoursesLiveData: MutableLiveData<List<CoursePreview>> = MutableLiveData()
    val coursesLiveData: LiveData<List<CoursePreview>>
        get() = mutableCoursesLiveData

    private val mutableError: MutableLiveData<ApiResponse.OnErrorResponse> = MutableLiveData()

    val errorLiveData: LiveData<ApiResponse.OnErrorResponse?>
        get() = mutableError

    private val isLastPage: MutableLiveData<Boolean> = MutableLiveData()
    val isLastPageLiveData: LiveData<Boolean>
        get() = isLastPage

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

        mutableError.value = null
        viewModelScope.launch {
            mutableLoading.value = true
            when (val result = searchRepository.getCoursesFromServer(
                query,
                currentPage,
                categories.joinToString(",")
            )) {
                is ApiResponse.OnSuccessResponse -> {
                    mutableError.value = null

                    if (result.value.courses.isEmpty()) {
                        page--
                        isLastPage.value = true
                    }
                    mutableCourses.addAll(result.value.courses)
                    mutableCoursesLiveData.value = mutableCourses
                }
                is ApiResponse.OnErrorResponse -> {
                    page--
                    mutableError.value = result
                }
            }
            mutableLoading.value = false
        }
    }

    fun initCourses() {
        if (isFirstInit) {
            isFirstInit = false
            refreshCourses()
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
        } else if (mutableCoursesLiveData.value == null) {
            refreshCourses()
        }
    }

    fun nextPage() {
        page++
        fetchCourses(query, page)
    }

    fun refreshCourses(categoriesQuery: Set<Int>? = null) {
        if (categoriesQuery != null) categories = categoriesQuery.toMutableSet()

        mutableError.value = null
        page = 1

        viewModelScope.launch {
            mutableLoading.value = true
            when (val result =
                searchRepository.getCoursesFromServer(query, 1, categories.joinToString(","))) {
                is ApiResponse.OnSuccessResponse -> {
                    mutableError.value = null
                    mutableCourses.clear()
                    isLastPage.value =
                        result.value.courses.isEmpty() || result.value.courses.size < 30
                    mutableCourses.addAll(result.value.courses)
                    mutableCoursesLiveData.value = mutableCourses
                }
                is ApiResponse.OnErrorResponse -> {
                    mutableError.value = result
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