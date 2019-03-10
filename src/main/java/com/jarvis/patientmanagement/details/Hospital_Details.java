package com.jarvis.patientmanagement.details;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.adapter.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.jarvis.patientmanagement.config.IP_Config;
import dmax.dialog.SpotsDialog;

public class Hospital_Details extends AppCompatActivity {

    TextView hospital_name, hospital_phone;
    TextView hospital_address, hospital_address2, hospital_location;
    String name, hospital_image_url, hospital_location_map2;
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
        setContentView(R.layout.activity_hospital__details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hospital_name = (TextView) findViewById(R.id.hospital_details_name);
        hospital_phone = (TextView) findViewById(R.id.hospital_details_phone);
        hospital_address = (TextView) findViewById(R.id.hospital_details_address);
        hospital_address2 = (TextView) findViewById(R.id.hospital_details_address_2);
        hospital_location = (TextView) findViewById(R.id.hospital_details_location);
        hospital_image = (NetworkImageView) findViewById(R.id.hospital_details_image);
        hospital_call = (RelativeLayout)findViewById(R.id.hospital_call);
        hospital_map = (RelativeLayout)findViewById(R.id.hospital_map);

        //progressDialog();
        progressStyle2();

        hospital_name.setText("");
        hospital_phone.setText("");
        hospital_address.setText("");
        hospital_address2.setText("");

        hospital_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetCall();
            }
        });

        hospital_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        hospital_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getLocation();
            }
        });

        //getMySqlDetails();
//        name = getIntent().getStringExtra("TITLE");
//        hospital_name.setText(name);
        //setTitle(name);
    }

    public void SetCall() {

        String phone_no = hospital_phone.getText().toString().replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone_no));
        //callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

    }

    public void progressDialog(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Data...");
        pDialog.setCancelable(false);
        pDialog.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                pDialog.cancel();
                getMySqlDetails();
                name = getIntent().getStringExtra("TITLE");
                setTitle(name);
                hospital_name.setText(name);
                //GetImage();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);
    }

    public void progressStyle2(){
        progressDialog = new SpotsDialog(Hospital_Details.this, R.style.Custom);
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
                        hospital_name.setText(name);
                        //GetImage();
                    }
                });
            }
        }).start();
    }

   /* @Override
    protected void onStart() {
        super.onStart();

        mImageLoader = ImageAdapter.getInstance(this.getApplicationContext()).getImageLoader();
        //final String url = "http://goo.gl/0rkaBz";

        final String url = "https://saidursumon96.000webhostapp.com/hospital/dmc.jpg";
        mImageLoader.get(url, ImageLoader.getImageListener(hospital_image, R.drawable.ic_vector_select_image,
                                    android.R.drawable .ic_dialog_alert));
        hospital_image.setImageUrl(url, mImageLoader);
    }*/

    private void getMySqlDetails() {

        stringRequest=new StringRequest(Request.Method.POST,IP_Config.URL_H_L,new Response.Listener<String>() {
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
                        String hospital_name2 = j.getString("image_title");
                        String hospital_image2 = j.getString("image_url");
                        String hospital_address_1 = j.getString("name");
                        String hospital_address_2 = j.getString("subject");
                        String hospital_phone_number = j.getString("phone_number");
                        String hospital_location_map = j.getString("location");

                        hospital_phone.setText(hospital_phone_number);
                        hospital_address.setText(hospital_address_1);
                        hospital_address2.setText(hospital_address_2);
                        //hospital_location.setText(hospital_image2);
                        hospital_location_map2 = hospital_location_map;
                        hospital_image_url = hospital_image2;
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

                params.put("image_title", name);

                return params;}
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Hospital_Details.this);
        requestQueue.add(stringRequest);
    }

    public void GetImage(){

        final String url = "https://saidursumon96.000webhostapp.com/hospital/dmc.jpg";
        //String get_url = hospital_image_url;

        mImageLoader = ImageAdapter.getInstance(this.getApplicationContext()).getImageLoader();
        mImageLoader.get(url, ImageLoader.getImageListener(hospital_image, R.drawable.nopreview2,
                android.R.drawable .ic_dialog_alert));
        hospital_image.setImageUrl(url, mImageLoader);
    }

    public void getLocation(){

        //String location = "22.6863517,90.36146659999997";
        String location = hospital_location_map2;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:"+location));
        Intent chooser = Intent.createChooser(intent, "Launch Maps");
        startActivity(chooser);
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
