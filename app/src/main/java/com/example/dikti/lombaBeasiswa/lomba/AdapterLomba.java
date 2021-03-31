package com.example.dikti.lombaBeasiswa.lomba;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.SplashScreen;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdapterLomba extends FirestoreRecyclerAdapter<VariabelLomba, AdapterLomba.ViewHolder> {

    DocumentReference documentReference;
    Boolean favorit1;


    public AdapterLomba(@NonNull FirestoreRecyclerOptions<VariabelLomba> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull final VariabelLomba variabelLomba) {
        Glide.with(viewHolder.gambarLomba.getContext())
                .load(variabelLomba.getFoto())
                .placeholder(R.drawable.logo_dikti)
                .into(viewHolder.gambarLomba);
        viewHolder.namaLomba.setText(variabelLomba.getNama());
        viewHolder.jenisLomba.setText(variabelLomba.getJenis());
        String deadlineTanggal = Long.toString(variabelLomba.getDeadlineTanggal());
        String deadlineTahun = Long.toString(variabelLomba.getDeadlineTahun());
        favorit1 = variabelLomba.getFavorit();
        viewHolder.deadline.setText(deadlineTanggal +" "+ variabelLomba.getDeadlineBulan() +" "+ deadlineTahun);
        final String key = getSnapshots().getSnapshot(i).getId();

        if (!Preference.getDataAs(viewHolder.itemView.getContext()).equals("Admin")){
            viewHolder.favorit.setVisibility(View.GONE);
        }

        if (favorit1){
            viewHolder.favorit.setImageResource(R.drawable.ic_baseline_favorite_red);
        }else {
            viewHolder.favorit.setImageResource(R.drawable.ic_baseline_favorite_gray);
        }

        viewHolder.favorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Map<String,Object> map = new HashMap<>();
                if (favorit1){
                    map.put("favorit",false);
                    favorit1 = variabelLomba.getFavorit();
                    viewHolder.favorit.setImageResource(R.drawable.ic_baseline_favorite_gray);
                }else {
                    map.put("favorit",true);
                    favorit1 = variabelLomba.getFavorit();
                    viewHolder.favorit.setImageResource(R.drawable.ic_baseline_favorite_red);
                }
                Task documentReference = FirebaseFirestore.getInstance().collection("Lomba").document(key)
                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(),"Update Berhasil "+key,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Bundle bundle = new Bundle();
                bundle.putString("1",key);
                SplashScreen activity = (SplashScreen) view.getContext();
                FragmentDetailLomba fragmentDetailLomba = new FragmentDetailLomba();
                fragmentDetailLomba.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contain_all,fragmentDetailLomba).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampilan_lomba,parent,false);
        return new ViewHolder(view);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView gambarLomba;
        final ImageView favorit;
        final TextView namaLomba;
        final TextView jenisLomba;
        final TextView deadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gambarLomba=itemView.findViewById(R.id.gambarlomba);
            favorit=itemView.findViewById(R.id.rekomendasi);
            namaLomba=itemView.findViewById(R.id.nama_lomba);
            jenisLomba=itemView.findViewById(R.id.jenis_lomba);
            deadline=itemView.findViewById(R.id.deadline);
        }
    }
}
