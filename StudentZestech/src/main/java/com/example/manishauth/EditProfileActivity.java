package com.example.manishauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;
    ImageView profilepictv;
    TextView  classTV, rollnoTv,phoneTv,nameTv,grnoTv,dobTv,roleTv;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    TextView getstd;
    RadioGroup Role;
    RadioButton Role1,Role2;
    private RadioGroup stdroleGroup;
    private RadioButton radiocoreButton;
    StorageReference storageReference;
    String[] core = {"VCP", "CP"};
    String[] concil = {"SPONSORSHIP", "MARKETING", "CREATIVE/LOGISTIC", "REFRESHMENT/CERTIFICATE ", "PR/SSM", "PHOTOGRAPHY","TECHNICAL/GRAPHICS","SECURITY/MEMBERS", "SPORTS"};
    SharedPreferences pref;
    String profilepic;
    String[] std = {"FYIT-A","FYIT-B", "FYCS-A", "FYCS-B","SYIT-A","SYIT-B", "SYCS-A","SYCS-A", "TYIT-A","TYIT-B","TYCS-A","TYCS-B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
       // CORES= findViewById(R.id.role1);
        getstd= findViewById(R.id.setstd);
        profilepictv = (ImageView) findViewById(R.id.profilepicTv);
        nameTv=(TextView)findViewById(R.id.name);
        grnoTv=(TextView)findViewById(R.id.grno);
        dobTv=(TextView)findViewById(R.id.dob);

        // data can be edited
      //  choosebtn=findViewById(R.id.choose);
     //   uploadbtn=findViewById(R.id.upload);
        classTV = (TextView) findViewById(R.id.std);
        rollnoTv = (TextView) findViewById(R.id.rollno);
        phoneTv = (TextView) findViewById(R.id.phoneno);
        roleTv=findViewById(R.id.stdrole);




        pref = getSharedPreferences("user_details",MODE_PRIVATE);


        //spinner
       /* final Spinner spin = (Spinner) findViewById(R.id.role1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, core);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
*/
        String email = user.getEmail();


        //role
      /*  Role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = stdroleGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radiocoreButton = (RadioButton) findViewById(selectedId);
                String stdrole = radiocoreButton.getText().toString();
                if(stdrole.equals("CORE")){
                    spin.setVisibility(Integer.parseInt("visible"));
                }
            }
        }); */
        //user is signed in
        // get the details from firebase checking with email
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name=""+ds.child("name").getValue();
                    String dob=""+ds.child("dob").getValue();
                    String std = "" + ds.child("std").getValue();
                    String rollno = "" + ds.child("rollno").getValue();
                    String phone= "" +ds.child("phoneno").getValue();
                    String grno=""+ds.child("grno").getValue();

                    String studentrole = "" + ds.child("department").getValue();
                    //set data
                    classTV.setText(std);
                    rollnoTv.setText(rollno);
                    phoneTv.setText(phone);
                    nameTv.setText(name);
                    grnoTv.setText(grno);
                    dobTv.setText(dob);
                    roleTv.setText(studentrole);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//upload image and choose




        //data should be edited
        classTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDaildog(view,"stdclass");

            }
        });
        rollnoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDaildog(view,"rollno");
            }
        });
        phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDaildog(view,"phoneno");
            }
        });

        //to select the role of std
       roleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setTitle("Select Your Role");
                dialog.setContentView(R.layout.roledailog);
                dialog.show();
                Button corebtn = dialog.findViewById(R.id.corebtn);
                Button councilbtn = dialog.findViewById(R.id.councilbtn);
                Button Cancelbtn = (Button) dialog.findViewById(R.id.backbtn);
                // to select from Core
                corebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(EditProfileActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.studentrole_dailogbox,null);
                        mBuilder.setTitle("Select the Role");
                        final Spinner mSpinner = (Spinner)mView.findViewById(R.id.spinnerRole);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this,android.R.layout.simple_spinner_item,core);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinner.setAdapter(adapter);

                        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String stdRole=mSpinner.getSelectedItem().toString();
                                updateStudentRole(stdRole);
                                Toast.makeText(EditProfileActivity.this,mSpinner.getSelectedItem().toString()+" Updated",Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                dialog.dismiss();
                            }
                        });
                        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        mBuilder.setView(mView);
                        AlertDialog dialog=mBuilder.create();
                        dialog.show();

                    }
                });



                //to select from Council
                councilbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(EditProfileActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.studentrole_dailogbox,null);
                        mBuilder.setTitle("Select the Role");
                        final Spinner mSpinner = (Spinner)mView.findViewById(R.id.spinnerRole);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this,android.R.layout.simple_spinner_item,concil);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinner.setAdapter(adapter);

                        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String stdRole=mSpinner.getSelectedItem().toString();
                                updateStudentRole(stdRole);
                                Toast.makeText(EditProfileActivity.this,mSpinner.getSelectedItem().toString()+" Updated ",Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                                dialog.dismiss();
                            }
                        });
                        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        mBuilder.setView(mView);
                        AlertDialog dialog=mBuilder.create();
                        dialog.show();

                    }
                });
                //to Save data
                //to cancel the dailog
                Cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });


    }
    public void updateStudentRole(String StdRole){
        String uid = user.getUid();
        databaseReference.child(uid).child("department").setValue(StdRole);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("dpt_key",StdRole);
        editor.commit();

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilepictv.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null){
            pref = getSharedPreferences("user_details",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("profilepic_key", String.valueOf( filePath ) );
            editor.commit();
            Bitmap bm = BitmapFactory.decodeFile( String.valueOf( filePath ) );
            profilepictv.setImageBitmap(bm);
        }
        else {
            Toast.makeText(EditProfileActivity.this,"Choose image then upload",Toast.LENGTH_SHORT).show();
        }

    }

    public void editDaildog(View edView, final String btnclick){
        // Create custom dialog object
        if(btnclick.equals( "stdclass" )){
            final Dialog dialog = new Dialog(EditProfileActivity.this);
            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(EditProfileActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.studentrole_dailogbox,null);
            mBuilder.setTitle("Select the Class");
            final Spinner mSpinner = (Spinner)mView.findViewById(R.id.spinnerRole);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this,android.R.layout.simple_spinner_item,std);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String stdclass=mSpinner.getSelectedItem().toString();
                    updateStudentclass(stdclass);
                    dialogInterface.dismiss();
                    dialog.dismiss();
                }
            });
            mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            mBuilder.setView(mView);
            AlertDialog dialog1=mBuilder.create();
            dialog1.show();

        }
        else if(btnclick=="rollno"){
            updateroll( );

        }
        else if(btnclick=="phoneno") {
            updatephone();
        }
        else {
            Toast.makeText( this, "something went wrong", Toast.LENGTH_SHORT ).show();
        }

    }

    private void updateStudentclass(String stdclass) {
        String uid = user.getUid();
        if(!stdclass.isEmpty()){
                databaseReference.child(uid).child("std").setValue(stdclass);

                Toast.makeText(EditProfileActivity.this, stdclass + " Updated", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(EditProfileActivity.this, "Failed to Updated", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String standard = core[i];
        getstd.setText(standard);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditProfileActivity.this, UserActivity.class));
        finish();
    }


    public void updateroll(){
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.setTitle("Edit Dialog");
        dialog.setContentView(R.layout.editdailog_layout);
        dialog.show();
        // set values for custom dialog components - text, image and button
        Button Cancelbtn = (Button) dialog.findViewById(R.id.canclebtn);
        Button SubmitButton = (Button) dialog.findViewById(R.id.submitbtn);
        // if SubmitButton is clicked
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit in firrebase
                String uid = user.getUid();

                final EditText editData = (EditText) dialog.findViewById(R.id.edituserprofile);
                final String xvar = editData.getText().toString();
                if(!xvar.isEmpty()){
                        databaseReference.child(uid).child("rollno").setValue(xvar);
                        Toast.makeText(EditProfileActivity.this, "rollno Updated", Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(EditProfileActivity.this, "Failed to Updated", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();

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
    public void updatephone(){
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.setTitle("Edit Dialog");
        dialog.setContentView(R.layout.phone_dailoag);
        dialog.show();
        // set values for custom dialog components - text, image and button
        Button Cancelbtn = (Button) dialog.findViewById(R.id.canclebtn);
        Button SubmitButton = (Button) dialog.findViewById(R.id.submitbtn);
        // if SubmitButton is clicked
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit in firrebase
                String uid = user.getUid();

                final EditText editData = (EditText) dialog.findViewById(R.id.edituserprofile);
                final String xvar = editData.getText().toString();
                if(!xvar.isEmpty()){


                    if(!xvar.matches("^[6-9][0-9]{9}$")){
                        Toast.makeText( EditProfileActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                        editData.setError("invalid phone no");
                        editData.setFocusable(true);
                    }
                    else {
                        databaseReference.child(uid).child("phoneno").setValue(xvar);
                        Toast.makeText(EditProfileActivity.this, "phoneno Updated", Toast.LENGTH_SHORT).show();
                    }

                }

                else {
                    Toast.makeText(EditProfileActivity.this, "Failed to Updated", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();

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
}
