package com.example.dikti.banksoal.tambahSoal;

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

import com.example.dikti.R;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.banksoal.VariabelBankSoal;
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

public class fragment_tambah_soal extends Fragment {

    private Spinner semester,matkul,kelas;
    private FirebaseFirestore firebaseFirestore;
    private EditText dosen,tahunSoal;
    private List<String> isiMatkullist;
    public Uri gambar;
    private ImageView fotoSoal;
    private String isiSemester,isiKelas,isiMatkul;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tambah_soal,container,false);

        semester = view.findViewById(R.id.semester);
        matkul = view.findViewById(R.id.jenis_matkul);
        kelas = view.findViewById(R.id.kelas);
        dosen = view.findViewById(R.id.dosen);
        tahunSoal = view.findViewById(R.id.tahun_soal);
        TextView add = view.findViewById(R.id.addData);
        fotoSoal = view.findViewById(R.id.foto_soal);
        View tambahFoto= view.findViewById(R.id.tambah_foto);

        firebaseFirestore = FirebaseFirestore.getInstance();

        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new Fragment_Home_Bank_Soal()).commit();
            }
        });

        final String[] isiSemesterList = {"Semester 1","Semester 2","Semester 3","Semester 4","Semester 5","Semester 6","Semester 7","Semester 8","Pilihan Semester Ganjit","Pilihan Semester Genap"};
        String[] isiKelasList = {"A","B","C"};
        SpinnerString(isiSemesterList,semester);
        SpinnerString(isiKelasList,kelas);

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isiSemester = semester.getSelectedItem().toString();
                SpinnerJenisLomba(isiSemester);
                matkul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        isiMatkul=matkul.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
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
            fotoSoal.setImageURI(gambar);
        }
    }

    private void Upload(){

        final StorageReference ref= FirebaseStorage.getInstance().getReference().child("Bank Soal/"+isiSemester+"/"+isiMatkul+"/"+tahunSoal.getText().toString()+" "+kelas.getSelectedItem().toString()+" "+isiMatkul+"."+getExtension(gambar));
        ref.putFile(gambar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference isiData= FirebaseFirestore.getInstance().document(isiSemester+"/"+isiMatkul+"/"+isiMatkul+"/"+tahunSoal.getText().toString()+" "+kelas.getSelectedItem().toString()+" "+isiMatkul);
                        VariabelBankSoal variabelBankSoal = new VariabelBankSoal();
                        variabelBankSoal.setDosen(dosen.getText().toString());
                        variabelBankSoal.setSemester(isiSemester);
                        variabelBankSoal.setMataKuliah(isiMatkul);
                        variabelBankSoal.setTahun(Long.parseLong(tahunSoal.getText().toString()));
                        variabelBankSoal.setFoto(String.valueOf(uri));
                        isiData.set(variabelBankSoal);
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

    private void SpinnerString (String[] strings,Spinner spinner){
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(strings));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.spinner_departemen,arrayList);
        spinner.setAdapter(arrayAdapter);
    }

    private void SpinnerJenisLomba(final String semester){
        isiMatkullist = new ArrayList<>();
        Task<QuerySnapshot> collectionReference = firebaseFirestore.collection(semester).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("mataKuliah");
                        isiMatkullist.add(subject);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( getActivity(),R.layout.spinner_departemen,isiMatkullist);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_departemen);
                    matkul.setAdapter(arrayAdapter);
                }
            }
        });
    }
}
