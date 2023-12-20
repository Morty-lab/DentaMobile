package com.example.dentamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText email,password;
    Button registerButton;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
            email = findViewById(R.id.emailEditText);
            password = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

            // Perform the registration with the entered username, email, and password


    }
    private boolean validateEmailAndPassword() {
        boolean isValid = true;

        String emailValue = email.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();

        // Reset errors
        email.setError(null);
        password.setError(null);

        // Validation for Email
        if (emailValue.isEmpty()) {
            email.setError("Please enter your email");
            isValid = false;
        } else if (!isValidEmail(emailValue)) {
            email.setError("Invalid email address");
            isValid = false;
        }

        // Validation for Password
        if (passwordValue.isEmpty()) {
            password.setError("Please enter your password");
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidEmail(String email) {
        // You can use a more sophisticated email validation pattern if needed
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        if (validateEmailAndPassword()) {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();

            Toast.makeText(this, emailValue + "\n" + passwordValue, Toast.LENGTH_SHORT).show();

            mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success
                                String userId = mAuth.getCurrentUser().getUid();
                                Intent intent = new Intent(getApplicationContext(),RegisterInformation.class);
                                intent.putExtra("UID", userId);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }
}