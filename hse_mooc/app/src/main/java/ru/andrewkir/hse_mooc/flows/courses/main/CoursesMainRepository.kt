package ru.andrewkir.hse_mooc.flows.courses.main

import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.requests.RegisterRequest

class CoursesMainRepository(
    private val coursesApi: CoursesApi
) : BaseRepository() {

    suspend fun getTrendingCourses(url: String) = protectedApiCall{
        coursesApi.getTrending(url)
    }
}