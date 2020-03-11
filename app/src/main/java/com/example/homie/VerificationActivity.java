package com.example.homie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    String verificationId;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button signin;
    EditText editTextcode;
    ProgressBar progressBar;
     FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.veriactivity);
        signin=findViewById(R.id.btn_signin);
        //sharedp
        sharedPreferences =getSharedPreferences("myPref",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        firebaseAuth=FirebaseAuth.getInstance();
        String phonenumber=getIntent().getStringExtra("phonenumber");
        Log.d("ph",phonenumber);
        signin=findViewById(R.id.btn_signin);
        editTextcode=findViewById(R.id.code);
        progressBar=findViewById(R.id.progressbar);
        sendVerificationCode(phonenumber);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextcode.getText().toString().trim().isEmpty() || editTextcode.getText().toString().trim().length()<6){
                    editTextcode.setError("Enter code");
                    editTextcode.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(editTextcode.getText().toString().trim());
            }
        });
    }
    private void verifyCode(String Code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,Code);
        signinWithcredential(credential);
    }

    private void signinWithcredential(PhoneAuthCredential credential) {
     firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               editor.putString("number",getIntent().getStringExtra("phonenumber"));
               editor.commit();
               editor.putString("name",getIntent().getStringExtra("name"));
               editor.commit();
               Log.d("success", "signInWithCredential:success");
            Intent intent=new Intent(VerificationActivity.this, Homepage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            String numberSubs=getIntent().getStringExtra("phonenumber").replaceAll("[\\-\\+\\.\\^:,]","");
               FirebaseMessaging.getInstance().subscribeToTopic(numberSubs);
               startActivity(intent);
           }
         }
     });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String verificationcode=phoneAuthCredential.getSmsCode();
            if(verificationcode != null){
                progressBar.setVisibility(View.VISIBLE);
                editTextcode.setText(verificationcode);
                verifyCode(verificationcode);
            }
        }
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId =s;
            Log.d("veri",verificationId);

        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };


}
