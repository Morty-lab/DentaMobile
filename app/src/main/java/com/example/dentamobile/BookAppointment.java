package com.example.dentamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BookAppointment extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener , View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
    TextView patient_name,key;
    EditText date,time;
    Button addApp;

    Spinner spinner;

    String selectedItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Teeth Cleaning");
        adapter.add("Dental Checkup");
        adapter.add("Tooth Extraction");
        adapter.add("Root Canal Treatment");
        adapter.add("Dental Fillings");
        adapter.add("Braces Installation");
        adapter.add("Teeth Whitening");
        adapter.add("Gum Disease Treatment");
        adapter.add("Dental Implants");
        adapter.add("Oral Surgery");
        adapter.add("Orthodontic Consultation");
// Add more items as needed
        spinner = findViewById(R.id.spinnerServices);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item text
                selectedItemText = parentView.getItemAtPosition(position).toString();

                // Now you can use the selectedItemText as needed
                // For example, you can display it or perform some action
                Toast.makeText(getApplicationContext(), "Selected: " + selectedItemText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected
            }
        });



        date = findViewById(R.id.editTextDate);
        time = findViewById(R.id.editTextTime);
        addApp = findViewById(R.id.addAppointment);
        addApp.setOnClickListener(this);





    }

    // Called when the user clicks the Date EditText
    public void showDatePicker(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Called when the user clicks the Time EditText
    public void showTimePicker(View view) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "time  Picker");
    }

    // Callback for when the date is set in the DatePickerDialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
        // Set the selected date in the corresponding EditText or handle it as needed
        ((EditText) findViewById(R.id.editTextDate)).setText(date);
    }

    // Callback for when the time is set in the TimePickerDialog
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = String.format("%02d:%02d", hourOfDay, minute);
        // Set the selected time in the corresponding EditText or handle it as needed
        ((EditText) findViewById(R.id.editTextTime)).setText(time);
    }

    @Override
    public void onClick(View v) {
//        String message = time.getText().toString() +" \n" + date.getText().toString() + " \n" + patient_name.getText().toString()+ " \n" + key.getText().toString() + " \n" + selectedItemText;
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        String timeValue = time.getText().toString();
        String dateValue = date.getText().toString();
        String userId = user.getUid();

        String selectedItemTextValue = selectedItemText;

        HashMap<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("time", timeValue);
        appointmentData.put("date", dateValue);
        appointmentData.put("userId", userId);
        appointmentData.put("service", selectedItemTextValue);

        String appointmentKey = appointmentsRef.push().getKey();

// Add the data to the database under the generated key
        appointmentsRef.child(appointmentKey).setValue(appointmentData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
