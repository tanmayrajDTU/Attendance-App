package com.example.ATMS.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ATMS.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.ATMS.R;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;

public class studentlogin extends AppCompatActivity {
    String message;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    Toolbar mToolbar;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbStudent;
    DatabaseReference reff;
    private static long back_pressed;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        mToolbar.setTitle(message+"'s Dashboard"+"("+date+")");
        final TextView txtView = (TextView) findViewById(R.id.textView1);
        dbStudent = ref.child("Student").child(message);
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String name =snapshot.child("sname").getValue().toString();
                txtView.setText("Welcome : "+ name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong",LENGTH_LONG).show();
            }
        });
    }
    public void viewAttendance(View v){
        Bundle basket = new Bundle();
        basket.putString("sid", message);
        Intent intent = new Intent(this, studentAttendanceActivity.class);
        intent.putExtras(basket);
        startActivity(intent);
    }

    public void logoutStudent(View view) {
        Intent logoutStudent=new Intent(studentlogin.this,DashboardActivity.class);
        logoutStudent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutStudent);
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
    public void profile_s(View view){
        final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.DialogAnimation).create(); //Read Update
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.setTitle("Student Details");
        dbStudent = ref.child("Student");
        reff=FirebaseDatabase.getInstance().getReference().child("Student").child(message);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String branch=snapshot.child("classes").getValue().toString();
                String stu_id=snapshot.child("sid").getValue().toString();
                String name=snapshot.child("sname").getValue().toString();
                String passw=snapshot.child("spass").getValue().toString();
                String email=snapshot.child("email").getValue().toString();
                String phone=snapshot.child("phone").getValue().toString();
                String message_last = ("\nStudent Name:        " + name + "\n\n" + "Student ID:               " + stu_id
                        + "\n\n" + "Student Branch:       " + branch + "\n\n" + "Student Password:  " + passw
                        +"\n\nEmail Address:     "+email+"\n\nPhone Number:     "+phone);
                alertDialog.setMessage(message_last);
                alertDialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong",LENGTH_LONG).show();
            }
        });
    }

    public void viewOverall(View view) {
        Bundle basket = new Bundle();
        basket.putString("sid", message);
        Intent intent = new Intent(this, overallAttendance.class);
        intent.putExtras(basket);
        startActivity(intent);
    }
}
