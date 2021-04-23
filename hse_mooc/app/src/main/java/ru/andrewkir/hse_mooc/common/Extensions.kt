package ru.andrewkir.hse_mooc.common

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ru.andrewkir.hse_mooc.network.responses.ApiResponse

fun <activity : Activity> Activity.startActivityClearBackStack(activityClass: Class<activity>) {
    Intent(this, activityClass).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.createSnackBarWithAction(msg: String, buttonText: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
    action?.let { thatAction ->
        snackbar.setAction(buttonText) {
            thatAction()
        }
    }
    snackbar.show()
}


fun Fragment.handleApiError(
    error: ApiResponse.OnErrorResponse,
    retry: (() -> Unit)? = null
) {
    //TODO
}