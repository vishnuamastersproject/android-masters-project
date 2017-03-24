package com.vishnuapp.restaurant;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ReservationActivity extends AppCompatActivity {
    static TextView dateTextView;
    static TextView timeTextView;
    static EditText messageEditText;
    ArrayAdapter arrayAdapter;
    Spinner spinner;
    ReservationRepo reservationRepo;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        setTitle("Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkSmsPermission();
        checkCallPermission();
        reservationRepo = new ReservationRepo(this);

        listView = (ListView) findViewById(R.id.listView);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        //adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listItems);

        listView.setAdapter(adapter);

        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);

        String array_spinner[] = new String[10];
        array_spinner[0] = "1";
        array_spinner[1] = "2";
        array_spinner[2] = "3";
        array_spinner[3] = "4";
        array_spinner[4] = "5";
        array_spinner[5] = "6";
        array_spinner[6] = "7";
        array_spinner[7] = "8";
        array_spinner[8] = "9";
        array_spinner[9] = "10";

        spinner = (Spinner) findViewById(R.id.spinner);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        spinner.setAdapter(arrayAdapter);

        fillReservations();
    }

    public void fillReservations() {
        Cursor cursor = reservationRepo.reservationsByUserId(UserRepo.userId);
        //String text="";
        listItems.clear();// = new ArrayList<String>();
        if(cursor!=null){
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String reservationTime = cursor.getString(2);
                String partySize = cursor.getString(3);
               // text+="# " + t;
                listItems.add(reservationTime+"   -   "+ partySize);
                cursor.moveToNext();
            }
            cursor.close();
            // adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
            // listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            // int size = listItems.size();
            // Toast.makeText(getApplicationContext(),"Size: "+size, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
        return true;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void callUs(View v) {
        String phnum = "5556";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));

        try {
            startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void save(View v) {
        String reservationDate = dateTextView.getText().toString();//
        String reservationTime = timeTextView.getText().toString();
        long partySize = spinner.getSelectedItemId()+1;
        long userId = UserRepo.userId;
//yyyy-MM-dd HH:mm:ss
        //'2007-01-01 10:00:00'
        if(reservationRepo.save(userId,reservationDate+" "+reservationTime+":00",(int)partySize)){
            Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show();
            fillReservations();
        }else{
            Toast.makeText(this,"Unable to save", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS2(View v) {
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5556", null, messageEditText.getText().toString(), null, null);
            Toast.makeText(this, "SMS sent.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void checkSmsPermission() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.SEND_SMS
                        }, 10);
            }
            return;
        }
    }

    void checkCallPermission() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.CALL_PHONE
                        }, 10);
            }
            return;
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeTextView.setText((hourOfDay<10?"0"+hourOfDay:hourOfDay) + ":" + (minute <10 ? "0"+minute : minute));//

        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dayString = "";
            if(day<10){
                dayString = "0"+day;
            }else{
                dayString = ""+day;
            }
            dateTextView.setText(year + "-" + (++month<10?("0"+month):month )+ "-" + dayString);//2016-08-02
        }
    }
}
