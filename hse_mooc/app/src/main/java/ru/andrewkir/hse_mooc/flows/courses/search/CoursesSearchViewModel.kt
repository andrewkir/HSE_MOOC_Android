package ru.andrewkir.hse_mooc.flows.courses.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.Course


class CoursesSearchViewModel(
    private val searchRepository: CoursesSearchRepository
) : BaseViewModel(searchRepository) {

    private val mutableCourses = arrayListOf<Course>()
    private val mutableCoursesLiveData: MutableLiveData<List<Course>> = MutableLiveData()
    val coursesLiveData: LiveData<List<Course>>
        get() = mutableCoursesLiveData

    private val mutableError: MutableLiveData<ApiResponse.OnErrorResponse> = MutableLiveData()
    val errorLiveData: LiveData<ApiResponse.OnErrorResponse>
        get() = mutableError

    private val isLastPage: MutableLiveData<Boolean> = MutableLiveData()
    val isLastPageLiveData: LiveData<Boolean>
        get() = isLastPage

    init {
        isLastPage.value = true
    }

    private var page = 1
    private var query = ""

    private fun fetchCourses(query: String, currentPage: Int) {
        if (isLastPage.value!!) return

        mutableError.value = null
        viewModelScope.launch {
            when (val result = searchRepository.getCoursesFromServer(query, currentPage)) {
                is ApiResponse.OnSuccessResponse -> {
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
        }
    }

    fun initCourses(searchQuery: String) {
        if (searchQuery != query) {
            query = searchQuery
            refreshCourses()
        } else if (mutableCoursesLiveData.value == null) {
            refreshCourses()
        }
    }

    fun nextPage() {
        page++
        fetchCourses(query, page)
    }

    fun refreshCourses() {
        mutableError.value = null
        page = 1

        viewModelScope.launch {
            when (val result = searchRepository.getCoursesFromServer(query, 1)) {
                is ApiResponse.OnSuccessResponse -> {
                    mutableCourses.clear()
                    isLastPage.value = result.value.courses.isEmpty() || result.value.courses.size < 10
                    mutableCourses.addAll(result.value.courses)
                    mutableCoursesLiveData.value = mutableCourses
                }
                is ApiResponse.OnErrorResponse -> {
                    mutableError.value = result
                }
            }
        }
    }
}