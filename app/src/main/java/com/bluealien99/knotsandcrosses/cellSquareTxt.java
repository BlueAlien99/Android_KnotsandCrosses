package com.bluealien99.knotsandcrosses;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class cellSquareTxt extends TextView {

    public cellSquareTxt(Context context) {
        super(context);
    }

    public cellSquareTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
