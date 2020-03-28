package com.example.manishauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MeetingActivity extends AppCompatActivity{
    RecyclerView recyclerView;

    MeetingAdapter adapter;
    List<Meeting> MeetingList;
    DatabaseReference dbmeetings;
    SharedPreferences prf;
    String dpt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        dpt_name =  prf.getString("dpt_key","");

        MeetingList=new ArrayList<>();
        recyclerView =(RecyclerView)findViewById(R.id.recycleviewer);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MeetingList = new ArrayList<>();
        adapter = new MeetingAdapter(this,MeetingList);

        //onclick recycleview
        adapter.setOnItemClickListener(new RecycleviewOnclick() {
            @Override
            public void onItemClick(View view, int position) {
                final String meetingtimeconculsion = MeetingList.get(position).getTime();

                //2
                final Dialog dialog = new Dialog(MeetingActivity.this);
                dialog.setContentView(R.layout.viewdailog_layout);
                final TextView viewConclusion = dialog.findViewById(R.id.conclusion1);
                Button BackButton = (Button) dialog.findViewById(R.id.submitbtn);
                dbmeetings.child(meetingtimeconculsion).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String convalue=dataSnapshot.child("conclusion").getValue().toString();
                        viewConclusion.setText(convalue);
                        //Toast.makeText(MeetingActivity.this,convalue,Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.show();
                BackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        recyclerView.setAdapter(adapter);
        dbmeetings= FirebaseDatabase.getInstance().getReference("Meetings");
        if((dpt_name.equals( "CP" ) | (dpt_name.equals( "VCP" )))){
            dbmeetings.addListenerForSingleValueEvent(valueEventListener);
        }
        else {
            final Query query = dbmeetings.orderByChild( "department" ).equalTo( "ADMIN" );
            query.addListenerForSingleValueEvent( valueEventListener );


            try {
                if (dpt_name.equals( "" )) {
                    Toast.makeText( this, "Please Go to edit profile and select your Role/Department", Toast.LENGTH_SHORT ).show();
                } else {

                    final Query query1 = dbmeetings.orderByChild( "department" ).equalTo( dpt_name );
                    query1.addListenerForSingleValueEvent( valueEventListener );


                }
            } catch (Error error) {
                Toast.makeText( this, "" + error, Toast.LENGTH_SHORT ).show();
            }

        }
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Meeting meets = snapshot.getValue(Meeting.class);
                    MeetingList.add(meets);
                }
                adapter.notifyDataSetChanged();
            }
            else {
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText( MeetingActivity.this, ""+databaseError, Toast.LENGTH_SHORT ).show();

        }
    };

    @Override
    public void onBackPressed() {
        MeetingList.clear();
        startActivity(new Intent(MeetingActivity.this, UserActivity.class));
        finish();
    }
}
