package com.jarvis.patientmanagement.diseases;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import org.json.JSONArray;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.android.volley.RequestQueue;

import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.adapter.DataAdapterAll;

public class Diseases_List extends AppCompatActivity {

    List<DataAdapterAll> dataAdapterAllClassList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    JsonArrayRequest jsonArrayRequest ;
    ArrayList<String> SubjectNames,SubjectPhone,SubjectSubject;
    RequestQueue requestQueue ;
    String HTTP_SERVER_URL = "http://192.168.0.154/jsonmulti/StudentDetails.php";
    View ChildView ;
    private ProgressDialog pDialog;
    int RecyclerViewClickedItemPOS ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases__list);

        dataAdapterAllClassList = new ArrayList<>();
        SubjectNames = new ArrayList<>();
        SubjectPhone = new ArrayList<>();
        SubjectSubject = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        JSON_WEB_CALL();
        ITEM_CLICK();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void JSON_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(HTTP_SERVER_URL,

                new Response.Listener<JSONArray>() {
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
                getDataAdapterAll2.setId(json.getString("id"));
                getDataAdapterAll2.setName(json.getString("name"));

                //Adding subject name here to show on click event.
                SubjectNames.add(json.getString("name"));
                getDataAdapterAll2.setSubject(json.getString("subject"));
                getDataAdapterAll2.setAddress(json.getString("phone_number"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            dataAdapterAllClassList.add(getDataAdapterAll2);
        }
        recyclerViewadapter = new RecyclerAdapterDiseasesList(dataAdapterAllClassList, this);
        recyclerView.setAdapter(recyclerViewadapter);
        //recyclerViewadapter.notifyItemChanged(1);
    }

    public void ITEM_CLICK(){

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(Diseases_List.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked item value.
                    RecyclerViewClickedItemPOS = Recyclerview.getChildAdapterPosition(ChildView);

                    //Printing RecyclerView Clicked item clicked value using Toast Message.
                    Toast.makeText(Diseases_List.this, SubjectNames.get(RecyclerViewClickedItemPOS), Toast.LENGTH_LONG).show();

                    //Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    //intent.putExtra("NAME", SubjectNames.get(RecyclerViewClickedItemPOS));
//                    intent.putExtra("PHONE", SubjectPhone.get(RecyclerViewClickedItemPOS));
                    //                  intent.putExtra("SUBJECT", SubjectSubject);
                    //intent.putExtra("IMAGE", RecyclerViewItemPosition);
                    //startActivity(intent);
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
