package com.example.dikti.lombaBeasiswa.Beasiswa.tambahBeasiswa;

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

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.lombaBeasiswa.Beasiswa.FragmentBeasiswa;
import com.example.dikti.lombaBeasiswa.Beasiswa.VariabelBeasiswa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FragmentTambahBeasiswa extends Fragment {

    private EditText namaBeasiswa,link,deadlineTahun,deskripsi;
    private Spinner deadlineTanggal,deadlineBulan;
    private ImageView fotoLomba;
    private Long isiDeadlineTahunLong,isiDeadlineTanggalLong,deadline;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private  TextView tambah;
    private String isiNamaBeasiswa,isiLink,isiDeskripsi,isiDeadlineBulan,isiDeadlineTanggal,isiDeadlineTanggalint,isiDeadlineBulanint;
    public Uri gambar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tambah_beasiswa,container,false);

        namaBeasiswa = view.findViewById(R.id.nama_beasiswa);
        link = view.findViewById(R.id.link_lomba);
        deskripsi = view.findViewById(R.id.deskripsi_lomba);
        deadlineTanggal = view.findViewById(R.id.deadline_tanggal);
        deadlineBulan = view.findViewById(R.id.deadline_Bulan);
        deadlineTahun = view.findViewById(R.id.deadline_tahun);
        View tambahFoto = view.findViewById(R.id.tambah_foto);
        fotoLomba = view.findViewById(R.id.foto_beasiswa);
        fotoLomba.setVisibility(View.GONE);
        tambah = view.findViewById(R.id.addData);

        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new FragmentBeasiswa()).addToBackStack(null).commit();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Integer[] tanggal = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
        SpinnerInterger(tanggal,deadlineTanggal);

        String[] bulan = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        SpinnerString(bulan,deadlineBulan);

        deadlineTanggal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiDeadlineTanggal = deadlineTanggal.getSelectedItem().toString();
                convertTanggal(isiDeadlineTanggal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deadlineBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiDeadlineBulan = deadlineBulan.getSelectedItem().toString();
                convertBulan(isiDeadlineBulan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pilihgambar();
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
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
            fotoLomba.setVisibility(View.VISIBLE);
            fotoLomba.setImageURI(gambar);
        }
    }

    private void Upload(){
        if (!namaBeasiswa.getText().toString().isEmpty() && !deadlineTahun.getText().toString().isEmpty()){
            showNotification("Beasiswa sedang ditambahkan","Tunggu hingga muncul notifikasi berikutnya");
            tambah.setText("Tunggu");
            isiNamaBeasiswa = namaBeasiswa.getText().toString();
            isiDeskripsi = deskripsi.getText().toString();
            isiLink = link.getText().toString();
            final String pengirim = Preference.getDataUsername(getContext());
            isiDeadlineTanggalLong = Long.parseLong(isiDeadlineTanggal);
            isiDeadlineTahunLong = Long.parseLong(deadlineTahun.getText().toString());
            deadline = Long.parseLong(deadlineTahun.getText().toString()+isiDeadlineBulanint+isiDeadlineTanggalint);

            final StorageReference ref=storageReference.child("Beasiswa/"+isiDeadlineTahunLong+"/"+isiDeadlineBulan+"/"+"/"+isiNamaBeasiswa+"."+getExtension(gambar));
            ref.putFile(gambar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DocumentReference isiData=firebaseFirestore.collection("Beasiswa").document(isiNamaBeasiswa);
                            VariabelBeasiswa variabelBeasiswa = new VariabelBeasiswa();
                            variabelBeasiswa.setDeadline(deadline);
                            variabelBeasiswa.setDeadlineBulan(isiDeadlineBulan);
                            variabelBeasiswa.setDeadlineTahun(isiDeadlineTahunLong);
                            variabelBeasiswa.setDeadlineTanggal(isiDeadlineTanggalLong);
                            variabelBeasiswa.setDeskripsi(isiDeskripsi);
                            variabelBeasiswa.setFoto(String.valueOf(uri));
                            variabelBeasiswa.setLink(isiLink);
                            variabelBeasiswa.setNama(isiNamaBeasiswa);
                            variabelBeasiswa.setQueryNama(isiNamaBeasiswa.toLowerCase());
                            if (Preference.getDataUsername(getContext()).isEmpty()){
                                variabelBeasiswa.setPengirim("Tidak Diketahui");
                            }else {
                                variabelBeasiswa.setPengirim(pengirim);
                            }
                            variabelBeasiswa.setFavorit(false);
                            isiData.set(variabelBeasiswa);
                            tambah.setText("Add");
                            showNotification(isiNamaBeasiswa+" berhasil ditambahkan","Terima kasih telah menambahkan daftar beasiswa");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tambah.setText("Add");
                            showNotification("Beasiswa gagal ditambahkan","Pastikan sinyal di rumah anda dalam keadaan baik");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    tambah.setText("Add");
                    showNotification("Beasiswa gagal ditambahkan","Pastikan sinyal di rumah anda dalam keadaan baik");
                }
            });
        }else {
            if (namaBeasiswa.getText().toString().isEmpty()){
                namaBeasiswa.setError("Nama Beasiswa Tidak boleh Kosong");
            }

            if (deadlineTahun.getText().toString().isEmpty()){
                deadlineTahun.setError("Deadline Tahun Tidak boleh Kosong");
            }
        }

    }

    private void SpinnerString (String[] strings,Spinner spinner){
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(strings));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.spinner_departemen,arrayList);
        spinner.setAdapter(arrayAdapter);
    }

    private void SpinnerInterger (Integer[] strings,Spinner spinner){
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(strings));
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.spinner_departemen,arrayList);
        spinner.setAdapter(arrayAdapter);
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

    private void convertBulan (String string){
        isiDeadlineBulanint = "0";
        if (string.equals("Januari")){
            isiDeadlineBulanint="01";
        }else if (string.equals("Februari")){
            isiDeadlineBulanint="02";
        }else if (string.equals("Maret")){
            isiDeadlineBulanint="03";
        }else if (string.equals("April")){
            isiDeadlineBulanint="04";
        }else if (string.equals("Mei")){
            isiDeadlineBulanint="05";
        }else if (string.equals("Juni")){
            isiDeadlineBulanint="06";
        }else if (string.equals("Juli")){
            isiDeadlineBulanint="07";
        }else if (string.equals("Agustus")){
            isiDeadlineBulanint="08";
        }else if (string.equals("September")){
            isiDeadlineBulanint="09";
        }else if (string.equals("Oktober")){
            isiDeadlineBulanint="10";
        }else if (string.equals("November")){
            isiDeadlineBulanint="11";
        }else if (string.equals("Desember")){
            isiDeadlineBulanint="12";
        }
    }

    private void convertTanggal (String string){
        int acuan = 0;
        acuan = Integer.parseInt(string);
        if (acuan <= 9){
            isiDeadlineTanggalint = "0"+string;
        }else {
            isiDeadlineTanggalint = string;
        }
    }
}
