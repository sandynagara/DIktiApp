package com.example.dikti.anggota.tambahAnggota;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dikti.R;
import com.example.dikti.anggota.FragmentAnggota;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentTambahAnggota extends Fragment {

    private Spinner spinner;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private EditText username,namaLengkap,nim,angkatan;
    private CircleImageView fotoAnggota;
    private String isiUsername,isiNamaLengkap,isiNim,isiAngkatan,isiDepartemen;
    private Long isiAngkatanInt;
    private TextView add;
    private VariabelTambahAnggota variabelTambahAnggota;
    public Uri gambar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tambah_anggota,container,false);

        fotoAnggota=view.findViewById(R.id.foto_anggota);
        spinner = view.findViewById(R.id.departemen);
        username = view.findViewById(R.id.username);
        namaLengkap = view.findViewById(R.id.nama_lengkap);
        nim = view.findViewById(R.id.NIM);
        angkatan = view.findViewById(R.id.angkatan);
        ImageView tambahFoto = view.findViewById(R.id.add_foto);
        add = view.findViewById(R.id.addData);

        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new FragmentAnggota()).addToBackStack(null).commit();
            }
        });

        variabelTambahAnggota = new VariabelTambahAnggota();

        tambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pilihgambar();
            }
        });

        List<String> departemen = new ArrayList<>();

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore= FirebaseFirestore.getInstance();
        final List<String> isiJenisLombalist = new ArrayList<>();
        Task<QuerySnapshot> collectionReference = firebaseFirestore.collection("Departemen").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("Departemen");
                        isiJenisLombalist.add(subject);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_departemen,isiJenisLombalist);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_departemen);
                    spinner.setAdapter(arrayAdapter);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiDepartemen = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uploadgambar();
            }
        });

        return view;
    }

    private void Pilihgambar(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && data != null){
            gambar=data.getData();
            fotoAnggota.setImageURI(gambar);
        }
    }

    private void Uploadgambar(){
        showNotification("Anggota sedang ditambahkan","Tunggu hingga muncul notifikasi berikutnya");
        add.setText("Tunggu");
        isiUsername=username.getText().toString();
        isiNamaLengkap=namaLengkap.getText().toString();
        isiNim = nim.getText().toString();
        isiAngkatan = angkatan.getText().toString();
        final GeoPoint geoPoint = new GeoPoint(0,0);
        final StorageReference ref=storageReference.child("Anggota/"+isiAngkatan+"/"+isiDepartemen+"/"+isiNamaLengkap+"."+getExtension(gambar));
        ref.putFile(gambar)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                DocumentReference isiData=firebaseFirestore.collection("Anggota").document(isiUsername);
                                variabelTambahAnggota.setAngkatan(isiAngkatan);
                                variabelTambahAnggota.setFoto(String.valueOf(uri));
                                variabelTambahAnggota.setDepartemen(isiDepartemen);
                                variabelTambahAnggota.setEmail("-");
                                variabelTambahAnggota.setLine("-");
                                variabelTambahAnggota.setPassword(isiUsername);
                                variabelTambahAnggota.setNIM(isiNim);
                                variabelTambahAnggota.setStatus("User");
                                variabelTambahAnggota.setPosisi(geoPoint);
                                variabelTambahAnggota.setQueryangkatan(isiAngkatan+" "+isiNamaLengkap.toLowerCase());
                                variabelTambahAnggota.setQuerycampuran(isiAngkatan+" "+isiDepartemen+" "+isiNamaLengkap.toLowerCase());
                                variabelTambahAnggota.setQuerydepartemen(isiDepartemen+" "+isiNamaLengkap.toLowerCase());
                                variabelTambahAnggota.setQueryNama(isiNamaLengkap.toLowerCase());
                                variabelTambahAnggota.setTelepon("-");
                                variabelTambahAnggota.setUsername(isiUsername);
                                variabelTambahAnggota.setNamaLengkap(isiNamaLengkap);
                                variabelTambahAnggota.setAboutMe("-");
                                variabelTambahAnggota.setAlamat("-");
                                isiData.set(variabelTambahAnggota).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        add.setText("Add");
                                        showNotification(isiNamaLengkap+" berhasil ditambahkan","Terima kasih telah menambahkan anggota");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        add.setText("Add");
                                        showNotification("Anggota gagal ditambahkan","Pastikan sinyal di rumah anda dalam keadaan baik");
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showNotification("Anggota gagal ditambahkan","Pastikan sinyal di rumah anda dalam keadaan baik");
                                add.setText("Add");
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showNotification("Anggota gagal ditambahkan","Pastikan sinyal di rumah anda dalam keadaan baik");
                    }
                });
    }

    private String getExtension(Uri uri){
        ContentResolver contentResolver = Objects.requireNonNull(getActivity()).getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.logo_dikti)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }
}
