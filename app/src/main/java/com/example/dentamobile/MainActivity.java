package com.example.dentamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FloatingActionButton btnAddApp;
    RecyclerView recyclerViewAppointments;
    AppointmentAdapter appointmentAdapter;
    DatabaseReference appointmentsRef;
    FirebaseAuth firebaseAuth;
    List<Appointment> appointments;
    String userId;
    FirebaseUser currentUser;

    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();

        btnAddApp = findViewById(R.id.addAppBTN);
        btnAddApp.setOnClickListener(this);

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");
        appointments = new ArrayList<>();
        // Create an instance of the AppointmentAdapter
        appointmentAdapter = new AppointmentAdapter(this, appointments);
        recyclerViewAppointments.setAdapter(appointmentAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Handle navigation to home
                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Handle navigation to profile
                    Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });

        appointmentsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();
                try{
                    if(snapshot.exists()){
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Appointment appointment=  dataSnapshot.getValue(Appointment.class);
                            appointments.add((appointment));
                        }
                        appointmentAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Log.d("Fucking error", "Failed to fetch data", e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,BookAppointment.class);
        String uid = currentUser.getUid();

//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put("userid",uid );
//        dataMap.put("record", "https://firebasestorage.googleapis.com/v0/b/dentamobile-e32b0.appspot.com/o/medicalRecords%2Fimg-sample-consolidated-medical-record-summ.jpg?alt=media&token=e16e9536-fbd5-411a-8ca2-1bbd8b06c1bd");
//        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("medicalRecords");
//        String recordKey = mdatabase.push().getKey();
//        mdatabase.child(recordKey).setValue(dataMap);

        startActivity(intent);
    }
}