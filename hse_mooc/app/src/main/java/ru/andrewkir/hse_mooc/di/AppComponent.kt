package ru.andrewkir.hse_mooc.di

import androidx.viewbinding.ViewBinding
import dagger.Component
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.BaseViewModel
import ru.andrewkir.hse_mooc.data.repositories.BaseRepository
import ru.andrewkir.hse_mooc.flows.auth.AuthFragment
import ru.andrewkir.hse_mooc.flows.auth.login.LoginFragment
import ru.andrewkir.hse_mooc.flows.auth.register.RegisterFragment
import ru.andrewkir.hse_mooc.flows.course.CourseFragment
import ru.andrewkir.hse_mooc.flows.courses.profile.ProfileFragment
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchFragment
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataModule::class, ViewModelModule::class, NetworkModule::class])
@Singleton
interface AppComponent {
    fun inject(baseFragment: BaseFragment<BaseViewModel, BaseRepository, ViewBinding>)
    fun inject(profileFragment: ProfileFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(coursesSearchFragment: CoursesSearchFragment)
    fun inject(courseFragment: CourseFragment)
    fun inject(authFragment: AuthFragment)
}