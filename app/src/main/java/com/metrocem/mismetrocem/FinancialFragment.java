package com.metrocem.mismetrocem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metrocem.mismetrocem.Subclasses.DataManager;

public class FinancialFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_financial, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.financial_info);

        TextView creditLimitBtn = view.findViewById(R.id.creditLimitBtn);
        creditLimitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Credit Limit";
                String msg = "Your current Credit Limit is BDT 50,00,000.";
                DataManager.alertShow(title, msg, getActivity());

            }
        });

        TextView dsoBtn = view.findViewById(R.id.dsoBtn);
        dsoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "DSO";
                String msg = "Your last 30 days DSO is 35 days.";
                DataManager.alertShow(title, msg, getActivity());
            }
        });

        TextView collectionReportBtn = view.findViewById(R.id.collectionReportBtn);
        collectionReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Collection Report";
                String msg = "Total Collection Received BDT 1,00,000 (One Lac).";
                DataManager.alertShow(title, msg, getActivity());

            }
        });

        TextView dueBalanceBtn = view.findViewById(R.id.dueBalanceBtn);
        dueBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tap", "tap");
                String title = "Due Balance";
                String msg = "Your last due balance is 56,00,000.";
                DataManager.alertShow(title, msg, getActivity());

            }
        });



    }
}
