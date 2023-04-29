package ru.andrewkir.hse_mooc.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.andrewkir.hse_mooc.data.repositories.*
import ru.andrewkir.hse_mooc.domain.repositories.*
import ru.andrewkir.hse_mooc.flows.auth.login.LoginViewModel
import ru.andrewkir.hse_mooc.flows.auth.register.RegisterViewModel
import ru.andrewkir.hse_mooc.flows.course.CourseViewModel
import ru.andrewkir.hse_mooc.flows.courses.main.CoursesMainViewModel
import ru.andrewkir.hse_mooc.flows.courses.profile.ProfileViewModel
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchViewModel

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository as AuthRepositoryImpl) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as AuthRepositoryImpl) as T
            modelClass.isAssignableFrom(CoursesSearchViewModel::class.java) -> CoursesSearchViewModel(repository as CoursesSearchRepositoryImpl) as T
            modelClass.isAssignableFrom(CoursesMainViewModel::class.java) -> CoursesMainViewModel(repository as CoursesMainRepositoryImpl) as T
            modelClass.isAssignableFrom(CourseViewModel::class.java) -> CourseViewModel(repository as CourseRepositoryImpl) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository as ProfileRepositoryImpl) as T
            else -> throw IllegalArgumentException("Provide correct viewModel class")
        }
    }
}