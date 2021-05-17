package com.example.dikti.geodesiBangga;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dikti.R;
import com.example.dikti.fragment_Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class TambahGeodesiBangga extends Fragment {

    TextView tambah,add,hapus;
    EditText judul,pemenang,link;
    ImageView gambarGeodesiBangga,kembali;
    public Uri gambar;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.fragment_tambah_informasi,container,false);

        tambah = view.findViewById(R.id.tambah);
        judul = view.findViewById(R.id.judul_informasi);
        pemenang = view.findViewById(R.id.isi_informasi);
        link = view.findViewById(R.id.link_informasi);
        add = view.findViewById(R.id.addData);
        hapus = view.findViewById(R.id.hapusdata);
        hapus.setVisibility(View.GONE);
        View tambahFoto = view.findViewById(R.id.tambah_foto);

        tambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pilihgambar();
            }
        });

        gambarGeodesiBangga = view.findViewById(R.id.gambar_geodesi_bangga);
        kembali = view.findViewById(R.id.kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).addToBackStack(null).commit();
            }
        });


        storageReference = FirebaseStorage.getInstance().getReference();

        tambah.setText("Tambah Geodesi Bangga");
        judul.setHint("Nama Lomba");
        pemenang.setHint("Nama Pemenang");
        link.setHint("Deskripsi (Opsional)");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload();
            }
        });
        return view;
    }

    private void Pilihgambar(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && data != null){
            gambar=data.getData();
            gambarGeodesiBangga.setImageURI(gambar);
        }
    }

    private void Upload(){
        showNotification("Geodesi Bangga sedang ditambahkan","Tunggu hingga muncul notifikasi berikutnya");
        add.setText("Tunggu");
        final StorageReference ref=storageReference.child("Geodesi Bangga/"+judul.getText().toString()+"."+getExtension(gambar));
        ref.putFile(gambar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference isiData=FirebaseFirestore.getInstance().collection("Geodesi Bangga").document(judul.getText().toString());
                        VariabelGeodesiBangga variabelGeodesiBangga = new VariabelGeodesiBangga();
                        variabelGeodesiBangga.setJudul(judul.getText().toString());
                        variabelGeodesiBangga.setFoto(String.valueOf(uri));
                        variabelGeodesiBangga.setPemenang(pemenang.getText().toString());
                        if (link.getText().toString().isEmpty()){
                            variabelGeodesiBangga.setDeskripsi("");
                        }else {
                            variabelGeodesiBangga.setDeskripsi(link.getText().toString());
                        }
                        isiData.set(variabelGeodesiBangga);
                        showNotification("Geodesi Bannga berhasil ditambahkan","");
                        add.setText("Add");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showNotification("Geodesi Bannga gagal ditambahkan","");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showNotification("Geodesi Bannga gagal ditambahkan","");
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
