package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class saved_requests extends Activity {
    public String[] requests = {"empty"};
    public String[] request_urls = {"empty"};
    public String[] a = {"empty"};
    public String[] b = {"empty"};
    public String[] ids  = {"emepty"};
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
                    request_urls = new String[list.size()];
                    a = new String[list.size()];
                    b = new String[list.size()];
                    ids = new String[list.size()];
                    requests = new String[list.size()];
                    for (int i=0; i<list.size(); i++) {
                        requests[i] = "From: "  + list.get(i).getPointa() + "  To: " + list.get(i).getPointb();
                        request_urls[i] = list.get(i).getRequest_url();
                        a[i] = list.get(i).getPointa();
                        b[i] = list.get(i).getPointb();
                        ids[i] = list.get(i).getObjectId();
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

        lvRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
//                        + id);

                Intent intent = new Intent(getBaseContext(), results_activity.class);
                intent.putExtra("data", request_urls[position]);
                intent.putExtra("pointa", a[position]);
                intent.putExtra("pointb", b[position]);
                intent.putExtra("user_id", Backendless.UserService.loggedInUser());
                intent.putExtra("showDeleteBtn", "1");
                intent.putExtra("objectID", ids[position]);

                startActivity(intent);

            }
        });
    }

    public void startAdapter(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requests);

        lvRequests.setAdapter(adapter);
    }
}