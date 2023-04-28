package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.data.network.api.CoursesApi

class CoursesSearchRepository(
    private val coursesApi: CoursesApi
) : BaseRepository() {

    suspend fun getCoursesFromServer(query: String, currentPage: Int, categories: String = "") = protectedApiCall {
        coursesApi.getCourses(30, currentPage, query, categories)
    }

    suspend fun getCategories() = protectedApiCall {
        coursesApi.getCategories()
    }
}