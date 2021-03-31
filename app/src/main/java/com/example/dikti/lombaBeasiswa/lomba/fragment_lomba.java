package com.example.dikti.lombaBeasiswa.lomba;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.anggota.FragmentAnggota;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.fragment_Home;
import com.example.dikti.login.FragmentLogin;
import com.example.dikti.lombaBeasiswa.Beasiswa.FragmentBeasiswa;
import com.example.dikti.lombaBeasiswa.lomba.requestTim.FragmentRequesLomba;
import com.example.dikti.lombaBeasiswa.lomba.tambahLomba.FragmentTambahLomba;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class fragment_lomba extends Fragment implements View.OnClickListener {

    private RecyclerView lomba;
    private AdapterLomba adapterLomba;
    private FirestoreRecyclerOptions<VariabelLomba> options;
    private FirebaseFirestore firebaseFirestore;
    private TextView namaJenisLomba;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_lomba,container,false);

        lomba = view.findViewById(R.id.daftarLomba);
        SearchView searchView = view.findViewById(R.id.search);
        searchView.onActionViewExpanded();
        View filterJenisLomba = view.findViewById(R.id.filter_jenis_lomba);
        namaJenisLomba = view.findViewById(R.id.jenis_lomba);
        View tambahLomba = view.findViewById(R.id.tambah_lomba);
        ImageView daftarRequestLomba = view.findViewById(R.id.daftar_request_lomba);
        View toolbar = view.findViewById(R.id.toolbar);
        View anggota = toolbar.findViewById(R.id.anggota);
        View profile1 = toolbar.findViewById(R.id.bank_soal);
        ImageView tombolLomba = toolbar.findViewById(R.id.simbol_lomba);
        TextView textLomba = toolbar.findViewById(R.id.text_lomba);
        View lomba1 = toolbar.findViewById(R.id.lomba);
        View beasiswa = toolbar.findViewById(R.id.home1);
        TextView pindahBeasiswa = view.findViewById(R.id.fragment_beasiswa);

        tombolLomba.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_emoji_events_blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textLomba.setTextColor(getActivity().getColor(R.color.deepSkyBlue));
        }else {
            textLomba.setTextColor(ContextCompat.getColor(getContext(),R.color.deepSkyBlue));
        }

        lomba.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        firebaseFirestore = FirebaseFirestore.getInstance();
        filterJenisLomba.setOnClickListener(this);
        tambahLomba.setOnClickListener(this);

        if (Preference.getDataJenisLomba(getContext()).equals("")) {
            options = new FirestoreRecyclerOptions.Builder<VariabelLomba>()
                    .setQuery(firebaseFirestore.collection("Lomba"), VariabelLomba.class)
                    .build();
            adapterLomba = new AdapterLomba(options);
            adapterLomba.startListening();
            lomba.setAdapter(adapterLomba);
        }
        else if (Preference.getDataJenisLomba(getContext()).equals("All")){
            GantiFilter(view);

            options = new FirestoreRecyclerOptions.Builder<VariabelLomba>()
                    .setQuery(firebaseFirestore.collection("Lomba"), VariabelLomba.class)
                    .build();
            adapterLomba = new AdapterLomba(options);
            adapterLomba.startListening();
            lomba.setAdapter(adapterLomba);
        }
        else {
            namaJenisLomba.setText(Preference.getDataJenisLomba(getContext()));
            namaJenisLomba.setTextColor(Color.WHITE);
            RelativeLayout relativeLayout = view.findViewById(R.id.filter_jenis_lomba);
            Drawable drawable = getResources().getDrawable(R.drawable.border_biru);
            relativeLayout.setBackground(drawable);
            int padding_in_dp = 7;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            relativeLayout.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            options = new FirestoreRecyclerOptions.Builder<VariabelLomba>()
                    .setQuery(firebaseFirestore.collection("Lomba").orderBy("jenis").startAt(Preference.getDataJenisLomba(getContext())).endAt(Preference.getDataJenisLomba(getContext())+"\ufaff"), VariabelLomba.class)
                    .build();

            adapterLomba = new AdapterLomba(options);
            adapterLomba.startListening();
            lomba.setAdapter(adapterLomba);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(Preference.getDataJenisLomba(getContext()).isEmpty() || Preference.getDataJenisLomba(getContext()).equals("All")){
                    FirebaseSearch(s,view);
                }else {
                    FirebaseSearchLomba(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(Preference.getDataJenisLomba(getContext()).isEmpty() || Preference.getDataJenisLomba(getContext()).equals("All")){
                    FirebaseSearch(s,view);
                }else {
                    FirebaseSearchLomba(s);
                }
                return false;
            }
        });

        anggota.setOnClickListener(this);
        profile1.setOnClickListener(this);
        lomba1.setOnClickListener(this);
        beasiswa.setOnClickListener(this);
        pindahBeasiswa.setOnClickListener(this);
        daftarRequestLomba.setOnClickListener(this);

        return view;
    }

    private void GantiFilter(View view){
        namaJenisLomba.setText("Jenis Lomba : All");
        namaJenisLomba.setTextColor(Color.GRAY);
        RelativeLayout relativeLayout = view.findViewById(R.id.filter_jenis_lomba);
        Drawable drawable = getResources().getDrawable(R.drawable.border);
        relativeLayout.setBackground(drawable);
        int padding_in_dp = 7;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        relativeLayout.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
    }

    private void FirebaseSearch(String kataMasukan,View view) {
        GantiFilter(view);

        String lower = kataMasukan.toLowerCase();
        options = new FirestoreRecyclerOptions.Builder<VariabelLomba>()
                .setQuery(firebaseFirestore.collection("Lomba").orderBy("queryNama").startAt(lower).endAt(lower+"\ufaff"), VariabelLomba.class)
                .build();

        adapterLomba = new AdapterLomba(options);
        adapterLomba.startListening();
        lomba.setAdapter(adapterLomba);
    }

    private void FirebaseSearchLomba(String kataMasukan) {
        String lower = kataMasukan.toLowerCase();
        options = new FirestoreRecyclerOptions.Builder<VariabelLomba>()
                .setQuery(firebaseFirestore.collection("Lomba").orderBy("queryJenis")
                        .startAt(Preference.getDataJenisLomba(getContext())+" "+lower)
                        .endAt(Preference.getDataJenisLomba(getContext())+" "+lower+"\ufaff"), VariabelLomba.class)
                .build();

        adapterLomba = new AdapterLomba(options);
        adapterLomba.startListening();
        lomba.setAdapter(adapterLomba);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId() == R.id.filter_jenis_lomba) {
            PopBottomJenisLomba popBottomJenisLomba = new PopBottomJenisLomba();
            popBottomJenisLomba.show((Objects.requireNonNull(getFragmentManager())), "FilterJenisLomba");
            final FragmentManager fragmentManager1 = getFragmentManager();
            fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                    super.onFragmentViewDestroyed(fm, f);
                    fragmentManager1.unregisterFragmentLifecycleCallbacks(this);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).commit();
                }
            }, false);
        }
        else if (view.getId() == R.id.tambah_lomba){
            FragmentManager fragmentManager2 = getFragmentManager();
            fragmentManager2.beginTransaction().replace(R.id.contain_all, new FragmentTambahLomba())
                    .commit();
        }else if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).addToBackStack(null).commit();
        } else if (view.getId() == R.id.bank_soal) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.home1) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.fragment_beasiswa) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentBeasiswa()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.daftar_request_lomba){
            if (Preference.getDataUsername(getContext()).isEmpty()){
                FragmentLogin fragmentLogin = new FragmentLogin();
                fragmentManager.beginTransaction().replace(R.id.contain_all, fragmentLogin).commit();
            }else {
                Bundle bundle =new Bundle();
                bundle.putString("1","kosong");
                FragmentRequesLomba fragmentRequesLomba = new FragmentRequesLomba();
                fragmentRequesLomba.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.contain_all, fragmentRequesLomba).commit();
            }
        }
}}
