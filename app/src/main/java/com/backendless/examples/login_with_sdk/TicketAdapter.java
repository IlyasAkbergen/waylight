package com.backendless.examples.login_with_sdk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private Context context;
    private List<Ticket> list;

    public TicketAdapter(Context context, List<Ticket> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_results, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ticket ticket = list.get(position);
        holder.textPointa.setText("From: " + ticket.getPointa());
        holder.textPointb.setText("To: " + ticket.getPointb());
        holder.textDepartdate.setText("Depart: " + String.valueOf(ticket.getDepartdate()));
        holder.textReturndate.setText("Return: " + String.valueOf(ticket.getReturndate()));
        holder.textClass.setText(ticket.getFlightClass() + " class");
        holder.textPrice.setText("Price: " + ticket.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textPointa, textPointb, textDepartdate, textReturndate, textClass, textPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            textPointa = itemView.findViewById(R.id.Pointa);
            textPointb = itemView.findViewById(R.id.Pointb);
            textDepartdate = itemView.findViewById(R.id.Departdate);
            textReturndate = itemView.findViewById(R.id.Returndate);
            textClass = itemView.findViewById(R.id.textclass);
            textPrice = itemView.findViewById(R.id.textprice);
        }
    }

}