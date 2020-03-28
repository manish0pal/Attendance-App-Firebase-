package com.example.adminzestech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText username,password,name;
    CheckBox showpassword;
    ImageButton btnsignin;
    SharedPreferences pref;
    DatabaseReference dbuser;
    ProgressDialog loginprogress;
    Spinner mSpinner;
    String dptnamestr,Uname,Pword,Namestr;
    String[] department = {"ADMIN","SPONSORSHIP", "MARKETING", "CREATIVE/LOGISTIC", "REFRESHMENT/CERTIFICATE ", "PR/SSM", "PHOTOGRAPHY","TECHNICAL/GRAPHICS","SECURITY/MEMBERS", "SPORTS"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name= findViewById(R.id.nameEt);
        password=findViewById(R.id.passwordEt);
        username=findViewById(R.id.usernameEt);
        showpassword=findViewById(R.id.checkbox);
        btnsignin=findViewById(R.id.signinIb);

        //firebase
        dbuser= FirebaseDatabase.getInstance().getReference();
        loginprogress=new ProgressDialog(this);
        loginprogress.setMessage("Logging In....");

        //dropdown list

        mSpinner = (Spinner)findViewById(R.id.spindepartmet);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this,R.layout.spinner_item,department);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);


        // ddl ends



        //1
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        if(pref.contains("username") && pref.contains("password")) {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    // hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginprogress.show();
                Namestr = name.getText().toString();
                Uname = username.getText().toString();
                Pword = password.getText().toString();

            if(Namestr.isEmpty()){
                loginprogress.dismiss();
                    name.setError("Enter the name");
                    name.setFocusable(true);
                }
                else if(!Namestr.matches("([A-Za-z]*[ '-][a-zA-Z]+)*")){
                loginprogress.dismiss();
                    name.setError("FirstName LastName");
                    name.setFocusable(true);
                }
                else {

                    if (Uname.equals("admin")) {
                        dbuser.child("AdminPassword").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String pwd = dataSnapshot.getValue(String.class);
                                if (Pword.equals(pwd)) {
                                    //save into sqlitedatabase
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("username", Uname);
                                    editor.putString("password", Pword);
                                    editor.putString("name_key",Namestr);
                                    editor.putString("dpt_key","ADMIN");
                                    editor.commit();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    loginprogress.dismiss();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    loginprogress.dismiss();
                                    password.setError("Invalid Password");
                                    password.setFocusable(true);
                                    // Toast.makeText(LoginActivity.this,"Password not match",Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                loginprogress.dismiss();
                                Toast.makeText(LoginActivity.this, "" + databaseError, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (Uname.equals("zestech")) {
                        if (Pword.equals("zestech")) {
                            loginprogress.dismiss();
                            //others
                            if (dptnamestr.equals("ADMIN")) {
                                username.setError("Invalid username");
                                username.setFocusable(true);
                                Toast.makeText(LoginActivity.this, "Or change the Department", Toast.LENGTH_SHORT).show();
                            } else {
                                //save into sqlitedatabase
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("username", Uname);
                                editor.putString("password", Pword);
                                editor.putString("name_key",Namestr);
                                editor.putString("dpt_key",dptnamestr);
                                editor.commit();


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else {
                        loginprogress.dismiss();
                        username.setError("Invalid username");
                        username.setFocusable(true);
                        //Toast.makeText(LoginActivity.this,"Invalid Username",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
         dptnamestr = mSpinner.getItemAtPosition(position).toString();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
