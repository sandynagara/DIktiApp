package com.example.dikti.lombaBeasiswa.lomba;

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

public class AdapterFilterJenisLomba extends FirestoreRecyclerAdapter<VariabelFilterJenisLomba,AdapterFilterJenisLomba.ViewHolderFilter> {

    final PopBottomJenisLomba popBottomJenisLomba;

    public AdapterFilterJenisLomba(@NonNull FirestoreRecyclerOptions<VariabelFilterJenisLomba> options, PopBottomJenisLomba popBottomJenisLomba) {
        super(options);
        this.popBottomJenisLomba = popBottomJenisLomba;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolderFilter viewHolderFilter, int i, @NonNull final VariabelFilterJenisLomba variabelFilterJenisLomba) {
        viewHolderFilter.jenisLomba.setText(variabelFilterJenisLomba.getJenisLomba());
        viewHolderFilter.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Preference.clearJenisLomba(view.getContext());
                Preference.setDataJenisLomba(view.getContext(),variabelFilterJenisLomba.getJenisLomba());
                popBottomJenisLomba.dismiss();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolderFilter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.isi_pop_bottom_departemen,parent,false);
        return new AdapterFilterJenisLomba.ViewHolderFilter(view);
    }

    public static class ViewHolderFilter extends RecyclerView.ViewHolder{
        final TextView jenisLomba;

        public ViewHolderFilter(@NonNull View itemView) {
            super(itemView);
            jenisLomba = itemView.findViewById(R.id.nama_departemen);
        }
    }
}
