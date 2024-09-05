package com.newagedevs.sis_emu.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.newagedevs.sis_emu.R
import com.newagedevs.sis_emu.ui.viewmodel.MainViewModel
import com.newagedevs.sis_emu.ui.views.DisplayView
import com.newagedevs.sis_emu.ui.views.KeyCommandListener
import com.newagedevs.sis_emu.ui.views.KeyCommands
import com.newagedevs.sis_emu.ui.views.KeyControlsView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var displayView: DisplayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        displayView = this.findViewById(R.id.display_view)


        val keyControlView = this.findViewById<KeyControlsView>(R.id.key_control_view)
        keyControlView.keyCommandListener = object : KeyCommandListener {
            override fun onKeyCommand(command: KeyCommands) {
//                viewModel.setToast(command.toString())

                displayView.showToast(command.toString())


                when (command) {
                    KeyCommands.OPTION, KeyCommands.MENU -> {
                        displayView.hideStatusBar()
                        displayView.hideNavigationBar()
                        displayView.navigateTo(R.id.nav_menu)
                    }
                    KeyCommands.CALL -> { }
                    KeyCommands.CANCEL, KeyCommands.EXIT -> { displayView.navigateUp() }
                    KeyCommands.HOME -> {
                        displayView.showStatusBar()
                        displayView.showNavigationBar()
                    }
                    else ->  { }
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return displayView.navigateUp() || super.onSupportNavigateUp()
    }

}