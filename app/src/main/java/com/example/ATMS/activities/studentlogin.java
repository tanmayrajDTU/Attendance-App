package com.example.ATMS.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

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
    private static long back_pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        mToolbar.setTitle(message+"'s Dashboard"+"("+date+")");
        TextView txtView = (TextView) findViewById(R.id.textView1);


        txtView.setText("Welcome :"+message);

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
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update
        alertDialog.setTitle("Student Details");
        final String[] message1 = new String[1];
        dbStudent = ref.child("Student");
        dbStudent.orderByChild("sid").equalTo(message).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                message1[0] =dataSnapshot.getValue().toString();
                alertDialog.setMessage(message1[0]);
                alertDialog.show();
                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", LENGTH_LONG).show();
            }

        });
    }

}
