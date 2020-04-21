package com.metrocem.mismetrocem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metrocem.mismetrocem.R;

public class OrderAdapter extends BaseAdapter {

    public Context mContext;

    public OrderAdapter(Context activity) {
        mContext = activity;
    }

    @Override
    public int getCount() {
        return 0;
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

        final View inFlateview;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            inFlateview = mInflater.inflate(R.layout.order_cell, null);
        }
        else
        {
            inFlateview =  convertView;
        }

        TextView receiveBtn = inFlateview.findViewById(R.id.receiveBtn);

//        if (position == 2){
//
//            receiveBtn.setBackgroundColor(Color.GREEN);
//            receiveBtn.setText("RECEIVED");
//        }
        return inFlateview;
    }
}
