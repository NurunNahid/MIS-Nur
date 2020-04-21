package com.metrocem.mismetrocem.OrderInformation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.metrocem.mismetrocem.Activity.OrderDetailActivity;
import com.metrocem.mismetrocem.Adapter.DeliveredOrderAdapter;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class OrderPendingActivity extends AppCompatActivity {

    String[] monthNameArray;
    TextView btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pending);

        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
        String thisM = sdf.format(c.getTime());

        c.add(Calendar.MONTH, -1);
        String previousM = sdf.format(c.getTime());

        monthNameArray = new String[]{"Today","Yesterday","Last 7 days",thisM,previousM,"Last 6 months"};

        btnView = findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsBtnClick();
            }
        });

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
        if (currentUser.role.equals("Dealer") || currentUser.role.equals("dealer")){
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

    public void optionsBtnClick() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your option");

        // add a list
        builder.setItems(monthNameArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        btnView.setText(monthNameArray[which]);
                        break;
                    case 1:
                        btnView.setText(monthNameArray[which]);
                        break;
                    case 2:
                        btnView.setText(monthNameArray[which]);
                        break;
                    case 3:
                        btnView.setText(monthNameArray[which]);
                        //getMonthData(monthNameArray[which]);
                        break;
                    case 4:
                        btnView.setText(monthNameArray[which]);
                        //getMonthData(monthNameArray[which]);
                        break;
                    case 5:
                        btnView.setText(monthNameArray[which]);
                        break;


                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
