package org.smartregister.ondoganci.service.intent.path;

import android.content.Intent;

import org.smartregister.ondoganci.receiver.VaccinatorAlarmReceiver;
import org.smartregister.stock.sync.StockSyncIntentService;

/**
 * Created by keyman on 4/16/2018.
 */

public class PathStockSyncIntentService extends StockSyncIntentService {

    @Override
    protected void onHandleIntent(Intent workIntent) {
        super.onHandleIntent(workIntent);

        VaccinatorAlarmReceiver.completeWakefulIntent(workIntent);
    }
}
