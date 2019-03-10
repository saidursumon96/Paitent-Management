package com.jarvis.patientmanagement.users;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jarvis.patientmanagement.R;

import dmax.dialog.SpotsDialog;

public class Email_Verify_D extends AppCompatActivity {

    private static final String TAG = "Email_Verify_D";
    RelativeLayout confirm_verify_d, send_link_again_d;
    private SpotsDialog progressDialog;
    private String email, password;
    private FirebaseAuth mAuth;
    TextView time_d, email_show;
    private CountDownTimer countDownTimer;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email__verify__d);

        confirm_verify_d = (RelativeLayout)findViewById(R.id.confirm_verify_d);
        send_link_again_d = (RelativeLayout)findViewById(R.id.send_link_again_d);
        time_d = (TextView)findViewById(R.id.time_d);
        email_show = (TextView)findViewById(R.id.email_show);

        mAuth = FirebaseAuth.getInstance();
        startCountdownTimer();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        email_show.setText(email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification();
            mAuth.signOut();
            toastMessage("Email Verification Link Has Been Sent To " + email);
        }

        confirm_verify_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new SpotsDialog(Email_Verify_D.this, R.style.Custom);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();

                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(Email_Verify_D.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                                if (task.isSuccessful()) {
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    if (user!=null){
                                                        if (user.isEmailVerified()){
                                                            toastMessage("Your E-Mail " + email + " has been successfully Verified");

                                                            Intent intent = new Intent(getApplicationContext(), Sign_In_D.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            mAuth.signOut();
                                                            startActivity(intent);
                                                        }
                                                        else {
                                                            toastMessage("Please, Confirm Your Email Verification !");
                                                        }
                                                    } else {
                                                        toastMessage("Please, Confirm Your Email Verification !");
                                                    }
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }).start();
            }
        });

        send_link_again_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new SpotsDialog(Email_Verify_D.this, R.style.Custom);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user!=null){
                                    user.sendEmailVerification();
                                    mAuth.signOut();
                                    toastMessage("Email Verification Link Has Been Again Sent To " + email);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public void progressStyle21(){
        progressDialog = new SpotsDialog(Email_Verify_D.this, R.style.Custom);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        //checkSignup2();
//                        Toast.makeText(getApplicationContext(), "Completed.", Toast.LENGTH_SHORT).show();
//                        SharedPrefs.saveSharedSetting(Sign_Up_D.this, "check", "false");
//                        Intent intent = new Intent(getApplicationContext(), Sign_In_D.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                time_d.setText("Time Remaining : " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                //Toast.makeText(getApplicationContext(), "Time Out !", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
