package com.example.ATMS.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.ATMS.R;
import com.example.ATMS.models.Student;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_LONG;


public class addstudent extends AppCompatActivity {
    EditText Sname;
    EditText Sid, spassword;
    String sname, sid, classname, spass;
    Spinner classes;
    DatabaseReference databaseStudent;
    Toolbar mToolbar;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

        databaseStudent = FirebaseDatabase.getInstance().getReference("Student");

        Sname = (EditText) findViewById(R.id.editText1);
        Sid = (EditText) findViewById(R.id.editText3);
        classes = (Spinner) findViewById(R.id.spinner3);
        spassword = (EditText) findViewById(R.id.editText4);
        mToolbar = (Toolbar) findViewById(R.id.ftoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Maintain Student Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void addStudent(View v) {
        if(TextUtils.isEmpty(Sname.getText().toString())){
            Sname.setError("Please Enter Student Name");
        }
        else if(TextUtils.isEmpty(Sid.getText().toString())){
            Sid.setError("Please Enter Student Id");
        }
        else if(TextUtils.isEmpty(spassword.getText().toString())){
            spassword.setError("Please Enter Student Password");
        }
        else if (!(TextUtils.isEmpty(Sid.getText().toString()))) {
            //String id = databaseStudent.push().getKey();
            sname = Sname.getText().toString();
            sid = Sid.getText().toString();
            classname = classes.getSelectedItem().toString();
            spass = spassword.getText().toString();

            Student student = new Student(sname, sid, classname, spass);
            databaseStudent.child(sid).setValue(student);
            Toast.makeText(getApplicationContext(), "Student Added/Updated Successfully", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Fields Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
    }

    public void removeStudent(View v) {
        if (!TextUtils.isEmpty(Sid.getText().toString())) {
            sid = Sid.getText().toString();
            databaseStudent.child(sid).setValue(null);
            Toast.makeText(getApplicationContext(), "Student Removed Successfully", Toast.LENGTH_LONG).show();

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

    public void viewStudent(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.DialogAnimation).create(); //Read Update
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.setTitle("Student Details");
        if (!TextUtils.isEmpty(Sid.getText().toString())) {
            sid = Sid.getText().toString();
            reff = FirebaseDatabase.getInstance().getReference().child("Student").child(sid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String branch = snapshot.child("classes").getValue().toString();
                    String stu_id = snapshot.child("sid").getValue().toString();
                    String name = snapshot.child("sname").getValue().toString();
                    String passw = snapshot.child("spass").getValue().toString();
                    String message_last = ("\nStudent Name:        " + name + "\n\n" + "Student ID:               " + stu_id + "\n\n" + "Student Branch:       " + branch + "\n\n" + "Student Password:  " + passw);

                    alertDialog.setMessage(message_last);
                    alertDialog.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "ID Cannot Be Empty", Toast.LENGTH_LONG).show();
        }

    }
}
