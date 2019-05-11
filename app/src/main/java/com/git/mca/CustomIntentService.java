package com.git.mca;

import android.app.IntentService;
import android.content.Intent;

public class CustomIntentService extends IntentService {

    public CustomIntentService() {
        super("CustomIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do some long running background task

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("TASK_COMPLETED");
        getBaseContext().sendBroadcast(broadcastIntent);
    }
}
