package com.jarvis.patientmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvis.patientmanagement.activities.Abouts;
import com.jarvis.patientmanagement.activities.Feedback;
import com.jarvis.patientmanagement.activities.Helps;
import com.jarvis.patientmanagement.activities.Settings;
import com.jarvis.patientmanagement.appoinment.Appoinment_RecyclerView;
import com.jarvis.patientmanagement.diseases.Diseases_List;
import com.jarvis.patientmanagement.extra.Hospital_RecyclerView;
import com.jarvis.patientmanagement.fragments.Appoinment;
import com.jarvis.patientmanagement.fragments.Blood;
import com.jarvis.patientmanagement.fragments.Diseases;
import com.jarvis.patientmanagement.fragments.Hospital;
import com.jarvis.patientmanagement.helper.SQLiteHandler_P;
import com.jarvis.patientmanagement.helper.SessionManager;
import com.jarvis.patientmanagement.list.Doctor_List;
import com.jarvis.patientmanagement.list.Doctor_Details;
import com.jarvis.patientmanagement.patient.Patient_List;
import com.jarvis.patientmanagement.profile.Doctor_Profile;
import com.jarvis.patientmanagement.profile.Patient_Profile;
import com.jarvis.patientmanagement.session.SharedPrefs;
import com.jarvis.patientmanagement.users.Sign_In_P;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Handler mHandler;
    private ImageView imgProfile;
    private FloatingActionButton fab, fab2;

    String name;
    TextView email;
    ProgressDialog dialog;
    private SpotsDialog progressDialog;

    private SQLiteHandler_P db;
    private SessionManager session;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private int hold = 0;
    public static int navItemIndex = 0;
    private String[] activityTitles;
    private static final String TAG_HOME = "appoinment";
    private static final String TAG_BLOOD = "blood";
    private static final String TAG_DISEASES = "diseases";
    private static final String TAG_HOSPITAL = "hospital";
    public static String CURRENT_TAG = TAG_HOME;

    private boolean isUserClickedBackButton = false;
    private boolean shouldLoadHomeFragOnBackPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        navItemIndex = 0;
        CURRENT_TAG = TAG_HOME;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_main, Appoinment.newInstance()).commit();

        View headerview = getLayoutInflater().inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(headerview);

        db = new SQLiteHandler_P(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        String value221 = preferences.getString("key", "defaultValue");

        if (value221.equals("doctor")){
            getSupportActionBar().setTitle(R.string.app_name_2);
        }
        else {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        name = getIntent().getStringExtra("MAIN");
        email = (TextView) headerview.findViewById(R.id.user_email_upper);
        //email.setText(name);

        //TextView email2 = (TextView)headerview.findViewById(R.id.user_email_upper);
        //email2.setText(name);

        imgProfile = (ImageView)headerview.findViewById(R.id.user_profile_icon);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                String value = preferences.getString("key", "defaultValue");

                if (value.equals("patient")){
                    String activityToStart = "com.jarvis.patientmanagement.profile.Patient_Profile";
                    try {
                        Class<?> c = Class.forName(activityToStart);
                        Intent intent = new Intent(MainActivity.this, c);
                        startActivity(intent);
                    }
                    catch (ClassNotFoundException ignored) {
                    }
                }
                else if (value.equals("doctor")){
                    String activityToStart = "com.jarvis.patientmanagement.profile.Doctor_Profile";
                    try {
                        Class<?> c = Class.forName(activityToStart);
                        Intent intent2 = new Intent(MainActivity.this, c);
                        startActivity(intent2);
                    }
                    catch (ClassNotFoundException ignored) {
                    }
                }
                String value2 = preferences.getString("key2", "defaultValue");
                //Toast.makeText(getApplicationContext(), value2, Toast.LENGTH_SHORT).show();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Doctor_List.class));
                //startActivity(new Intent(getApplicationContext(), Appoinment_RecyclerView.class));
            }
        });

        fab2 = (FloatingActionButton)findViewById(R.id.fab_diseases);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Doctor_Details.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void toggleFab() {
        if (navItemIndex == 0) {
            fab.show();
            fab2.hide();
        }
        else {
            fab.hide();
            //fab2.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (CURRENT_TAG == TAG_HOME){
            if (!isUserClickedBackButton){

                String value22 = preferences.getString("key", "defaultValue");

                if (value22.equals("doctor")){
                    getSupportActionBar().setTitle(R.string.app_name_2);
                }
                else {
                    getSupportActionBar().setTitle(R.string.app_name);
                }

                //getSupportActionBar().setTitle(R.string.app_name);
                Toast.makeText(this,"Press Back Again To Exit.", Toast.LENGTH_SHORT).show();
                isUserClickedBackButton = true;

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                //hold++;
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (hold == 1){
//                            Toast.makeText(getApplicationContext(),"Press Back Again To Exit.", Toast.LENGTH_SHORT).show();
//                        }
//                        else if(hold == 2){
//                            Intent startMain = new Intent(Intent.ACTION_MAIN);
//                            startMain.addCategory(Intent.CATEGORY_HOME);
//                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(startMain);
//                        }
//                        hold = 0;
//                    }
//                }, 1500);
            }
            else {
                super.onBackPressed();
            }
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    isUserClickedBackButton = false;
                }
            }.start();
        }

        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                fab.show();
                fab2.hide();

                String value21 = preferences.getString("key", "defaultValue");

                if (value21.equals("patient")){
                    getSupportActionBar().setTitle(R.string.app_name);
                }
                else {
                    getSupportActionBar().setTitle(R.string.app_name_2);
                }

                //getSupportActionBar().setTitle(R.string.app_name);
                MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                        Appoinment.newInstance()).commit();
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.search_doctor_list, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent = null, chooser=null;

        if(id == R.id.action_help){
            startActivity(new Intent(getApplicationContext(), Helps.class));
        }
        else if (id == R.id.action_settings) {
            //startActivity(new Intent(getApplicationContext(), Sign_In_P.class));
            return true;
        }
        else if (id == R.id.action_logout){
            //DialogNew();
            SharedPrefs.saveSharedSetting(MainActivity.this, "check", "true");
            progressStyleMain();
        }
        return super.onOptionsItemSelected(item);
    }

    public void DialogNew(){

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
//                        progressDialog.hide();
                        Intent intent = new Intent(getApplicationContext(), Sign_In_P.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    public void progressStyleMain(){

        progressDialog = new SpotsDialog(MainActivity.this, R.style.Custom);
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
                        //progressDialog.dismiss();
                        CekSession();
//                        Intent intent = new Intent(getApplicationContext(), Sign_In_P.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    public void CekSession(){

        Boolean Check = Boolean.valueOf(SharedPrefs.readSharedSetting(MainActivity.this, "check", "true"));

        Intent introIntent = new Intent(MainActivity.this, Sign_In_P.class);
        introIntent.putExtra("CaptainCode", Check);

        //The Value if you click on Login Activity and Set the value is FALSE and whe false the activity will be visible
        if (Check) {
            introIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(introIntent);
            //finish();
        } //If no the Main Activity not Do Anything
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_appoinment) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            fab.show();
            setToolbarTitle();
            fab2.hide();
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                    Appoinment.newInstance()).commit();
        }
        else if (id == R.id.nav_blood) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_BLOOD;
            fab.hide();
            setToolbarTitle();
            fab2.hide();
            //startActivity(new Intent(getApplicationContext(), Patient_List.class));
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                    Blood.newInstance()).commit();
        }
        else if (id == R.id.nav_diseases) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_DISEASES;
            fab.hide();
            setToolbarTitle();
            fab2.hide();
            //startActivity(new Intent(getApplicationContext(), Diseases_List.class));
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                   Diseases.newInstance()).commit();
        }
        else if (id == R.id.nav_hospital) {
            navItemIndex = 3;
            CURRENT_TAG = TAG_HOSPITAL;
            fab.hide();
            setToolbarTitle();
            fab2.hide();
            //startActivity(new Intent(getApplicationContext(), Hospital_RecyclerView.class));
            MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                    Hospital.newInstance()).commit();
        }
        else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), Settings.class));
        }
        else if (id == R.id.nav_feedback) {
            startActivity(new Intent(getApplicationContext(), Feedback.class));
        }
        else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), Abouts.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
