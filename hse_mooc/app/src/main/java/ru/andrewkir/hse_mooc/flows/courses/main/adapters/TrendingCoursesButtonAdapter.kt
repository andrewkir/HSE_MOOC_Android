package ru.andrewkir.hse_mooc.flows.courses.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.data.network.responses.Compilations.CompilationsResponseItem


class TrendingCoursesButtonAdapter(
    private val context: Context,
    private val onButtonClick: ((CompilationsResponseItem) -> Unit)? = null
) :
    RecyclerView.Adapter<TrendingCoursesButtonAdapter.CourseViewHolder>() {

    var data: List<CompilationsResponseItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_trending_course_item, viewGroup, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: CourseViewHolder, position: Int) {
        viewHolder.name?.text = data[position].name.ru

        Glide.with(context)
            .load(data[position].icon)
            .circleCrop()
            .into(viewHolder.button!!)

        viewHolder.button?.setOnClickListener { onButtonClick?.invoke(data[position]) }
    }

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var button: ImageView? = null
        var name: TextView? = null

        init {
            button = view.findViewById(R.id.trendingImageButton)
            name = view.findViewById(R.id.trendingName)
        }
    }

    override fun getItemCount() = data.size
}