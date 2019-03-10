package com.jarvis.patientmanagement.users;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.jarvis.patientmanagement.helper.SQLiteHandler_P;
import com.jarvis.patientmanagement.helper.SessionManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Sign_Up_P extends Activity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG3 = Sign_Up_P.class.getSimpleName();
    private static final String TAG2 = "Sign_Up_P";
    private static final String TAG = Sign_Up_P.class.getSimpleName();
    Button btn_return_login_link_p, btnRegister_p;
    private ImageView view_image_signup_p;
    private SpotsDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler_P db;

    PopupMenu pop_up_sign_up_district, blood_group2, gender, pop_up_sign_up_division;

    EditText blood_group_signup_p;
    private AutoCompleteTextView address_district_signup_p, address_division_signup_p;
    EditText birthday_signup_p, gender_signup_p, name_signup_p, address_signup_p, email_signup_p;
    EditText mobile_no_signup_p, password_signup_p;

    private String[] address_district_list, address_division_list;
    private ArrayAdapter<String> division, district;

    private Bitmap bitmap;
    private final int IMG_REQUEST = 1;

    private ProgressDialog pd;
    private FirebaseAuth mAuth;
    String email, password;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Calendar calendar;
    String SelectedDate;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    CheckBox patient_signup_show_password;

    private JSONArray result;
    ArrayAdapter<CharSequence> adapter;
    private ArrayList<String> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__p);

        mAuth = FirebaseAuth.getInstance();
        students = new ArrayList<String>();
        calendar = Calendar.getInstance();
        pd = new ProgressDialog(Sign_Up_P.this);

        view_image_signup_p = (ImageView)findViewById(R.id.view_image_signup_p);
        birthday_signup_p = (EditText)findViewById(R.id.birthday_signup_p);
        btn_return_login_link_p = (Button)findViewById(R.id.btn_return_login_link_p);
        //address_division_signup_p = (EditText)findViewById(R.id.address_division_signup_p);
        //address_district_signup_p = (EditText)findViewById(R.id.address_district_signup_p);
        address_division_signup_p = (AutoCompleteTextView) findViewById(R.id.patient_profile_view_address_division);
        address_district_signup_p = (AutoCompleteTextView) findViewById(R.id.patient_profile_view_address_district);

        blood_group_signup_p = (EditText)findViewById(R.id.blood_group_signup_p);
        gender_signup_p = (EditText)findViewById(R.id.gender_signup_p);
        name_signup_p = (EditText)findViewById(R.id.patient_profile_view_name);
        address_signup_p = (EditText)findViewById(R.id.patient_profile_view_address);
        email_signup_p = (EditText)findViewById(R.id.patient_profile_view_email);
        mobile_no_signup_p = (EditText)findViewById(R.id.patient_profile_view_mobile);
        password_signup_p = (EditText)findViewById(R.id.patient_profile_view_password);
        btnRegister_p = (Button)findViewById(R.id.btnRegister_p);
        patient_signup_show_password = (CheckBox)findViewById(R.id.patient_signup_show_password);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler_P(getApplicationContext());

        PopUpDivision();
        PopUpDistrict();
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

        btnRegister_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();
                //progressStyle();
                //Intent intent = new Intent(getApplicationContext(), Sign_In_P.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            }
        });

        gender_signup_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuGender();
            }
        });

        blood_group_signup_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuBloodGroup2();
            }
        });

        birthday_signup_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Year = calendar.get(Calendar.YEAR);
                Month = calendar.get(Calendar.MONTH);
                Day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = DatePickerDialog.newInstance(Sign_Up_P.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#139469"));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //Toast.makeText(Sign_Up_P.this, "Date Not Selected !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_return_login_link_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sign_In_P.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        view_image_signup_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage2();
                //Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(i, 100);
            }
        });
    }

    public void ShowPassword(){

        patient_signup_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password_signup_p.setTransformationMethod(null);
                }
                else {
                    password_signup_p.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    public void validation(){

        String name = name_signup_p.getText().toString().trim();
        String address = address_signup_p.getText().toString().trim();
        String division = address_division_signup_p.getText().toString().trim();
        String district = address_district_signup_p.getText().toString().trim();
        email = email_signup_p.getText().toString().trim();

        String birthday = birthday_signup_p.getText().toString().trim();
        String gender = gender_signup_p.getText().toString().trim();
        String mobile = mobile_no_signup_p.getText().toString().trim();
        String blood = blood_group_signup_p.getText().toString().trim();
        password = password_signup_p.getText().toString().trim();

        if (!name.isEmpty() && !address.isEmpty() && !division.isEmpty() && !district.isEmpty() && !email.isEmpty()
                && !birthday.isEmpty() && !gender.isEmpty() && !mobile.isEmpty() && !blood.isEmpty() && !password.isEmpty()) {

            progressStyle();
            //checkSignup();
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_SHORT).show();
        }
    }

    public void UP(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, IP_Config.URL_REGISTER_P, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String check = "Success Sign Up";
                String check1 = "Error Sign Up";

                if(response.equals(check)) {
                    Toast.makeText(getApplicationContext(), "Registration Successfully Completed.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Sign_In_P.class);
                            //Intent intent = new Intent(getApplicationContext(), Email_Verify.class);
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

                params.put("name", name_signup_p.getText().toString());
                params.put("address", address_signup_p.getText().toString());
                params.put("division", address_division_signup_p.getText().toString());
                params.put("district", address_district_signup_p.getText().toString());
                params.put("email", email_signup_p.getText().toString());
                params.put("birthday", birthday_signup_p.getText().toString());
                params.put("gender", gender_signup_p.getText().toString());
                params.put("mobile", mobile_no_signup_p.getText().toString());
                params.put("blood", blood_group_signup_p.getText().toString());
                params.put("password", password_signup_p.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    public void checkSignup(){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Sign_Up_P.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG2, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {

                            UP();

                            Intent intent = new Intent(getApplicationContext(), Email_Verify.class);
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

    public void PopUpDivision(){

        address_division_list = getResources().getStringArray(R.array.all_division);
        division = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, address_division_list);
        address_division_signup_p.setThreshold(1);
        address_division_signup_p.setAdapter(division);
    }

    public void PopUpDistrict(){

        address_district_list = getResources().getStringArray(R.array.all_district);
        district = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, address_district_list);
        address_district_signup_p.setThreshold(3);
        address_district_signup_p.setAdapter(district);
    }

    public void PopupMenuBloodGroup2(){
        blood_group2 = new PopupMenu(Sign_Up_P.this, blood_group_signup_p);
        blood_group2.getMenuInflater().inflate(R.menu.pop_up_blood_group, blood_group2.getMenu());
        blood_group2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Toast.makeText(Sign_Up_P.this, item.getTitle(),Toast.LENGTH_LONG).show();
                blood_group_signup_p.setText(item.getTitle());
                return true;
            }
        });
        blood_group2.show();
    }

    public void PopupMenuGender(){
        gender = new PopupMenu(Sign_Up_P.this, gender_signup_p);
        gender.getMenuInflater().inflate(R.menu.pop_up_gender, gender.getMenu());
        gender.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Toast.makeText(Sign_Up_P.this, item.getTitle(),Toast.LENGTH_LONG).show();
                gender_signup_p.setText(item.getTitle());
                return true;
            }
        });
        gender.show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        //String date = "Selected Date : " + Day + "-" + (Month+1) + "-" + Year;
        SelectedDate = Day + "-" + (Month+1) + "-" + Year;
        birthday_signup_p.setText(SelectedDate);
        //Toast.makeText(Sign_Up_P.this, SelectedDate, Toast.LENGTH_SHORT).show();
    }

    private void selectImage2(){
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent2, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                view_image_signup_p.setImageBitmap(bitmap);
                view_image_signup_p.setVisibility(View.VISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void progressStyle(){
        progressDialog = new SpotsDialog(Sign_Up_P.this, R.style.Custom);
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

                        UP();
                        //checkSignup();
//                        Toast.makeText(getApplicationContext(), "Completed.", Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(getApplicationContext(), Sign_In_P.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                });
            }
        }).start();
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
