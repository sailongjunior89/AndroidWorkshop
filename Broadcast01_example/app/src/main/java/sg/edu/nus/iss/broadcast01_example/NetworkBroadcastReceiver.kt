package sg.edu.nus.iss.broadcast01_example

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.widget.Toast
import androidx.core.app.NotificationCompat

class NetworkBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val isOn = intent.getBooleanExtra("state", false)
                val message = if (isOn) "Airplane mode turned ON" else "Airplane mode turned OFF"
                sendNotification(context, message)
            }
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN
                )
                when (state) {
                    WifiManager.WIFI_STATE_ENABLED ->
                        Toast.makeText(context, "WiFi switched ON", Toast.LENGTH_SHORT).show()
                    WifiManager.WIFI_STATE_DISABLED ->
                        Toast.makeText(context, "WiFi switched OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendNotification(context: Context, message: String) {
        val notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Airplane Mode Changed")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}
