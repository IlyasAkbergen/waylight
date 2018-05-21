package com.backendless.examples.login_with_sdk;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RecyclerViewClickListener mListener;

    RowViewHolder(View v, RecyclerViewClickListener listener) {
        super(v);
        mListener = listener;
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getAdapterPosition());
    }
}