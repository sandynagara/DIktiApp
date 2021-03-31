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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfil extends Fragment implements View.OnClickListener {

    private TextView nama,email,password,line,noHp,alamat,aboutMe;
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
        ImageView kembali = view.findViewById(R.id.kembali);

        ImageButton editEmail = view.findViewById(R.id.editEmail);
        ImageButton editPassword = view.findViewById(R.id.editPassword);
        ImageButton editNoHp = view.findViewById(R.id.editNoHp);
        ImageButton editLine = view.findViewById(R.id.editLine);
        ImageButton editAlamat = view.findViewById(R.id.edit_alamat);
        ImageButton editAboutMe = view.findViewById(R.id.edit_about_me);
        View logOut = view.findViewById(R.id.log_out);

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

                nama.setText(dataNama);
                email.setText(dataEmail);
                password.setText(dataPassword);
                line.setText(dataLine);
                noHp.setText(dataNoHp);
                alamat.setText(dataAlamat);
                aboutMe.setText(dataAboutMe);
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
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_Home fragmentHome = new fragment_Home();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentHome).addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        View simpan,batal;
        TextView popup_masukan;
        dialog.setContentView(R.layout.popup_update);
        popup_masukan = dialog.findViewById(R.id.popup_masukan);
        simpan = dialog.findViewById(R.id.update);
        batal = dialog.findViewById(R.id.batal);
        if (view.getId()==R.id.log_out) {
            Preference.clearData(getContext());
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).commit();
        }
        else if (view.getId()==R.id.editEmail){
            isiUpdate = dialog.findViewById(R.id.isi_update);
            isiUpdate.setText(dataEmail);
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task<Void> update = firebaseFirestore.collection("Anggota").document(Preference.getDataUsername(getContext())).update("email",isiUpdate.getText().toString());
                    email.setText(isiUpdate.getText().toString());
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
        else if (view.getId()==R.id.editPassword){
            popup_masukan.setText("Masukkan Password Anda");
            isiUpdate = dialog.findViewById(R.id.isi_update);
            isiUpdate.setHint("Password Anda");
            isiUpdate.setText(dataPassword);
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task<Void> update = firebaseFirestore.collection("Anggota").document(Preference.getDataUsername(getContext())).update("password",isiUpdate.getText().toString());
                    password.setText(isiUpdate.getText().toString());
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
        else if (view.getId()==R.id.editNoHp){
            popup_masukan.setText("Masukkan Nomor Handphone Anda");
            isiUpdate = dialog.findViewById(R.id.isi_update);
            isiUpdate.setHint("Nomor Handphone Anda");
            isiUpdate.setText(dataNoHp);
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task<Void> update = firebaseFirestore.collection("Anggota").document(Preference.getDataUsername(getContext())).update("telepon",isiUpdate.getText().toString());
                    noHp.setText(isiUpdate.getText().toString());
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
        else if (view.getId()==R.id.editLine){
            popup_masukan.setText("Masukkan Id Line Anda");
            isiUpdate = dialog.findViewById(R.id.isi_update);
            isiUpdate.setHint("Id Line Anda");
            isiUpdate.setText(dataLine);
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task<Void> update = firebaseFirestore.collection("Anggota").document(Preference.getDataUsername(getContext())).update("line",isiUpdate.getText().toString());
                    line.setText(isiUpdate.getText().toString());
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
        else if (view.getId()==R.id.edit_alamat){
            popup_masukan.setText("Masukkan Alamat Anda");
            isiUpdate = dialog.findViewById(R.id.isi_update);
            isiUpdate.setHint("Alamat Anda");
            isiUpdate.setText(dataAlamat);
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task<Void> update = firebaseFirestore.collection("Anggota").document(Preference.getDataUsername(getContext())).update("alamat",isiUpdate.getText().toString());
                    alamat.setText(isiUpdate.getText().toString());
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
    }else if (view.getId()==R.id.edit_about_me){
            popup_masukan.setText("Masukkan deskripsi Anda");
            isiUpdate = dialog.findViewById(R.id.isi_update);
            isiUpdate.setHint("About Me");
            isiUpdate.setText(dataAboutMe);
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task<Void> update = firebaseFirestore.collection("Anggota").document(Preference.getDataUsername(getContext())).update("aboutMe",isiUpdate.getText().toString());
                    aboutMe.setText(isiUpdate.getText().toString());
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
}
