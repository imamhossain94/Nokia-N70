package com.newagedevs.sis_emu.ui.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.newagedevs.sis_emu.R

class DisplayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var toastRunnable: Runnable? = null

    private val statusBarView: StatusBarView
    private val navigationBarView: NavigationBarView
    private val toastTextView: TextView
    private val fragmentContainer: FragmentContainerView
    private val handler = Handler(Looper.getMainLooper())
    private val navController: NavController

    init {
        inflate(context, R.layout.view_display, this)
        statusBarView = findViewById(R.id.status_bar_view)
        navigationBarView = findViewById(R.id.navigation_bar_view)
        toastTextView = findViewById(R.id.toast)
        fragmentContainer = findViewById(R.id.nav_host_fragment)

        // Initialize NavController
        val navHostFragment = (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun showStatusBar() {
        statusBarView.show()
    }

    fun hideStatusBar() {
        statusBarView.hide()
    }

    fun showNavigationBar() {
        navigationBarView.show()
    }

    fun hideNavigationBar() {
        navigationBarView.hide()
    }

    fun showToast(message: String, duration: Long = 1000) {
        toastTextView.text = message
        toastTextView.visibility = View.VISIBLE
        toastRunnable?.let {
            handler.removeCallbacks(it)
        }
        toastRunnable = Runnable { hideToast() }
        toastRunnable?.let {
            handler.postDelayed(it, duration)
        }
    }

    private fun hideToast() {
        toastTextView.visibility = View.GONE
    }

    fun currentDestinationId(): Int {
        return navController.currentDestination?.id ?: 0
    }

    fun navigateTo(destinationId: Int, args: Bundle? = null) {
        if(currentDestinationId() != destinationId) navController.navigate(destinationId, args)
    }

    fun navigateUp(clearStack:Boolean = false): Boolean {
        return if(clearStack){
            navController.popBackStack(navController.graph.startDestinationId, false)
        } else {
            navController.navigateUp()
        }
    }


}
