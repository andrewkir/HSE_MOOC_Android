package ru.andrewkir.hse_mooc.flows.courses.course

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.openLink
import ru.andrewkir.hse_mooc.common.px
import ru.andrewkir.hse_mooc.databinding.FragmentCoursePageBinding
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchRepository
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchViewModel
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.CoursesSearch.Course


class CourseFragment :
    BaseFragment<CoursesSearchViewModel, CoursesSearchRepository, FragmentCoursePageBinding>() {

    companion object {
        const val COURSE_TAG = "courseItem"

        fun newInstance(course: Course): CourseFragment {
            val args = Bundle()
            args.putParcelable(COURSE_TAG, course)
            val fragment = CourseFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideViewModelClass() = CoursesSearchViewModel::class.java

    override fun provideRepository(): CoursesSearchRepository {
        return CoursesSearchRepository(
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
    ): FragmentCoursePageBinding = FragmentCoursePageBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val course = arguments?.getParcelable<Course>(COURSE_TAG)

        course?.let {
            bind.courseName.text = it.courseName

            Glide.with(requireContext())
                .load(it.previewImageLink)
                .placeholder(ru.andrewkir.hse_mooc.R.drawable.course_image_overlay)
                .into(bind.imageView3)

            bind.button.setOnClickListener {
                openLink(course.link)
            }
        }
    }
}