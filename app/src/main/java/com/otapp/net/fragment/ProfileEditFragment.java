package com.otapp.net.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otapp.net.HomeActivity;
import com.otapp.net.ImagePickerActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeListAdapter;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.UserEditPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.CircleTransform;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ProfileEditFragment = "Tag_" + "ProfileEditFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvUploadPic)
    TextView tvUploadPic;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    //    @BindView(R.id.tvCountryCode)
//    TextView tvCountryCode;
    @BindView(R.id.spinCountryCode)
    TextView spinCountryCode;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.view)
    View view;

    String mProfileBage64 = "";
    private int mMobNumberMaxLength = 9;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    int flag=0,mobFlag=0;
    List<CountryCodePojo.CountryCode> tempCountryCodeList;
    int countryCodeFlag=0;
    CountryCodeListAdapter mCountryCodeSpinAdapter;
    public static ProfileEditFragment newInstance() {
        ProfileEditFragment fragment = new ProfileEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        etFirstName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));
        etLastName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, ""));
        tvEmail.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, ""));
      //  etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").replace("+255", "").replace("255", ""));
        etFirstName.setSelection(etFirstName.getText().length());
        etLastName.setSelection(etLastName.getText().length());
        etMobileNumber.setSelection(etMobileNumber.getText().length());

        if((MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").length()>1)){
            spinCountryCode.setText((MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "")));
            spinCountryCode.setVisibility(View.VISIBLE);
        }else {
            spinCountryCode.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }


        if (Otapp.mCountryCodePojoList != null && Otapp.mCountryCodePojoList.size() > 0) {
            //setSpinCountryCode();
        } else {
            getCountryCodeList();
        }

        etFirstName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etFirstName.setFilters(new InputFilter[]{
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

        etLastName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etLastName.setFilters(new InputFilter[]{
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

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              //  etFirstName.setSelection(etFirstName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //           etMobileNumber.setSelection(etMobileNumber.getText().length());
                if(charSequence.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "chage", Toast.LENGTH_SHORT).show();
                    spinCountryCode.setVisibility(View.VISIBLE);
                    if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("")||MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                        /* spinCountryCode.setSelection(214);*/
                        spinCountryCode.setText("+255");
                    }
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                    view.setVisibility(View.VISIBLE);
                    flag=0;
                }
                if(charSequence.toString().trim().length()<13){
                    mobFlag=1;
                }else {
                    mobFlag=0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             //   etLastName.setSelection(etLastName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              //  etMobileNumber.setSelection(etMobileNumber.getText().length());
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
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                            countryCodeFlag=0;
                        }else {
                            if(Otapp.mCountryCodePojoList.get(position).code.equals("")){
                                spinCountryCode.setText("+255");
                            }else {
                                spinCountryCode.setText(Otapp.mCountryCodePojoList.get(position).code);
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
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

        String mob=MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");
        if(mob.length()==12){
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").startsWith("+")){
                etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            }else {
                etMobileNumber.setText("+" + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            }
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            flag=1;
        }else {
            etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.VISIBLE);
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("")||MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                spinCountryCode.setText("+255");
            }
            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
            view.setVisibility(View.VISIBLE);
            flag=0;
        }
        if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_PROFILE, ""))) {
            Picasso.get().load(MyPref.getPref(getActivity(), MyPref.PREF_USER_PROFILE, "")).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivProfile);
        } else {
            Picasso.get().load(R.drawable.bg_circle_white).into(ivProfile);
        }

        tvBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvUploadPic.setOnClickListener(this);

    }
    @Override
    public void onResume() {
        super.onResume();
        String mob=MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");
        if(mob.length()==12){
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").startsWith("+")){
                etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            }else {
                etMobileNumber.setText("+" + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            }
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            flag=1;
        }else {
            etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.VISIBLE);
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("")||MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                /* spinCountryCode.setSelection(214);*/
                spinCountryCode.setText("+255");
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
            }
            view.setVisibility(View.VISIBLE);
            flag=0;
        }
    }
   /* private void setSpinCountryCode() {

        CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
        spinCountryCode.setAdapter(mCountryCodeSpinAdapter);

        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
            spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
        } else {
            spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
        }

        spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                mMobNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobNumberMaxLength)});
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }*/

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            //popBackStack();
              //  switchFragment(ProfileFragment.newInstance(), ProfileFragment.Tag_ProfileFragment);
            try {
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mFrameLayout,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } catch (Exception e) {
                // TODO: handle exception
            }


        } else if (view == tvUploadPic) {

            Dexter.withActivity(getActivity())
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_PROFILE, ""))) {

                                    ImagePickerActivity.showImagePickerOptionsWithImage(getActivity(), new ImagePickerActivity.PickerOptionListener() {
                                        @Override
                                        public void onTakeCameraSelected() {
                                            launchCameraIntent();
                                        }

                                        @Override
                                        public void onChooseGallerySelected() {
                                            launchGalleryIntent();
                                        }

                                        @Override
                                        public void onRemovePicture() {
                                            removePicture();
                                        }
                                    });

                                } else {

                                    ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
                                        @Override
                                        public void onTakeCameraSelected() {
                                            launchCameraIntent();
                                        }

                                        @Override
                                        public void onChooseGallerySelected() {
                                            launchGalleryIntent();
                                        }

                                        @Override
                                        public void onRemovePicture() {
                                            removePicture();
                                        }
                                    });
                                }


                            } else {
                                // TODO - handle permission denied case
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        } else if (view == tvSave) {
            if (isValidField()) {
                saveProfile();
            }
        }
    }

    private void removePicture() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        Map<String, String> jsonParams = new ArrayMap<>();
        jsonParams.put("cust_id", MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        jsonParams.put("user_token", Otapp.mUniqueID);
        jsonParams.put("cust_log_key", MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));
        jsonParams.put("key", "46c0170d3725d55b0dbf32a4e2392dfa");

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.removePicture(jsonParams);
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

                            mProfileBage64 = "";

                            MyPref.setPref(getActivity(), MyPref.PREF_USER_PROFILE, "");
                            Picasso.get().load(R.drawable.bg_circle_white).into(ivProfile);

                            ProfileFragment mProfileFragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ProfileFragment.Tag_ProfileFragment);
                            if (mProfileFragment != null) {
                                mProfileFragment.setProfile();
                            }

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

    private void saveProfile() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        Map<String, String> jsonParams = new ArrayMap<>();
        jsonParams.put("key", "5e0801ee804de49f1d3786aeca409d09");
        jsonParams.put("cust_id", MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        jsonParams.put("first_name", etFirstName.getText().toString());
        jsonParams.put("last_name", etLastName.getText().toString());
        jsonParams.put("isd_code",spinCountryCode.getText().toString());
        jsonParams.put("cust_mob", "" + etMobileNumber.getText().toString());
        if (!TextUtils.isEmpty(mProfileBage64)) {
            jsonParams.put("profile", mProfileBage64);
        }
        jsonParams.put("user_token", Otapp.mUniqueID);
        jsonParams.put("cust_log_key", MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<UserEditPojo> mCall = mApiInterface.saveProfile(jsonParams);
        mCall.enqueue(new Callback<UserEditPojo>() {
            @Override
            public void onResponse(Call<UserEditPojo> call, Response<UserEditPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    UserEditPojo mUserEditPojo = response.body();
                    if (mUserEditPojo != null) {
                        if (mUserEditPojo.status.equalsIgnoreCase("200")) {

                            Utils.showToast(getActivity(), "" + mUserEditPojo.message);

                            MyPref.setPref(getActivity(), MyPref.PREF_USER_ID, mUserEditPojo.data.custId);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, mUserEditPojo.data.custName);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_LAST_NAME, mUserEditPojo.data.lastName);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_EMAIL, mUserEditPojo.data.custEmail);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, mUserEditPojo.data.custCountryCode);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_MOB, mUserEditPojo.data.custMob);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_PROFILE, mUserEditPojo.data.custProfile);
                            MyPref.setPref(getActivity(), MyPref.PREF_USER_KEY, mUserEditPojo.data.custLogKey);

                            ProfileFragment mProfileFragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ProfileFragment.Tag_ProfileFragment);
                            if (mProfileFragment != null) {
                                mProfileFragment.setProfile();
                            }

                            popBackStack();


                        } else {
                            Utils.showToast(getActivity(), "" + mUserEditPojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserEditPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);

        getActivity().startActivityForResult(intent, Constants.RC_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        getActivity().startActivityForResult(intent, Constants.RC_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtils.e(Tag_ProfileEditFragment, "onActivityResult requestCode:" + requestCode + " resultCode:" + resultCode);
        if (requestCode == Constants.RC_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getParcelableExtra("path");
            LogUtils.e("", "uri::" + uri);
            try {
                // You can update this bitmap to your server
                LogUtils.e("", "uri.toString()::" + uri.toString());

                if (uri != null && !TextUtils.isEmpty(uri.toString())) {
                    Picasso.get().load(uri).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivProfile);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    if (bitmap != null) {
                        mProfileBage64 = Utils.getBase64FromBitmap(bitmap);
                        LogUtils.e("", "mProfileBage64::" + mProfileBage64);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidField() {

        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_firstname));
            return false;
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_lastname));
            return false;
        } else if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone));
            return false;
        }else  if(etMobileNumber.getText().toString().length()+spinCountryCode.getText().toString().length()!=13){
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
            Utils.showToast(getActivity(), "" + etMobileNumber.getText().toString().length()+" "+spinCountryCode.getText().toString().length());
            return false;
        } /*else if (etMobileNumber.getText().toString().length() < mMobNumberMaxLength || etMobileNumber.getText().toString().length() > mMobNumberMaxLength) {
            Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobNumberMaxLength));
            return false;
        }*/
//        else if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_PROFILE, "")) && TextUtils.isEmpty(mProfileBage64)) {
//            Utils.showToast(getActivity(), "" + getString(R.string.alert_select_profile_image));
//            return false;
//        }

        return true;
    }

    private void getCountryCodeList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CountryCodePojo> mCall = mApiInterface.getCountryCodeList(jsonParams);
        mCall.enqueue(new Callback<CountryCodePojo>() {
            @Override
            public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {

                if (response.isSuccessful()) {
                    CountryCodePojo mCountryCodePojo = response.body();
                    if (mCountryCodePojo != null) {
                        if (mCountryCodePojo.status.equalsIgnoreCase("200")) {

                            Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                            Otapp.mAdsPojoList = mCountryCodePojo.ad5;
                            Otapp.mServiceList=mCountryCodePojo.servicesList;

                            //setSpinCountryCode();

                        } else {
                            Utils.showToast(getActivity(), "" + mCountryCodePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }
}
