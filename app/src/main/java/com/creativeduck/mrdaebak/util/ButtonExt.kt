package com.creativeduck.mrdaebak.util

import android.os.SystemClock
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Button

fun Button.dispatch(time: Int) {
    dispatchTouchEvent(
        MotionEvent
            .obtain(
                SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + time,
                KeyEvent.ACTION_DOWN, 0f, 0f, 0))
}