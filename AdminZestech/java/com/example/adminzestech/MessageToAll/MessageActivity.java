package com.example.adminzestech.MessageToAll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzestech.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MessageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MessageAdapter adapter;
    DatabaseReference adminmsg;
    List<messages_gone> messages_goneList;
    EditText msgtosend,headingofmsg;
    ImageButton btnsend;
    String date;
    RelativeLayout ifnodataavail;
    SharedPreferences prf;
    String dpt_name,uname;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messages_goneList= new ArrayList<>();
        ifnodataavail = findViewById(R.id.nodataavailable);
        recyclerView =(RecyclerView)findViewById(R.id.adminmsg);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messages_goneList = new ArrayList<>();
        adapter = new MessageAdapter(this,messages_goneList);
        recyclerView.setAdapter(adapter);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        dpt_name =  prf.getString("dpt_key","");

        if(dpt_name.equals("ADMIN")){
            adminmsg= FirebaseDatabase.getInstance().getReference("Admin_Msg");
            final Query query = adminmsg.orderByChild("time");
            query.addListenerForSingleValueEvent(valueEventListener);
        }else{
            adminmsg= FirebaseDatabase.getInstance().getReference("Admin_Msg");
            final Query query = adminmsg.orderByChild("department").equalTo("ADMIN");
            query.addListenerForSingleValueEvent(valueEventListener);
            final Query query1 = adminmsg.orderByChild("department").equalTo(dpt_name);
            query1.addListenerForSingleValueEvent(valueEventListener);
        }


        msgtosend=findViewById(R.id.datatobesend);
        headingofmsg=findViewById(R.id.headingtobesend);
        btnsend=findViewById(R.id.send_msg);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        dpt_name =  prf.getString("dpt_key","");
        uname =  prf.getString("name_key","");


        //date
        Date strDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a E~dd_MMM_yyyy");
        date = formatter.format(strDate);

        //onclick

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headmesg = headingofmsg.getText().toString().toUpperCase().trim();
                String editmsg = msgtosend.getText().toString().trim();
                if (headmesg.isEmpty()){
                    headingofmsg.setError("It can't be empty");
                    headingofmsg.setFocusable(true);
                }
                if(!editmsg.isEmpty()){
                    //send msg
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    messages_gone meeting= new messages_gone(dpt_name,uname,date,headmesg+'~'+editmsg);
                    mDatabaseReference = mDatabase.getReference("Admin_Msg").child(date);
                    mDatabaseReference.setValue(meeting);
               //    query.addListenerForSingleValueEvent(valueEventListener);
               //     query1.addListenerForSingleValueEvent(valueEventListener);

                startActivity(new Intent(MessageActivity.this,MessageActivity.class));
                    finish();

                }
                else {
                    msgtosend.setError("It can't be empty");
                    msgtosend.setFocusable(true);
                    //Toast.makeText(MessageActivity.this,"Message can't be Empty!!",Toast.LENGTH_SHORT).show();
                }

                headingofmsg.setText("");
                msgtosend.setText("");

            }
        });



    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
           // messages_goneList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    messages_gone messages_gones = snapshot.getValue(messages_gone.class);
                    messages_goneList.add(messages_gones);
                }
                adapter.notifyDataSetChanged();
            }
            else{
                getBackground();
                Toast.makeText(MessageActivity.this, "data not exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        messages_goneList.clear();
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
