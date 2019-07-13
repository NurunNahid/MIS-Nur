package com.metrocem.mismetrocem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.metrocem.mismetrocem.Activity.OrderDetailActivity;
import com.metrocem.mismetrocem.Adapter.OrderAdapter;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;

public class OrderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.order_info);

        //CurrentUser currentUser = DataManager.getCurrentUser(getActivity());

        ListView orderListView = view.findViewById(R.id.orderListView);

        if (getActivity()!=null){
            OrderAdapter adapter = new OrderAdapter(getActivity());
            orderListView.setAdapter(adapter);
            orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("position = ", String.valueOf(position));

                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    getActivity().startActivity(intent);

                }
            });
        }

    }
}
