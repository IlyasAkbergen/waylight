package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.service.MyAlarmReceiver;

import java.util.List;

public class saved_requests extends Activity {
    public String[] requests = {"empty"};
    private ListView lvRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_requests);
        LinearLayout lay = (LinearLayout)findViewById(R.id.saved_requests);
        lay.setBackgroundResource(R.drawable.background);
        lvRequests = (ListView) findViewById(R.id.lv_saved_requests);

        // make array requests
        Backendless.initApp(this, getString( R.string.backendless_AppId), getString( R.string.backendless_ApiKey));
        String whereClause = "ownerId = '" + Backendless.UserService.loggedInUser() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause( whereClause );

        Backendless.Persistence.of( Request.class ).find( queryBuilder, new AsyncCallback<List<Request>>(){
            public void handleResponse( List<Request> list ){
                if(list.size() > 0 ){
                    requests = new String[list.size()];
                    for (int i=0; i<list.size(); i++) {
                        requests[i] = "From: "  + list.get(i).getPointa() + "  To: " + list.get(i).getPointb();
                    }

                    startAdapter();

                }else{
                    requests = new String[1];
                    requests[0] = "You have no saved request yet.";
                    startAdapter();
                }
            }

            public void handleFault( BackendlessFault fault )
            {
                requests = new String[1];
                requests[0] = "You have no saved request yet.";
                startAdapter();
            }
        });
    }

    public void startAdapter(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requests);

        lvRequests.setAdapter(adapter);
    }
}