package uk.co.droidinactu.nanowrimo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.Calendar;

import uk.co.droidinactu.nanowrimo.db.DataManager;

/**
 * Implementation of App Widget functionality.
 */
public class NaNoMonthWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sp = context.getSharedPreferences(DataManager.PREFS_NAME, Context.MODE_PRIVATE);
        final int wordcountTrgt = sp.getInt("pref_wordcount_target", 50000);

        final int monthNbr = sp.getInt("pref_appwidget_" + appWidgetId + "_month", 11);
        final int yearNbr = sp.getInt("pref_appwidget_" + appWidgetId + "_year", Calendar.getInstance().get(Calendar.YEAR));

        final int currntWrdCount = NaNoApplication.getInstance().getDataManager().getCurrentTotal(monthNbr, yearNbr);

        CharSequence widgetTextName = context.getString(R.string.appwidget_text_name);
        CharSequence widgetTextNovel = context.getString(R.string.appwidget_text_novel);
        CharSequence widgetTextProgress = context.getString(R.string.appwidget_text_progress);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.nano_month_widget);
        views.setTextViewText(R.id.appwidget_text_name, widgetTextName);
        views.setTextViewText(R.id.appwidget_text_novel, widgetTextNovel);
        views.setTextViewText(R.id.appwidget_progress_text, widgetTextProgress);
        views.setProgressBar(R.id.appwidget_progress, wordcountTrgt, currntWrdCount, false);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, Dashboard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.nano_month_widget);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
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

