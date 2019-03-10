package com.jarvis.patientmanagement.list;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.Menu;
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
import com.jarvis.patientmanagement.adapter.DataAdapterAll;
import com.jarvis.patientmanagement.config.IP_Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Doctor_List extends AppCompatActivity {

    List<DataAdapterAll> dataAdapterAllClassList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    JsonArrayRequest jsonArrayRequest ;
    ArrayList<String> SubjectNames,SubjectPhone,SubjectSubject;
    RequestQueue requestQueue ;
    View ChildView ;
    private SpotsDialog progressDialog;
    private ProgressDialog pDialog;
    int RecyclerViewClickedItemPOS ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__list);

        dataAdapterAllClassList = new ArrayList<>();

        SubjectNames = new ArrayList<>();
        SubjectPhone = new ArrayList<>();
        SubjectSubject = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        progressStyle3();
        //JSON_WEB_CALL();
        //JSON_CARD_VIEW();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void JSON_CARD_VIEW(){

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(Doctor_List.this, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }
            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    RecyclerViewClickedItemPOS = Recyclerview.getChildAdapterPosition(ChildView);

                    //Toast.makeText(Doctor_List.this, SubjectNames.get(RecyclerViewClickedItemPOS), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), Doctor_Details.class);
                    intent.putExtra("TITLE", SubjectNames.get(RecyclerViewClickedItemPOS));
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

    public void progressStyle3(){
        progressDialog = new SpotsDialog(Doctor_List.this, R.style.Custom);
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

                        JSON_WEB_CALL();
                        JSON_CARD_VIEW();
                    }
                });
            }
        }).start();
    }

    public void JSON_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(IP_Config.URL_D_L, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            DataAdapterAll getDataAdapterAll2 = new DataAdapterAll();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                getDataAdapterAll2.setId(json.getString("expertise"));
                getDataAdapterAll2.setName(json.getString("name"));

                SubjectNames.add(json.getString("name"));
                getDataAdapterAll2.setSubject(json.getString("address"));
                getDataAdapterAll2.setAddress(json.getString("chamber"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            dataAdapterAllClassList.add(getDataAdapterAll2);
        }
        recyclerViewadapter = new RecyclerAdapterDoctorList(dataAdapterAllClassList, this);
        recyclerView.setAdapter(recyclerViewadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_doctor_list, menu);

        final MenuItem item = menu.findItem(R.id.search_doctor);
        SearchView searchView = (SearchView)item.getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }
        else if (id == R.id.search_doctor) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
