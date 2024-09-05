package com.newagedevs.sis_emu.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.newagedevs.sis_emu.R
import com.newagedevs.sis_emu.extensions.vibrate

enum class KeyCommands {
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
    STAR, HASH, MENU, SETTINGS, OPTION, CALL, CANCEL, EXIT,
    // Navigation
    LEFT, RIGHT, TOP, BOTTOM, HOME, TOP_LEFT, BOTTOM_LEFT, TOP_RIGHT, BOTTOM_RIGHT
}

interface KeyCommandListener {
    fun onKeyCommand(command: KeyCommands)
}

class KeyControlsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var keyCommandListener: KeyCommandListener? = null

    init {
        inflate(context, R.layout.view_key_controls, this)
        setupListeners()
    }

    private fun setupListeners() {
        val buttonMappings = mapOf(
            R.id.btn_0 to KeyCommands.ZERO,
            R.id.btn_1 to KeyCommands.ONE,
            R.id.btn_2 to KeyCommands.TWO,
            R.id.btn_3 to KeyCommands.THREE,
            R.id.btn_4 to KeyCommands.FOUR,
            R.id.btn_5 to KeyCommands.FIVE,
            R.id.btn_6 to KeyCommands.SIX,
            R.id.btn_7 to KeyCommands.SEVEN,
            R.id.btn_8 to KeyCommands.EIGHT,
            R.id.btn_9 to KeyCommands.NINE,
            R.id.btn_star to KeyCommands.STAR,
            R.id.btn_hash to KeyCommands.HASH,
            R.id.btn_menu to KeyCommands.MENU,
            R.id.btn_settings to KeyCommands.SETTINGS,
            R.id.btn_option to KeyCommands.OPTION,
            R.id.btn_call to KeyCommands.CALL,
            R.id.btn_cancel to KeyCommands.CANCEL,
            R.id.btn_exit to KeyCommands.EXIT
        )

        buttonMappings.forEach { (id, command) ->
            findViewById<ImageView>(id).applyScaleOnTouch().setOnClickListener { sendCommand(command) }
        }

        findViewById<View>(R.id.btn_navigation).applyScaleOnTouch { action ->
            if(action.size == 1) {
                sendCommand(action.first())
            } else when (action) {
                setOf(KeyCommands.TOP, KeyCommands.LEFT) -> sendCommand(KeyCommands.TOP_LEFT)
                setOf(KeyCommands.BOTTOM, KeyCommands.LEFT) -> sendCommand(KeyCommands.BOTTOM_LEFT)
                setOf(KeyCommands.TOP, KeyCommands.RIGHT) -> sendCommand(KeyCommands.TOP_RIGHT)
                setOf(KeyCommands.BOTTOM, KeyCommands.RIGHT) -> sendCommand(KeyCommands.BOTTOM_RIGHT)
                else -> sendCommand(KeyCommands.HOME)
            }
        }
    }

    private fun sendCommand(command: KeyCommands) {
        keyCommandListener?.onKeyCommand(command)
    }

    private fun View.applyScaleOnTouch(): View {
        setOnTouchListener { v, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).start()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.context.vibrate()
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(50).start()
                    v.performClick()
                    true
                }
                else -> false
            }
        }
        return this
    }

    private fun View.applyScaleOnTouch(nav: (Set<KeyCommands>) -> Unit) {
        setOnTouchListener { v, motionEvent ->
            val actions = mutableSetOf<KeyCommands>()
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val viewWidth = v.width
                    val viewHeight = v.height

                    val touchX = motionEvent.x
                    val touchY = motionEvent.y

                    if (touchX < viewWidth * 0.25) actions.add(KeyCommands.LEFT)
                    if (touchX > viewWidth * 0.75) actions.add(KeyCommands.RIGHT)
                    if (touchY < viewHeight * 0.25) actions.add(KeyCommands.TOP)
                    if (touchY > viewHeight * 0.75) actions.add(KeyCommands.BOTTOM)

                    if (actions.isEmpty()) actions.add(KeyCommands.HOME)

                    nav.invoke(actions)

                    v.scaleX = 1.0f
                    v.scaleY = 1.0f
                    v.pivotX = viewWidth / 2f
                    v.pivotY = viewHeight / 2f

                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).start()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.context.vibrate()
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(50).start()
                    v.performClick()
                    true
                }
                else -> false
            }
        }
    }

}

