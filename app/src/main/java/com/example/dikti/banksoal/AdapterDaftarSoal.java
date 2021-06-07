package com.example.dikti.banksoal;

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

public class AdapterDaftarSoal extends FirestoreRecyclerAdapter<VariabelBankSoal, AdapterDaftarSoal.Viewholder> {

    public AdapterDaftarSoal(@NonNull FirestoreRecyclerOptions<VariabelBankSoal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder viewholder, int i, @NonNull final VariabelBankSoal variabelBankSoal) {
        Glide.with(viewholder.fotoSoal.getContext())
                .load(variabelBankSoal.getFoto())
                .placeholder(R.drawable.logo_dikti)
                .into(viewholder.fotoSoal);
        viewholder.dosen.setText(variabelBankSoal.getDosen());
        viewholder.tahun.setText(variabelBankSoal.getTahun().toString());
        final String key = getSnapshots().getSnapshot(i).getId();
        if (variabelBankSoal.getUtsUas() == null){
            viewholder.buang.setVisibility(View.GONE);
        }else {
            viewholder.buang.setText(variabelBankSoal.getUtsUas());
        }
        viewholder.rekomnedasi.setVisibility(View.GONE);
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("1",key);
                bundle.putString("2",variabelBankSoal.getMataKuliah());
                bundle.putString("3",variabelBankSoal.getSemester());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentDetailSoal fragmentDetailSoal = new FragmentDetailSoal();
                fragmentDetailSoal.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.contain_all,fragmentDetailSoal,"FRAGMENT_ZOOM").commit();
            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampilan_lomba,parent,false);
        return new Viewholder(view);
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        final ImageView fotoSoal;
        final ImageView rekomnedasi;
        final TextView dosen;
        final TextView tahun;
        final TextView buang;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            fotoSoal = itemView.findViewById(R.id.gambarlomba);
            dosen = itemView.findViewById(R.id.nama_lomba);
            tahun = itemView.findViewById(R.id.jenis_lomba);
            buang = itemView.findViewById(R.id.deadline);
            rekomnedasi = itemView.findViewById(R.id.rekomendasi);
        }
    }
}
