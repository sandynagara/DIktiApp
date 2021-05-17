package com.example.dikti.profil;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.fragment_Home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfil extends Fragment implements View.OnClickListener {

    private TextView nama,email,password,line,noHp,alamat,aboutMe,posisiLintang,posisiBujur,departemen;
    private FirebaseFirestore firebaseFirestore;
    private CircleImageView imageView;
    private String dataNama,dataEmail,dataPassword,dataLine,dataNoHp,Foto,dataAlamat,dataAboutMe;
    EditText isiUpdate;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profil,container,false);

        dialog =new Dialog(Objects.requireNonNull(getContext()));

        nama = view.findViewById(R.id.namaprofil);
        email = view.findViewById(R.id.email2);
        password = view.findViewById(R.id.password2);
        line=view.findViewById(R.id.id_line2);
        noHp=view.findViewById(R.id.no_telepon2);
        imageView = view.findViewById(R.id.foto_anggota);
        alamat = view.findViewById(R.id.alamat2);
        aboutMe = view.findViewById(R.id.about_me2);
        posisiLintang = view.findViewById(R.id.posisilintang);
        posisiBujur= view.findViewById(R.id.posisibujur);
        departemen = view.findViewById(R.id.departemen);
        ImageView kembali = view.findViewById(R.id.kembali);

        ImageView editEmail = view.findViewById(R.id.editEmail);
        ImageView editPassword = view.findViewById(R.id.editPassword);
        ImageView editNoHp = view.findViewById(R.id.editNoHp);
        ImageView editLine = view.findViewById(R.id.editLine);
        ImageView editAlamat = view.findViewById(R.id.edit_alamat);
        ImageView editAboutMe = view.findViewById(R.id.edit_about_me);
        ImageView editPosisi = view.findViewById(R.id.editPosisi);
        TextView logOut = view.findViewById(R.id.log_out);

        firebaseFirestore= FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Anggota/" + Preference.getDataUsername(getContext())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dataNama = documentSnapshot.getString("namaLengkap");
                dataEmail = documentSnapshot.getString("email");
                dataPassword = documentSnapshot.getString("password");
                dataLine = documentSnapshot.getString("line");
                dataNoHp = documentSnapshot.getString("telepon");
                Foto = documentSnapshot.getString("foto");
                dataAlamat = documentSnapshot.getString("alamat");
                dataAboutMe = documentSnapshot.getString("aboutMe");
                GeoPoint isiPosisi = documentSnapshot.getGeoPoint("posisi");
                String lintang = Double.toString(isiPosisi.getLatitude());
                String bujur = Double.toString(isiPosisi.getLongitude());

                nama.setText(dataNama);
                email.setText(dataEmail);
                password.setText(dataPassword);
                line.setText(dataLine);
                noHp.setText(dataNoHp);
                alamat.setText(dataAlamat);
                aboutMe.setText(dataAboutMe);
                posisiLintang.setText(lintang);
                posisiBujur.setText(","+bujur);
                departemen.setText(documentSnapshot.getString("departemen"));
                Glide.with(getActivity().getApplicationContext())
                        .load(Foto)
                        .placeholder(R.drawable.logo_dikti)
                        .into(imageView);
            }
        });

        logOut.setOnClickListener(this);
        editLine.setOnClickListener(this);
        editNoHp.setOnClickListener(this);
        editEmail.setOnClickListener(this);
        editPassword.setOnClickListener(this);
        editAboutMe.setOnClickListener(this);
        editAlamat.setOnClickListener(this);
        editPosisi.setOnClickListener(this);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_Home fragmentHome = new fragment_Home();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentHome).commit();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.log_out) {
            Preference.clearData(getContext());
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).commit();
        }
        else if (view.getId()==R.id.editEmail){
            PopupUpdate("email","Masukkan Email Anda","Email Anda",dataEmail,email);
        }
        else if (view.getId()==R.id.editPassword){
            PopupUpdate("password","Masukkan Password Anda","Password Anda",dataPassword,password);
        }
        else if (view.getId()==R.id.editNoHp){
            PopupUpdate("telepon","Masukkan Nomor Handphone Anda","Nomor Handphone Anda",dataNoHp,noHp);
        }
        else if (view.getId()==R.id.editLine){
            PopupUpdate("line","Masukkan Id Line Anda","Id Line Anda",dataLine,line);
        }
        else if (view.getId()==R.id.edit_alamat){
            PopupUpdate("alamat","Masukkan Alamat Anda","Alamat Anda",dataAlamat,alamat);
        }
        else if (view.getId()==R.id.edit_about_me){
            PopupUpdate("aboutMe","Masukkan deskripsi Anda","About Me",dataAboutMe,aboutMe);
        }
        else if (view.getId()==R.id.editPosisi){
            FragmentMapsPosisi fragmentMapsPosisi = new FragmentMapsPosisi();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentMapsPosisi).addToBackStack(null).commit();
        }
    }

    private void PopupUpdate(final String tempatUpdate, String judul, String hint, String isiSebelum, final TextView ubah){
        View simpan,batal;
        TextView popup_masukan;
        dialog.setContentView(R.layout.popup_update);
        popup_masukan = dialog.findViewById(R.id.popup_masukan);
        popup_masukan.setText(judul);
        simpan = dialog.findViewById(R.id.update);
        batal = dialog.findViewById(R.id.batal);
        isiUpdate = dialog.findViewById(R.id.isi_update);
        isiUpdate.setHint(hint);
        isiUpdate.setText(isiSebelum);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task<Void> update = firebaseFirestore.collection("Anggota").document(Preference.getDataUsername(getContext())).update(tempatUpdate,isiUpdate.getText().toString());
                ubah.setText(isiUpdate.getText().toString());
                dialog.dismiss();
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         dialog.dismiss();
                                     }
                                 }
        );
        dialog.show();
    }
}
