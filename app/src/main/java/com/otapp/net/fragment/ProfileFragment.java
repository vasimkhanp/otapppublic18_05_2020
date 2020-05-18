package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.otapp.net.BuildConfig;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.RegisterTermsActivity;
import com.otapp.net.adapter.CountryCodeListAdapter;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.UserPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.CircleTransform;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public static String Tag_ProfileFragment = "Tag_" + "ProfileFragment";

    View mView;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.tvEditProfile)
    TextView tvEditProfile;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.lnrProfile)
    LinearLayout lnrProfile;
    @BindView(R.id.lnrLogin)
    LinearLayout lnrLogin;
    @BindView(R.id.lnrRegister)
    LinearLayout lnrRegister;
    @BindView(R.id.lnrForgotPassword)
    LinearLayout lnrForgotPassword;
    @BindView(R.id.lnrResetPassword)
    LinearLayout lnrResetPassword;

    //Login

    @BindView(R.id.tvLoginFacebook)
    TextView tvLoginFacebook;
    @BindView(R.id.tvLoginGoogle)
    TextView tvLoginGoogle;
    @BindView(R.id.tvLoginContinue)
    TextView tvLoginContinue;
    @BindView(R.id.tvLoginForgotPassword)
    TextView tvLoginForgotPassword;
    @BindView(R.id.tvLoginNewRegister)
    TextView tvLoginNewRegister;
    @BindView(R.id.etLoginEmailID)
    EditText etLoginEmailID;
    @BindView(R.id.etLoginPassword)
    EditText etLoginPassword;
    String strName="",strLastName="";
    String fullName[];
    String mCustomerName = "",
            mCustomerEmail = "",
            mCustomerMobile = "",
            mCustomerType = "0",
            mCustomerPhoto = "",
            mCustomerID = "",
            mURLKey = "",
            mCustomerPassword = "",
            mParamKey = "";

    int mAuthKey;

    private GoogleApiClient mGoogleApiClient;

    private static final String EMAIL = "email";

    List<CountryCodePojo.CountryCode> tempCountryCodeList;
    int countryCodeFlag=0;
    CountryCodeListAdapter mCountryCodeSpinAdapter;


    CallbackManager mFBCallbackManager = CallbackManager.Factory.create();

    // Register
    @BindView(R.id.tvRegisterTermsPrivacy)
    TextView tvRegisterTermsPrivacy;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvRegisterLoginHere)
    TextView tvRegisterLoginHere;
    //    @BindView(R.id.tvRegisterCountryCode)
//    TextView tvRegisterCountryCode;
    @BindView(R.id.tvAppVersion)
    TextView tvAppVersion;
    @BindView(R.id.spinCountryCode)
    TextView spinCountryCode;
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

    private int mMobNumberMaxLength = 9;

    //forgot password
    @BindView(R.id.tvFpSend)
    TextView tvFpSend;

    @BindView(R.id.tvFpLoginHere)
    TextView tvFpLoginHere;
    @BindView(R.id.etFpEmailID)
    EditText etFpEmailID;

    private enum ViewType {Profile, Login, Register, ForgotPassword}

    private ViewType mSelectedViewType;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
        if (mSelectedViewType == ViewType.Profile) {
            InitializeFacebook();
        }

        return mView;
    }

    private void InitializeControls() {

        tvAppVersion.setText(getString(R.string.app_version) + " v" + BuildConfig.VERSION_NAME);


        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            setViewType(ViewType.Profile);
        } else {
            setViewType(ViewType.Login);
        }

        tvLogout.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);
        lnrResetPassword.setOnClickListener(this);

        //Register
        tvRegisterLoginHere.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        spinCountryCode.setText("+255");
        etRegisterMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});

      /*  CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
        spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
        spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                mMobNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                etRegisterMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobNumberMaxLength)});
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


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





       /* etRegisterFirstName.addTextChangedListener(new TextWatcher() {
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
        });*/

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

        //Login
        if (mSelectedViewType != ViewType.Profile) {
            try {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity(), this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

       /* etLoginEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etLoginEmailID.setSelection(etLoginEmailID.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etLoginPassword.setSelection(etLoginPassword.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        tvLoginGoogle.setOnClickListener(this);
        tvLoginFacebook.setOnClickListener(this);
        tvLoginNewRegister.setOnClickListener(this);
        tvLoginForgotPassword.setOnClickListener(this);
        tvLoginContinue.setOnClickListener(this);

        // Forgot Password

       /* etFpEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etFpEmailID.setSelection(etFpEmailID.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        tvFpSend.setOnClickListener(this);
        tvFpLoginHere.setOnClickListener(this);

    }

    private void setViewType(ViewType mViewType) {
        lnrProfile.setVisibility(View.GONE);
        lnrLogin.setVisibility(View.GONE);
        lnrRegister.setVisibility(View.GONE);
        lnrForgotPassword.setVisibility(View.GONE);
        switch (mViewType) {
            case Profile:
                lnrProfile.setVisibility(View.VISIBLE);
                setProfile();
                break;
            case Login:
                lnrLogin.setVisibility(View.VISIBLE);
                break;
            case Register:
                lnrRegister.setVisibility(View.VISIBLE);
                break;
            case ForgotPassword:
                lnrForgotPassword.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setProfile() {

        tvName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, "") + " " + MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, ""));
        tvEmail.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, ""));

            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").contains("+")){
                if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")){
                    tvPhone.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                }else {
                    tvPhone.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "")+MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                }

            }else {
                if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")){
                    if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").length()>1){
                        tvPhone.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                    }else {
                        tvPhone.setText("+" + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                    }
                }else {
                    if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").length()>1){
                        tvPhone.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "")+MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                    }else {
                        tvPhone.setText("+" + MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "") + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                    }
                }

            }


        if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_PROFILE, ""))) {
            Picasso.get().load(MyPref.getPref(getActivity(), MyPref.PREF_USER_PROFILE, "")).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivProfile);
        } else {
            Picasso.get().load(R.drawable.bg_circle_white).into(ivProfile);
        }

//        if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""))) {
//
//            tvName.setVisibility(View.VISIBLE);
//        } else {
//            tvName.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onClick(View view) {
        if (view == tvLogout) {

            new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.msg_logout))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (dialog != null) {
                                dialog.dismiss();
                                dialog.cancel();
                            }

                            MyPref.setPref(getActivity(), MyPref.PREF_IS_LOGGED, false);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_ID, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_LAST_NAME, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_EMAIL, "");
                            MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_MOB, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_PROFILE, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_GENDER, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_MARITAL_STATUS, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_KEY, "");
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_LOG_ID, "");

                            logoutApi();

                            setViewType(ViewType.Login);

//                            IntentHandler.startActivityClearTop(getActivity(), LoginActivity.class);
//                            getActivity().finish();

                        }
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();


        } else if (view == tvEditProfile) {

            switchFragment(ProfileEditFragment.newInstance(), ProfileEditFragment.Tag_ProfileEditFragment);

        } else if (view == tvRegisterLoginHere) { // Register

            setViewType(ViewType.Login);

        } else if (view == tvRegister) {
            if (isRegisterValidField()) {
                registerUser();
            }
        } else if (view == tvLoginGoogle) { // Login

//            if (mGoogleApiClient != null) {
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
//            }

        } else if (view == tvLoginFacebook) {

//            loginWithFacebook();

        } else if (view == tvLoginNewRegister) {

            setViewType(ViewType.Register);

        } else if (view == tvLoginForgotPassword) {

            setViewType(ViewType.ForgotPassword);

        } else if (view == tvLoginContinue) {

            if (isLoginValidField()) {
                loginUser();
            }
        } else if (view == tvFpSend) {
            if (isFpValidField()) {
                forgotPassword();
            }
        } else if (view == tvFpLoginHere) {
            setViewType(ViewType.Login);
        } else if (view == lnrResetPassword) {
            switchFragment(ResetPasswordFragment.newInstance(), ResetPasswordFragment.Tag_ResetPasswordFragment);
        }
    }

    // Register

    private boolean isRegisterValidField() {

        if (TextUtils.isEmpty(etRegisterEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email));
            return false;
        } else if (!Utils.isValidEmail(etRegisterEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_email));
            return false;
        } else if (TextUtils.isEmpty(etRegisterMobileNumber.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone));
            return false;
        } /*else if (etRegisterMobileNumber.getText().toString().length() < mMobNumberMaxLength || etRegisterMobileNumber.getText().toString().length() > mMobNumberMaxLength) {
            Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobNumberMaxLength));
            return false;
        } */else if (TextUtils.isEmpty(etRegisterCreatePassword.getText().toString())) {
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

                            Utils.showToast(getActivity(), mApiResponse.message);

                            etRegisterEmailID.setText("");
                            etRegisterMobileNumber.setText("");
                            etRegisterCreatePassword.setText("");
                            etRegisterFirstName.setText("");
                            etRegisterLastName.setText("");

                            setViewType(ViewType.Login);

                        } else if (mApiResponse.status.equalsIgnoreCase("201")) {
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

    private void forgotPassword() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        String mKey = Utils.MD5("F0rgetP@ssw0rd");

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("email", "" + etFpEmailID.getText().toString());
        jsonParams.put("key", "" + mKey);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.forgotPassword(jsonParams);

        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Utils.closeProgressDialog();
                LogUtils.e("", "response::" + response);

                if (response.isSuccessful()) {

                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null) {

                        if (mApiResponse.status.equalsIgnoreCase("200")) {

//                            Utils.showToast(getActivity(), mApiResponse.message);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(getActivity().getString(R.string.app_name));
                            builder.setCancelable(false);
                            builder.setMessage(mApiResponse.message);
                            builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    etFpEmailID.setText("");
                                    setViewType(ViewType.Login);
                                }
                            });

                            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return keyCode == KeyEvent.KEYCODE_BACK;
                                }
                            });

                            AlertDialog alert1 = builder.create();
                            alert1.show();

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

    private void logoutApi() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("cust_id", MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        jsonParams.put("cust_log_key", MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));
        jsonParams.put("log_id", MyPref.getPref(getActivity(), MyPref.PREF_USER_LOG_ID, ""));

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.logout(jsonParams);

        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Utils.closeProgressDialog();
                LogUtils.e("", "response::" + response);

                if (response.isSuccessful()) {

                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null) {

                        if (mApiResponse.status.equalsIgnoreCase("200")) {

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

    // Login
    private void InitializeFacebook() {

        LoginManager.getInstance().registerCallback(mFBCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        LogUtils.e("accessToken", accessToken);


                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {

                                        Log.i("LoginActivity",
                                                response.toString());

                                        String id = "", name = "", email = "", gender = "", birthday = "";

                                        try {
                                            id = object.getString("id");
                                            name = object.getString("name");
                                            email = object.getString("email");
                                            gender = object.getString("gender");
                                            birthday = object.getString("birthday");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        String personPhotoUrl = "http://graph.facebook.com/" + id + "/picture?type=large";

                                        LogUtils.e("", "id:" + id);
                                        LogUtils.e("", "name:" + name);
                                        LogUtils.e("", "email:" + email);
                                        LogUtils.e("", "gender:" + gender);
                                        LogUtils.e("", "birthday:" + birthday);
                                        LogUtils.e("", "personPhotoUrl:" + personPhotoUrl);


                                        clearParam();

                                        if (!TextUtils.isEmpty(name)) {
                                            mCustomerName = name;
                                        }

                                        if (!TextUtils.isEmpty(personPhotoUrl)) {
                                            mCustomerPhoto = personPhotoUrl;
                                        }

                                        if (!TextUtils.isEmpty(email)) {
                                            mCustomerEmail = email;
                                        }

                                        if (!TextUtils.isEmpty(id)) {
                                            mCustomerID = id;
                                        }

                                        mCustomerType = "2";

                                        registerUserWithSocial();


                                    }
                                });

                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                    }
                });
    }

    private void loginUser() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }


        Utils.showProgressDialog(getActivity());

//        mAuthKey = Utils.getAuthKey();
//        LogUtils.e("", "mAuthKey : " + mAuthKey);
//        mURLKey = Utils.GetHash(mAuthKey, "L0g!n" + mAuthKey);
//        LogUtils.e("", "mURLKey : " + mURLKey);
//
//        if (mCustomerType.equals("0")) {
        mCustomerPassword = Utils.getPassword(etLoginPassword.getText().toString());
//            if (etLoginEmailID.getText().toString().contains("@")) {
        mCustomerEmail = etLoginEmailID.getText().toString();
//            } else {
//                mCustomerMobile = etLoginEmailID.getText().toString();
//            }
//        }
        String mKey = Utils.MD5("L0g!n");
        LogUtils.e("", "mCustomerPassword : " + mCustomerPassword);


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("userid", "" + mCustomerEmail);
        jsonParams.put("password", "" + mCustomerPassword);
        jsonParams.put("key", "" + mKey);


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<UserPojo> mCall = mApiInterface.loginUser(jsonParams);
        mCall.enqueue(new Callback<UserPojo>() {
            @Override
            public void onResponse(Call<UserPojo> call, Response<UserPojo> response) {

                Utils.closeProgressDialog();
                LogUtils.e("", "response::" + response);

                if (response.isSuccessful()) {

                    UserPojo mUserPojo = response.body();
                    if (mUserPojo != null) {

                        if (mUserPojo.status.equalsIgnoreCase("200")) {

//                            String mSuccessKey = Utils.GetHash(mAuthKey, mUserPojo.custId + mUserPojo.custName + mAuthKey + "L)g!n_5ucc55e$");

//                            LogUtils.e("", "mSuccessKey::" + mSuccessKey);

//                            if (mSuccessKey.equals(mUserPojo.key)) {
                            strName=mUserPojo.data.custName;
                            strLastName=mUserPojo.data.custLastName;
                            if(strLastName.equals("")){
                                if(strName.contains(" ")) {
                                    fullName = strName.split("\\s+");
                                    if (fullName.length == 3) {
                                        MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, fullName[0] + fullName[1]);
                                        MyPref.setPref(getActivity(), MyPref.PREF_USER_LAST_NAME, fullName[2]);
                                    } else {
                                        if (fullName.length == 2) {
                                            MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, fullName[0]);
                                            MyPref.setPref(getActivity(), MyPref.PREF_USER_LAST_NAME, fullName[1]);
                                        } else {
                                            MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, mUserPojo.data.custName);
                                        }
                                    }
                                }else {
                                    MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, mUserPojo.data.custName);
                                }
                            }else {
                                MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME,strName);
                                MyPref.setPref(getActivity(), MyPref.PREF_USER_LAST_NAME, strLastName);

                            }
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_ID, mUserPojo.data.custId);
//                            MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, mUserPojo.data.custName);
//                            MyPref.setPref(getActivity(), MyPref.PREF_USER_LAST_NAME, mUserPojo.data.custLastName);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_EMAIL, mUserPojo.data.custEmail);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, mUserPojo.data.custCountryCode);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_MOB, mUserPojo.data.custMob);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_PROFILE, mUserPojo.data.custProfile);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_GENDER, mUserPojo.data.custGender);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_MARITAL_STATUS, mUserPojo.data.custMaritalStatus);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_KEY, mUserPojo.data.custKey);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_LOG_ID, mUserPojo.data.logId);
                            MyPref.setPref(getActivity(), MyPref.PREF_IS_LOGGED, true);

                            Utils.showToast(getActivity(), mUserPojo.message);

                            etLoginEmailID.setText("");
                            etLoginPassword.setText("");

                            setViewType(ViewType.Profile);
//                            }

                        } else if (mUserPojo.status.equalsIgnoreCase("404")) {
                            Utils.showDialog(getActivity(), mUserPojo.message);
                        } else {
                            Utils.showDialog(getActivity(), mUserPojo.message);
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<UserPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private boolean isLoginValidField() {

        if (TextUtils.isEmpty(etLoginEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email_mobile));
            return false;
        } else if (etLoginEmailID.getText().toString().contains("@") && !Utils.isValidEmail(etLoginEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_email));
            return false;
        }
//        else if (TextUtils.isDigitsOnly(etLoginEmailID.getText().toString()) && (etLoginEmailID.getText().toString().length() < 9 || etLoginEmailID.getText().toString().length() > 9)) {
//            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone));
//            return false;
//        }
        else if (TextUtils.isEmpty(etLoginPassword.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_password));
            return false;
        } else if (etLoginPassword.getText().toString().length() < 3) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_8_password));
            return false;
        }


        return true;
    }

    private void loginWithFacebook() {

        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile", "user_photos"));

    }

    private void clearParam() {

        mCustomerName = "";
        mCustomerEmail = "";
        mCustomerMobile = "";
        mCustomerType = "0";
        mCustomerPhoto = "";
        mCustomerID = "";
        mURLKey = "";
        mCustomerPassword = "";
        mParamKey = "";
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.d("", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFBCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();

                LogUtils.e("", "display name: " + acct.getDisplayName());

                String personName = acct.getDisplayName();
                String personPhotoUrl = (acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "");
                String email = acct.getEmail();
                String id = acct.getId();

                LogUtils.e("", "id::" + id + " Name: " + personName + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                clearParam();

                if (!TextUtils.isEmpty(personName)) {
                    mCustomerName = personName;
                }

                if (!TextUtils.isEmpty(personPhotoUrl)) {
                    mCustomerPhoto = personPhotoUrl;
                }

                if (!TextUtils.isEmpty(email)) {
                    mCustomerEmail = email;
                }

                if (!TextUtils.isEmpty(id)) {
                    mCustomerID = id;
                }

                mCustomerType = "1";

                registerUserWithSocial();

                if (mGoogleApiClient != null) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                }
                            });
                }
            }
        }
    }

    private void registerUserWithSocial() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        Map<String, String> jsonParams = new ArrayMap<>();
        jsonParams.put("name", "" + mCustomerName);
        jsonParams.put("email", "" + mCustomerEmail);
        jsonParams.put("cust_mob", "" + mCustomerMobile);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<UserPojo> mCall = mApiInterface.loginUserWithSocial(jsonParams);
        mCall.enqueue(new Callback<UserPojo>() {
            @Override
            public void onResponse(Call<UserPojo> call, Response<UserPojo> response) {

                Utils.closeProgressDialog();
                LogUtils.e("", "response::" + response);

                if (response.isSuccessful()) {

                    UserPojo mUserPojo = response.body();
                    if (mUserPojo != null) {

                        if (mUserPojo.status.equalsIgnoreCase("200")) {

                            MyPref.setPref(getActivity(), MyPref.PREF_USER_ID, mUserPojo.data.custId);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, mUserPojo.data.custName);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_EMAIL, mUserPojo.data.custEmail);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_MOB, mUserPojo.data.custMob);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_PROFILE, mUserPojo.data.custProfile);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_GENDER, mUserPojo.data.custGender);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_MARITAL_STATUS, mUserPojo.data.custMaritalStatus);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_KEY, mUserPojo.data.custKey);
                            MyPref.setPref(getActivity(), MyPref.PREF_IS_LOGGED, true);

                            Utils.showToast(getActivity(), mUserPojo.message);

                            IntentHandler.startActivityReorderFront(getActivity(), HomeActivity.class);

                            setViewType(ViewType.Profile);

                        } else if (mUserPojo.status.equalsIgnoreCase("201")) {
                            Utils.showToast(getActivity(), mUserPojo.message);
                        } else {
                            Utils.showToast(getActivity(), mUserPojo.message);
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<UserPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }


    // forgot password
    private boolean isFpValidField() {

        if (TextUtils.isEmpty(etFpEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email_mobile));
            return false;
        }

        return true;
    }
}
