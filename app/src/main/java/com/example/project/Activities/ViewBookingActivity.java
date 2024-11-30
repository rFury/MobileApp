package com.example.project.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ViewBookingActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");


    private Button back, cancel;

    private TextView name, email, phoneNumber;

    private TextView bookingID, vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost;


    private Booking booking;

    private Vehicle vehicle;

    private User_Details customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        initComponents();
        listenHandler();
        displaySummary();
        displayTotalCost();
    }

    private void initComponents() {
        back = findViewById(R.id.back);
        cancel = findViewById(R.id.cancelBook);
        cancel.setVisibility(View.GONE);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);


        vehicleName = findViewById(R.id.vehicleName);
        rate = findViewById(R.id.rate);
        totalDays = findViewById(R.id.totalDays);
        _pickup = findViewById(R.id.pickup);
        _return = findViewById(R.id.dropoff);


        totalCost = findViewById(R.id.totalCost);


        booking = (Booking) getIntent().getSerializableExtra("BOOKING");

        if (booking.getBookingStatus().equalsIgnoreCase("waiting for approval")){
            cancel.setVisibility(View.VISIBLE);
        }

        DatabaseReference myRef = database.getReference("Vehicles").child(String.valueOf(booking.getVehicleID()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vehicle = dataSnapshot.getValue(Vehicle.class);
                vehicleName.setText(vehicle.fullTitle());
                rate.setText("$"+vehicle.getPrice()+"/Day");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference otRef = database.getReference("User_Details").child(String.valueOf(booking.getCustomerID()));
        otRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customer = dataSnapshot.getValue(User_Details.class);
                FirebaseAuth Auth = FirebaseAuth.getInstance();
                FirebaseUser x = Auth.getCurrentUser();

                name.setText(customer.getF_name()+" "+customer.getL_name());
                email.setText(x.getEmail());
                phoneNumber.setText(String.valueOf(customer.getT_number()));

                bookingID.setText("BookingID: " + booking.getBookingID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        bookingID = findViewById(R.id.bookingID);
    }

    private void listenHandler() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog
                new android.app.AlertDialog.Builder(ViewBookingActivity.this)
                        .setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel this booking?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            DatabaseReference bookingRef = database.getReference("BookedCars").child(String.valueOf(booking.getBookingID()));
                            bookingRef.removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DatabaseReference vehicleRef = database.getReference("Vehicles").child(String.valueOf(booking.getVehicleID()));
                                            vehicleRef.child("availability").setValue(true);
                                            Log.d(TAG, "Booking cancelled successfully.");
                                            finish();
                                        } else {
                                            Log.e(TAG, "Failed to cancel booking.", task.getException());
                                        }
                                    });
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Dismiss the dialog
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            }
        });

    }


    private void displaySummary(){
        totalDays.setText(getDayDifference(booking.pickupTime,booking.returnTime)+" Days");
        _pickup.setText(booking.pickupTime);
        _return.setText(booking.returnTime);
    }

    private void displayTotalCost(){
        totalCost.setText("$"+booking.getTotalCost());
    }


    public static long getDayDifference(String startDateStr, String endDateStr) {
        // Define the date format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a MMMM, dd yyyy");

        try {
            // Parse the start and end dates
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            // Calculate the difference in milliseconds
            long diffInMillis = endDate.getTime() - startDate.getTime();

            // Convert milliseconds to days
            return TimeUnit.MILLISECONDS.toDays(diffInMillis);

        } catch (ParseException e) {
            e.printStackTrace();
            // Return 0 if parsing fails
            return 0;
        }
    }
}
