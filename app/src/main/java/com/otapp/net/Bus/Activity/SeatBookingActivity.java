package com.otapp.net.Bus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.otapp.net.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeatBookingActivity extends AppCompatActivity {

    @BindView(R.id.buttonBook)
    Button buttonBook;
    @BindView(R.id.buttonCancel)
    Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonBook)
    void onBook() {
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonCancel)
    void onCancel() {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Log", "Language : " + Locale.getDefault().getDisplayLanguage());
        if (!Locale.getDefault().getDisplayLanguage().equals("English")) {
            Toast.makeText(this, "This app supports only english language, Please select english language", Toast.LENGTH_SHORT).show();
            Locale locale = new Locale("English");
            Locale.setDefault(locale);
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            Locale.setDefault(Locale);
        }
    }
}
