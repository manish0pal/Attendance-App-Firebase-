package com.example.manishauth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RigesterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText emailTV, passwordTV,name,dob,phone,grno,rollno,cnfirmpassword;
    TextView getstd;
    Spinner stdS;
    String[] std = {"FYIT-A","FYIT-B", "FYCS-A", "FYCS-B","SYIT-A","SYIT-B", "SYCS-A","SYCS-A", "TYIT-A","TYIT-B","TYCS-A","TYCS-B"};
    CheckBox checkbox;
    Button regBtn;
    ProgressBar progressBar;
    ProgressDialog progressDialog;


    private FirebaseAuth mAuth;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mDatabaseReference = mDatabase.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigester);
        getstd= findViewById(R.id.setstd);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        phone = findViewById(R.id.phoneno);
       // std = findViewById(R.id.std);
        grno = findViewById(R.id.grno);
        rollno = findViewById(R.id.rollno);
        cnfirmpassword = findViewById(R.id.cnfpassword);
        checkbox = (CheckBox) findViewById(R.id.checkbox);


        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        regBtn = findViewById(R.id.register);
        stdS = findViewById(R.id.dropdownclass);
        progressBar = findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User.....");

        mAuth = FirebaseAuth.getInstance();
//spinner
        Spinner spin = (Spinner) findViewById(R.id.dropdownclass);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, std);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);


//show password

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    passwordTV.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    passwordTV.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                //registerNewUser();

                //mDatabaseReference = mDatabase.getReference().child("name");
                //mDatabaseReference.setValue("Donald Duck");

                String email = emailTV.getText().toString().trim();
                String password = passwordTV.getText().toString().trim();
                String cnfpassword = cnfirmpassword.getText().toString().trim();
                String std = getstd.getText().toString();
                String name1 = name.getText().toString().trim().toUpperCase();
                String dob1 =dob.getText().toString().trim();
                String phone1 =phone.getText().toString().trim();
                String grno1 = grno.getText().toString().trim();
                String rollno1= rollno.getText().toString().trim();
                SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                sdfrmt.setLenient(false);
                int checkdate;
                try
                {
                    Date javaDate = sdfrmt.parse(dob1);
                    checkdate = 1;
                }
                /* Date format is invalid */
                catch (ParseException e)
                {
                    checkdate = 0;
                }

                if(!name1.matches("([A-Za-z]*[ '-][a-zA-Z]+)*")){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    name.setError("Firstname Lastname");
                    name.setFocusable(true);
                }
                else if(name1.isEmpty()){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    name.setError("Enter the name");
                    name.setFocusable(true);
                }
                else if(grno1.isEmpty()){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    grno.setError("Enter the grno. ");
                    grno.setFocusable(true);
                }
                else if(rollno1.isEmpty()){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    rollno.setError("Enter the roll no");
                    rollno.setFocusable(true);
                }
                //phone
                else if(phone1.isEmpty()){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    phone.setError("Enter the phone no.");
                    phone.setFocusable(true);
                }
                else if(!phone1.matches("^[6-9][0-9]{9}$")){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    phone.setError("invalid phone no");
                    phone.setFocusable(true);
                }

//dob
                else if(checkdate==0){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    dob.setError("dd/MM/YYYY");
                    dob.setFocusable(true);
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText( RigesterActivity.this, "Given Details are incorrect", Toast.LENGTH_SHORT ).show();
                    emailTV.setError("Invalid Email");
                    emailTV.setFocusable(true);
                }
                else if(password.length()<6){
                    passwordTV.setError("password length atleast 6 character");
                    passwordTV.setFocusable(true);
                }
                else if(!cnfpassword.equals(password)){
                    cnfirmpassword.setError("password not match");
                    cnfirmpassword.setFocusable(true);

                }
                else{
                   registerUser(email,password,name1,std,grno1,dob1,rollno1,phone1);
                }



            }
        });

    }
//stdgetvariable
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(), "Selected Class: " + std[position], Toast.LENGTH_SHORT).show();
        String standard = std[position];
        getstd.setText(standard);

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
//sppinerend
    private void registerUser(String email, String password, final String name, final String std, final String grno, final String dob, final String rollno, final String phone) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RigesterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid=user.getUid();
                            User user1 = new User(name,std,grno,rollno,phone,dob,email,uid);
                            mDatabaseReference = mDatabase.getReference("Users").child(uid);
                            mDatabaseReference.setValue(user1);
                            startActivity(new Intent(RigesterActivity.this, UserActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RigesterActivity.this, "Failed!  Check your Internet", Toast.LENGTH_SHORT).show();


                        }


                    }
                    /*  });
                     if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Registered ...\n"+user.getEmail(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RigesterActivity.this,UserActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user
                            progressDialog.dismiss();
                            Toast.makeText(RigesterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }


                    }  */
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RigesterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onBackPressed() {
        startActivity(new Intent(RigesterActivity.this, ActivityLogin.class));
        finish();
    }


}