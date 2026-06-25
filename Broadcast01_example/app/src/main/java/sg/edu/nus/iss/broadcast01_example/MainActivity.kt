package sg.edu.nus.iss.broadcast01_example

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "airplane_wifi_channel"
        const val BATTERY_CHANNEL_ID = "battery_channel"
    }

    private val receiver = NetworkBroadcastReceiver()
    private val batteryReceiver = BatteryBroadcastReceiver()

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val status = if (granted) "Ready — listening for broadcasts" else "Notification permission denied"
        findViewById<TextView>(R.id.tvStatus).text = status
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        createBatteryNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        }
        registerReceiver(receiver, filter)

        val batteryFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
        }
        registerReceiver(batteryReceiver, batteryFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
        unregisterReceiver(batteryReceiver)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Airplane & WiFi Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifies when airplane mode changes"
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun createBatteryNotificationChannel() {
        val channel = NotificationChannel(
            BATTERY_CHANNEL_ID,
            "Battery Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies when battery is low or recovered"
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
