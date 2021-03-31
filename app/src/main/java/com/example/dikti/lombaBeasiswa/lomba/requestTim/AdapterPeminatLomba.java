package com.example.dikti.lombaBeasiswa.lomba.requestTim;

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
import com.example.dikti.anggota.Fragment_Detail_Anggota;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterPeminatLomba extends FirestoreRecyclerAdapter<VariabelRequestLomba, AdapterPeminatLomba.ViewHolder> {

    public AdapterPeminatLomba(@NonNull FirestoreRecyclerOptions<VariabelRequestLomba> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final VariabelRequestLomba variabelRequestLomba) {
        Glide.with(viewHolder.fotoAnggota.getContext())
                .load(variabelRequestLomba.getFoto())
                .into(viewHolder.fotoAnggota);
        viewHolder.angkatan.setText(variabelRequestLomba.getAngkatan());
        viewHolder.namaAnggota.setText(variabelRequestLomba.getNamaLengkap());
        if (variabelRequestLomba.getNamaLengkap().length()<24){
            viewHolder.point.setVisibility(View.GONE);
        }else {
            viewHolder.point.setText("...");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("1",variabelRequestLomba.getIdPeminat());
                SplashScreen activity = (SplashScreen) view.getContext();
                Fragment_Detail_Anggota fragment_detail_anggota = new Fragment_Detail_Anggota();
                fragment_detail_anggota.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contain_all,fragment_detail_anggota).addToBackStack(null).commit();

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menampilkan_anggota,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView fotoAnggota;
        private final TextView namaAnggota;
        private final TextView angkatan;
        private final TextView point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fotoAnggota = itemView.findViewById(R.id.foto_anggota);
            namaAnggota = itemView.findViewById(R.id.nama_anggota);
            angkatan = itemView.findViewById(R.id.nama_departemen);
            point = itemView.findViewById(R.id.point);
        }
    }
}
