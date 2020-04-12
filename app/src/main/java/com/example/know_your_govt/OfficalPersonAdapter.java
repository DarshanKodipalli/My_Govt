package com.example.know_your_govt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficalPersonAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<OfficialPersonDetails> officialPersonsList;
    private MainActivity mainActivity;

    public OfficalPersonAdapter(List<OfficialPersonDetails> officialList, MainActivity mainAct) {
        this.officialPersonsList= officialList;
        this.mainActivity= mainAct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent, false);
        view.setOnClickListener(mainActivity);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        OfficialPersonDetails official = officialPersonsList.get(position);
        if (official.getPartyName() == null) {
            holder.row_name.setText(official.getPersonName());
        }
        else{
            holder.row_name.setText(official.getPersonName()+'('+official.getPartyName()+')');
        }
        holder.row_office.setText(official.getOfficeName());
    }

    @Override
    public int getItemCount() {
        return officialPersonsList.size();
    }
}