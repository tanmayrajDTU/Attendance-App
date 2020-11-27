package com.example.ATMS.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ATMS.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class adminattendance extends AppCompatActivity {
    ListView listView;
    String sid, teacher_id;
    Spinner class_name;
    String classes;


    EditText date;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Studentlist = new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    DatabaseReference dbStudent;
    String required_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminattendance);

        listView = (ListView) findViewById(R.id.list);
        class_name = (Spinner) findViewById(R.id.spinner13);
        date = (EditText) findViewById(R.id.date);
        //teacher_id = "Tanmay";
    }
    public void viewList(View v) {

        Userlist.clear();
        dbStudent = ref.child("Student");
        //Admin is now able to print all the attendance branch wise
        classes = class_name.getSelectedItem().toString();
        dbStudent.orderByChild("classes").equalTo(classes).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(dsp.child("sid").getValue().toString());
                }
                display_list(Userlist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void display_list(final ArrayList userlist) {

        Studentlist.clear();
        required_date = date.getText().toString();
        dbAttendance = ref.child("attendance");
        Studentlist.add("      SID       "+"Status" + "   period");
        for (Object sid : userlist) {
            dbAttendance.child(required_date).child(sid.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                        String p1 = dsp.getValue().toString();
                        Studentlist.add(dataSnapshot.getKey().toString() + "            " + p1.substring(0,1) +"        "+dsp.getKey());
                    }
                    list(Studentlist);

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                }

            });


        }

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

    public void createPdf(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument myPdfDocument=new PdfDocument();
            Paint paint=new Paint();
            PdfDocument.PageInfo myPageInfo=new PdfDocument.PageInfo.Builder(250,4000,1).create();
            PdfDocument.Page myPage=myPdfDocument.startPage(myPageInfo);
            Canvas canvas=myPage.getCanvas();
            paint.setTextSize(10.5f);
            paint.setColor(Color.rgb(135,206,235));
            canvas.drawText("Attendance Tracking Management System",20,20,paint);
            paint.setTextSize(8.0f);
            paint.setColor(Color.rgb(0,0,0));
            String message=Studentlist.get(0).toString();
            canvas.drawText(message,40,40,paint);
            int size=Studentlist.size();
            int y=40;
            for(int i=1;i<size;i++){
                message=Studentlist.get(i).toString();
                y=y+20;
                canvas.drawText(message,40,(y),paint);
            }
            myPdfDocument.finishPage(myPage);
            File file =new File(this.getExternalFilesDir("/"),"ATMS Report.pdf");
            try{
                myPdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(getApplicationContext(),"PDF created successfully", Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Device not applicable for this feature",Toast.LENGTH_LONG).show();
        }
    }
}