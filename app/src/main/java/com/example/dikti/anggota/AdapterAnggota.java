package com.example.dikti.anggota;

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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;


public class AdapterAnggota extends FirestoreRecyclerAdapter<VariabelAnggota,AdapterAnggota.ViewHolder> {

    private DatabaseReference databaseReference;

    public AdapterAnggota(@NonNull FirestoreRecyclerOptions<VariabelAnggota> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menampilkan_anggota,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final VariabelAnggota variabelAnggota) {
        Glide.with(holder.fotoAnggota.getContext())
                .load(variabelAnggota.getFoto())
                .placeholder(R.drawable.logo_dikti)
                .into(holder.fotoAnggota);
        holder.namaAnggota.setText(variabelAnggota.getNamaLengkap());
        holder.namaDepartemen.setText(variabelAnggota.getDepartemen());
        holder.angkatan.setText(variabelAnggota.getAngkatan());
        if (variabelAnggota.getNamaLengkap().length()<22){
            holder.point.setText("");
        }else {
            holder.point.setText("...");
        }
        final String key = getSnapshots().getSnapshot(position).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                variabelAnggota.setKey(key);
                Bundle bundle = new Bundle();
                bundle.putString("1",variabelAnggota.getKey());
                SplashScreen activity = (SplashScreen) view.getContext();
                Fragment_Detail_Anggota fragment_detail_anggota = new Fragment_Detail_Anggota();
                fragment_detail_anggota.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contain_all,fragment_detail_anggota).commit();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView fotoAnggota;
        final TextView namaAnggota;
        final TextView namaDepartemen;
        final TextView angkatan;
        final TextView point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoAnggota = itemView.findViewById(R.id.foto_anggota);
            namaAnggota = itemView.findViewById(R.id.nama_anggota);
            namaDepartemen = itemView.findViewById(R.id.nama_departemen);
            angkatan = itemView.findViewById(R.id.angkatan);
            point = itemView.findViewById(R.id.point);
        }
    }
}
