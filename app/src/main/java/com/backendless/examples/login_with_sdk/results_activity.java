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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class results_activity extends Activity{
    private List<Ticket> ticketList;
    private RecyclerView.Adapter adapter;
    RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    String data, pointa, pointb, user_id;
    ProgressBar progressBar ;
    String baseUrl = "http://singer.kz/waylight/public/api/tickets";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        LinearLayout lay = (LinearLayout)findViewById(R.id.activity_results);
        lay.setBackgroundResource(R.drawable.background);
        mList = findViewById(R.id.main_list);
        ticketList = new ArrayList<>();
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
        System.out.println("here" + data);
        new RetrieveFeedTask().execute();

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

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

                    for (int i = 0; i < finalResult.length(); i++) {
                        try {
                            JSONObject jsonObject = finalResult.getJSONObject(i);
                            Ticket ticket = new Ticket();
                            ticket.setPointa(jsonObject.getString("pointa"));
                            ticket.setPointb(jsonObject.getString("pointb"));
                            ticket.setDepartdate(jsonObject.getString("depart"));
                            ticket.setReturndate(jsonObject.getString("return"));
                            ticket.setFlightClass(jsonObject.getString("class"));
                            ticket.setPrice(jsonObject.getInt("price"));

                            ticketList.add(ticket);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progressDialog.dismiss();
                        }
                    }

                    if(finalResult.length() == 0){
                        Intent intent = new Intent(getBaseContext(), save_request_activity.class);
                        intent.putExtra("data", data);
                        intent.putExtra("pointa", pointa);
                        intent.putExtra("pointb", pointb);
                        intent.putExtra("user_id", user_id);
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
            adapter.notifyDataSetChanged();
            Log.i("INFO", response);
            // responseView.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

//    private void sendRequest(){
//        HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost(baseUrl);
//
//        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        pairs.add(new BasicNameValuePair("pointa", pointa.toString()));
//        pairs.add(new BasicNameValuePair("pointb", pointb.toString()));
//        try {
//            post.setEntity(new UrlEncodedFormEntity(pairs));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            HttpResponse response = client.execute(post);
//            getTicketsList(response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        requestQueue = Volley.newRequestQueue(this);
//
//    }

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
        // progressDialog.dismiss();

//        If the JSON is actually a single line, then you can also remove the loop and builder.
//
//        HttpResponse response; // some response object
//        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//        String json = reader.readLine();
//        JSONTokener tokener = new JSONTokener(json);
//        JSONArray finalResult = new JSONArray(tokener);

//    }
}
