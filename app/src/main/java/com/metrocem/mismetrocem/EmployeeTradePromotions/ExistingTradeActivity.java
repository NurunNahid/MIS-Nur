package com.metrocem.mismetrocem.EmployeeTradePromotions;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.metrocem.mismetrocem.Adapter.TradePromotionAdapter;
import com.metrocem.mismetrocem.R;

public class ExistingTradeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_trade);

        getSupportActionBar().setTitle("Existing Trade Promotions"); // for set actionbar title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorOrange)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView tradePromotionListView = findViewById(R.id.tradePromotionListView);

        TradePromotionAdapter adapter = new TradePromotionAdapter(this);
        tradePromotionListView.setAdapter(adapter);
        tradePromotionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position = ", String.valueOf(position));

                //Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
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