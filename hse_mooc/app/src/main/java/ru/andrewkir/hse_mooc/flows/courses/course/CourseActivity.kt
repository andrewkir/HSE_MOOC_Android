package ru.andrewkir.hse_mooc.flows.courses.course

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.network.responses.CoursesSearch.Course

class CourseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        val item = intent.getParcelableExtra<Course>("COURSE_ITEM")!!

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, CourseFragment.newInstance(item))
            .commit()
    }
}