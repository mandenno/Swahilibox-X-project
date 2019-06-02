package com.swahilibox.mobile.vision.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class Test extends AppCompatActivity {
    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String URL_SUBMIT = "http://www.fastagas.com/swahilibox/validate_qr.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        final String bno=Test.this.getIntent().getExtras().getString("bus_no");
        final String lat=Test.this.getIntent().getExtras().getString("lat");
        final String lng=Test.this.getIntent().getExtras().getString("lng");
        final String code=Test.this.getIntent().getExtras().getString("code");
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("CODE SCANNED!")
                .setContentText("SCAN the Business Permit QR Code")
                .setCancelText("CANCEL")
                .setConfirmText("VERIFY CODE")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finish();
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                      verifyPermit(code, bno,lat,lng);
                        sDialog.cancel();
                    }
                })
                .show();
    }


    public void verifyPermit(final String bcode, final String bno, final String lat, final String lng)
    {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFC107"));
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

                            new SweetAlertDialog(Test.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Permit Verified!")
                                    .setContentText("This business permit is valid!")
                                    .show();
                            Intent intent = new Intent(Test.this, Favs.class);
                            startActivity(intent);
                        }

                        else if(response.contains("wrong"))
                        {

                            new SweetAlertDialog(Test.this, SweetAlertDialog.ERROR_TYPE)
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
                                        Intent intent = new Intent(Test.this, Business_Details.class);
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

                            new SweetAlertDialog(Test.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("We have encountered a technical error! Please try again later.")
                                    .show();
                            finish();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        new SweetAlertDialog(Test.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops!")
                                .setContentText("Check your network connection and try again!")
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        finish();
                                    }
                                })
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_phone", loginDataBaseAdapter.getSinlgeEntry1());
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("qr_code", bcode);
                params.put("bus_no", bno);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Test.this);
        requestQueue.add(stringRequest);
    }
}
