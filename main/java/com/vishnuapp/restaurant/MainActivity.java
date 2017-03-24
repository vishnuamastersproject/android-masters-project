package com.vishnuapp.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
    public boolean onOptionsItemSelected(MenuItem item){
        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        return true;
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, LoginActivity.class));
        // code here to show dialog
       // super.onBackPressed();  // optional depending on your needs
    }

    public void openReservationActivity(View view) {
        startActivity(new Intent(this, ReservationActivity.class));
    }
    public void openEventsActivity(View view) {
        startActivity(new Intent(this, EventsActivity.class));
    }
    public void openMenuActivity(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }
    public void openLocationActivity(View view) {
        startActivity(new Intent(this, LocationActivity.class));
    }
    public void openTestimonialActivity(View view) {
        startActivity(new Intent(this, TestimonialActivity.class));
    }
}
