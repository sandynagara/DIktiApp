package com.example.dikti;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.dikti.Informasi.AdapterInformasi;
import com.example.dikti.Informasi.FragmentInformasi;
import com.example.dikti.Informasi.VariabelInformasi;
import com.example.dikti.Informasi.TambahInformasi;
import com.example.dikti.anggota.FragmentAnggota;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.banner.AdapterBanner;
import com.example.dikti.banner.VariabelBanner;
import com.example.dikti.geodesiBangga.AdapterGeodesiBangga;
import com.example.dikti.geodesiBangga.FragmentGeodesiBangga;
import com.example.dikti.geodesiBangga.TambahGeodesiBangga;
import com.example.dikti.geodesiBangga.VariabelGeodesiBangga;
import com.example.dikti.login.FragmentLogin;
import com.example.dikti.lombaBeasiswa.lomba.VariabelLomba;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.example.dikti.profil.FragmentProfil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class fragment_Home extends Fragment implements View.OnClickListener {

    private CircleImageView circleImageView;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);

        circleImageView = view.findViewById(R.id.foto_anggota);
        dialog =new Dialog(getContext());

        View toolbar = view.findViewById(R.id.toolbar);
        View anggota = toolbar.findViewById(R.id.anggota);
        View profile1 = toolbar.findViewById(R.id.bank_soal);
        ImageView home = toolbar.findViewById(R.id.simbol_home);
        final ImageView tambahInformasi = view.findViewById(R.id.tambah_informasi);
        TextView textHome = toolbar.findViewById(R.id.text_home);
        View lomba = toolbar.findViewById(R.id.lomba);
        View beasiswa = toolbar.findViewById(R.id.home1);
        View dasboard = view.findViewById(R.id.dashboard);
        ImageView credit = view.findViewById(R.id.credit);
        final TextView loginAtauUsername = view.findViewById(R.id.login_atau_username);
        RecyclerView banner = view.findViewById(R.id.banner);
        RecyclerView lomba1 = view.findViewById(R.id.daftarLomba1);
        RecyclerView informasi = view.findViewById(R.id.informasi);
        RecyclerView geodesiBangga = view.findViewById(R.id.geodesi_bangga);
        View lainnya = view.findViewById(R.id.cardinformasi);
        TextView aboutSIG = view.findViewById(R.id.about_sig);
        TextView aboutPJ = view.findViewById(R.id.about_pj);
        TextView aboutHidro = view.findViewById(R.id.about_hidro);
        TextView aboutSutris = view.findViewById(R.id.about_sutris);
        ImageView about = view.findViewById(R.id.credit);
        ImageView tambahGeodesiBangga = view.findViewById(R.id.tambah_geodesi_bangga);
        tambahGeodesiBangga.setVisibility(View.GONE);
        View lainnyaGeodesiBangga = view.findViewById(R.id.card_lainnya);

        //Notifikasi();

        //TambahInformasi
        tambahInformasi.setVisibility(View.INVISIBLE);
        if (Preference.getDataAs(getContext()).equals("Admin")){
            tambahInformasi.setVisibility(View.VISIBLE);
            tambahInformasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    TambahInformasi tambahInformasi = new TambahInformasi();
                    Bundle bundle = new Bundle();
                    bundle.putString("1","kosong");
                    tambahInformasi.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.contain_all,tambahInformasi).commit();
                }
            });
            tambahGeodesiBangga.setVisibility(View.VISIBLE);
            tambahGeodesiBangga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contain_all,new TambahGeodesiBangga()).commit();
                }
            });
        }

        Preference.clearAngkatan(getContext());
        Preference.clearDepartemen(getContext());
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        //Mengecheck SDK dan Kondisi Login
        home.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_home_blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textHome.setTextColor(getActivity().getColor(R.color.deepSkyBlue));
        }else {
            textHome.setTextColor(ContextCompat.getColor(getContext(),R.color.deepSkyBlue));
        }

        if (!Preference.getDataLogin(getContext())){
            circleImageView.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_person_24));
            loginAtauUsername.setText("Sign In");
        }
        else if (Preference.getDataLogin(getContext())){
            Task<DocumentSnapshot> documentReference = firebaseFirestore.document("Anggota/" + Preference.getDataUsername(getContext())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String dataNama = documentSnapshot.getString("namaLengkap");
                    String Foto = documentSnapshot.getString("foto");

                    Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext())
                            .load(Foto)
                            .placeholder(R.drawable.logo_dikti)
                            .into(circleImageView);

                    loginAtauUsername.setText(dataNama);
                }
            });
        }

        //Firestore untuk Banner
        FirestoreRecyclerOptions<VariabelBanner> optionBanner =new FirestoreRecyclerOptions.Builder<VariabelBanner>()
                .setQuery(firebaseFirestore.collection("Banner"), VariabelBanner.class)
                .build();
        banner.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        AdapterBanner adapterBanner = new AdapterBanner(optionBanner);
        adapterBanner.startListening();
        banner.setAdapter(adapterBanner);

        //Firestore untuk Lomba
        FirestoreRecyclerOptions optionsLomba = new FirestoreRecyclerOptions.Builder<VariabelLomba>()
                .setQuery(firebaseFirestore.collection("Lomba").orderBy("favorit").startAt(true),VariabelLomba.class)
                .build();
        lomba1.setLayoutManager(new StaggeredGridLayoutManager(1,LinearLayoutManager.HORIZONTAL));
        AdapterHome adapterLomba = new AdapterHome(optionsLomba);
        adapterLomba.startListening();
        lomba1.setAdapter(adapterLomba);

        //Firestore untuk Informasi
        FirestoreRecyclerOptions optionsInformasi = new FirestoreRecyclerOptions.Builder<VariabelInformasi>()
                .setQuery(firebaseFirestore.collection("Informasi").orderBy("time", Query.Direction.DESCENDING),VariabelInformasi.class)
                .build();
        informasi.setLayoutManager(new StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL));
        final AdapterInformasi adapterInformasi = new AdapterInformasi(optionsInformasi,getContext());
        adapterInformasi.startListening();
        informasi.setAdapter(adapterInformasi);

        //Firestore untuk Geodesi Bangga
        FirestoreRecyclerOptions optionsGeodesiBangga = new FirestoreRecyclerOptions.Builder<VariabelGeodesiBangga>()
                .setQuery(firebaseFirestore.collection("Geodesi Bangga"),VariabelGeodesiBangga.class)
                .build();
        geodesiBangga.setLayoutManager(new StaggeredGridLayoutManager(1,LinearLayoutManager.HORIZONTAL));
        final AdapterGeodesiBangga adapterGeodesiBangga = new AdapterGeodesiBangga(optionsGeodesiBangga,"1");
        adapterGeodesiBangga.startListening();
        geodesiBangga.setAdapter(adapterGeodesiBangga);

        anggota.setOnClickListener(this);
        profile1.setOnClickListener(this);
        lomba.setOnClickListener(this);
        beasiswa.setOnClickListener(this);
        dasboard.setOnClickListener(this);
        lainnya.setOnClickListener(this);
        aboutHidro.setOnClickListener(this);
        aboutPJ.setOnClickListener(this);
        aboutSIG.setOnClickListener(this);
        aboutSutris.setOnClickListener(this);
        about.setOnClickListener(this);
        lainnyaGeodesiBangga.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.anggota) {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
        } else if (view.getId() == R.id.bank_soal) {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).commit();
        }else if (view.getId() == R.id.lomba) {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).commit();
        }else if (view.getId() == R.id.dashboard) {
            if (!Preference.getDataLogin(getContext())) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentLogin()).addToBackStack(null).commit();
            }else {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentProfil()).addToBackStack(null).commit();
            }
        }else if (view.getId()==R.id.cardinformasi){
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentInformasi()).commit();
        }else if (view.getId()==R.id.about_pj){
            PopUp("Geodinamika",R.color.Black,R.drawable.penginderaan_jauh);
        }else if (view.getId()==R.id.about_hidro){
            PopUp("Surhid",R.color.DarkBlue,R.drawable.hidro);
        }else if (view.getId()==R.id.about_sig){
            PopUp("Sig",R.color.DarkOrange,R.drawable.sig);
        }else if (view.getId()==R.id.about_sutris){
            PopUp("Sutris",R.color.DarkGreen,R.drawable.sutris);
        }else if (view.getId()==R.id.credit){
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAbout()).commit();
        }else if (view.getId()==R.id.card_lainnya){
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentGeodesiBangga()).commit();
        }
    }

    private void PopUp(String namaTim, final int color, final int gamber){
        Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document("Tim/" + namaTim).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                dialog.setContentView(R.layout.pop_up_tim_keilmuan);
                ImageView gambarTim = dialog.findViewById(R.id.gambarTim);
                TextView nama = dialog.findViewById(R.id.nama_tim);
                TextView deskripsiTim = dialog.findViewById(R.id.deskripsi_tim);
                TextView join = dialog.findViewById(R.id.link_tim);
                View card = dialog.findViewById(R.id.cardTim);
                ImageView close = dialog.findViewById(R.id.close);
                card.setBackgroundColor(getResources().getColor(color));
                gambarTim.setImageResource(gamber);
                nama.setText(documentSnapshot.getString("nama"));
                deskripsiTim.setText(documentSnapshot.getString("isi"));
                if (documentSnapshot.getString("link").equals("")){
                    join.setText("Coming Soon");
                }else {
                    join.setText("Join");
                    join.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentSnapshot.getString("link")));
                            startActivity(intent);
                        }
                    });
                }
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });
    }

    private void Notifikasi(){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, token);
                        Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
