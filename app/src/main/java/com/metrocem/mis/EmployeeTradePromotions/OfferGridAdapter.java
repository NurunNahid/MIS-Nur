package com.metrocem.mis.EmployeeTradePromotions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metrocem.mis.R;
import com.metrocem.mis.Model.Promotion;

import java.util.ArrayList;

public class OfferGridAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList promotionArray;

    // Constructor
    public OfferGridAdapter(Context c, ArrayList promotionArrayList) {
        mContext = c;
        promotionArray = promotionArrayList;
    }
    @Override
    public int getCount() {
        return promotionArray.size();
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

        Promotion promotion = (Promotion) promotionArray.get(position);

        //ImageView imageView = inFlateview.findViewById(R.id.imageView);
        TextView titleText = inFlateview.findViewById(R.id.titleTextView);
        titleText.setText(promotion.getName());


        return inFlateview;
    }
}
