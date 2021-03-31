package com.example.dikti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentDevelopers extends Fragment {

    ImageView fotoFrontEnd1,fotoBackEnd1;
    TextView departemenFrontEnd1, departemenBackEnd1;
    TextView angkatanFrontEnd1, angkatanBackEnd1;
    TextView namaFrontEnd1, namaBackEnd1;
    String nama,angkatan,foto,departemen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_developers,container,false);

        fotoBackEnd1 = view.findViewById(R.id.foto_backend1);
        fotoFrontEnd1 = view.findViewById(R.id.foto_frontend1);
        departemenBackEnd1 = view.findViewById(R.id.nama_departemen_backend1);
        departemenFrontEnd1 = view.findViewById(R.id.nama_departemen_frontend1);
        namaBackEnd1 = view.findViewById(R.id.nama_backend1);
        namaFrontEnd1 = view.findViewById(R.id.nama_frontend1);
        angkatanBackEnd1 = view.findViewById(R.id.angkatan_backend1);
        angkatanFrontEnd1 = view.findViewById(R.id.angkatan_frontend1);

        ImageView kembali = view.findViewById(R.id.kembali);
        TextView aplikasi = view.findViewById(R.id.fragment_apliksi);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).addToBackStack(null).commit();
            }
        });

        aplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAbout()).addToBackStack(null).commit();
            }
        });

        Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document("Anggota/2018_Sandy").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                foto = documentSnapshot.getString("foto");
                fotoDevelop(fotoBackEnd1,foto);
                fotoDevelop(fotoFrontEnd1,foto);
            }
        });

        nama = "Sandy Setyanagara";
        angkatan = "2018";
        departemen = "Pendidikan dan Penelitian";

        develop(namaBackEnd1,nama);
        develop(namaFrontEnd1,nama);
        develop(angkatanBackEnd1,angkatan);
        develop(angkatanFrontEnd1,angkatan);
        develop(departemenBackEnd1,departemen);
        develop(departemenFrontEnd1,departemen);
        return view;
    }
    private void develop (TextView textView ,String string){
        textView.setText(string);
    }
    private void fotoDevelop (ImageView imageView ,String string){
        Glide.with(getContext())
                .load(string)
                .into(imageView);
    }
}
