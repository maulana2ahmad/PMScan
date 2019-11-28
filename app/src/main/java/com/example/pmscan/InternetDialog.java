package com.example.pmscan;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.util.Objects;

/**
 * Created by kapil on 03/11/18.
 */

class InternetDialog {
    private Context context;

    public InternetDialog(int errorCode){

    }
    public InternetDialog(MainActivity context){
        this.context = context;
    }

    private void showNoInternetDialog(){
        final Dialog dialog1 = new Dialog(context, R.style.df_dialog);
        dialog1.setContentView(R.layout.dialog_no_internet);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.findViewById(R.id.btnSpinAndWinRedeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    public  boolean getInternetStatus() {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //noinspection deprecation
        @SuppressWarnings("deprecation") NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            //show no internet dialog
            showNoInternetDialog();
        }
        return isConnected;
    }


}
