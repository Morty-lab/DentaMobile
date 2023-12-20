package com.example.dentamobile;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentViewHolder extends RecyclerView.ViewHolder {
    public TextView timeTextView;
    public TextView dateTextView;
    public TextView selectedItemTextView;

    public AppointmentViewHolder(@NonNull View itemView) {
        super(itemView);

        timeTextView = itemView.findViewById(R.id.timeTextView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        selectedItemTextView = itemView.findViewById(R.id.selectedItemTextView);
    }
}
