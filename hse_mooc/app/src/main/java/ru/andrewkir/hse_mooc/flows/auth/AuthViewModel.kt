package ru.andrewkir.hse_mooc.flows.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.LoginResponse
import ru.andrewkir.hse_mooc.repository.AuthRepository

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    val mutableLoginResponse: MutableLiveData<ApiResponse<LoginResponse>> = MutableLiveData()

    val loginResponse: LiveData<ApiResponse<LoginResponse>>
        get() = mutableLoginResponse

    fun login(username: String, password: String) {
        viewModelScope.launch {
            mutableLoginResponse.value = repo.login(username, password)
        }
    }
}