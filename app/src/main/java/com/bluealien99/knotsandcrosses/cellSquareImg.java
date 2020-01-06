package com.bluealien99.knotsandcrosses;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class cellSquareImg extends ImageView {

    public cellSquareImg(Context context) {
        super(context);
    }

    public cellSquareImg(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
