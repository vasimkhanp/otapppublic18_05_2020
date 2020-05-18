package com.otapp.net.Bus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PassengerInfoAdapter extends BaseAdapter {

    private Context context;
    private JSONArray jsonArrayPassengerDetails;

    public PassengerInfoAdapter(Context context, JSONArray jsonArrayPassengerDetails) {
        this.context = context;
        this.jsonArrayPassengerDetails = jsonArrayPassengerDetails;
    }

    @Override
    public int getCount() {
        return jsonArrayPassengerDetails.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArrayPassengerDetails.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_passenget_info, parent, false);
        TextView textViewPersonName = view.findViewById(R.id.textViewPersonName);
        TextView textViewPersonSeatNo = view.findViewById(R.id.textViewPersonSeatNo);
        try {
            JSONObject jsonObject = jsonArrayPassengerDetails.getJSONObject(position);
            String sName = jsonObject.getString("passenger_name");
            String sAge = jsonObject.getString("passenger_seat_no");
            textViewPersonName.setText(sName);
            textViewPersonSeatNo.setText(sAge);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
