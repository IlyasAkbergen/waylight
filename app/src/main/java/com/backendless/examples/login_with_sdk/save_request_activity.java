package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.Persistence;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.service.MyAlarmReceiver;

import java.util.Map;

public class save_request_activity extends Activity{
    SharedPreferences sPref;
    String[] requests;
    Button saveBtn;
    String data, pointa, pointb, user_id;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_request);
        RelativeLayout lay = (RelativeLayout)findViewById(R.id.save_request);
        lay.setBackgroundResource(R.drawable.background);
        this.saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 data = getIntent().getStringExtra("data");
                 pointa = getIntent().getStringExtra("pointa");
                 pointb = getIntent().getStringExtra("pointb");

                 Request request = new Request();
                 request.setPointa( pointa );
                 request.setPointb( pointb );
                 Intent intent = getIntent();
                 request.setRequest_url( intent.getStringExtra("data") );

                 // save object synchronously
                 Backendless.initApp(getBaseContext(), getString( R.string.backendless_AppId), getString( R.string.backendless_ApiKey));
                 //Request savedRequest = Backendless.Persistence.save( request );

                 // save object asynchronously
                 Backendless.Persistence.save( request, new AsyncCallback<Request>() {
                     public void handleResponse( Request response )
                     {
                         // new Contact instance has been saved
                         Toast.makeText(getBaseContext(), "Request saved.", Toast.LENGTH_SHORT ).show();
//                         scheduleAlarm();
                     }

                     public void handleFault( BackendlessFault fault )
                     {
                         Toast.makeText(getBaseContext(), "Error.", Toast.LENGTH_SHORT ).show();
                         // an error has occurred, the error code can be retrieved with fault.getCode()
                     }
                 });

                 finish();
             }
        });
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_HALF_HOUR, pIntent);
    }
}
