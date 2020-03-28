package com.example.adminzestech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class GenmeetingActivity extends AppCompatActivity {
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    TextView datetv, counttime;
    EditText venue, agendaet;
    ImageView iv;
    Button btn;
    Random Number;
    int Rnumber, counter;
    public ProgressDialog progressDialog;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mDatabase.getReference();
    private Handler mHandler = new Handler();
    String date;
    LinearLayout Form;
    SharedPreferences prf;
    String dpt_name, uname;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_genmeeting );
        datetv = (TextView) findViewById( R.id.dataandtime );
        iv = (ImageView) findViewById( R.id.iv );
        venue = (EditText) findViewById( R.id.venue );
        agendaet = (EditText) findViewById( R.id.agenda );
        btn = (Button) findViewById( R.id.btn );
        counttime = findViewById( R.id.countertext );
        Form = findViewById( R.id.form );
        //01:27 Fri 06/12/2019
        Date strDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat( "hh:mm a E~dd_MMM_yyyy" );
        date = formatter.format( strDate );
        //date = DateFormat.getDateTimeInstance().format(new Date());
        datetv.setText( date );
        //getdata from sqllite
        prf = getSharedPreferences( "user_details", MODE_PRIVATE );
        dpt_name = prf.getString( "dpt_key", "" );
        uname = prf.getString( "name_key", "" );

        tts = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage( Locale.UK );
            }
        } );
        progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "Please Wait..... " );


        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 23/9/19
                // progressDialog.show();
                // Jul 30, 2019 8:25:01 AM
                InputMethodManager in = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
                in.hideSoftInputFromWindow( v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
                final String venue1 = venue.getText().toString().trim();
                String agenda1 = agendaet.getText().toString().trim();
                if (venue1.isEmpty()) {
                    venue.setError( "Enter the venue" );
                    venue.setFocusable( true );
                    //   progressDialog.dismiss();
                } else if (agenda1.isEmpty()) {
                    agendaet.setError( "Enter the agenda" );
                    agendaet.setFocusable( true );
                    //  progressDialog.dismiss();
                } else {
                    // invisible agenda edit tex
                    Form.setVisibility( View.GONE );
                    Toast.makeText( GenmeetingActivity.this, "Please Wait....", Toast.LENGTH_SHORT ).show();
                    Number = new Random();
                    Rnumber = Number.nextInt( 999 );
                    final String code = Integer.toString( Rnumber );


                    String etqr = String.format( "{'Time':' %s ','Code': %s}", date, code );
                    try {
                        //insert into data base
                        Meeting meeting = new Meeting( date, code, venue1, "" + agenda1, "", uname, dpt_name );
                        mDatabaseReference = mDatabase.getReference( "Meetings" ).child( date );
                        mDatabaseReference.setValue( meeting );
                        //venue.setText(Integer.toString(Rnumber));
                        // String date = DateFormat.getDateTimeInstance().format(new Date());
                        bitmap = TextToImageEncode( etqr );
                        iv.setImageBitmap( bitmap );
                        //1
                        tts.speak( "Qr code will be disable in 2 minute's", TextToSpeech.QUEUE_FLUSH, null );
                        //  progressDialog.dismiss();


                        // Toast.makeText(GenmeetingActivity.this, "QR code is Generated", Toast.LENGTH_SHORT).show();
                        try {
                            counter = 120;
                            new CountDownTimer( 120000, 1000 ) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    counttime.setText( String.valueOf( counter ) + " sec" );
                                    counter--;
                                }

                                @Override
                                public void onFinish() {
                                    mDatabase.getReference( "Meetings/" + date ).child( "code" ).setValue( null );
                                    Toast.makeText( GenmeetingActivity.this, "QR-code disabled", Toast.LENGTH_SHORT ).show();
                                    tts.speak( "QR code disabled ", TextToSpeech.QUEUE_FLUSH, null );
                                    counttime.setText( "Qr-code Disabled" );
                                }
                            }.start();
                        } catch (Error error) {

                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        } );


    }


    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get( x, y ) ?
                        getResources().getColor( R.color.black ) : getResources().getColor( R.color.white );
            }
        }
        Bitmap bitmap = Bitmap.createBitmap( bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444 );

        bitmap.setPixels( pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight );
        return bitmap;

    }


}