package com.newagedevs.sis_emu.ui.views

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextClock
import androidx.core.content.ContextCompat
import com.newagedevs.sis_emu.R

class StatusBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val mainHandler = Handler(Looper.getMainLooper())

    private val clockView: TextClock
    private val wifiView: ImageView
    private val signalView: ImageView
    private val batteryView: ImageView

    private val telephonyManager: TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val wifiManager: WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val wifiInfo = networkCapabilities.transportInfo as? WifiInfo
                val level = wifiInfo?.let { WifiManager.calculateSignalLevel(it.rssi, 5) } ?: 0
                updateWifiStatus(level)
            }
            else {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    val wifiInfo = wifiManager.connectionInfo
                    val level = WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
                    updateWifiStatus(level)
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            updateWifiStatus(0)
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_status_bar, this, true)
        clockView = findViewById(R.id.clock_view)
        wifiView = findViewById(R.id.wifi_view)
        signalView = findViewById(R.id.signal_view)
        batteryView = findViewById(R.id.battery_view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(
                context.mainExecutor,
                object : TelephonyCallback(), TelephonyCallback.SignalStrengthsListener {
                    override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                        val level = signalStrength.level
                        updateSignalStatus(level)
                    }
                })
        } else {
            @Suppress("DEPRECATION")
            telephonyManager.listen(object : PhoneStateListener() {
                @Deprecated("Deprecated in Java")
                override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                    super.onSignalStrengthsChanged(signalStrength)
                    val level = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        signalStrength.level
                    } else {
                        getDeprecatedSignalLevel(signalStrength)
                    }
                    updateSignalStatus(level)
                }
            }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        }

        updateWifiStatus(getCurrentWifiLevel())
        updateBatteryStatus(getCurrentBatteryLevel())
    }

    private fun getDeprecatedSignalLevel(signalStrength: SignalStrength): Int {
        val signalStrengths = signalStrength.gsmSignalStrength
        return when (signalStrengths) {
            in 0..7 -> 0
            in 8..14 -> 1
            in 15..19 -> 2
            else -> 3
        }
    }

    private fun registerNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun configureColors(backgroundColor: String, foregroundColor: String) {
        setBackgroundColor(Color.parseColor(backgroundColor))
        clockView.setTextColor(Color.parseColor(foregroundColor))
        wifiView.setColorFilter(Color.parseColor(foregroundColor))
        signalView.setColorFilter(Color.parseColor(foregroundColor))
        batteryView.setColorFilter(Color.parseColor(foregroundColor))
    }

    fun configureClockFormat(is24HourFormat: Boolean) {
        clockView.format12Hour = if (is24HourFormat) null else "hh:mm a"
        clockView.format24Hour = if (is24HourFormat) "HH:mm" else null
    }

    fun updateWifiStatus(level: Int) {
        mainHandler.post {
            wifiView.visibility = if(level == 0) View.INVISIBLE else View.VISIBLE
            val wifiDrawableId = when (level) {
                0 -> R.drawable.ic_wifi_0
                1 -> R.drawable.ic_wifi_1
                2 -> R.drawable.ic_wifi_2
                3 -> R.drawable.ic_wifi_3
                else -> R.drawable.ic_wifi_4
            }
            wifiView.setImageDrawable(ContextCompat.getDrawable(context, wifiDrawableId))
        }
    }

    fun updateSignalStatus(level: Int) {
        mainHandler.post {
            val signalDrawableId = when (level) {
                0 -> R.drawable.ic_cellular_signal_0
                1 -> R.drawable.ic_cellular_signal_1
                2 -> R.drawable.ic_cellular_signal_2
                3 -> R.drawable.ic_cellular_signal_3
                else -> R.drawable.ic_cellular_signal_4
            }
            signalView.setImageDrawable(ContextCompat.getDrawable(context, signalDrawableId))
        }
    }

    fun updateBatteryStatus(level: Int) {
        mainHandler.post {
            val batteryDrawableId = when (level) {
                in 0..10 -> R.drawable.ic_battery_0
                in 11..25 -> R.drawable.ic_battery_25
                in 26..50 -> R.drawable.ic_battery_50
                in 51..75 -> R.drawable.ic_battery_75
                else -> R.drawable.ic_battery_100
            }
            batteryView.setImageDrawable(ContextCompat.getDrawable(context, batteryDrawableId))
        }
    }

    fun show() {
        if(visibility != View.VISIBLE) {
            visibility = View.VISIBLE
            val animation = TranslateAnimation(0f, 0f, -height.toFloat(), 0f).apply {
                duration = 100
                fillAfter = true
            }
            startAnimation(animation)
        }
    }


    fun hide() {
        if(visibility != View.GONE) {
            val animation = TranslateAnimation(0f, 0f, 0f, -height.toFloat()).apply {
                duration = 100
                fillAfter = true
            }
            startAnimation(animation)
            visibility = View.GONE
        }

    }

    private fun getCurrentWifiLevel(): Int {
        val wifiInfo = wifiManager.connectionInfo
        return if (wifiInfo != null && wifiInfo.networkId != -1) {
            @Suppress("DEPRECATION")
            WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
        } else {
            0 // No Wi-Fi connection
        }
    }

    private fun getCurrentBatteryLevel(): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        registerNetworkCallback()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregisterNetworkCallback()
    }
}
