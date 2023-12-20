package com.example.dentamobile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView fullNameTextView, dobTextView, genderTextView, addressTextView;
    private FloatingActionButton logout;
    private ActivityResultLauncher<String> mGetContent;
    private RecyclerView record;
    private MedicalRecordAdapter medicalRecordAdapter;
    private DatabaseReference medreference ;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



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

        profileImageView = findViewById(R.id.profileImageView);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        dobTextView = findViewById(R.id.dobTextView);
        genderTextView = findViewById(R.id.genderTextView);
        addressTextView = findViewById(R.id.addressTextView);
        logout = findViewById(R.id.addAppBTN);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                // After signing out, you might want to navigate to the sign-in screen or another appropriate activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("userinfo").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String address = dataSnapshot.child("address").getValue(String.class);
                        String contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                        String dob = dataSnapshot.child("dob").getValue(String.class);
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String middleName = dataSnapshot.child("middleName").getValue(String.class);
                        String suffix = dataSnapshot.child("suffix").getValue(String.class);

                        String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                        // Now you can load the image from the URL
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            // Load the image from the URL
                            Picasso.get().load(profileImageUrl).into(profileImageView);
                        }

                        // Now you can use the retrieved data
                        // For example, you can set the text of the fullNameTextView
                        fullNameTextView.setText(firstName + " " + middleName + " " + lastName);
                        addressTextView.setText(address);
                        dobTextView.setText(dob);
                        genderTextView.setText(gender);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });





        }

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        profileImageView.setImageBitmap(bitmap);
                        uploadImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
        medreference = FirebaseDatabase.getInstance().getReference().child("medicalRecords");
        String userId = user.getUid();
        record = findViewById(R.id.records);
        record.setLayoutManager(new LinearLayoutManager(this));
        medreference.orderByChild("userid").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MedicalRecord> medicalRecords = new ArrayList<>();
                for (DataSnapshot recordSnapshot : snapshot.getChildren()) {
                    String imageURL = recordSnapshot.child("record").getValue(String.class);
                    // Other fields can be retrieved similarly

                    // Create a MedicalRecord object with the fetched data
                    MedicalRecord medicalRecord = new MedicalRecord(imageURL);
                    medicalRecords.add(medicalRecord);
                }
                int length = medicalRecords.size();
                Log.d("Dblist size", String.valueOf(length));
                medicalRecordAdapter = new MedicalRecordAdapter(medicalRecords, getApplicationContext());
                record.setAdapter(medicalRecordAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void uploadImage() {
        if (profileImageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("profileImages/" + userId);
                UploadTask uploadTask = storageRef.putBytes(data);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                // Now you can store the download URL in the Realtime Database
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("userinfo").child(userId);
                                userRef.child("profileImageUrl").setValue(downloadUrl);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


}