package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Loader extends AppCompatActivity {

    private FirebaseAuth Auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loader);

        Auth = FirebaseAuth.getInstance();
        Auth.addAuthStateListener(firebaseAuth -> {
            if (Auth.getCurrentUser()!=null){
                Intent i = new Intent(Loader.this,MainActivity.class);
                startActivity(i);
                finish();

            }else{
                Intent i = new Intent(Loader.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}