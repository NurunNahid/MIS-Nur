package com.metrocem.mis.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.metrocem.mis.Challan.ChallanDetailActivity;
import com.metrocem.mis.Container.ChallanContainer;
import com.metrocem.mis.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChallanAdapter extends RecyclerView.Adapter<ChallanAdapter.ChallanRecyclerHolder> {

    private List<ChallanContainer> challan;
    private Context context;

    public ChallanAdapter(Context context, List<ChallanContainer> challan) {
        this.challan = challan;
        this.context = context;
    }

    @NonNull
    @Override
    public ChallanAdapter.ChallanRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.challan_cell, viewGroup, false);

        return new ChallanAdapter.ChallanRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallanAdapter.ChallanRecyclerHolder doRecyclerHolder, final int i) {

        doRecyclerHolder.do_number.setText(challan.get(i).doNumber);
        doRecyclerHolder.product_name.setText(challan.get(i).productName);
        doRecyclerHolder.status.setText(challan.get(i).status.toUpperCase());
        doRecyclerHolder.qty.setText(String.valueOf(challan.get(i).bagQuantity));

        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(challan.get(i).doAllocating);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        spf= new SimpleDateFormat("dd MMM yy hh:mm a");
        String date = spf.format(newDate);

        doRecyclerHolder.approve_date.setText(date);

        doRecyclerHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChallanContainer challanObj  = challan.get(i);
                Intent intent = new Intent(context, ChallanDetailActivity.class);
                intent.putExtra("Challan_Obj", (Serializable) challanObj);
                context.startActivity(intent);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation);
        doRecyclerHolder.container.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return challan.size();
    }

    public class ChallanRecyclerHolder extends RecyclerView.ViewHolder {

        private TextView do_number,product_name,approve_date, qty, status;
        CardView container;
        //private Button status;

        public ChallanRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            do_number = itemView.findViewById(R.id.doNumber);
            product_name = itemView.findViewById(R.id.product_name);
            approve_date = itemView.findViewById(R.id.approve_date);
            status = itemView.findViewById(R.id.statusBtn);
            qty = itemView.findViewById(R.id.bagQuantity);
            container = itemView.findViewById(R.id.container);

        }
    }
}
