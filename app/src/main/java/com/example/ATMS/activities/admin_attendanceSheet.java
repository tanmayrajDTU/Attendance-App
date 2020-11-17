package com.example.ATMS.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ATMS.R;

import java.util.ArrayList;

public class admin_attendanceSheet extends AppCompatActivity {

    ListView listView;
    Spinner class_name;
    String classes;
    EditText date;
    ArrayList<String> Userlist = new ArrayList<String>();
    ArrayList<String> Studentlist = new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    DatabaseReference dbStudent;
    String required_date;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance_sheet);
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Attendance Records");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.list);
        class_name = (Spinner) findViewById(R.id.spinner3);
        date = (EditText) findViewById(R.id.date);
        classes = class_name.getSelectedItem().toString();



    }

    public void display_list(final ArrayList<String> userlist) {
        Studentlist.clear();
        required_date = date.getText().toString();
        dbAttendance = ref.child("attendance");
        dbAttendance.child(required_date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be holded Here

                Studentlist.add("      SID              "+"p1  "+"p2  ");
                for (Object sid : userlist) {

                    //DataSnapshot dsp=dataSnapshot.child(sid.toString());
                    String p1=dataSnapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p2=dataSnapshot.child(sid.toString()).child("p2").getValue().toString().substring(0,1);
                    Studentlist.add(dataSnapshot.child(sid.toString()).getKey().toString()+"    "+p1+"     "+p2+"     "); //add result into array list
                }
                //Toast.makeText(getApplicationContext(),Studentlist.toString(), Toast.LENGTH_LONG).show();
                list(Studentlist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });




    }




    public void viewlist(View v){

        Userlist.clear();
        dbStudent = ref.child("Student");
        dbStudent.orderByChild("classes").equalTo(class_name.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        Userlist.add(dsp.child("sid").getValue().toString());
                    }
                    display_list(Userlist);
                }
                catch (Exception e)
                {
                    Toast.makeText(admin_attendanceSheet.this,"exception: "+e,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });




    }
public void list(ArrayList<String> studentlist){
    //int color = Color.argb(255, 255, 175, 64);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, studentlist);
    listView.setAdapter(adapter);


}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
