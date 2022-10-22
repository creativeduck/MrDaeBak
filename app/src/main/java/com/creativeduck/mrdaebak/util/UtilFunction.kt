package com.creativeduck.mrdaebak.util

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.widget.LinearLayoutCompat
import com.creativeduck.mrdaebak.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

fun LinearLayoutCompat.dispatch(time: Int) {
    dispatchTouchEvent(
        MotionEvent
            .obtain(
                SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + time,
                KeyEvent.ACTION_DOWN, 0f, 0f, 0))
}

fun Long.moneyFormat(): String {
    if (this < 0) return ""
    val formatter = DecimalFormat("#,###") as NumberFormat
    return formatter.format(this) + "원"
}

fun <T: Any> Call<T>.getResponse(success: (T) -> Unit, failure: (Throwable) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful && response.body() != null)
                success(response.body()!!)
            else
                failure(Exception("결과값이 비어있습니다."))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            failure(t)
        }
    })
}

inline fun <reified T: Activity> Activity.goActivityWithInt(vararg value: Pair<String, Int>, clear: Boolean = false) {
    val intent = Intent(this, T::class.java)
    for(item in value) {
        intent.putExtra(item.first, item.second)
    }
    if (clear)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

inline fun <reified T: Activity> Activity.goActivityWithLong(vararg value: Pair<String, Long>, clear: Boolean = false) {
    val intent = Intent(this, T::class.java)
    for(item in value) {
        intent.putExtra(item.first, item.second)
    }
    if (clear)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}