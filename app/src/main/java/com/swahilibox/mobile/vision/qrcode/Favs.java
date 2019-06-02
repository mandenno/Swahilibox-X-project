package com.swahilibox.mobile.vision.qrcode;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Favs extends AppCompatActivity implements ContactsAdapterFavs.ContactsAdapterListener, View.OnClickListener {
    // private static final String TAG = Favs.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Contactfavs> contactList;
    private ContactsAdapterFavs mAdapter;
    private SearchView searchView;
    Context mcontext;
    LoginDataBaseAdapter loginDataBaseAdapter;
    ProgressDialog progress;
    Utils utils;
FloatingActionButton fab;
    private static final String URLTWO = "http://fastagas.com/swahilibox/my_account.php?phone=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);
        Toolbar toolbar = findViewById(R.id.toolbarfriend);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        fab=(FloatingActionButton)findViewById(R.id.verify_fab);
        fab.setOnClickListener(this);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Mombasa County");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_reorder_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        fetchContactsf();
        mcontext = getApplicationContext();
        utils = new Utils(mcontext);
        recyclerView = findViewById(R.id.recycler_view_friends);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapterFavs(this, contactList, this);

        // white background notification bar
        //whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

    }


    private void fetchContactsf() {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(URLTWO +"0721107552",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                            linlaHeaderProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Contactfavs> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contactfavs>>() {
                        }.getType());
                        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                        linlaHeaderProgress.setVisibility(View.GONE);
                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                //  Log.e(TAG, "Error: " + error.getMessage());
                LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                linlaHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favs, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
   if (id == R.id.action_refresh) {
       new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
               .setTitleText("Confirm!")
               .setContentText("Proceed to business permit verification?")
               .setCancelText("NO")
               .setConfirmText("YES")
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
                       Intent intent = new Intent(Favs.this, Bus_number.class);
                       startActivity(intent);
                       sDialog.cancel();
                   }
               })
               .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onContactSelected(Contactfavs contact) {
        if(contact.getId().equals(""))
        {
            fetchContactsf();
        }

    }


    @Override
    public void onClick(View view) {
        if(view==fab)
        {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirm!")
                    .setContentText("Proceed to business permit verification?")
                    .setCancelText("NO")
                    .setConfirmText("YES")
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
                            Intent intent = new Intent(Favs.this, Bus_number.class);
                            startActivity(intent);
                            sDialog.cancel();
                        }
                    })
                    .show();

        }
    }
}



