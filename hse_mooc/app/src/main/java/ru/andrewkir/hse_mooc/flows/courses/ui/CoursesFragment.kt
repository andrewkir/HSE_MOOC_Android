package ru.andrewkir.hse_mooc.flows.courses.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesBinding
import ru.andrewkir.hse_mooc.flows.courses.CoursesRepository
import ru.andrewkir.hse_mooc.flows.courses.CoursesViewModel
import ru.andrewkir.hse_mooc.network.api.CoursesApi

class CoursesFragment :
    BaseFragment<CoursesViewModel, CoursesRepository, FragmentCoursesBinding>() {
    override fun provideViewModelClass() = CoursesViewModel::class.java

    override fun provideRepository(): CoursesRepository {
        return CoursesRepository(
            apiProvider.provideApi(
                CoursesApi::class.java,
                requireContext(),
                userPrefsManager.obtainAccessToken(),
                userPrefsManager.obtainRefreshToken()
            )
        )
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCoursesBinding = FragmentCoursesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.logoutButton.setOnClickListener {
            userLogout()
        }
    }
}