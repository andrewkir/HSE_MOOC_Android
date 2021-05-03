package ru.andrewkir.hse_mooc.flows.courses.course.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import per.wsj.library.AndRatingBar
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.network.responses.Reviews.Review
import java.text.SimpleDateFormat

class CourseCommentsAdapter(
    private val context: Context,
    private val onCommentClick: ((Review) -> Unit)? = null
) :
    RecyclerView.Adapter<CourseCommentsAdapter.CommentViewHolder>() {

    var data: List<Review> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.reycler_course_review_item, viewGroup, false)
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: CommentViewHolder, position: Int) {
        viewHolder.commentRating?.rating = data[position].rating.toFloat()
        viewHolder.commentUsername?.text = data[position].user.username
        viewHolder.commentContent?.text = data[position].text

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = formatter.parse(data[position].creationDate)

        viewHolder.commentDate?.text = SimpleDateFormat("dd-MM-yyyy").format(date).toString()
        if(data[position].isMyReview == true){
            viewHolder.removeButton?.visibility = View.VISIBLE
            viewHolder.removeButton?.setOnClickListener { onCommentClick?.invoke(data[position]) }
        } else {
            viewHolder.removeButton?.visibility = View.GONE
        }
    }


    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var commentRating: AndRatingBar? = null
        var commentUsername: TextView? = null
        var commentContent: TextView? = null
        var commentDate: TextView? = null

        var removeButton: ImageButton? = null

        init {
            commentRating = view.findViewById(R.id.reviewRating)
            commentUsername = view.findViewById(R.id.reviewAuthor)
            commentContent = view.findViewById(R.id.reviewContent)
            commentDate = view.findViewById(R.id.reviewDate)

            removeButton = view.findViewById(R.id.reviewDeleteButton)
        }
    }

    override fun getItemCount() = data.size
}