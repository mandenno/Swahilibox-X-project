package com.swahilibox.mobile.vision.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class Bus_number extends AppCompatActivity implements View.OnClickListener {
Button next;
EditText bno;
String lati="";
String longi="";
    GpsTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_bus_number);
        bno=(EditText)findViewById(R.id.bus_no);
        next=(Button)findViewById(R.id.next_btn);
        next.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Bus_number.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            //  Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GpsTracker(this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                lati = String.valueOf(gps.getLatitude());
                longi =  String.valueOf(gps.getLongitude());

                // \n is for new line
             //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lati + "\nLong: " + longi, Toast.LENGTH_LONG).show();
            }
            else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view==next)
        {
            if(bno.getText().toString().equals(""))
            {
                Toasty.error(this,"Enter the Business no!", Toast.LENGTH_SHORT).show();
            }
            else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("SCAN QR CODE")
                        .setContentText("SCAN the Business Permit QR Code")
                        .setCancelText("CANCEL")
                        .setConfirmText("SCAN QR")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(Bus_number.this, NewScanner.class);
                                intent.putExtra("bus_no", bno.getText().toString());
                                intent.putExtra("lat", lati+"");
                                intent.putExtra("lng", longi+"");
                                startActivity(intent);
                                sDialog.cancel();
                            }
                        })
                        .show();


            }
        }
    }
}
