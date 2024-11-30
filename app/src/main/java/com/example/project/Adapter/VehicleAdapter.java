package com.example.project.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.project.Model.Vehicle;
import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {

    private Context context;
    private ArrayList<Vehicle> vehicle;
    private onVehicleListener onVehicleListener;

    public VehicleAdapter(Context context, ArrayList<Vehicle> vehicle, onVehicleListener onVehicleListener){
        this.context = context;
        this.vehicle = vehicle;
        this.onVehicleListener = onVehicleListener;
    }

    @NonNull
    @Override
    public VehicleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vehicle_card,null);
        return new VehicleHolder(view,onVehicleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleHolder vehicleHolder, int i) {
        Vehicle v = vehicle.get(i);
        vehicleHolder.vehicle.setText(v.getYear() + " " + v.getManufacturer() + " " + v.getModel());
        vehicleHolder.price.setText("$"+v.getPrice()+"/day");
        vehicleHolder.category.setText(v.getCategory());
        Picasso.get().load(v.getVehicleImageURL()).into(vehicleHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return vehicle.size();
    }

    class VehicleHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView vehicle, detail, price;
        ImageView imageView;
        ConstraintLayout card;
        onVehicleListener onVehicleListener;
        Button category;
        public VehicleHolder(@NonNull View itemView, onVehicleListener onVehicleListener) {
            super(itemView);
            vehicle = itemView.findViewById(R.id.vehicle);
            detail = itemView.findViewById(R.id.detail);
            card = itemView.findViewById(R.id.card);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.vehicleImage);
            category = itemView.findViewById(R.id.book);

            this.onVehicleListener = onVehicleListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onVehicleListener.onClick(getAdapterPosition());
        }
    }

    public interface onVehicleListener{
        void onClick(int position);
    }

}
