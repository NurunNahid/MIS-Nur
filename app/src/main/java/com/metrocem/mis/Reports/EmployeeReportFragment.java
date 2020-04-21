package com.metrocem.mis.Reports;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.metrocem.mis.R;

public class EmployeeReportFragment extends Fragment {

    private CheckBox checkBox, checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12, checkBox13, checkBox14, checkBox15, checkBox16, checkBox17;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee_order, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.report);

        checkBox = view.findViewById(R.id.all_item_check);
        checkBox1 = view.findViewById(R.id.checkbox1);
        checkBox2 = view.findViewById(R.id.checkbox2);
        checkBox3 = view.findViewById(R.id.checkbox3);
        checkBox4 = view.findViewById(R.id.checkbox4);
        checkBox5 = view.findViewById(R.id.checkbox5);
        checkBox6 = view.findViewById(R.id.checkbox6);
        checkBox7 = view.findViewById(R.id.checkbox7);
        checkBox8 = view.findViewById(R.id.checkbox8);
        checkBox9 = view.findViewById(R.id.checkbox9);
        checkBox10 = view.findViewById(R.id.checkbox10);
        checkBox11 = view.findViewById(R.id.checkbox11);
        checkBox12 = view.findViewById(R.id.checkbox12);
        checkBox13 = view.findViewById(R.id.checkbox13);
        checkBox14 = view.findViewById(R.id.checkbox14);
        checkBox15 = view.findViewById(R.id.checkbox15);
        checkBox16 = view.findViewById(R.id.checkbox16);
        checkBox17 = view.findViewById(R.id.checkbox17);

        //checkBox.setChecked(!checkBox.isChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if(cb.isChecked()){

                    Log.d("n","Press");
                    checkBox1.setChecked(true);
                    checkBox2.setChecked(true);
                    checkBox3.setChecked(true);
                    checkBox4.setChecked(true);
                    checkBox5.setChecked(true);
                    checkBox6.setChecked(true);
                    checkBox7.setChecked(true);
                    checkBox8.setChecked(true);
                    checkBox9.setChecked(true);
                    checkBox10.setChecked(true);
                    checkBox11.setChecked(true);
                    checkBox12.setChecked(true);
                    checkBox13.setChecked(true);
                    checkBox14.setChecked(true);
                    checkBox15.setChecked(true);
                    checkBox16.setChecked(true);
                    checkBox17.setChecked(true);


                }else{

                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                    checkBox5.setChecked(false);
                    checkBox6.setChecked(false);
                    checkBox7.setChecked(false);
                    checkBox8.setChecked(false);
                    checkBox9.setChecked(false);
                    checkBox10.setChecked(false);
                    checkBox11.setChecked(false);
                    checkBox12.setChecked(false);
                    checkBox13.setChecked(false);
                    checkBox14.setChecked(false);
                    checkBox15.setChecked(false);
                    checkBox16.setChecked(false);
                    checkBox17.setChecked(false);

                }
            }
        });

        checkBox15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if(cb.isChecked()){
                    checkBox16.setChecked(true);
                    checkBox17.setChecked(true);
                }else {
                    checkBox16.setChecked(false);
                    checkBox17.setChecked(false);
                }
            }
        });

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.all_item_check:
                if (checked){
                    Log.d("Check","Press");
                }
                else {

                }
                // Do your coding
                // Do your coding

                break;
            // Perform your logic
        }
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        //CheckBox checkBox = (CheckBox)v;

    }

}
