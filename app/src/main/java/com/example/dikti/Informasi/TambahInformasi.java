package com.example.dikti.Informasi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dikti.R;
import com.example.dikti.fragment_Home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TambahInformasi extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tambah_informasi,container,false);

        final EditText judulInformasi = view.findViewById(R.id.judul_informasi);
        final EditText isiInformasi = view.findViewById(R.id.isi_informasi);
        final EditText linkInformasi = view.findViewById(R.id.link_informasi);
        final TextView hapus = view.findViewById(R.id.hapusdata);
        hapus.setVisibility(View.GONE);
        ImageView kembali = view.findViewById(R.id.kembali);
        final TextView add = view.findViewById(R.id.addData);
        View tambahGambar = view.findViewById(R.id.tambah_foto);
        tambahGambar.setVisibility(View.GONE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        final String timestamp = simpleDateFormat.format(new Date());

        final String idInformasi = getArguments().getString("1");

        if (!idInformasi.equals("kosong")){
            Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document("Informasi/" + idInformasi).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(final DocumentSnapshot documentSnapshot) {
                    add.setText("Update");
                    judulInformasi.setText(documentSnapshot.getString("judul"));
                    isiInformasi.setText(documentSnapshot.getString("info"));
                    linkInformasi.setText(documentSnapshot.getString("link"));
                    hapus.setVisibility(View.VISIBLE);
                }
            });
        }

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).addToBackStack(null).commit();
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Informasi").document(idInformasi);
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).addToBackStack(null).commit();
                    }
                });
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference isiData;
                if (!idInformasi.equals("kosong")){
                    isiData= FirebaseFirestore.getInstance().collection("Informasi").document(idInformasi);
                }else {
                    isiData= FirebaseFirestore.getInstance().collection("Informasi").document(timestamp);
                }
                VariabelInformasi variabelInformasi = new VariabelInformasi();
                variabelInformasi.setJudul(judulInformasi.getText().toString());
                variabelInformasi.setInfo(isiInformasi.getText().toString());
                variabelInformasi.setTime(timestamp);
                if (linkInformasi.getText().toString().isEmpty()){
                    variabelInformasi.setLink("-");
                }else {
                    variabelInformasi.setLink(linkInformasi.getText().toString());
                }
                isiData.set(variabelInformasi).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Upload Success",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
