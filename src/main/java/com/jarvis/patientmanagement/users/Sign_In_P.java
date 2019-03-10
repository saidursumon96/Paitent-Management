package com.jarvis.patientmanagement.users;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jarvis.patientmanagement.MainActivity;
import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.config.IP_Config;
import com.jarvis.patientmanagement.helper.SQLiteHandler_P;
import com.jarvis.patientmanagement.helper.SessionManager;
import com.jarvis.patientmanagement.session.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Sign_In_P extends Activity {

    private Button patient_signin_btn, patient_signup_btn, login_as_doctor_link;
    Drawable draw;
    private String email, password;
    private static final String TAG = "Sign_In_P";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CheckBox patient_signin_show_password;
    private SpotsDialog progressDialog;
    EditText patient_login_inputEmail, patient_login_inputPassword;
    private SessionManager session;
    private SQLiteHandler_P db;
    String email_sh;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in__p);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        mAuth = FirebaseAuth.getInstance();

        patient_login_inputEmail = (EditText) findViewById(R.id.patient_login_inputEmail);
        patient_login_inputPassword = (EditText) findViewById(R.id.patient_login_inputPassword);
        patient_signin_btn = (Button) findViewById(R.id.patient_signin_btn);
        patient_signup_btn = (Button) findViewById(R.id.patient_signup_link);
        login_as_doctor_link = (Button)findViewById(R.id.login_as_doctor_link);
        patient_signin_show_password = (CheckBox)findViewById(R.id.patient_signin_show_password);

        email = patient_login_inputEmail.getText().toString();
        password = patient_login_inputPassword.getText().toString();

        db = new SQLiteHandler_P(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };

        //startActivity(new Intent(getApplicationContext(), MainActivity.class));

        ShowPassword();

        patient_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
                //progressStyle();
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        patient_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Sign_Up_P.class));
            }
        });

        login_as_doctor_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Sign_In_D.class));
            }
        });
    }

    public void ShowPassword(){

        patient_signin_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    patient_login_inputPassword.setTransformationMethod(null);
                }
                else {
                    patient_login_inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void validation(){

        String email = patient_login_inputEmail.getText().toString().trim();
        String password = patient_login_inputPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()){
            patient_login_inputEmail.setError(null);
            patient_login_inputPassword.setError(null);
                progressStyle();
        }
        else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            patient_login_inputEmail.setError("Enter Valid Email Address !");
        }
        else if (password.isEmpty() || password.length() < 3){
            patient_login_inputPassword.setError("Password Must Be Greater Than 6 Character !");
        }
        //Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_SHORT).show();
    }

    private void checkSignin(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_LOGIN_P,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String log = "User Logged In";

                        if(response.matches(log)) {
                            //startActivity(new Intent(getApplicationContext(), Welcome.class));
                            SharedPrefs.saveSharedSetting(Sign_In_P.this, "check", "false");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("MAIN", email);

                            email_sh = patient_login_inputEmail.getText().toString().trim();
                            editor.putString("key2",email_sh);
                            editor.putString("key","patient");
                            editor.apply();

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Hello "+ patient_login_inputEmail.getText(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Invalid Email or Password !", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ErrorResponse", finalResponse);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", patient_login_inputEmail.getText().toString());
                params.put("password", patient_login_inputPassword.getText().toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    public void progressStyle(){
        progressDialog = new SpotsDialog(Sign_In_P.this, R.style.Custom);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        checkSignin();
                       // startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        Toast.makeText(getApplicationContext(), "Completed.", Toast.LENGTH_SHORT).show();
//
//                        SharedPrefs.saveSharedSetting(Sign_In_P.this, "check", "false");
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void onBackPressed() {
//        mAuth.signOut();
//        super.onBackPressed();
//    }
}
