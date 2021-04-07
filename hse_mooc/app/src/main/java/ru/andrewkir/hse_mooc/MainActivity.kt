package ru.andrewkir.hse_mooc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.andrewkir.hse_mooc.flows.auth.AuthActivity
import ru.andrewkir.hse_mooc.repository.UserPrefsManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = UserPrefsManager(this)
        Toast.makeText(this, prefs.obtainAccessToken(), Toast.LENGTH_SHORT).show()

        finish()
        startActivity(Intent(this, AuthActivity::class.java))
    }
}