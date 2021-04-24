package ru.andrewkir.hse_mooc.flows.courses

import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.requests.RegisterRequest

class CoursesRepository(
    private val coursesApi: CoursesApi
) : BaseRepository() {
    suspend fun getCoursesFromServer(currentPage: Int) = protectedApiCall {
        coursesApi.getCourses(10, currentPage)
    }
}