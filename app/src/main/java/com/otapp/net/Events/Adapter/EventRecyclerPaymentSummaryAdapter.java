package com.otapp.net.Events.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.Events.Core.EventPaymentSummaryResponse;
import com.otapp.net.R;

import java.util.List;

public class EventRecyclerPaymentSummaryAdapter extends RecyclerView.Adapter<EventRecyclerPaymentSummaryAdapter.MyViewHolder> {
    public Context context;
    public List<EventPaymentSummaryResponse.Fare> fareList;
    EventPaymentSummaryResponse.Fare fare;
    public String currency;

    public EventRecyclerPaymentSummaryAdapter(Context context, List<EventPaymentSummaryResponse.Fare> fareList, String currency) {
        this.context = context;
        this.fareList = fareList;
        this.currency= currency;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_payment_summary, viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        fare=fareList.get(i);
        myViewHolder.tvLabel.setText(fare.label);
        myViewHolder.tvAmount.setText(currency+" "+fare.amount);
        fare.totalAmount=fare.amount;
    }

    @Override
    public int getItemCount() {
        return fareList.size()-1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvLabel;
        TextView tvAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel=itemView.findViewById(R.id.tvLabel);
            tvAmount=itemView.findViewById(R.id.tvAmount);
        }
    }
}
