package com.metrocem.mis.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metrocem.mis.R;

public class DeliveredOrderAdapter extends BaseAdapter {
    public Context mContext;
    public String orderStatus;

    public DeliveredOrderAdapter(Context activity, String status) {
        mContext = activity;
        this.orderStatus = status;
    }

    @Override
    public int getCount() {
        return 5;
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
            inFlateview = mInflater.inflate(R.layout.delivered_order_cell, null);
        }
        else
        {
            inFlateview =  convertView;
        }

        TextView receiveBtn = inFlateview.findViewById(R.id.receiveBtn);

        if (orderStatus.equals("Delivered")){
           receiveBtn.setBackgroundColor(Color.GREEN);
            receiveBtn.setText("RECEIVED");
        }else {
            receiveBtn.setBackgroundColor(Color.RED);
            receiveBtn.setText("NOT RECEIVED");
        }

        return inFlateview;    }
}
