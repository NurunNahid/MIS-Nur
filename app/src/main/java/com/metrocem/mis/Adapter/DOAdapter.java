package com.metrocem.mis.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metrocem.mis.Activity.OrderDetailActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Container.DOOrderContainer;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DOAdapter extends RecyclerView.Adapter<DOAdapter.DoRecyclerHolder>{

    private ArrayList<DOOrderContainer> do_array;
    public Context context;
    private SelectedDO selectedDO;

    public DOAdapter(Context context, ArrayList<DOOrderContainer> orders, SelectedDO selectedDO) {
        this.do_array = orders;
        this.context = context;
        this.selectedDO = selectedDO;
    }

    @NonNull
    @Override
    public DoRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_order_cell, viewGroup, false);

        return new DoRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoRecyclerHolder doRecyclerHolder, final int i) {

        final DOOrderContainer do_order = (DOOrderContainer) do_array.get(i);
        doRecyclerHolder.do_number.setText(do_order.doNumber);
        doRecyclerHolder.product_name.setText(do_order.productName);

        String dateStr;
        DateFormat dateFormatter = null;
        if (do_order.approvedAt != null) {
            Log.d("response", "call");
            dateStr = do_order.approvedAt;
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

        }else {
            dateStr = do_order.createdAt;
            dateFormatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        }

        //dateFormatter.setLenient(false);
        Date convertedDate = new Date();

        try {
            convertedDate = dateFormatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (do_order.approvedAt != null) {
            dateFormatter = new SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault());
        }else{
            dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        }

        String time = dateFormatter.format(convertedDate);

        doRecyclerHolder.approve_date.setText(time);
        if(do_order.status.toLowerCase().equals("delivered") || do_order.status.toLowerCase().equals("partial_delivered")){
            doRecyclerHolder.status.setText(do_order.status.toUpperCase());
        }
        doRecyclerHolder.qty.setText(String.valueOf(do_order.actualBagQty));
        doRecyclerHolder.dealerName.setText(do_order.dealerName);

//        doRecyclerHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //DOOrderContainer doObj  = do_order;
//                Pair[] pair = new Pair[5];
//                pair[0] = new Pair<View, String>(doRecyclerHolder.qty,"qty_tran");
//                pair[1] = new Pair<View, String>(doRecyclerHolder.do_number,"do_no_tran");
//                pair[2] = new Pair<View, String>(doRecyclerHolder.do_number,"product_name_tran");
//                pair[3] = new Pair<View, String>(doRecyclerHolder.do_number,"dealer_tran");
//                pair[4] = new Pair<View, String>(doRecyclerHolder.do_number,"date_tran");
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pair);
//                Intent intent = new Intent(context, OrderDetailActivity.class);
//                intent.putExtra("SELECTED_DO", (Serializable) do_order);
//                context.startActivity(intent, options.toBundle());
//            }
//        });

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation);
        doRecyclerHolder.container.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return do_array.size();
    }

    public class DoRecyclerHolder extends RecyclerView.ViewHolder {

        private TextView do_number,product_name,approve_date, qty, dealerName, status;
        CardView container;
        //private Button status;
        //OnDoListener listener;

        public DoRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            do_number = itemView.findViewById(R.id.doNumber);
            product_name = itemView.findViewById(R.id.product_name);
            approve_date = itemView.findViewById(R.id.approve_date);
            status = itemView.findViewById(R.id.status);
            qty = itemView.findViewById(R.id.bagQuantity);
            dealerName = itemView.findViewById(R.id.dealer_name_TV);

            //this.listener = listener;
            //itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedDO.selectedDO(do_array.get(getAdapterPosition()));
                }
            });

        }

    }
    public interface SelectedDO {
        void selectedDO(DOOrderContainer selectedOrder);
    }

}