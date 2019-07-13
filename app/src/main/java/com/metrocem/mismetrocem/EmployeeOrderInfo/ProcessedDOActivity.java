package com.metrocem.mismetrocem.EmployeeOrderInfo;

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

import com.metrocem.mismetrocem.Activity.OrderDetailActivity;
import com.metrocem.mismetrocem.Adapter.OrderAdapter;
import com.metrocem.mismetrocem.R;

public class ProcessedDOActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processed_do);

        getSupportActionBar().setTitle("Processed DO History"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorOrange)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView orderListView = findViewById(R.id.orderListView);

        OrderAdapter adapter = new OrderAdapter(this);
        orderListView.setAdapter(adapter);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position = ", String.valueOf(position));

                Intent intent = new Intent(ProcessedDOActivity.this, OrderDetailActivity.class);
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
