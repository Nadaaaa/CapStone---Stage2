package com.example.nada.devhires.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.nada.devhires.R;
import com.example.nada.devhires.activities.MainActivity;
import com.example.nada.devhires.models.User;

/**
 * Implementation of App Widget functionality.
 */
public class InfoAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        User user = MainActivity.getDataFromSharedPrefs(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.info_app_widget);
        views.setTextViewText(R.id.appwidget_name, user.getUsername());
        views.setTextViewText(R.id.appwidget_title, user.getJobTitle());
        views.setTextViewText(R.id.appwidget_company, user.getCompany());
        views.setTextViewText(R.id.appwidget_phone, user.getPhone());
        views.setTextViewText(R.id.appwidget_email, user.getEmail());
        views.setTextViewText(R.id.appwidget_maritalStatus, user.getMaritalStatus());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

