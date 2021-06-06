package com.tushar.map.utils.extension

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.tushar.network.exception.NetworkException
import com.tushar.network.model.NetworkError
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

suspend inline fun <reified T> Response<T>.getResponse() = suspendCoroutine<T> { cont ->
    val responseBody = body()
    if (this.isSuccessful && responseBody != null) {
        cont.resume(responseBody)
    } else {
        cont.resumeWithException(
            NetworkException(
                Gson().fromJson(
                    errorBody()!!.string(),
                    NetworkError::class.java
                )
            )
        )
    }
}

fun View.clickWithDebounce(action: () -> Unit, debounceTime: Long = 1200L) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}
