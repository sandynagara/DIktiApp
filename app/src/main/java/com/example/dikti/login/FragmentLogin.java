package com.example.dikti.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dikti.MainActivity;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.anggota.tambahAnggota.VariabelTambahAnggota;
import com.example.dikti.fragment_Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class FragmentLogin extends Fragment implements View.OnClickListener {

    private EditText username,password;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_login,container,false);

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        View login = view.findViewById(R.id.login);

        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_Home()).addToBackStack(null).commit();
            }
        });

        login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login){
            final String isiUsername = username.getText().toString();
            final String isiPassword = password.getText().toString();
            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Anggota/" + isiUsername).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String cekUsername = documentSnapshot.getString("username");
                    if (isiUsername.equals(cekUsername)) {
                        String cekPassword = documentSnapshot.getString("password");
                        if (isiPassword.equals(cekPassword)) {
                            String cekAdmin = documentSnapshot.getString("status");
                            assert cekAdmin != null;
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            if (cekAdmin.equals("Admin")) {
                                Preference.setDataAs(getContext(), "Admin");
                                Preference.setDataLogin(getContext(), true);
                                Preference.setDataUsername(getContext(), isiUsername);
                                Notifikasi(isiUsername);
                                fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).commit();
                            } else if (cekAdmin.equals("User")) {
                                Preference.setDataLogin(getContext(), true);
                                Preference.setDataAs(getContext(), "User");
                                Preference.setDataUsername(getContext(), isiUsername);
                                Notifikasi(isiUsername);
                                fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).commit();
                            }
                        } else {
                            Toast.makeText(getContext(), "Password salah", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Username tidak Terdaftar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void Notifikasi(final String isiUsername){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                        Log.d(TAG, token);
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        DocumentReference isiData=firebaseFirestore.collection("Anggota").document(isiUsername);
                        Map<String,Object> map = new HashMap<>();
                        map.put("token",token);
                        isiData.update(map);
                    }
                });
    }
}
