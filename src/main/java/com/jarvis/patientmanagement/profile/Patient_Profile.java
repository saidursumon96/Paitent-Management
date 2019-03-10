package com.jarvis.patientmanagement.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jarvis.patientmanagement.MainActivity;
import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.config.IP_Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Patient_Profile extends AppCompatActivity {

    StringRequest stringRequest;
    JSONArray jsonarray = null;
    String email2, value2;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button btn_update_p;
    private SpotsDialog progressDialog;
    EditText patient_profile_view_name,patient_profile_view_address;
    EditText patient_profile_view_address_division, patient_profile_view_address_district;
    EditText patient_profile_view_email, patient_profile_view_age;
    EditText patient_profile_view_gender, patient_profile_view_mobile;
    EditText patient_profile_view_blood, patient_profile_view_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        patient_profile_view_name = (EditText)findViewById(R.id.patient_profile_view_name);
        patient_profile_view_address = (EditText)findViewById(R.id.patient_profile_view_address);
        patient_profile_view_address_division = (EditText)findViewById(R.id.patient_profile_view_address_division);
        patient_profile_view_address_district = (EditText)findViewById(R.id.patient_profile_view_address_district);
        patient_profile_view_email = (EditText)findViewById(R.id.patient_profile_view_email);
        patient_profile_view_age = (EditText)findViewById(R.id.patient_profile_view_age);
        patient_profile_view_gender = (EditText)findViewById(R.id.patient_profile_view_gender);
        patient_profile_view_mobile = (EditText)findViewById(R.id.patient_profile_view_mobile);
        patient_profile_view_blood = (EditText)findViewById(R.id.patient_profile_view_blood);
        patient_profile_view_password = (EditText)findViewById(R.id.patient_profile_view_password);
        btn_update_p = (Button)findViewById(R.id.btn_update_p);

        //email2 = getIntent().getStringExtra("MAIN");
        //email2 = "saidur96@gmail.com";
        value2 = preferences.getString("key2", "defaultValue");
        setTitle(value2);
        setEditFalse2();
        getMySqlDetails221();

        btn_update_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressStyle2();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setEditFalse2(){

        patient_profile_view_name.setEnabled(false);
        patient_profile_view_address.setEnabled(false);
        patient_profile_view_address_division.setEnabled(false);
        patient_profile_view_address_district.setEnabled(false);
        patient_profile_view_age.setEnabled(false);
        patient_profile_view_mobile.setEnabled(false);
        patient_profile_view_password.setEnabled(false);
        btn_update_p.setVisibility(View.GONE);
    }

    public void progressStyle2(){
        progressDialog = new SpotsDialog(Patient_Profile.this, R.style.Custom);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        ProfileUpdate2();
                    }
                });
            }
        }).start();
    }

    private void getMySqlDetails221() {

        stringRequest=new StringRequest(Request.Method.POST, IP_Config.URL_PP_V,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response);
                    jsonarray=jsonObject.getJSONArray("result");
                    for (int i=0;i<jsonarray.length();i++) {
                        JSONObject j=jsonarray.getJSONObject(i);
                        String id = j.getString("id");
                        String name = j.getString("name");
                        String address = j.getString("address");
                        String division = j.getString("division");
                        String district = j.getString("district");
                        String email = j.getString("email");
                        String birthday = j.getString("birthday");
                        String gender = j.getString("gender");
                        String mobile = j.getString("mobile");
                        String blood = j.getString("blood");
                        String password = j.getString("password");

                        patient_profile_view_name.setText(name);
                        patient_profile_view_address.setText(address);
                        patient_profile_view_address_division.setText(division);
                        patient_profile_view_address_district.setText(district);
                        patient_profile_view_email.setText(email);
                        patient_profile_view_age.setText(birthday);
                        patient_profile_view_gender.setText(gender);
                        patient_profile_view_mobile.setText(mobile);
                        patient_profile_view_blood.setText(blood);
                        patient_profile_view_password.setText(password);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", value2);
                return params;}
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Patient_Profile.this);
        requestQueue.add(stringRequest);
    }

    public void ProfileUpdate2(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_PP_U, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String check = "Success";
                String check1 = "Error";

                if(response.equals(check)) {
                    Toast.makeText(getApplicationContext(), "Profile Updated.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (response.equals(check1)){
                    Toast.makeText(getApplicationContext(), "Something Wrong !", Toast.LENGTH_SHORT).show();
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

                params.put("name", patient_profile_view_name.getText().toString());
                params.put("address", patient_profile_view_address.getText().toString());
                params.put("division", patient_profile_view_address_division.getText().toString());
                params.put("district", patient_profile_view_address_district.getText().toString());
                params.put("email", patient_profile_view_email.getText().toString());
                params.put("birthday", patient_profile_view_age.getText().toString());
                //params.put("gender", patient_profile_view_gender.getText().toString());
                params.put("mobile", patient_profile_view_mobile.getText().toString());
                //params.put("blood", patient_profile_view_blood.getText().toString());
                params.put("password", patient_profile_view_password.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }
        else if (id == R.id.edit_profile){

            patient_profile_view_name.setEnabled(true);
            patient_profile_view_address.setEnabled(true);
            patient_profile_view_address_division.setEnabled(true);
            patient_profile_view_address_district.setEnabled(true);
            patient_profile_view_age.setEnabled(true);
            patient_profile_view_mobile.setEnabled(true);
            patient_profile_view_password.setEnabled(true);
            btn_update_p.setVisibility(View.VISIBLE);

            //startActivity(new Intent(getApplicationContext(), Patient_Edit.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
