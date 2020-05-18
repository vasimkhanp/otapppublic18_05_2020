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
import android.widget.Toast;

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

public class FlightPersonDetailsAdapter extends BaseAdapter {

    private boolean isInternationalFlight;
    Context context;
    List<FlightPerson> mPersonList = new ArrayList<>();
    private ListView lvPerson;
    List<String> mNationalityList;
    private int mVisibleCount = 0, mVisiblePosition = 0;
    String mCurrency = "";
    private FlightPersonAdapter.OnAddonsListener onAddonsListener;

    List<FlightOneDetailsPojo.Meal> mMealsList;
    List<FlightOneDetailsPojo.Baggage> mBaggagesList;

    FlightOneDetailsPojo mFlightOneDetailsPojo;
    TextView tvPersonCategory, tvPersonArrow, tvMr, tvMrs, tvMs; //tvCountryCode
    EditText etDOB, etPassportIssueDate, etPassportExpDate;
    Spinner spinMeal, spinBaggage;
    AutoCompleteTextView actvIssuedBy, actvCitizenship, actvPassportNumber, actvFirstName, actvMiddleName, actvLastName; // actvMobileNumber
    LinearLayout lnrDetails, lnrPerson, lnrInternational;

    public FlightPersonDetailsAdapter(Context context, List<FlightPerson> mPersonList, FlightOneDetailsPojo mFlightOneDetailsPojo, boolean isInternationalFlight,
                                      ListView lvPerson, List<String> mNationalityList, FlightPersonAdapter.OnAddonsListener onAddonsListener) {
        this.context = context;
        this.mPersonList = mPersonList;
        this.mNationalityList = mNationalityList;
        this.onAddonsListener = onAddonsListener;
        this.lvPerson = lvPerson;
        this.mFlightOneDetailsPojo = mFlightOneDetailsPojo;
        this.isInternationalFlight = isInternationalFlight;

        if (mFlightOneDetailsPojo != null) {
            mMealsList = mFlightOneDetailsPojo.data.meals;
            mBaggagesList = mFlightOneDetailsPojo.data.baggages;
            mCurrency = mFlightOneDetailsPojo.data.currency;


            if (mMealsList != null && mMealsList.size() > 0) {
                if (mMealsList.get(0).id == null || !mMealsList.get(0).id.equals("AO")) {
                    FlightOneDetailsPojo.Meal mMeal = new FlightOneDetailsPojo().new Meal();
                    mMeal.id = "A0";
                    mMeal.name = context.getString(R.string.hint_meal);
                    mMeal.price = 0;
                    this.mMealsList.add(0, mMeal);
                }
            }

            if (mBaggagesList != null && mBaggagesList.size() > 0) {
                if (mBaggagesList.get(0).id == null || !mBaggagesList.get(0).id.equals("AO")) {
                    FlightOneDetailsPojo.Baggage mBaggage = new FlightOneDetailsPojo().new Baggage();
                    mBaggage.id = "A0";
                    mBaggage.name = context.getString(R.string.hint_baggage);
                    mBaggage.price = 0;
                    this.mBaggagesList.add(0, mBaggage);
                }
            }
        } else {
            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getCount() {
        return mPersonList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_flight_person_details, parent, false);
       /* if (isInternationalFlight) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_flight_person_details, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_flight_person_details_optional, null);
        }*/

        tvPersonCategory = view.findViewById(R.id.tvPersonCategory);
        tvPersonArrow = view.findViewById(R.id.tvPersonArrow);
        tvMr = view.findViewById(R.id.tvMr);
        tvMrs = view.findViewById(R.id.tvMrs);
        tvMs = view.findViewById(R.id.tvMs);
        actvFirstName = view.findViewById(R.id.actvFirstName);
        actvMiddleName = view.findViewById(R.id.actvMiddleName);
        actvLastName = view.findViewById(R.id.actvLastName);
        etDOB = view.findViewById(R.id.etDOB);
        actvPassportNumber = view.findViewById(R.id.actvPassportNumber);
        etPassportIssueDate = view.findViewById(R.id.etPassportIssueDate);
        etPassportExpDate = view.findViewById(R.id.etPassportExpDate);
        actvIssuedBy = view.findViewById(R.id.actvIssuedBy);
        actvCitizenship = view.findViewById(R.id.actvCitizenship);
        lnrDetails = view.findViewById(R.id.lnrDetails);
        lnrPerson = view.findViewById(R.id.lnrPerson);
        lnrInternational = view.findViewById(R.id.lnrInternational);
        spinMeal = view.findViewById(R.id.spinMeal);
        spinBaggage = view.findViewById(R.id.spinBaggage);

        FlightPerson mPerson = mPersonList.get(position);
        if (position == 0) {
            if (mPersonList.get(position).firstname != null) {
                Log.d("Log", position + " Firt name " + mPersonList.get(position).firstname);
                Log.d("Log", position + " Firt name " + mPersonList.get(0).firstname);
                actvFirstName.setText(mPersonList.get(position).firstname);
            }
            if (mPersonList.get(position).middlename != null) {
                actvMiddleName.setText(mPersonList.get(position).middlename);
            }
            if (mPersonList.get(position).lastname != null) {
                actvLastName.setText(mPersonList.get(position).lastname);
            }
        }

//        if (mPerson != null) {
//            Log.d("Log", position + " - person details count-" + count++);
        tvPersonCategory.setText("" + mPerson.name);

        if (etDOB.getText().toString().endsWith("")) {
            if (mPerson.name.contains(context.getString(R.string.adult))) {
                etDOB.setHint(context.getString(R.string.dob_optional));
            } else {
                etDOB.setHint(context.getString(R.string.dob));
            }
        }

        if (mVisiblePosition == position) {
            lnrDetails.setVisibility(View.VISIBLE);
            tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_up);
        } else {
            lnrDetails.setVisibility(View.GONE);
            tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_down);
        }

        if (position == 0) {
            if (mPersonList.get(position).firstname != null) {
                Log.d("Log", position + " Firt name " + mPersonList.get(position).firstname);
                Log.d("Log", position + " Firt name " + mPersonList.get(0).firstname);
                actvFirstName.setText(mPersonList.get(position).firstname);
            }
            if (mPersonList.get(position).middlename != null) {
                actvMiddleName.setText(mPersonList.get(position).middlename);
            }
            if (mPersonList.get(position).lastname != null) {
                actvLastName.setText(mPersonList.get(position).lastname);
            }
        }


        if (!TextUtils.isEmpty(mPersonList.get(position).passportNumber)) {
            actvPassportNumber.setText(mPersonList.get(position).passportNumber);
        }

        if (!TextUtils.isEmpty(mPersonList.get(position).issuedBy)) {
            actvIssuedBy.setText(mPersonList.get(position).issuedBy);
        }

        if (!TextUtils.isEmpty(mPersonList.get(position).citizenShip)) {
            actvCitizenship.setText(mPersonList.get(position).citizenShip);
        }

        ArrayAdapter<String> mIssueAdapter = new ArrayAdapter<String>
                (context, R.layout.autocomplete_drop_down_item, mNationalityList);
        actvIssuedBy.setAdapter(mIssueAdapter);

        ArrayAdapter<String> mCitizenshipAdapter = new ArrayAdapter<String>
                (context, R.layout.autocomplete_drop_down_item, mNationalityList);
        actvCitizenship.setAdapter(mCitizenshipAdapter);

        if (mMealsList != null && mMealsList.size() > 0) {
            spinMeal.setVisibility(View.VISIBLE);
//                AddonsMealAdapter mAddonsMealAdapter = new AddonsMealAdapter(context, mMealsList, mCurrency);
            AddonsMealSpinAdapter mAddonsMealAdapter = new AddonsMealSpinAdapter(context, R.layout.list_item_addons, mMealsList, mCurrency, spinMeal, new AddonsMealSpinAdapter.OnMealSelectListener() {
                @Override
                public void onMealSelected(int pos) {
//                    if (spinMeal.getTag() != null && (int) spinMeal.getTag() == position) {
                    spinMeal.setSelection(pos);
                    if (pos > 0) {
                        mPerson.mMeal = mMealsList.get(pos);
                        mPerson.mMealPosition = pos;
                        onAddonsListener.onAddonsSelected(pos);
                    }
//                    }
                }
            });
            spinMeal.setAdapter(mAddonsMealAdapter);
//                if (mPerson.mMeal != null && !TextUtils.isEmpty(mPerson.mMeal.code)) {
//                    for (int i = 0; i < mMealsList.size(); i++) {
//                        LogUtils.e("", i+" "+mMealsList.get(i).code+" code::"+mPerson.mMeal.code);
//                        if (mMealsList.get(i).code.equalsIgnoreCase(mPerson.mMeal.code)) {
            LogUtils.e("", position + " mPerson.mMealPosition::" + mPerson.mMealPosition);
            spinMeal.setSelection(mPerson.mMealPosition);
//                            break;
//                        }
//                    }
//                }
        } else {
            spinMeal.setVisibility(View.GONE);
        }

        if (mBaggagesList != null && mBaggagesList.size() > 0) {
            spinBaggage.setVisibility(View.VISIBLE);
//                AddonsBaggageAdapter mAddonsBaggageAdapter = new AddonsBaggageAdapter(context, mBaggagesList, mCurrency);
            AddonsBaggageSpinAdapter mAddonsBaggageAdapter = new AddonsBaggageSpinAdapter(context, R.layout.list_item_addons, mBaggagesList, mCurrency, spinBaggage, new AddonsBaggageSpinAdapter.OnBaggageSelectListener() {
                @Override
                public void onBaggageSelected(int pos) {
//                    if (spinBaggage.getTag() != null && (int) spinBaggage.getTag() == position) {
                    spinBaggage.setSelection(pos);
                    if (pos > 0) {
                        mPerson.mBaggage = mBaggagesList.get(pos);
                        mPerson.mBaggagePosition = pos;
                        onAddonsListener.onAddonsSelected(pos);
                    }
//                    }
                }
            });
            spinBaggage.setAdapter(mAddonsBaggageAdapter);
//                if (mPerson.mBaggage != null && !TextUtils.isEmpty(mPerson.mBaggage.code)) {
//                    for (int i = 0; i < mBaggagesList.size(); i++) {
//                        LogUtils.e("", i+" "+mBaggagesList.get(i).code+" code::"+mPerson.mBaggage.code);
//                        if (mBaggagesList.get(i).code.equalsIgnoreCase(mPerson.mBaggage.code)) {
            LogUtils.e("", position + " mPerson.mBaggagePosition::" + mPerson.mBaggagePosition);
            spinBaggage.setSelection(mPerson.mBaggagePosition);
//                            break;
//                        }
//                    }
//                }
        } else {
            spinBaggage.setVisibility(View.GONE);
        }


        tvMr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mPerson.type = "Mr";

                tvMr.setBackgroundResource(R.drawable.bg_button_batli_gender);
                tvMr.setTextColor(context.getResources().getColor(R.color.white));

                tvMrs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                tvMrs.setTextColor(context.getResources().getColor(R.color.batli_gender));

                tvMs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                tvMs.setTextColor(context.getResources().getColor(R.color.batli_gender));

            }

        });

        tvMrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    if (tvMs.getTag() != null && (int) tvMs.getTag() == position) {

                mPerson.type = "Mrs";

                tvMrs.setBackgroundResource(R.drawable.bg_button_batli_gender);
                tvMrs.setTextColor(context.getResources().getColor(R.color.white));

                tvMr.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                tvMr.setTextColor(context.getResources().getColor(R.color.batli_gender));

                tvMs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                tvMs.setTextColor(context.getResources().getColor(R.color.batli_gender));

            }

//                }
        });

        tvMs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    if (tvMs.getTag() != null && (int) tvMs.getTag() == position) {

                mPerson.type = "Ms";

                tvMs.setBackgroundResource(R.drawable.bg_button_batli_gender);
                tvMs.setTextColor(context.getResources().getColor(R.color.white));

                tvMrs.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                tvMrs.setTextColor(context.getResources().getColor(R.color.batli_gender));

                tvMr.setBackgroundResource(R.drawable.bg_rectangle_border_batli_gender);
                tvMr.setTextColor(context.getResources().getColor(R.color.batli_gender));
            }

//                }
        });

            /*actvFirstName.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_FLAG_CAP_WORDS);

            actvFirstName.setFilters(new InputFilter[]{
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
            actvMiddleName.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_FLAG_CAP_WORDS);

            actvMiddleName.setFilters(new InputFilter[]{
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

            actvLastName.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_FLAG_CAP_WORDS);

            actvLastName.setFilters(new InputFilter[]{
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

        actvFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (actvFirstName.getTag() != null && (int) actvFirstName.getTag() == position) {
                LogUtils.e("", position + " firstname onTextChanged:" + charSequence.toString() + " tag" + actvFirstName.getTag());
                if (charSequence.length() > 0) {
                    mPersonList.get(position).firstname = charSequence.toString();
                } else {
                    mPersonList.get(position).firstname = "";
                }
                actvFirstName.setSelection(actvFirstName.getText().length());
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        actvMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (actvMiddleName.getTag() != null && (int) actvMiddleName.getTag() == position) {
                LogUtils.e("", position + " middle onTextChanged:" + charSequence.toString() + " tag" + actvMiddleName.getTag());
                if (charSequence.length() > 0) {
                    mPersonList.get(position).middlename = charSequence.toString();
                } else {
                    mPersonList.get(position).middlename = "";
                }
                actvMiddleName.setSelection(actvMiddleName.getText().length());
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        actvLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (actvLastName.getTag() != null && (int) actvLastName.getTag() == position) {
                LogUtils.e("", position + " lastname onTextChanged:" + charSequence.toString() + " tag" + actvLastName.getTag());
                if (charSequence.length() > 0) {
                    mPersonList.get(position).lastname = charSequence.toString();
                } else {
                    mPersonList.get(position).lastname = "";
                }
                actvLastName.setSelection(actvLastName.getText().length());
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

//            actvMobileNumber.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (charSequence.length() > 0) {
//                        mPerson.phone = tvCountryCode.getText().toString() + "" + charSequence.toString();
//                    } else {
//                        mPerson.phone = "";
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                }
//            });

        actvPassportNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (actvPassportNumber.getTag() != null && (int) actvPassportNumber.getTag() == position) {
                if (charSequence.length() > 0) {
                    mPerson.passportNumber = charSequence.toString();
                } else {
                    mPerson.passportNumber = "";
                }
                actvPassportNumber.setSelection(actvPassportNumber.getText().length());
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        actvIssuedBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (actvIssuedBy.getTag() != null && (int) actvIssuedBy.getTag() == position) {
                if (charSequence.length() > 0) {
                    mPerson.issuedBy = charSequence.toString();
                    LogUtils.e("", "mPerson.issuedBy::" + mPerson.issuedBy);
                } else {
                    mPerson.issuedBy = "";
                }
                actvIssuedBy.setSelection(actvIssuedBy.getText().length());
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        actvCitizenship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (actvCitizenship.getTag() != null && (int) actvCitizenship.getTag() == position) {
                if (charSequence.length() > 0) {
                    mPerson.citizenShip = charSequence.toString();
                    LogUtils.e("", "mPerson.citizenShip::" + mPerson.citizenShip);
                } else {
                    mPerson.citizenShip = "";
                }
                actvCitizenship.setSelection(actvCitizenship.getText().length());
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        lnrPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVisiblePosition = position;
//                notifyDataSetChanged();

//                    if (lnrDetails.getVisibility() == View.VISIBLE) {
//                        lnrDetails.setVisibility(View.GONE);
//                        tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_down);
//                        mVisibleCount--;
//                    } else {
//                        lnrDetails.setVisibility(View.VISIBLE);
//                        tvPersonArrow.setBackgroundResource(R.drawable.ic_arrow_up);
//                        mVisibleCount++;
//
//                    }

//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lvPerson.getLayoutParams();
//                    params.height = (int) (context.getResources().getDimension(R.dimen._30sdp) * mPersonList.size()) +  (int) (context.getResources().getDimension(R.dimen._250sdp) * mVisibleCount);
//                    lvPerson.setLayoutParams(params);
//                    lvPerson.requestLayout();

            }
        });


//        }

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDOB.requestFocus();
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        int yearAdult = Utils.getYearTimeDifference(calendar.getTime(), new Date());
                        Log.d("Log", "Year Diff : " + yearAdult);

//                        Toast.makeText(context, "Birth date : "+DateFormate.sdfPersonDisplayDate.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                        if (mPerson.name.contains(context.getString(R.string.adult))) {
                            if (yearAdult > 12) {
                                Log.d("Log", "True" + DateFormate.sdfPersonDisplayDate.format(calendar.getTime()));
                                etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(calendar.getTime().getTime()));
                                mPerson.dob = DateFormate.sdfFlightServerDate.format(calendar.getTime());
                                mPerson.displayDOB = calendar.getTimeInMillis();
                            } else {
                                Log.d("Log", "False");
                                Utils.showToast(context, context.getString(R.string.msg_adult_age));
                            }
                        } else if (mPerson.name.contains(context.getString(R.string.child))) {
                            int yearChild = Utils.getYearTimeDifference(calendar.getTime(), new Date());
                            LogUtils.e("", "yearChild::" + yearChild);
                            if (yearChild >= 2 && yearChild <= 12) {

                                etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(calendar.getTime()));
                                mPerson.dob = DateFormate.sdfFlightServerDate.format(calendar.getTime());
                                mPerson.displayDOB = calendar.getTimeInMillis();
                            } else {
                                Utils.showToast(context, context.getString(R.string.msg_children_age));
                            }
                        } else if (mPerson.name.contains(context.getString(R.string.child))) {
                            int yearInfant = Utils.getYearTimeDifference(calendar.getTime(), new Date());
                            LogUtils.e("", "yearInfant::" + yearInfant);
                            if (yearInfant < 2) {

                                etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(calendar.getTime()));
                                mPerson.dob = DateFormate.sdfFlightServerDate.format(calendar.getTime());
                                mPerson.displayDOB = calendar.getTimeInMillis();
                            } else {
                                Utils.showToast(context, context.getString(R.string.msg_infant_age));
                            }
                        }
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
                if (!datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
//                    showDatePickerDialog(mPerson, 1, position);
            }
        });

        etPassportIssueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPassportIssueDate.requestFocus();
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
//                        Toast.makeText(context, "Issue date : "+DateFormate.sdfPersonDisplayDate.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                        etPassportIssueDate.setText("" + DateFormate.sdfPersonDisplayDate.format(calendar.getTime()));
                        mPerson.passportIssueDate = DateFormate.sdfFlightServerDate.format(calendar.getTime());
                        mPerson.displayIssueExpDate = calendar.getTimeInMillis();
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
                if (!datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
            }
        });

        etPassportExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPassportExpDate.requestFocus();
//                    showDatePickerDialog(mPerson, 3, position);
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
//                        Toast.makeText(context, "Expiry date : "+DateFormate.sdfPersonDisplayDate.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                        etPassportExpDate.setText("" + DateFormate.sdfPersonDisplayDate.format(calendar.getTime()));
                        mPerson.passportExpDate = DateFormate.sdfFlightServerDate.format(calendar.getTime());
                        mPerson.displayPassportExpDate = calendar.getTimeInMillis();
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
                if (!datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
            }
        });
        return view;
    }


   /* private void showDatePickerDialog(FlightPerson mPerson, final int type, int position) {

        int mStartYear, mStartMonth, mStartDay;
        final Calendar mStartCalDate = Calendar.getInstance();
        if (type == 1 || type == 2) {
//            mStartCalDate.add(Calendar.YEAR, -1);
        }

        mStartYear = mStartCalDate.get(Calendar.YEAR);
        mStartMonth = mStartCalDate.get(Calendar.MONTH);
        mStartDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateStartPickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mStartCalDate.set(Calendar.YEAR, year);
                        mStartCalDate.set(Calendar.MONTH, monthOfYear);
                        mStartCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Toast.makeText(context, "Date : " + mStartCalDate.getTime().getTime(), Toast.LENGTH_SHORT).show();

                        if (type == 1) {

//                            if (etDOB.getTag() != null && (int) etDOB.getTag() == position) {
                            if (mPerson.name.contains(context.getString(R.string.adult))) {

                                int yearAdult = Utils.getYearTimeDifference(mStartCalDate.getTime(), new Date());
                                Log.d("Log", "Date : " + yearAdult);
                                LogUtils.e("", "yearAdult::" + yearAdult);
                                if (yearAdult > 12) {
                                    Log.d("Log", "True");
                                    etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                    mPerson.dob = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                    mPerson.displayDOB = mStartCalDate.getTimeInMillis();
                                } else {
                                    Log.d("Log", "False");
                                    Utils.showToast(context, context.getString(R.string.msg_adult_age));
                                }

                            } else if (mPerson.name.contains(context.getString(R.string.child))) {

                                int yearChild = Utils.getYearTimeDifference(mStartCalDate.getTime(), new Date());
                                LogUtils.e("", "yearChild::" + yearChild);
                                if (yearChild >= 2 && yearChild <= 12) {

                                    etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                    mPerson.dob = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                    mPerson.displayDOB = mStartCalDate.getTimeInMillis();
                                } else {
                                    Utils.showToast(context, context.getString(R.string.msg_children_age));
                                }

                            } else if (mPerson.name.contains(context.getString(R.string.child))) {

                                int yearInfant = Utils.getYearTimeDifference(mStartCalDate.getTime(), new Date());
                                LogUtils.e("", "yearInfant::" + yearInfant);
                                if (yearInfant < 2) {

                                    etDOB.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                                    mPerson.dob = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                    mPerson.displayDOB = mStartCalDate.getTimeInMillis();
                                } else {
                                    Utils.showToast(context, context.getString(R.string.msg_infant_age));
                                }
                            }

//                            }

                        } else if (type == 2) {

//                            if (etPassportIssueDate.getTag() != null && (int) etPassportIssueDate.getTag() == position) {

                            etPassportIssueDate.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                            mPerson.passportIssueDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                            mPerson.displayIssueExpDate = mStartCalDate.getTimeInMillis();
//                            }

                        } else if (type == 3) {

//                            if (etPassportExpDate.getTag() != null && (int) etPassportExpDate.getTag() == position) {
                            etPassportExpDate.setText("" + DateFormate.sdfPersonDisplayDate.format(mStartCalDate.getTime()));
                            mPerson.passportExpDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                            mPerson.displayPassportExpDate = mStartCalDate.getTimeInMillis();
//                            }
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

    }*/
}
