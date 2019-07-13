package com.metrocem.mismetrocem.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.metrocem.mismetrocem.R;

import java.util.Locale;

public class CommissionIncentiveFragment extends Fragment {

    TextView commisionValue, incentiveValue;
    Spinner dropdown;
    Integer dropdownValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commission_incentive,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.commission_incentive);

        commisionValue = view.findViewById(R.id.commisionValue);
        incentiveValue = view.findViewById(R.id.incentiveValue);

        dropdown = view.findViewById(R.id.spinner1);
        final String[] items = new String[]{"5", "7", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        EditText commisionCalculatorET = view.findViewById(R.id.commisionCalculatorET);
        commisionCalculatorET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editTValue) {
                //SaleItemActivity.this.saleAdapter.filterItem(s);
                //saleItemListView.invalidateViews();

                commisionCalculator(editTValue);
            }
        });

        final EditText incentiveCalculatorET = view.findViewById(R.id.incentiveCalculatorET);
        incentiveCalculatorET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editTValue) {
                //SaleItemActivity.this.saleAdapter.filterItem(s);
                //saleItemListView.invalidateViews();

                incentiveCalculator(editTValue);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                Log.d("value", items[position]);
                dropdownValue = Integer.valueOf(items[position]);
                Integer editTextValue = Integer.valueOf(incentiveCalculatorET.getText().toString());
                if (incentiveCalculatorET.getText().length() > 0){
                    Integer value = dropdownValue*editTextValue;
                    incentiveValue.setText(value.toString());
                }else {
                    Toast.makeText(getActivity(),"Empty Incentive field", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void commisionCalculator(Editable editTValue){

        String value = editTValue.toString();
        if (value.length() > 0){
            if (Integer.parseInt(value) >= 20000){

                Integer commvalue = (Integer.parseInt(String.valueOf(value))*31);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 15000 && Integer.parseInt(String.valueOf(value)) < 20000){
                Integer commvalue = (Integer.parseInt(value)*29);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 10000 && Integer.parseInt(String.valueOf(value)) < 15000){
                Integer commvalue = (Integer.parseInt(value)*26);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 5000 && Integer.parseInt(String.valueOf(value)) < 10000){
                Integer commvalue = (Integer.parseInt(value)*23);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 100 && Integer.parseInt(String.valueOf(value)) < 5000){
                Integer commvalue = (Integer.parseInt(value)*19);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) < 100){
                Integer commvalue = (Integer.parseInt(value)*0);
                commisionValue.setText(commvalue.toString());
            }
        }

    }

    public void incentiveCalculator(Editable editTValue){

        String value = editTValue.toString();

        if (value.length() > 0) {
            Integer incentive = Integer.parseInt(value)*dropdownValue;
            incentiveValue.setText(incentive.toString());
        }else {
            incentiveValue.setText("0");
        }


    }

}
