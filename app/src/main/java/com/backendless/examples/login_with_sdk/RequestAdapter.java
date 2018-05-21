package com.backendless.examples.login_with_sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Request> list;
    private RecyclerViewClickListener mListener;

    RequestAdapter(Context context, List<Request> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_saved_requests, parent, false);
        return new RowViewHolder(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowHolder = (RowViewHolder) holder;
            Request request = list.get(position);
            //set values of data here
            if(request != null)
                rowHolder.text.setText( request.getPointa() + " - " + request.getPointb());
            else
                rowHolder.text.setText("No saved requests.");
        }
    }


//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView text;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            text = itemView.findViewById(R.id.requestTitle);
//        }
//    }
    //            holder.text.setText("No saved requests.");
    //        else
    //            holder.text.setText(request.getPointa() + " - " + request.getPointb());
    //        if(request != null)
    //        Request request = list.get(position);
    //    public void onBindViewHolder(ViewHolder holder, int position) {
//    @Override

//    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}