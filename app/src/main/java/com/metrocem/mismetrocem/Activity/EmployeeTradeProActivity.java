package com.metrocem.mismetrocem.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.metrocem.mismetrocem.EmployeeTradePromotions.ExistingTradeActivity;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;

public class EmployeeTradeProActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_trade_pro);
    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        CurrentUser currentUser = DataManager.getCurrentUser(this);

        getSupportActionBar().setTitle("Trade Promotions"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorOrange)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout existingTrade = findViewById(R.id.existingTrade);
        existingTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmployeeTradeProActivity.this, ExistingTradeActivity.class);
                startActivity(intent);

            }
        });

        LinearLayout allTradeBtn = findViewById(R.id.allTradeBtn);
        allTradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                //getActivity().startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
