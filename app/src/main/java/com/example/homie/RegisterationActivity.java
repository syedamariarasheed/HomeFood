package com.example.homie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterationActivity extends AppCompatActivity {

    EditText editTextnumber,editTextName; Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        editTextnumber=findViewById(R.id.number);
        editTextName=findViewById(R.id.input_fullname);
        signup=findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=editTextnumber.getText().toString().trim();
                if(number.isEmpty() || number.length() <10){
                    editTextnumber.setError("Number is required");
                    editTextnumber.requestFocus();
                    return;
                }
                Intent intent=new Intent(getApplicationContext(), VerificationActivity.class);
                intent.putExtra("phonenumber",number);
                intent.putExtra("name",editTextName.getText().toString());
                startActivity(intent);
            }
        });
    }
}
