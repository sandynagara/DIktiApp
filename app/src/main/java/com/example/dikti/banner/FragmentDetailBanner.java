package com.example.dikti.banner;

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
import com.example.dikti.fragment_Home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentDetailBanner extends Fragment {

    ImageView banner,kembali;
    TextView judul,isi,link;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_banner,container,false);

        banner = view.findViewById(R.id.gambarbanner);
        kembali = view.findViewById(R.id.kembali);
        judul = view.findViewById(R.id.judul);
        isi = view.findViewById(R.id.isi_banner);
        link = view.findViewById(R.id.link);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).commit();
            }
        });

        final String idBanner = getArguments().getString("1");

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Banner/" + idBanner).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                judul.setText(documentSnapshot.getString("judul"));
                isi.setText(documentSnapshot.getString("isi"));
                link.setText(documentSnapshot.getString("link"));

                Glide.with(getContext())
                        .load(documentSnapshot.getString("fotoBanner"))
                        .placeholder(R.drawable.logo_dikti)
                        .into(banner);
            }

        });

        return view;
    }
}
