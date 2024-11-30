package com.example.project.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Model.Vehicle;
import com.example.project.R;
import com.squareup.picasso.Picasso;

public class VehicleInfoActivity extends AppCompatActivity {

    //VEHICLE OBJECT
    private Vehicle vehicle;
    //VEHICLE TITLE
    private TextView vehicleTitle;
    //VEHICLE IMAGE OBJECT
    private ImageView vehicleImage;
    //VEHICLE PRICE
    private TextView vehiclePrice;

    //VEHICLE AVAILABILITY FIELD
    private ConstraintLayout available;
    private ConstraintLayout notAvailable;

    //GOING BACK BUTTON
    private Button back;
    private Button book;

    //VEHICLE INFO FIELD
    private TextView year, manufacturer, model, mileage, seats, type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);

        initComponents();
        listenHandler();
        displayVehicleInfo();



    }

    private void initComponents() {

        vehicle = (Vehicle) getIntent().getSerializableExtra("VEHICLE");
        back = findViewById(R.id.back);
        vehicleTitle = findViewById(R.id.vehicleTitle);
        vehicleImage = findViewById(R.id.vehicleImage);

        available = findViewById(R.id.available);
        notAvailable = findViewById(R.id.notAvailable);

        year = findViewById(R.id.year);
        manufacturer = findViewById(R.id.manufacturer);
        model = findViewById(R.id.model);
        mileage = findViewById(R.id.mileage);
        seats = findViewById(R.id.seats);
        type = findViewById(R.id.type);

        //VEHICLE PRICE
        vehiclePrice = findViewById(R.id.vehiclePrice);


        //BOOK BUTTON
        book = findViewById(R.id.book_this_car);

    }

    private void listenHandler() {

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VehicleInfoActivity.this,BookingCarActivity.class);
                i.putExtra("Vehicle",vehicle);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void displayVehicleInfo() {
        vehicleTitle.setText(vehicle.fullTitle());
        Picasso.get().load(vehicle.getVehicleImageURL()).into(vehicleImage);
        if(vehicle.isAvailability()){
            available.setVisibility(ConstraintLayout.VISIBLE);
            notAvailable.setVisibility(ConstraintLayout.INVISIBLE);
            book.setEnabled(true);
            book.setBackground(ContextCompat.getDrawable(VehicleInfoActivity.this,R.drawable.round_button));
            book.setText("Book This Car");
        }else{
            available.setVisibility(ConstraintLayout.INVISIBLE);
            notAvailable.setVisibility(ConstraintLayout.VISIBLE);
            book.setEnabled(false);
            book.setBackground(ContextCompat.getDrawable(VehicleInfoActivity.this,R.drawable.disable_button));
            book.setText("Vehicle Not Available");
        }

        year.setText(vehicle.getYear()+"");
        manufacturer.setText(vehicle.getManufacturer());
        model.setText(vehicle.getModel());
        mileage.setText(vehicle.getMileage()+"");
        seats.setText(vehicle.getSeats()+"");
        type.setText(vehicle.getCategory());

        vehiclePrice.setText("$" + vehicle.getPrice()+"/Day");

    }

    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }

}
