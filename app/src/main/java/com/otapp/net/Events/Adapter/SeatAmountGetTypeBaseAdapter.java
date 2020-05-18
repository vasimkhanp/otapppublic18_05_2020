package com.otapp.net.Events.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;
import com.otapp.net.R;

import java.text.DecimalFormat;
import java.util.List;

public class SeatAmountGetTypeBaseAdapter extends RecyclerView.Adapter<SeatAmountGetTypeBaseAdapter.MyViewHolder>{
    public Context context;
    public List<GetTicketTypeResponse.GetTickets> tempTicketArrayList;
    GetTicketTypeResponse.GetTickets tickets;

    public SeatAmountGetTypeBaseAdapter(Context context, List<GetTicketTypeResponse.GetTickets> tempTicketArrayList) {
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
       // DecimalFormat decimalFormat = new DecimalFormat("0.00");
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
}
