package ru.andrewkir.hse_mooc.flows.courses.course

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.network.responses.Course.Course
import ru.andrewkir.hse_mooc.network.responses.CoursesPreview.CoursePreview

class CourseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.parseColor("#20111111")
        }

        val item = intent.getParcelableExtra<CoursePreview>("COURSE_ITEM")!!

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, CourseFragment.newInstance(item.id)) //TODO fix
                .commit()
        }
    }
}