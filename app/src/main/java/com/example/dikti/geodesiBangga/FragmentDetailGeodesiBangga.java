package com.example.dikti.geodesiBangga;

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

public class FragmentDetailGeodesiBangga extends Fragment {

    private ImageView gambar;
    private TextView namaLomba,pemenang,deskripsi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_geodesi_bangga,container,false);

        ImageView kembali = view.findViewById(R.id.kembali);
        gambar = view.findViewById(R.id.gambar_geodesi_bangga);
        namaLomba = view.findViewById(R.id.nama_lomba);
        pemenang = view.findViewById(R.id.pemenang);
        deskripsi = view.findViewById(R.id.deskripsi_geodesi_bangga);

        final String idGeodesiBangga = getArguments().getString("1");

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new FragmentGeodesiBangga()).addToBackStack(null).commit();
            }
        });

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Geodesi Bangga/" + idGeodesiBangga).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(getContext())
                        .load(documentSnapshot.getString("foto"))
                        .placeholder(R.drawable.logo_dikti)
                        .into(gambar);

                namaLomba.setText(documentSnapshot.getString("judul"));
                pemenang.setText(documentSnapshot.getString("pemenang"));
                deskripsi.setText(documentSnapshot.getString("deskripsi"));
            }

        });

        return view;
    }
}
