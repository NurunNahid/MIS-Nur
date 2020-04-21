package com.metrocem.mis.Activity;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.metrocem.mis.R;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Model.DataManager;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        CurrentUser currentUser = DataManager.getCurrentUser(this);
        //Log.d("response", currentUser.role);

        getSupportActionBar().setTitle("Order Detail"); // for set actionbar title
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorBeach) ));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();

        DOOrderContainer order = (DOOrderContainer) bundle.getSerializable("SELECTED_DO");
        TextView do_Id_TV = findViewById(R.id.do_Id_TV);
        do_Id_TV.setText(order.id.toString());

        TextView employee_name_TV = findViewById(R.id.employee_name_TV);
        employee_name_TV.setText(order.employeeName);

        TextView dealer_name_TV = findViewById(R.id.dealer_name_TV);
        dealer_name_TV.setText(order.dealerName);

        TextView bag_qty_TV = findViewById(R.id.bag_qty_TV);
        bag_qty_TV.setText(order.actualBagQty.toString());

        TextView delivery_address_TV = findViewById(R.id.delivery_address_TV);
        delivery_address_TV.setText(order.deliveryAddress);

        TextView contact_no_TV = findViewById(R.id.contact_no_TV);
        contact_no_TV.setText(order.contactNumber);

        TextView unit_price_TV = findViewById(R.id.unit_price_TV);
        unit_price_TV.setText(order.unitPrice.toString());

        TextView product_name_TV = findViewById(R.id.product_name_TV);
        product_name_TV.setText(order.productName);

        TextView do_number_TV = findViewById(R.id.do_number_TV);
        do_number_TV.setText(order.doNumber);

        TextView mode_TV = findViewById(R.id.mode_TV);
        mode_TV.setText(order.deliveryMode);

        TextView processing_time_TV = findViewById(R.id.processing_time_TV);
        processing_time_TV.setText(order.createdAt);

        TextView allocated_time_TV = findViewById(R.id.allocated_time_TV);
        allocated_time_TV.setText(order.approvedAt);


    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

//        CurrentUser currentUser = DataManager.getCurrentUser(this);
//
//        getSupportActionBar().setTitle("Order Detail"); // for set actionbar title
//        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorBeach) ));
//        if (currentUser.role.equals("Dealer") || currentUser.role.equals("dealer")){
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorGreen)));
//        }else {
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorOrange)));
//        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        //Bundle extras = getIntent().getExtras();
//        //Order order = (Order) extras.getSerializable("SELECTED_DO");
//        Intent mIntent = getIntent();
//        int intValue = mIntent.getIntExtra("SELECTED_DO", 0);
//        Log.d("response", String.valueOf(intValue));
//        ArrayList doArray = DataManager.getDOOrderList(this);
//        //Object order = doArray.get(intValue);
//
//        DOOrderContainer order = (DOOrderContainer) doArray.get(intValue);
//        TextView do_Id_TV = findViewById(R.id.do_Id_TV);
//        do_Id_TV.setText(order.id.toString());
//
//        TextView employee_name_TV = findViewById(R.id.employee_name_TV);
//        employee_name_TV.setText(order.employeeName);
//
//        TextView dealer_name_TV = findViewById(R.id.dealer_name_TV);
//        dealer_name_TV.setText(order.dealerName);
//
//        TextView bag_qty_TV = findViewById(R.id.bag_qty_TV);
//        bag_qty_TV.setText(order.actualBagQty.toString());
//
//        TextView delivery_address_TV = findViewById(R.id.delivery_address_TV);
//        delivery_address_TV.setText(order.deliveryAddress);
//
//        TextView contact_no_TV = findViewById(R.id.contact_no_TV);
//        contact_no_TV.setText(order.contactNumber);
//
//        TextView unit_price_TV = findViewById(R.id.unit_price_TV);
//        unit_price_TV.setText(order.unitPrice.toString());
//
//        TextView product_name_TV = findViewById(R.id.product_name_TV);
//        product_name_TV.setText(order.productName);
//
//        TextView do_number_TV = findViewById(R.id.do_number_TV);
//        do_number_TV.setText(order.doNumber);
//
//        TextView mode_TV = findViewById(R.id.mode_TV);
//        mode_TV.setText(order.deliveryMode);
//
//        TextView processing_time_TV = findViewById(R.id.processing_time_TV);
//        processing_time_TV.setText(order.createdAt);
//
//        TextView allocated_time_TV = findViewById(R.id.allocated_time_TV);
//        allocated_time_TV.setText(order.approvedAt);




        //Log.d("response",order.getDealerName());
        //List<Order> order = new LiOrder();
        //order.get(1).getDoNumber();
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//        // your code.
//    }

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
