package com.example.project.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activities.ViewBookingActivity;
import com.example.project.Adapter.BookingAdapter;
import com.example.project.Model.Booking;
import com.example.project.R;
import com.example.project.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment implements BookingAdapter.onBookingListener{

    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseUser x = Auth.getCurrentUser();

    private RecyclerView recyclerView;
    private ArrayList<Booking> bookings;
    private BookingAdapter bookingAdapter;

    private String customerID;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents(root);
        return root;

    }

    private void initComponents(View view) {
        bookings= new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        customerID = x.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("BookedCars");
        Query query = myRef.orderByChild("customerID").equalTo(customerID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookings.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking b = new Booking();
                    b.setBookingID(snapshot.child("bookingID").getValue(String.class)); // Assuming ID is a String
                    b.setCustomerID(snapshot.child("customerID").getValue(String.class));
                    String pickupTime = snapshot.child("pickupTime").getValue(String.class);
                    String returnTime = snapshot.child("returnTime").getValue(String.class);
                    b.setPickupTime((pickupTime));
                    b.setReturnTime((returnTime));
                    b.setTotalCost(snapshot.child("totalCost").getValue(Double.class));
                    b.setVehicleID(snapshot.child("vehicleID").getValue(Integer.class));
                    b.setBookingStatus(snapshot.child("bookingStatus").getValue(String.class));
                    bookings.add(b);
                }
                bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        bookingAdapter = new BookingAdapter(getContext(),bookings,this);
        recyclerView.setAdapter(bookingAdapter);
    }

    @Override
    public void onClick(int position) {
        String bookingID = bookings.get(position).getBookingID();
        Intent viewBooking = new Intent(getContext(), ViewBookingActivity.class);
        viewBooking.putExtra("BOOKING",bookings.get(position));
        startActivity(viewBooking);
    }

    private void toast(String txt){
        Toast toast = Toast.makeText(getContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }

    private Calendar parseTimeToCalendar(String timeString) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            Date time = timeFormat.parse(timeString);
            if (time != null) {
                calendar.setTime(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}