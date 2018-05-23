package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class save_request_activity extends Activity{
    SharedPreferences sPref;
    String[] requests;
    Button saveBtn;
    String data, pointa, pointb, user_id;
    String departdate, returndate;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_request);
        RelativeLayout lay = (RelativeLayout)findViewById(R.id.save_request);
        lay.setBackgroundResource(R.drawable.background);
        this.saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                 data = getIntent().getStringExtra("data");
                 pointa = getIntent().getStringExtra("pointa");
                 pointb = getIntent().getStringExtra("pointb");
                 departdate = getIntent().getStringExtra("departdate");
                 returndate = getIntent().getStringExtra("returndate");
//                 try {
//                     departdate = formatter.parse(getIntent().getStringExtra("departdate"));
//                 } catch (ParseException e) {
//                     e.printStackTrace();
//                 }
//
//                 try {
//                     returndate = formatter.parse(getIntent().getStringExtra("returndate"));
//                 } catch (ParseException e) {
//                     e.printStackTrace();
//                 }
//
//                 String myFormat = "yyyy-MM-dd HH:mm:ss"; //In which you need put here
//                 departdate.setText(sdf.format(myCalendar.getTime()));

//                 String string = "January 2, 2010";
//                 DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
//                 Date date = format.parse(string);

                 Request request = new Request();
                 request.setPointa( pointa );
                 request.setPointb( pointb );
                 request.setDepartdate(departdate);
                 request.setReturndate(returndate);
                 //Intent intent = getIntent();
                 request.setRequest_url( data );

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
                         Toast.makeText(getBaseContext(), "Error: ", Toast.LENGTH_SHORT ).show();
                         System.out.println(" ***  " + fault);
                         // an error has occurred, the error code can be retrieved with fault.getCode()
                     }
                 });

                 finish();
             }
        });
    }
}
