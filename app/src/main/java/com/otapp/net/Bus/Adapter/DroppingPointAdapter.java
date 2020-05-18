package com.otapp.net.Bus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.otapp.net.Bus.Core.BDPoints;
import com.otapp.net.Bus.Impl.DroppingPointInterface;
import com.otapp.net.R;

import java.util.ArrayList;

public class DroppingPointAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BDPoints> bdPointsArrayList;
    private DroppingPointInterface droppingPointInterface;

    public DroppingPointAdapter(Context context, ArrayList<BDPoints> bdPointsArrayList, DroppingPointInterface droppingPointInterface) {
        this.context = context;
        this.bdPointsArrayList = bdPointsArrayList;
        this.droppingPointInterface = droppingPointInterface;
    }

    @Override
    public int getCount() {
        return bdPointsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return bdPointsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_bd, parent, false);
        RadioButton radioButtonPoint = view.findViewById(R.id.radioButtonPoint);
        TextView textViewPoint = view.findViewById(R.id.textViewPoint);
        TextView textViewTime = view.findViewById(R.id.textViewTime);
        TextView textViewDate = view.findViewById(R.id.textViewDate);

        BDPoints bdPoints = bdPointsArrayList.get(position);
        textViewPoint.setText(bdPoints.getsBDName());
//        textViewDate.setText("20-Apr-2020");
//        textViewTime.setText("0" + (position + 1) + ":00");

        if (bdPoints.isSelected()){
            radioButtonPoint.setChecked(true);
        }else {
            radioButtonPoint.setChecked(false);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < bdPointsArrayList.size(); i++) {
                    BDPoints bdPoints1 = bdPointsArrayList.get(i);
                    if (bdPoints.getsBDID().equals(bdPoints1.getsBDID())) {
                        bdPointsArrayList.get(i).setSelected(true);
                    } else {
                        bdPointsArrayList.get(i).setSelected(false);
                    }
                }
                droppingPointInterface.onDroppingPointSelected(bdPoints);
                notifyDataSetChanged();
            }
        });

        radioButtonPoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < bdPointsArrayList.size(); i++) {
                    BDPoints bdPoints1 = bdPointsArrayList.get(i);
                    if (bdPoints.getsBDID().equals(bdPoints1.getsBDID())) {
                        bdPointsArrayList.get(i).setSelected(true);
                    } else {
                        bdPointsArrayList.get(i).setSelected(false);
                    }
                }
                droppingPointInterface.onDroppingPointSelected(bdPoints);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
