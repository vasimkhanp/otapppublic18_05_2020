package com.otapp.net.Bus.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.otapp.net.Bus.Core.BusOperatorName;
import com.otapp.net.Bus.Impl.BusOperatorSelectedListener;
import com.otapp.net.R;

import java.util.ArrayList;

public class BusOperatorNameAdapter extends RecyclerView.Adapter<BusOperatorNameAdapter.MyViewHolder>{
    public Context context;
    public ArrayList<BusOperatorName> operatorNameList;
    BusOperatorSelectedListener busOperatorSelectedListener;
    BusOperatorName busOperatorName;

    public BusOperatorNameAdapter(Context context, ArrayList<BusOperatorName> operatorNameList, BusOperatorSelectedListener busOperatorSelectedListener) {
        this.context = context;
        this.operatorNameList = operatorNameList;
        this.busOperatorSelectedListener = busOperatorSelectedListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_bus_operator_selection, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        busOperatorName=operatorNameList.get(i);
        if(busOperatorName.isSelected){
            myViewHolder.radioBusOperator.setChecked(true);
        }else {
            myViewHolder.radioBusOperator.setChecked(false);
        }
        myViewHolder.tvBusOperatorName.setText(operatorNameList.get(i).sBusOperatorName);

    }

    @Override
    public int getItemCount() {
        return operatorNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvBusOperatorName;
        RadioButton radioBusOperator;
        LinearLayout layoutBusOperator;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             tvBusOperatorName=itemView.findViewById(R.id.busOperetorName);
             radioBusOperator=itemView.findViewById(R.id.radioBusOperator);
             layoutBusOperator=itemView.findViewById(R.id.layoutBusOperator);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(operatorNameList.get(getAdapterPosition()).isSelected){
                         operatorNameList.get(getAdapterPosition()).setSelected(false);
                         radioBusOperator.setChecked(false);
                         busOperatorSelectedListener.selectedBusOperator(operatorNameList.get(getAdapterPosition()).sBusOperatorName,0);

                     }else {
                         operatorNameList.get(getAdapterPosition()).setSelected(true);
                         busOperatorSelectedListener.selectedBusOperator(operatorNameList.get(getAdapterPosition()).sBusOperatorName,1);
                         radioBusOperator.setChecked(true);
                     }
                 }
             });
        }

    }
}
