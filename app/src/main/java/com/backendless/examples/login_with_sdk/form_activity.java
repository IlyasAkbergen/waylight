package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class form_activity extends Activity {

    EditText pointa, pointb;

    String baseUrl = "http://localhost/waylight/public/api/tickets";
    String url, ticketClass;
    private int mYearD,mMonthD, mDayD, mYearR, mMonthR, mDayR;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        RelativeLayout lay = (RelativeLayout)findViewById(R.id.activity_form);
        lay.setBackgroundResource(R.drawable.background);
        this.pointa = (EditText) findViewById(R.id.pointa);
        this.pointb = (EditText) findViewById(R.id.pointb);
        final Button pickDepartDate = (Button) findViewById(R.id.departDate);
        final Button pickReturnDate = (Button) findViewById(R.id.returnDate);
        final TextView departdate = (TextView) findViewById(R.id.departdate);
        final TextView returndate = (TextView) findViewById(R.id.returndate);
        RequestQueue requestQueue;
        final Calendar myCalendar = Calendar.getInstance();
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
                String myFormat = "yyyy-MM-dd hh:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                departdate.setText(sdf.format(myCalendar.getTime()));
            }


        };

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

                                departdate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

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

                                returndate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYearR, mMonthR, mDayR);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });
        requestQueue = Volley.newRequestQueue(this);
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
