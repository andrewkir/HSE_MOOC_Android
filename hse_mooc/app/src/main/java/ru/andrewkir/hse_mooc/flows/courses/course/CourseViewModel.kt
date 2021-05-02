package ru.andrewkir.hse_mooc.flows.courses.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.Course.Course
import ru.andrewkir.hse_mooc.network.responses.Course.CourseResponse

public class CourseViewModel(
    private val courseRepository: CourseRepository
) : BaseViewModel(courseRepository) {

    val courseResponse: MutableLiveData<ApiResponse<CourseResponse>> by lazy {
        MutableLiveData<ApiResponse<CourseResponse>>()
    }

    val isViewed: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLiked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        isLiked.value = false
        isViewed.value = false
    }

    fun toggleLike(id: String) {
        viewModelScope.launch {
            if (isLiked.value != null) {
                when (val result = courseRepository.updateFavorites(id, !isLiked.value!!)) {
                    is ApiResponse.OnSuccessResponse -> {
                        isLiked.value = !isLiked.value!!
                    }
                    is ApiResponse.OnErrorResponse -> {
                        //TODO
                    }
                }
            }
        }
    }

    fun toggleViewed(id: String) {
        viewModelScope.launch {
            if (isViewed.value != null) {
                when (val result = courseRepository.updateViewed(id, !isViewed.value!!)) {
                    is ApiResponse.OnSuccessResponse -> {
                        isViewed.value = !isViewed.value!!
                    }
                    is ApiResponse.OnErrorResponse -> {
                        //TODO
                    }
                }
            }
        }
    }

    fun init(id: String) {
        viewModelScope.launch {
            mutableLoading.value = true
            courseResponse.value = courseRepository.getCourse(id)
            mutableLoading.value = false
        }
    }
}
