package com.metrocem.mismetrocem.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.metrocem.mismetrocem.EmployeeOrderInfo.AllocatedDOActivity;
import com.metrocem.mismetrocem.EmployeeOrderInfo.ProcessedDOActivity;
import com.metrocem.mismetrocem.EmployeeOrderInfo.RequestedOrderActivity;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        getSupportActionBar().setTitle("Order History"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorOrange)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout requestedDOHistory = findViewById(R.id.requestedDOHistory);
        requestedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderHistoryActivity.this, RequestedOrderActivity.class);
                startActivity(intent);

            }
        });

        LinearLayout processedDOHistory = findViewById(R.id.processedDOHistory);
        processedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderHistoryActivity.this, ProcessedDOActivity.class);
                startActivity(intent);

            }
        });

        LinearLayout allocatedDOHistory = findViewById(R.id.allocatedDOHistory);
        allocatedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderHistoryActivity.this, AllocatedDOActivity.class);
                startActivity(intent);

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
