package com.example.dikti.banksoal;

import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.anggota.FragmentAnggota;
import com.example.dikti.banksoal.tambahSoal.fragment_tambah_soal;
import com.example.dikti.fragment_Home;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Fragment_Home_Bank_Soal extends Fragment implements View.OnClickListener {

    public Fragment_Home_Bank_Soal() {
    }

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView matkulSemester1,matkulSemester2,matkulSemester3,matkulSemester4,matkulSemester5,matkulSemester6,matkulSemester7,matkulSemester8,matkulSemesterGasal,matkulSemesterGenap;
    private Boolean cek1,cek2,cek3,cek4,cek5,cek6,cek7,cek8,cek9,cek10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__home__bank__soal, container, false);

        TextView semester1 = view.findViewById(R.id.semester_1);
        TextView semester2 = view.findViewById(R.id.semester_2);
        TextView semester3 = view.findViewById(R.id.semester_3);
        TextView semester4 = view.findViewById(R.id.semester_4);
        TextView semester5 = view.findViewById(R.id.semester_5);
        TextView semester6 = view.findViewById(R.id.semester_6);
        TextView semester7 = view.findViewById(R.id.semester_7);
        TextView semester8 = view.findViewById(R.id.semester_8);
        TextView pilihanGasal = view.findViewById(R.id.semester_pilihan_gesal);
        TextView pilihanGenap = view.findViewById(R.id.semester_pilihan_genap);
        matkulSemester1 = view.findViewById(R.id.matkul_semester1);
        matkulSemester2 = view.findViewById(R.id.matkul_semester2);
        matkulSemester3 = view.findViewById(R.id.matkul_semester3);
        matkulSemester4 = view.findViewById(R.id.matkul_semester4);
        matkulSemester5 = view.findViewById(R.id.matkul_semester5);
        matkulSemester6 = view.findViewById(R.id.matkul_semester6);
        matkulSemester7 = view.findViewById(R.id.matkul_semester7);
        matkulSemester8 = view.findViewById(R.id.matkul_semester8);
        matkulSemesterGasal = view.findViewById(R.id.matkul_semester_pilihan_gasal);
        matkulSemesterGenap = view.findViewById(R.id.matkul_semester_pilihan_genap);
        View toolbar = view.findViewById(R.id.toolbar);
        View anggota = toolbar.findViewById(R.id.anggota);
        View profile1 = toolbar.findViewById(R.id.bank_soal);
        ImageView tombolBankSoal = toolbar.findViewById(R.id.simbol_bank_soal);
        TextView textBankSoal = toolbar.findViewById(R.id.text_bank_soal);
        View lomba = toolbar.findViewById(R.id.lomba);
        View home1 = toolbar.findViewById(R.id.home1);

        tombolBankSoal.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_insert_drive_file_blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textBankSoal.setTextColor(getActivity().getColor(R.color.deepSkyBlue));
        }else {
            textBankSoal.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.deepSkyBlue));
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        cek1 = true;cek2 = true ;cek3 =true ;cek4 = true;cek5 = true ;cek6 =true ;cek7 = true;cek8 = true;cek9 =true;cek10 =true;

        semester1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 1",matkulSemester1,cek1);
                cek1= !cek1;
            }
        });

        semester2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 2",matkulSemester2,cek2);
                cek2= !cek2;
            }
        });

        semester3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 3",matkulSemester3,cek3);
                cek3= !cek3;
            }
        });

        semester4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 4",matkulSemester4,cek4);
                cek4= !cek4;
            }
        });

        semester5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 5",matkulSemester5,cek5);
                cek5= !cek5;
            }
        });

        semester6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 6",matkulSemester6,cek6);
                cek6= !cek6;
            }
        });

        semester7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 7",matkulSemester7,cek7);
                cek7= !cek7;
            }
        });

        semester8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester 8",matkulSemester8,cek8);
                cek8= !cek8;
            }
        });

        pilihanGasal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester Gasal",matkulSemesterGasal,cek9);
                cek9= !cek9;
            }
        });

        pilihanGenap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dropdown("Semester Genap",matkulSemesterGenap,cek10);
                cek10= !cek10;
            }
        });

        View tambahSoal = view.findViewById(R.id.tambah_soal);
        if (!Preference.getDataUsername(getContext()).isEmpty()){
            tambahSoal.setVisibility(View.VISIBLE);
            tambahSoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_tambah_soal())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        anggota.setOnClickListener(this);
        profile1.setOnClickListener(this);
        lomba.setOnClickListener(this);
        home1.setOnClickListener(this);

        return view;
    }

    public void Dropdown(String collection, RecyclerView recyclerView, Boolean cek){
        if (cek){
            recyclerView.setVisibility(View.VISIBLE);
            FirestoreRecyclerOptions<VariabelBankSoal> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<VariabelBankSoal>()
                    .setQuery(firebaseFirestore.collection(collection), VariabelBankSoal.class)
                    .build();

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            AdapterBankSoal adapterBankSoal = new AdapterBankSoal(firestoreRecyclerOptions);
            adapterBankSoal.startListening();
            recyclerView.setAdapter(adapterBankSoal);
        }else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId() == R.id.anggota) {
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