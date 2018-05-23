package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class form_activity extends Activity {

    EditText pointa, pointb;
    String url, ticketClass = "";
    private int mYearD,mMonthD, mDayD, mYearR, mMonthR, mDayR;
    RequestQueue requestQueue;
    private Button searchBtn;
    private Button logout;
    private Button goToSavedRequests;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        RelativeLayout lay = (RelativeLayout)findViewById(R.id.activity_form);
        lay.setBackgroundResource(R.drawable.background);
        this.pointa = (EditText) findViewById(R.id.pointa);
        this.pointb = (EditText) findViewById(R.id.pointb);
        this.searchBtn = (Button) findViewById(R.id.searchBtn);
        this.logout = (Button) findViewById(R.id.logout);
        this.goToSavedRequests = (Button) findViewById(R.id.go_to_saved_requests);
        final Button pickDepartDate = (Button) findViewById(R.id.departDate);
        final Button pickReturnDate = (Button) findViewById(R.id.returnDate);
        final TextView departdate = (TextView) findViewById(R.id.departdate);
        final TextView returndate = (TextView) findViewById(R.id.returndate);
        final Calendar myCalendar = Calendar.getInstance();

        if(Backendless.UserService.loggedInUser() == ""){
            logout.setVisibility(View.GONE);
            goToSavedRequests.setVisibility(View.GONE);
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "yyyy-MM-dd HH:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                departdate.setText(sdf.format(myCalendar.getTime()));
            }


        };

        goToSavedRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), saved_requests.class);
                intent.putExtra("ownerId", Backendless.UserService.loggedInUser());
                startActivity(intent);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "pointa=" + pointa.getText().toString() + "&pointb=" + pointb.getText().toString(); //data to post
                if(ticketClass != ""){
                    data += "&class=" + ticketClass;
                }

                if(!departdate.getText().toString().equals("")){
                    data += "&depart=" + departdate.getText().toString();
                }


                if(!returndate.getText().toString().equals("")){
                    data += "&return=" + returndate.getText().toString();
                }

                Intent intent = new Intent(getBaseContext(), results_activity.class);
                intent.putExtra("data", data);
                intent.putExtra("pointa", pointa.getText().toString());
                intent.putExtra("pointb", pointb.getText().toString());
                intent.putExtra("user_id", Backendless.UserService.loggedInUser());
                intent.putExtra("showDeleteBtn", "0");
                intent.putExtra("departdate", departdate.getText().toString());
                intent.putExtra("returndate", returndate.getText().toString());

                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        pickDepartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYearD = c.get(Calendar.YEAR);
                mMonthD = c.get(Calendar.MONTH);
                mDayD = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(form_activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYearD)
                                    view.updateDate(mYearD,mMonthD,mDayD);

                                if (monthOfYear < mMonthD && year == mYearD)
                                    view.updateDate(mYearD,mMonthD,mDayD);

                                if (dayOfMonth < mDayD && year == mYearD && monthOfYear == mMonthD)
                                    view.updateDate(mYearD,mMonthD,mDayD);

                                departdate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth + " 00:00:00");

                            }
                        }, mYearD, mMonthD, mDayD);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });

        pickReturnDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYearR = c.get(Calendar.YEAR);
                mMonthR = c.get(Calendar.MONTH);
                mDayR = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(form_activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYearR)
                                    view.updateDate(mYearR,mMonthR,mDayR);

                                if (monthOfYear < mMonthR && year == mYearR)
                                    view.updateDate(mYearR,mMonthR,mDayR);

                                if (dayOfMonth < mDayR && year == mYearR && monthOfYear == mMonthR)
                                    view.updateDate(mYearR,mMonthR,mDayR);

                                returndate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth + " 00:00:00");

                            }
                        }, mYearR, mMonthR, mDayR);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_business:
                if (checked)
                    this.ticketClass = "business";
                    break;
            case R.id.radio_econom:
                if (checked)
                    this.ticketClass = "econom";
                    break;
        }
    }
}