package com.example.lastproject5

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

private var currentIndex = 0
/**
 * Implementation of App Widget functionality.
 */
class SearchAndImage : AppWidgetProvider() {
    private val swapAction = "com.example.LastProject5.SWAP"
    private val searchAction = "com.example.LastProject5.SEARCH"

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

    @SuppressLint("QueryPermissionsNeeded")
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        Log.e("Intent", intent?.action.toString())
        Log.e("TESTTEST", "intent?.action.toString()")
        if (intent?.action == swapAction) {
            updateImageView(context!!)
            currentIndex += 1
        } else if (intent?.action == searchAction) {
            Log.e("SEARCHACTION", "SEARCH")

            // opens url that we want
            val urlToOpen = "https://www.google.com"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen))
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            // check if there is app to open browser
            if (browserIntent.resolveActivity(context?.packageManager!!) != null) {
                context.startActivity(browserIntent)
            } else {
                Log.e("SearchAndImage", "Brak aplikacji obsługującej przeglądarkę.")
            }
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
    views.setOnClickPendingIntent(R.id.swapImage, getPendingSelfIntentSwap(context))
    views.setOnClickPendingIntent(R.id.searchButton, getPendingSelfIntentSearch(context))
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun getPendingSelfIntentSwap(context: Context): PendingIntent {
    val intent = Intent(context, SearchAndImage::class.java)
    intent.action = "com.example.LastProject5.SWAP"

    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
}

private fun getPendingSelfIntentSearch(context: Context): PendingIntent {
    val intent = Intent(context, SearchAndImage::class.java)
    intent.action = "com.example.LastProject5.SEARCH"

    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
}