package com.jarvis.patientmanagement.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.adapter.DataAdapterAll;
import com.jarvis.patientmanagement.config.IP_Config;
import com.jarvis.patientmanagement.patient.Patient_Details;
import com.jarvis.patientmanagement.patient.RecyclerAdapterPatientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Blood extends Fragment {

    List<DataAdapterAll> dataAdapterAllClassList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    JsonArrayRequest jsonArrayRequest ;
    ArrayList<String> SubjectNames,SubjectPhone,SubjectSubject;
    RequestQueue requestQueue ;
    private SpotsDialog progressDialog;
    View ChildView ;
    private ProgressDialog pDialog;
    int RecyclerViewClickedItemPOS ;

    public static Blood newInstance(){
        Blood blood=new Blood();

        return blood;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_patient__list, null);

        dataAdapterAllClassList = new ArrayList<>();
        SubjectNames = new ArrayList<>();
        SubjectPhone = new ArrayList<>();
        SubjectSubject = new ArrayList<>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        JSON_WEB_CALL();
        JSONLIST();

        return rootView;
    }

    public void JSONLIST(){

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    RecyclerViewClickedItemPOS = Recyclerview.getChildAdapterPosition(ChildView);

                    //Toast.makeText(Patient_List.this, SubjectNames.get(RecyclerViewClickedItemPOS), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(), Patient_Details.class);
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

    public void JSON_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(IP_Config.URL_P_L, new Response.Listener<JSONArray>() {
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
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            DataAdapterAll getDataAdapterAll2 = new DataAdapterAll();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                getDataAdapterAll2.setId(json.getString("blood"));
                getDataAdapterAll2.setName(json.getString("name"));

                SubjectNames.add(json.getString("name"));
                getDataAdapterAll2.setSubject(json.getString("address"));
                getDataAdapterAll2.setAddress(json.getString("mobile"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            dataAdapterAllClassList.add(getDataAdapterAll2);
        }
        recyclerViewadapter = new RecyclerAdapterPatientList(dataAdapterAllClassList, getActivity());
        recyclerView.setAdapter(recyclerViewadapter);
    }

    @Override
    public String toString() {
        return "Blood";
    }
}
