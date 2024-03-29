package org.smartregister.ondoganci.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import org.smartregister.ondoganci.service.intent.CoverageDropoutIntentService;
import org.smartregister.ondoganci.service.intent.HIA2IntentService;
import org.smartregister.ondoganci.service.intent.PullUniqueIdsIntentService;
import org.smartregister.sync.intent.SyncIntentService;
import org.smartregister.ondoganci.service.intent.path.PathImageUploadSyncService;
import org.smartregister.ondoganci.service.intent.path.PathRecurringIntentService;
import org.smartregister.ondoganci.service.intent.path.PathVaccineIntentService;
import org.smartregister.ondoganci.service.intent.path.PathWeightIntentService;
import org.smartregister.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.smartregister.ondoganci.util.AppConstants;

public class VaccinatorAlarmReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = VaccinatorAlarmReceiver.class.getCanonicalName();

    private static final String serviceActionName = "org.smartregister.path.action.START_SERVICE_ACTION";
    private static final String serviceTypeName = "serviceType";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        int serviceType = alarmIntent.getIntExtra(serviceTypeName, 0);
        Intent serviceIntent = null;
        switch (serviceType) {
            case AppConstants.ServiceType.AUTO_SYNC:
                android.util.Log.i(TAG, "Started AUTO_SYNC service at: " + dateFormatter.format(new Date()));
                serviceIntent = new Intent(context, SyncIntentService.class);
//                serviceIntent.putExtra(SyncIntentService.WAKE_UP, true);
                break;
            case AppConstants.ServiceType.DAILY_TALLIES_GENERATION:
                android.util.Log.i(TAG, "Started DAILY_TALLIES_GENERATION service at: " + dateFormatter.format(new Date()));
                serviceIntent = new Intent(context, HIA2IntentService.class);
                break;
            case AppConstants.ServiceType.PULL_UNIQUE_IDS:
                serviceIntent = new Intent(context, PullUniqueIdsIntentService.class);
                android.util.Log.i(TAG, "Started PULL_UNIQUE_IDS service at: " + dateFormatter.format(new Date()));
                break;
            case AppConstants.ServiceType.WEIGHT_SYNC_PROCESSING:
                serviceIntent = new Intent(context, PathWeightIntentService.class);
                android.util.Log.i(TAG, "Started WEIGHT_SYNC_PROCESSING service at: " + dateFormatter.format(new Date()));
                break;
            case AppConstants.ServiceType.VACCINE_SYNC_PROCESSING:
                serviceIntent = new Intent(context, PathVaccineIntentService.class);
                android.util.Log.i(TAG, "Started VACCINE_SYNC_PROCESSING service at: " + dateFormatter.format(new Date()));
                break;
            case AppConstants.ServiceType.RECURRING_SERVICES_SYNC_PROCESSING:
                serviceIntent = new Intent(context, PathRecurringIntentService.class);
                android.util.Log.i(TAG, "Started RECURRING_SERVICES_SYNC_PROCESSING service at: " + dateFormatter.format(new Date()));
                break;
            case AppConstants.ServiceType.IMAGE_UPLOAD:
                serviceIntent = new Intent(context, PathImageUploadSyncService.class);
                android.util.Log.i(TAG, "Started IMAGE_UPLOAD_SYNC service at: " + dateFormatter.format(new Date()));
                break;
            case AppConstants.ServiceType.COVERAGE_DROPOUT_GENERATION:
                serviceIntent = new Intent(context, CoverageDropoutIntentService.class);
                android.util.Log.i(TAG, "Started COVERAGE_DROPOUT_GENERATION service at: " + dateFormatter.format(new Date()));
                break;
            default:
                break;
        }

        if (serviceIntent != null)
            this.startService(context, serviceIntent);


    }

    private void startService(Context context, Intent serviceIntent) {
        startWakefulService(context, serviceIntent);
    }

    /**
     * @param context
     * @param triggerIteration in minutes
     * @param taskType         a constant from pathconstants denoting the service type
     */
    public static void setAlarm(Context context, long triggerIteration, int taskType) {
        try {
            AlarmManager alarmManager;
            PendingIntent alarmIntent;

            long triggerAt;
            long triggerInterval;
            if (context == null) {
                throw new Exception("Unable to schedule service without app context");
            }

            // Otherwise schedule based on normal interval
            triggerInterval = TimeUnit.MINUTES.toMillis(triggerIteration);
            // set trigger time to be current device time + the interval (frequency). Probably randomize this a bit so that services not launch at exactly the same time
            triggerAt = System.currentTimeMillis() + triggerInterval;

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmReceiverIntent = new Intent(context, VaccinatorAlarmReceiver.class);

            alarmReceiverIntent.setAction(serviceActionName + taskType);
            alarmReceiverIntent.putExtra(serviceTypeName, taskType);
            alarmIntent = PendingIntent.getBroadcast(context, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                alarmManager.cancel(alarmIntent);
            } catch (Exception e) {
                Log.logError(TAG, e.getMessage());
            }
            //Elapsed real time uses the "time since system boot" as a reference, and real time clock uses UTC (wall clock) time
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAt, triggerInterval, alarmIntent);
        } catch (Exception e) {
            Log.logError(TAG, "Error in setting service Alarm " + e.getMessage());
        }

    }

}
