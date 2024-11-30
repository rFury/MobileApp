package com.example.project.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.project.MainActivity;
import com.example.project.Model.Booking;
import com.example.project.Model.User_Details;
import com.example.project.Model.Vehicle;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Random;

public class BookingSummaryActivity extends AppCompatActivity {

    private Boolean paid = false;


    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");

    private Button back, book;

    private TextView name, email, phoneNumber;

    private TextView vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost;

    private ImageView vehicleImage;


    private Booking booking;

    private Vehicle vehicle;

    private ProgressBar paidLoading;

    private CardView booking_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        initComponents();

        listenHandler();
        displayCustomerInformation();
        displaySummary();
        displayTotalCost();

    }

    private void initComponents() {
        back = findViewById(R.id.back);
        book = findViewById(R.id.book);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);

        vehicleName = findViewById(R.id.vehicleName);
        rate = findViewById(R.id.rate);
        totalDays = findViewById(R.id.totalDays);
        _pickup = findViewById(R.id.pickup);
        _return = findViewById(R.id.dropoff);

        totalCost = findViewById(R.id.totalCost);

        vehicleImage = findViewById(R.id.vehicleImage);

        booking_complete = findViewById(R.id.booking_complete);

        booking = (Booking) getIntent().getSerializableExtra("BOOKING");
        vehicle = (Vehicle) getIntent().getSerializableExtra("Vehicle");
    }

    private void listenHandler() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paid){finish();}
                else{
                    Intent bookingCompletePage = new Intent(BookingSummaryActivity.this, MainActivity.class);
                    bookingCompletePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Add flags to clear all previous activities
                    startActivity(bookingCompletePage);
                    finish(); // Finish the current activity
                }
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBilling_Payment();

                vehicleImage.setVisibility(View.INVISIBLE);
                booking_complete.setVisibility(View.VISIBLE);
                book.setEnabled(false);
                book.setVisibility(View.GONE);
                paid = true;
                /*Intent bookingCompletePage = new Intent(BookingSummaryActivity.this,BookingCompleteActivity.class);
                bookingCompletePage.putExtra("BOOKING",booking);
                startActivity(bookingCompletePage);*/
            }
        });

    }

    private void generateBilling_Payment() {


        Calendar currentDate = Calendar.getInstance();

        double cost = calculateTotalCost();


        Intent i = getIntent();
        Integer vehcileID = vehicle.getVehicleID();
        DatabaseReference myRef = database.getReference("BookedCars");
        String generatedId = myRef.push().getKey();
        booking.setBookingID(generatedId);
        booking.setTotalCost(cost);
        myRef.child(generatedId).setValue(booking);

        DatabaseReference vehicleRef = database.getReference("Vehicles").child(String.valueOf(vehcileID));
        vehicleRef.child("availability").setValue(false);
    }

    private void displayCustomerInformation() {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser x = Auth.getCurrentUser();

        if (x != null) {
            DatabaseReference myRef = database.getReference("User_Details").child(x.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User_Details user = dataSnapshot.getValue(User_Details.class);

                    if (user != null) {
                        String firstName = user.getF_name() != null ? user.getF_name() : "Unknown";
                        String lastName = user.getL_name() != null ? user.getL_name() : "Unknown";
                        name.setText(firstName + " " + lastName);

                        String userEmail = x.getEmail() != null ? x.getEmail() : "No email";
                        email.setText(userEmail);

                        phoneNumber.setText(user.getT_number().toString());
                    } else {
                        Log.e(TAG, "User data is null");
                        name.setText("Unknown User");
                        email.setText("No email");
                        phoneNumber.setText("No phone number");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        } else {
            Log.e(TAG, "Firebase user is null");
            name.setText("Unknown User");
            email.setText("No email");
            phoneNumber.setText("No phone number");
        }
    }


    private void displaySummary(){

        vehicleName.setText(vehicle.fullTitle());
        rate.setText("$"+vehicle.getPrice()+"/Day");
        totalDays.setText(getDayDifference(booking.getPickupDate(),booking.getReturnDate())+" Days");
        _pickup.setText(booking.getPickupTime());
        _return.setText(booking.getReturnTime());
        Picasso.get().load(vehicle.getVehicleImageURL()).into(vehicleImage);
    }

    private void displayTotalCost(){
        double cost = calculateTotalCost();
        totalCost.setText("$"+cost);
    }


    private long getDayDifference(Calendar start, Calendar end){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ChronoUnit.DAYS.between(start.toInstant(), end.toInstant()) + 2;
        }
        return 0;
    }

    private double calculateTotalCost(){
        long _days = getDayDifference(booking.getPickupDate(),booking.getReturnDate());
        double _vehicleRate = vehicle.getPrice();

        return (_days*_vehicleRate);
    }


    //DEBUGING
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }

}
