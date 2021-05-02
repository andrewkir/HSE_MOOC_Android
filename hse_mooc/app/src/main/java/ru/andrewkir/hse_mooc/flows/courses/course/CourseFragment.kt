package ru.andrewkir.hse_mooc.flows.courses.course

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_course_page.*
import kotlinx.android.synthetic.main.fragment_login.*
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.handleApiError
import ru.andrewkir.hse_mooc.common.openLink
import ru.andrewkir.hse_mooc.databinding.FragmentCoursePageBinding
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchRepository
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchViewModel
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.Course.Course
import ru.andrewkir.hse_mooc.network.responses.Course.CourseResponse
import java.text.DecimalFormat
import java.util.*


class CourseFragment :
    BaseFragment<CourseViewModel, CourseRepository, FragmentCoursePageBinding>() {

    companion object {
        const val COURSE_TAG = "courseItem"

        fun newInstance(courseId: String): CourseFragment {
            val args = Bundle()
            args.putString(COURSE_TAG, courseId)
            val fragment = CourseFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var courseId: String

    override fun provideViewModelClass() = CourseViewModel::class.java

    override fun provideRepository(): CourseRepository {
        return CourseRepository(
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

        courseId = arguments?.getString(COURSE_TAG)!!

        if (viewModel.courseResponse.value == null) viewModel.init(courseId)

        setupInteractButtons()
        subscribeToCourse()
        subscribeToLoading()
        subscribeToInteract()
    }

    private fun subscribeToCourse() {
        viewModel.courseResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is ApiResponse.OnSuccessResponse -> {
                    response.value.let {
                        updateUi(it)
                    }
                }
                is ApiResponse.OnErrorResponse -> {
                    if (response.isNetworkFailure) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.check_internet),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().finish()
                    } else {
                        handleApiError(response)
                    }
                }
            }
        })
    }

    private fun updateUi(course: CourseResponse) {
        course.let {
            bind.courseName.text = it.course.courseName

            Glide.with(requireContext())
                .load(it.course.previewImageLink)
                .placeholder(R.drawable.course_image_overlay)
                .into(bind.courseImage)

            bind.button.setOnClickListener { _ ->
                openLink(it.course.link)
            }

            if (it.course.price.amount != 0.0) {
                val currency = Currency.getInstance(it.course.price.currency)
                val symbol: String = currency.getSymbol(Locale("ru", "RU"))
                val format = DecimalFormat("0.#")
                bind.courseCost.text = format.format(it.course.price.amount) + " $symbol"
            } else {
                bind.courseCost.text = "Бесплатно"
            }

            bind.courseLanguage.text =
                it.course.courseLanguages.joinToString(", ").toUpperCase()

            bind.courseVendor.text = it.course.vendor.name
            Glide.with(requireContext())
                .load(it.course.vendor.icon)
                .placeholder(R.drawable.designer)
                .into(bind.courseVendorIcon)
            bind.courseVendor.setOnClickListener { _ ->
                openLink(it.course.vendor.link)
            }

            bind.courseAuthor.text = it.course.author.name
            Glide.with(requireContext())
                .load(it.course.author.icon)
                .placeholder(R.drawable.designer)
                .into(bind.courseAuthorIcon)
            bind.courseAuthor.setOnClickListener { _ ->
                openLink(it.course.author.link)
            }

            bind.ratingVendorText.text = "Рейтинг ${it.course.vendor.name}"
            bind.ratingVendor.rating = it.course.rating.external.averageScore.toFloat()

            bind.ratingInternal.rating = it.course.rating.internal.averageScore.toFloat()

            bind.courseDescription.text = it.course.description

            categoryHeaderText.text =
                if (it.course.categories.size > 1) "Категории" else "Категория"
            for (category in it.course.categories) {
                val chip =
                    this.layoutInflater.inflate(
                        R.layout.chip_category_page,
                        bind.categoriesChipGroup,
                        false
                    ) as Chip
                chip.id = View.generateViewId()
                chip.text = category.name.ru
                chip.setOnClickListener {

                }
                bind.categoriesChipGroup.addView(chip)
            }

            viewModel.isLiked.value = it.isFavourite
            viewModel.isViewed.value = it.isViewed
        }
    }

    private fun subscribeToLoading() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) bind.coursePageLoading.visibility = View.VISIBLE
            else bind.coursePageLoading.visibility = View.GONE
        })
    }

    private fun subscribeToInteract() {
        viewModel.isLiked.observe(viewLifecycleOwner, Observer {
            if (it) bind.likeButton.setImageResource(R.drawable.ic_favorite_filled)
            else bind.likeButton.setImageResource(R.drawable.ic_favorite)
        })

        viewModel.isViewed.observe(viewLifecycleOwner, Observer {
            if (it) bind.alreadySawButton.setImageResource(R.drawable.ic_visibility)
            else bind.alreadySawButton.setImageResource(R.drawable.ic_visibility_off)
        })
    }

    private fun setupInteractButtons() {
        bind.likeButton.setOnClickListener {
            viewModel.toggleLike(courseId)
        }

        bind.alreadySawButton.setOnClickListener {
            viewModel.toggleViewed(courseId)
        }
    }
}