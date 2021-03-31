package com.example.dikti.geodesiBangga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dikti.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterGeodesiBangga extends FirestoreRecyclerAdapter<VariabelGeodesiBangga, AdapterGeodesiBangga.ViewHolder> {

    final String tipe;

    public AdapterGeodesiBangga(@NonNull FirestoreRecyclerOptions<VariabelGeodesiBangga> options ,String tipe) {
        super(options);
        this.tipe = tipe;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i, @NonNull VariabelGeodesiBangga variabelGeodesiBangga) {
        if (!tipe.equals("1")){
            viewHolder.love.setVisibility(View.GONE);
        }
        viewHolder.deadline.setVisibility(View.GONE);
        viewHolder.jenis.setVisibility(View.GONE);
        Glide.with(viewHolder.foto.getContext())
                .load(variabelGeodesiBangga.foto)
                .placeholder(R.drawable.logo_dikti)
                .into(viewHolder.foto);
        viewHolder.namaLomba.setText(variabelGeodesiBangga.getJudul());
        viewHolder.namaLomba.setMaxLines(5);
        final String key = getSnapshots().getSnapshot(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle kirim = new Bundle();
                kirim.putString("1",key);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentDetailGeodesiBangga fragmentDetailGeodesiBangga = new FragmentDetailGeodesiBangga();
                fragmentDetailGeodesiBangga.setArguments(kirim);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contain_all,fragmentDetailGeodesiBangga).commit();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (tipe.equals("1")){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_lomba,parent,false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampilan_lomba,parent,false);
        }
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView foto;
        ImageView love;
        final TextView namaLomba;
        final TextView deadline;
        final TextView jenis;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (!tipe.equals("1")){
                love = itemView.findViewById(R.id.rekomendasi);
            }
            foto = itemView.findViewById(R.id.gambarlomba);
            namaLomba =itemView.findViewById(R.id.nama_lomba);
            deadline = itemView.findViewById(R.id.deadline);
            jenis = itemView.findViewById(R.id.jenis_lomba);
        }
    }
}
