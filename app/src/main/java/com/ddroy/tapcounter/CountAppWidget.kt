package com.ddroy.tapcounter

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.preference.PreferenceManager

/**
 * Implementation of App Widget functionality.
 */
class CountAppWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_ADD = "com.ddroy.tapcounter.ADD_COUNTER"
        const val ACTION_SUBTRACT = "com.ddroy.tapcounter.SUBTRACT_COUNTER"
        const val ACTION_RESET = "com.ddroy.tapcounter.RESET_COUNTER"

        const val PREF_NAME = "count"
        fun getCounter(context: Context): Int {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getInt(PREF_NAME, 0)
        }

        fun resetCounter(context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit().putInt(PREF_NAME, 0).apply()
        }

        fun setCounter(context: Context, value: Int) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit().putInt(PREF_NAME, value).apply()
        }

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.count_app_widget)

            //set counter value
            views.setTextViewText(R.id.tvCount, getCounter(context).toString())


            //for add ACTION
            val add = Intent(context, CountAppWidget::class.java).apply {
                action = ACTION_ADD
            }
            val addPendingIntent = PendingIntent.getBroadcast(
                context, 1, add, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            //for subtract ACTION
            val subtract = Intent(context, CountAppWidget::class.java).apply {
                action = ACTION_SUBTRACT
            }
            val subtractPendingIntent = PendingIntent.getBroadcast(
                context,
                2,
                subtract,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            //for reset ACTION
            val resetIntent = Intent(context, CountAppWidget::class.java).apply {
                action = ACTION_RESET
            }
            val resetPendingIntent = PendingIntent.getBroadcast(
                context,
                3,
                resetIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val mainIntent = Intent(context, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            val mainPendingIntent = PendingIntent.getActivity(
                context,
                4,
                mainIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            views.setOnClickPendingIntent(R.id.btnReset, resetPendingIntent)
            views.setOnClickPendingIntent(R.id.btnMinus, subtractPendingIntent)
            views.setOnClickPendingIntent(R.id.tvCount, addPendingIntent)
            views.setOnClickPendingIntent(R.id.btnHistory, mainPendingIntent)


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val current = getCounter(context)
        when (intent.action) {
            ACTION_ADD -> setCounter(context, current + 1)
            ACTION_SUBTRACT -> setCounter(context, current - 1)
            ACTION_RESET -> resetCounter(context)
        }

        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, CountAppWidget::class.java))
        ids.forEach { updateAppWidget(context, manager, it) }

    }
}

