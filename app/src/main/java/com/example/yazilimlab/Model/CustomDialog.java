package com.example.yazilimlab.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.yazilimlab.R;

public class CustomDialog {

    Dialog dialog;
    Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }

    public void startLoadingDialog() {
        //https://www.youtube.com/watch?v=oVJeffNvOQI
        dialog = new Dialog(context);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.screenBrightness=WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        dialog.setContentView(R.layout.custom_loader);
        dialog.setCancelable(false);

        dialog.show();
        //https://www.youtube.com/watch?v=oVJeffNvOQI
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
