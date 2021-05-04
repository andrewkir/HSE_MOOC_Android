package ru.andrewkir.hse_mooc.flows.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.common.SingleLiveEvent
import ru.andrewkir.hse_mooc.network.requests.ReviewRequest
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.Course.CourseResponse
import ru.andrewkir.hse_mooc.network.responses.Reviews.Review

public class CourseViewModel(
    private val courseRepository: CourseRepository
) : BaseViewModel(courseRepository) {

    val courseResponse: MutableLiveData<ApiResponse<CourseResponse>> by lazy {
        MutableLiveData<ApiResponse<CourseResponse>>()
    }

    val reviews: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    val errorResponse: SingleLiveEvent<ApiResponse.OnErrorResponse> by lazy {
        SingleLiveEvent<ApiResponse.OnErrorResponse>()
    }

    val isViewed: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLiked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    var courseId = ""

    init {
        isLiked.value = false
        isViewed.value = false
    }

    fun toggleLike() {
        viewModelScope.launch {
            if (isLiked.value != null) {
                when (val result = courseRepository.updateFavorites(courseId, !isLiked.value!!)) {
                    is ApiResponse.OnSuccessResponse -> {
                        isLiked.value = !isLiked.value!!
                    }
                    is ApiResponse.OnErrorResponse -> {
                        errorResponse.value = result
                    }
                }
            }
        }
    }

    fun toggleViewed() {
        viewModelScope.launch {
            if (isViewed.value != null) {
                when (val result = courseRepository.updateViewed(courseId, !isViewed.value!!)) {
                    is ApiResponse.OnSuccessResponse -> {
                        isViewed.value = !isViewed.value!!
                    }
                    is ApiResponse.OnErrorResponse -> {
                        errorResponse.value = result
                    }
                }
            }
        }
    }

    fun postReview(rating: Float, text: String) {
        viewModelScope.launch {
            when (val result = courseRepository.postReview(
                courseId,
                ReviewRequest(rating.toDouble(), text)
            )) {
                is ApiResponse.OnSuccessResponse -> {
                    updateReviews()
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            when (val result = courseRepository.removeReview(reviewId)) {
                is ApiResponse.OnSuccessResponse -> {
                    updateReviews()
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
        }
    }

    private fun updateReviews() {
        viewModelScope.launch {
            when (val result = courseRepository.getReviews(courseId)) {
                is ApiResponse.OnSuccessResponse -> {
                    reviews.value = result.value.reviews
                    init(courseId)
                }
                is ApiResponse.OnErrorResponse -> {
                    errorResponse.value = result
                }
            }
        }
    }

    fun init(id: String) {
        courseId = id
        viewModelScope.launch {
            mutableLoading.value = true
            courseResponse.value = courseRepository.getCourse(id)
            mutableLoading.value = false
        }
    }
}
