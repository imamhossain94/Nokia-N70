package com.newagedevs.sis_emu

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.newagedevs.sis_emu.extensions.NavAction
import com.newagedevs.sis_emu.extensions.applyScaleOnTouch

class MainActivity : AppCompatActivity() {

    private lateinit var btnOption: ImageView
    private lateinit var btnCall: ImageView
    private lateinit var btnCancel: ImageView
    private lateinit var btnExit: ImageView

    private lateinit var btnNavigation: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        btnOption = this.findViewById(R.id.btn_option)
        btnCall = this.findViewById(R.id.btn_call)
        btnCancel = this.findViewById(R.id.btn_cancel)
        btnExit = this.findViewById(R.id.btn_exit)

        btnNavigation = this.findViewById(R.id.btn_navigation)

        btnOption.applyScaleOnTouch()
        btnCall.applyScaleOnTouch()
        btnCancel.applyScaleOnTouch()
        btnExit.applyScaleOnTouch()


        btnOption.setOnClickListener {
            Toast.makeText(this, "Option", Toast.LENGTH_SHORT).show()
        }

        btnCall.setOnClickListener {
            Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show()
        }

        btnCancel.setOnClickListener {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }

        btnExit.setOnClickListener {
            Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
        }


        btnNavigation.applyScaleOnTouch { actions ->
            Toast.makeText(this, actions.toString(), Toast.LENGTH_SHORT).show()
            if (NavAction.LEFT in actions) {

            }
            if (NavAction.RIGHT in actions) {
                // Handle right side touch
            }
            if (NavAction.TOP in actions) {
                // Handle top side touch
            }
            if (NavAction.BOTTOM in actions) {
                // Handle bottom side touch
            }
        }

    }



}