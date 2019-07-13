package com.metrocem.mismetrocem.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.widget.TextView;

import com.metrocem.mismetrocem.FinancialFragment;
import com.metrocem.mismetrocem.Fragment.AboutUsFragment;
import com.metrocem.mismetrocem.Fragment.CommissionIncentiveFragment;
import com.metrocem.mismetrocem.Fragment.CommunicationFragment;
import com.metrocem.mismetrocem.Fragment.ContactUsFragment;
import com.metrocem.mismetrocem.Fragment.DeliveryFragment;
import com.metrocem.mismetrocem.Fragment.EmployeeDashboard;
import com.metrocem.mismetrocem.Fragment.EmployeeOrderInfoFragment;
import com.metrocem.mismetrocem.Fragment.EmployeeReportFragment;
import com.metrocem.mismetrocem.Fragment.HomeFragment;
import com.metrocem.mismetrocem.Fragment.ReportFragment;
import com.metrocem.mismetrocem.Fragment.RequestFragment;
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
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        headerView = navigationView.getHeaderView(0);
        menuTitle = headerView.findViewById(R.id.menuTitle);
        mobileNoTV = headerView.findViewById(R.id.mobileNo);

        //DataManager.removeCurrentUser(this);

        menu = navigationView.getMenu();




    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        //currentUser = new CurrentUser();
        currentUser = DataManager.getCurrentUser(this);


        try {

            if (currentUser.role != null){

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                Log.d("Role", currentUser.role);
                try {
                    menuTitle.setText(currentUser.userName);
                    mobileNoTV.setText("01700123123");
                }catch (Exception e){
                    Log.d("error", e.getMessage());
                }

                if (currentUser.role.equals("Dealer")){
                    toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                    //navigationView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGreen));
                    headerView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGreen));

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                    if (sharedPreferences.getBoolean("isFlag", true)) {
                        transaction.replace(R.id.area, new HomeFragment());
                        transaction.commit();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isFlag", false);
                        editor.commit();
                    }




                    //SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                }else {
                    toolbar.setBackgroundColor(Color.parseColor("#FA8045"));
                    //navigationView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorOrange));
                    headerView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorOrange));

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                    if (sharedPreferences.getBoolean("isFlag", true)) {
                        transaction.replace(R.id.area, new EmployeeDashboard());
                        transaction.commit();

                        //SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isFlag", false);
                        editor.commit();
                    }


                }

                getMenuOptions();

            }else {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                MainActivity.this.startActivity(intent);
            }
        }catch (Exception e){
            Log.d("error", e.getMessage());
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            MainActivity.this.startActivity(intent);
        }




    }

    public void getMenuOptions(){

        menu.clear();

        try {
            if (currentUser.role.equals("Dealer")){

                menu.add(R.id.mainGroup, 1, 100, R.string.menu_home);
                menu.add(R.id.mainGroup, 2, 200, R.string.do_request);
                menu.add(R.id.mainGroup, 3, 300, R.string.order_info);
                menu.add(R.id.mainGroup, 4, 400, R.string.delivery_info);
                menu.add(R.id.mainGroup, 5, 500, R.string.financial_info);
                menu.add(R.id.mainGroup, 6, 600, R.string.commission_incentive);
                menu.add(R.id.mainGroup, 7, 700, R.string.trade_brand);
                menu.add(R.id.mainGroup, 8, 800, R.string.communication);
                menu.add(R.id.mainGroup, 9, 900, R.string.report);
                menu.add(R.id.mainGroup, 10, 1000, R.string.about_us);
                menu.add(R.id.mainGroup, 11, 1100, R.string.contact_us);
                menu.add(R.id.mainGroup, 12, 1200, "Sign out");


                menu.getItem(0).setIcon(R.drawable.left_arrow);
                menu.getItem(1).setIcon(R.drawable.left_arrow);
                menu.getItem(2).setIcon(R.drawable.left_arrow);
                menu.getItem(3).setIcon(R.drawable.left_arrow);
                menu.getItem(4).setIcon(R.drawable.left_arrow);
                menu.getItem(5).setIcon(R.drawable.left_arrow);
                menu.getItem(6).setIcon(R.drawable.left_arrow);
                menu.getItem(7).setIcon(R.drawable.left_arrow);
                menu.getItem(8).setIcon(R.drawable.left_arrow);
                menu.getItem(9).setIcon(R.drawable.left_arrow);
                menu.getItem(10).setIcon(R.drawable.left_arrow);
                menu.getItem(11).setIcon(R.drawable.left_arrow);

                //menu.getItem(10).setIcon(R.mipmap.stock_icon_new);
                //menu.getItem(11).setIcon(R.mipmap.expense_icon_new);
                //menu.getItem(12).setIcon(R.drawable.help);
                //menu.getItem(13).setIcon(R.mipmap.logout_icon_new);


                //menu.setGroupCheckable(R.id.firstGroup, false, false);
                //menu.setGroupVisible(R.id.firstGroup, false);

                //menu.setGroupCheckable(R.id.mainGroup, true, true);
                //menu.setGroupVisible(R.id.mainGroup, true);
            }else {

                menu.add(R.id.mainGroup, 1, 100, R.string.menu_home);
                menu.add(R.id.mainGroup, 2, 200, R.string.do_request);
                menu.add(R.id.mainGroup, 3, 300, R.string.order_info);
                menu.add(R.id.mainGroup, 4, 400, R.string.delivery_info);
                menu.add(R.id.mainGroup, 5, 500, R.string.report);
                menu.add(R.id.mainGroup, 6, 600, R.string.commission_incentive);
                menu.add(R.id.mainGroup, 7, 700, R.string.trade_brand);
                menu.add(R.id.mainGroup, 8, 800, R.string.communication);
                menu.add(R.id.mainGroup, 9, 900, "Sign out");

                //menu.setGroupCheckable(R.id.firstGroup, true, true);
                //menu.setGroupVisible(R.id.firstGroup, true);

                //menu.setGroupCheckable(R.id.mainGroup, false, false);
                //menu.setGroupVisible(R.id.mainGroup, false);

                menu.getItem(0).setIcon(R.drawable.left_arrow);
                menu.getItem(1).setIcon(R.drawable.left_arrow);
                menu.getItem(2).setIcon(R.drawable.left_arrow);
                menu.getItem(3).setIcon(R.drawable.left_arrow);
                menu.getItem(4).setIcon(R.drawable.left_arrow);
                menu.getItem(5).setIcon(R.drawable.left_arrow);
                menu.getItem(6).setIcon(R.drawable.left_arrow);
                menu.getItem(7).setIcon(R.drawable.left_arrow);
                menu.getItem(8).setIcon(R.drawable.left_arrow);

                //menu.getItem(7).setIcon(R.mipmap.stock_icon_new);
                //menu.getItem(8).setIcon(R.mipmap.expense_icon_new);
                //menu.getItem(9).setIcon(R.drawable.help);
                //menu.getItem(10).setIcon(R.mipmap.logout_icon_new);
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

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.area, new HomeFragment());
        transaction.commit();
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == 1) {
            // Handle the camera action
            //Intent i = new Intent(MainActivity.this,SecondActivity.class);
            //startActivity(i);
            //fragment = new HomeFragment();
            if (currentUser.role.equals("Dealer")) {
                transaction.replace(R.id.area, new HomeFragment());
            }else {
                transaction.replace(R.id.area, new EmployeeDashboard());
            }

        }
        else if (id == 2) {
            // Handle the camera action
            //fragment = new RequestFragment();
            transaction.replace(R.id.area, new RequestFragment());

        }
        else if (id == 3) {
            if (currentUser.role.equals("Dealer")){
                //fragment = new OrderFragment();
                transaction.replace(R.id.area, new OrderFragment());

            }else {
                //fragment = new EmployeeOrderInfoFragment();
                transaction.replace(R.id.area, new EmployeeOrderInfoFragment());

            }

        } else if (id == 4) {
            //fragment = new DeliveryFragment();
            transaction.replace(R.id.area, new DeliveryFragment());


        } else if (id == 5) {
            if (currentUser.role.equals("Dealer")){
                //fragment = new FinancialFragment();
                transaction.replace(R.id.area, new FinancialFragment());
            }else {
                //fragment = new EmployeeReportFragment();
                transaction.replace(R.id.area, new EmployeeReportFragment());

            }

        } else if (id == 6) {
            //fragment = new CommissionIncentiveFragment();
            transaction.replace(R.id.area, new CommissionIncentiveFragment());

        } else if (id == 7) {
            //fragment = new TradeFragment();
            transaction.replace(R.id.area, new TradeFragment());


        } else if (id == 8){
            //fragment = new CommunicationFragment();
            transaction.replace(R.id.area, new CommunicationFragment());


        } else if (id == 9){
            if (currentUser.role.equals("Dealer")){
                //fragment = new ReportFragment();
                transaction.replace(R.id.area, new ReportFragment());

            }else {
                DataManager.removeCurrentUser(this);
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                MainActivity.this.startActivity(intent);
            }


        } else if (id == 10){
            //fragment = new AboutUsFragment();
            transaction.replace(R.id.area, new AboutUsFragment());


        } else if (id == 11){
            //fragment = new ContactUsFragment();
            transaction.replace(R.id.area, new ContactUsFragment());
        }else if (id == 12){

            DataManager.removeCurrentUser(this);
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            MainActivity.this.startActivity(intent);
        }

//        if (fragment != null){
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.area, fragment);
//            fragmentTransaction.commit();
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
