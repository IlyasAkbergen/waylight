package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

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
            try{
                Backendless.Messaging.registerDevice("308381610368", "default", new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText(getBaseContext(),"Registered", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getBaseContext(),"fault: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(main_activity.this, form_activity.class);
                startActivity(intent);
            }catch (Exception e){

                Toast.makeText(getBaseContext(),"fault: "+ e, Toast.LENGTH_SHORT).show();

                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Intent intent = new Intent(getBaseContext(), main_activity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getBaseContext(), "Logout Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}
