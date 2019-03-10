package com.jarvis.patientmanagement.patient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.config.IP_Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Patient_Details extends AppCompatActivity {

    private SpotsDialog progressDialog;
    StringRequest stringRequest;
    String name;
    JSONArray jsonarray = null;
    TextView patient_details_name, patient_details_blood;
    TextView patient_details_mobile, patient_details_address;
    RelativeLayout patient_details_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__details);

        patient_details_name = (TextView)findViewById(R.id.patient_details_name);
        patient_details_blood = (TextView)findViewById(R.id.patient_details_blood);
        patient_details_mobile = (TextView)findViewById(R.id.patient_details_mobile);
        patient_details_address = (TextView)findViewById(R.id.patient_details_address);
        patient_details_call = (RelativeLayout) findViewById(R.id.patient_details_call);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressStyle2();

        patient_details_name.setText("");
        patient_details_blood.setText("");
        patient_details_mobile.setText("");
        patient_details_address.setText("");

        patient_details_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetCall();
            }
        });
    }

    public void progressStyle2(){
        progressDialog = new SpotsDialog(Patient_Details.this, R.style.Custom);
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

                        getMySqlDetails();
                        name = getIntent().getStringExtra("TITLE");
                        setTitle(name);
                        patient_details_name.setText(name);
                    }
                });
            }
        }).start();
    }

    public void SetCall(){

        String phone_no = patient_details_mobile.getText().toString().replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone_no));
        startActivity(callIntent);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

    private void getMySqlDetails() {

        stringRequest=new StringRequest(Request.Method.POST, IP_Config.URL_P_LIST,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response);
                    jsonarray=jsonObject.getJSONArray("result");
                    for (int i=0;i<jsonarray.length();i++) {
                        JSONObject j=jsonarray.getJSONObject(i);
                        String id = j.getString("id");
                        String patient_name2 = j.getString("name");
                        String patient_address2 = j.getString("address");
                        String patient_division2 = j.getString("division");
                        String patient_district2 = j.getString("district");
                        String patient_mobile2 = j.getString("mobile");
                        String patient_blood2 = j.getString("blood");

                        patient_details_name.setText(patient_name2);
                        patient_details_blood.setText(patient_blood2);
                        patient_details_mobile.setText(patient_mobile2);
                        patient_details_address.setText(patient_address2+", "+patient_district2+", "+patient_division2);
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
                params.put("name", name);

                return params;}
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Patient_Details.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
