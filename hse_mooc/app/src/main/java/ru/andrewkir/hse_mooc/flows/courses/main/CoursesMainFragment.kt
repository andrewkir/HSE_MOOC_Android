package ru.andrewkir.hse_mooc.flows.courses.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.handleApiError
import ru.andrewkir.hse_mooc.common.startActivityClearBackStack
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesBinding
import ru.andrewkir.hse_mooc.flows.courses.CoursesActivity
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchRepository
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchViewModel
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse

class CoursesMainFragment :
    BaseFragment<CoursesMainViewModel, CoursesMainRepository, FragmentCoursesBinding>() {
    override fun provideViewModelClass() = CoursesMainViewModel::class.java

    override fun provideRepository(): CoursesMainRepository {
        return CoursesMainRepository(
            apiProvider.provideApi(
                CoursesApi::class.java,
                requireContext(),
                userPrefsManager.obtainAccessToken()
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

        bind.testApiButton.setOnClickListener {
            viewModel.testApi()
        }

        setupResponseListener()
    }

    private fun setupResponseListener(){
        viewModel.response.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiResponse.OnSuccessResponse -> {
                    Toast.makeText(
                        requireContext(),
                        it.value.string(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                is ApiResponse.OnErrorResponse -> handleApiError(it)
            }
        })
    }
}