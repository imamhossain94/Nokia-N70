package com.newagedevs.sis_emu.extensions

import android.view.MotionEvent
import android.view.View
import android.widget.Toast

fun View.applyScaleOnTouch() {
    setOnTouchListener { v, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                v.performClick()
                true
            }
            else -> false
        }
    }
}


enum class NavAction {
    LEFT, RIGHT, TOP, BOTTOM, NONE
}

fun View.applyScaleOnTouch(nav: (Set<NavAction>) -> Unit) {
    setOnTouchListener { v, motionEvent ->
        val actions = mutableSetOf<NavAction>()
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                val viewWidth = v.width
                val viewHeight = v.height

                val touchX = motionEvent.x
                val touchY = motionEvent.y

                if (touchX < viewWidth * 0.25) actions.add(NavAction.LEFT)
                if (touchX > viewWidth * 0.75) actions.add(NavAction.RIGHT)
                if (touchY < viewHeight * 0.25) actions.add(NavAction.TOP)
                if (touchY > viewHeight * 0.75) actions.add(NavAction.BOTTOM)

                if (actions.isEmpty()) actions.add(NavAction.NONE)

                nav.invoke(actions)

                v.scaleX = 1.0f
                v.scaleY = 1.0f
                v.pivotX = viewWidth / 2f
                v.pivotY = viewHeight / 2f

                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                v.performClick()
                true
            }
            else -> false
        }
    }
}





