package ru.andrewkir.hse_mooc.flows.courses.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.data.repositories.ProfileRepositoryImpl
import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview.CoursePreview

class ProfileViewModel(
    private val profileRepository: ProfileRepositoryImpl
) : BaseViewModel(profileRepository) {

    val favoritesCourses: MutableLiveData<List<CoursePreview>> by lazy {
        MutableLiveData<List<CoursePreview>>()
    }

    val viewedCourses: MutableLiveData<List<CoursePreview>> by lazy {
        MutableLiveData<List<CoursePreview>>()
    }

    fun getFavorites(){
        viewModelScope.launch {
            mutableLoading.value = true
            when(val result = profileRepository.getFavoritesCourses()){
                is ApiResponse.OnSuccessResponse -> {
                    favoritesCourses.value = result.value.favouriteCourses
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            mutableLoading.value = false
        }
    }

    fun getViewed(){
        viewModelScope.launch {
            mutableLoading.value = true
            when(val result = profileRepository.getViewedCourses()){
                is ApiResponse.OnSuccessResponse -> {
                    viewedCourses.value = result.value.viewedCourses
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
            mutableLoading.value = false
        }
    }
}