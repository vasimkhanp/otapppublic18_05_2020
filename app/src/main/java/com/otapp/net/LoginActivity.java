package com.otapp.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.otapp.net.model.UserPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


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
    @BindView(R.id.tvLoginSkip)
    TextView tvLoginSkip;
    @BindView(R.id.etLoginEmailID)
    EditText etLoginEmailID;
    @BindView(R.id.etLoginPassword)
    EditText etLoginPassword;
    String strName="";
    String fullName[];
    String strLastName="";

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
    private String mScreenName = "";

    private GoogleApiClient mGoogleApiClient;

    private static final String EMAIL = "email";

    CallbackManager mFBCallbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeFacebook();

    }

    private void InitializeFacebook() {

/*
        etLoginEmailID.addTextChangedListener(new TextWatcher() {
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
*/

/*
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
        });
*/

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

    private Context getActivity() {
        return LoginActivity.this;
    }

    private void InitializeControls() {

        tvLoginSkip.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.BNDL_SCREEN_NAME)) {
            mScreenName = bundle.getString(Constants.BNDL_SCREEN_NAME, "");
            tvLoginSkip.setVisibility(View.GONE);
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        tvLoginGoogle.setOnClickListener(this);
        tvLoginFacebook.setOnClickListener(this);
        tvLoginNewRegister.setOnClickListener(this);
        tvLoginForgotPassword.setOnClickListener(this);
        tvLoginContinue.setOnClickListener(this);
        tvLoginSkip.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == tvLoginGoogle) {

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, Constants.RC_SIGN_IN);

        } else if (view == tvLoginFacebook) {

            loginWithFacebook();

        } else if (view == tvLoginNewRegister) {

            if (!TextUtils.isEmpty(mScreenName) && mScreenName.equalsIgnoreCase(LoginActivity.class.getName())) {
                IntentHandler.startActivityReorderFront(getActivity(), RegisterActivity.class, getIntent().getExtras());
            } else {
                IntentHandler.startActivityReorderFront(getActivity(), RegisterActivity.class);
            }

            finish();

        } else if (view == tvLoginForgotPassword) {

            if (!TextUtils.isEmpty(mScreenName) && mScreenName.equalsIgnoreCase(LoginActivity.class.getName())) {
                IntentHandler.startActivityReorderFront(getActivity(), ForgotPasswordActivity.class, getIntent().getExtras());
            } else {
                IntentHandler.startActivityReorderFront(getActivity(), ForgotPasswordActivity.class);
            }

//            IntentHandler.startActivityReorderFront(getActivity(), ForgotPasswordActivity.class);
            finish();

        } else if (view == tvLoginContinue) {

            if (isValidField()) {
                loginUser();
            }
        } else if (view == tvLoginSkip) {
            MyPref.setPref(getActivity(), MyPref.PREF_IS_SKIPED, true);
            IntentHandler.startActivityReorderFront(getActivity(), HomeActivity.class);
            finish();
        }
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
                        JSONObject jsonObjectResponse = null;
                        try {
                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Log", "Log in Response : " + jsonObjectResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                                    MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, fullName[0] +" "+ fullName[1]);
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
                           /* MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, mUserPojo.data.custName);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_LAST_NAME, mUserPojo.data.custLastName);*/
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


                            if (!TextUtils.isEmpty(mScreenName) && mScreenName.equalsIgnoreCase(LoginActivity.class.getName())){
                                setResult(RESULT_OK);
                            }else{
                                IntentHandler.startActivityReorderFront(getActivity(), HomeActivity.class);
                            }

                            finish();
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

    private boolean isValidField() {

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
        } else if (etLoginPassword.getText().toString().length() < 8) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_8_password));
            return false;
        }


        return true;
    }

    private void loginWithFacebook() {

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile", "user_photos"));

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

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                            }
                        });
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

                            finish();

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
}
