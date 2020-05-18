package com.otapp.net.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.fragment.FlightMultiCityFragment;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FlightCityAdapter extends BaseAdapter {

    private Context mContext;
    private FlightMultiCityFragment mFlightMultiCityFragment;

    private List<FlightCityPojo.City> mAirportCityList;

    List<FlightCity> mFlightCityList;
    List<String> mCityList = new ArrayList<>();


    public FlightCityAdapter(Context mContext, FlightMultiCityFragment mFlightMultiCityFragment) {
        this.mContext = mContext;
        this.mFlightMultiCityFragment = mFlightMultiCityFragment;

        Calendar mStartCalDate = Calendar.getInstance();
        int mStartYear, mStartMonth, mStartDay;
        mStartYear = mStartCalDate.get(Calendar.YEAR);
        mStartMonth = mStartCalDate.get(Calendar.MONTH);
        mStartDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

        if (mFlightCityList != null) {
            for (int i = 0; i < mFlightCityList.size(); i++) {
                FlightCity mFlightCity = mFlightCityList.get(i);
                mFlightCity.adultCount = mStartYear;
                mFlightCity.childCount = mStartMonth;
                mFlightCity.infantCount = mStartDay;
            }
        }

    }

    public void addAll(List<FlightCity> mTempFlightCityList) {

        this.mFlightCityList = mTempFlightCityList;

//        if (mFlightCityList != null && mFlightCityList.size() > 0) {
//            mFlightCityList.clear();
//        }
//
//        if (mTempFlightCityList != null && mTempFlightCityList.size() > 0) {
//            mFlightCityList.addAll(mTempFlightCityList);
//        }

        LogUtils.e("", "mFlightCityList::" + mFlightCityList);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mFlightCityList == null) {
            return 0;
        }
        return mFlightCityList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFlightCityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_city, null);
            final ViewHolder holder = new ViewHolder();
            holder.etFromCity = view.findViewById(R.id.etFromCity);
            holder.etToCity = view.findViewById(R.id.etToCity);
            holder.etDate = view.findViewById(R.id.etDate);
            holder.tvDelete = view.findViewById(R.id.tvDelete);

            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final FlightCity mFlightCity = mFlightCityList.get(position);
        mFlightCity.from = holder.etFromCity.getText().toString();
        mFlightCity.to = holder.etToCity.getText().toString();

        if (position == 0) {
            holder.tvDelete.setVisibility(View.GONE);
        } else {
            holder.tvDelete.setVisibility(View.VISIBLE);
        }


        if (mCityList != null && mCityList.size() > 0) {

            ArrayAdapter<String> adapterFromCity = new ArrayAdapter<String>
                    (mContext, R.layout.autocomplete_drop_down_item, mCityList);
            holder.etFromCity.setAdapter(adapterFromCity);

            ArrayAdapter<String> adapterToCity = new ArrayAdapter<String>
                    (mContext, R.layout.autocomplete_drop_down_item, mCityList);
            holder.etToCity.setAdapter(adapterToCity);

            holder.etFromCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mFlightCity.fromCity = adapterView.getItemAtPosition(i).toString();
                }
            });

            holder.etToCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mFlightCity.toCity = adapterView.getItemAtPosition(i).toString();
                }
            });

            if (!TextUtils.isEmpty(mFlightCity.fromCity)) {
                holder.etFromCity.setText(mFlightCity.fromCity);
            } else {
                holder.etFromCity.setText("");
            }

            if (!TextUtils.isEmpty(mFlightCity.toCity)) {
                holder.etToCity.setText(mFlightCity.toCity);
            } else {
                holder.etToCity.setText("");
            }

            if (mFlightCity.displayDepartDate > 0) {

                final Calendar mStartCalDate = Calendar.getInstance();
                mStartCalDate.setTimeInMillis(mFlightCityList.get(position).displayDepartDate);

                mFlightCity.adultCount = mStartCalDate.get(Calendar.YEAR);
                mFlightCity.childCount = mStartCalDate.get(Calendar.MONTH);
                mFlightCity.infantCount = mStartCalDate.get(Calendar.DAY_OF_MONTH);

                LogUtils.e("", position+" mFlightCity.displayDepartDate:"+mFlightCity.displayDepartDate+" "+DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()));
                holder.etDate.setText("" + DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()));

            } else {
                holder.etDate.setText("");
            }


//            holder.etFromCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean isFocused) {
//                    LogUtils.e("", isFocused + " isFocused etFromCity " + (holder.etFromCity.getAdapter() != null));
//                    if (isFocused && holder.etFromCity.getAdapter() != null) {
//                        holder.etFromCity.showDropDown();
//                    }
//                }
//            });
//
//            holder.etToCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean isFocused) {
//                    LogUtils.e("", isFocused + " isFocused etToCity " + (holder.etToCity.getAdapter() != null));
//                    if (isFocused && holder.etToCity.getAdapter() != null) {
//                        holder.etToCity.showDropDown();
//                    }
//                }
//            });
        }

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e("", "position::" + position);
                mFlightCityList.remove(position);
                LogUtils.e("", "mFlightCityList::" + mFlightCityList);
                notifyDataSetChanged();
                mFlightMultiCityFragment.onDeleteCity(position);
            }
        });

        holder.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar mStartCalDate = Calendar.getInstance();
                if (position > 0 && mFlightCityList.get(position - 1).displayDepartDate > 0) {
                    mStartCalDate.setTimeInMillis(mFlightCityList.get(position - 1).displayDepartDate);
                }

                mStartCalDate.set(Calendar.YEAR, mFlightCity.adultCount);
                mStartCalDate.set(Calendar.MONTH, mFlightCity.childCount);
                mStartCalDate.set(Calendar.DAY_OF_MONTH, mFlightCity.infantCount);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mStartCalDate.set(Calendar.YEAR, year);
                                mStartCalDate.set(Calendar.MONTH, monthOfYear);
                                mStartCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                mFlightCity.adultCount = year;
                                mFlightCity.childCount = monthOfYear;
                                mFlightCity.infantCount = dayOfMonth;

                                mFlightCityList.get(position).displayDepartDate = mStartCalDate.getTimeInMillis();
                                mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

                                holder.etDate.setText("" + DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()));

                                LogUtils.e("", position + " position " + (mFlightCityList.size() - 1) + " " + (position < (mFlightCityList.size() - 1)));
                                if (position < (mFlightCityList.size() - 1)) {
                                    boolean isDateChange = false;
                                    for (int i = (position + 1); i < mFlightCityList.size(); i++) {
                                        LogUtils.e("", i+" "+mFlightCityList.get(i).displayDepartDate);
                                        if (mFlightCityList.get(i).displayDepartDate > 0) {
                                            Calendar mEndCalDate = Calendar.getInstance();
                                            mEndCalDate.setTimeInMillis(mFlightCityList.get(i).displayDepartDate);
                                            mEndCalDate.set(Calendar.HOUR_OF_DAY, 0);
                                            mEndCalDate.set(Calendar.MINUTE, 0);
                                            mEndCalDate.set(Calendar.SECOND, 0);
                                            mEndCalDate.set(Calendar.MILLISECOND, 0);

                                            Calendar mNowCalDate = Calendar.getInstance();
                                            mNowCalDate.setTimeInMillis(mStartCalDate.getTimeInMillis());
                                            mNowCalDate.set(Calendar.HOUR_OF_DAY, 0);
                                            mNowCalDate.set(Calendar.MINUTE, 0);
                                            mNowCalDate.set(Calendar.SECOND, 0);
                                            mNowCalDate.set(Calendar.MILLISECOND, 0);

                                            LogUtils.e("", "mNowCalDate::"+mNowCalDate.getTime()+" mEndCalDate::"+mEndCalDate.getTime()+" "+mNowCalDate.after(mEndCalDate));

                                            if (mNowCalDate.after(mEndCalDate)) {

//                                                mFlightCityList.get(i).adultCount = year;
//                                                mFlightCityList.get(i).childCount = monthOfYear;
//                                                mFlightCityList.get(i).infantCount = dayOfMonth;

                                                mFlightCityList.get(i).displayDepartDate = mNowCalDate.getTimeInMillis();
                                                mFlightCityList.get(i).departDate = DateFormate.sdfFlightServerDate.format(mNowCalDate.getTime());
                                                LogUtils.e("", "mNowCalDate.getTimeInMillis():"+mNowCalDate.getTimeInMillis());

                                                isDateChange = true;


//                                                holder.etDate.setText("" + DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()));
                                            }
                                        }

                                    }

                                    LogUtils.e("", "isDateChange::"+isDateChange);
                                    if (isDateChange) {
                                        notifyDataSetChanged();
                                    }
                                }

                            }
                        }, mFlightCity.adultCount, mFlightCity.childCount, mFlightCity.infantCount);

                datePickerDialog.setTitle("" + mContext.getString(R.string.departure_date));

                Calendar mStartCal = Calendar.getInstance();
                if (position > 0 && mFlightCityList.get(position - 1).displayDepartDate > 0) {
                    mStartCal.setTimeInMillis(mFlightCityList.get(position - 1).displayDepartDate);
                }
                datePickerDialog.getDatePicker().setMinDate(mStartCal.getTimeInMillis() - 1000);
                mStartCal.add(Calendar.MONTH, 6);
                datePickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 1000);

                if (datePickerDialog != null) {
                    datePickerDialog.show();
                }

            }
        });

        return view;
    }

    public void setCityList(List<FlightCityPojo.City> mAirportCityList) {
        LogUtils.e("", "setCityList");
        this.mAirportCityList = mAirportCityList;

        if (mAirportCityList != null && mAirportCityList.size() > 0) {

            if (mCityList != null && mCityList.size() > 0) {
                mCityList.clear();
            }

            for (int i = 0; i < mAirportCityList.size(); i++) {
                mCityList.add(mAirportCityList.get(i).cityName);
            }

        }

        notifyDataSetChanged();
    }


    private class ViewHolder {
        TextView tvDelete;
        AutoCompleteTextView etFromCity, etToCity;
        EditText etDate;
    }
}
