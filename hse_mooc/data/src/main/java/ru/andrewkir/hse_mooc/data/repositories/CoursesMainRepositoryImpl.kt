package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.data.network.api.CoursesApi

class CoursesMainRepository(
    private val coursesApi: CoursesApi
) : BaseRepository() {

    suspend fun getTrendingCourses(url: String) = protectedApiCall{
        coursesApi.getTrending(url)
    }

    suspend fun getCoursesMain() = protectedApiCall {
        coursesApi.getMainCourses()
    }

    suspend fun getCompilations() = protectedApiCall {
        coursesApi.getCompilations()
    }
}