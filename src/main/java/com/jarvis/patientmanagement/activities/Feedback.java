package com.jarvis.patientmanagement.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jarvis.patientmanagement.R;

public class Feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void btnClick(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("email"));
        //i.setPackage("com.google.android.gm");
        //i.setPackage("com.android.email");
        String[] s = {"saidursumon96@gmail.com"};
        i.putExtra(Intent.EXTRA_EMAIL, s);
        i.putExtra(Intent.EXTRA_SUBJECT,"Patient Management : Feedback");
        i.putExtra(Intent.EXTRA_TEXT,"Hi, i am ....");
        i.setType("message/rfc822");
        Intent chosser = Intent.createChooser(i,"Open Email");
        startActivity(chosser);
    }
}
