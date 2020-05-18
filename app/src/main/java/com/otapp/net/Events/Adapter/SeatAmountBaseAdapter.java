package com.otapp.net.Events.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.R;

import java.text.DecimalFormat;
import java.util.List;

public class SeatAmountBaseAdapter extends RecyclerView.Adapter<SeatAmountBaseAdapter.MyViewHolder> {
    public Context context;
    public List<EventsListResponse.Events.EventTickets.Tickets> tempTicketArrayList;
    EventsListResponse.Events.EventTickets.Tickets tickets;

    public SeatAmountBaseAdapter(Context context, List<EventsListResponse.Events.EventTickets.Tickets> tempTicketArrayList) {
        this.context = context;
        this.tempTicketArrayList = tempTicketArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_no_of_seats_list,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        tickets=tempTicketArrayList.get(i);
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");

        if(Double.parseDouble(String.valueOf(tempTicketArrayList.get(i).count))>0) {
            String sValue = String.valueOf(decimalFormat.format(Double.parseDouble(tempTicketArrayList.get(i).tkt_amount.replaceAll(",","")) * Double.parseDouble(String.valueOf(tempTicketArrayList.get(i).count))));
          //  myViewHolder.tvSeatAmount.setText("" + Double.parseDouble(tempTicketArrayList.get(i).tkt_amount) * Double.parseDouble(String.valueOf(tempTicketArrayList.get(i).count)));
            myViewHolder.tvSeatAmount.setText(""+sValue);
            myViewHolder.tvTicketClass.setText(tempTicketArrayList.get(i).tkt_name);
        }else {
            myViewHolder.tvSeatAmount.setVisibility(View.GONE);
            myViewHolder.tvTicketClass.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return tempTicketArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTicketClass,tvSeatAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeatAmount=itemView.findViewById(R.id.tvAmount);
            tvTicketClass=itemView.findViewById(R.id.tvTicketClass);
        }
    }

    /*  LayoutInflater layoutInflater;

    public SeatAmountBaseAdapter(Context context, List<EventsListResponse.Events.EventTickets.Tickets> tempTicketArrayList) {
        this.context = context;
        this.tempTicketArrayList = tempTicketArrayList;
        this.layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return tempTicketArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= layoutInflater.inflate(R.layout.layout_no_of_seats_list, null);

        TextView tvTicketClass= convertView.findViewById(R.id.tvTicketClass);

        TextView tvSeatAmount= convertView.findViewById(R.id.tvAmount);

        tvSeatAmount.setText(""+Double.parseDouble(tempTicketArrayList.get(position).tkt_amount)*Double.parseDouble(String.valueOf(tempTicketArrayList.get(position).count)));
        tvTicketClass.setText(tempTicketArrayList.get(position).tkt_name);

        return convertView;

    }*/
}
