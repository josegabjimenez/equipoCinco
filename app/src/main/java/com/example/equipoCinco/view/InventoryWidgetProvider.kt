package com.example.equipoCinco.view

import android.app.PendingIntent
import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.equipoCinco.R


class InventoryWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        //Obtener estado del inicio de sesion
        val sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val isUserLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
        //Configuracion de la Activity a abrir
        val targetActivityClass = if(isUserLoggedIn) MainActivity::class.java else LoginActivity::class.java
        val intent = Intent (context, targetActivityClass)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget)

            views.setOnClickPendingIntent(R.id.ivSettings, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}