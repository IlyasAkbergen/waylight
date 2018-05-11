package com.backendless.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class AlarmService extends IntentService {

    public AlarmService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("MyTestService", "Service running");
    }
}