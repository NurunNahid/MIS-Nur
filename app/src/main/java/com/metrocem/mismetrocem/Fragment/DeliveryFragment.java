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

import com.metrocem.mismetrocem.Activity.OrderDeliveredActivity;
import com.metrocem.mismetrocem.Activity.OrderDetailActivity;
import com.metrocem.mismetrocem.Activity.OrderPendingActivity;
import com.metrocem.mismetrocem.R;

public class DeliveryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.delivery_info);

        LinearLayout deliveredBtn = view.findViewById(R.id.deliveredBtn);
        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderDeliveredActivity.class);
                getActivity().startActivity(intent);
            }
        });

        LinearLayout pendingBtn = view.findViewById(R.id.pendingBtn);
        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderPendingActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }
}
