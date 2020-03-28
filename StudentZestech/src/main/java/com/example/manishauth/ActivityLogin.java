package com.example.manishauth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {

    private TextView registerBtn,forgotpasswordBtn;
    CheckBox checkbox;
    private EditText emailTV, passwordTV;
    private Button loginBtn;
    private ProgressBar progressBar;
    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // header
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true); */
        builder = new AlertDialog.Builder(this);

        mAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.register);
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        forgotpasswordBtn= findViewById(R.id.forgetpassword);
        loginBtn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
        checkbox = (CheckBox) findViewById(R.id.checkbox);



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

        forgotpasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email;
                email = emailTV.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailTV.setError("Invalid Email");
                    emailTV.setFocusable(true);
                }
                else {

                    builder.setMessage("Do you want to reset the password of "+email+" ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //firebase forgot password
                                    mAuth.sendPasswordResetEmail(email) .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Check email "+email+" to reset your password!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("AlertDialogExample");
                    alert.show();

                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, RigesterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailTV.getText().toString().trim();
                password = passwordTV.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailTV.setError("Invalid Email");
                    emailTV.setFocusable(true);
                }
                else if(password.length()<6){
                    passwordTV.setError("password length atleast 6 character");
                    passwordTV.setFocusable(true);
                }
                else{
                    loginUser(email,password);
                }

            }
        });
        pd=new ProgressDialog(this);
        pd.setMessage("Logging In....");
    }

    private void loginUser(String email, String password) {
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(ActivityLogin.this, UserActivity.class));
                            finish();

                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ActivityLogin.this, "Authentication failed \n try after sometime.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}