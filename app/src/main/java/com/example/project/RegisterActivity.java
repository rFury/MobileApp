package com.example.project;

import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.Model.User_Details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth Auth;

    // Declare all the variables
    private EditText firstName, lastName, email, license, phoneNumber, password, confirmPassword;
    private Button register;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        Auth = FirebaseAuth.getInstance();

        // Initialize EditText fields
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        license = findViewById(R.id.license);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        // Initialize Button
        register = findViewById(R.id.register);

        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();

                    Auth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(RegisterActivity.this, "Authentication success.",Toast.LENGTH_SHORT).show();
                                FirebaseUser user = Auth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileproject-53e34-default-rtdb.europe-west1.firebasedatabase.app");
                                DatabaseReference myRef = database.getReference("User_Details");
                                Integer phoneText = Integer.parseInt(phoneNumber.getText().toString());
                                String lastname = lastName.getText().toString();
                                String firstname = firstName.getText().toString();
                                Integer d_license = Integer.parseInt(license.getText().toString());
                                String ID = user.getUid();
                                User_Details x = new User_Details(ID,firstname,lastname,phoneText,d_license);

                                myRef.child(ID).setValue(x).addOnCompleteListener(scndtask -> {
                                    if (scndtask.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User details saved successfully!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RegisterActivity.this,Loader.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Log.e(TAG, "Failed to save user details: ", scndtask.getException());
                                    }
                                });



                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateFields() {
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("First Name is required");
            return false;
        }

        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Last Name is required");
            return false;
        }

        String emailText = email.getText().toString();
        if (emailText.isEmpty()) {
            email.setError("Email is required");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Enter a valid email address");
            return false;
        }

        if (license.getText().toString().isEmpty()) {
            license.setError("License number is required");
            return false;
        }

        String phoneText = phoneNumber.getText().toString();
        if (phoneText.isEmpty()) {
            phoneNumber.setError("Phone Number is required");
            return false;
        } else if (!phoneText.matches("\\d{10}")) {
            phoneNumber.setError("Enter a valid 10-digit phone number");
            return false;
        }

        String passwordText = password.getText().toString();
        if (passwordText.isEmpty()) {
            password.setError("Password is required");
            return false;
        } else if (passwordText.length() < 6) {
            password.setError("Password should be at least 6 characters long");
            return false;
        }

        String confirmPasswordText = confirmPassword.getText().toString();
        if (confirmPasswordText.isEmpty()) {
            confirmPassword.setError("Confirm Password is required");
            return false;
        } else if (!confirmPasswordText.equals(passwordText)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }

        return true; // All validations passed
    }
}
