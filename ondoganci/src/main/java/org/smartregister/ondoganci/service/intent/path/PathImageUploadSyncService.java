package org.smartregister.ondoganci.service.intent.path;

import android.content.Intent;

import org.smartregister.ondoganci.receiver.VaccinatorAlarmReceiver;
import org.smartregister.service.ImageUploadSyncService;

/**
 * Created by keyman on 4/16/2018.
 */

public class PathImageUploadSyncService extends ImageUploadSyncService {

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);

        VaccinatorAlarmReceiver.completeWakefulIntent(intent);
    }
}

