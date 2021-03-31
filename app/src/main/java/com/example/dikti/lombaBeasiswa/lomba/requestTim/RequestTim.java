package com.example.dikti.lombaBeasiswa.lomba.requestTim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RequestTim extends BottomSheetDialogFragment {

    private ImageView gambarLomba;
    private TextView namaLomba;
    private TextView isiJenisLomba;
    private TextView deadlineTampil;
    private EditText jumlahAnggota,syaratAnggota;
    private Long deadlineTahun,deadlineTanggal,deadline;
    private String idLomba,namaLomba2,isiJenisLombaString,deadlineBulan,foto,namaPengirim,idRequest,angkatan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_reques_tim,container,false);

        gambarLomba = view.findViewById(R.id.gambarlomba);
        namaLomba = view.findViewById(R.id.nama_lomba);
        TextView request = view.findViewById(R.id.requestTim);
        TextView cancel = view.findViewById(R.id.cancel);
        deadlineTampil = view.findViewById(R.id.deadline);
        jumlahAnggota = view.findViewById(R.id.jumlah_anggota);
        syaratAnggota = view.findViewById(R.id.syarat_anggota);
        isiJenisLomba = view.findViewById(R.id.isi_jenis_lomba);
        ImageView hapus = view.findViewById(R.id.hapus);
        TextView apply = view.findViewById(R.id.apply);
        TextView cancel2 = view.findViewById(R.id.cancel2);
        View pilihan1 = view.findViewById(R.id.pilihan1);
        View pilihan2 = view.findViewById(R.id.pilihan2);

        idLomba = getArguments().getString("1");
        idRequest = getArguments().getString("2");

        pilihan2.setVisibility(View.INVISIBLE);
        pilihan1.setVisibility(View.INVISIBLE);

        if (idLomba=="1"){
            pilihan2.setVisibility(View.VISIBLE);

            Task<DocumentSnapshot> task = FirebaseFirestore.getInstance().collection("Request Tim").document(idRequest).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    namaLomba.setText(documentSnapshot.getString("namaLomba"));
                    deadlineTampil.setText(documentSnapshot.getLong("deadlineTanggal")+" "+documentSnapshot.getString("deadlineBulan")+" "+documentSnapshot.getLong("deadlineTahun"));
                    isiJenisLomba.setText(documentSnapshot.getString("jenisLomba"));
                    jumlahAnggota.setText(Long.toString(documentSnapshot.getLong("jumlahAnggota")));
                    syaratAnggota.setText(documentSnapshot.getString("syaratAnggota"));

                    Glide.with(getContext())
                            .load(documentSnapshot.getString("foto"))
                            .into(gambarLomba);
                }
            });

            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Request Tim")
                            .document(idRequest);
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("jumlahAnggota",Long.parseLong(jumlahAnggota.getText().toString()));
                    map.put("syaratAnggota",syaratAnggota.getText().toString());
                    documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(),"Data berhasil di Update",Toast.LENGTH_SHORT);
                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(),"Data gagal di Update",Toast.LENGTH_SHORT);
                        }
                    });
                }
            });

            cancel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Request Tim")
                            .document(idRequest);
                    DocumentReference documentReference1 = FirebaseFirestore.getInstance().collection("Request Tim")
                            .document(idRequest).collection("Peminat").document();
                    documentReference1.delete();
                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(),"Data berhasil di hapus",Toast.LENGTH_SHORT);
                            dismiss();
                        }
                    });
                }
            });
        }
        else {
            pilihan1.setVisibility(view.VISIBLE);

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Lomba/"+idLomba).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    namaLomba.setText(documentSnapshot.getString("nama"));
                    isiJenisLombaString = documentSnapshot.getString("jenis");
                    namaLomba2 = documentSnapshot.getString("nama");
                    deadlineTahun = documentSnapshot.getLong("deadlineTahun");
                    deadlineBulan = documentSnapshot.getString("deadlineBulan");
                    deadlineTanggal = documentSnapshot.getLong("deadlineTanggal");
                    deadline = documentSnapshot.getLong("deadline");
                    foto = documentSnapshot.getString("foto");

                    Glide.with(getContext())
                            .load(documentSnapshot.getString("foto"))
                            .into(gambarLomba);
                }
            });

            Task<DocumentSnapshot> documentReferencePengguna = firebaseFirestore.document("Anggota/"+Preference.getDataUsername(getContext())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    namaPengirim = documentSnapshot.getString("namaLengkap");
                    angkatan = documentSnapshot.getString("angkatan");
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Request Tim").document(namaLomba2+" "+namaPengirim);
                    VariabelRequestLomba variabelRequestLomba = new VariabelRequestLomba();
                    variabelRequestLomba.setDeadline(deadline);
                    variabelRequestLomba.setDeadlineBulan(deadlineBulan);
                    variabelRequestLomba.setDeadlineTahun(deadlineTahun);
                    variabelRequestLomba.setDeadlineTanggal(deadlineTanggal);
                    variabelRequestLomba.setNamaLomba(namaLomba2);
                    variabelRequestLomba.setFoto(foto);
                    variabelRequestLomba.setSyaratAnggota(syaratAnggota.getText().toString());
                    variabelRequestLomba.setJumlahAnggota(Long.parseLong(jumlahAnggota.getText().toString()));
                    variabelRequestLomba.setIdLomba(idLomba);
                    variabelRequestLomba.setJenisLomba(isiJenisLombaString);
                    variabelRequestLomba.setIdPengirim(Preference.getDataUsername(getContext()));
                    variabelRequestLomba.setNamaPengirim(namaPengirim);
                    variabelRequestLomba.setQueryJenisLomba(isiJenisLombaString+" "+namaLomba2.toLowerCase());
                    variabelRequestLomba.setQueryNamaLomba(namaLomba2.toLowerCase());
                    variabelRequestLomba.setAngkatan(angkatan);
                    variabelRequestLomba.setQueryMyRequest(Preference.getDataUsername(getContext())+" "+namaLomba2.toLowerCase());
                    variabelRequestLomba.setQueryJenisMyRequest(isiJenisLombaString+" "+Preference.getDataUsername(getContext())+" "+namaLomba2.toLowerCase());

                    documentReference.set(variabelRequestLomba).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Request Tim Berhasil",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Request Tim Gagal",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        return view;
    }
}
