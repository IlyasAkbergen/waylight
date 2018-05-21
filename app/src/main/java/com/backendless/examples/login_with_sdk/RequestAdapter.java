package com.backendless.examples.login_with_sdk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    //private Context context;
    private List<Request> list;
    private RecyclerViewClickListener mListener;

    RequestAdapter(Context context, List<Request> list, RecyclerViewClickListener listener) {
      //  this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_saved_requests, parent, false);
        return new RowViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Request request = list.get(position);
        if(request != null)
            holder.text.setText(request.getPointa() + " - " + request.getPointb());
        else
            holder.text.setText("No saved requests.");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RequestAdapter.ViewHolder {
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.requestTitle);
        }
    }

}