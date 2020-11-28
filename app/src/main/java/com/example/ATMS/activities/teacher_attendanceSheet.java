package com.example.ATMS.activities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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


public class teacher_attendanceSheet extends AppCompatActivity {
    ListView listView;
    String teacher_id,class_selected;


    EditText date;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Studentlist = new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    DatabaseReference dbStudent;
    String required_date;
    androidx.appcompat.widget.Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance_sheet);

        listView = (ListView) findViewById(R.id.list);
        date = (EditText) findViewById(R.id.date);
        mToolbar=(androidx.appcompat.widget.Toolbar)findViewById(R.id.ftoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Previous Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle1 = getIntent().getExtras();
        class_selected = bundle1.getString("class_selected");
        teacher_id = bundle1.getString("tid");

    }

    public void viewList(View v) {

        Userlist.clear();
        dbStudent = ref.child("Student");
        dbStudent.orderByChild("classes").equalTo(class_selected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(dsp.child("sid").getValue().toString());
                }
                display_list(Userlist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
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
                        if((p1.equals("A / "+teacher_id))||(p1.equals("P / "+teacher_id))){
                            Studentlist.add(dataSnapshot.getKey().toString() + "            " + p1.substring(0,1) +"        "+dsp.getKey());
                        }
                    }
                    list(Studentlist);

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
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
            PdfDocument.PageInfo myPageInfo=new PdfDocument.PageInfo.Builder(250,2000,1).create();
            PdfDocument.Page myPage=myPdfDocument.startPage(myPageInfo);
            Canvas canvas=myPage.getCanvas();
            paint.setTextSize(10.5f);
            paint.setColor(Color.rgb(135,206,235));
            canvas.drawText("Attendance Tracking Management System",20,20,paint);
            paint.setTextSize(8.0f);
            canvas.drawText("Teacher Attendance Report ",40,40,paint);
            paint.setColor(Color.rgb(0,0,0));
            String message=Studentlist.get(0).toString();
            canvas.drawText(message,40,60,paint);
            int size=Studentlist.size();
            int y=60;
            for(int i=1;i<size;i++){
                message=Studentlist.get(i).toString();
                y=y+20;
                canvas.drawText(message,40,(y),paint);
            }
            myPdfDocument.finishPage(myPage);
            File file =new File(this.getExternalFilesDir("/"),"ATMS Teacher Report.pdf");
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
