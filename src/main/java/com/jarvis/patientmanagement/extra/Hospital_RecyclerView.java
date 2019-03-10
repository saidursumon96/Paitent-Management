package com.jarvis.patientmanagement.extra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.adapter.DataAdapter;
import com.jarvis.patientmanagement.adapter.RecyclerViewAdapter;
import com.jarvis.patientmanagement.config.IP_Config;
import com.jarvis.patientmanagement.details.Hospital_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Hospital_RecyclerView extends AppCompatActivity {

    List<DataAdapter> ListOfdataAdapter;
    RecyclerView recyclerView;
    private SpotsDialog progressDialog;
    String Image_Name_JSON = "image_title";
    String Image_URL_JSON = "image_url";
    private ProgressDialog pDialog;
    JsonArrayRequest RequestOfJSonArray ;
    RequestQueue requestQueue ;
    View view ;
    ProgressBar progressBar;
    ArrayList<String> SubjectNames,SubjectPhone,SubjectSubject;

    int RecyclerViewItemPosition ;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;
    RecyclerView.Adapter recyclerViewadapter;
    ArrayList<String> ImageTitleNameArrayListForClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital__recycler_view);

        ImageTitleNameArrayListForClick = new ArrayList<>();
        ListOfdataAdapter = new ArrayList<>();
        SubjectNames = new ArrayList<>();
        SubjectPhone = new ArrayList<>();
        SubjectSubject = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview21);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        layoutManagerOfrecyclerView = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        progressStyle2();
        //DialogBox();
        //JSON_HTTP_CALL();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void JSONLIST(){

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
                view = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(view != null && gestureDetector.onTouchEvent(motionEvent)) {
                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(view);
                    // Showing RecyclerView Clicked Item value using Toast.
                    //Toast.makeText(getApplicationContext(), ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition),
                    //  Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), Hospital_Details.class);
                    intent.putExtra("TITLE", ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition));
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    public void progressStyle2(){
        progressDialog = new SpotsDialog(Hospital_RecyclerView.this, R.style.Custom);
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

                        JSON_HTTP_CALL();
                        JSONLIST();
                    }
                });
            }
        }).start();
    }

    public void DialogBox(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Data...");
        pDialog.setCancelable(false);
        pDialog.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                pDialog.cancel();
                //JSON_HTTP_CALL();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);
    }

    public void JSON_HTTP_CALL(){

        RequestOfJSonArray = new JsonArrayRequest(IP_Config.URL_H_JSON_L, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ParseJSonResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(RequestOfJSonArray);
    }

    public void ParseJSonResponse(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            DataAdapter GetDataAdapter2 = new DataAdapter();
            JSONObject json = null;
            //hidePDialog();

            try {
                json = array.getJSONObject(i);
                GetDataAdapter2.setHospitalName(json.getString(Image_Name_JSON));
                ImageTitleNameArrayListForClick.add(json.getString(Image_Name_JSON));
                GetDataAdapter2.setHospitalImageUrl(json.getString(Image_URL_JSON));
                //GetDataAdapter2.setId(json.getInt("id"));
                GetDataAdapter2.setHospitalAddress(json.getString("name"));
                GetDataAdapter2.setHospitalLocation(json.getString("subject"));
                GetDataAdapter2.setHospitalPhone(json.getString("phone_number"));
            }
            catch (JSONException e) {
                hidePDialog();
                e.printStackTrace();
            }
            ListOfdataAdapter.add(GetDataAdapter2);
        }
        recyclerViewadapter = new RecyclerViewAdapter(ListOfdataAdapter, this);
        recyclerView.setAdapter(recyclerViewadapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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
