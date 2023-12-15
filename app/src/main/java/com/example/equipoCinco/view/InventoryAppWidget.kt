package com.appmovil.loginfirestore.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.example.equipoCinco.R
import com.example.equipoCinco.view.LoginActivity
import com.example.equipoCinco.view.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale

/**
 * Implementation of App Widget functionality.
 */
class InventoryAppWidget : AppWidgetProvider() {
    companion object {
        var saldoVisible: Boolean = false
        var totalSum = 0
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

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent!!.action?: "") {
            "VIEW_EARNINGS_ACTION" -> {
                if (isUserLoggedIn()) {
                    updateSaldoWidget(context!!)
                } else {
                    val loginIntent = Intent(context, LoginActivity::class.java)
                    context?.sendBroadcast(loginIntent)
                    loginIntent.putExtra("fromWidget", "true")
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(loginIntent)
                }
            }
            "ENTER_TO_INVENTORY_ACTION" -> {
                calculateTotalEarnings()
                if (isUserLoggedIn()) {
                    val homeIntent = Intent(context, MainActivity::class.java)
                    context?.sendBroadcast(homeIntent)
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(homeIntent)
                } else {
                    val loginIntent = Intent(context, LoginActivity::class.java)
                    context?.sendBroadcast(loginIntent)
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(loginIntent)
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        calculateTotalEarnings()
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        calculateTotalEarnings()
    }

    private fun calculateTotalEarnings() {
        // Reset accumulated value
        totalSum = 0

        // Fetch data from Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("inventory").get()
            .addOnSuccessListener { querySnapshot ->

                for (document in querySnapshot.documents) {
                    val price = document.getLong("price") ?: 0
                    val quantity = document.getLong("quantity") ?: 0

                    val productTotal = price.toInt() * quantity.toInt()
                    totalSum += productTotal
                }

            }
            .addOnFailureListener { exception ->
                // Handle failures
                println("Error: $exception")
            }
    }

    private fun isUserLoggedIn(): Boolean {
        // Verify if the user is authenticated in Firebase
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser != null
    }

    private fun formatPrice(price: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale("es", "ES"))
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        return numberFormat.format(price)
    }

    private fun updateSaldoWidget(context: Context) {
        // Cambiar el contenido del TextView text_saldo
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(context.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
        val views = RemoteViews(context.packageName, R.layout.inventory_app_widget)
        if (saldoVisible){
            views.setImageViewResource(R.id.ivShow_inventory, R.drawable.ojo_alternancia_1)
            views.setTextViewText(R.id.tvEarnings, "$ * * * *")
            saldoVisible = false
        }else{

            val formattedTotalSum = formatPrice(totalSum.toDouble())

            views.setImageViewResource(R.id.ivShow_inventory, R.drawable.ojo_alternancia_2)
            views.setTextViewText(R.id.tvEarnings, "$ $formattedTotalSum")
            saldoVisible = true
        }

        // Update widget
        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }
    private fun pendingIntent(
        context: Context?,
        action:String
    ): PendingIntent? {
        val intent = Intent(context, javaClass)
        intent.action = action

        return PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.inventory_app_widget)

        // Action to view the total earnings
        views.setOnClickPendingIntent(R.id.ivShow_inventory, pendingIntent(context, "VIEW_EARNINGS_ACTION"))

        // Action to enter to the inventory
        views.setOnClickPendingIntent(R.id.ivSettings, pendingIntent(context, "ENTER_TO_INVENTORY_ACTION"))

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}