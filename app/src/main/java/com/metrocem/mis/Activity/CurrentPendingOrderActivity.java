package com.metrocem.mis.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.metrocem.mis.Adapter.DeliveredOrderAdapter;
import com.metrocem.mis.R;

public class CurrentPendingOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        ListView orderListView = findViewById(R.id.orderListView);

        DeliveredOrderAdapter adapter = new DeliveredOrderAdapter(this,"Delivered");
        orderListView.setAdapter(adapter);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position = ", String.valueOf(position));

                Intent intent = new Intent(CurrentPendingOrderActivity.this, OrderDetailActivity.class);
                CurrentPendingOrderActivity.this.startActivity(intent);

            }
        });
    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        getSupportActionBar().setTitle("Current Pending Orders"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
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
