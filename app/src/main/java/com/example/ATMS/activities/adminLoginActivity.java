package com.example.ATMS.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ATMS.R;

public class adminLoginActivity extends AppCompatActivity {
    Button login;
    DatabaseReference ref;
    String userid,pass;
    String dbpassword;
    Bundle basket;
    EditText username,password;
    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        login = findViewById(R.id.login);
        username = findViewById(R.id.adminEditText);
        password = findViewById(R.id.adminPasswordEditText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = username.getText().toString();
                pass = password.getText().toString();
                mDialog=new ProgressDialog(adminLoginActivity.this);
                mDialog.setMessage("Please Wait..."+userid);
                mDialog.setTitle("Loading");
                mDialog.show();
                basket = new Bundle();
                basket.putString("message", userid);

                ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference dbuser = ref.child("Admin");


                dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            mDialog.dismiss();
                            dbpassword = dataSnapshot.child("Admin").getValue(String.class);
                            verify(dbpassword);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void verify(String dbpassword) {
        if(userid.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Username Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
        else if (userid.equals("Admin") && pass.equalsIgnoreCase(this.dbpassword) ) {
            mDialog.dismiss();
            Intent intent = new Intent(this, adminlogin.class);
            intent.putExtras(basket);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please Enter Valid User ID And Password", Toast.LENGTH_LONG).show();
        }
    }
}
