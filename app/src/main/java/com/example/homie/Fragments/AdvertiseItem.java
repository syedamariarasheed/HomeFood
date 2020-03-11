package com.example.homie.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.homie.R;
import com.example.homie.Selectimage;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class AdvertiseItem extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText title,Desc;
    SharedPreferences shared;
    String category;
    Spinner spinner;
    EditText Price;
    String days="";
    CheckBox mon,tues,wed,thurs,fri,sat,sun;
    RadioGroup radioButtonGroup;
    RadioButton radioButton;
    Button Send;

    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.advertiseitem,container,false);
        Send =v.findViewById(R.id.nextButton);
        title=v.findViewById(R.id.title);
        Desc=v.findViewById(R.id.desc);
        Price=v.findViewById(R.id.price);
        radioButtonGroup=v.findViewById(R.id.groupradio);
        spinner=v.findViewById(R.id.spinner);
        mon=v.findViewById(R.id.checkBox1);
        tues=v.findViewById(R.id.checkBox2);
        wed=v.findViewById(R.id.checkBox3);
        thurs=v.findViewById(R.id.checkBox4);
        fri=v.findViewById(R.id.checkBox5);
        sat=v.findViewById(R.id.checkBox6);
        sun=v.findViewById(R.id.checkBox7);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days="";
                if(mon.isChecked()){
                    days+=mon.getText().toString()+" ";
                }
                if(tues.isChecked()){
                    days+=tues.getText().toString()+" ";
                }
                if(wed.isChecked()){
                    days+=wed.getText().toString()+" ";
                } if(thurs.isChecked()){
                    days+=thurs.getText().toString()+" ";
                } if(fri.isChecked()){
                    days+=fri.getText().toString()+" ";
                } if(sat.isChecked()){
                    days+=sat.getText().toString()+" ";
                } if(sun.isChecked()){
                    days+=sun.getText().toString()+" ";
                }
                Toast.makeText(v.getContext(), days, Toast.LENGTH_SHORT).show();
                int selectedId = radioButtonGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) v.findViewById(selectedId);
                Intent intent=new Intent(v.getContext(), Selectimage.class);
                intent.putExtra("days",days);
                if(radioButton.getText().toString().equals("Visible")){
                    shared = v.getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                intent.putExtra("number", shared.getString("number", ""));
                    Toast.makeText(getContext(), shared.getString("number", ""), Toast.LENGTH_SHORT).show();
                }
                else {
                    intent.putExtra("number", " ");
                }
                intent.putExtra("title",title.getText().toString());
                intent.putExtra("category",category);
                intent.putExtra("desc",Desc.getText().toString());
                intent.putExtra("price",Price.getText().toString());
                startActivity(intent);
            }
        });
        //spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array._array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });// spinner End
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}