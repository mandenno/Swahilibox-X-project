package com.swahilibox.mobile.vision.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Final extends AppCompatActivity implements View.OnClickListener {
Button submit;
TextView qr_details;
    public static final String URL_SUBMIT = "http://www.fastagas.com/swahilibox/validate_qr.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_final);
        submit=(Button) findViewById(R.id.submit);
        qr_details=(TextView)findViewById(R.id.qr_details);
        qr_details.setText(Final.this.getIntent().getExtras().getString("qr_code"));
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==submit)
        {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirm!")
                    .setContentText("Submit these details for verification?")
                    .setCancelText("CANCEL")
                    .setConfirmText("SUBMIT")
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
                          verifyPermit();
                            sDialog.cancel();
                        }
                    })
                    .show();
        }
    }

    public void verifyPermit()
    {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2a628e"));
        pDialog.setTitleText("Verifying Permit...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        if(response.contains("right"))
                        {

                            new SweetAlertDialog(Final.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Permit Verified!")
                                    .setContentText("This business permit is valid!")
                                    .show();
                        }

                        else if(response.contains("wrong"))
                        {

                            new SweetAlertDialog(Final.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Invalid Permit")
                                    .setContentText("The Business permit details are invalid!")
                                    .show();
                            if (response != null) {
                                try {
                                    JSONArray jsonarray = new JSONArray(response);


                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject obj = jsonarray.getJSONObject(i);
                                        String case_no = obj.getString("case_no");
                                        String court_date = obj.getString("court_date");
                                        Intent intent = new Intent(Final.this, Business_Details.class);
                                        intent.putExtra("case_no", case_no);
                                        intent.putExtra("court_date", court_date);
                                        startActivity(intent);
                                        finish();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {

                            new SweetAlertDialog(Final.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("We have encountered a technical error! Please try again later.")
                                    .show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        new SweetAlertDialog(Final.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops!")
                                .setContentText("Check your network connection and try again!")
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_phone", "0721107552");
                params.put("lat", Final.this.getIntent().getExtras().getString("lat"));
                params.put("lng", Final.this.getIntent().getExtras().getString("lng"));
                params.put("qr_code", Final.this.getIntent().getExtras().getString("qr_code"));
                params.put("bus_no", Final.this.getIntent().getExtras().getString("bus_no"));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Final.this);
        requestQueue.add(stringRequest);
    }
}
