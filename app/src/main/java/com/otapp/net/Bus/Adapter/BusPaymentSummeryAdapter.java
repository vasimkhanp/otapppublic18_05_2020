package com.otapp.net.Bus.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.Bus.Core.GetFareResponse;
import com.otapp.net.Events.Adapter.EventRecyclerPaymentSummaryAdapter;
import com.otapp.net.R;

import java.util.ArrayList;

public class BusPaymentSummeryAdapter extends RecyclerView.Adapter<BusPaymentSummeryAdapter.MyViewHolder> {
    Context context;
    ArrayList<GetFareResponse.Fare> fareArrayList;
    GetFareResponse.Fare fare;

    public BusPaymentSummeryAdapter(Context context, ArrayList<GetFareResponse.Fare> fareArrayList) {
        this.context = context;
        this.fareArrayList = fareArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_payment_summary, viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        fare=fareArrayList.get(i);
        myViewHolder.tvLabel.setText(fare.label);
        myViewHolder.tvAmount.setText(fare.amount);
    }

    @Override
    public int getItemCount() {
        return fareArrayList.size();
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
