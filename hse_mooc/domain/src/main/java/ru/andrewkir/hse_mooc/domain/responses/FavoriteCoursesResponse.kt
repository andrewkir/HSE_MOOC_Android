package ru.andrewkir.hse_mooc.domain.network.responses

import ru.andrewkir.hse_mooc.data.network.responses.CoursesPreview.CoursePreview

data class FavoriteCoursesResponse (
    val favouriteCourses: List<CoursePreview>
)