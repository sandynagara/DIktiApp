package com.example.dikti.anggota;

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
import com.example.dikti.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Fragment_Detail_Anggota extends Fragment {

    private TextView namaAnggota,namaDepartemen,namaAngkatan,email,nomorHandphone,idLine,alamat,aboutMe,nim;
    private ImageView fotoAnggota;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_anggota,container,false);

        namaAnggota = view.findViewById(R.id.nama_anggota);
        namaDepartemen = view.findViewById(R.id.nama_departemen);
        namaAngkatan = view.findViewById(R.id.angkatan);
        email = view.findViewById(R.id.email2);
        nomorHandphone = view.findViewById(R.id.no_telepon2);
        idLine = view.findViewById(R.id.id_line2);
        fotoAnggota = view.findViewById(R.id.foto_anggota);
        alamat = view.findViewById(R.id.alamat);
        aboutMe = view.findViewById(R.id.about_me);
        ImageView kembali = view.findViewById(R.id.kembali);
        nim = view.findViewById(R.id.nim);

        String anggotaDetail = getArguments().getString("1");
        final Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("FRAGMENT_ANGGOTA");

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(fragment).addToBackStack(null).commit();
            }
        });

        Task<DocumentSnapshot> databaseReference = FirebaseFirestore.getInstance().document("Anggota/" + anggotaDetail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String dataNama = documentSnapshot.getString("namaLengkap");
                String dataDepartemen = documentSnapshot.getString("departemen");
                String Foto = documentSnapshot.getString("foto");
                String angkatan = documentSnapshot.getString("angkatan");
                String isiidLine = documentSnapshot.getString("line");
                String noHandphone = documentSnapshot.getString("telepon");
                String isiEmail = documentSnapshot.getString("email");
                String isiAlamat = documentSnapshot.getString("alamat");
                String isiAboutMe = documentSnapshot.getString("aboutMe");
                String isiNim = documentSnapshot.getString("nim");

                namaAnggota.setText(dataNama);
                namaDepartemen.setText(dataDepartemen);
                namaAngkatan.setText(angkatan.toString());
                idLine.setText(isiidLine);
                nomorHandphone.setText(noHandphone);
                email.setText(isiEmail);
                alamat.setText(isiAlamat);
                aboutMe.setText(isiAboutMe);
                nim.setText(isiNim);

                Glide.with(getContext())
                        .load(Foto)
                        .placeholder(R.drawable.logo_dikti_format)
                        .into(fotoAnggota);
            }
        });

        return view;
    }
}
