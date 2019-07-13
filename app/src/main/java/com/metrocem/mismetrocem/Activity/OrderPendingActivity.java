package com.metrocem.mismetrocem.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.metrocem.mismetrocem.Adapter.DeliveredOrderAdapter;
import com.metrocem.mismetrocem.Adapter.OrderAdapter;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;

public class OrderPendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pending);

        ListView orderPendingListView = findViewById(R.id.orderPendingListView);

        DeliveredOrderAdapter adapter = new DeliveredOrderAdapter(this, "Pending");
        orderPendingListView.setAdapter(adapter);
        orderPendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position = ", String.valueOf(position));

                Intent intent = new Intent(OrderPendingActivity.this, OrderDetailActivity.class);
                startActivity(intent);

            }
        });
        
    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        CurrentUser currentUser = DataManager.getCurrentUser(this);

        getSupportActionBar().setTitle("Pending"); // for set actionbar title
        if (currentUser.role.equals("Dealer")){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorGreen)));
        }else {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorOrange)));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
