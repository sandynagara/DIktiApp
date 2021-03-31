package com.example.dikti.Informasi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.R;
import com.example.dikti.fragment_Home;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentInformasi extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_informasi,container,false);

        RecyclerView daftarInformasi = view.findViewById(R.id.daftar_informasi);
        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).addToBackStack(null).commit();
            }
        });

        FirestoreRecyclerOptions<VariabelInformasi> optionInformasi =new FirestoreRecyclerOptions.Builder<VariabelInformasi>()
                .setQuery(FirebaseFirestore.getInstance().collection("Informasi").orderBy("time", Query.Direction.DESCENDING), VariabelInformasi.class)
                .build();
        daftarInformasi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        AdapterInformasi adapterInformasi = new AdapterInformasi(optionInformasi,getContext());
        adapterInformasi.startListening();
        daftarInformasi.setAdapter(adapterInformasi);

        return view;
    }
}
