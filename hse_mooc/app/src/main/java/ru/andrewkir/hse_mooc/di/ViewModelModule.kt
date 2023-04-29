package ru.andrewkir.hse_mooc.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.andrewkir.hse_mooc.common.ViewModelFactory
import ru.andrewkir.hse_mooc.flows.auth.login.LoginViewModel
import ru.andrewkir.hse_mooc.flows.auth.register.RegisterViewModel
import ru.andrewkir.hse_mooc.flows.course.CourseViewModel
import ru.andrewkir.hse_mooc.flows.courses.main.CoursesMainViewModel
import ru.andrewkir.hse_mooc.flows.courses.profile.ProfileViewModel
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchViewModel

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun viewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun viewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun viewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CourseViewModel::class)
    abstract fun viewModel(viewModel: CourseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoursesSearchViewModel::class)
    abstract fun viewModel(viewModel: CoursesSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoursesMainViewModel::class)
    abstract fun viewModel(viewModel: CoursesMainViewModel): ViewModel
}