package com.otapp.net.Bus.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.otapp.net.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusDetailsActivity extends AppCompatActivity {
    @BindView(R.id.imageViewHeaderBack)
    ImageView imageViewHeaderBack;
    @BindView(R.id.textViewHeaderTitle)
    TextView textViewHeaderTitle;

    @OnClick(R.id.imageViewHeaderBack)
    void onBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        ButterKnife.bind(this);

    }
}
