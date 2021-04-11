package ru.andrewkir.hse_mooc.flows.courses

import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.api.CoursesApi

class CoursesRepository(
    private val coursesApi: CoursesApi
): BaseRepository() {
}