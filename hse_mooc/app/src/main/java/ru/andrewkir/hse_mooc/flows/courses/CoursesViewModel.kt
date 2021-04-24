package ru.andrewkir.hse_mooc.flows.courses

import android.util.Log
import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.Course
import ru.andrewkir.hse_mooc.network.responses.CoursesResponse


class CoursesViewModel(
    private val repository: CoursesRepository
) : BaseViewModel(repository) {

    private val mutableCourses = arrayListOf<Course>()
    private val mutableCoursesLiveData: MutableLiveData<List<Course>> = MutableLiveData()
    val coursesLiveData: LiveData<List<Course>>
        get() = mutableCoursesLiveData

    private val mutableError: MutableLiveData<ApiResponse.OnErrorResponse> = MutableLiveData()
    val errorLiveData: LiveData<ApiResponse.OnErrorResponse>
        get() = mutableError

    private var page = 1


    private fun fetchCourses(currentPage: Int) {
        mutableError.value = null
        viewModelScope.launch {
            when (val result = repository.getCoursesFromServer(currentPage)) {
                is ApiResponse.OnSuccessResponse -> {
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

    fun init() {
        if (mutableCoursesLiveData.value == null) {
            fetchCourses(1)
        }
    }

    fun nextPage() {
        page++
        fetchCourses(page)
    }

    fun refreshCourses() {
        mutableError.value = null
        viewModelScope.launch {
            when (val result = repository.getCoursesFromServer(1)) {
                is ApiResponse.OnSuccessResponse -> {
                    mutableCourses.clear()
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