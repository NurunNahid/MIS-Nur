package com.metrocem.mismetrocem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.metrocem.mismetrocem.Activity.BrandCommunicationActivity;
import com.metrocem.mismetrocem.Activity.EmployeeTradeProActivity;
import com.metrocem.mismetrocem.Activity.TradePromotionActivity;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;

public class TradeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trade, null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Context context = null;

        getActivity().setTitle(R.string.trade_brand);
        final CurrentUser currentUser = DataManager.getCurrentUser(getActivity());

        LinearLayout tradeProBtn = (LinearLayout) view.findViewById(R.id.tradePromotionBtn);
        tradeProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser.role.equals("Dealer")){
                    Intent intent = new Intent(getActivity(), TradePromotionActivity.class);
                    getActivity().startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), EmployeeTradeProActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });


        LinearLayout brandCommBtn = (LinearLayout) view.findViewById(R.id.brandCommunicationBtn);
        brandCommBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), BrandCommunicationActivity.class);
                getActivity().startActivity(intent);

            }
        });


    }



}
