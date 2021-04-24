package ru.andrewkir.hse_mooc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.andrewkir.hse_mooc.flows.auth.ui.AuthActivity
import ru.andrewkir.hse_mooc.flows.courses.CoursesActivity
import ru.andrewkir.hse_mooc.repository.UserPrefsManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = UserPrefsManager(this)
        if (prefs.obtainAccessToken() != null) {
            startActivity(Intent(this, CoursesActivity::class.java))
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        finish()
    }
}