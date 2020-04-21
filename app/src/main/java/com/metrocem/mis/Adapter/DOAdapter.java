package com.metrocem.mismetrocem.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.metrocem.mismetrocem.Activity.OrderDetailActivity;
import com.metrocem.mismetrocem.Container.ChallanContainer;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.Container.DOOrderContainer;

import java.io.Serializable;
import java.util.List;

public class DOAdapter extends RecyclerView.Adapter<DOAdapter.DoRecyclerHolder>{

    private List<DOOrderContainer> do_order;
    public Context context;

    public DOAdapter(Context context, List<DOOrderContainer> orders) {
        this.do_order = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public DoRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_order_cell, viewGroup, false);

        return new DoRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoRecyclerHolder doRecyclerHolder, final int i) {

        doRecyclerHolder.do_number.setText(do_order.get(i).doNumber);
        doRecyclerHolder.product_name.setText(do_order.get(i).productName);
        doRecyclerHolder.approve_date.setText(do_order.get(i).approvedAt);
        if(do_order.get(i).status.toLowerCase().equals("delivered") || do_order.get(i).status.toLowerCase().equals("partial_delivered")){
            doRecyclerHolder.status.setText(do_order.get(i).status.toUpperCase());
        }
        doRecyclerHolder.qty.setText(do_order.get(i).actualBagQty.toString());
        doRecyclerHolder.dealerName.setText(do_order.get(i).dealerName);

        doRecyclerHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOOrderContainer doObj  = do_order.get(i);
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("SELECTED_DO", (Serializable) doObj);
                context.startActivity(intent);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        doRecyclerHolder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return do_order.size();
    }

    public class DoRecyclerHolder extends RecyclerView.ViewHolder {

        private TextView do_number,product_name,approve_date, qty, dealerName, status;
        //private Button status;

        public DoRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            do_number = itemView.findViewById(R.id.doNumber);
            product_name = itemView.findViewById(R.id.product_name);
            approve_date = itemView.findViewById(R.id.approve_date);
            status = itemView.findViewById(R.id.status);
            qty = itemView.findViewById(R.id.bagQuantity);
            dealerName = itemView.findViewById(R.id.dealer_name_TV);

        }
    }

}