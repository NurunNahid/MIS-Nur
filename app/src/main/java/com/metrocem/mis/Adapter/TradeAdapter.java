package com.metrocem.mismetrocem.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metrocem.mismetrocem.R;

public class TradeAdapter extends BaseAdapter {

    public Context mContext;
    String[] options;

    public TradeAdapter(FragmentActivity activity, String[] array) {
        this.mContext = activity;
        this.options = array;
    }


    //public TradeAdapter(FragmentActivity activity) {
        //mContext = activity;
    //}

    @Override
    public int getCount() {
        Log.d("count", String.valueOf(this.options.length));
        return this.options.length;
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
            inFlateview = mInflater.inflate(R.layout.trade_cell, null);
        }
        else
        {
            inFlateview =  convertView;
        }

        TextView title = inFlateview.findViewById(R.id.titleText);
        title.setText(this.options[position]);
        return inFlateview;
    }

}
