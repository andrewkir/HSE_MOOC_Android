package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.domain.repositories.CoursesSearchRepository

class CoursesSearchRepositoryImpl(
    private val coursesApi: CoursesApi
) : CoursesSearchRepository, BaseRepository() {

    override suspend fun getCoursesFromServer(query: String, currentPage: Int, categories: String) = protectedApiCall {
        coursesApi.getCourses(30, currentPage, query, categories)
    }

    override suspend fun getCategories() = protectedApiCall {
        coursesApi.getCategories()
    }
}