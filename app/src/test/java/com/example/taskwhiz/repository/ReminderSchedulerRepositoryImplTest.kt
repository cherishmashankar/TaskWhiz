package com.example.taskwhiz.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.taskwhiz.data.reminder.TaskReminderReceiver
import com.example.taskwhiz.data.repository.ReminderSchedulerRepositoryImpl
import com.example.taskwhiz.utils.Constants
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ReminderSchedulerRepositoryImplTest {

    private val context: Context = RuntimeEnvironment.getApplication()

    private fun repository(): ReminderSchedulerRepositoryImpl {
        return ReminderSchedulerRepositoryImpl(context)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.R])
    fun schedule_schedulesAlarmCorrectly() {
        val repository = repository()

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val shadowAlarmManager = Shadows.shadowOf(alarmManager)

        repository.schedule(
            taskId = 1L,
            title = "Test Reminder",
            timeInMs = 5000L
        )

        val scheduledAlarm = shadowAlarmManager.nextScheduledAlarm

        assertThat(scheduledAlarm).isNotNull()
        assertThat(scheduledAlarm.type)
            .isEqualTo(AlarmManager.RTC_WAKEUP)

        assertThat(scheduledAlarm.triggerAtMs)
            .isEqualTo(5000L)
    }

    @Test
    fun cancel_removesScheduledAlarm() {
        val repository = repository()

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val shadowAlarmManager = Shadows.shadowOf(alarmManager)

        repository.schedule(
            taskId = 2L,
            title = "Cancel Reminder",
            timeInMs = 10000L
        )

        assertThat(shadowAlarmManager.scheduledAlarms)
            .hasSize(1)

        repository.cancel(2L)

        assertThat(shadowAlarmManager.scheduledAlarms)
            .isEmpty()
    }

    @Test
    fun schedule_createsPendingIntentWithCorrectExtras() {
        val repository = repository()

        repository.schedule(
            taskId = 3L,
            title = "Reminder Title",
            timeInMs = 15000L
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            3L.hashCode(),
            Intent(context, TaskReminderReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val shadowPendingIntent = Shadows.shadowOf(pendingIntent)

        val savedIntent = shadowPendingIntent.savedIntent

        assertThat(
            savedIntent.getLongExtra(
                Constants.EXTRA_TASK_ID,
                -1L
            )
        ).isEqualTo(3L)

        assertThat(
            savedIntent.getStringExtra(
                Constants.EXTRA_TASK_TITLE
            )
        ).isEqualTo("Reminder Title")
    }
}