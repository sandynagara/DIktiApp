package com.example.dikti.lombaBeasiswa.Beasiswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dikti.R;
import com.example.dikti.SplashScreen;
import com.example.dikti.lombaBeasiswa.Beasiswa.tambahBeasiswa.FragmentDetailBeasiswa;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterBeasiswa extends FirestoreRecyclerAdapter<VariabelBeasiswa, AdapterBeasiswa.ViewHolder> {


    public AdapterBeasiswa(@NonNull FirestoreRecyclerOptions<VariabelBeasiswa> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampilan_lomba,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final VariabelBeasiswa variabelBeasiswa) {
        Glide.with(viewHolder.gambarBeasiswa.getContext())
                .load(variabelBeasiswa.getFoto())
                .placeholder(R.drawable.logo_dikti)
                .into(viewHolder.gambarBeasiswa);
        viewHolder.rekomendasi.setVisibility(View.GONE);
        viewHolder.namaBeasiswa.setText(variabelBeasiswa.getNama());
        viewHolder.jenisBeasiswa.setVisibility(View.GONE);
        String deadlineTanggal = Long.toString(variabelBeasiswa.getDeadlineTanggal());
        String deadlineTahun = Long.toString(variabelBeasiswa.getDeadlineTahun());
        viewHolder.deadline.setText(deadlineTanggal +" "+ variabelBeasiswa.getDeadlineBulan() +" "+ deadlineTahun);
        final String key = getSnapshots().getSnapshot(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                variabelBeasiswa.setKey(key);
                Bundle bundle = new Bundle();
                bundle.putString("1",variabelBeasiswa.getKey());
                FragmentDetailBeasiswa fragmentDetailBeasiswa = new FragmentDetailBeasiswa();
                fragmentDetailBeasiswa.setArguments(bundle);
                SplashScreen activity = (SplashScreen) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().add(R.id.contain_all,fragmentDetailBeasiswa,"FRAGMENT_BEASISWA").commit();
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView gambarBeasiswa;
        final ImageView rekomendasi;
        final TextView namaBeasiswa;
        final TextView jenisBeasiswa;
        final TextView deadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gambarBeasiswa=itemView.findViewById(R.id.gambarlomba);
            namaBeasiswa=itemView.findViewById(R.id.nama_lomba);
            jenisBeasiswa=itemView.findViewById(R.id.jenis_lomba);
            deadline=itemView.findViewById(R.id.deadline);
            rekomendasi = itemView.findViewById(R.id.rekomendasi);
        }
}
}
