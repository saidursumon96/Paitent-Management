package com.jarvis.patientmanagement.diseases;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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

public class Diseases_Details extends AppCompatActivity {

    String name, link;
    TextView diseases_name, diseases_cause, click_link;
    TextView diseases_symptom, diseases_diagonis, diseases_treatment;
    private NetworkImageView hospital_image;
    private ImageLoader mImageLoader;
    RelativeLayout hospital_call, hospital_map;
    StringRequest stringRequest;
    JSONArray jsonarray = null;
    private SpotsDialog progressDialog;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases__details);

        diseases_name = (TextView)findViewById(R.id.appoinment_name);
        diseases_cause = (TextView)findViewById(R.id.diseases_cause);
        diseases_symptom = (TextView)findViewById(R.id.diseases_symptom);
        diseases_diagonis = (TextView)findViewById(R.id.diseases_diagonis);
        diseases_treatment = (TextView)findViewById(R.id.diseases_treatment);
        click_link = (TextView)findViewById(R.id.click_link);

        progressStyle2();

        diseases_name.setText("");
        diseases_cause.setText("");
        diseases_symptom.setText("");
        diseases_diagonis.setText("");
        diseases_treatment.setText("");

        click_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoweb();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void progressStyle2(){
        progressDialog = new SpotsDialog(Diseases_Details.this, R.style.Custom);
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
                        getMySqlDetails2();
                        name = getIntent().getStringExtra("NAME");
                        setTitle(name);
                        diseases_name.setText(name);
                    }
                });
            }
        }).start();
    }

    private void getMySqlDetails2() {

        stringRequest=new StringRequest(Request.Method.POST, IP_Config.URL_DI_LIST,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response);
                    //Toast.makeText(Hospital_Details.this, response, Toast.LENGTH_SHORT).show();
                    jsonarray=jsonObject.getJSONArray("result");
                    for (int i=0;i<jsonarray.length();i++) {
                        JSONObject j=jsonarray.getJSONObject(i);
                        String id = j.getString("id");
                        String name = j.getString("name");
                        String cause = j.getString("cause");
                        String symptom = j.getString("diagnosis");
                        String diagnosis = j.getString("treatment");
                        String treatment = j.getString("symptom");
                        String link2 = j.getString("link");

                        link = link2;
                        diseases_name.setText(name);
                        diseases_cause.setText(cause);
                        diseases_symptom.setText(symptom);
                        diseases_diagonis.setText(diagnosis);
                        diseases_treatment.setText(treatment);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Diseases_Details.this);
        requestQueue.add(stringRequest);
    }

    public void  gotoweb(){

        String url = link;
        //Uri uri = Uri.parse("http://www.google.com");
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
