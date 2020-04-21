package com.metrocem.mismetrocem.EmployeeTradePromotions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.metrocem.mismetrocem.R;

public class OfferGridAdapter extends BaseAdapter {
    private Context mContext;

    // Constructor
    public OfferGridAdapter(Context c) {
        mContext = c;
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
        View inFlateview;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            inFlateview = mInflater.inflate(R.layout.offer_grid_cell, null);
        }
        else
        {
            inFlateview =  convertView;
        }

        //ImageView imageView = inFlateview.findViewById(R.id.imageView);
        //TextView titleText = inFlateview.findViewById(R.id.titleTextView);


        return inFlateview;
    }
}
