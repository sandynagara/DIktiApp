package com.example.dikti.anggota;

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
import com.example.dikti.anggota.tambahAnggota.FragmentTambahAnggota;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.fragment_Home;
import com.example.dikti.login.FragmentLogin;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FragmentAnggota extends Fragment implements View.OnClickListener {

    private AdapterAnggota adapterAnggota;
    private RecyclerView namaAnggota;
    private FirestoreRecyclerOptions<VariabelAnggota> options;
    private FirebaseFirestore firebaseFirestore;
    private TextView daftarDepartemen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_anggota,container,false);

        if (Preference.getDataUsername(getContext()).isEmpty()) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentLogin fragmentLogin = new FragmentLogin();
            fragmentManager.beginTransaction().replace(R.id.contain_all, fragmentLogin).commit();
        }
        View filterDepartemen = view.findViewById(R.id.filter_departemen);
        View filterAngkatan = view.findViewById(R.id.filter_angkatan);
        daftarDepartemen = view.findViewById(R.id.daftarDepartemen);
        TextView daftarAngkatan = view.findViewById(R.id.daftar_angkatan);
        SearchView searchView = view.findViewById(R.id.search);
        namaAnggota = view.findViewById(R.id.daftaranggota);
        View tambahAnggota = view.findViewById(R.id.tambah_anggota);
        View toolbar = view.findViewById(R.id.toolbar);
        View anggota = toolbar.findViewById(R.id.anggota);
        View profile1 = toolbar.findViewById(R.id.bank_soal);
        ImageView tombolAnggota = toolbar.findViewById(R.id.simbol_anggota);
        TextView textAnggota = toolbar.findViewById(R.id.text_anggota);
        View lomba = toolbar.findViewById(R.id.lomba);
        View beasiswa = toolbar.findViewById(R.id.home1);

        namaAnggota.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        firebaseFirestore = FirebaseFirestore.getInstance();
        filterDepartemen.setOnClickListener(this);
        filterAngkatan.setOnClickListener(this);
        tambahAnggota.setOnClickListener(this);
        tambahAnggota.setVisibility(View.GONE);

        tombolAnggota.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_group_blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textAnggota.setTextColor(getActivity().getColor(R.color.deepSkyBlue));
        }else {
            textAnggota.setTextColor(ContextCompat.getColor(getContext(),R.color.deepSkyBlue));
        }

        if (Preference.getDataAs(getContext()).equals("Admin")){
            tambahAnggota.setVisibility(View.VISIBLE);
        }

        if(Preference.getFilterDepartemen(getContext()).equals("All")){
            Preference.clearDepartemen(getContext());
        }

        if(Preference.getFilterAngkatan(getContext()).equals("All")){
            Preference.clearAngkatan(getContext());
        }

        if (Preference.getFilterDepartemen(getContext()).isEmpty() && Preference.getFilterAngkatan(getContext()).isEmpty()){
            options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                    .setQuery(firebaseFirestore.collection("Anggota").orderBy("angkatan", Query.Direction.DESCENDING),VariabelAnggota.class)
                    .build();

            adapterAnggota=new AdapterAnggota(options);
            adapterAnggota.startListening();
            namaAnggota.setAdapter(adapterAnggota);
        } else {
            if (!Preference.getFilterDepartemen(getContext()).equals("") && !Preference.getFilterAngkatan(getContext()).equals("")){
                daftarDepartemen.setText(Preference.getFilterDepartemen(getContext()));
                daftarDepartemen.setTextColor(Color.WHITE);

                daftarAngkatan.setText(Preference.getFilterAngkatan(getContext()));
                daftarAngkatan.setTextColor(Color.WHITE);

                filterDepartemen = view.findViewById(R.id.filter_departemen);
                filterAngkatan = view.findViewById(R.id.filter_angkatan);
                Drawable drawable1 = getResources().getDrawable(R.drawable.border_biru);
                Drawable drawable2 = getResources().getDrawable(R.drawable.border_biru);

                filterDepartemen.setBackground(drawable2);
                filterAngkatan.setBackground(drawable1);

                int padding_in_dp = 7;
                final float scale = getResources().getDisplayMetrics().density;
                int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                filterAngkatan.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
                filterDepartemen.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);

                Long Angkatan = Long.valueOf(Preference.getFilterAngkatan(getContext()));
                options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                        .setQuery(firebaseFirestore.collection("Anggota").orderBy("querycampuran")
                                .startAt(Angkatan+" "+Preference.getFilterDepartemen(getContext()))
                                .endAt(Angkatan+" "+Preference.getFilterDepartemen(getContext())+"\ufaff"), VariabelAnggota.class)
                        .build();

                adapterAnggota=new AdapterAnggota(options);
                adapterAnggota.startListening();
                namaAnggota.setAdapter(adapterAnggota);
            }else {
                if (!Preference.getFilterAngkatan(getContext()).equals("")){
                    daftarAngkatan.setText(Preference.getFilterAngkatan(getContext()));
                    daftarAngkatan.setTextColor(Color.WHITE);
                    Drawable drawable = getResources().getDrawable(R.drawable.border_biru);
                    filterAngkatan.setBackground(drawable);
                    int padding_in_dp = 7;
                    final float scale = getResources().getDisplayMetrics().density;
                    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                    filterAngkatan.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
                    options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                            .setQuery(firebaseFirestore.collection("Anggota").orderBy("angkatan").startAt(Preference.getFilterAngkatan(getContext()))
                                    .endAt(Preference.getFilterAngkatan(getContext())), VariabelAnggota.class)
                            .build();
                    adapterAnggota=new AdapterAnggota(options);
                    adapterAnggota.startListening();
                    namaAnggota.setAdapter(adapterAnggota);
                }
                if (!Preference.getFilterDepartemen(getContext()).equals("")){
                    daftarDepartemen.setText(Preference.getFilterDepartemen(getContext()));
                    daftarDepartemen.setTextColor(Color.WHITE);
                    Drawable drawable = getResources().getDrawable(R.drawable.border_biru);
                    filterDepartemen.setBackground(drawable);
                    int padding_in_dp = 7;
                    final float scale = getResources().getDisplayMetrics().density;
                    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                    filterDepartemen.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
                    options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                            .setQuery(firebaseFirestore.collection("Anggota").orderBy("departemen")
                                    .startAt(Preference.getFilterDepartemen(getContext()))
                                    .endAt(Preference.getFilterDepartemen(getContext())+"\ufaff"), VariabelAnggota.class)
                            .build();
                    adapterAnggota=new AdapterAnggota(options);
                    adapterAnggota.startListening();
                    namaAnggota.setAdapter(adapterAnggota);
                }
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!Preference.getFilterDepartemen(getContext()).equals("") && !Preference.getFilterAngkatan(getContext()).equals("")){
                    FirebaseSearchCampuran(s);
                }else if (!Preference.getFilterDepartemen(getContext()).equals("") ){
                    FirebaseSearchDepartemen(s);
                }else if (!Preference.getFilterAngkatan(getContext()).equals("")){
                    FirebaseSearchAngkatan(s);
                }else {
                    FirebaseSearch(s,view);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (!Preference.getFilterDepartemen(getContext()).equals("") && !Preference.getFilterAngkatan(getContext()).equals("")){
                    FirebaseSearchCampuran(s);
                }else if (!Preference.getFilterDepartemen(getContext()).equals("") ){
                    FirebaseSearchDepartemen(s);
                }else if (!Preference.getFilterAngkatan(getContext()).equals("")){
                    FirebaseSearchAngkatan(s);
                }else {
                    FirebaseSearch(s,view);
                }
                return false;
            }
        });

        anggota.setOnClickListener(this);
        profile1.setOnClickListener(this);
        lomba.setOnClickListener(this);
        beasiswa.setOnClickListener(this);

        return view;
    }

    private void FirebaseSearch(String kataMasukan,View view) {
        Preference.clearDepartemen(getContext());
        daftarDepartemen.setText("Departemen : All");
        daftarDepartemen.setTextColor(Color.GRAY);
        RelativeLayout relativeLayout = view.findViewById(R.id.filter_departemen);
        Drawable drawable = getResources().getDrawable(R.drawable.border);
        relativeLayout.setBackground(drawable);
        int padding_in_dp = 7;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        relativeLayout.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
        options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy("queryNama").startAt(kataMasukan).endAt(kataMasukan+"\ufaff"), VariabelAnggota.class)
                .build();

        adapterAnggota=new AdapterAnggota(options);
        adapterAnggota.startListening();
        namaAnggota.setAdapter(adapterAnggota);
    }

    private void FirebaseSearchDepartemen(String kataMasukan) {
        String lower = kataMasukan.toLowerCase();
        options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy("querydepartemen")
                        .startAt(Preference.getFilterDepartemen(getContext())+" "+lower)
                        .endAt(Preference.getFilterDepartemen(getContext())+" "+lower+"\ufaff"), VariabelAnggota.class)
                .build();

        adapterAnggota=new AdapterAnggota(options);
        adapterAnggota.startListening();
        namaAnggota.setAdapter(adapterAnggota);
    }

    private void FirebaseSearchAngkatan(String kataMasukan) {
        String lower = kataMasukan.toLowerCase();
        options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy("queryangkatan")
                        .startAt(Preference.getFilterAngkatan(getContext())+" "+lower)
                        .endAt(Preference.getFilterAngkatan(getContext())+" "+lower+"\ufaff"), VariabelAnggota.class)
                .build();

        adapterAnggota=new AdapterAnggota(options);
        adapterAnggota.startListening();
        namaAnggota.setAdapter(adapterAnggota);
    }

    private void FirebaseSearchCampuran(String kataMasukan) {
        String lower = kataMasukan.toLowerCase();
        options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy("querycampuran")
                        .startAt(Preference.getFilterAngkatan(getContext())+" "+Preference.getFilterDepartemen(getContext())+" "+lower)
                        .endAt(Preference.getFilterAngkatan(getContext())+" "+Preference.getFilterDepartemen(getContext())+" "+lower+"\ufaff"), VariabelAnggota.class)
                .build();

        adapterAnggota=new AdapterAnggota(options);
        adapterAnggota.startListening();
        namaAnggota.setAdapter(adapterAnggota);
    }

    private void PindahFragment(){
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
            }
        }, false);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId()==R.id.filter_departemen){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Departemen");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterDepartemen");
            PindahFragment();

        }else if (view.getId()==R.id.filter_angkatan){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Angkatan");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterAngkatan");
            PindahFragment();

        }else if (view.getId()==R.id.tambah_anggota){
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentTambahAnggota()).commit();
        }
        else if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).addToBackStack(null).commit();
        } else if (view.getId() == R.id.bank_soal) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).addToBackStack(null).commit();
        }else if (view.getId() == R.id.home1) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).addToBackStack(null).commit();
        }
    }
}
