package com.example.dikti.lombaBeasiswa.Beasiswa;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dikti.R;
import com.example.dikti.anggota.FragmentAnggota;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.fragment_Home;
import com.example.dikti.lombaBeasiswa.Beasiswa.tambahBeasiswa.FragmentTambahBeasiswa;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentBeasiswa extends Fragment implements View.OnClickListener {

    private RecyclerView beasiswa;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerOptions options;
    private AdapterBeasiswa adapterBeasiswa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_beasiswa,container,false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        beasiswa =view.findViewById(R.id.daftar_beasiswa);
        SearchView searchView = view.findViewById(R.id.search);
        TextView pindahLomba = view.findViewById(R.id.fragment_lomba);
        View toolbar = view.findViewById(R.id.toolbar);
        View anggota = toolbar.findViewById(R.id.anggota);
        View profile1 = toolbar.findViewById(R.id.bank_soal);
        ImageView tombolLomba = toolbar.findViewById(R.id.simbol_lomba);
        TextView textLomba = toolbar.findViewById(R.id.text_lomba);
        View lomba1 = toolbar.findViewById(R.id.lomba);
        View beasiswa1 = toolbar.findViewById(R.id.home1);
        View tambahBeasiswa = view.findViewById(R.id.tambah_beasiswa);

        tombolLomba.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_emoji_events_blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textLomba.setTextColor(getActivity().getColor(R.color.deepSkyBlue));
        }else {
            textLomba.setTextColor(ContextCompat.getColor(getContext(),R.color.deepSkyBlue));
        }
        textLomba.setText("Beasiswa");

        options = new FirestoreRecyclerOptions.Builder<VariabelBeasiswa>()
                .setQuery(firebaseFirestore.collection("Beasiswa").orderBy("deadline"),VariabelBeasiswa.class)
                .build();
        beasiswa.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapterBeasiswa = new AdapterBeasiswa(options);
        adapterBeasiswa.startListening();
        beasiswa.setAdapter(adapterBeasiswa);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                FirebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                FirebaseSearch(s);
                return false;
            }
        });

        anggota.setOnClickListener(this);
        profile1.setOnClickListener(this);
        lomba1.setOnClickListener(this);
        beasiswa1.setOnClickListener(this);
        pindahLomba.setOnClickListener(this);
        tambahBeasiswa.setOnClickListener(this);

        return view;
    }

    private void FirebaseSearch(String kataMasukan) {
        String lower = kataMasukan.toLowerCase();
        options = new FirestoreRecyclerOptions.Builder<VariabelBeasiswa>()
                .setQuery(firebaseFirestore.collection("Beasiswa").orderBy("queryNama").startAt(lower).endAt(lower+"\ufaff"),VariabelBeasiswa.class)
                .build();
        beasiswa.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapterBeasiswa = new AdapterBeasiswa(options);
        adapterBeasiswa.startListening();
        beasiswa.setAdapter(adapterBeasiswa);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).addToBackStack(null).commit();
        } else if (view.getId() == R.id.bank_soal) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentBeasiswa()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.home1) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.fragment_lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).addToBackStack(null).commit();
        }else if (view.getId()==R.id.tambah_beasiswa){
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentTambahBeasiswa()).addToBackStack(null).commit();
        }
}}
