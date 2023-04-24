package ru.andrewkir.hse_mooc.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.data.network.api.AuthApi
import ru.andrewkir.hse_mooc.data.repositories.BaseRepository
import ru.andrewkir.hse_mooc.domain.model.ApiResponse

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    suspend fun logoutUser(api: AuthApi): ApiResponse<ResponseBody> {
        return withContext(Dispatchers.IO) {
            repository.userLogout(api)
        }
    }

    protected val mutableLoading: MutableLiveData<Boolean> = MutableLiveData()

    val loading: LiveData<Boolean>
        get() = mutableLoading


    val errorResponse: SingleLiveEvent<ApiResponse.OnErrorResponse> by lazy {
        SingleLiveEvent<ApiResponse.OnErrorResponse>()
    }
}