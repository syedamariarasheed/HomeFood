package com.example.homie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

public class OrderRequest extends AppCompatActivity {


    int Count;
    TextView title,desc,days,price,contact,category,total;
    ImageView imageView;
    Button sendLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);
        title=findViewById(R.id.TITLE);
        desc=findViewById(R.id.desc);
        days=findViewById(R.id.Days);
        price=findViewById(R.id.Price);
        category=findViewById(R.id.cate);
        sendLoc=findViewById(R.id.sendloc);
        contact=findViewById(R.id.contact);
        total=findViewById(R.id.total);
        imageView=findViewById(R.id.Img);
        final ElegantNumberButton button = (ElegantNumberButton) findViewById(R.id.number_button);
        button.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
              Count = Integer.parseInt(button.getNumber());
              int tot= Integer.parseInt(getIntent().getStringExtra("Price"));
              tot=tot*Count;
              total.setText(""+tot+ " RS/-");
            }
        });

        title.setText(getIntent().getStringExtra("title"));
        desc.setText(getIntent().getStringExtra("Desc"));
        days.setText(getIntent().getStringExtra("Days"));
        price.setText(getIntent().getStringExtra("Price"));
        category.setText(getIntent().getStringExtra("Cate"));
        contact.setText(getIntent().getStringExtra("Visi"));
        Picasso.get()
                .load(getIntent().getStringExtra("Image"))
                .into(imageView);

        sendLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderRequest.this,MapsActivity.class);
                intent.putExtra("title",getIntent().getStringExtra("title"));
                intent.putExtra("Image",getIntent().getStringExtra("Image"));
                intent.putExtra("Cate",getIntent().getStringExtra("Cate"));
                intent.putExtra("Days",getIntent().getStringExtra("Days"));
                intent.putExtra("Price",getIntent().getStringExtra("Price"));
                intent.putExtra("Number",getIntent().getStringExtra("Number"));
                intent.putExtra("Desc",getIntent().getStringExtra("Desc"));
                intent.putExtra("Quantity",String.valueOf(Count));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        total.setText(getIntent().getStringExtra("Price")+ " RS/-");
    }
}
