package com.example.ATMS.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ATMS.R;
import com.example.ATMS.models.Teacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_LONG;


public class addteacher extends AppCompatActivity {
    EditText Tname;
    EditText Tid;
    EditText subject,tpassword;
    String tname,tid,sub,classname,tpass;
    Spinner classes;
    Button addButton;
    DatabaseReference databaseTeacher;
    DatabaseReference reff_2;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteacher);

        databaseTeacher = FirebaseDatabase.getInstance().getReference("Teacher");

        Tname =  (EditText) findViewById(R.id.editText1);
        Tid =  (EditText) findViewById(R.id.editText3);
        subject =  (EditText) findViewById(R.id.editText4);
        classes = (Spinner) findViewById(R.id.spinner3);
        classes = (Spinner) findViewById(R.id.spinner3);
        tpassword =  (EditText) findViewById(R.id.editText5);
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add/Remove Teacher");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    public void addTeacher(View v){
        tname = Tname.getText().toString();
        tid = Tid.getText().toString();
        sub = subject.getText().toString();
        classname = classes.getSelectedItem().toString();
        tpass = tpassword.getText().toString();

        if (!(TextUtils.isEmpty(Tid.getText().toString()))) {
           // String id = databaseTeacher.push().getKey();
            Teacher teacher =new Teacher(tname ,tid ,sub ,classname,tpass);
            databaseTeacher.child(tid).setValue(teacher);
            Toast.makeText(getApplicationContext(),"Teacher Added Successfully", Toast.LENGTH_LONG).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(),"Fields Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
    }
    public void removeTeacher(View v){
        if (!TextUtils.isEmpty(Tid.getText().toString())) {
            tid = Tid.getText().toString();
            databaseTeacher.child(tid).setValue(null);
            Toast.makeText(getApplicationContext(),"Teacher Removed Successfully", Toast.LENGTH_LONG).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(),"ID Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
    }
    public void viewTeacher(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update
        alertDialog.setTitle("Teacher Details");
        if (!TextUtils.isEmpty(Tid.getText().toString())) {
            tid = Tid.getText().toString();
            reff_2 = FirebaseDatabase.getInstance().getReference().child("Teacher").child(tid);
            reff_2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String branch = snapshot.child("classes").getValue().toString();
                    String teacher_id = snapshot.child("tid").getValue().toString();
                    String name = snapshot.child("tname").getValue().toString();
                    String passw = snapshot.child("tpass").getValue().toString();
                    String Subject = snapshot.child("subject").getValue().toString();
                    String message_last = ("\nTeacher Name:        " + name + "\n\n" + "Teacher ID:              " + teacher_id + "\n\n" + "Teacher Subject:     " + Subject + "\n\n" + "Teacher Branch:      " + branch + "\n\n" + "Teacher Password: " + passw);
                    alertDialog.setMessage(message_last);
                    alertDialog.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "ID Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
