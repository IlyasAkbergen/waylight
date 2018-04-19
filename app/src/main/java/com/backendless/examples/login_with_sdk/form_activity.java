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

import com.android.volley.RequestQueue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class form_activity extends Activity {

    EditText pointa, pointb;
    String url, ticketClass = "";
    private int mYearD,mMonthD, mDayD, mYearR, mMonthR, mDayR;
    RequestQueue requestQueue;
    private Button searchBtn;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        RelativeLayout lay = (RelativeLayout)findViewById(R.id.activity_form);
        lay.setBackgroundResource(R.drawable.background);
        this.pointa = (EditText) findViewById(R.id.pointa);
        this.pointb = (EditText) findViewById(R.id.pointb);
        this.searchBtn = (Button) findViewById(R.id.searchBtn);
        final Button pickDepartDate = (Button) findViewById(R.id.departDate);
        final Button pickReturnDate = (Button) findViewById(R.id.returnDate);
        final TextView departdate = (TextView) findViewById(R.id.departdate);
        final TextView returndate = (TextView) findViewById(R.id.returndate);
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
                String myFormat = "yyyy-MM-dd HH:mm:ss"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                departdate.setText(sdf.format(myCalendar.getTime()));
            }


        };

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "pointa=" + pointa.getText().toString() + "&pointb=" + pointb.getText().toString(); //data to post
                if(ticketClass != ""){
                    data += "&class=" + ticketClass;
                }

                if(departdate.getText().toString().equals("")){
                    data += "&depart=" + departdate.getText().toString();
                }


                if(returndate.getText().toString().equals("")){
                    data += "&return=" + returndate.getText().toString();
                }

                Intent intent = new Intent(getBaseContext(), results_activity.class);
                intent.putExtra("data", data);
                startActivity(intent);
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

//    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
//
//        private Exception exception;
//
//        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        protected String doInBackground(Void... params) {
//            String data = "pointa=" + pointa.getText().toString() + "&pointb=" + pointb.getText().toString(); //data to post
//            if(ticketClass != ""){
//                data += "&class=" + ticketClass;
//            }
//            OutputStream out = null;
//            try {
//                URL url = new URL(baseUrl);// + "?pointa=" + pointa.getText().toString() + "&pointb=" + pointb.getText().toString()
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                try {
//                    out = new BufferedOutputStream(urlConnection.getOutputStream());
//
//                    BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));
//
//                    writer.write(data);
//
//                    writer.flush();
//
//                    writer.close();
//
//                    out.close();
//
//                    urlConnection.connect();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                    StringBuilder stringBuilder = new StringBuilder();
//                    for (String line = null; (line = bufferedReader.readLine()) != null;) {
//                        stringBuilder.append(line).append("\n");
//                    }
//                    bufferedReader.close();
//                    //JSONTokener tokener = new JSONTokener(stringBuilder.toString());
//                    JSONObject jobject = new JSONObject(stringBuilder.toString());
//                    JSONArray finalResult = jobject.getJSONArray("data" );
//
//                    for (int i = 0; i < finalResult.length(); i++) {
//                        try {
//                            JSONObject jsonObject = finalResult.getJSONObject(i);
//                            Ticket ticket = new Ticket();
//                            ticket.setPointa(jsonObject.getString("pointa"));
//                            ticket.setPointb(jsonObject.getString("pointb"));
//                            ticket.setDepartdate(jsonObject.getString("depart"));
//                            ticket.setReturndate(jsonObject.getString("return"));
//                            ticket.setFlightClass(jsonObject.getString("class"));
//                            //                ticket.setYear(jsonObject.getInt("releaseYear"));
//
//                            ticketList.add(ticket);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            //progressDialog.dismiss();
//                        }
//                    }
//
//                    return "";
//                }
//                finally{
//                    urlConnection.disconnect();
//                }
//            }
//            catch(Exception e) {
//                Log.e("ERROR", e.getMessage(), e);
//                return null;
//            }
//        }
//
//        protected void onPostExecute(String response) {
//            if(response == null) {
//                response = "THERE WAS AN ERROR";
//            }
//            progressBar.setVisibility(View.GONE);
//            Log.i("INFO", response);
//           // responseView.setText(response);
           // TODO: check this.exception
    // TODO: do something with the feed
//
////            try {
////                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
////                String requestID = object.getString("requestId");
////                int likelihood = object.getInt("likelihood");
////                JSONArray photos = object.getJSONArray("photos");
////                .
////                .
////                .
////                .
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//        }
//    }
//
////    private void sendRequest(){
////        HttpClient client = new DefaultHttpClient();
////        HttpPost post = new HttpPost(baseUrl);
////
////        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
////        pairs.add(new BasicNameValuePair("pointa", pointa.toString()));
////        pairs.add(new BasicNameValuePair("pointb", pointb.toString()));
////        try {
////            post.setEntity(new UrlEncodedFormEntity(pairs));
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
////
////        try {
////            HttpResponse response = client.execute(post);
////            getTicketsList(response);
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////
////        requestQueue = Volley.newRequestQueue(this);
////
////    }
//
//    private void getTicketsList(HttpResponse response) throws IOException, JSONException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//        StringBuilder builder = new StringBuilder();
//        for (String line = null; (line = reader.readLine()) != null;) {
//            builder.append(line).append("\n");
//        }
//        JSONTokener tokener = new JSONTokener(builder.toString());
//        JSONArray finalResult = new JSONArray(tokener);
//
//        for (int i = 0; i < finalResult.length(); i++) {
//            try {
//                JSONObject jsonObject = finalResult.getJSONObject(i);
//                Ticket ticket = new Ticket();
//                ticket.setPointa(jsonObject.getString("pointa"));
//                ticket.setPointb(jsonObject.getString("pointb"));
//                ticket.setDepartdate(jsonObject.getString("depart"));
//                ticket.setReturndate(jsonObject.getString("return"));
//                ticket.setFlightClass(jsonObject.getString("class"));
//                //                ticket.setYear(jsonObject.getInt("releaseYear"));
//
//                ticketList.add(ticket);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                //progressDialog.dismiss();
//            }
//        }
//        adapter.notifyDataSetChanged();
//       // progressDialog.dismiss();
//
////        If the JSON is actually a single line, then you can also remove the loop and builder.
////
////        HttpResponse response; // some response object
////        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
////        String json = reader.readLine();
////        JSONTokener tokener = new JSONTokener(json);
////        JSONArray finalResult = new JSONArray(tokener);
//
//    }

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