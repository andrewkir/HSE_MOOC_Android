package ru.andrewkir.hse_mooc.common

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import ru.andrewkir.hse_mooc.flows.auth.ui.LoginFragment
import ru.andrewkir.hse_mooc.network.responses.ApiResponse

fun <activity : Activity> Activity.startActivityClearBackStack(activityClass: Class<activity>) {
    Intent(this, activityClass).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun Fragment.handleApiError(
    error: ApiResponse.OnErrorResponse,
    retry: (() -> Unit)? = null
) {
    if (error.isNetworkFailure) {
        requireView().createRetrySnackbar(
            "Проверьте интернет подключение",
            retry
        )
        return
    }

    val parsedError = try {
        if (error.body == null) ""
        else {
            val jsonObj = JSONObject(error.body.string())
            jsonObj.getString("error")
        }
    } catch (ex: JSONException) {
        ""
    }

    if (error.code == 401) {
        if (this is LoginFragment) {
            if (parsedError.isNotEmpty()) requireView().createRetrySnackbar(parsedError)
            else requireView().createRetrySnackbar("Попробуйте ещё раз")
        } else (this as BaseFragment<*, *, *>).userLogout()

        return
    }

    if (parsedError.isNotEmpty()) requireView().createRetrySnackbar(parsedError)
    else requireView().createRetrySnackbar(error.body?.string().toString())
}

fun View.createRetrySnackbar(msg: String, retry: (() -> Unit)? = null) {
    val snack = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
    retry?.let {
        snack.setAction("Повторить") {
            it()
        }
    }
    snack.show()
}