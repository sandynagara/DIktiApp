package com.example.dikti.lombaBeasiswa.Beasiswa;

import android.app.Dialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.dikti.R;
import com.example.dikti.lombaBeasiswa.Beasiswa.tambahBeasiswa.FragmentDetailBeasiswa;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FragmentEditBeasiswa extends Fragment implements View.OnClickListener {

    private ImageView gambarBeasiswa;
    private TextView deadline;
    private EditText namaBeasiswa,link,deskripsi;
    Dialog dialog;
    private String idBeasiswa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_beasiswa,container,false);

        ImageView hapus = view.findViewById(R.id.hapus);
        deadline = view.findViewById(R.id.deadline);
        TextView update = view.findViewById(R.id.update);
        TextView cancel = view.findViewById(R.id.cancel);
        namaBeasiswa = view.findViewById(R.id.nama_beasiswa);
        link = view.findViewById(R.id.link);
        deskripsi = view.findViewById(R.id.deskripsi_beasiswa);

        idBeasiswa = getArguments().getString("1");

        Task<DocumentSnapshot> documentReference =  FirebaseFirestore.getInstance().document("Beasiswa/"+idBeasiswa).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String isiNamaLomba = documentSnapshot.getString("nama");
                Long deadlineTanggal = documentSnapshot.getLong("deadlineTanggal");
                String deadlineBulan = documentSnapshot.getString("deadlineBulan");
                Long deadlineTahun = documentSnapshot.getLong("deadlineTahun");
                String isiLink = documentSnapshot.getString("link");
                String isiDeskripsi = documentSnapshot.getString("deskripsi");

                namaBeasiswa.setText(isiNamaLomba);
                deadline.setText(deadlineTanggal.toString()+" "+deadlineBulan+" "+deadlineTahun.toString());
                link.setText(isiLink);
                deskripsi.setText(isiDeskripsi);
            }

        });

        update.setOnClickListener(this);
        cancel.setOnClickListener(this);
        hapus.setOnClickListener(this);

        return view;
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
        bundle.putString("1",idBeasiswa);
        final FragmentDetailBeasiswa fragmentDetailBeasiswa = new FragmentDetailBeasiswa();
        fragmentDetailBeasiswa.setArguments(bundle);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Beasiswa").document(idBeasiswa);

        if (view.getId()==R.id.update){
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("nama",namaBeasiswa.getText().toString());
                    map.put("link",link.getText().toString());
                    map.put("deskripsi",deskripsi.getText().toString());
                    map.put("queryNama",namaBeasiswa.getText().toString().toLowerCase());

                    documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Update Berhasil",Toast.LENGTH_SHORT).show();
                            fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentDetailBeasiswa).addToBackStack(null).commit();
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
                    fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentDetailBeasiswa).addToBackStack(null).commit();
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
                            fragmentManager.beginTransaction().replace(R.id.contain_all,new FragmentBeasiswa()).addToBackStack(null).commit();
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
