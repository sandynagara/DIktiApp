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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dikti.R;
import com.example.dikti.fragment_Home;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentGeodesiBangga extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_matkul,container,false);

        TextView judul = view.findViewById(R.id.soal);
        ImageView kembali = view.findViewById(R.id.kembali);
        RecyclerView daftar = view.findViewById(R.id.daftar_soal);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).addToBackStack(null).commit();
            }
        });

        judul.setText("Geodesi Bangga");

        FirestoreRecyclerOptions optionsGeodesiBangga = new FirestoreRecyclerOptions.Builder<VariabelGeodesiBangga>()
                .setQuery(FirebaseFirestore.getInstance().collection("Geodesi Bangga"),VariabelGeodesiBangga.class)
                .build();

        daftar.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        final AdapterGeodesiBangga adapterGeodesiBangga = new AdapterGeodesiBangga(optionsGeodesiBangga,"2");
        adapterGeodesiBangga.startListening();
        daftar.setAdapter(adapterGeodesiBangga);

        return view;
    }
}
