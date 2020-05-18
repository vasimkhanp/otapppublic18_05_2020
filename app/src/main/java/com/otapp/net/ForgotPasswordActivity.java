package com.otapp.net;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.otapp.net.model.ApiResponse;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tvFpSend)
    TextView tvFpSend;
    @BindView(R.id.tvFpLoginHere)
    TextView tvFpLoginHere;
    @BindView(R.id.etFpEmailID)
    EditText etFpEmailID;

    private String mScreenName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        InitializeControls();
    }

    private void InitializeControls() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.BNDL_SCREEN_NAME)){
            mScreenName = bundle.getString(Constants.BNDL_SCREEN_NAME, "");
        }

        etFpEmailID.addTextChangedListener(new TextWatcher() {
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
        });


        tvFpSend.setOnClickListener(this);
        tvFpLoginHere.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == tvFpSend) {
            if (isValidField()) {
                forgotPassword();
            }
        } else if (view == tvFpLoginHere){

            if (!TextUtils.isEmpty(mScreenName) && mScreenName.equalsIgnoreCase(LoginActivity.class.getName())){
                IntentHandler.startActivityReorderFront(getActivity(), LoginActivity.class, getIntent().getExtras());
            }else{
                IntentHandler.startActivityReorderFront(getActivity(), LoginActivity.class);
            }

//            IntentHandler.startActivityReorderFront(getActivity(), LoginActivity.class);
            finish();
        }
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
                                    finish();
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

    private boolean isValidField() {

        if (TextUtils.isEmpty(etFpEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email_mobile));
            return false;
        }

        return true;
    }

    private Context getActivity() {
        return ForgotPasswordActivity.this;
    }
}
