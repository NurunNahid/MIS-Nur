package com.metrocem.mismetrocem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.metrocem.mismetrocem.Activity.ChallanDetailActivity;
import com.metrocem.mismetrocem.Activity.OrderDetailActivity;
import com.metrocem.mismetrocem.Container.ChallanContainer;
import com.metrocem.mismetrocem.Container.DOOrderContainer;
import com.metrocem.mismetrocem.R;

import java.io.Serializable;
import java.util.List;

public class ChallanAdapter extends RecyclerView.Adapter<ChallanAdapter.ChallanRecyclerHolder> {

    private List<ChallanContainer> challan;
    Context context;

    public ChallanAdapter(Context context, List<ChallanContainer> challan) {
        this.challan = challan;
        this.context = context;
    }

    @NonNull
    @Override
    public ChallanAdapter.ChallanRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_cell, viewGroup, false);

        return new ChallanAdapter.ChallanRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallanAdapter.ChallanRecyclerHolder doRecyclerHolder, final int i) {

        doRecyclerHolder.do_number.setText(challan.get(i).doNumber);
        doRecyclerHolder.product_name.setText(challan.get(i).productName);
        doRecyclerHolder.approve_date.setText(challan.get(i).doAllocating);
        doRecyclerHolder.status.setText(challan.get(i).status);
        doRecyclerHolder.qty.setText(challan.get(i).bagQuantity.toString());

        doRecyclerHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChallanContainer challanObj  = challan.get(i);
                Log.d("response", String.valueOf(challan.get(i)));
                Intent intent = new Intent(context, ChallanDetailActivity.class);
                intent.putExtra("Challan_Obj", (Serializable) challanObj);

                //intent.putExtra("SELECTED_CHALLAN", i);
                //intent.putExtra("DO_NO",challan.get(i).doNumber);
                context.startActivity(intent);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        doRecyclerHolder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return challan.size();
    }

    public class ChallanRecyclerHolder extends RecyclerView.ViewHolder {

        private TextView do_number,product_name,approve_date, qty;
        private Button status;

        public ChallanRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            do_number = itemView.findViewById(R.id.doNumber);
            product_name = itemView.findViewById(R.id.product_name);
            approve_date = itemView.findViewById(R.id.approve_date);
            status = itemView.findViewById(R.id.statusBtn);
            qty = itemView.findViewById(R.id.bagQuantity);

        }
    }
}
