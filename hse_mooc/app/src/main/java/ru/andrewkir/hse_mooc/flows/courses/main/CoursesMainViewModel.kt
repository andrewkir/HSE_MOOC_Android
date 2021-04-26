package ru.andrewkir.hse_mooc.flows.courses.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse


class CoursesMainViewModel(
    private val searchRepository: CoursesMainRepository
) : BaseViewModel(searchRepository) {

    private val mutableResponse: MutableLiveData<ApiResponse<ResponseBody>> =
        MutableLiveData()

    val response: LiveData<ApiResponse<ResponseBody>>
        get() = mutableResponse

    fun testApi() {
        viewModelScope.launch {
            mutableResponse.value = searchRepository.testApi()
        }
    }
}