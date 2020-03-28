package com.example.manishauth.MessageToAll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manishauth.AboutActivity;
import com.example.manishauth.R;
import com.example.manishauth.UserActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MessageAdapter adapter;
    DatabaseReference adminmsg;
    List<messages_gone> messages_goneList;
    EditText msgtosend;
    SharedPreferences prf;
    String dpt_name;
    Button adminbtn,departmentbtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meesage);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        dpt_name =  prf.getString("dpt_key","");

        adminbtn = findViewById( R.id.adminbtn );
        departmentbtn = findViewById( R.id.departmentbtn );
        messages_goneList= new ArrayList<>();
        recyclerView =(RecyclerView)findViewById(R.id.adminmsg);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messages_goneList = new ArrayList<>();
        adapter = new MessageAdapter(this,messages_goneList);
        recyclerView.setAdapter(adapter);
        //admin & department message
        adminmsg= FirebaseDatabase.getInstance().getReference("Admin_Msg");
        if((dpt_name.equals( "CP" ) | (dpt_name.equals( "VCP" )))){
            adminbtn.setVisibility( View.GONE );
            adminbtn.setVisibility( View.GONE );
            adminmsg.addListenerForSingleValueEvent(valueEventListener);
        }
        else {
            final Query query = adminmsg.orderByChild( "department" ).equalTo( "ADMIN" );
            query.addListenerForSingleValueEvent( valueEventListener );
            adminbtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adminbtn.setBackgroundResource( R.drawable.blue_btn_msg );
                    departmentbtn.setBackgroundResource( R.drawable.button_round);
                    adminbtn.setTextColor( Color.WHITE );
                    departmentbtn.setTextColor( Color.BLACK );
                    messages_goneList.clear();
                    adapter.notifyDataSetChanged();
                    final Query query = adminmsg.orderByChild( "department" ).equalTo( "ADMIN" );
                    query.addListenerForSingleValueEvent( valueEventListener );
                }
            } );
            try {
                if (dpt_name.equals( "" )) {
                    Toast.makeText( this, "Please Go to edit profile and select your Role/Department", Toast.LENGTH_SHORT ).show();
                } else {
                    departmentbtn.setText( dpt_name );
                    departmentbtn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adminbtn.setBackgroundResource( R.drawable.button_round );
                            departmentbtn.setBackgroundResource( R.drawable.blue_btn_msg);
                            adminbtn.setTextColor( Color.BLACK );
                            departmentbtn.setTextColor( Color.WHITE );
                            messages_goneList.clear();
                            adapter.notifyDataSetChanged();
                            final Query query1 = adminmsg.orderByChild( "department" ).equalTo( dpt_name );
                            query1.addListenerForSingleValueEvent( valueEventListener );
                        }
                    } );
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
                messages_goneList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    messages_gone messages_gones = snapshot.getValue(messages_gone.class);
                    messages_goneList.add(messages_gones);
                }
                adapter.notifyDataSetChanged();
            }
            else {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText( MessageActivity.this, ""+databaseError.toString(), Toast.LENGTH_SHORT ).show();
        }
    };
    @Override
    public void onBackPressed() {
        messages_goneList.clear();
        startActivity(new Intent( MessageActivity.this, UserActivity.class));
        finish();
    }
    protected void onStop() {
        messages_goneList.clear();
        super.onStop();
    }
}
