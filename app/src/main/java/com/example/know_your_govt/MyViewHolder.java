package com.example.know_your_govt;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView row_name;
    public TextView row_office;

    public MyViewHolder(View itemView) {
        super(itemView);
        row_name = itemView.findViewById(R.id.row_nameText);
        row_office = itemView.findViewById(R.id.row_officeText);
    }
}
