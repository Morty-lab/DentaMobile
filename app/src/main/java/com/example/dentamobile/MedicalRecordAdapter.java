package com.example.dentamobile;

// MedicalRecordAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordViewHolder> {
    private List<MedicalRecord> medicalRecords;
    private Context context;

    public MedicalRecordAdapter(List<MedicalRecord> medicalRecords, Context context) {
        this.medicalRecords = medicalRecords;
        this.context = context;
    }

    @NonNull
    @Override
    public MedicalRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_record_item, parent, false);
        return new MedicalRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordViewHolder holder, int position) {
        MedicalRecord medicalRecord = medicalRecords.get(position);
        holder.bind(medicalRecord);
    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }
}
