package com.example.manishauth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.manishauth.MessageToAll.MessageActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;


public class UserActivity extends AppCompatActivity {
    CardView btnLogOut,btnAttendance,btnAboutus,btnEditprofile,btnMeeting,MsgToAdmin;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    AlertDialog.Builder builder;

    private FirebaseAuth.AuthStateListener authStateListener;
    ImageView profilepictv;
    TextView nameTv,classTV,rollnoTv;
    RelativeLayout rootrelativeuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

       firebaseAuth = FirebaseAuth.getInstance();
       user = firebaseAuth.getCurrentUser();
       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference("Users");
        rootrelativeuser=findViewById( R.id.rootrelativeuser );
        profilepictv = (ImageView) findViewById(R.id.profilepicTv);
        nameTv = (TextView)findViewById(R.id.nametv);
        classTV = (TextView)findViewById(R.id.stdtv);
        rollnoTv = (TextView)findViewById(R.id.rolltv);
        btnLogOut = (CardView) findViewById(R.id.btnLogOut);
        btnAttendance= (CardView)findViewById(R.id.attendace);
        btnEditprofile = (CardView) findViewById(R.id.editprofile);
        btnAboutus = (CardView) findViewById(R.id.aboutus) ;
        btnMeeting = (CardView) findViewById(R.id.meeting) ;
        MsgToAdmin = findViewById(R.id.msgtoadmin);
       //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //checkfor user
        ckeckUserStatus();

        builder = new AlertDialog.Builder(UserActivity.this);
        //1
//About Activity
      btnAboutus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // startActivity(new Intent(UserActivity.this,AboutActivity.class));
            String stdname = nameTv.getText().toString();
            Intent myIntent = new Intent(UserActivity.this, Stdmeeting.class);
            myIntent.putExtra("Stdnamekey",stdname);
            startActivity(myIntent);
            }
        });
      // Edit Profile
        btnEditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this,EditProfileActivity.class));
            }
        });
        //Message
        MsgToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, MessageActivity.class));
            }
        });
        btnMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this,MeetingActivity.class));
            }
        });

        //random images
        int[] images = {R.drawable.ic_boy1,R.drawable.ic_boy_2,R.drawable.ic_girl_1,R.drawable.logoc};
        Random rand = new Random();
        profilepictv.setImageResource(images[rand.nextInt(images.length)]);

        //random images



        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDaildog(view);
            }
        });
        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = nameTv.getText().toString();
                String std1=classTV.getText().toString();
                String rollno1=rollnoTv.getText().toString();
                if(name1.equals( "" ) || std1.equals( "" ) || rollno1.equals( "" )){
                    Snackbar snackbar = Snackbar
                            .make(rootrelativeuser, "Check your Internet ", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
                    i.putExtra("name",name1);
                    i.putExtra("class",std1);
                    i.putExtra("rollno",rollno1);
                    startActivity(i);
                }

            }
        });
    }
    public void logoutDaildog(View edView){
        // Create custom dialog object
        final Dialog dialog = new Dialog(UserActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.logout_dailog);
        // Set dialog title
        dialog.setTitle("Logout");
        dialog.show();
        // set values for custom dialog components - text, image and button
        Button Cancelbtn = (Button) dialog.findViewById(R.id.canclebtn);
        Button SubmitButton = (Button) dialog.findViewById(R.id.submitbtn);
        // if SubmitButton is clicked
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout in firrebase
                finish();
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(UserActivity.this, ActivityLogin.class);
                startActivity(I);
            }
        });
        // if Cancel button clicked
        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
            private void ckeckUserStatus() {
        try{
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                String email=user.getEmail();
                //user is signed in
                // get the details from firebase checking with email
                Query query=databaseReference.orderByChild("email").equalTo(email);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                            //get data
                            String name= ""+ds.child("name").getValue();
                            String std= ""+ds.child("std").getValue();
                            String rollno= ""+ds.child("rollno").getValue();

                            //set data
                            nameTv.setText(name);
                            classTV.setText(std);
                            rollnoTv.setText(rollno);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //use random avatar


            } else {
                //user not signed in
                startActivity(new Intent(UserActivity.this,ActivityLogin.class));
                finish();

            }
        }
        catch (Exception e){
            Toast.makeText( this, ""+e, Toast.LENGTH_SHORT ).show();
        }
            }
}
