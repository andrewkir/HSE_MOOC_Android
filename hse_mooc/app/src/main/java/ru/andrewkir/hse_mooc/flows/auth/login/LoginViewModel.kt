package ru.andrewkir.hse_mooc.flows.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.flows.auth.AuthRepository
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.LoginResponse

class LoginViewModel(
    private val repo: AuthRepository
) : BaseViewModel(repo) {

    private val mutableLoginResponse: MutableLiveData<ApiResponse<LoginResponse>> =
        MutableLiveData()

    val loginResponse: LiveData<ApiResponse<LoginResponse>>
        get() = mutableLoginResponse

    fun loginByUsername(username: String, password: String) {
        viewModelScope.launch {
            mutableLoading.value = true
            mutableLoginResponse.value = repo.loginByUsername(username, password)
            mutableLoading.value = false
        }
    }

    fun loginByEmail(email: String, password: String) {
        viewModelScope.launch {
            mutableLoading.value = true
            mutableLoginResponse.value = repo.loginByEmail(email, password)
            mutableLoading.value = false
        }
    }
}