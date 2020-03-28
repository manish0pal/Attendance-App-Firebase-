package com.example.manishauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonScan;
    private TextView textViewName,textViewcode;
    private IntentIntegrator qrScan;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mDatabaseReference;
    ProgressDialog pd;
    RelativeLayout rootrelative;

    TextToSpeech textToSpeech;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference studentinmeeting,studentinmeeting1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference("Meetings");
        studentinmeeting = firebaseDatabase.getReference();
        studentinmeeting1 = firebaseDatabase.getReference();

        buttonScan = (Button) findViewById(R.id.buttonScan);
        rootrelative = findViewById( R.id.rootrelative );
       // textViewcode=(TextView) findViewById(R.id.textViewcode);
       // textViewName = (TextView) findViewById(R.id.textViewName);
        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
        textToSpeech = new TextToSpeech( AttendanceActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage( Locale.UK);
            }
        } );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        final String name1=getIntent().getStringExtra("name");
        final String std1=getIntent().getStringExtra("class");
        final String rollno1=getIntent().getStringExtra("rollno");
        if (result != null) {

           // isInternetAvailable();
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                pd=new ProgressDialog(this);

                pd.setMessage("Please Wait....");

                try {

                    JSONObject obj = new JSONObject(result.getContents());
                    final String Time = obj.getString("Time").trim();
                    final String passcode= obj.getString("Code");

                  //  textViewName.setText(Time);
                    //set value
                    Query query=mDatabaseReference.orderByChild("time").equalTo(Time);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds:dataSnapshot.getChildren()) {
                                String code = "" + ds.child("code").getValue();
                               // textViewcode.setText(code);
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String uid= user.getUid();
                                if(code.equals(passcode)) {
                                    AttendanceUser user1 = new AttendanceUser(name1,std1, rollno1);
                                    studentinmeeting = mDatabase.getReference("StudentInMeeting/"+Time).child(uid);
                                    studentinmeeting.setValue(user1);
                                    studentinmeeting1 = mDatabase.getReference("Users").child( uid ).child( "meetingattend" );
                                    String key =  studentinmeeting1.push().getKey();
                                    studentinmeeting1.child( key ).setValue( Time );
                                    pd.dismiss();

                                    //speak start


                                    String tospeak=name1+"  your attendance is Marked";
                                    textToSpeech.speak( tospeak,TextToSpeech.QUEUE_FLUSH,null );
                                    //speak end
                                    Toast.makeText(AttendanceActivity.this, "Attendance marked !", Toast.LENGTH_LONG).show();

                                }
                                else{
                                    pd.dismiss();
                                    Snackbar snackbar = Snackbar
                                            .make(rootrelative, "QR-code is disabled", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                } catch (Exception e) {
                    pd.dismiss();
                    Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View view) {
        qrScan.initiateScan();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AttendanceActivity.this, UserActivity.class));
        finish();
    }
}
