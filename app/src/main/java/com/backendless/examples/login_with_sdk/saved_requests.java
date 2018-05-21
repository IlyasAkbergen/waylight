package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    // recycler view
    //private List<Request> requestList;
    private RecyclerView.Adapter adapter;
    RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    // recycler view

    private ListView lvRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_requests);
        LinearLayout lay = (LinearLayout)findViewById(R.id.saved_requests);
        lay.setBackgroundResource(R.drawable.background);
        //lvRequests = (ListView) findViewById(R.id.lv_saved_requests);

        //registerForContextMenu(lvRequests);

        // recycler view

        mList = findViewById(R.id.requests_list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
       // adapter = new RequestAdapter(getApplicationContext(), list);

        // recycler view

        // make array requests
        Backendless.initApp(this, getString( R.string.backendless_AppId), getString( R.string.backendless_ApiKey));
        String whereClause = "ownerId = '" + Backendless.UserService.loggedInUser() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause( whereClause );

        Backendless.Persistence.of( Request.class ).find( queryBuilder, new AsyncCallback<List<Request>>(){
            public void handleResponse( List<Request> list ){
                if(list.size() > 0 ){
//                    requests = new String[list.size()];
//                    request_urls = new String[list.size()];
//                    a = new String[list.size()];
//                    b = new String[list.size()];
//                    ids = new String[list.size()];
//                    requests = new String[list.size()];
//                    for (int i=0; i<list.size(); i++) {
//                        requests[i] = "From: "  + list.get(i).getPointa() + "  To: " + list.get(i).getPointb();
//                        request_urls[i] = list.get(i).getRequest_url();
//                        a[i] = list.get(i).getPointa();
//                        b[i] = list.get(i).getPointb();
//                        ids[i] = list.get(i).getObjectId();
//                    }

                    RecyclerViewClickListener listener = (view, position) -> {
                        Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();
                    };

                    adapter = new RequestAdapter(getApplicationContext(), list, listener);

                    mList.setAdapter(adapter);
                    startAdapter();

                }else{
//                    List<Request> emptyList = new ArrayList<>();
//                    Request r = new Request();
//                    r.setPointa("You have no saved request.");
//                    r.setPointb("");
//                    emptyList.add(r);
//                    adapter = new RequestAdapter(getApplicationContext(), emptyList);
//                    mList.setAdapter(adapter);
//                    startAdapter();
                    TextView tv = findViewById(R.id.requestTitle);
                    tv.setText("You have no saved request.");
                }
            }


            public void handleFault( BackendlessFault fault )
            {
                requests = new String[1];
                requests[0] = "You have no saved request yet.";
                startAdapter();
            }
        });

//        lvRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
//                        + id);
//
//                Intent intent = new Intent(getBaseContext(), results_activity.class);
//                intent.putExtra("data", request_urls[position]);
//                intent.putExtra("pointa", a[position]);
//                intent.putExtra("pointb", b[position]);
//                intent.putExtra("user_id", Backendless.UserService.loggedInUser());
//                intent.putExtra("showDeleteBtn", "1");
//                intent.putExtra("objectID", ids[position]);
//
//                startActivity(intent);
//
//            }
//        });


//        lvRequests.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                return false;
//            }
//        });
    }

    public void startAdapter(){

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requests);

//        lvRequests.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

}