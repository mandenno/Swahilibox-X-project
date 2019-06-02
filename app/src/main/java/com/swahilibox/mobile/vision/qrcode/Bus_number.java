package com.swahilibox.mobile.vision.qrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class Bus_number extends AppCompatActivity implements View.OnClickListener {
Button next;
EditText bno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_bus_number);
        bno=(EditText)findViewById(R.id.bus_no);
        next=(Button)findViewById(R.id.next_btn);
        next.setOnClickListener(this);
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
                Intent intent = new Intent(this, Camera.class);
                intent.putExtra("bus_no", bno.getText().toString());
                startActivity(intent);

            }
        }
    }
}
