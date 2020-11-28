package com.example.ATMS.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ATMS.R;

import java.util.ArrayList;
import java.util.Objects;

public class overallAttendance extends AppCompatActivity {

    ListView listView;
    String sid, teacher_id;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Studentlist = new ArrayList<>();
    ArrayList Dates=new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_attendance);
        listView = (ListView) findViewById(R.id.list);
        Bundle bundle1 = getIntent().getExtras();
        sid = bundle1.getString("sid");
        teacher_id = "123";
    }
    public void viewList(View v) {
        Studentlist.clear();
        dbAttendance = ref.child("attendance");
        final int[] present = {0};
        final int[] absent = { 0 };
        final int[] count = { 0 };
        Studentlist.add("      SID       "+"   Status " + "     period");
        dbAttendance.endAt("2020\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String a1 = dataSnapshot.child(sid).getKey();
                    if (sid.equals(a1)) {
                        Dates.add(dsp.getKey());
                    }
                }
                int size=Dates.size();
                for(int i=0;i<size;i++){
                    final String required_date= (String) Dates.get(i);
                    dbAttendance.child(required_date).child(sid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                                String a1 = dataSnapshot.getKey();
                                if (sid == a1) {
                                    String p1 = dsp.getValue().toString();
                                    Studentlist.add(required_date + "            " + p1.substring(0, 1) + "        " + dsp.getKey());
                                    if(p1.startsWith("P")) {
                                        present[0] += 1;
                                    }
                                    else {
                                        absent[0] +=1;
                                    }
                                    count[0] +=1;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                list(Studentlist);
                float percentage=((present[0] *1.0f)/ (count[0]*1.0f))*100;
                if(percentage>74.9f)
                    Toast.makeText(getApplicationContext(), "Your Overall Attendance  is " + percentage + " %", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Your Overall Attendance is Short " + ". It is " + percentage + " %", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void list(ArrayList studentlist){

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