package com.bluealien99.knotsandcrosses;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class squareAdapter extends BaseAdapter {

    private Context mContext;

    public squareAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.cell, parent, false);

        View korc = view.findViewById(R.id.korc);
        korc.setBackgroundColor(ContextCompat.getColor(mContext, R.color.empty));

        View cover = view.findViewById(R.id.cover);
        cover.setAlpha(1f);
        cover.setVisibility(View.VISIBLE);

        return view;
    }
}
