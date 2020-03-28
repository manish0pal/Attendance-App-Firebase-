package com.example.adminzestech.MeetingList1;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.adminzestech.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adminzestech.R;

public class StudentMarkedActivity extends AppCompatActivity {
    RelativeLayout rootrelative;
    TextView timeofmeeting;
    RecyclerView recyclerView;
    StudentAttendAdapter adapter;
    FloatingActionButton downloadstudentdata;
    RelativeLayout ifnodataavail;
    List<Student> StudentList;
    DatabaseReference dbuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marked);
        downloadstudentdata =findViewById(R.id.downloadmeetingdata);
        ifnodataavail = findViewById(R.id.nodataavailable);
        rootrelative = findViewById( R.id.rootrelative );
        //****************************
        Intent intent = getIntent();
        final String timingofmeet = intent.getStringExtra("Meeting-Time");
        final String agendaofmeet = intent.getStringExtra("MeetingAgenda");
        final String dptofmeet = intent.getStringExtra("Meeting-Dpt");
        timeofmeeting = (TextView)findViewById(R.id.timingofmeeting);
        timeofmeeting.setText(timingofmeet);

        StudentList=new ArrayList<>();
        recyclerView =(RecyclerView)findViewById(R.id.recycleviewer);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StudentList = new ArrayList<>();
        adapter = new StudentAttendAdapter(this,StudentList);
        //1
        //2
        recyclerView.setAdapter(adapter);

        dbuser= FirebaseDatabase.getInstance().getReference("StudentInMeeting/"+timingofmeet);
        dbuser.addListenerForSingleValueEvent(valueEventListener);

        //data
        downloadstudentdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWriteStoragePermissionGranted();
                try{
                    StringBuilder studentlist = new StringBuilder();
                    int numberofstudent = adapter.getItemCount();
                    //seprate tme and date
                    int indexofmsgStart = timingofmeet.indexOf("~");
                    String time = timingofmeet.substring(0,indexofmsgStart);
                    String date = timingofmeet.substring(indexofmsgStart+1);
                    studentlist.append("Time:-,"+time+"\n");
                    studentlist.append("Department:-"+dptofmeet+"\n");
                    studentlist.append("Agenda:-,"+agendaofmeet+"\n");
                    studentlist.append("Date:-,"+date+"\n");
                    studentlist.append("\nClass,Name,Roll-no");
                    for (int count=0;count<numberofstudent;count++) {
                        Student Student = StudentList.get(count);
                        studentlist.append("\n"+Student.std+","+Student.name+","+Student.rollno);
                    }
                         //csv
                    //1
                    checkExternalMedia();
                    writeToSDFile(studentlist,timingofmeet);




                    //2

                }
                catch (Exception ex){
                    Toast.makeText(StudentMarkedActivity.this, ""+ex, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            StudentList.clear();
            try{
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Student usermeets = snapshot.getValue(Student.class);
                        StudentList.add(usermeets);
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    getBackground();
                    Toast.makeText(StudentMarkedActivity.this, "data not exist", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Toast.makeText(StudentMarkedActivity.this,""+e, Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        //Toast.makeText(this, "External Media: readable=" +mExternalStorageAvailable+" writable="+mExternalStorageWriteable, Toast.LENGTH_SHORT).show();
      //  tv.append("\n\nExternal Media: readable=" +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }


    private void writeToSDFile(StringBuilder studentlist,String filename ){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();
       // Toast.makeText(this, "External file system root: "+root, Toast.LENGTH_SHORT).show();


        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath() + "/Zestech");
        dir.mkdirs();
        File file = new File(dir, filename+".csv");
        Snackbar snackbar = Snackbar
                .make(rootrelative, "Downloaded in Zestech Folder", Snackbar.LENGTH_LONG);
        snackbar.show();

        try {

            FileOutputStream f = new FileOutputStream(file);
            f.write(studentlist.toString().getBytes());
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText( this, ""+e, Toast.LENGTH_SHORT ).show();
           // Toast.makeText(this, "******* File not found. Did you add a WRITE_EXTERNAL_STORAGE permission to the   manifest?", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText( this, ""+e, Toast.LENGTH_SHORT ).show();
        }
        Toast.makeText(this, "File written to "+file, Toast.LENGTH_SHORT).show();
    }
    public void getBackground() {
        int number = new Random().nextInt(4) ;
        Random generator = new Random();
        switch (number){
            case 0:
                ifnodataavail.setBackgroundResource(R.drawable.no1);
                break;
            case 1:
                ifnodataavail.setBackgroundResource(R.drawable.no2);
                break;
            case 2:
                ifnodataavail.setBackgroundResource(R.drawable.no3);
                break;
            case 3:
                ifnodataavail.setBackgroundResource(R.drawable.no4);
                break;

        }
    }



    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
               // Log.v('TAG',"Permission is granted2");
                return true;
            } else {

               // Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
           // Log.v(TAG,"Permission is granted2");
            return true;
        }
    }
}

/////






