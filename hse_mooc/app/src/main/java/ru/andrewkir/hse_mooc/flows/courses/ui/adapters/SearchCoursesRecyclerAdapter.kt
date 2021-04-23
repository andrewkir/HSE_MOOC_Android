package ru.andrewkir.hse_mooc.flows.courses.ui.adapters

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.network.responses.Course

class SearchCoursesRecyclerAdapter(
    private val context: Context,
    private val onCourseClick: ((Course) -> Unit)? = null
) :
    RecyclerView.Adapter<SearchCoursesRecyclerAdapter.ViewHolder>() {


    var data: List<Course> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var courseTitle: TextView? = null
        var courseDescription: TextView? = null

        var courseRank: TextView? = null
        var courseCost: TextView? = null

        var courseImage: ImageView? = null

        var cardView: CardView? = null

        init {
            courseTitle = view.findViewById(R.id.rawCourseSearchTitle)
            courseDescription = view.findViewById(R.id.rawCourseSearchDescription)

            courseRank = view.findViewById(R.id.rawCourseSearchRating)
            courseCost = view.findViewById(R.id.rawCourseSearchCost)

            courseImage = view.findViewById(R.id.rawCourseSearchImageView)

            cardView = view.findViewById(R.id.cardViewCoursesSearch)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_search_course_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.courseTitle?.text = data[position].courseName
        viewHolder.courseDescription?.text = data[position].shortDescription

        viewHolder.courseCost?.text = if(data[position].price == "0") "Бесплатно" else data[position].price

        Glide.with(context)
            .load(data[position].previewImageLink)
            .transform(CenterCrop(), RoundedCorners(25))
            .into(viewHolder.courseImage!!)

        viewHolder.cardView?.setOnClickListener { onCourseClick?.invoke(data[position]) }
    }

    override fun getItemCount() = data.size
}