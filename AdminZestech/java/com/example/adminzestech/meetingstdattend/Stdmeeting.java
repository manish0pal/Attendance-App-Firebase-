package com.example.adminzestech.meetingstdattend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminzestech.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Stdmeeting extends AppCompatActivity {

    ListView meetinglistLV;

    RelativeLayout ifnodataavail;
    DatabaseReference dbuser;
    TextView Stdname,MeetingTime;
    String stdnamestr,stduid;
    List meetingtimestdattend = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdmeeting);
        Stdname=findViewById(R.id.meetinglisttext);
        ifnodataavail = findViewById(R.id.nodataavailable);
        meetinglistLV = findViewById(R.id.meetingLV);
        MeetingTime = findViewById(R.id.text1);

        Intent intent = getIntent();
        stdnamestr = intent.getStringExtra("Stdnamekey");
        stduid=intent.getStringExtra("StdUid");

        Stdname.setText(stdnamestr);
        dbuser= FirebaseDatabase.getInstance().getReference("Users/"+stduid+"/meetingattend");
        dbuser.addListenerForSingleValueEvent(valueEventListener);
       // Log.d("studentuid",stduid);
        // List View started


        //List view ends
    }

 ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<String> lst = new ArrayList<String>();
            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    lst.add(String.valueOf(snapshot));
                }
                String strlist = lst.toString();
                setMeetingList(strlist);
            }
            else{
               getBackground();
                Toast.makeText(Stdmeeting.this, "data not exist", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(Stdmeeting.this, ""+databaseError, Toast.LENGTH_SHORT).show();
        }
    };
    public  void setMeetingList( String strOrig){
        try{
            String strFind = " value = ";
            int count = 0, fromIndex = 0;
            while ((fromIndex = strOrig.indexOf(strFind, fromIndex)) != -1 ){
                List a = new ArrayList();
                for(int i=fromIndex+9;i<=fromIndex+32;i++){
                    a.add(strOrig.charAt(i));
                }
                String timestr = a.toString().substring(1, 3 * a.size() - 1).replaceAll(", ", "");
                // print string
                meetingtimestdattend.add(timestr);
                count++;
                fromIndex++;
            }
            //Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        }
        catch (Error error){
            Toast.makeText(this, "Something went wrong:- "+ error, Toast.LENGTH_SHORT).show();
        }
        //1

        //2
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
               R.layout.listlayout,meetingtimestdattend );
        meetinglistLV.setAdapter(adapter);

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
}
