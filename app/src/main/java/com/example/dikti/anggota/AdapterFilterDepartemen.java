package com.example.dikti.anggota;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.R;
import com.example.dikti.Preference;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterFilterDepartemen extends FirestoreRecyclerAdapter<VariabelFilterDepartemen, AdapterFilterDepartemen.ViewHolderFilter> {

    final PopBottomDepartemen popBottomDepartemen;

    public AdapterFilterDepartemen(@NonNull FirestoreRecyclerOptions<VariabelFilterDepartemen> options, PopBottomDepartemen popBottomDepartemen) {
        super(options);
        this.popBottomDepartemen = popBottomDepartemen;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolderFilter viewHolderFilter, int i, @NonNull final VariabelFilterDepartemen variabelFilterDepartemen) {
        viewHolderFilter.namaDepartemen.setText(variabelFilterDepartemen.getDepartemen());
        viewHolderFilter.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Preference.clearDepartemen(view.getContext());
                Preference.setFilterDepartemen(view.getContext(),variabelFilterDepartemen.getDepartemen());
                popBottomDepartemen.dismiss();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolderFilter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.isi_pop_bottom_departemen,parent,false);
        return new ViewHolderFilter(view);
    }

    public static class ViewHolderFilter extends RecyclerView.ViewHolder{
        final TextView namaDepartemen;

        public ViewHolderFilter(@NonNull View itemView) {
            super(itemView);
            namaDepartemen = itemView.findViewById(R.id.nama_departemen);
        }
    }
}
