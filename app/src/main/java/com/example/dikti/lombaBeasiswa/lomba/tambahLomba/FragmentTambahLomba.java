package com.example.dikti.lombaBeasiswa.lomba.tambahLomba;

import android.content.ContentResolver;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FragmentTambahLomba extends Fragment {

    private EditText namaLomba,biayaPendaftaran,link,deskripsi,deadlineTahun;
    private Spinner jenisPeserta,jenisLomba,deadlineTanggal,deadlineBulan;
    private String isiNamaLomba,isiJenislomba,isiJenisPeserta,isiLink,isiDeskripsi,isiDeadlineBulan,isiDeadlineTanggal,isiDeadlineTanggalint,isiDeadlineBulanint;
    private Long isiDeadlineTahunLong,isiDeadlineTanggalLong,isiBiayaLong,deadline;
    private FirebaseFirestore firebaseFirestore;
    private List<String> isiJenisLombalist;
    private ImageView fotoLomba;
    private StorageReference storageReference;
    public Uri gambar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tambah_lomba,container,false);

        namaLomba = view.findViewById(R.id.nama_lomba);
        biayaPendaftaran = view.findViewById(R.id.biaya_pendaftaran);
        link = view.findViewById(R.id.link_lomba);
        deskripsi = view.findViewById(R.id.deskripsi_lomba);

        jenisPeserta = view.findViewById(R.id.jenis_peserta);
        jenisLomba = view.findViewById(R.id.jenis_lomba);
        deadlineTanggal = view.findViewById(R.id.deadline_tanggal);
        deadlineBulan = view.findViewById(R.id.deadline_Bulan);
        deadlineTahun = view.findViewById(R.id.deadline_tahun);
        View addFoto = view.findViewById(R.id.tambah_foto);
        fotoLomba = view.findViewById(R.id.foto_lomba);
        fotoLomba.setVisibility(View.GONE);

        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_lomba()).addToBackStack(null).commit();
            }
        });

        TextView add = view.findViewById(R.id.addData);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        SpinnerJenisLomba();

        Integer[] tanggal = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
        SpinnerInterger(tanggal,deadlineTanggal);

        String[] bulan = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        String[] peserta = {"Individu","Tim"};
        SpinnerString(peserta,jenisPeserta);
        SpinnerString(bulan,deadlineBulan);

        jenisLomba.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiJenislomba = jenisLomba.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        jenisPeserta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiJenisPeserta =  jenisPeserta.getSelectedItem().toString();
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

        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pilihgambar();
            }
        });

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
            fotoLomba.setVisibility(View.VISIBLE);
            fotoLomba.setImageURI(gambar);
        }
    }

    private void Upload(){
        isiNamaLomba = namaLomba.getText().toString();
        isiDeskripsi = deskripsi.getText().toString();
        isiLink = link.getText().toString();
        final String pengirim = Preference.getDataUsername(getContext());
        isiBiayaLong = Long.parseLong(biayaPendaftaran.getText().toString());
        isiDeadlineTanggalLong = Long.parseLong(isiDeadlineTanggal);
        isiDeadlineTahunLong = Long.parseLong(deadlineTahun.getText().toString());
        deadline = Long.parseLong(deadlineTahun.getText().toString()+isiDeadlineBulanint+isiDeadlineTanggalint);

        final StorageReference ref=storageReference.child("Lomba/"+isiDeadlineTahunLong+"/"+isiDeadlineBulan+"/"+isiJenislomba+"/"+isiNamaLomba+"."+getExtension(gambar));
        ref.putFile(gambar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference isiData=firebaseFirestore.collection("Lomba").document(isiNamaLomba);
                        VariabelTambahLomba variabelTambahLomba = new VariabelTambahLomba();
                        variabelTambahLomba.setBiaya(isiBiayaLong);
                        variabelTambahLomba.setDeadline(deadline);
                        variabelTambahLomba.setDeadlineBulan(isiDeadlineBulan);
                        variabelTambahLomba.setDeadlineTahun(isiDeadlineTahunLong);
                        variabelTambahLomba.setDeadlineTanggal(isiDeadlineTanggalLong);
                        variabelTambahLomba.setDeskripsi(isiDeskripsi);
                        variabelTambahLomba.setFoto(String.valueOf(uri));
                        variabelTambahLomba.setJenis(isiJenislomba);
                        variabelTambahLomba.setLink(isiLink);
                        variabelTambahLomba.setNama(isiNamaLomba);
                        variabelTambahLomba.setPeserta(isiJenisPeserta);
                        variabelTambahLomba.setQueryJenis(isiJenislomba+" "+isiNamaLomba.toLowerCase());
                        variabelTambahLomba.setQueryNama(isiNamaLomba.toLowerCase());
                        if (Preference.getDataUsername(getContext()).isEmpty()){
                            variabelTambahLomba.setPengirim("Tidak Diketahui");
                        }else {
                            variabelTambahLomba.setPengirim(pengirim);
                        }
                        variabelTambahLomba.setFavorit(false);
                        isiData.set(variabelTambahLomba);
                        Toast.makeText(getContext(),"Upload Success",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Upload Gagal",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Upload Gagal",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getExtension(Uri uri){
        ContentResolver contentResolver = Objects.requireNonNull(getActivity()).getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.spinner_departemen,isiJenisLombalist);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_departemen);
                    jenisLomba.setAdapter(arrayAdapter);
                }
            }
        });
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
