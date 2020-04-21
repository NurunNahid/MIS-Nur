package com.metrocem.mis;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.metrocem.mis.Activity.BrandCommunicationActivity;
import com.metrocem.mis.Activity.EmployeeTradeProActivity;
import com.metrocem.mis.EmployeeTradePromotions.ExistingTradeActivity;
import com.metrocem.mis.Subclasses.CurrentUser;
import com.metrocem.mis.Subclasses.DataManager;

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
                if (currentUser.role.equals("Dealer") || currentUser.role.equals("dealer")){
                    Intent intent = new Intent(getActivity(), ExistingTradeActivity.class);
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
