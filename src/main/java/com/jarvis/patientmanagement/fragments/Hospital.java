package com.jarvis.patientmanagement.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class Hospital extends Fragment {

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

    public static Hospital newInstance(){
        Hospital hospital=new Hospital();

        return hospital;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_hospital__recycler_view, null);

        ImageTitleNameArrayListForClick = new ArrayList<>();
        ListOfdataAdapter = new ArrayList<>();
        SubjectNames = new ArrayList<>();
        SubjectPhone = new ArrayList<>();
        SubjectSubject = new ArrayList<>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview21);
        recyclerView.setHasFixedSize(true);
        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        JSON_HTTP_CALL();
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
                view = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(view != null && gestureDetector.onTouchEvent(motionEvent)) {
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(view);

                    Intent intent = new Intent(getActivity(), Hospital_Details.class);
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
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(RequestOfJSonArray);
    }

    public void ParseJSonResponse(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            DataAdapter GetDataAdapter2 = new DataAdapter();
            JSONObject json = null;

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
                e.printStackTrace();
            }
            ListOfdataAdapter.add(GetDataAdapter2);
        }
        recyclerViewadapter = new RecyclerViewAdapter(ListOfdataAdapter, getActivity());
        //recyclerViewadapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerViewadapter);

    }

    @Override
    public String toString() {
        return "Hospital";
    }
}
