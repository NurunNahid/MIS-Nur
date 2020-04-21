package com.metrocem.mismetrocem.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.metrocem.mismetrocem.Activity.TradePromotionActivity;
import com.metrocem.mismetrocem.R;

public class TradePromotionAdapter extends BaseAdapter {
    public Context mContext;

    public TradePromotionAdapter(Context activity) {
        this.mContext = activity;
    }


    @Override
    public int getCount() {
        return 3;
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
            inFlateview = mInflater.inflate(R.layout.trade_promotion_cell, null);
        }
        else
        {
            inFlateview =  convertView;
        }
        return inFlateview;
    }
}
