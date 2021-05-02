package ru.andrewkir.hse_mooc.flows.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.flows.auth.AuthRepository
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.LoginResponse

class RegisterViewModel(
    private val repo: AuthRepository
) : BaseViewModel(repo) {

    private val mutableRegisterResponse: MutableLiveData<ApiResponse<ResponseBody>> = MutableLiveData()

    val registerResponse: LiveData<ApiResponse<ResponseBody>>
        get() = mutableRegisterResponse

    private val mutableLoginResponse: MutableLiveData<ApiResponse<LoginResponse>> =
        MutableLiveData()

    val loginResponse: LiveData<ApiResponse<LoginResponse>>
        get() = mutableLoginResponse

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            mutableLoading.value = true
            mutableRegisterResponse.value = repo.register(email, username, password)
            mutableLoading.value = false
        }
    }

    fun loginByUsername(username: String, password: String) {
        viewModelScope.launch {
            mutableLoading.value = true
            mutableLoginResponse.value = repo.loginByUsername(username, password)
            mutableLoading.value = false
        }
    }
}