package com.example.dikti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dikti.lombaBeasiswa.lomba.FragmentDetailLomba;
import com.example.dikti.lombaBeasiswa.lomba.VariabelLomba;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterHome extends FirestoreRecyclerAdapter<VariabelLomba, AdapterHome.ViewHolder> {

    public AdapterHome(@NonNull FirestoreRecyclerOptions<VariabelLomba> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final VariabelLomba variabelLomba) {
        Glide.with(viewHolder.gambarLomba.getContext())
                .load(variabelLomba.getFoto())
                .placeholder(R.drawable.logo_dikti)
                .into(viewHolder.gambarLomba);
        viewHolder.namaLomba.setText(variabelLomba.getNama());
        viewHolder.jenisLomba.setText(variabelLomba.getJenis());
        String deadlineTanggal = Long.toString(variabelLomba.getDeadlineTanggal());
        String deadlineTahun = Long.toString(variabelLomba.getDeadlineTahun());
        viewHolder.deadline.setText(deadlineTanggal +" "+ variabelLomba.getDeadlineBulan() +" "+ deadlineTahun);
        final String key = getSnapshots().getSnapshot(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                variabelLomba.setKey(key);
                Bundle bundle = new Bundle();
                bundle.putString("1",variabelLomba.getKey());
                SplashScreen activity = (SplashScreen) view.getContext();
                FragmentDetailLomba fragmentDetailLomba = new FragmentDetailLomba();
                fragmentDetailLomba.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.contain_all,fragmentDetailLomba,"FRAGMENT_LOMBA").addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_lomba,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView gambarLomba;
        final TextView namaLomba;
        final TextView deadline;
        final TextView jenisLomba;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gambarLomba =itemView.findViewById(R.id.gambarlomba);
            namaLomba = itemView.findViewById(R.id.nama_lomba);
            deadline = itemView.findViewById(R.id.deadline);
            jenisLomba = itemView.findViewById(R.id.jenis_lomba);
        }
    }
}
