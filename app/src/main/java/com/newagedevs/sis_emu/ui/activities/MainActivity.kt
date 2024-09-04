package com.newagedevs.sis_emu.ui.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.newagedevs.sis_emu.R
import com.newagedevs.sis_emu.extensions.NavAction
import com.newagedevs.sis_emu.extensions.applyScaleOnTouch
import com.newagedevs.sis_emu.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

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
            viewModel.setToast("Option")
        }

        btnCall.setOnClickListener {
            viewModel.setToast("Call")
        }

        btnCancel.setOnClickListener {
            viewModel.setToast("Cancel")
        }

        btnExit.setOnClickListener {
            viewModel.setToast("Exit")
        }


        btnNavigation.applyScaleOnTouch { actions ->
            viewModel.setToast(actions.toString())
            if (NavAction.HOME in actions) {

            }
            if (NavAction.LEFT in actions) {

            }
            if (NavAction.RIGHT in actions) {

            }
            if (NavAction.TOP in actions) {

            }
            if (NavAction.BOTTOM in actions) {

            }
        }

    }



}