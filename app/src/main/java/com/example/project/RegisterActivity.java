package com.example.project;

import static android.content.ContentValues.TAG;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth Auth;

    // Declare all the variables
    private EditText firstName, lastName, email, license, phoneNumber, password, confirmPassword;
    private Button register;
    private TextView aMember, registration, rentACar, login;

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

        // Initialize TextView fields
        aMember = findViewById(R.id.a_member);
        registration = findViewById(R.id.registration);
        rentACar = findViewById(R.id.rent_A_Car);
        login = findViewById(R.id.login);

        // Set OnClickListener for the register button
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
                                FirebaseUser user = Auth.getCurrentUser();
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to validate all fields
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
