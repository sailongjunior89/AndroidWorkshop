package sg.edu.nus.iss.broadcast01_example

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat

class BatteryBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> {
                Toast.makeText(context, "Battery low!", Toast.LENGTH_LONG).show()
                sendNotification(context, "Battery Low", "Please charge your device soon.")
            }
            Intent.ACTION_BATTERY_OKAY -> {
                Toast.makeText(context, "Battery level OK", Toast.LENGTH_SHORT).show()
                sendNotification(context, "Battery OK", "Battery level has recovered.")
            }
        }
    }

    private fun sendNotification(context: Context, title: String, message: String) {
        val notification = NotificationCompat.Builder(context, MainActivity.BATTERY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1002
    }
}
