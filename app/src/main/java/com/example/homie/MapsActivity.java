package com.example.homie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{
    private int flag=0;
    Button sendorder;
    SharedPreferences shared;
    ProgressBar progressBar;
    String latLang;
    String url="https://test350999.000webhostapp.com/HomieOrder.php";
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationManager mlocationManager;
    private LocationRequest locationRequest;
    private  com.google.android.gms.location.LocationListener listener;
    private long Update_Intervel = 2000;
    private long fastest_Intervel = 5000;
    private LatLng latLng;
    private boolean isPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        sendorder=findViewById(R.id.Sendorder);
        progressBar=findViewById(R.id.progressbar2);
        sendorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert=new AlertDialog.Builder(MapsActivity.this);
                alert.setTitle("Confirm details and place order");
                alert.setIcon(R.drawable.ic_shopping_cart_black_24dp);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      progressBar.setVisibility(View.VISIBLE);
                        sendOrder();
                        sendNotification();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                });

                alert.show();
            }
        });
        if (requestSinglePermission()) {


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            checkLocation();
    }}

    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            String number=getIntent().getStringExtra("Number").replaceAll("[\\-\\+\\.\\^:,]","");
            json.put("to","/topics/"+number);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Order");
            notificationObj.put("body","You recieve order");
            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");
            json.put("notification",notificationObj);
            json.put("data",extraData);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send",
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyCVDIBb-nLTRe409wBP64paEaezI4xjkrs");
                    return header;
                }
            }; Volley.newRequestQueue(MapsActivity.this).add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }

    private void sendOrder() {
        final Intent intent=getIntent();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MapsActivity.this,Homepage.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap();
                shared = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                //product
                param.put("title", getIntent().getStringExtra("title"));
                param.put("category", getIntent().getStringExtra("Cate"));
                param.put("days", getIntent().getStringExtra("Days"));
                param.put("desc_", getIntent().getStringExtra("Desc"));
                param.put("price", getIntent().getStringExtra("Price"));
                param.put("Quan","Quantity: "+getIntent().getStringExtra("Quantity"));
                //seller and buyer number
                param.put("sellernumber",getIntent().getStringExtra("Number"));
                param.put("orderreqNumber",shared.getString("number", ""));
                //date of order
                String a= String.valueOf((Calendar.getInstance().get(Calendar.DATE)));
                String b= String.valueOf((Calendar.getInstance().get(Calendar.MONTH)));
                String c= String.valueOf((Calendar.getInstance().get(Calendar.YEAR)));
                param.put("datee",a+"/"+b+"/"+c);
                param.put("image",getIntent().getStringExtra("Image"));
                param.put("Loc", String.valueOf(latLng));
                return param;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);


    }

    private boolean checkLocation() {

            if(!isLocationEnabled()){
                showAlert();
            }
            return isLocationEnabled();
        }

        private void showAlert() {
            final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("Enable your Location")
                    .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })

            ;
            dialog.show();
        }

    private boolean isLocationEnabled() {
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isPermission=true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        if(response.isPermanentlyDenied()){
                            isPermission=false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }

                }).check();
        return isPermission;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(latLng!=null && flag == 0){
            mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            flag=1;
        }}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdate();
        mLocation=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(mLocation == null){
            startLocationUpdate();
        }

    }

    private void startLocationUpdate() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Update_Intervel)
                .setFastestInterval(fastest_Intervel);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest,this);

    }



    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latLng=new LatLng(location.getLatitude(),location.getLongitude());
        // latLang=latLng.toString();
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(googleApiClient !=null){
            googleApiClient.connect();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();

        }
    }

}
