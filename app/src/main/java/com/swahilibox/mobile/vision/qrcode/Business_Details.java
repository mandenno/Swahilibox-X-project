package com.swahilibox.mobile.vision.qrcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Business_Details extends AppCompatActivity {
EditText case_no, courtdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__details);
        case_no=(EditText)findViewById(R.id.case_no);
        courtdate=(EditText)findViewById(R.id.court_date);
        String caseno=Business_Details.this.getIntent().getExtras().getString("case_no");
        String court_date=Business_Details.this.getIntent().getExtras().getString("court_date");
        case_no.setText(caseno);
        courtdate.setText(court_date);
    }
}
