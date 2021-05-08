package ru.andrewkir.hse_mooc.flows.courses.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.common.SingleLiveEvent
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

    fun init(){
        getMainCourses()
        getCompilations()
    }

    fun getTrending(url: String){
        viewModelScope.launch {
            mutableLoading.value = true
            when(val result = mainCoursesRepository.getTrendingCourses(url)){
                is ApiResponse.OnSuccessResponse -> {
                    trendingCourses.value = result.value.courses
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            mutableLoading.value = false
        }
    }

    fun getMainCourses(){
        viewModelScope.launch {
            mutableLoading.value = true
            when(val result = mainCoursesRepository.getCoursesMain()){
                is ApiResponse.OnSuccessResponse -> {
                    trendingCourses.value = result.value.courses
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            mutableLoading.value = false
        }
    }

    fun getCompilations(){
        viewModelScope.launch {
            mutableLoading.value = true
            when(val result = mainCoursesRepository.getCompilations()){
                is ApiResponse.OnSuccessResponse -> {
                    compilations.value = result.value
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            mutableLoading.value = false
        }
    }

    //TODO изменить название методов
}