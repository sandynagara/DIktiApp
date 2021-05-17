package com.example.dikti.lombaBeasiswa.lomba.tambahLomba;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.dikti.R;
import com.example.dikti.lombaBeasiswa.lomba.FragmentDetailLomba;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FragmentEditLomba extends Fragment implements View.OnClickListener {

    private TextView deadline;
    private EditText namaLomba,biaya,link,deskripsi;
    private Spinner jenisLomba,peserta;
    private FirebaseFirestore firebaseFirestore;
    private ImageView gambarLomba;
    private List<String> isiJenisLombalist;
    private String isiJenislomba,isiPeserta,idLomba;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_lomba,container,false);

        dialog =new Dialog(getContext());

        deadline=view.findViewById(R.id.deadline);
        namaLomba=view.findViewById(R.id.nama_lomba);
        jenisLomba=view.findViewById(R.id.jenis_lomba);
        biaya=view.findViewById(R.id.biaya);
        peserta=view.findViewById(R.id.jenis_peserta);
        link=view.findViewById(R.id.link);
        deskripsi=view.findViewById(R.id.deskripsi_lomba);
        gambarLomba=view.findViewById(R.id.gambarlomba);
        TextView update = view.findViewById(R.id.update);
        TextView cancel = view.findViewById(R.id.cancel);
        ImageView hapus = view.findViewById(R.id.hapus);

        firebaseFirestore = FirebaseFirestore.getInstance();

        SpinnerJenisLomba();

        idLomba = getArguments().getString("1");

        String[] peserta1 = {"Individu","Tim"};
        SpinnerString(peserta1,peserta);

        jenisLomba.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiJenislomba = jenisLomba.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        peserta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiPeserta =  peserta.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String idLomba = getArguments().getString("1");

        Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Lomba/" + idLomba).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String isiNamaLomba = documentSnapshot.getString("nama");
                Long deadlineTanggal = documentSnapshot.getLong("deadlineTanggal");
                String deadlineBulan = documentSnapshot.getString("deadlineBulan");
                Long deadlineTahun = documentSnapshot.getLong("deadlineTahun");
                String isiFoto = documentSnapshot.getString("foto");
                String isiLink = documentSnapshot.getString("link");
                String isiDeskripsi = documentSnapshot.getString("deskripsi");
                Long isiBiaya = documentSnapshot.getLong("biaya");

                namaLomba.setText(isiNamaLomba);
                deadline.setText(deadlineTanggal.toString() + " " + deadlineBulan + " " + deadlineTahun.toString());
                link.setText(isiLink);
                deskripsi.setText(isiDeskripsi);
                biaya.setText(isiBiaya.toString());

                Glide.with(getContext())
                        .load(isiFoto)
                        .into(gambarLomba);
            }

        });

        update.setOnClickListener(this);
        cancel.setOnClickListener(this);
        hapus.setOnClickListener(this);

        return view;
    }

    private void SpinnerJenisLomba(){
        isiJenisLombalist = new ArrayList<>();
        Task<QuerySnapshot> collectionReference = firebaseFirestore.collection("JenisLomba").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("JenisLomba");
                        isiJenisLombalist.add(subject);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.support_simple_spinner_dropdown_item,isiJenisLombalist);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_departemen);
                    jenisLomba.setAdapter(arrayAdapter);
                }
            }
        });
    }

    private void SpinnerString (String[] strings,Spinner spinner){
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(strings));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.support_simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        TextView update,cancel,popup_masukan,popup_tambahan;
        dialog.setContentView(R.layout.popup_peringatan);
        popup_masukan = dialog.findViewById(R.id.popup_masukan);
        popup_tambahan = dialog.findViewById(R.id.popup_tambahan);
        update = dialog.findViewById(R.id.update);
        cancel = dialog.findViewById(R.id.cancel);

        Bundle bundle = new Bundle();
        bundle.putString("1",idLomba);
        final FragmentDetailLomba fragmentDetailLomba = new FragmentDetailLomba();
        fragmentDetailLomba.setArguments(bundle);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Lomba").document(idLomba);

        if (view.getId()==R.id.update){
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("nama",namaLomba.getText().toString());
                    map.put("link",link.getText().toString());
                    map.put("deskripsi",deskripsi.getText().toString());
                    map.put("jenis",isiJenislomba);
                    map.put("peserta",isiPeserta);
                    map.put("biaya",Long.parseLong(biaya.getText().toString()));
                    map.put("queryJenis",isiJenislomba+" "+namaLomba.getText().toString());
                    map.put("queryNama",namaLomba.getText().toString().toLowerCase());

                    documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Update Berhasil",Toast.LENGTH_SHORT).show();
                            fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentDetailLomba).addToBackStack(null).commit();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Update Gagal",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            }
            );
            dialog.show();
        }
        else if (view.getId()==R.id.cancel){
            popup_masukan.setText("Apa anda Yakin ?");
            popup_tambahan.setText("Data yang anda masukan akan ke reset");
            update.setText("Ya");
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentDetailLomba).addToBackStack(null).commit();
                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            }
            );

            dialog.show();
        }
        else if (view.getId()==R.id.hapus){
            popup_masukan.setText("Apa anda Yakin ?");
            popup_tambahan.setText("Data yang anda masukan akan ke hilang secara permanen");
            update.setText("Ya");
            update.setBackground(getActivity().getDrawable(R.drawable.border_merah));
            update.setPadding(28,28,28,28);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_lomba()).addToBackStack(null).commit();
                            dialog.dismiss();
                        }
                    });
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            }
            );

            dialog.show();
        }
    }
}
