package com.example.manishauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AboutActivity extends AppCompatActivity {
    EditText message;
    Button Click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        message = (EditText) findViewById(R.id.msgtodev);
        Click = (Button)findViewById(R.id.mailtome);

        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgtodev = message.getText().toString().trim();
                sendMail(msgtodev);
            }
        });

    }

    private void sendMail(String message) {
        Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
        mEmailIntent.setData(Uri.parse("mailto:"));
        mEmailIntent.setType("text/plane");
        String aEmailList[] = {"manish1999pal@gmail.com"};
        mEmailIntent.putExtra(Intent.EXTRA_EMAIL,aEmailList);
        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT,"Zestech APP Feedback");
        mEmailIntent.putExtra(Intent.EXTRA_TEXT,message);

        try {
            //send mail
            startActivity(Intent.createChooser(mEmailIntent,"Choose Email"));
        }
        catch (Exception e){
            Toast.makeText(AboutActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutActivity.this, Stdmeeting.class));
        finish();
    }
}
