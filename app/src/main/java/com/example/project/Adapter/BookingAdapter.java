package com.example.project.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


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

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder>{

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");

    private Context context;
    private ArrayList<Booking> bookings;
    private onBookingListener onBookingListener;
    private Vehicle _vehicle;
    private User_Details _customer;


    public BookingAdapter(Context context, ArrayList<Booking> bookings, BookingAdapter.onBookingListener onBookingListener) {
        this.context = context;
        this.bookings = bookings;
        this.onBookingListener = onBookingListener;
    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.booking_card,null);
        return new BookingHolder(view,onBookingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder bookingHolder, int position) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        Booking _booking = bookings.get(position);

        // Load vehicle data
        DatabaseReference vehicleRef = database.getReference("Vehicles").child(String.valueOf(_booking.getVehicleID()));
        vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Vehicle _vehicle = dataSnapshot.getValue(Vehicle.class);
                if (_vehicle != null) {
                    bookingHolder.vehicleName.setText(_vehicle.fullTitle());
                } else {
                    bookingHolder.vehicleName.setText("Unknown Vehicle");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read vehicle data.", error.toException());
            }
        });

        // Load user details
        if (user != null) {
            DatabaseReference userRef = database.getReference("User_Details").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User_Details _customer = snapshot.getValue(User_Details.class);
                    if (_customer != null) {
                        bookingHolder.customerName.setText(_customer.getF_name() + " " + _customer.getL_name());
                    } else {
                        bookingHolder.customerName.setText("Unknown Customer");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read user data.", error.toException());
                }
            });
        }

        bookingHolder.bookingID.setText(String.valueOf(_booking.getBookingID()));
        bookingHolder.pickupDate.setText(_booking.pickupTime);
        bookingHolder.returnDate.setText(_booking.returnTime);
        bookingHolder.bookingStatus.setText(_booking.getBookingStatus());
        if (_booking.getBookingStatus().equalsIgnoreCase("waiting for approval")){
            bookingHolder.bookingStatus.setTextColor(ContextCompat.getColor(bookingHolder.itemView.getContext(), R.color.waiting_for_approval));
        } else if (_booking.getBookingStatus().equalsIgnoreCase("overdue")) {
            bookingHolder.bookingStatus.setTextColor(ContextCompat.getColor(bookingHolder.itemView.getContext(), R.color.overdue));
        } else if (_booking.getBookingStatus().equalsIgnoreCase("accepted")) {
            bookingHolder.bookingStatus.setTextColor(ContextCompat.getColor(bookingHolder.itemView.getContext(), R.color.accepted));
        }
    }


    @Override
    public int getItemCount() { return bookings.size(); }

    class BookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView vehicleName, bookingID, customerName,
                 pickupDate, returnDate, bookingStatus;
        onBookingListener onBookingListener;

        public BookingHolder(@NonNull View itemView, onBookingListener onBookingListener) {
            super(itemView);

            vehicleName = itemView.findViewById(R.id.vehicleName);
            bookingID = itemView.findViewById(R.id.bookingID);
            customerName = itemView.findViewById(R.id.customerName);
            pickupDate = itemView.findViewById(R.id.pickupDate);
            returnDate = itemView.findViewById(R.id.returnDate);
            bookingStatus = itemView.findViewById(R.id.bookingStatus);

            this.onBookingListener = onBookingListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onBookingListener.onClick(getAdapterPosition());
        }
    }

    public interface onBookingListener{
        void onClick(int position);
    }
}
