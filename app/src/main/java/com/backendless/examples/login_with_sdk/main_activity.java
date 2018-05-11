package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RelativeLayout;

import com.backendless.Backendless;
import com.backendless.service.MyAlarmReceiver;

public class main_activity extends Activity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        RelativeLayout lay = (RelativeLayout)findViewById(R.id.activity_form);
        lay.setBackgroundResource(R.drawable.background);
        Backendless.initApp(this, getString( R.string.backendless_AppId), getString( R.string.backendless_ApiKey));

        if(Backendless.UserService.loggedInUser() == ""){
            Intent intent = new Intent(main_activity.this, choose_social_network_activity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(main_activity.this, form_activity.class);
            startActivity(intent);
        }
    }
}
