package com.otapp.net;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.otapp.net.rangeseekbar.CrystalRangeSeekbar;
import com.otapp.net.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterBusActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @BindView(R.id.rangeSbPrice)
    CrystalRangeSeekbar rangeSbPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_bus_filter);
        ButterKnife.bind(this);

        InitializeControls();

    }

    private void InitializeControls() {

        rangeSbPrice
                .setCornerRadius(10f)
                .setBarHeight(Utils.pxFromDp(getActivity(), 2))
                .setBarColor(getResources().getColor(R.color.orange_light))
                .setBarHighlightColor(getResources().getColor(R.color.orange))
                .setMinValue(200)
                .setMaxValue(1600)
                .setMinStartValue(200)
                .setMaxStartValue(1600)
                .setSteps(100)
                .setLeftThumbDrawable(R.drawable.ic_price_thumb)
                .setLeftThumbHighlightDrawable(R.drawable.ic_price_thumb)
                .setRightThumbDrawable(R.drawable.ic_price_thumb)
                .setRightThumbHighlightDrawable(R.drawable.ic_price_thumb)
                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .apply();

        tvBack.setOnClickListener(this);
    }

    private Context getActivity() {
        return FilterBusActivity.this;
    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            finish();
        }
    }
}
