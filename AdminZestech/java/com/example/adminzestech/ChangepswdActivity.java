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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangepswdActivity extends AppCompatActivity {
    SharedPreferences pref;
    TextInputEditText curentpwd,newpwd,cnfpwd;
    DatabaseReference dbuser,demoref;
    Button submit;
    CheckBox showpassword;
    SharedPreferences prf;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepswd);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        dbuser= FirebaseDatabase.getInstance().getReference();
        demoref = dbuser.child("AdminPassword");

        curentpwd=findViewById(R.id.pwd1);
        newpwd=findViewById(R.id.pwd2);
        cnfpwd=findViewById(R.id.pwd3);
        submit=findViewById(R.id.changepwd);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..... ");
       // showpassword=findViewById(R.id.checkbox);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);


       /* showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    // show password
                    newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    // hide password
                    newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });  */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                dbuser.child("AdminPassword").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pwd = dataSnapshot.getValue(String.class);
                        //12
                        String password1,password2,password3;
                        password1 = curentpwd.getText().toString().trim();
                        password2 =newpwd.getText().toString().trim();
                        password3 =cnfpwd.getText().toString().trim();
                        if(!password1.equals(pwd)){
                            progressDialog.dismiss();
                            curentpwd.setError("password not match");
                            curentpwd.setFocusable(true);
                        }
                        else if(password2.length()<4){
                            progressDialog.dismiss();
                            newpwd.setError("more then 4 word");
                            newpwd.setFocusable(true);
                        }

                        else if(!password2.equals(password3)){
                            progressDialog.dismiss();
                            cnfpwd.setError("password not match");
                            cnfpwd.setFocusable(true);
                        }
                        else {
                            demoref.setValue(password3);
                            progressDialog.dismiss();
                            Toast.makeText(ChangepswdActivity.this,"Password Changed",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangepswdActivity.this,LoginActivity.class));
                            SharedPreferences.Editor editor = prf.edit();
                            editor.clear();
                            editor.commit();
                            Intent I = new Intent(ChangepswdActivity.this, LoginActivity.class);
                            startActivity(I);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ChangepswdActivity.this,""+databaseError,Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

    }
}
