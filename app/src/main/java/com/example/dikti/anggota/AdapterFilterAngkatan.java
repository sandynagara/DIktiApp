package com.example.dikti.anggota;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterFilterAngkatan extends FirestoreRecyclerAdapter<VariabelFilterDepartemen,AdapterFilterAngkatan.ViewHolder> {

    final PopBottomDepartemen popBottomDepartemen;

    public AdapterFilterAngkatan(@NonNull FirestoreRecyclerOptions<VariabelFilterDepartemen> options, PopBottomDepartemen popBottomDepartemen) {
        super(options);
        this.popBottomDepartemen = popBottomDepartemen;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final VariabelFilterDepartemen variabelFilterDepartemen) {
        viewHolder.Angkatan.setText(variabelFilterDepartemen.getAngkatan());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Preference.clearAngkatan(view.getContext());
                Preference.setFilterAngkatan(view.getContext(),variabelFilterDepartemen.getAngkatan());
                popBottomDepartemen.dismiss();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.isi_pop_bottom_departemen,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView Angkatan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Angkatan = itemView.findViewById(R.id.nama_departemen);
        }
    }
}
