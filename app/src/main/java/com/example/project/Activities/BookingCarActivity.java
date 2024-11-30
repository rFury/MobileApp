package com.example.project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project.Model.Booking;
import com.example.project.Model.Vehicle;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class BookingCarActivity extends AppCompatActivity {

    //PICKUP AND RETURN DATE
    private TextView pickupDate, returnDate;

    //PICKUP AND RETURN TIME
    private TextView pickupTime, returnTime;

    //PICKUP DATE/TIME
    private Calendar _pickup;

    //RETURN DATE/TIME
    private Calendar _return;


    //DATE FORMAT -> FOR DISPLAY PURPOSE
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, d yyyy", Locale.CANADA);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.CANADA);

    //DATE/TIME STORING
    //GOING BACK BUTTON and CONTINUE BOOKING BUTTON
    private Button back, continueBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_car);

        initComponents();
        listenHandler();
    }

    private void initComponents() {
        back = findViewById(R.id.back);
        continueBooking = findViewById(R.id.continueBooking);

        pickupDate = findViewById(R.id.pickupDate);
        pickupTime = findViewById(R.id.pickupTime);

        returnDate = findViewById(R.id.returnDate);
        returnTime = findViewById(R.id.returnTime);


        _pickup = Calendar.getInstance();
        _return = Calendar.getInstance();


        pickupDate.setText(dateFormat.format(_pickup.getTime()));
        pickupTime.setText(timeFormat.format(_pickup.getTime()));

        returnDate.setText(dateFormat.format(_return.getTime()));
        returnTime.setText(timeFormat.format(_return.getTime()));
    }

    private void listenHandler() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        continueBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent bookingSummaryPage = new Intent(BookingCarActivity.this, BookingSummaryActivity.class);
                startActivity(bookingSummaryPage);
            }
        });

        pickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(_pickup,pickupDate);
            }
        });
        pickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(_pickup, pickupTime);
            }
        });

        returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(_return,returnDate);
            }
        });
        returnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openTimePicker(_return, returnTime);
            }
        });

        continueBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate() {


        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();
        Intent i = getIntent();
        Vehicle vehcile = new Vehicle();
        vehcile = (Vehicle) i.getSerializableExtra("Vehicle");

        Booking newBooking = new Booking(_pickup,_return,"waiting for approval",user.getUid(),vehcile.getVehicleID());


        Intent bookingSummary = new Intent(BookingCarActivity.this,BookingSummaryActivity.class);
        bookingSummary.putExtra("BOOKING",newBooking);
        bookingSummary.putExtra("Vehicle",vehcile);
        startActivity(bookingSummary);

    }

    //OPEN CALENDAR DIALOG
    private void openCalendar(final Calendar rentalDate, final TextView rentalDateText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                rentalDate.set(year,month,dayOfMonth);
                rentalDateText.setText(dateFormat.format(rentalDate.getTime()));
            }
        });

        datePickerDialog.show();
    }

    //OPEN TIMEPICKER DIALOG
    private Date openTimePicker(final Calendar rentalTime, final TextView rentalTimeText){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);



        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                rentalTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                rentalTime.set(Calendar.MINUTE,minute);

                rentalTimeText.setText(timeFormat.format(rentalTime.getTime()));
            }
        },hour,min,false);

        timePickerDialog.show();

        return calendar.getTime();
    }

    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }

}
