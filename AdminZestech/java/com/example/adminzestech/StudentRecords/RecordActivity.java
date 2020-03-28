package com.example.adminzestech.StudentRecords;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzestech.MeetingList1.RecycleviewOnclick;
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

public class RecordActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    UsermeetingAdapter adapter;
    SharedPreferences prf;
    String dpt_name;
    RelativeLayout ifnodataavail;
    List<userinmeeting> userinmeetingList;
    DatabaseReference dbuser;
    RelativeLayout searchlayout, defualtlayout;
    ImageButton searchbtn,cancelbtn;
    EditText nametosearchet;
    ArrayList<userinmeeting> filteredlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        dpt_name =  prf.getString("dpt_key","");
        searchlayout=findViewById(R.id.hidewhennotsearch);
        defualtlayout=findViewById(R.id.hidewhensearch);
        searchbtn=findViewById(R.id.seacrhstd);
        cancelbtn=findViewById(R.id.canclebtn);
        nametosearchet=findViewById(R.id.nametosrch);
        userinmeetingList=new ArrayList<>();
        recyclerView =(RecyclerView)findViewById(R.id.recycleviewer);
        recyclerView.setHasFixedSize(true);
        ifnodataavail = findViewById(R.id.nodataavailable);
        //search start
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defualtlayout.setVisibility(View.GONE);
                searchlayout.setVisibility(View.VISIBLE);
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nametosearchet.setText("");
                defualtlayout.setVisibility(View.VISIBLE);
                searchlayout.setVisibility(View.GONE);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        nametosearchet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });



        //search ends


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userinmeetingList = new ArrayList<>();
        adapter = new UsermeetingAdapter(this,userinmeetingList);
        //onclick student name

        recyclerView.setAdapter(adapter);

        dbuser= FirebaseDatabase.getInstance().getReference("Users");

        // query
        if( dpt_name.equals("ADMIN")){
            admin();
        }
        else{
            other(dpt_name);
        }
    adapter.setOnItemClickListener(new RecycleviewOnclick1() {
        @Override
        public void onItemClick(View view, String stdname,String uid) {
            Intent myIntent = new Intent(RecordActivity.this, Stdmeeting.class);
            myIntent.putExtra("Stdnamekey",stdname);
            myIntent.putExtra("StdUid",uid);
            filteredlist.clear();
            startActivity(myIntent);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    });




    }

    private void filter(String text) {
        filteredlist.clear();
        for (userinmeeting item :userinmeetingList){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(item);
            }
        }
        adapter.filterlist(filteredlist);
    }

    public void admin(){
        dbuser.addListenerForSingleValueEvent(valueEventListener);
    }

    public void other(String otherdpt){
        Query query = dbuser.orderByChild("department").equalTo(otherdpt);
        query.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userinmeetingList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userinmeeting usermeets = snapshot.getValue(userinmeeting.class);
                    userinmeetingList.add(usermeets);
                }
                adapter.notifyDataSetChanged();
            }
            else{
                getBackground();
                Toast.makeText(RecordActivity.this, "data not exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


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


