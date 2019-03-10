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

public class Doctor_Profile extends AppCompatActivity {

    StringRequest stringRequest;
    JSONArray jsonarray = null;
    String value21;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button btn_update_d;
    private SpotsDialog progressDialog;
    EditText doctor_profile_view_name, doctor_profile_view_address;
    EditText doctor_profile_view_email, doctor_profile_view_reg;
    EditText doctor_profile_view_expertise, doctor_profile_view_chamber;
    EditText doctor_profile_view_start, doctor_profile_view_end;
    EditText doctor_profile_view_mobile, doctor_profile_view_blood;
    EditText doctor_profile_view_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        doctor_profile_view_name = (EditText)findViewById(R.id.patient_profile_view_name);
        doctor_profile_view_address = (EditText)findViewById(R.id.patient_profile_view_address);
        doctor_profile_view_email = (EditText)findViewById(R.id.doctor_profile_view_email);
        doctor_profile_view_reg = (EditText)findViewById(R.id.doctor_profile_view_reg);
        doctor_profile_view_expertise = (EditText)findViewById(R.id.doctor_profile_view_expertise);
        doctor_profile_view_chamber = (EditText)findViewById(R.id.doctor_profile_view_chamber);
        doctor_profile_view_start = (EditText)findViewById(R.id.patient_profile_view_address_division);
        doctor_profile_view_end = (EditText)findViewById(R.id.patient_profile_view_address_district);
        doctor_profile_view_mobile = (EditText)findViewById(R.id.patient_profile_view_mobile);
        doctor_profile_view_blood = (EditText)findViewById(R.id.patient_profile_view_age);
        doctor_profile_view_password = (EditText)findViewById(R.id.patient_profile_view_password);
        btn_update_d = (Button)findViewById(R.id.btn_update_d);

        value21 = preferences.getString("key2", "defaultValue");
        setTitle(value21);
        setEditFalse();
        getMySqlDetails222();

        btn_update_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressStyle();
                //ProfileUpdate();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setEditFalse(){

        doctor_profile_view_name.setEnabled(false);
        doctor_profile_view_address.setEnabled(false);
        doctor_profile_view_expertise.setEnabled(false);
        doctor_profile_view_chamber.setEnabled(false);
        doctor_profile_view_start.setEnabled(false);
        doctor_profile_view_end.setEnabled(false);
        doctor_profile_view_mobile.setEnabled(false);
        doctor_profile_view_password.setEnabled(false);
        btn_update_d.setVisibility(View.GONE);
    }

    public void progressStyle(){
        progressDialog = new SpotsDialog(Doctor_Profile.this, R.style.Custom);
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
                        ProfileUpdate();
                    }
                });
            }
        }).start();
    }

    public void getMySqlDetails222(){

        stringRequest=new StringRequest(Request.Method.POST, IP_Config.URL_DP_V,new Response.Listener<String>() {
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
                        String email = j.getString("email");
                        String registration = j.getString("registration");
                        String expertise = j.getString("expertise");
                        String chamber = j.getString("chamber");
                        String start = j.getString("start");
                        String end = j.getString("end");
                        String mobile = j.getString("mobile");
                        String blood = j.getString("blood");
                        String password = j.getString("password");

                        doctor_profile_view_name.setText(name);
                        doctor_profile_view_address.setText(address);
                        doctor_profile_view_email.setText(email);
                        doctor_profile_view_reg.setText(registration);
                        doctor_profile_view_expertise.setText(expertise);
                        doctor_profile_view_chamber.setText(chamber);
                        doctor_profile_view_start.setText(start);
                        doctor_profile_view_end.setText(end);
                        doctor_profile_view_mobile.setText(mobile);
                        doctor_profile_view_blood.setText(blood);
                        doctor_profile_view_password.setText(password);
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
                params.put("email", value21);
                return params;}
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Doctor_Profile.this);
        requestQueue.add(stringRequest);
    }

    public void ProfileUpdate(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_DP_U, new Response.Listener<String>() {
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

                params.put("name", doctor_profile_view_name.getText().toString());
                params.put("address", doctor_profile_view_address.getText().toString());
                params.put("email", doctor_profile_view_email.getText().toString());
                params.put("expertise", doctor_profile_view_expertise.getText().toString());
                //params.put("registration", doctor_profile_view_reg.getText().toString());
                params.put("chamber", doctor_profile_view_chamber.getText().toString());
                params.put("start", doctor_profile_view_start.getText().toString());
                params.put("end", doctor_profile_view_end.getText().toString());
                params.put("mobile", doctor_profile_view_mobile.getText().toString());
                //params.put("blood", doctor_profile_view_blood.getText().toString());
                params.put("password", doctor_profile_view_password.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_d, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.edit_profile_d){

            doctor_profile_view_name.setEnabled(true);
            doctor_profile_view_address.setEnabled(true);
            doctor_profile_view_expertise.setEnabled(true);
            doctor_profile_view_chamber.setEnabled(true);
            doctor_profile_view_start.setEnabled(true);
            doctor_profile_view_end.setEnabled(true);
            doctor_profile_view_mobile.setEnabled(true);
            doctor_profile_view_password.setEnabled(true);
            btn_update_d.setVisibility(View.VISIBLE);

            //startActivity(new Intent(getApplicationContext(), Doctor_Edit.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
