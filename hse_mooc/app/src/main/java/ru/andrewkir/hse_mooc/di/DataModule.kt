package ru.andrewkir.hse_mooc.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.andrewkir.hse_mooc.data.network.api.AuthApi
import ru.andrewkir.hse_mooc.data.repositories.*
import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.domain.repositories.*

@Module
class DataModule {

    @Provides
    fun provideAuthRepository(authApi: AuthApi): AuthRepository =
        AuthRepositoryImpl(authApi)

    @Provides
    fun provideProfileRepository(coursesApi: CoursesApi): ProfileRepository =
        ProfileRepositoryImpl(coursesApi)

    @Provides
    fun provideCourseRepository(coursesApi: CoursesApi): CourseRepository =
        CourseRepositoryImpl(coursesApi)

    @Provides
    fun provideCoursesMainRepository(coursesApi: CoursesApi): CoursesMainRepository =
        CoursesMainRepositoryImpl(coursesApi)

    @Provides
    fun provideCoursesSearchRepository(coursesApi: CoursesApi): CoursesSearchRepository =
        CoursesSearchRepositoryImpl(coursesApi)

}