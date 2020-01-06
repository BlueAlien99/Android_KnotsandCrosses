package com.bluealien99.knotsandcrosses;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.widget.Chronometer;

public class Chrono extends Chronometer {

    Context mContext;

    private long state = 0;
    private boolean running = false;

    public Chrono(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void start() {
        if (!running) {
            running = true;
            setBase(SystemClock.elapsedRealtime() + state);
            setTextColor(ContextCompat.getColor(mContext, R.color.dk_gray));
            super.start();
        }
    }

    @Override
    public void stop() {
        if (running) {
            super.stop();
            running = false;
            state = getBase() - SystemClock.elapsedRealtime();
            setTextColor(ContextCompat.getColor(mContext, R.color.Cross));
        }
    }
}
