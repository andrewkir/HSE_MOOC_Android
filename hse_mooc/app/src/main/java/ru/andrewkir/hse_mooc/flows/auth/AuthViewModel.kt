package ru.andrewkir.hse_mooc.flows.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.LoginResponse

class AuthViewModel(
    private val repo: AuthRepository
) : BaseViewModel(repo) {

    val mutableLoginResponse: MutableLiveData<ApiResponse<LoginResponse>> = MutableLiveData()

    val loginResponse: LiveData<ApiResponse<LoginResponse>>
        get() = mutableLoginResponse

    fun login(username: String, password: String) {
        viewModelScope.launch {
            mutableLoginResponse.value = repo.login(username, password)
        }
    }
}