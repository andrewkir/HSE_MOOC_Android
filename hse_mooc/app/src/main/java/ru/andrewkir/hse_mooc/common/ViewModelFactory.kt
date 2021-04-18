package ru.andrewkir.hse_mooc.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.andrewkir.hse_mooc.flows.auth.LoginViewModel
import ru.andrewkir.hse_mooc.flows.auth.AuthRepository
import ru.andrewkir.hse_mooc.flows.auth.RegisterViewModel
import ru.andrewkir.hse_mooc.flows.courses.CoursesRepository
import ru.andrewkir.hse_mooc.flows.courses.CoursesViewModel

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(CoursesViewModel::class.java) -> CoursesViewModel(repository as CoursesRepository) as T
            else -> throw IllegalArgumentException("Provide correct viewModel class")
        }
    }
}