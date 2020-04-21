package com.metrocem.mis.Subclasses;

public class CalculateCommission {

    public static Integer calculateCommissionAndIncentive(String value){

        Integer commvalue = null;
        if (Integer.parseInt(value) >= 20000){

            commvalue = (Integer.parseInt(String.valueOf(value))*31);
        }
        else if (Integer.parseInt(value) >= 15000 && Integer.parseInt(String.valueOf(value)) < 20000){
            commvalue = (Integer.parseInt(value)*29);
        }
        else if (Integer.parseInt(value) >= 10000 && Integer.parseInt(String.valueOf(value)) < 15000){
            commvalue = (Integer.parseInt(value)*26);
        }
        else if (Integer.parseInt(value) >= 5000 && Integer.parseInt(String.valueOf(value)) < 10000){
            commvalue = (Integer.parseInt(value)*23);
        }
        else if (Integer.parseInt(value) >= 100 && Integer.parseInt(String.valueOf(value)) < 5000){
            commvalue = (Integer.parseInt(value)*19);
        }
        else if (Integer.parseInt(value) < 100){
            commvalue = 0;
        }

        return commvalue;
    }
}
