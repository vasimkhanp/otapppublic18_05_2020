package com.otapp.net.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightPerson;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FlightPersonAdapter extends BaseAdapter {

    private Context mContext;
    private boolean isInternationalFlight;
    List<String> mNationalityList;
    List<FlightPerson> mPersonList = new ArrayList<>();
    private int mVisibleCount = 0, mVisiblePosition = 0;
    String mCurrency = "";
    private OnAddonsListener onAddonsListener;

    int count = 0;

    List<FlightOneDetailsPojo.Meal> mMealsList;
    List<FlightOneDetailsPojo.Baggage> mBaggagesList;
    FlightOneDetailsPojo mFlightOneDetailsPojo;

    public interface OnAddonsListener {
        public void onAddonsSelected(int position);
    }

    public FlightPersonAdapter(Context mContext, List<FlightPerson> mPersonList, FlightOneDetailsPojo mFlightOneDetailsPojo,
                               boolean isInternationalFlight, List<String> mNationalityList, OnAddonsListener onAddonsListener) {
        this.mContext = mContext;
        this.mNationalityList = mNationalityList;
        this.onAddonsListener = onAddonsListener;
        this.isInternationalFlight = isInternationalFlight;
        this.mPersonList = mPersonList;
        this.mFlightOneDetailsPojo = mFlightOneDetailsPojo;
        setAddons();
    }

    public void setAddons() {
        mMealsList = mFlightOneDetailsPojo.data.meals;
        mBaggagesList = mFlightOneDetailsPojo.data.baggages;
        mCurrency = mFlightOneDetailsPojo.data.currency;
        if (mMealsList != null && mMealsList.size() > 0) {
            if (mMealsList.get(0).id == null || !mMealsList.get(0).id.equals("AO")) {
                FlightOneDetailsPojo.Meal mMeal = new FlightOneDetailsPojo().new Meal();
                mMeal.id = "A0";
                mMeal.name = mContext.getString(R.string.hint_meal);
                mMeal.price = 0;
                this.mMealsList.add(0, mMeal);
            }
        }

        if (mBaggagesList != null && mBaggagesList.size() > 0) {
            if (mBaggagesList.get(0).id == null || !mBaggagesList.get(0).id.equals("AO")) {
                FlightOneDetailsPojo.Baggage mBaggage = new FlightOneDetailsPojo().new Baggage();
                mBaggage.id = "A0";
                mBaggage.name = mContext.getString(R.string.hint_baggage);
                mBaggage.price = 0;
                this.mBaggagesList.add(0, mBaggage);
            }
        }
    }

    /*public void addAll(List<FlightPerson> mTempPersonList) {

        Log.d("Log", "personList " + mTempPersonList.size());
        if (mPersonList != null && mPersonList.size() > 0) {
            mPersonList.clear();
        }

        if (mTempPersonList != null && mTempPersonList.size() > 0) {
            mPersonList.addAll(mTempPersonList);
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lvPerson.getLayoutParams();
        params.height = (int) (mContext.getResources().getDimension(R.dimen._30sdp) * mPersonList.size());
        lvPerson.setLayoutParams(params);

//        notifyDataSetChanged();
    }*/

    @Override
    public int getCount() {
        return mPersonList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPersonList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.d("Log", "View : " + position);
        if (view == null) {
//            Log.d("Log", "array size" + mPersonList.size());
            if (isInternationalFlight) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_person_details, viewGroup, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_person_details_optional, viewGroup, false);
            }
            ViewHolder holder = new ViewHolder();
            holder.tvPersonCategory = view.findViewById(R.id.tvPersonCategory);
            holder.tvPersonArrow = view.findViewById(R.id.tvPersonArrow);
            holder.tvMr = view.findViewById(R.id.tvMr);
            holder.tvMrs = view.findViewById(R.id.tvMrs);
            holder.tvMs = view.findViewById(R.id.tvMs);
            holder.actvFirstName = view.findViewById(R.id.actvFirstName);
            holder.actvMiddleName = view.findViewById(R.id.actvMiddleName);
            holder.actvLastName = view.findViewById(R.id.actvLastName);
//            holder.tvCountryCode = view.findViewById(R.id.tvCountryCode);
//            holder.actvMobileNumber = view.findViewById(R.id.actvMobileNumber);
            holder.etDOB = view.findViewById(R.id.etDOB);
            holder.actvPassportNumber = view.findViewById(R.id.actvPassportNumber);
            holder.etPassportIssueDate = view.findViewById(R.id.etPassportIssueDate);
            holder.etPassportExpDate = view.findViewById(R.id.etPassportExpDate);
            holder.actvIssuedBy = view.findViewById(R.id.actvIssuedBy);
            holder.actvCitizenship = view.findViewById(R.id.actvCitizenship);
            holder.lnrDetails = view.findViewById(R.id.lnrDetails);
            holder.lnrPerson = view.findViewById(R.id.lnrPerson);
            holder.lnrInternational = view.findViewById(R.id.lnrInternational);
            holder.spinMeal = view.findViewById(R.id.spinMeal);
            holder.spinBaggage = view.findViewById(R.id.spinBaggage);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final FlightPerson mPerson = mPersonList.get(position);

        holder.tvPersonCategory.setTag(position);
        holder.tvPersonArrow.setTag(position);
        holder.tvMr.setTag(position);
        holder.tvMrs.setTag(position);
        holder.tvMs.setTag(position);
        holder.actvFirstName.setTag(position);
        holder.actvMiddleName.setTag(position);
        holder.actvLastName.setTag(position);
        holder.etDOB.setTag(position);
        holder.actvPassportNumber.setTag(position);
        holder.etPassportIssueDate.setTag(position);
        holder.etPassportExpDate.setTag(position);
        holder.actvIssuedBy.setTag(position);
        holder.actvCitizenship.setTag(position);
        holder.lnrDetails.setTag(position);
        holder.lnrPerson.setTag(position);
        holder.lnrInternational.setTag(position);
        holder.spinMeal.setTag(position);
        holder.spinBaggage.setTag(position);

//        if (mPerson != null) {
//            Log.d("Log", position + " - person details count-" + count++);
        holder.tvPersonCategory.setText("" + mPerson.name);

        if (mPerson.name.contains(mContext.getString(R.string.adult))) {
            holder.etDOB.setHint(mContext.getString(R.string.dob_optional));
        } else {
            holder.etDOB.setHint(mContext.getString(R.string.dob));
        }

        if (mVisiblePosition == position) {
            holder.lnrDetails.setVisibility(View.VISIBLE);
            holder.tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_up);
        } else {
            holder.lnrDetails.setVisibility(View.GONE);
            holder.tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_down);
        }

           /* if (!TextUtils.isEmpty(mPersonList.get(position).firstname)) {
                LogUtils.e("", position + " in firstname::" + mPersonList.get(position).firstname);
                Log.d("Log"," first name::" + mPersonList.get(position).firstname);
                holder.actvFirstName.setText(mPersonList.get(position).firstname);
            } else {
                Log.d("Log","bank");
                holder.actvFirstName.setText("");
            }

            if (!TextUtils.isEmpty(mPersonList.get(position).middlename)) {
                holder.actvMiddleName.setText(mPersonList.get(position).middlename);
            }

            if (!TextUtils.isEmpty(mPersonList.get(position).lastname)) {
                LogUtils.e("", position + " in lastname::" + mPersonList.get(position).lastname);
                Log.d("Log"," last name::" + mPersonList.get(position).lastname);
                holder.actvLastName.setText(mPersonList.get(position).lastname);
            } else {
                holder.actvLastName.setText("");
            }*/

        if (mPersonList.get(position).firstname != null) {
            Log.d("Log", position + " Firt name " + mPersonList.get(position).firstname);
            Log.d("Log", position + " Firt name " + mPersonList.get(0).firstname);
            holder.actvFirstName.setText(mPersonList.get(position).firstname);
        }
        if (mPersonList.get(position).middlename != null) {
            holder.actvMiddleName.setText(mPersonList.get(position).middlename);
        }
        if (mPersonList.get(position).lastname != null) {
            holder.actvLastName.setText(mPersonList.get(position).lastname);
        }

        if (!TextUtils.isEmpty(mPersonList.get(position).passportNumber)) {
            holder.actvPassportNumber.setText(mPersonList.get(position).passportNumber);
        }

        if (!TextUtils.isEmpty(mPersonList.get(position).issuedBy)) {
            holder.actvIssuedBy.setText(mPersonList.get(position).issuedBy);
        }

        if (!TextUtils.isEmpty(mPersonList.get(position).citizenShip)) {
            holder.actvCitizenship.setText(mPersonList.get(position).citizenShip);
        }

        ArrayAdapter<String> mIssueAdapter = new ArrayAdapter<String>
                (mContext, R.layout.autocomplete_drop_down_item, mNationalityList);
        holder.actvIssuedBy.setAdapter(mIssueAdapter);

        ArrayAdapter<String> mCitizenshipAdapter = new ArrayAdapter<String>
                (mContext, R.layout.autocomplete_drop_down_item, mNationalityList);
        holder.actvCitizenship.setAdapter(mCitizenshipAdapter);

        if (mMealsList != null && mMealsList.size() > 0) {
            holder.spinMeal.setVisibility(View.VISIBLE);
//                AddonsMealAdapter mAddonsMealAdapter = new AddonsMealAdapter(mContext, mMealsList, mCurrency);
            AddonsMealSpinAdapter mAddonsMealAdapter = new AddonsMealSpinAdapter(mContext, R.layout.list_item_addons, mMealsList, mCurrency, holder.spinMeal, new AddonsMealSpinAdapter.OnMealSelectListener() {
                @Override
                public void onMealSelected(int pos) {
                    if (holder.spinMeal.getTag() != null && (int) holder.spinMeal.getTag() == position) {
                        holder.spinMeal.setSelection(pos);
                        if (pos > 0) {
                            mPerson.mMeal = mMealsList.get(pos);
                            mPerson.mMealPosition = pos;
                            onAddonsListener.onAddonsSelected(pos);
                        }
                    }
                }
            });
            holder.spinMeal.setAdapter(mAddonsMealAdapter);
//                if (mPerson.mMeal != null && !TextUtils.isEmpty(mPerson.mMeal.code)) {
//                    for (int i = 0; i < mMealsList.size(); i++) {
//                        LogUtils.e("", i+" "+mMealsList.get(i).code+" code::"+mPerson.mMeal.code);
//                        if (mMealsList.get(i).code.equalsIgnoreCase(mPerson.mMeal.code)) {
            LogUtils.e("", position + " mPerson.mMealPosition::" + mPerson.mMealPosition);
            holder.spinMeal.setSelection(mPerson.mMealPosition);
//                            break;
//                        }
//                    }
//                }
        } else {
            holder.spinMeal.setVisibility(View.GONE);
        }

        if (mBaggagesList != null && mBaggagesList.size() > 0) {
            holder.spinBaggage.setVisibility(View.VISIBLE);
//                AddonsBaggageAdapter mAddonsBaggageAdapter = new AddonsBaggageAdapter(mContext, mBaggagesList, mCurrency);
            AddonsBaggageSpinAdapter mAddonsBaggageAdapter = new AddonsBaggageSpinAdapter(mContext, R.layout.list_item_addons, mBaggagesList, mCurrency, holder.spinBaggage, new AddonsBaggageSpinAdapter.OnBaggageSelectListener() {
                @Override
                public void onBaggageSelected(int pos) {
                    if (holder.spinBaggage.getTag() != null && (int) holder.spinBaggage.getTag() == position) {
                        holder.spinBaggage.setSelection(pos);
                        if (pos > 0) {
                            mPerson.mBaggage = mBaggagesList.get(pos);
                            mPerson.mBaggagePosition = pos;
                            onAddonsListener.onAddonsSelected(pos);
                        }
                    }
                }
            });
            holder.spinBaggage.setAdapter(mAddonsBaggageAdapter);
//                if (mPerson.mBaggage != null && !TextUtils.isEmpty(mPerson.mBaggage.code)) {
//                    for (int i = 0; i < mBaggagesList.size(); i++) {
//                        LogUtils.e("", i+" "+mBaggagesList.get(i).code+" code::"+mPerson.mBaggage.code);
//                        if (mBaggagesList.get(i).code.equalsIgnoreCase(mPerson.mBaggage.code)) {
            LogUtils.e("", position + " mPerson.mBaggagePosition::" + mPerson.mBaggagePosition);
            holder.spinBaggage.setSelection(mPerson.mBaggagePosition);
//                            break;
//                        }
//                    }
//                }
        } else {
            holder.spinBaggage.setVisibility(View.GONE);
        }


        holder.tvMr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.tvMs.getTag() != null && (int) holder.tvMs.getTag() == position) {

                    mPerson.type = "Mr";

                    holder.tvMr.setBackgroundResource(R.drawable.bg_button_batli_gender);
                    holder.tvMr.setTextColor(mContext.getResources().getColor(R.color.white));

                    holder.tvMrs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                    holder.tvMrs.setTextColor(mContext.getResources().getColor(R.color.batli_gender));

                    holder.tvMs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                    holder.tvMs.setTextColor(mContext.getResources().getColor(R.color.batli_gender));

                }
            }
        });

        holder.tvMrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.tvMs.getTag() != null && (int) holder.tvMs.getTag() == position) {

                    mPerson.type = "Mrs";

                    holder.tvMrs.setBackgroundResource(R.drawable.bg_button_batli_gender);
                    holder.tvMrs.setTextColor(mContext.getResources().getColor(R.color.white));

                    holder.tvMr.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                    holder.tvMr.setTextColor(mContext.getResources().getColor(R.color.batli_gender));

                    holder.tvMs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                    holder.tvMs.setTextColor(mContext.getResources().getColor(R.color.batli_gender));

                }
            }
        });

        holder.tvMs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.tvMs.getTag() != null && (int) holder.tvMs.getTag() == position) {

                    mPerson.type = "Ms";

                    holder.tvMs.setBackgroundResource(R.drawable.bg_button_batli_gender);
                    holder.tvMs.setTextColor(mContext.getResources().getColor(R.color.white));

                    holder.tvMrs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                    holder.tvMrs.setTextColor(mContext.getResources().getColor(R.color.batli_gender));

                    holder.tvMr.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                    holder.tvMr.setTextColor(mContext.getResources().getColor(R.color.batli_gender));
                }
            }
        });

        /*holder.actvFirstName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        holder.actvFirstName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });
        holder.actvMiddleName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        holder.actvMiddleName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        holder.actvLastName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        holder.actvLastName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });*/

        holder.actvFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("Log","FIRST NAME : "+holder.actvFirstName.getText().toString());
                if (holder.actvFirstName.getTag() != null && (int) holder.actvFirstName.getTag() == position) {
                    LogUtils.e("", position + " firstname onTextChanged:" + charSequence.toString() + " tag" + holder.actvFirstName.getTag());
                    if (charSequence.length() > 0) {
                        mPersonList.get(position).firstname = charSequence.toString();
                    } else {
                        mPersonList.get(position).firstname = "";
                    }
                    holder.actvFirstName.setSelection(holder.actvFirstName.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.actvMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.actvMiddleName.getTag() != null && (int) holder.actvMiddleName.getTag() == position) {
                    LogUtils.e("", position + " middle onTextChanged:" + charSequence.toString() + " tag" + holder.actvMiddleName.getTag());
                    if (charSequence.length() > 0) {
                        mPersonList.get(position).middlename = charSequence.toString();
                    } else {
                        mPersonList.get(position).middlename = "";
                    }
                    holder.actvMiddleName.setSelection(holder.actvMiddleName.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.actvLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.actvLastName.getTag() != null && (int) holder.actvLastName.getTag() == position) {
                    LogUtils.e("", position + " lastname onTextChanged:" + charSequence.toString() + " tag" + holder.actvLastName.getTag());
                    if (charSequence.length() > 0) {
                        mPersonList.get(position).lastname = charSequence.toString();
                    } else {
                        mPersonList.get(position).lastname = "";
                    }
                    holder.actvLastName.setSelection(holder.actvLastName.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

//            holder.actvMobileNumber.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (charSequence.length() > 0) {
//                        mPerson.phone = holder.tvCountryCode.getText().toString() + "" + charSequence.toString();
//                    } else {
//                        mPerson.phone = "";
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                }
//            });

        holder.actvPassportNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.actvPassportNumber.getTag() != null && (int) holder.actvPassportNumber.getTag() == position) {
                    if (charSequence.length() > 0) {
                        mPerson.passportNumber = charSequence.toString();
                    } else {
                        mPerson.passportNumber = "";
                    }
                    holder.actvPassportNumber.setSelection(holder.actvPassportNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        holder.actvIssuedBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.actvIssuedBy.getTag() != null && (int) holder.actvIssuedBy.getTag() == position) {
                    if (charSequence.length() > 0) {
                        mPerson.issuedBy = charSequence.toString();
                        LogUtils.e("", "mPerson.issuedBy::" + mPerson.issuedBy);
                    } else {
                        mPerson.issuedBy = "";
                    }
                    holder.actvIssuedBy.setSelection(holder.actvIssuedBy.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        holder.actvCitizenship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.actvCitizenship.getTag() != null && (int) holder.actvCitizenship.getTag() == position) {
                    if (charSequence.length() > 0) {
                        mPerson.citizenShip = charSequence.toString();
                        LogUtils.e("", "mPerson.citizenShip::" + mPerson.citizenShip);
                    } else {
                        mPerson.citizenShip = "";
                    }
                    holder.actvCitizenship.setSelection(holder.actvCitizenship.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        holder.lnrPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVisiblePosition = position;
                notifyDataSetChanged();

//                    if (holder.lnrDetails.getVisibility() == View.VISIBLE) {
//                        holder.lnrDetails.setVisibility(View.GONE);
//                        holder.tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_down);
//                        mVisibleCount--;
//                    } else {
//                        holder.lnrDetails.setVisibility(View.VISIBLE);
//                        holder.tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_up);
//                        mVisibleCount++;
//
//                    }

//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lvPerson.getLayoutParams();
//                    params.height = (int) (mContext.getResources().getDimension(R.dimen._30sdp) * mPersonList.size()) +  (int) (mContext.getResources().getDimension(R.dimen._250sdp) * mVisibleCount);
//                    lvPerson.setLayoutParams(params);
//                    lvPerson.requestLayout();

            }
        });

        holder.etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(holder, mPerson, 1, position);
            }
        });

        holder.etPassportIssueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(holder, mPerson, 2, position);
            }
        });

        holder.etPassportExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(holder, mPerson, 3, position);
            }
        });
//        }

        return view;
    }


    private class ViewHolder {
        TextView tvPersonCategory, tvPersonArrow, tvMr, tvMrs, tvMs; //tvCountryCode
        EditText etDOB, etPassportIssueDate, etPassportExpDate;
        Spinner spinMeal, spinBaggage;
        AutoCompleteTextView actvIssuedBy, actvCitizenship, actvPassportNumber, actvFirstName, actvMiddleName, actvLastName; // actvMobileNumber
        LinearLayout lnrDetails, lnrPerson, lnrInternational;

    }

    public List<FlightPerson> getPersonData() {
        return mPersonList;
    }


    private void showDatePickerDialog(final ViewHolder holder, final FlightPerson mPerson, final int type, int position) {

        int mStartYear, mStartMonth, mStartDay;
        final Calendar mStartCalDate = Calendar.getInstance();
        if (type == 1 || type == 2) {
//            mStartCalDate.add(Calendar.YEAR, -1);
        }

        mStartYear = mStartCalDate.get(Calendar.YEAR);
        mStartMonth = mStartCalDate.get(Calendar.MONTH);
        mStartDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateStartPickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mStartCalDate.set(Calendar.YEAR, year);
                        mStartCalDate.set(Calendar.MONTH, monthOfYear);
                        mStartCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (type == 1) {

                            if (holder.etDOB.getTag() != null && (int) holder.etDOB.getTag() == position) {
                                if (mPerson.name.contains(mContext.getString(R.string.adult))) {

                                    int yearAdult = Utils.getYearTimeDifference(mStartCalDate.getTime(), new Date());
                                    LogUtils.e("", "yearAdult::" + yearAdult);
                                    if (yearAdult > 12) {

                                        holder.etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                        mPerson.dob = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                        mPerson.displayDOB = mStartCalDate.getTimeInMillis();
                                    } else {
                                        Utils.showToast(mContext, mContext.getString(R.string.msg_adult_age));
                                    }

                                } else if (mPerson.name.contains(mContext.getString(R.string.child))) {

                                    int yearChild = Utils.getYearTimeDifference(mStartCalDate.getTime(), new Date());
                                    LogUtils.e("", "yearChild::" + yearChild);
                                    if (yearChild >= 2 && yearChild <= 12) {

                                        holder.etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                        mPerson.dob = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                        mPerson.displayDOB = mStartCalDate.getTimeInMillis();
                                    } else {
                                        Utils.showToast(mContext, mContext.getString(R.string.msg_children_age));
                                    }

                                } else if (mPerson.name.contains(mContext.getString(R.string.infant))) {

                                    int yearInfant = Utils.getYearTimeDifference(mStartCalDate.getTime(), new Date());
                                    LogUtils.e("", "yearInfant::" + yearInfant);
                                    if (yearInfant < 2) {

                                        holder.etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                        mPerson.dob = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                        mPerson.displayDOB = mStartCalDate.getTimeInMillis();
                                    } else {
                                        Utils.showToast(mContext, mContext.getString(R.string.msg_infant_age));
                                    }
                                }

                            }

                        } else if (type == 2) {

                            if (holder.etPassportIssueDate.getTag() != null && (int) holder.etPassportIssueDate.getTag() == position) {

                                holder.etPassportIssueDate.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                mPerson.passportIssueDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                mPerson.displayIssueExpDate = mStartCalDate.getTimeInMillis();
                            }

                        } else if (type == 3) {

                            if (holder.etPassportExpDate.getTag() != null && (int) holder.etPassportExpDate.getTag() == position) {
                                holder.etPassportExpDate.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                mPerson.passportExpDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                mPerson.displayPassportExpDate = mStartCalDate.getTimeInMillis();
                            }
                        }


                    }
                }, mStartYear, mStartMonth, mStartDay);


        if (type == 1 || type == 2) {
            Calendar mStartCal = Calendar.getInstance();
            dateStartPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 1000);
        } else {
            Calendar mStartCal = Calendar.getInstance();
            dateStartPickerDialog.getDatePicker().setMinDate(mStartCal.getTimeInMillis() - 1000);
//            mStartCal.add(Calendar.MONTH, 6);
//            dateStartPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 1000);
        }


        dateStartPickerDialog.show();

    }
}
