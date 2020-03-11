package com.example.homie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.homie.Fragments.AdvertiseItem;
import com.example.homie.Fragments.MainPage;
import com.example.homie.Fragments.StartAdvertise;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.ID;


public class Homepage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    SharedPreferences shared;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String Json_Url = "https://test350999.000webhostapp.com/viewitems.php";
    myitemAdapter adapter;
    JSONObject json_object;
    String category;
    JSONArray jsonArray;
    ArrayList<myitemModel> Listt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        recyclerView = findViewById(R.id.recyclerview);
        Listt = new ArrayList<>();
        setUpToolbar();
        navigationViewOptionSelected();
    }

    @Override
    protected void onStart() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            AlertDialog.Builder builder1;
            builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Check your internet connection");
            builder1.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }
            );
            AlertDialog alert1 = builder1.create();
            alert1.show();


        } else {
            getList();
        }
        super.onStart();
    }

    private void getList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Json_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Listt.clear();
                try {
                    json_object = new JSONObject(response);
                    jsonArray = json_object.getJSONArray("server_response");
                    int count = 0;

                    while (count < jsonArray.length()) {
                        JSONObject jo = jsonArray.getJSONObject(count);
                        myitemModel myitemmodel = new myitemModel();
                        myitemmodel.setTitle(jo.getString("title"));
                        myitemmodel.setCategory(jo.getString("category"));
                        myitemmodel.setDatee(jo.getString("datee"));
                        myitemmodel.setDays(jo.getString("days"));
                        myitemmodel.setDesc_(jo.getString("desc_"));
                        myitemmodel.setImage(jo.getString("imageurl"));
                        myitemmodel.setPrice(jo.getString("price"));
                        myitemmodel.setNumber(jo.getString("number"));
                        myitemmodel.setVisibility(jo.getString("visibility"));
                        Listt.add(myitemmodel);
                        count++;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new myitemAdapter(Homepage.this, Listt);
                recyclerView.setAdapter(adapter);
                layoutManager = new GridLayoutManager(Homepage.this, 2);
                recyclerView.setLayoutManager(layoutManager);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap();
                param.put("checkcategory", "");
                return param;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void navigationViewOptionSelected() {
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Mainpage:
                        getList();
                        recyclerView.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MainPage()).commit();
                        break;
                    case R.id.Advertise:
                        recyclerView.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new StartAdvertise()).commit();
                        break;
                    case R.id.Order:
                        Intent intent = new Intent(Homepage.this, ViewOrder.class);
                        startActivity(intent);
                        break;
                    case R.id.MyAdvertiseitem:
                        getmyitem();
                        recyclerView.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MainPage()).commit();
                        break;
                    case R.id.Signout:
                        shared = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                        AlertDialog.Builder alert = new AlertDialog.Builder(Homepage.this);
                        alert.setTitle(shared.getString("number", "") + " Are you sure to sign out?");
                        alert.setIcon(R.drawable.ic_error_outline_black_24dp);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String numberSubs = shared.getString("number", "").replaceAll("[\\-\\+\\.\\^:,]", "");
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(numberSubs);
                                FirebaseAuth.getInstance().signOut();
                                Intent intent2 = new Intent(Homepage.this, RegisterationActivity.class);
                                startActivity(intent2);
                                finish();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alert.show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void getmyitem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://test350999.000webhostapp.com/viewmyitems.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Listt.clear();
                try {
                    json_object = new JSONObject(response);
                    jsonArray = json_object.getJSONArray("server_response");
                    int count = 0;

                    while (count < jsonArray.length()) {
                        JSONObject jo = jsonArray.getJSONObject(count);
                        myitemModel myitemmodel = new myitemModel();
                        myitemmodel.setTitle(jo.getString("title"));
                        myitemmodel.setCategory(jo.getString("category"));
                        myitemmodel.setDatee(jo.getString("datee"));
                        myitemmodel.setDays(jo.getString("days"));
                        myitemmodel.setDesc_(jo.getString("desc_"));
                        myitemmodel.setImage(jo.getString("imageurl"));
                        myitemmodel.setPrice(jo.getString("price"));
                        myitemmodel.setNumber(jo.getString("number"));
                        myitemmodel.setVisibility(jo.getString("visibility"));
                        Listt.add(myitemmodel);
                        count++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new myitemAdapter(Homepage.this, Listt);
                recyclerView.setAdapter(adapter);
                layoutManager = new GridLayoutManager(Homepage.this, 2);
                recyclerView.setLayoutManager(layoutManager);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap();
                shared = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                param.put("mynumber", shared.getString("number", ""));
                return param;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter, menu);
        MenuItem filteritem = menu.findItem(R.id.actionfilter);
        Spinner spinner = (Spinner) filteritem.getActionView();
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array._array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("Other")) {
                    category = "";
                } else {
                    category = adapterView.getItemAtPosition(i).toString();
                }

                Toast.makeText(getApplicationContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Json_Url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Listt.clear();
                        try {
                            json_object = new JSONObject(response);
                            jsonArray = json_object.getJSONArray("server_response");
                            int count = 0;

                            while (count < jsonArray.length()) {
                                JSONObject jo = jsonArray.getJSONObject(count);
                                myitemModel myitemmodel = new myitemModel();
                                myitemmodel.setTitle(jo.getString("title"));
                                myitemmodel.setCategory(jo.getString("category"));
                                myitemmodel.setDatee(jo.getString("datee"));
                                myitemmodel.setDays(jo.getString("days"));
                                myitemmodel.setDesc_(jo.getString("desc_"));
                                myitemmodel.setImage(jo.getString("imageurl"));
                                myitemmodel.setPrice(jo.getString("price"));
                                myitemmodel.setNumber(jo.getString("number"));
                                myitemmodel.setVisibility(jo.getString("visibility"));
                                Listt.add(myitemmodel);
                                count++;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter = new myitemAdapter(Homepage.this, Listt);
                        recyclerView.setAdapter(adapter);
                        layoutManager = new GridLayoutManager(Homepage.this, 2);
                        recyclerView.setLayoutManager(layoutManager);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> param = new HashMap();
                        param.put("checkcategory", category);
                        return param;
                    }
                };
                Volley.newRequestQueue(Homepage.this).add(stringRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });// spinner End

        return true;
    }
}
