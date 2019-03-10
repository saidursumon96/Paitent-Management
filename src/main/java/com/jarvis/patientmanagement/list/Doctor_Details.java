package com.jarvis.patientmanagement.list;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Doctor_Details extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private SpotsDialog progressDialog;
    StringRequest stringRequest;
    String name, SelectedTime3;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    JSONArray jsonarray = null;
    TextView doctor_details_name, doctor_details_expertise, doctor_details_chamber;
    TextView doctor_details_start, doctor_details_end, doctor_details_mobile;
    Button doctor_appoinment_set;
    EditText doctor_details_edit, doctor_details_date;
    RelativeLayout doctor_details_call;
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        progressStyle2();

        doctor_appoinment_set = (Button)findViewById(R.id.doctor_appoinment_set);
        doctor_details_call = (RelativeLayout)findViewById(R.id.doctor_details_call);
        doctor_details_name = (TextView)findViewById(R.id.doctor_details_name);
        doctor_details_expertise = (TextView)findViewById(R.id.doctor_details_expertise);
        doctor_details_chamber = (TextView)findViewById(R.id.doctor_details_chamber);
        doctor_details_start = (TextView)findViewById(R.id.doctor_details_start);
        doctor_details_end = (TextView)findViewById(R.id.doctor_details_end);
        doctor_details_mobile = (TextView)findViewById(R.id.doctor_details_mobile);

        doctor_details_edit = (EditText)findViewById(R.id.doctor_details_edit);
        doctor_details_date = (EditText)findViewById(R.id.doctor_details_date);

        doctor_details_name.setText("");
        doctor_details_expertise.setText("");
        doctor_details_chamber.setText("");
        doctor_details_start.setText("");
        doctor_details_end.setText("");
        doctor_details_mobile.setText("");

        doctor_details_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        doctor_details_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetCall2();
            }
        });

        doctor_appoinment_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = doctor_details_edit.getText().toString().trim();
                String date = doctor_details_date.getText().toString().trim();

                if (!date.isEmpty() && !title.isEmpty()){
                    progressStyle();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        HideThem();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void HideThem(){

        doctor_details_edit.setVisibility(View.GONE);
        doctor_details_date.setVisibility(View.GONE);
        doctor_appoinment_set.setVisibility(View.GONE);
    }

    public void SETAPPOINMENT(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_APP_SET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String check = "Success Set";
                String check1 = "Error";

                if(response.equals(check)) {
                    Toast.makeText(getApplicationContext(), "You Created An Appoinment To - "+name, Toast.LENGTH_SHORT).show();
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

                String value = preferences.getString("key2", "defaultValue");

                params.put("doctor", name);
                params.put("patientemail", value);
                params.put("date", doctor_details_date.getText().toString());
                params.put("details", doctor_details_edit.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    public void progressStyle(){
        progressDialog = new SpotsDialog(Doctor_Details.this, R.style.Custom);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        SETAPPOINMENT();
                    }
                });
            }
        }).start();
    }

    public void getDate(){

        datePickerDialog = DatePickerDialog.newInstance(Doctor_Details.this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#139469"));
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //Toast.makeText(Doctor_Details.this, "Date Not Selected !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SetCall2() {

        String phone_no = doctor_details_mobile.getText().toString().replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone_no));
        //callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

    public void progressStyle2(){
        progressDialog = new SpotsDialog(Doctor_Details.this, R.style.Custom);
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
                        name = getIntent().getStringExtra("TITLE");
                        setTitle(name);
                        //hospital_name.setText(name);
                        //GetImage();
                    }
                });
            }
        }).start();
    }

    private void getMySqlDetails2() {

        stringRequest=new StringRequest(Request.Method.POST, IP_Config.URL_D_LIST,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response);
                    jsonarray=jsonObject.getJSONArray("result");
                    for (int i=0;i<jsonarray.length();i++) {
                        JSONObject j=jsonarray.getJSONObject(i);
                        String id = j.getString("id");
                        String name2 = j.getString("name");
                        String expertise2 = j.getString("expertise");
                        String chamber2 = j.getString("chamber");
                        String start2 = j.getString("start");
                        String end2 = j.getString("end");
                        String mobile2 = j.getString("mobile");

                        doctor_details_name.setText(name2);
                        doctor_details_expertise.setText(expertise2);
                        doctor_details_chamber.setText(chamber2);
                        doctor_details_start.setText(start2);
                        doctor_details_end.setText(end2);
                        doctor_details_mobile.setText(mobile2);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Doctor_Details.this);
        requestQueue.add(stringRequest);
    }

    public void checkSignup(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_REGISTER_P, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String check = "Success Sign Up";
                String check1 = "Error Sign Up";

                if(response.equals(check)) {
                    Toast.makeText(getApplicationContext(), "You Created An Appoinment To - "+name, Toast.LENGTH_SHORT).show();
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

                params.put("details", doctor_details_edit.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }
        else if (id == R.id.edit_show){

            doctor_details_edit.setVisibility(View.VISIBLE);
            doctor_details_date.setVisibility(View.VISIBLE);
            doctor_appoinment_set.setVisibility(View.VISIBLE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
        doctor_details_date.setText(date);
    }
}
