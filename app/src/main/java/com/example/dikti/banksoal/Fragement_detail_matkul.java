package com.example.dikti.banksoal;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dikti.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class Fragement_detail_matkul extends Fragment {

    public Fragement_detail_matkul() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_matkul, container, false);

        RecyclerView daftarSoal = view.findViewById(R.id.daftar_soal);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        String semester = getArguments().getString("1");
        String namaMatkul = getArguments().getString("3");

        ImageView kembali = view.findViewById(R.id.kembali);


        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new Fragment_Home_Bank_Soal()).addToBackStack(null).commit();
            }
        });

        daftarSoal.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));

        FirestoreRecyclerOptions<VariabelBankSoal> options = new FirestoreRecyclerOptions.Builder<VariabelBankSoal>()
                .setQuery(firebaseFirestore.collection(semester).document(namaMatkul)
                        .collection(namaMatkul), VariabelBankSoal.class)
                .build();

        AdapterDaftarSoal adapterDaftarSoal = new AdapterDaftarSoal(options);
        adapterDaftarSoal.startListening();
        daftarSoal.setAdapter(adapterDaftarSoal);

        return view;
    }
}
