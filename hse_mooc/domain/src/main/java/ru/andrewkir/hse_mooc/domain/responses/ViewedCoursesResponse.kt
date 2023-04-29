package ru.andrewkir.hse_mooc.domain.network.responses

import ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview.CoursePreview

data class ViewedCoursesResponse (
    val viewedCourses: List<CoursePreview>
)