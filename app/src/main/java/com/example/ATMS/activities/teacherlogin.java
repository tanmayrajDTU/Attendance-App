package com.example.ATMS.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ATMS.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class teacherlogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String item;
    String message;
    Toolbar mToolbar;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbTeacher;
    DatabaseReference reff_2;
    private static long back_pressed;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlogin);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);


        //to get username from login page
        Bundle bundle1 = getIntent().getExtras();
        message = bundle1.getString("message");
        mToolbar=(Toolbar)findViewById(R.id.takeattendancebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(message+"'s Dashboard  - "+date);

        final TextView txtView = (TextView) findViewById(R.id.textView1);
        dbTeacher = ref.child("Teacher").child(message);
        dbTeacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name =snapshot.child("tname").getValue().toString();
                txtView.setText("Welcome : "+ name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong",LENGTH_LONG).show();
            }
        });
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> categories = new ArrayList<String>();
        categories.add("SE");
        categories.add("CSE");
        categories.add("IT");
        categories.add("ECE");
        categories.add("ME");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void takeAttendanceButton(View v){
        Bundle basket= new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid", message);


        Intent intent = new Intent(this, takeAttendance.class);
        intent.putExtras(basket);
        startActivity(intent);
    }
    public void  previous_records(View v){
        Bundle basket= new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid", message);


        Intent intent = new Intent(this, teacher_attendanceSheet.class);
        intent.putExtras(basket);
        startActivity(intent);
    }


    public void logoutTeacher(View view) {
        Intent logoutTeacher=new Intent(teacherlogin.this,DashboardActivity.class);
        logoutTeacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutTeacher);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
    public void profile_t(View view){
        final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.DialogAnimation).create(); //Read Update
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.setTitle("Teacher Details");
        dbTeacher = ref.child("Teacher");
        reff_2=FirebaseDatabase.getInstance().getReference().child("Teacher").child(message);
        reff_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String branch=snapshot.child("classes").getValue().toString();
                String teacher_id=snapshot.child("tid").getValue().toString();
                String name=snapshot.child("tname").getValue().toString();
                String passw=snapshot.child("tpass").getValue().toString();
                String Subject=snapshot.child("subject").getValue().toString();
                String phone=snapshot.child("phone").getValue().toString();
                String email=snapshot.child("email").getValue().toString();
                String message_last = ("\nTeacher Name:        " + name + "\n\n" + "Teacher ID:              " + teacher_id + "\n\n" +
                        "Teacher Subject:     " + Subject + "\n\n" + "Teacher Branch:      " + branch + "\n\n" + "Teacher Password: " + passw
                        +"\n\nPhone Number:     "+phone+"\n\nEmail Address:    "+email);
                alertDialog.setMessage(message_last);
                alertDialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong",LENGTH_LONG).show();
            }
        });
    }
}
