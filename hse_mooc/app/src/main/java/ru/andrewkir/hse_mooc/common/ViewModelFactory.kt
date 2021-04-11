package ru.andrewkir.hse_mooc.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.andrewkir.hse_mooc.flows.auth.AuthViewModel
import ru.andrewkir.hse_mooc.flows.auth.AuthRepository
import ru.andrewkir.hse_mooc.flows.courses.CoursesRepository
import ru.andrewkir.hse_mooc.flows.courses.CoursesViewModel

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(CoursesViewModel::class.java) -> CoursesViewModel(repository as CoursesRepository) as T
            else -> throw IllegalArgumentException("Provide correct viewModel class")
        }
    }
}