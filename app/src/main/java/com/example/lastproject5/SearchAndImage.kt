package com.example.lastproject5

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

private var currentIndex = 0
/**
 * Implementation of App Widget functionality.
 */
class SearchAndImage : AppWidgetProvider() {
    val swapAction = "com.example.LastProject5.SWAP"

    private val imageResources = intArrayOf(
        R.drawable.kot1,
        R.drawable.kot2,
        R.drawable.kot3
    )


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

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        Log.e("Intent", intent?.action.toString())
        Log.e("TESTTEST", "intent?.action.toString()")
        if (intent?.action == swapAction) {
            updateImageView(context!!)
            currentIndex += 1
        }

    }

    private fun updateImageView(context: Context) {
        if (currentIndex > 2) {
            currentIndex = 0
        }
        Log.d("MyWidgetProvider", currentIndex.toString())
        val views = RemoteViews(context.packageName, R.layout.search_and_image)
        views.setImageViewResource(R.id.imageView, imageResources[currentIndex])

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, SearchAndImage::class.java)
        )
        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.search_and_image)
    views.setOnClickPendingIntent(R.id.swapImage, getPendingSelfIntent(context))
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun getPendingSelfIntent(context: Context): PendingIntent {
    val intent = Intent(context, SearchAndImage::class.java)
    intent.action = "com.example.LastProject5.SWAP"

    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
}