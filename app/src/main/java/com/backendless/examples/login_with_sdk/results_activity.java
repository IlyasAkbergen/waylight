package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.backendless.Backendless;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class results_activity extends Activity{
    private List<Ticket> ticketList;
    private RecyclerView.Adapter adapter;
    RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    String data, pointa, pointb, user_id, showDeleteBtn, objectID, returndate, departdate;
    ProgressBar progressBar ;
    String baseUrl = "http://singer.kz/waylight/public/api/tickets";
   // Button deleteBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        LinearLayout lay = (LinearLayout)findViewById(R.id.activity_results);
        lay.setBackgroundResource(R.drawable.background);
        ticketList = new ArrayList<>();
        mList = findViewById(R.id.main_list);
        adapter = new TicketAdapter(getApplicationContext(), ticketList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);
        data = getIntent().getStringExtra("data");
        pointa = getIntent().getStringExtra("pointa");
        pointb = getIntent().getStringExtra("pointb");
        user_id = getIntent().getStringExtra("user_id");
        showDeleteBtn = getIntent().getStringExtra("showDeleteBtn");
        objectID = getIntent().getStringExtra("objectID");
        departdate = getIntent().getStringExtra("departdate");
        returndate = getIntent().getStringExtra("returndate");

        System.out.println("### " + departdate  + " - " + returndate);

        //System.out.println("here" + data);
//        this.deleteBtn = (Button) findViewById(R.id.deleteBtn);
        new RetrieveFeedTask().execute();

//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // find object by id
//                deleteRequest();
//
//            }
//        });

    }

    private void deleteRequest() {
        Backendless.initApp(getBaseContext(), getString( R.string.backendless_AppId), getString( R.string.backendless_ApiKey));

        new Thread(new Runnable() {
            public void run() {
                // synchronous backendless API call here:
                Backendless.Persistence.of( "Request" ).remove( Backendless.Data.of( "Request" ).findById( objectID ) );
            }
        }).start();

//        finish();

        Intent intent = new Intent(getBaseContext(), saved_requests.class);
        intent.putExtra("ownerId", Backendless.UserService.loggedInUser());
        startActivity(intent);
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {
            OutputStream out = null;
            try {
                URL url = new URL(baseUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                try {
                    out = new BufferedOutputStream(urlConnection.getOutputStream());

                    BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                    writer.write(data);

                    writer.flush();

                    writer.close();

                    out.close();

                    urlConnection.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String line = null; (line = bufferedReader.readLine()) != null;) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    JSONObject jobject = new JSONObject(stringBuilder.toString());
                    JSONArray finalResult = jobject.getJSONArray("data" );

                    boolean st1 = true, st2 = true;

                    for (int i = 0; i < finalResult.length(); i++) {
                        try {
                            JSONObject jsonObject = finalResult.getJSONObject(i);

                            if( !departdate.equals("") ){
                                if(!formatter.parse(jsonObject.getString("depart")).after(formatter.parse(departdate))){
                                    st1 = false;
                                    System.out.println("st1 = false");
                                }
                            }

                            if( !returndate.equals("")){
                                if(!formatter.parse(jsonObject.getString("return")).after(formatter.parse(returndate))){
                                    st2 = false;
                                    System.out.println("st2 = false");
                                }
                            }

                            if( st1 && st2 ) {
                                Ticket ticket = new Ticket();
                                ticket.setPointa(jsonObject.getString("pointa"));
                                ticket.setPointb(jsonObject.getString("pointb"));
                                ticket.setDepartdate(jsonObject.getString("depart"));
                                ticket.setReturndate(jsonObject.getString("return"));
                                ticket.setFlightClass(jsonObject.getString("class"));
                                ticket.setPrice(jsonObject.getInt("price"));
                                ticketList.add(ticket);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progressDialog.dismiss();
                        }
                    }

                    if(ticketList.size() == 0 && showDeleteBtn.equals("0")){
                        Intent intent = new Intent(getBaseContext(), save_request_activity.class);
                        intent.putExtra("data", data);
                        intent.putExtra("pointa", pointa);
                        intent.putExtra("pointb", pointb);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("departdate", departdate);
                        intent.putExtra("returndate", returndate);
                        startActivity(intent);
                    }

                    return "";
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            if(showDeleteBtn.equals("1")){
//                deleteBtn.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
            Log.i("INFO", response);
            // responseView.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed

        }
    }
}
