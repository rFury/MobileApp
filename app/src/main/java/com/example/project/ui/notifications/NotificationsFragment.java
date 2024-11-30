package com.example.project.ui.notifications;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.LoginActivity;
import com.example.project.Model.User_Details;
import com.example.project.Model.Vehicle;
import com.example.project.R;
import com.example.project.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private TextView fullNameTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private TextView driversLicenseTextView;

    private Button logOut;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents(root);

        return root;
    }

    public void initComponents(View view) {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser x = Auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("User_Details").child(x.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_Details user = dataSnapshot.getValue(User_Details.class);
                fullNameTextView.setText(user.getF_name()+" "+user.getL_name());
                phoneNumberTextView.setText("+216:"+String.valueOf(user.getT_number()));
                driversLicenseTextView.setText(String.valueOf(user.getD_license()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        fullNameTextView = view.findViewById(R.id.view_full_name);
        emailTextView = view.findViewById(R.id.view_email);
        phoneNumberTextView = view.findViewById(R.id.view_phone_number);
        driversLicenseTextView = view.findViewById(R.id.view_drivers_license);
        logOut = view.findViewById(R.id.btn_logout);
        emailTextView.setText(x.getEmail());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
