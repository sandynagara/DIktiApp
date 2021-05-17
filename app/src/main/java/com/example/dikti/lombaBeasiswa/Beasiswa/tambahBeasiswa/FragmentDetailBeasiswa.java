package com.example.dikti.lombaBeasiswa.Beasiswa.tambahBeasiswa;

import android.content.Intent;
import android.net.Uri;
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
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.banksoal.FragmentDetailSoal;
import com.example.dikti.lombaBeasiswa.Beasiswa.FragmentBeasiswa;
import com.example.dikti.lombaBeasiswa.Beasiswa.FragmentEditBeasiswa;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentDetailBeasiswa extends Fragment {

    private ImageView gambarBeasiswa;
    private TextView namaBeasiswa,deadline,deskripsi;
    private String isiLink,username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_beasiswa,container,false);

        gambarBeasiswa = view.findViewById(R.id.gambar_beasiswa);
        final ImageView edit = view.findViewById(R.id.edit);
        edit.setVisibility(View.INVISIBLE);
        ImageView kembali = view.findViewById(R.id.kembali);
        final TextView link = view.findViewById(R.id.link_browser);
        namaBeasiswa = view.findViewById(R.id.nama_beasiswa);
        deadline = view.findViewById(R.id.deadline);
        deskripsi = view.findViewById(R.id.deskripsi_beasiswa);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new FragmentBeasiswa())
                        .commit();
            }
        });

        final String idBeasiswa = getArguments().getString("1");

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Beasiswa/" + idBeasiswa).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String isiNamaLomba = documentSnapshot.getString("nama");
                Long deadlineTanggal = documentSnapshot.getLong("deadlineTanggal");
                String deadlineBulan = documentSnapshot.getString("deadlineBulan");
                Long deadlineTahun = documentSnapshot.getLong("deadlineTahun");
                String isiFoto = documentSnapshot.getString("foto");
                isiLink = documentSnapshot.getString("link");
                String isiDeskripsi = documentSnapshot.getString("deskripsi");
                username = documentSnapshot.getString("username");
                String pengirim = documentSnapshot.getString("pengirim");

                namaBeasiswa.setText(isiNamaLomba);
                deadline.setText(deadlineTanggal.toString() + " " + deadlineBulan + " " + deadlineTahun.toString());
                deskripsi.setText(isiDeskripsi);
                link.setText(isiLink);

                if (Preference.getDataAs(getContext()).equals("Admin") || Preference.getDataUsername(getContext()).equals(pengirim)){
                    edit.setVisibility(View.VISIBLE);
                }

                Glide.with(getContext())
                        .load(isiFoto)
                        .placeholder(R.drawable.logo_dikti)
                        .into(gambarBeasiswa);

                gambarBeasiswa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("1",idBeasiswa);
                        bundle.putString("2","Beasiswa");
                        FragmentDetailSoal fragmentDetailSoal = new FragmentDetailSoal();
                        fragmentDetailSoal.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentDetailSoal).commit();
                    }
                });
            }

        });



        if (Preference.getDataUsername(getContext()).equals(username)){
            edit.setVisibility(View.VISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("1",idBeasiswa);
                FragmentEditBeasiswa fragmentEditBeasiswa = new FragmentEditBeasiswa();
                fragmentEditBeasiswa.setArguments(bundle);
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentEditBeasiswa).commit();
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(isiLink));
                startActivity(intent);
            }
        });

        return view;
    }
}
