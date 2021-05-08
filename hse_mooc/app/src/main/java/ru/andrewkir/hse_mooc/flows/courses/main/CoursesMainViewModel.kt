package ru.andrewkir.hse_mooc.flows.courses.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.Compilations.CompilationsResponse
import ru.andrewkir.hse_mooc.network.responses.CoursesPreview.CoursePreview


class CoursesMainViewModel(
    private val mainCoursesRepository: CoursesMainRepository
) : BaseViewModel(mainCoursesRepository) {

    val trendingCourses: MutableLiveData<List<CoursePreview>> by lazy {
        MutableLiveData<List<CoursePreview>>()
    }

    val compilations: MutableLiveData<CompilationsResponse> by lazy {
        MutableLiveData<CompilationsResponse>()
    }

    fun init() {
        mutableLoading.value = true
        obtainMainCourses(false)
        obtainCompilations(false)
        mutableLoading.value = false
    }

    fun obtainTrendingCourses(url: String, displayLoading: Boolean = true) {
        viewModelScope.launch {
            if (displayLoading) mutableLoading.value = true
            when (val result = mainCoursesRepository.getTrendingCourses(url)) {
                is ApiResponse.OnSuccessResponse -> {
                    trendingCourses.value = result.value.courses
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            if (displayLoading) mutableLoading.value = false
        }
    }

    fun obtainMainCourses(changeLoading: Boolean = true) {
        viewModelScope.launch {
            if (changeLoading) mutableLoading.value = true
            when (val result = mainCoursesRepository.getCoursesMain()) {
                is ApiResponse.OnSuccessResponse -> {
                    trendingCourses.value = result.value.courses
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            if (changeLoading) mutableLoading.value = false
        }
    }

    private fun obtainCompilations(changeLoading: Boolean = true) {
        viewModelScope.launch {
            if (changeLoading) mutableLoading.value = true
            when (val result = mainCoursesRepository.getCompilations()) {
                is ApiResponse.OnSuccessResponse -> {
                    compilations.value = result.value
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            if (changeLoading) mutableLoading.value = false
        }
    }
}