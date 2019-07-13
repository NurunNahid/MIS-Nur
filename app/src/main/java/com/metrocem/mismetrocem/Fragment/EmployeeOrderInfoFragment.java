package com.metrocem.mismetrocem.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.metrocem.mismetrocem.Activity.OrderHistoryActivity;
import com.metrocem.mismetrocem.Activity.OrderTrendActivity;
import com.metrocem.mismetrocem.Activity.CurrentPendingOrderActivity;
import com.metrocem.mismetrocem.R;

public class EmployeeOrderInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emp_order_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.order_info);

        LinearLayout orderHistoryBtn = view.findViewById(R.id.orderHistoryBtn);
        orderHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                getActivity().startActivity(intent);

            }
        });


        LinearLayout pendingOrderBtn = view.findViewById(R.id.pendingOrderBtn);
        pendingOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CurrentPendingOrderActivity.class);
                getActivity().startActivity(intent);

            }
        });

        LinearLayout trendOrderBtn = view.findViewById(R.id.trendOrderBtn);
        trendOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), OrderTrendActivity.class);
                getActivity().startActivity(intent);

            }
        });

    }
}
