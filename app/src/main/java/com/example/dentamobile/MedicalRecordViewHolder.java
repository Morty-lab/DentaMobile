package com.example.dentamobile;

// MedicalRecordViewHolder.java
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MedicalRecordViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public MedicalRecordViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);

    }

    public void bind(MedicalRecord medicalRecord) {
        // Set the image resource for the ImageView
        Picasso.get().load(medicalRecord.getImageURL()).into(imageView);

    }
}

