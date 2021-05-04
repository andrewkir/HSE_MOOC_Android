package ru.andrewkir.hse_mooc.flows.courses.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.databinding.FragmentCoursesSearchBinding
import ru.andrewkir.hse_mooc.databinding.FragmentProfileBinding
import ru.andrewkir.hse_mooc.flows.course.CourseActivity
import ru.andrewkir.hse_mooc.flows.courses.profile.adapters.ProfileCourseRecyclerAdapter
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchRepository
import ru.andrewkir.hse_mooc.flows.courses.search.adapters.SearchCoursesRecyclerAdapter
import ru.andrewkir.hse_mooc.flows.courses.search.adapters.SearchScrollListener
import ru.andrewkir.hse_mooc.network.api.CoursesApi

class ProfileFragment :
    BaseFragment<ProfileViewModel, ProfileRepository, FragmentProfileBinding>() {

    private lateinit var recyclerFavoritesAdapter: ProfileCourseRecyclerAdapter
    private lateinit var linearFavoritesLayoutManager: LinearLayoutManager

    private lateinit var recyclerViewedAdapter: ProfileCourseRecyclerAdapter
    private lateinit var linearViewedLayoutManager: LinearLayoutManager


    override fun provideViewModelClass() = ProfileViewModel::class.java

    override fun provideRepository(): ProfileRepository {
        return ProfileRepository(
            apiProvider.provideApi(
                CoursesApi::class.java,
                requireContext(),
                userPrefsManager.accessToken
            )
        )
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupExpandButtons()
        subscribeToFavorites()
        subscribeToViewed()

        bind.profileEmail.text = userPrefsManager.email
        bind.profileUsername.text = userPrefsManager.username
    }

    private fun setupRecyclerViews() {
        recyclerFavoritesAdapter = ProfileCourseRecyclerAdapter(requireContext()) {
            val intent = Intent(requireContext(), CourseActivity::class.java)
            intent.putExtra("COURSE_ID", it.id)
            startActivity(intent)
        }
        linearFavoritesLayoutManager = LinearLayoutManager(requireContext())
        bind.profileFavoritesRecyclerView.run {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = linearFavoritesLayoutManager
            adapter = recyclerFavoritesAdapter
        }

        recyclerViewedAdapter = ProfileCourseRecyclerAdapter(requireContext()) {
            val intent = Intent(requireContext(), CourseActivity::class.java)
            intent.putExtra("COURSE_ID", it.id)
            startActivity(intent)
        }
        linearViewedLayoutManager = LinearLayoutManager(requireContext())
        bind.profileViewedRecyclerView.run {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = linearViewedLayoutManager
            adapter = recyclerViewedAdapter
        }
    }

    private fun setupExpandButtons() {
        bind.expandFavorites.setOnClickListener {
            //TODO fix animation with loading
            if (bind.expandFavorites.rotation == 0f) {
                viewModel.getFavorites()
                bind.expandFavorites.animate()
                    .rotation(90f).interpolator =
                    AccelerateDecelerateInterpolator()
            } else {
                bind.expandFavorites.animate().rotation(0f).interpolator =
                    AccelerateDecelerateInterpolator()
            }
        }

        bind.expandViewed.setOnClickListener {
            if (bind.expandViewed.rotation == 0f) {
                viewModel.getViewed()
                bind.expandViewed.animate()
                    .rotation(90f).interpolator =
                    AccelerateDecelerateInterpolator()
            } else {
                bind.expandViewed.animate().rotation(0f).interpolator =
                    AccelerateDecelerateInterpolator()
            }
        }
    }

    private fun subscribeToFavorites(){
        viewModel.viewedCourses.observe(viewLifecycleOwner, Observer {
            recyclerViewedAdapter.data = it
        })
    }

    private fun subscribeToViewed(){
        viewModel.favoritesCourses.observe(viewLifecycleOwner, Observer {
            recyclerFavoritesAdapter.data = it
        })
    }
}