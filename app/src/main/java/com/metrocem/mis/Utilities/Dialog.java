package com.metrocem.mis.Utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.metrocem.mis.R;

public class Dialog {

    public static void showDORequestSuccessfulDialog(String doNumber,String productName, String qty, String time, Context context) {



        final View dialogView = View.inflate(context, R.layout.do_successful_dialog,null);

        final android.app.Dialog dialog = new android.app.Dialog(context,R.style.MyAlertDialogStyle);
        //dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        dialog.setCancelable(true);
        dialog.setTitle("");
        dialog.setContentView(dialogView);

        TextView doNumberTV = dialog.findViewById(R.id.do_number_TV);
        String doText = "Requested DO Number is "+ doNumber;
        doNumberTV.setText(doText);

        TextView productNameTV = dialog.findViewById(R.id.product_name_TV);
        productNameTV.setText(productName);

        TextView timeTV = dialog.findViewById(R.id.allocated_time_TV);
        timeTV.setText(time);

        TextView qtyTV = dialog.findViewById(R.id.bag_qty_TV);
        qtyTV.setText(qty);

        TextView okBtn = dialog.findViewById(R.id.closeDialogImg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //onBack();

                dialog.dismiss();


            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //revealShow(dialogView, true, null);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){

                    //revealShow(dialogView, false, dialog);
                    return true;
                }

                return false;
            }
        });



        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }

}
