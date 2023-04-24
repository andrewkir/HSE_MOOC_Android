package ru.andrewkir.hse_mooc.data.network.responses

import ru.andrewkir.hse_mooc.data.network.responses.CoursesPreview.CoursePreview

data class ViewedCoursesResponse (
    val viewedCourses: List<CoursePreview>
)