package com.example.adminzestech.MeetingList1;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzestech.MainActivity;
import com.example.adminzestech.Meeting;
import com.example.adminzestech.R;
import com.example.adminzestech.meetingstdattend.Stdmeeting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MeetingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MeetingAdapter adapter;
    List<meetingdone> meetingdoneList;
    DatabaseReference dbuser;
    Button btn;
    RelativeLayout ifnodataavail;
    SharedPreferences prf;
    String dpt_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        ifnodataavail = findViewById(R.id.nodataavailable);
        meetingdoneList=new ArrayList<>();
        recyclerView =(RecyclerView)findViewById(R.id.recycleviewer);
        recyclerView.setHasFixedSize(true);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        dpt_name =  prf.getString("dpt_key","");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        meetingdoneList = new ArrayList<>();
        adapter = new MeetingAdapter(this,meetingdoneList);
        //1
        //Create custom interface object and send it to adapter
        //Adapter trigger it when any item view is clicked
        adapter.setOnItemClickListener(new RecycleviewOnclick() {
            @Override
            public void onItemClick(View view, int position) {
                String meetingdepartment = meetingdoneList.get(position).getDepartment();
                String meetingtime = meetingdoneList.get(position).getTime();
                String meetingagenda=meetingdoneList.get(position).getAgenda();
                Intent myIntent = new Intent(MeetingActivity.this, StudentMarkedActivity.class);
                myIntent.putExtra("Meeting-Time",meetingtime);
                myIntent.putExtra("MeetingAgenda",meetingagenda);
                myIntent.putExtra("Meeting-Dpt",meetingdepartment);
                startActivity(myIntent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                //1
                final String meetingtimeconculsion = meetingdoneList.get(position).getTime();

                //2
                final Dialog dialog = new Dialog(MeetingActivity.this);
                dialog.setContentView(R.layout.editdailog_layout);
                final EditText editConclusion = dialog.findViewById(R.id.conclusion1);
                Button Cancelbtn = (Button) dialog.findViewById(R.id.canclebtn);
                Button SubmitButton = (Button) dialog.findViewById(R.id.submitbtn);
                dbuser.child(meetingtimeconculsion).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String convalue=dataSnapshot.child("conclusion").getValue().toString();
                        editConclusion.setText(convalue);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.show();
                SubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       //get Value

                        //set Value
                       String conclusiondata= editConclusion.getText().toString();
                        dbuser.child(meetingtimeconculsion).child("conclusion").setValue(conclusiondata);
                        dialog.dismiss();
                        Toast.makeText(MeetingActivity.this,"Data Inserted",Toast.LENGTH_SHORT).show();
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
        //2
        recyclerView.setAdapter(adapter);

        dbuser= FirebaseDatabase.getInstance().getReference("Meetings");

        if(dpt_name.equals("ADMIN")){
            dbuser.addListenerForSingleValueEvent(valueEventListener);
        }
        else{
            Query query = dbuser.orderByChild("department").equalTo(dpt_name);
            query.addListenerForSingleValueEvent(valueEventListener);
        }

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            meetingdoneList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    meetingdone usermeets = snapshot.getValue(meetingdone.class);
                    meetingdoneList.add(usermeets);
                }
                adapter.notifyDataSetChanged();
            }
            else{

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
