package com.otapp.net.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ResetPasswordFragment = "Tag_" + "ResetPasswordFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmNewPassword)
    EditText etConfirmNewPassword;

    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        tvBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == tvBack) {
            popBackStack();
        } else if (view == tvSubmit) {
            if (isValidField()) {
                resetPassword();
            }
        }

    }

    private void resetPassword() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);
        jsonParams.put("old_password", "" + Utils.getPassword(etOldPassword.getText().toString()));
        jsonParams.put("new_password", "" + Utils.getPassword(etNewPassword.getText().toString()));
        jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        jsonParams.put("cust_log_key", MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));
        jsonParams.put("key", "ee67151f0a02760d3080a5e3f69cfcf8");

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.resetPassword(jsonParams);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null) {
                        if (mApiResponse.status.equalsIgnoreCase("200")) {

                            Utils.showToast(getActivity(), "" + mApiResponse.message);

                            etOldPassword.setText("");
                            etNewPassword.setText("");
                            etConfirmNewPassword.setText("");

                            popBackStack();

                        } else {
                            Utils.showToast(getActivity(), "" + mApiResponse.message);
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

        if (TextUtils.isEmpty(etOldPassword.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_enter_old_password));
            return false;
        }else if (etNewPassword.getText().toString().length() < 8) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_old_password));
            return false;
        } else if (TextUtils.isEmpty(etNewPassword.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_enter_new_password));
            return false;
        } else if (etNewPassword.getText().toString().length() < 8) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_new_password));
            return false;
        } else if (TextUtils.isEmpty(etConfirmNewPassword.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_enter_confirm_new_password));
            return false;
        } else if (etConfirmNewPassword.getText().toString().length() < 8) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_confirm_password));
            return false;
        } else if (!TextUtils.equals(etNewPassword.getText().toString(), etConfirmNewPassword.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_password_not_match));
            return false;
        }

        return true;
    }
}
