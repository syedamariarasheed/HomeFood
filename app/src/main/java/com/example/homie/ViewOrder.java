package com.example.homie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String Json_Url = "https://test350999.000webhostapp.com/vieworder.php";
    OrderAdapter adapter;
    JSONObject json_object;
    JSONArray jsonArray;
    ArrayList<Ordermodel> Listt;
    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        recyclerView=findViewById(R.id.recyclerview);
        Listt=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getList();
    }

    private void getList() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Json_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Listt.clear();
                try {
                    json_object=new JSONObject(response);
                    jsonArray =json_object.getJSONArray("server_response");
                    int count=0;

                    while (count<jsonArray.length()){
                        JSONObject jo=jsonArray.getJSONObject(count);
                        Ordermodel ordermodel=new Ordermodel();

                        ordermodel.setTitle(jo.getString("title"));
                        ordermodel.setQuan(jo.getString("Quan"));
                        ordermodel.setCategory(jo.getString("category"));
                        ordermodel.setDatee(jo.getString("datee"));
                        ordermodel.setDays(jo.getString("days"));
                        ordermodel.setDesc_(jo.getString("desc_"));
                        ordermodel.setImage(jo.getString("imageurl"));
                        ordermodel.setPrice(jo.getString("price"));
                        ordermodel.setOrderreqnumber(jo.getString("orderreqnumber"));
                        ordermodel.setLoc(jo.getString("Loc"));

                        Listt.add(ordermodel);
                        count++;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter=new OrderAdapter(ViewOrder.this, Listt);
                recyclerView.setAdapter(adapter);
                layoutManager=new GridLayoutManager(ViewOrder.this,1);
                recyclerView.setLayoutManager(layoutManager);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap();
                shared = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                param.put("checksellernumber", shared.getString("number", ""));
                return param;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

}
