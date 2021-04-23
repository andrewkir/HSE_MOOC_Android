package ru.andrewkir.hse_mooc.flows.courses

import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.CoursesResponse


class CoursesViewModel(
    private val repository: CoursesRepository
): BaseViewModel(repository) {

    private val mutableCoursesLiveData: MutableLiveData<CoursesResponse> = MutableLiveData()

    val coursesLiveData: LiveData<CoursesResponse>
        get() = mutableCoursesLiveData

    fun fetchCourses(){
        viewModelScope.launch {
            when(val result = repository.getCoursesFromServer()){
                is ApiResponse.OnSuccessResponse -> {
                    mutableCoursesLiveData.value = result.value
                }
                is ApiResponse.OnErrorResponse -> {

                }
            }
        }
    }
}