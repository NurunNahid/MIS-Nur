package com.metrocem.mis.ReuestedDOInfo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.metrocem.mis.OrderInformation.OrderTrendActivity;
import com.metrocem.mis.R;

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

        LinearLayout requestedDOHistory = view.findViewById(R.id.requestedDOHistory);
        requestedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RequestedOrderActivity.class);
                intent.putExtra("DO_STATUS", "requested");
                startActivity(intent);

            }
        });

        LinearLayout processedDOHistory = view.findViewById(R.id.processedDOHistory);
        processedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RequestedOrderActivity.class);
                intent.putExtra("DO_STATUS", "approved");
                startActivity(intent);

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
