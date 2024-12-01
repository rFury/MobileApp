package com.example.project.ui.home;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.toLowerCase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activities.VehicleInfoActivity;
import com.example.project.Adapter.VehicleAdapter;
import com.example.project.Model.Vehicle;
import com.example.project.Model.VehicleComp;
import com.example.project.R;
import com.example.project.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class HomeFragment extends Fragment implements VehicleAdapter.onVehicleListener{

    private FragmentHomeBinding binding;


    private RecyclerView recyclerView;
    private ArrayList<Vehicle> list;
    private VehicleAdapter adapter;
    private EditText SearchBar;
    private Button Search;
    private Boolean Filter;
    private Button filter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents(root);

        return root;
    }

    private void initComponents(View view) {
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Search = view.findViewById(R.id.search);
        SearchBar = view.findViewById(R.id.searchBar);
        filter = view.findViewById(R.id.filter);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Vehicles");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vehicle vehicle = snapshot.getValue(Vehicle.class);
                    if (vehicle != null) {
                        list.add(vehicle);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        SearchBar.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    resetList(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SearchBar.getText().length()!=0)
                {
                    resetList(false);
                    String str = SearchBar.getText().toString()+" ";
                    str = str.toLowerCase();
                    List<String> st = new ArrayList<>(Arrays.asList(str.split(" ")));
                    List<Vehicle> filteredList = new ArrayList<>();

                    for (Vehicle vehicle : list) {
                        List<String> x = vehicle.getSim();
                        if (new HashSet<>(x).containsAll(st)) {
                            filteredList.add(vehicle);
                        }
                    }
                    list.clear();
                    list.addAll(filteredList);
                    adapter.notifyDataSetChanged();
                }
                else{
                    resetList(true);
                }

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleComp c = new VehicleComp();
                if (Filter == null || !Filter){
                    Filter = true;
                    list.sort(c.reversed());
                }else if (Filter){
                    Filter = false;
                    list.sort(c);
                }
                adapter.notifyDataSetChanged();
            }
        });


        adapter = new VehicleAdapter(getContext(), list, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(int position) {
        Intent vehicleInfoPage = new Intent(getActivity(), VehicleInfoActivity.class);
        vehicleInfoPage.putExtra("VEHICLE",list.get(position));
        startActivity(vehicleInfoPage);
    }


    //DEBUGING
    private void toast(String txt){
        Toast toast = Toast.makeText(getContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void resetList(Boolean x) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Vehicles");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vehicle vehicle = snapshot.getValue(Vehicle.class);
                    if (vehicle != null) {
                        list.add(vehicle);
                    }
                }
                if (x){adapter.notifyDataSetChanged();}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to reset list.", error.toException());
            }
        });
    }

}