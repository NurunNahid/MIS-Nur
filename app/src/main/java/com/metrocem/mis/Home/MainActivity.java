package com.metrocem.mismetrocem.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.metrocem.mismetrocem.FinancialFragment;
import com.metrocem.mismetrocem.Fragment.AboutUsFragment;
import com.metrocem.mismetrocem.CommissionAndIncentive.CommissionIncentiveFragment;
import com.metrocem.mismetrocem.Fragment.CommunicationFragment;
import com.metrocem.mismetrocem.Fragment.ContactUsFragment;
import com.metrocem.mismetrocem.Fragment.DORequestFragment;
import com.metrocem.mismetrocem.Fragment.DealerChallanFragment;
import com.metrocem.mismetrocem.Fragment.DeliveryInfoFragment;
import com.metrocem.mismetrocem.Fragment.EmployeeChallanFragment;
import com.metrocem.mismetrocem.Fragment.EmployeeDOFragment;
import com.metrocem.mismetrocem.Fragment.EmployeeDashboard;
import com.metrocem.mismetrocem.Fragment.EmployeeOrderInfoFragment;
import com.metrocem.mismetrocem.Fragment.EmployeeReportFragment;
import com.metrocem.mismetrocem.Fragment.HomeFragment;
import com.metrocem.mismetrocem.Fragment.ReportFragment;
import com.metrocem.mismetrocem.OrderFragment;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.SignIn.SignInActivity;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;
import com.metrocem.mismetrocem.TradeFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CurrentUser currentUser;
    Menu menu;
    Toolbar toolbar;
    NavigationView navigationView;
    View headerView;
    TextView menuTitle, mobileNoTV;
    public static final String MyPREFERENCES = "MyPref" ;
    SharedPreferences sharedPreferences;
    public static final String dashboard = "dashboard";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //toggle.setDrawerIndicatorEnabled(false);
        //toggle.setHomeAsUpIndicator(R.drawable.home_icon);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        String packageName = getApplicationContext().getPackageName();
        Log.d("package", packageName);

        headerView = navigationView.getHeaderView(0);
        menuTitle = headerView.findViewById(R.id.menuTitle);
        mobileNoTV = headerView.findViewById(R.id.mobileNo);

        //DataManager.removeCurrentUser(this);

        menu = navigationView.getMenu();
        navigationView.setItemIconTintList(null);
        //navigationView.setItemTextColor(ColorStateList.valueOf(R.color.colorDarkBlack));


    }

    @Override
    public void onDestroy() {

        //Log.d("response", "call");

        //SharedPreferences sp = getSharedPreferences("SIGN_IN", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        boolean cb1 = sharedPreferences.getBoolean("KEEP_SIGN_IN", false);
        //Log.d("bool", String.valueOf(cb1));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(dashboard, true);
        editor.commit();


        Log.d("response dealer", String.valueOf(sharedPreferences.getBoolean(dashboard, true)));

        if (!cb1){
            DataManager.removeCurrentUser(this);

        }
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("bool call", "call");

        //SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putBoolean("isFlag", true);
    }



    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        //currentUser = new CurrentUser();

        try {
            currentUser = DataManager.getCurrentUser(this);


            if (currentUser.role != null){


                //SharedPreferences sharedPreferences3 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                //SharedPreferences.Editor homeeditor = sharedPreferences3.edit();
                //homeeditor.putBoolean("isFlag", true);
                //homeeditor.commit();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                //Log.d("Role", currentUser.role);
                try {
                    menuTitle.setText(currentUser.userName);
                    mobileNoTV.setText(currentUser.email);
                }catch (Exception e){
                    //Log.d("error", e.getMessage());
                }

                sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                Log.d("response dealer", String.valueOf(sharedPreferences.getBoolean(dashboard, true)));

                if (currentUser.role.toLowerCase().equals("dealer")){

                    //Log.i("response call", "call");

                    //Log.i("response dealer", sharedPreferences.getBoolean("isFlag", true));

                    //toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                    //navigationView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGreen));
                    //headerView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGreen));

                    //transaction.replace(R.id.area, new HomeFragment());
                    //transaction.commit();

                    //sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                    if (sharedPreferences.getBoolean(dashboard, true)) {
                        Log.d("response", currentUser.role);

                        transaction.replace(R.id.area, new HomeFragment());
                        transaction.commit();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(dashboard, false);
                        editor.commit();
                    }




                    //SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                }else {

                    //toolbar.setBackgroundColor(Color.parseColor("#232A32"));
                    //navigationView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorOrange));
                    //headerView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGreen));

                    //transaction.replace(R.id.area, new EmployeeDashboard());
                    //transaction.commit();

                    //SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                    //Log.d("response dealer", String.valueOf(sharedPreferences.getBoolean("isFlag", true)));

                    if (sharedPreferences.getBoolean(dashboard, true)) {
                        Log.d("response call", "call 2");

                        transaction.replace(R.id.area, new EmployeeDashboard());
                        transaction.commit();

                        //SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(dashboard, false);
                        editor.commit();
                    }


                }

                getMenuOptions();

            }else {

                //Log.i("response2", currentUser.role);

                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                MainActivity.this.startActivity(intent);
            }
        }catch (Exception e){

            //Log.i("response3", e.getMessage());
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            MainActivity.this.startActivity(intent);
        }

    }

    public void getMenuOptions(){

        menu.clear();

        try {
            if (currentUser.role.equals("Dealer") || currentUser.role.equals("dealer")){

                menu.add(R.id.mainGroup, 1, 100, R.string.menu_home);
                menu.add(R.id.mainGroup, 2, 200, R.string.do_request);
                menu.add(R.id.mainGroup, 3, 300, R.string.order_info);
                menu.add(R.id.mainGroup, 4, 400, R.string.challan_info);
                menu.add(R.id.mainGroup, 5, 500, R.string.delivery_info);
                menu.add(R.id.mainGroup, 6, 600, R.string.financial_info);
                menu.add(R.id.mainGroup, 7, 700, R.string.commission_incentive);
                menu.add(R.id.mainGroup, 8, 800, R.string.trade_brand);
                menu.add(R.id.mainGroup, 9, 900, R.string.communication);
                menu.add(R.id.mainGroup, 10, 1000, R.string.report);
                menu.add(R.id.mainGroup, 11, 1100, R.string.about_us);
                menu.add(R.id.mainGroup, 12, 1200, R.string.contact_us);
                menu.add(R.id.mainGroup, 13, 1300, "Sign out");


                menu.getItem(0).setIcon(R.drawable.dashboard_icon);
                menu.getItem(1).setIcon(R.drawable.do_icon);
                menu.getItem(2).setIcon(R.drawable.order_info_icon);
                menu.getItem(3).setIcon(R.drawable.dashboard_icon);
                menu.getItem(4).setIcon(R.drawable.delivery_icon);
                menu.getItem(5).setIcon(R.drawable.financial_info);
                menu.getItem(6).setIcon(R.drawable.commission_incentive);
                menu.getItem(7).setIcon(R.drawable.dashboard_icon);
                menu.getItem(8).setIcon(R.drawable.dashboard_icon);
                menu.getItem(9).setIcon(R.drawable.reports_icon);
                menu.getItem(10).setIcon(R.drawable.dashboard_icon);
                menu.getItem(11).setIcon(R.drawable.dashboard_icon);
                menu.getItem(12).setIcon(R.drawable.signout_icon);

            }else {

                menu.add(R.id.mainGroup, 1, 100, R.string.menu_home);
                menu.add(R.id.mainGroup, 2, 200, R.string.do_request);
                menu.add(R.id.mainGroup, 3, 300, R.string.order_info);
                menu.add(R.id.mainGroup, 4, 400, R.string.challan_info);
                menu.add(R.id.mainGroup, 5, 500, R.string.delivery_info);
                menu.add(R.id.mainGroup, 6, 600, R.string.report);
                menu.add(R.id.mainGroup, 7, 700, R.string.commission_incentive);
                menu.add(R.id.mainGroup, 8, 800, R.string.trade_brand);
                menu.add(R.id.mainGroup, 9, 900, R.string.communication);
                menu.add(R.id.mainGroup, 10, 1000, "Sign out");


                menu.getItem(0).setIcon(R.drawable.dashboard_icon);
                menu.getItem(1).setIcon(R.drawable.do_icon);
                menu.getItem(2).setIcon(R.drawable.order_info_icon);
                menu.getItem(3).setIcon(R.drawable.dashboard_icon);
                menu.getItem(4).setIcon(R.drawable.delivery_icon);
                menu.getItem(5).setIcon(R.drawable.reports_icon);
                menu.getItem(6).setIcon(R.drawable.commission_incentive);
                menu.getItem(7).setIcon(R.drawable.dashboard_icon);
                menu.getItem(8).setIcon(R.drawable.dashboard_icon);
                menu.getItem(9).setIcon(R.drawable.signout_icon);

            }
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.commit();
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == 1) {

            if (currentUser.role.toLowerCase().equals("dealer")) {
                transaction.replace(R.id.area, new HomeFragment());
                //fragment = new HomeFragment();
            }else {
                transaction.replace(R.id.area, new EmployeeDashboard());
            }

        }
        else if (id == 2) {
            if (currentUser.role.toLowerCase().equals("dealer")){

                transaction.replace(R.id.area, new DORequestFragment());
                //fragment = new DORequestFragment();

            }else{
                transaction.replace(R.id.area, new EmployeeDOFragment());

            }
        }
        else if (id == 3) {
            if (currentUser.role.toLowerCase().equals("dealer")){
                transaction.replace(R.id.area, new OrderFragment());
                //fragment = new OrderFragment();


            }else {
                transaction.replace(R.id.area, new EmployeeOrderInfoFragment());
            }

        }
        else if (id == 4) {
            if (currentUser.role.toLowerCase().equals("dealer")){
                transaction.replace(R.id.area, new DealerChallanFragment());
                //fragment = new OrderFragment();


            }else {
                transaction.replace(R.id.area, new EmployeeChallanFragment());
            }

        }
        else if (id == 5) {
            //fragment = new DeliveryInfoFragment();
            transaction.replace(R.id.area, new DeliveryInfoFragment());


        } else if (id == 6) {
            if (currentUser.role.equals("Dealer") || currentUser.role.equals("dealer")){
                //fragment = new FinancialFragment();
                transaction.replace(R.id.area, new FinancialFragment());
            }else {
                //fragment = new EmployeeReportFragment();
                transaction.replace(R.id.area, new EmployeeReportFragment());

            }

        } else if (id == 7) {
            //fragment = new CommissionIncentiveFragment();
            transaction.replace(R.id.area, new CommissionIncentiveFragment());

        } else if (id == 8) {
            //fragment = new TradeFragment();
            transaction.replace(R.id.area, new TradeFragment());


        } else if (id == 9){
            //fragment = new CommunicationFragment();
            transaction.replace(R.id.area, new CommunicationFragment());


        } else if (id == 10){
            if (currentUser.role.equals("Dealer") || currentUser.role.equals("dealer")){
                //fragment = new ReportFragment();
                transaction.replace(R.id.area, new ReportFragment());

            }else {
                DataManager.removeCurrentUser(this);
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                MainActivity.this.startActivity(intent);
            }


        } else if (id == 11){
            //fragment = new AboutUsFragment();
            transaction.replace(R.id.area, new AboutUsFragment());


        } else if (id == 12){
            //fragment = new ContactUsFragment();
            transaction.replace(R.id.area, new ContactUsFragment());
        } else if (id == 13){

            DataManager.removeCurrentUser(this);
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            MainActivity.this.startActivity(intent);
        }

        if (fragment != null){
            FragmentManager fragmentManager2 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager2.beginTransaction();
            fragmentTransaction.replace(R.id.area, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
