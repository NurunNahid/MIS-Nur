package com.metrocem.mis.Utilities;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressHud {

    public static KProgressHUD setHud(Context context){
        KProgressHUD hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        return hud;
    }


}
