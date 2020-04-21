package com.metrocem.mismetrocem.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.metrocem.mismetrocem.Container.DOOrderContainer;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.Subclasses.SecurityInfoContainer;

import java.util.List;

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.SecurityRecyclerHolder> {

    private List<SecurityInfoContainer> securityInfos;
    public Context context;

    public SecurityAdapter(Context context, List<SecurityInfoContainer> securities) {
        this.securityInfos = securities;
        this.context = context;
    }

    @NonNull
    @Override
    public SecurityRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.security_cell, viewGroup, false);
        return new SecurityAdapter.SecurityRecyclerHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SecurityRecyclerHolder securityRecyclerHolder, int i) {

        Log.d("response new", securityInfos.get(i).dealerName);
        securityRecyclerHolder.name.setText(securityInfos.get(i).dealerName);
        securityRecyclerHolder.bank_guarantee.setText(securityInfos.get(i).bank_guarantee);
        securityRecyclerHolder.security_cheque.setText(securityInfos.get(i).security_cheque);
        securityRecyclerHolder.blank_cheque.setText(securityInfos.get(i).blank_cheque);

    }

    @Override
    public int getItemCount() {
        Log.d("response count", String.valueOf(securityInfos.size()));
        return securityInfos.size();
    }

    public class SecurityRecyclerHolder extends RecyclerView.ViewHolder {

        private TextView name,bank_guarantee,security_cheque, blank_cheque;
        private Button status;

        public SecurityRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.dealerName);
            bank_guarantee = itemView.findViewById(R.id.bank_guarantee_TV);
            security_cheque = itemView.findViewById(R.id.security_cheque_TV);
            blank_cheque = itemView.findViewById(R.id.blank_cheque_TV);

        }
    }
}
