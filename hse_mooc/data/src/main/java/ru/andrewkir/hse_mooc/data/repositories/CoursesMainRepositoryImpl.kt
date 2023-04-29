package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.domain.repositories.CoursesMainRepository

class CoursesMainRepositoryImpl(
    private val coursesApi: CoursesApi
) : CoursesMainRepository, BaseRepository() {

    override suspend fun getTrendingCourses(url: String) = protectedApiCall{
        coursesApi.getTrending(url)
    }

    override suspend fun getCoursesMain() = protectedApiCall {
        coursesApi.getMainCourses()
    }

    override suspend fun getCompilations() = protectedApiCall {
        coursesApi.getCompilations()
    }
}