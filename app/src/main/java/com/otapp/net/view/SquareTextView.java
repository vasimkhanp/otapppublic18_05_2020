package com.otapp.net.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.otapp.net.R;

public class SquareTextView extends TextView {

    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (width + (int) getResources().getDimension(R.dimen._5sdp)));
    }
}
