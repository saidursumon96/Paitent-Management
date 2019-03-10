package com.jarvis.patientmanagement.users;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.TextView;
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
import com.jarvis.patientmanagement.session.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Sign_In_D extends Activity {

    private Button doctor_signin_btn, doctor_signup_btn;
    private EditText doctor_signin_inputEmail, doctor_signin_inputPassword;
    Drawable draw;
    private String email2, password2;
    private static final String TAG = "Sign_In_D";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CheckBox doctor_signin_show_password;
    private ProgressDialog pDialog;
    private SpotsDialog progressDialog;
    private TextView return_to_patient_login_link;

    String email_sh_d;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in__d);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        mAuth = FirebaseAuth.getInstance();

        doctor_signin_inputEmail = (EditText) findViewById(R.id.doctor_login_inputEmail);
        doctor_signin_inputPassword = (EditText) findViewById(R.id.doctor_login_inputPassword);
        doctor_signin_btn = (Button) findViewById(R.id.doctor_signin_btn);
        doctor_signup_btn = (Button) findViewById(R.id.doctor_signup_link);
        return_to_patient_login_link = (TextView)findViewById(R.id.return_to_patient_login_link);
        doctor_signin_show_password = (CheckBox)findViewById(R.id.doctor_signin_show_password);

        //doctor_signin_inputPassword.getSelectionStart();
        //doctor_signin_inputPassword.getSelectionEnd();
        email2 = doctor_signin_inputEmail.getText().toString();
        password2 = doctor_signin_inputPassword.getText().toString();

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

        ShowPassword();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        doctor_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
                //progressStyle2();
            }
        });

        return_to_patient_login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sign_In_P.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        doctor_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Sign_Up_D.class));
            }
        });
    }

    public void ShowPassword(){

        doctor_signin_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    doctor_signin_inputPassword.setTransformationMethod(null);
                }
                else {
                    doctor_signin_inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void validation(){

        String email = doctor_signin_inputEmail.getText().toString().trim();
        String password = doctor_signin_inputPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()){
            doctor_signin_inputEmail.setError(null);
            doctor_signin_inputPassword.setError(null);
            progressStyle2();
        }
        else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            doctor_signin_inputEmail.setError("Enter Valid Email Address !");
        }
        else if (password.isEmpty() || password.length() < 6){
            doctor_signin_inputPassword.setError("Password Must Be Greater Than 6 Character !");
        }
        //Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_SHORT).show();
    }

    public void progressStyle2(){
        progressDialog = new SpotsDialog(Sign_In_D.this, R.style.Custom);
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
                        checkSignin2();
                        //Toast.makeText(getApplicationContext(), "Completed.", Toast.LENGTH_SHORT).show();
                        //onBackPressed();
                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        //moveTaskToBack(true);
                        //finish();
                    }
                });
            }
        }).start();
    }

    public void checkSignin2(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_LOGIN_D,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String log = "User Logged In";

                        if(response.matches(log)) {
                            //startActivity(new Intent(getApplicationContext(), Welcome.class));
                            //SharedPrefs.saveSharedSetting(Sign_In_D.this, "check", "true");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("TITLE", doctor_signin_inputEmail.getText());

                            email_sh_d = doctor_signin_inputEmail.getText().toString().trim();
                            editor.putString("key2",email_sh_d);
                            editor.putString("key","doctor");
                            editor.apply();


                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Hello "+ doctor_signin_inputEmail.getText(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Invalid email or password !", Toast.LENGTH_SHORT).show();
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
                params.put("email", doctor_signin_inputEmail.getText().toString());
                params.put("password", doctor_signin_inputPassword.getText().toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
}
