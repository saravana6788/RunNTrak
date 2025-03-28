package com.skcodes.run.presentation.active_run.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.compose.material.icons.Icons
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.skcodes.presentation.ui.formatted
import com.skcodes.run.domain.RunningTracker
import com.skcodes.run.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.internal.notify
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class ActiveRunService():Service() {

    val notificationManager by lazy {
        applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }



    val baseNotification by lazy{
        NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setSmallIcon(com.skcodes.presentation.designsystem.R.drawable.logo)
            .setContentTitle(getString(R.string.active_run))
    }

    private val runningTracker by inject<RunningTracker>()

    private var serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun start(activityClass:Class<*>){

        if(!isServiceActive){
            isServiceActive = true
            createNotificationChannel()

        val activityIntent = Intent(applicationContext,activityClass).apply {
            data = "runntrak://active_run".toUri()
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0,PendingIntent.FLAG_IMMUTABLE)

            }

            val notificaition = baseNotification
                .setContentText("00:00:00")
                .setContentIntent(pendingIntent)
                .build()

            startForeground(1,notificaition)
            updateNotification()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            ACTIVE_START ->{

                val activityClassName = intent.extras?.getString(EXTRA_ACTIVITY_CLASS) ?:
                throw IllegalArgumentException("No Activity class provided..")
                start(Class.forName(activityClassName))
            }

            ACTIVE_STOP ->{
                stop()
            }
        }

        return START_STICKY

    }

    private fun updateNotification(){
        runningTracker.elapsedTime
            .onEach {elapsedTime ->
                val notification = baseNotification
                    .setContentText(elapsedTime.formatted())
                    .build()
                notificationManager.notify(1,notification)
            }.launchIn(serviceScope)

    }

    private fun stop(){
        stopSelf()
        isServiceActive = false
        serviceScope.cancel()
        serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= 26){
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.active_run),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object{
        var isServiceActive = false
        private const val CHANNEL_ID = "ACTIVE_RUN_NOTIFICATION_CHANNEL"
        private const val ACTIVE_START = "ACTIVE_START"
        private const val ACTIVE_STOP = "ACTIVE_STOP"
        private const val EXTRA_ACTIVITY_CLASS = "EXTRA_ACTIVITY_CLASS"

        fun createStartServiceCommand(context: Context, activityClass: Class<*>):Intent{
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTIVE_START
                putExtra(EXTRA_ACTIVITY_CLASS, activityClass.name)
            }
        }

        fun createStopServiceCommand(context: Context): Intent {
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTIVE_STOP

            }
        }
    }

}