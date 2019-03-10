package com.jarvis.patientmanagement.users;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.config.IP_Config;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Sign_Up_D extends Activity implements TimePickerDialog.OnTimeSetListener{

    Button btn_return_login_link_d, btnRegister_d;
    private ImageView view_image_signup_d;
    private static final String TAG2 = "Sign_Up_D";
    EditText end_time_signup_d, start_time_signup_d, chamber_days_signup_d;
    EditText expertise_signup_d, blood_group_signup_d, password_signup_d, regirtration_no_d;
    EditText name_signup_d, address_signup_d, email_signup_d, mobile_no_signup_d;

    private Bitmap bitmap;
    CheckBox doctor_signup_show_password;
    private final int IMG_REQUEST = 1;
    private SpotsDialog progressDialog;

    String email, password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String[] listItems, listItems2;
    boolean[] checkedItem, checkedItem2;
    ArrayList<Integer> mUserItems, mUserItems2;

    Calendar calendar, calendar2 ;
    PopupMenu blood_group;
    String SelectedTime, SelectedTime2;
    TimePickerDialog timePickerDialog, timePickerDialog2 ;
    int CalendarHour, CalendarMinute;
    int CalendarHour2, CalendarMinute2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__d);

        mAuth = FirebaseAuth.getInstance();
        mUserItems = new ArrayList<>();
        mUserItems2 = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        listItems = getResources().getStringArray(R.array.chamber_days_signup_d);
        checkedItem = new boolean[listItems.length];
        listItems2 = getResources().getStringArray(R.array.pop_up_sign_up_expertise);
        checkedItem2 = new boolean[listItems2.length];

        view_image_signup_d = (ImageView)findViewById(R.id.view_image_signup_d);
        btn_return_login_link_d = (Button)findViewById(R.id.btn_return_login_link_d);
        name_signup_d = (EditText)findViewById(R.id.name_signup_d);
        address_signup_d = (EditText)findViewById(R.id.address_signup_d);
        email_signup_d = (EditText)findViewById(R.id.email_signup_d);
        regirtration_no_d = (EditText)findViewById(R.id.regirtration_no_d);
        chamber_days_signup_d = (EditText)findViewById(R.id.chamber_days_signup_d);
        expertise_signup_d = (EditText)findViewById(R.id.expertise_signup_d);
        start_time_signup_d = (EditText)findViewById(R.id.start_time_signup_d);
        end_time_signup_d = (EditText)findViewById(R.id.end_time_signup_d);
        mobile_no_signup_d = (EditText)findViewById(R.id.mobile_no_signup_d);
        blood_group_signup_d = (EditText)findViewById(R.id.blood_group_signup_d);
        btnRegister_d = (Button)findViewById(R.id.btnRegister_d);
        password_signup_d = (EditText)findViewById(R.id.password_signup_d);
        doctor_signup_show_password = (CheckBox)findViewById(R.id.doctor_signup_show_password);

        ShowPassword();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG2, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    Log.d(TAG2, "onAuthStateChanged:signed_out");
                }
            }
        };

        btnRegister_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation2();
                //progressStyle();
                //Intent intent = new Intent(getApplicationContext(), Sign_In_D.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            }
        });

        //view_image_signup_d.setOnClickListener(this);

        expertise_signup_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuExpertise();
            }
        });

        blood_group_signup_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuBloodGroup();
            }
        });

        chamber_days_signup_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuChamberDays();
            }
        });

        start_time_signup_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = TimePickerDialog.newInstance(Sign_Up_D.this, CalendarHour, CalendarMinute, false);
                timePickerDialog.setThemeDark(false);
                timePickerDialog.setAccentColor(Color.parseColor("#139469"));
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        //Toast.makeText(Sign_Up_D.this, "Time Not Selected !", Toast.LENGTH_SHORT).show();
                    }
                });
                timePickerDialog.show(getFragmentManager(), "Material Design TimePicker Dialog");
            }
        });

        end_time_signup_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarHour2 = calendar2.get(Calendar.HOUR_OF_DAY);
                CalendarMinute2 = calendar2.get(Calendar.MINUTE);

                timePickerDialog2 = TimePickerDialog.newInstance(Sign_Up_D.this, CalendarHour2, CalendarMinute2, false);
                timePickerDialog2.setThemeDark(false);
                timePickerDialog2.setAccentColor(Color.parseColor("#569749"));
                timePickerDialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        //Toast.makeText(Sign_Up_D.this, "Time Not Selected !", Toast.LENGTH_SHORT).show();
                    }
                });
                timePickerDialog2.show(getFragmentManager(), "Material Design TimePicker Dialog2");
            }
        });

        btn_return_login_link_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sign_In_D.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        view_image_signup_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                //Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(i, 100);
            }
        });
    }

    public void validation2(){

        String name = name_signup_d.getText().toString().trim();
        String address = address_signup_d.getText().toString().trim();
        email = email_signup_d.getText().toString().trim();
        String registration = regirtration_no_d.getText().toString().trim();
        String expertise = expertise_signup_d.getText().toString().trim();
        String chamber = chamber_days_signup_d.getText().toString().trim();
        String start = start_time_signup_d.getText().toString().trim();
        String end = end_time_signup_d.getText().toString().trim();
        String mobile = mobile_no_signup_d.getText().toString().trim();
        String blood = blood_group_signup_d.getText().toString().trim();
        password = password_signup_d.getText().toString().trim();

      if (!name.isEmpty() && !address.isEmpty() && !email.isEmpty() && !expertise.isEmpty() && !chamber.isEmpty() && !start.isEmpty()
                && !registration.isEmpty() &&!end.isEmpty() && !mobile.isEmpty() && !blood.isEmpty() && !password.isEmpty()) {

            progressStyle();
            //checkSignup();
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_SHORT).show();
        }
    }

    public void UP2(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_REGISTER_D, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String check = "User Logged Up";
                String check1 = "Error Sign Up";

                if(response.equals(check)) {

                    Toast.makeText(getApplicationContext(), "Registration Successfully Completed.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Sign_In_D.class);
                    //Intent intent = new Intent(getApplicationContext(), Email_Verify_D.class);
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

                params.put("name", name_signup_d.getText().toString());
                params.put("address", address_signup_d.getText().toString());
                params.put("email", email_signup_d.getText().toString());
                params.put("registration", regirtration_no_d.getText().toString());
                params.put("expertise", expertise_signup_d.getText().toString());
                params.put("chamber", chamber_days_signup_d.getText().toString());
                params.put("start", start_time_signup_d.getText().toString());
                params.put("end", end_time_signup_d.getText().toString());
                params.put("mobile", mobile_no_signup_d.getText().toString());
                params.put("blood", blood_group_signup_d.getText().toString());
                params.put("password", password_signup_d.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    public void checkSignup2(){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Sign_Up_D.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG2, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {

                            UP2();

                            Intent intent = new Intent(getApplicationContext(), Email_Verify_D.class);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Registration Successfully Completed.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            toastMessage("Email Address Already Exist !");
                        }
                    }
                });
    }

    public void ShowPassword(){

        doctor_signup_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password_signup_d.setTransformationMethod(null);
                }
                else {
                    password_signup_d.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    public void PopupMenuBloodGroup(){
        blood_group = new PopupMenu(Sign_Up_D.this, blood_group_signup_d);
        blood_group.getMenuInflater().inflate(R.menu.pop_up_blood_group, blood_group.getMenu());
        blood_group.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Toast.makeText(Sign_Up_D.this, item.getTitle(),Toast.LENGTH_LONG).show();
                blood_group_signup_d.setText(item.getTitle());
                return true;
            }
        });
        blood_group.show();
    }

    public void PopupMenuChamberDays(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Sign_Up_D.this);
        mBuilder.setTitle("Select Chamber Days");
        mBuilder.setCancelable(true);
        mBuilder.setMultiChoiceItems(listItems,checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if(isChecked){
                    mUserItems.add(position);
                }
                else{
                    mUserItems.remove((Integer.valueOf(position)));
                    //chamber_days_signup_d.setText("");
                }
            }
        });
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";

                for (int i=0; i<mUserItems.size();i++){
                    item = item + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() -1){
                        item = item + ", ";
                    }
                }
                //Toast.makeText(getApplicationContext(),item ,Toast.LENGTH_SHORT).show();
                chamber_days_signup_d.setText(item);
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i=0; i<checkedItem.length; i++){
                    checkedItem[i] = false;
                    mUserItems.clear();
                    chamber_days_signup_d.setText("");
                }
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mBuilder.show().getWindow().setLayout(650,900);
    }

    public void PopupMenuExpertise(){

        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(Sign_Up_D.this);
        mBuilder2.setTitle("Select Expertise On");
        mBuilder2.setCancelable(true);
        mBuilder2.setMultiChoiceItems(listItems2,checkedItem2, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked){
                    mUserItems2.add(position);
                }
                else{
                    mUserItems2.remove((Integer.valueOf(position)));
                    //expertise_signup_d.setText("");
                }
            }
        });
        mBuilder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";

                for (int i=0; i<mUserItems2.size(); i++){
                    item = item + listItems2[mUserItems2.get(i)];
                    if (i != mUserItems2.size() -1){
                        item = item + ", ";
                    }
                }
                //Toast.makeText(getApplicationContext(),item ,Toast.LENGTH_SHORT).show();
                expertise_signup_d.setText(item);
            }
        });
        mBuilder2.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i=0; i<checkedItem2.length; i++){
                    checkedItem2[i] = false;
                    mUserItems2.clear();
                    expertise_signup_d.setText("");
                }
            }
        });
        AlertDialog mDialog2 = mBuilder2.create();
        mBuilder2.show().getWindow().setLayout(650,900);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        String timeSet = "";
        if (hourOfDay > 12) {
                hourOfDay -= 12;
                timeSet = "PM";
        }
        else if (hourOfDay == 0) {
                hourOfDay += 12;
                timeSet = "AM";
        }
        else if (hourOfDay == 12){
                timeSet = "PM";
        }
        else {
            timeSet = "AM";
        }

        String minutes = "";
        if (minute < 10)
            minutes = "0" + minute;
        else {
            minutes = String.valueOf(minute);
        }
        //SelectedTime = "Selected Time is " + hourOfDay + " : " + minutes + " " +timeSet ;
        SelectedTime = hourOfDay + " : " + minutes + " " +timeSet ;
        SelectedTime2 = hourOfDay + " : " + minutes + " " +timeSet ;
        start_time_signup_d.setText(SelectedTime);
        end_time_signup_d.setText(SelectedTime2);

        //Toast.makeText(Sign_Up_D.this, SelectedTime, Toast.LENGTH_SHORT).show();
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    public void progressStyle(){
        progressDialog = new SpotsDialog(Sign_Up_D.this, R.style.Custom);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        //checkSignup2();
                        UP2();
//                        Toast.makeText(getApplicationContext(), "Completed.", Toast.LENGTH_SHORT).show();
//
//                        SharedPrefs.saveSharedSetting(Sign_Up_D.this, "check", "false");
//                        Intent intent = new Intent(getApplicationContext(), Sign_In_D.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                view_image_signup_d.setImageBitmap(bitmap);
                view_image_signup_d.setVisibility(View.VISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
