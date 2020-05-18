package com.otapp.net.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.otapp.net.R;

public class RoundRectCornerLinearLayout extends LinearLayout {

    private float radius = 18.0f;
    private Path path;
    private RectF rect;

    public RoundRectCornerLinearLayout(Context context) {
        super(context);
        init();
    }

    public RoundRectCornerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundRectCornerLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
        radius = getResources().getDimension(R.dimen._5sdp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
