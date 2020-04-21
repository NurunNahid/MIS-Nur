package com.metrocem.mis.TradeBrandPromotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.metrocem.mis.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.http.Url;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList multimediaList;
    //CurrentUser currentUser;
    boolean isImageFitToScreen;
    //private ImageViewClickListener mImageViewClickListener;

    // Constructor
    public GridAdapter(Context c, ArrayList multimediaArrayList) {
        mContext = c;
        multimediaList = multimediaArrayList;
    }



    @Override
    public int getCount() {

        return multimediaList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View inFlateview;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            inFlateview = mInflater.inflate(R.layout.multimedia_grid_cell, null);
        }
        else
        {
            inFlateview =  convertView;
        }

        MultimediaInfo info = (MultimediaInfo) multimediaList.get(position);
        final ImageView imageView = inFlateview.findViewById(R.id.imageView);
        //final ImageView imageView2 = inFlateview.findViewById(R.id.imageView2);

        TextView titleText = inFlateview.findViewById(R.id.titleTextView);
        titleText.setText(info.getTitle());
        TextView descriptionText = inFlateview.findViewById(R.id.descriptionTextView);
        descriptionText.setText(info.getDescription());

        final String imagePath = "http://mis.nurtech.xyz/storage/"+info.getPath();
        Picasso.with(mContext).load(imagePath).fit().centerCrop().into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imageView.setVisibility(View.GONE);
                //imageView2.setVisibility(View.VISIBLE);
                //Picasso.with(mContext).load(imagePath).into(imageView2);
                Log.d("TAG", "onClick " + position);
                //mImageViewClickListener.onImageClicked(position);

            }
        });

        return inFlateview;
    }

//    public interface ImageViewClickListener {
//        void onImageClicked(int position);
//    }
//
//    public void setImageViewClickListener (ImageViewClickListener viewClickListener) {
//        mImageViewClickListener = viewClickListener;
//    }


}
