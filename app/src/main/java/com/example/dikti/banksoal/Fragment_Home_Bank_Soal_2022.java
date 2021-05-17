package com.example.dikti.banksoal;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dikti.R;
import com.example.dikti.anggota.FragmentAnggota;
import com.example.dikti.fragment_Home;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;

import java.util.Objects;

public class Fragment_Home_Bank_Soal_2022 extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_bank_soal_2022, container, false);

        View toolbar = view.findViewById(R.id.toolbar);
        View anggota = toolbar.findViewById(R.id.anggota);
        View bankSoal = toolbar.findViewById(R.id.bank_soal);
        ImageView tombolBankSoal = toolbar.findViewById(R.id.simbol_bank_soal);
        TextView textBankSoal = toolbar.findViewById(R.id.text_bank_soal);
        View lomba = toolbar.findViewById(R.id.lomba);
        View home1 = toolbar.findViewById(R.id.home1);
        TextView k2016 = view.findViewById(R.id.k_2016);

        tombolBankSoal.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_insert_drive_file_blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textBankSoal.setTextColor(getActivity().getColor(R.color.deepSkyBlue));
        }else {
            textBankSoal.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.deepSkyBlue));
        }

        anggota.setOnClickListener(this);
        bankSoal.setOnClickListener(this);
        lomba.setOnClickListener(this);
        home1.setOnClickListener(this);
        k2016.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
        } else if (view.getId() == R.id.bank_soal) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).commit();
        }else if (view.getId() == R.id.lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).commit();
        }else if (view.getId() == R.id.home1) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).commit();
        }else if (view.getId() == R.id.k_2016){
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).commit();
        }
    }
}
