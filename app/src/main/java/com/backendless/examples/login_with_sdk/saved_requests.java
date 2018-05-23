package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class saved_requests extends Activity {
    public String[] requests = {"empty"};
    public String[] request_urls = {"empty"};
    public String[] a = {"empty"};
    public String[] b = {"empty"};
    public String[] ids  = {"emepty"};
    public String[] departs = {"empty"};
    public String[] returns = {"empty"};
// recycler view
    private List<Request> requestList;
    private RecyclerView.Adapter adapter;
    RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private ListView lvRequests;
    private RecyclerViewClickListener listener;
    // recycler view



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_requests);
        LinearLayout lay = (LinearLayout)findViewById(R.id.saved_requests);
        lay.setBackgroundResource(R.drawable.background);
        //lvRequests = (ListView) findViewById(R.id.lv_saved_requests);

        //registerForContextMenu(lvRequests);

        // recycler view

        run();

    }

    protected void run(){
        mList = findViewById(R.id.requests_list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        // adapter = new RequestAdapter(getApplicationContext(), list);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        // recycler view

        // make array requests
        Backendless.initApp(this, getString( R.string.backendless_AppId), getString( R.string.backendless_ApiKey));
        String whereClause = "ownerId = '" + Backendless.UserService.loggedInUser() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause( whereClause );



        Backendless.Persistence.of( Request.class ).find( queryBuilder, new AsyncCallback<List<Request>>(){
            public void handleResponse( List<Request> list ){
                requestList = list;
                if(list.size() > 0 ){
//                    requests = new String[list.size()];
                    request_urls = new String[list.size()];
                    a = new String[list.size()];
                    b = new String[list.size()];
                    ids = new String[list.size()];
                    departs = new String[list.size()];
                    returns = new String[list.size()];
//                    requests = new String[list.size()];
                    for (int i=0; i<list.size(); i++) {
//                        requests[i] = "From: "  + list.get(i).getPointa() + "  To: " + list.get(i).getPointb();
                        request_urls[i] = list.get(i).getRequest_url();
                        a[i] = list.get(i).getPointa();
                        b[i] = list.get(i).getPointb();
                        ids[i] = list.get(i).objectId;
                        departs[i] = list.get(i).getDepartdate();
                        returns[i] = list.get(i).getReturndate();
                        System.out.println(" 000 " + list.get(i).getDepartdate() + " - " + list.get(i).getReturndate());
                    }

                    listener = (View view, int position) -> {

                        PopupMenu popup = new PopupMenu(getBaseContext(), view);

                        popup.inflate(R.menu.request_menu);

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.request_delete:

                                        list.remove(position);
                                        try{
                                            new Thread(new Runnable() {
                                                public void run() {
                                                    // synchronous backendless API call here:
                                                    Backendless.Persistence.of( "Request" ).remove( Backendless.Data.of( "Request" ).findById( ids[position] ) );
                                                }
                                            }).start();
                                        }catch (Exception e){

                                            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                            Log.d("DELETE_REQUEST", e.toString());
                                        }
                                        startAdapter();
                                        return true;
                                    case R.id.request_result:
                                        System.out.println("--------------" + request_urls[position] + "--------------");
                                        Intent intent = new Intent(getBaseContext(), results_activity.class);
                                        intent.putExtra("data", request_urls[position]);
                                        intent.putExtra("pointa", a[position]);
                                        intent.putExtra("pointb", b[position]);
                                        intent.putExtra("user_id", Backendless.UserService.loggedInUser());
                                        intent.putExtra("showDeleteBtn", "1");
                                        intent.putExtra("objectID", ids[position]);
                                        intent.putExtra("departdate", departs[position]);
                                        intent.putExtra("departdate", "");
                                        intent.putExtra("returndate", returns[position]);
                                        intent.putExtra("returndate", "");
                                    startActivity(intent);
                                        adapter.notifyDataSetChanged();
                                        return true;
                                }
                                return false;
                            }
                        });

                        popup.show();

                    };

//                    adapter = new RequestAdapter(getApplicationContext(), list, listener);
//
//                    mList.setAdapter(adapter);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        adapter.notifyDataSetChanged();
    }

    public void startAdapter(){

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requests);

//        lvRequests.setAdapter(adapter);
        adapter = new RequestAdapter(getApplicationContext(), requestList, listener);

        mList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

}