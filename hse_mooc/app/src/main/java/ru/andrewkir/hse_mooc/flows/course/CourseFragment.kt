package ru.andrewkir.hse_mooc.flows.course

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import per.wsj.library.AndRatingBar
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.handleApiError
import ru.andrewkir.hse_mooc.common.openLink
import ru.andrewkir.hse_mooc.data.repositories.CourseRepositoryImpl
import ru.andrewkir.hse_mooc.databinding.FragmentCoursePageBinding
import ru.andrewkir.hse_mooc.flows.course.adapters.CourseCommentsAdapter
import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.domain.network.responses.Course.CourseResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Reviews.Review
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class CourseFragment :
    BaseFragment<CourseViewModel, CourseRepositoryImpl, FragmentCoursePageBinding>() {

    private lateinit var recyclerAdapter: CourseCommentsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var myCurrentReview: Review? = null

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

    override fun provideRepository(): CourseRepositoryImpl {
        return CourseRepositoryImpl(
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
    ): FragmentCoursePageBinding = FragmentCoursePageBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseId = arguments?.getString(COURSE_TAG)!!

        if (viewModel.courseResponse.value == null) viewModel.init(courseId)

        setupInteractButtons()
        setupRecyclerView()
        subscribeToCourse()
        subscribeToLoading()
        subscribeToInteract()
        subscribeToReview()
        subscribeToError()
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

    @SuppressLint("SetTextI18n")
    private fun updateUi(course: CourseResponse) {
        course.let { it ->
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

            bind.categoryHeaderText.text =
                if (it.course.categories.size > 1) "Категории" else "Категория"

            bind.categoriesChipGroup.removeAllViews()

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

            viewModel.reviews.value = it.course.reviews.toList()
        }
    }

    private fun subscribeToError() {
        viewModel.errorResponse.observe(viewLifecycleOwner, Observer {
            handleApiError(it)
        })
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

    private fun subscribeToReview() {
        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            val reviewList = ArrayList<Review>()
            val username = userPrefsManager.username

            myCurrentReview = null
            for (review in it) {
                if (review.user.username == username) {
                    reviewList.add(0, review.also { r -> r.isMyReview = true })
                    myCurrentReview = review
                } else reviewList.add(review)
            }
            if (reviewList.size == 0) bind.reviewPlaceHolder.visibility = View.VISIBLE
            else bind.reviewPlaceHolder.visibility = View.GONE

            recyclerAdapter.data = reviewList
        })
    }

    private fun setupInteractButtons() {
        bind.likeButton.setOnClickListener {
            viewModel.toggleLike()
        }

        bind.alreadySawButton.setOnClickListener {
            viewModel.toggleViewed()
        }

        bind.editReviewButton.setOnClickListener {
            showDialogNewReview()
        }
    }

    private fun setupRecyclerView() {
        recyclerAdapter = CourseCommentsAdapter(requireContext()) {
            viewModel.deleteReview(it.id)
        }
        linearLayoutManager = LinearLayoutManager(requireContext())

        bind.courseCommentsRecycler.run {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = linearLayoutManager
            adapter = recyclerAdapter
        }
    }

    private fun showDialogNewReview() {
        val dialog = AlertDialog.Builder(requireContext())

        val view = layoutInflater.inflate(R.layout.dialog_new_review, null)
        dialog.setView(view)
        val ratingBar = view.findViewById<AndRatingBar>(R.id.reviewDialogRating)
        val reviewText = view.findViewById<EditText>(R.id.reviewDialogText)

        myCurrentReview?.let {
            ratingBar.rating = it.rating.toFloat()
            reviewText.setText(it.text)
        }

        val alertDialog = dialog
            .setTitle("Отзыв на курс")
            .setPositiveButton("Ок") { _, _ -> }
            .setNeutralButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .create()

        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            var closeDialog = false
            if (ratingBar.rating < 1f) {
                Toast.makeText(
                    requireContext(),
                    "Рейтинг не может быть меньше 1",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (reviewText.text.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Отзыв не может быть пустым",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                closeDialog = true
                viewModel.postReview(
                    ratingBar.rating, reviewText.text.toString()
                )
            }
            if (closeDialog) alertDialog.dismiss()
        }
    }
}