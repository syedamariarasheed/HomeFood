package com.example.homie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.ID;

public class Selectimage extends AppCompatActivity {

    String days="";
    ImageView imageView;
    SharedPreferences shared;
    Uri filepath;
    ProgressBar progressBar;
    Bitmap bitmap;
    Button choose,Advertisebtn;
    String url="https://test350999.000webhostapp.com/senditem.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectimage);
        imageView=findViewById(R.id.imagee);
        choose=findViewById(R.id.chooseBtn);
        days=getIntent().getStringExtra("days");
        progressBar =findViewById(R.id.probar);

        Advertisebtn=findViewById(R.id.Advertisebtn);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Advertisebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senddata();

            }
        });
    }

    private void selectImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null ){
            filepath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }
            catch(Exception e){
                Toast.makeText(this,"Error in image selection",Toast.LENGTH_SHORT).show();
            }

        }
    }
    private String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    void senddata(){
        progressBar.setVisibility(View.VISIBLE);
        final Intent intent=getIntent();
       final String title = intent.getStringExtra("title");
        final String category= intent.getStringExtra("category");
       final String days = intent.getStringExtra("days");
       final String desc = intent.getStringExtra("desc");
       final String price = intent.getStringExtra("price");
        final String number = intent.getStringExtra("number");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Selectimage.this,Homepage.class);
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
                param.put("number", shared.getString("number", ""));
                param.put("title", title);
                param.put("category", category);
                param.put("days", days);
                param.put("desc_", desc);
                param.put("price", price);
                param.put("visibility", number);
                String a= String.valueOf((Calendar.getInstance().get(Calendar.DATE)));
                String b= String.valueOf((Calendar.getInstance().get(Calendar.MONTH)));
                String c= String.valueOf((Calendar.getInstance().get(Calendar.YEAR)));
                param.put("datee",a+"/"+b+"/"+c);
                param.put("image",BitmapToString(bitmap));
                return param;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }
}
