package com.newagedevs.sis_emu.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import com.newagedevs.sis_emu.R

enum class NavCommands {
    LEFT, ACTION, RIGHT
}

interface NavCommandListener {
    fun onNavCommand(command: NavCommands)
}


class NavigationBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val btnNavLeft: TextView
    private val btnNavAction: TextView
    private val btnNavRight: TextView

    var navCommandListener: NavCommandListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_navigation_bar, this, true)
        btnNavLeft = findViewById(R.id.btn_nav_left)
        btnNavAction = findViewById(R.id.btn_nav_action)
        btnNavRight = findViewById(R.id.btn_nav_right)

        btnNavLeft.setOnClickListener { navCommandListener?.onNavCommand(NavCommands.LEFT) }
        btnNavAction.setOnClickListener { navCommandListener?.onNavCommand(NavCommands.ACTION) }
        btnNavRight.setOnClickListener { navCommandListener?.onNavCommand(NavCommands.RIGHT) }
    }

    fun updateLeftButtonText(text: String) {
        btnNavLeft.text = text
    }

    fun updateActionButtonText(text: String) {
        btnNavAction.text = text
    }

    fun updateRightButtonText(text: String) {
        btnNavRight.text = text
    }

    fun show() {
        visibility = View.VISIBLE
        val animation = TranslateAnimation(0f, 0f, +height.toFloat(), 0f).apply {
            duration = 100
            fillAfter = true
        }
        startAnimation(animation)
    }


    fun hide() {
        val animation = TranslateAnimation(0f, 0f, 0f, height.toFloat()).apply {
            duration = 100
            fillAfter = true
        }
        startAnimation(animation)
        visibility = View.GONE
    }

}

