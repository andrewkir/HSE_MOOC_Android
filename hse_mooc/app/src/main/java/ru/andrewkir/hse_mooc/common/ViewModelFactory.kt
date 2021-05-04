package ru.andrewkir.hse_mooc.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.andrewkir.hse_mooc.flows.auth.login.LoginViewModel
import ru.andrewkir.hse_mooc.flows.auth.AuthRepository
import ru.andrewkir.hse_mooc.flows.auth.register.RegisterViewModel
import ru.andrewkir.hse_mooc.flows.course.CourseRepository
import ru.andrewkir.hse_mooc.flows.course.CourseViewModel
import ru.andrewkir.hse_mooc.flows.courses.main.CoursesMainRepository
import ru.andrewkir.hse_mooc.flows.courses.main.CoursesMainViewModel
import ru.andrewkir.hse_mooc.flows.courses.profile.ProfileRepository
import ru.andrewkir.hse_mooc.flows.courses.profile.ProfileViewModel
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchRepository
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchViewModel

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(CoursesSearchViewModel::class.java) -> CoursesSearchViewModel(repository as CoursesSearchRepository) as T
            modelClass.isAssignableFrom(CoursesMainViewModel::class.java) -> CoursesMainViewModel(repository as CoursesMainRepository) as T
            modelClass.isAssignableFrom(CourseViewModel::class.java) -> CourseViewModel(repository as CourseRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository as ProfileRepository) as T
            else -> throw IllegalArgumentException("Provide correct viewModel class")
        }
    }
}