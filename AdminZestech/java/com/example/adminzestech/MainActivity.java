package com.example.adminzestech;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adminzestech.MeetingList1.MeetingActivity;
import com.example.adminzestech.MessageToAll.MessageActivity;
import com.example.adminzestech.StudentRecords.RecordActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button btntakemeeting,record,stdatten,MsgToStd,btnlogout,changepwd;
    SharedPreferences prf;
    String dpt_name,uname;
    TextView dptnametv,unametv;
    LinearLayout hidebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btntakemeeting =(Button)findViewById(R.id.takemeeting);
        record=(Button)findViewById(R.id.records);
        stdatten=(Button)findViewById(R.id.meetsuser) ;
        MsgToStd=(Button)findViewById(R.id.msgtostd);
        btnlogout=findViewById(R.id.logout);
        changepwd=findViewById(R.id.changepass);
        dptnametv=findViewById(R.id.department);
        unametv=findViewById(R.id.nameid);
        hidebutton=findViewById(R.id.CngpassAndMsg);
//get user details
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
       dpt_name =  prf.getString("dpt_key","");
       uname =  prf.getString("name_key","");
        dptnametv.setText(dpt_name);
        unametv.setText(uname);

        if(!dpt_name.equals("ADMIN")){
            hidebutton.setVisibility(View.GONE);
        }

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
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
                        SharedPreferences.Editor editor = prf.edit();
                        editor.clear();
                        editor.commit();
                        Intent I = new Intent(MainActivity.this, LoginActivity.class);
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
        });

        btntakemeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GenmeetingActivity.class));

            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordActivity.class));

            }
        });
        stdatten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MeetingActivity.class));
            }
        });

        MsgToStd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, MessageActivity.class));

            }
        });
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChangepswdActivity.class));

            }
        });
    }



}
