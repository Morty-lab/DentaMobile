package com.example.dentamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterInformation extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText firstNameEditText, middleNameEditText, lastNameEditText, suffixEditText, dobEditText, contactNumberEditText, addressEditText;
    Spinner genderSpinner;
    Button registerButton;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_information);




        uid = getIntent().getStringExtra("UID");

        firstNameEditText = findViewById(R.id.firstNameEditText);
        middleNameEditText = findViewById(R.id.middleNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        suffixEditText = findViewById(R.id.suffixEditText);
        dobEditText = findViewById(R.id.editTextDate);
        genderSpinner = findViewById(R.id.genderSpinner);
        contactNumberEditText = findViewById(R.id.contactNumberEditText);
        addressEditText = findViewById(R.id.addressEditText);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Male");
        adapter.add("Female");

        genderSpinner.setAdapter(adapter);
    }
    public void showDatePicker(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
        // Set the selected date in the corresponding EditText or handle it as needed
        ((EditText) findViewById(R.id.editTextDate)).setText(date);
    }

    private boolean validateInputs() {
        // Reset errors
        firstNameEditText.setError(null);
        middleNameEditText.setError(null);
        lastNameEditText.setError(null);
        suffixEditText.setError(null);
        dobEditText.setError(null);
        contactNumberEditText.setError(null);
        addressEditText.setError(null);

        // Get values from EditText fields
        String firstName = firstNameEditText.getText().toString().trim();
        String middleName = middleNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String suffix = suffixEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String contactNumber = contactNumberEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        // Perform validation
        if (firstName.isEmpty()) {
            firstNameEditText.setError("First name is required");
            return false;
        }

        if (middleName.isEmpty()) {
            middleNameEditText.setError("Middle name is required");
            return false;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Last name is required");
            return false;
        }

        if (suffix.isEmpty()) {
            suffixEditText.setError("Suffix is required");
            return false;
        }

        if (dob.isEmpty()) {
            dobEditText.setError("Date of birth is required");
            return false;
        }

        if (contactNumber.isEmpty()) {
            contactNumberEditText.setError("Contact number is required");
            return false;
        }

        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            return false;
        }

        // If all validations pass, return true
        return true;
    }



    @Override
    public void onClick(View v) {
        if (validateInputs()== true){
            String firstName = firstNameEditText.getText().toString().trim();
            String middleName = middleNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String suffix = suffixEditText.getText().toString().trim();
            String dob = dobEditText.getText().toString().trim();
            String gender = genderSpinner.getSelectedItem().toString();
            String contactNumber = contactNumberEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("firstName", firstName);
            userInfo.put("middleName", middleName);
            userInfo.put("lastName", lastName);
            userInfo.put("suffix", suffix);
            userInfo.put("dob", dob);
            userInfo.put("gender", gender);
            userInfo.put("contactNumber", contactNumber);
            userInfo.put("address", address);

            mDatabase.child("userinfo").child(uid).setValue(userInfo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterInformation.this, "User information saved successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterInformation.this, "Failed to save user information", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }




    }

}