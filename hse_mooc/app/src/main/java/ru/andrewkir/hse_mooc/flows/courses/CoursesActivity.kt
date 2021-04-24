package ru.andrewkir.hse_mooc.flows.courses

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.plusAssign
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.NavigatorSavingState
import ru.andrewkir.hse_mooc.databinding.ActivityCoursesBinding


class CoursesActivity : AppCompatActivity() {

    private lateinit var bind: ActivityCoursesBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.coursesNavHost) as NavHostFragment

        val navigator = NavigatorSavingState(this, navHost.childFragmentManager, R.id.coursesNavHost)
        navHost.navController.navigatorProvider += navigator

        navHost.navController.setGraph(R.navigation.nav_courses)

        bind.bottomNavigationView.setupWithNavController(navHost.navController)
    }
}