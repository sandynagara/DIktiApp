package com.example.dikti.lombaBeasiswa.lomba;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.banksoal.FragmentDetailSoal;
import com.example.dikti.login.FragmentLogin;
import com.example.dikti.lombaBeasiswa.lomba.requestTim.FragmentRequesLomba;
import com.example.dikti.lombaBeasiswa.lomba.requestTim.RequestTim;
import com.example.dikti.lombaBeasiswa.lomba.tambahLomba.FragmentEditLomba;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentDetailLomba extends Fragment {

    private TextView deadline;
    private TextView namaLomba;
    private TextView jenisLomba;
    private TextView biaya;
    private TextView peserta;
    private TextView link;
    private TextView deskripsi;
    private ImageView gambarLomba;
    private String isiLink,isiPeserta,isiNamaLomba;
    private View request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_lomba,container,false);

        deadline=view.findViewById(R.id.deadline);
        namaLomba=view.findViewById(R.id.nama_lomba);
        jenisLomba=view.findViewById(R.id.jenis_lomba);
        biaya=view.findViewById(R.id.biaya);
        peserta=view.findViewById(R.id.jenis_peserta);
        link=view.findViewById(R.id.link);
        deskripsi=view.findViewById(R.id.deskripsi_lomba);
        gambarLomba=view.findViewById(R.id.gambarlomba);
        final TextView requestTim = view.findViewById(R.id.requestTim);
        final ImageView editLomba = view.findViewById(R.id.edit);
        request = view.findViewById(R.id.request);
        TextView daftarTim = view.findViewById(R.id.daftar_tim);
        request.setVisibility(View.GONE);
        editLomba.setVisibility(view.GONE);

        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_lomba()).addToBackStack(null).commit();
            }
        });

        final String idLomba = getArguments().getString("1");

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Lomba/" + idLomba).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                isiNamaLomba = documentSnapshot.getString("nama");
                Long deadlineTanggal = documentSnapshot.getLong("deadlineTanggal");
                String deadlineBulan = documentSnapshot.getString("deadlineBulan");
                Long deadlineTahun = documentSnapshot.getLong("deadlineTahun");
                String isiFoto = documentSnapshot.getString("foto");
                String isiJenisLomba = documentSnapshot.getString("jenis");
                isiLink = documentSnapshot.getString("link");
                String isiDeskripsi = documentSnapshot.getString("deskripsi");
                Long isiBiaya = documentSnapshot.getLong("biaya");
                isiPeserta = documentSnapshot.getString("peserta");
                String pengirim = documentSnapshot.getString("pengirim");

                namaLomba.setText(isiNamaLomba);
                deadline.setText(deadlineTanggal.toString() + " " + deadlineBulan + " " + deadlineTahun.toString());
                jenisLomba.setText(isiJenisLomba);
                deskripsi.setText(isiDeskripsi);
                biaya.setText("Rp " + isiBiaya.toString());
                peserta.setText(isiPeserta);
                link.setText(isiLink);

                if (isiPeserta.equals("Tim")){
                    request.setVisibility(View.VISIBLE);
                    request.setBackgroundColor(Color.TRANSPARENT);
                }

                if (Preference.getDataAs(getContext()).equals("Admin") || Preference.getDataUsername(getContext()).equals(pengirim)){
                    editLomba.setVisibility(View.VISIBLE);
                }

                Glide.with(getContext())
                        .load(isiFoto)
                        .placeholder(R.drawable.logo_dikti)
                        .into(gambarLomba);

                gambarLomba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("1",idLomba);
                        bundle.putString("2","Lomba");
                        FragmentDetailSoal fragmentDetailSoal = new FragmentDetailSoal();
                        fragmentDetailSoal.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentDetailSoal).commit();
                    }
                });
            }

        });


        editLomba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("1",idLomba);
                FragmentEditLomba fragmentEditLomba = new FragmentEditLomba();
                fragmentEditLomba.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentEditLomba).commit();
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(isiLink));
                startActivity(intent);
            }
        });

        requestTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preference.getDataUsername(getContext()).isEmpty()){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contain_all,new FragmentLogin()).commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("1",idLomba);
                    RequestTim requestTim = new RequestTim();
                    requestTim.setArguments(bundle);
                    requestTim.show(getActivity().getSupportFragmentManager(),"Request Tim");
                }
            }
        });

        daftarTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preference.getDataUsername(getContext()).isEmpty()){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contain_all,new FragmentLogin()).commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("1",isiNamaLomba);
                    FragmentRequesLomba fragmentRequesLomba = new FragmentRequesLomba();
                    fragmentRequesLomba.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentRequesLomba).commit();
                }

            }
        });

        return view;
    }
}
