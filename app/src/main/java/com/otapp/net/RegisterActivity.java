package com.otapp.net;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.adapter.CountryCodeListAdapter;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tvRegisterTermsPrivacy)
    TextView tvRegisterTermsPrivacy;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvRegisterLoginHere)
    TextView tvRegisterLoginHere;
    //    @BindView(R.id.tvRegisterCountryCode)
//    TextView tvRegisterCountryCode;
    @BindView(R.id.spinCountryCode)
    TextView spinCountryCode;
    @BindView(R.id.tvRegisterSkip)
    TextView tvRegisterSkip;
    @BindView(R.id.etRegisterEmailID)
    EditText etRegisterEmailID;
    @BindView(R.id.etRegisterMobileNumber)
    EditText etRegisterMobileNumber;
    @BindView(R.id.etRegisterCreatePassword)
    EditText etRegisterCreatePassword;
    @BindView(R.id.etRegisterFirstName)
    EditText etRegisterFirstName;
    @BindView(R.id.etRegisterLastName)
    EditText etRegisterLastName;
    String prevString = "";

    private String mScreenName = "";
    private int mMobNumberMaxLength = 9;

    List<CountryCodePojo.CountryCode> tempCountryCodeList;
    int countryCodeFlag=0;
    CountryCodeListAdapter mCountryCodeSpinAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        InitializeControls();
    }

    private void InitializeControls() {

        spinCountryCode.setText("+255");
        tvRegisterSkip.setVisibility(View.VISIBLE);

        tvRegisterLoginHere.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvRegisterSkip.setOnClickListener(this);
        spinCountryCode.setText("+255");
        etRegisterMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.BNDL_SCREEN_NAME)) {
            mScreenName = bundle.getString(Constants.BNDL_SCREEN_NAME, "");
            tvRegisterSkip.setVisibility(View.GONE);
        }

        if (Otapp.mCountryCodePojoList != null && Otapp.mCountryCodePojoList.size() > 0) {
         //   setSpinCountryCode();
        } else {
            getCountryCodeList();
        }

        etRegisterFirstName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etRegisterFirstName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        etRegisterLastName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etRegisterLastName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        etRegisterFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etRegisterFirstName.setSelection(etRegisterFirstName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etRegisterLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etRegisterLastName.setSelection(etRegisterLastName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etRegisterCreatePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etRegisterCreatePassword.setSelection(etRegisterCreatePassword.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etRegisterEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etRegisterEmailID.setSelection(etRegisterEmailID.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etRegisterMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etRegisterMobileNumber.setSelection(etRegisterMobileNumber.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spinCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dailog_spin_country_code);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                EditText editSearchCountryCode= dialog.findViewById(R.id.searchCountryCode);
                ListView listCountryCode= dialog.findViewById(R.id.listViewCountryCode);
                TextView tvCancle= dialog.findViewById(R.id.tvCancel);

                mCountryCodeSpinAdapter= new CountryCodeListAdapter(getActivity(),Otapp.mCountryCodePojoList);
                listCountryCode.setAdapter(mCountryCodeSpinAdapter);

                listCountryCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(countryCodeFlag==1){
                            if(tempCountryCodeList.get(position).code.equals("")){
                                spinCountryCode.setText("+255");
                            }else {
                                spinCountryCode.setText(tempCountryCodeList.get(position).code);
                            }
                            etRegisterMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                            countryCodeFlag=0;
                        }else {
                            if(Otapp.mCountryCodePojoList.get(position).code.equals("")){
                                spinCountryCode.setText("+255");
                            }else {
                                spinCountryCode.setText(Otapp.mCountryCodePojoList.get(position).code);
                            }
                            etRegisterMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                        }
                        dialog.dismiss();
                    }
                });

                editSearchCountryCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String search=s.toString();
                        tempCountryCodeList= new ArrayList<>();
                        if(search.equals("")){
                            mCountryCodeSpinAdapter= new CountryCodeListAdapter(getActivity(),Otapp.mCountryCodePojoList);
                            listCountryCode.setAdapter(mCountryCodeSpinAdapter);
                            mCountryCodeSpinAdapter.notifyDataSetChanged();
                            countryCodeFlag=0;
                        }else {
                            for(int i=0;i<Otapp.mCountryCodePojoList.size();i++){

                                if(Otapp.mCountryCodePojoList.get(i).name.toUpperCase().startsWith(search.toUpperCase())||Otapp.mCountryCodePojoList.get(i).code.replaceAll("\\+","").startsWith(search)){
                                    tempCountryCodeList.add(Otapp.mCountryCodePojoList.get(i));
                                    countryCodeFlag=1;
                                }

                            }
                            if(tempCountryCodeList.size()==0) {
                                Toast.makeText(getActivity(), "No Country Code Found", Toast.LENGTH_SHORT).show();
                            }
                            mCountryCodeSpinAdapter = new CountryCodeListAdapter(getActivity(), tempCountryCodeList);
                            listCountryCode.setAdapter(mCountryCodeSpinAdapter);
                            mCountryCodeSpinAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                tvCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_TITLE, "");
                bundle.putString(Constants.BNDL_URL, "http://www.managemyticket.net/android/api/auth/terms_and_conditions.php");
                IntentHandler.startActivity(getActivity(), RegisterTermsActivity.class, bundle);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        Spannable wordtoSpan = new SpannableString("" + tvRegisterTermsPrivacy.getText().toString());
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.batli)), 33, tvRegisterTermsPrivacy.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(clickableSpan, 33, tvRegisterTermsPrivacy.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegisterTermsPrivacy.setText(wordtoSpan);
        tvRegisterTermsPrivacy.setMovementMethod(LinkMovementMethod.getInstance());

//        etRegisterFirstName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String string = charSequence.toString();
//                Log.d("STRING", string + " prevString::" + prevString);
//                if (string.equals(prevString)) {
//                    return;
//                } else if (string.length() == 0)
//                    return;
//                    // 1st character
//                else if (string.length() == 1) {
//                    LogUtils.e("", "string.length() == 1:"+string.toUpperCase());
//                    prevString = string.toUpperCase();
//                    etRegisterFirstName.setText(string.toUpperCase());
////                    etRegisterFirstName.setSelection(string.length());
//                    LogUtils.e("", "etRegisterFirstName::"+etRegisterFirstName.getText().toString());
//                }
//                // if the last entered character is after a space
//                else if (string.length() > 0 && string.charAt(string.length() - 2) == ' ') {
//                    LogUtils.e("", "space");
//                    string = string.substring(0, string.length() - 1) + Character.toUpperCase(string.charAt(string.length() - 1));
//                    prevString = string;
//                    etRegisterFirstName.setText(string);
//                    etRegisterFirstName.setSelection(string.length());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

   /* private void setSpinCountryCode() {

        CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
        spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
        spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                LogUtils.e("", "spinCountryCode.getSelectedItemPosition()::"+spinCountryCode.getSelectedItemPosition()+" "+Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength);
                mMobNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                etRegisterMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobNumberMaxLength)});
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }*/

    @Override
    public void onClick(View view) {

        if (view == tvRegisterLoginHere) {

            if (!TextUtils.isEmpty(mScreenName) && mScreenName.equalsIgnoreCase(LoginActivity.class.getName())) {
                IntentHandler.startActivityReorderFront(getActivity(), LoginActivity.class, getIntent().getExtras());
            } else {
                IntentHandler.startActivityReorderFront(getActivity(), LoginActivity.class);
            }

            finish();

        } else if (view == tvRegister) {
            if (isValidField()) {
                registerUser();
            }
        } else if (view == tvRegisterSkip) {

            MyPref.setPref(getActivity(), MyPref.PREF_IS_SKIPED, true);
            IntentHandler.startActivityReorderFront(getActivity(), HomeActivity.class);
            finish();
        }
    }

    private void registerUser() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        String mCustomerPassword = Utils.getPassword(etRegisterCreatePassword.getText().toString());
        LogUtils.e("", "mCustomerPassword : " + mCustomerPassword);

        final String mCustomerFirstName = "" + etRegisterFirstName.getText().toString(),
                mCustomerLastName = "" + etRegisterLastName.getText().toString(),
                mCustomerEmail = "" + etRegisterEmailID.getText().toString(),
                mCustomerCountryCode = spinCountryCode.getText().toString(),
                mCustomerMobile = "" + etRegisterMobileNumber.getText().toString();


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("first_name", "" + mCustomerFirstName);
        jsonParams.put("last_name", "" + mCustomerLastName);
        jsonParams.put("email", "" + mCustomerEmail);
        jsonParams.put("isd_code", "" + mCustomerCountryCode);
        jsonParams.put("mobile", "" + mCustomerMobile);
        jsonParams.put("password", "" + mCustomerPassword);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.registerUser(jsonParams);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Utils.closeProgressDialog();
                LogUtils.e("", "response::" + response);

                if (response.isSuccessful()) {

                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null) {

                        if (mApiResponse.status.equalsIgnoreCase("200")) {

                            MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, mCustomerCountryCode);
//                            MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, mCustomerName);
//                            MyPref.setPref(getActivity(), MyPref.PREF_USER_KEY, mApiResponse.key);
//                            MyPref.setPref(getActivity(), MyPref.PREF_IS_LOGGED, true);

                            Utils.showToast(getActivity(), mApiResponse.message);

                            etRegisterEmailID.setText("");
                            etRegisterMobileNumber.setText("");
                            etRegisterCreatePassword.setText("");
                            etRegisterFirstName.setText("");
                            etRegisterLastName.setText("");

                            if (!TextUtils.isEmpty(mScreenName) && mScreenName.equalsIgnoreCase(LoginActivity.class.getName())) {
                                IntentHandler.startActivityReorderFront(getActivity(), LoginActivity.class, getIntent().getExtras());
                            } else {
                                IntentHandler.startActivityReorderFront(getActivity(), LoginActivity.class);
                            }

                            finish();

                        } else if (mApiResponse.status.equalsIgnoreCase("401")) {
                            Utils.showDialog(getActivity(), mApiResponse.message);
                        } else if (mApiResponse.status.equalsIgnoreCase("404")) {
                            Utils.showDialog(getActivity(), mApiResponse.message);
                        } else {
                            Utils.showDialog(getActivity(), mApiResponse.message);
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private boolean isValidField() {

        if (TextUtils.isEmpty(etRegisterEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email));
            return false;
        } else if (!Utils.isValidEmail(etRegisterEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_email));
            return false;
        } else if (TextUtils.isEmpty(etRegisterMobileNumber.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone));
            return false;
        } else if (etRegisterMobileNumber.getText().toString().length() < mMobNumberMaxLength || etRegisterMobileNumber.getText().toString().length() > mMobNumberMaxLength) {
            Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobNumberMaxLength));
            return false;
        } else if (TextUtils.isEmpty(etRegisterCreatePassword.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_password));
            return false;
        } else if (etRegisterCreatePassword.getText().toString().length() < 8) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_8_password));
            return false;
        } else if (etRegisterCreatePassword.getText().toString().length() > 12) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_12_password));
            return false;
        } else if (TextUtils.isEmpty(etRegisterFirstName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_firstname));
            return false;
        } else if (TextUtils.isEmpty(etRegisterLastName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_lastname));
            return false;
        } else  if(etRegisterMobileNumber.getText().toString().length()+spinCountryCode.getText().toString().length()!=13){
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
            return false;
        }/*else if (Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code == null || TextUtils.isEmpty(Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code)) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_select_country_code));
            return false;
        }*/

        return true;
    }

    private Context getActivity() {
        return RegisterActivity.this;
    }

    private void getCountryCodeList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CountryCodePojo> mCall = mApiInterface.getCountryCodeList(jsonParams);
        mCall.enqueue(new Callback<CountryCodePojo>() {
            @Override
            public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    CountryCodePojo mCountryCodePojo = response.body();
                    if (mCountryCodePojo != null) {
                        if (mCountryCodePojo.status.equalsIgnoreCase("200")) {

                            Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                            Otapp.mAdsPojoList = mCountryCodePojo.ad5;
                            Otapp.mServiceList=mCountryCodePojo.servicesList;

                       //     setSpinCountryCode();

                        } else {
                            Utils.showToast(getActivity(), "" + mCountryCodePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                LogUtils.e("", "onFailure:" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });

    }
}
