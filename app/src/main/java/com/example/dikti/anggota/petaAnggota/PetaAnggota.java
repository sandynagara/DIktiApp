package com.example.dikti.anggota.petaAnggota;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.anggota.FragmentAnggota;
import com.example.dikti.anggota.PopBottomDepartemen;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.fragment_Home;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class PetaAnggota extends Fragment implements View.OnClickListener {

    private GoogleMap map;
    private FirebaseFirestore db;
    private ImageView anggota,tombolAnggota;
    private View toolbar,home,lomba,banksoal,filterDepartemen,filterAngkatan;
    private TextView textAnggota,daftarDepartemen,daftarAngkatan;
    private SearchView searchView;
    private static View view;
    private CollectionReference collectionReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_peta_anggota,container,false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        anggota = view.findViewById(R.id.anggota);
        toolbar = view.findViewById(R.id.toolbar);
        home = toolbar.findViewById(R.id.home1);
        banksoal = toolbar.findViewById(R.id.bank_soal);
        tombolAnggota = toolbar.findViewById(R.id.simbol_anggota);
        textAnggota = toolbar.findViewById(R.id.text_anggota);
        lomba = toolbar.findViewById(R.id.lomba);
        searchView = view.findViewById(R.id.search);
        filterDepartemen = view.findViewById(R.id.filter_departemen);
        filterAngkatan = view.findViewById(R.id.filter_angkatan);
        daftarDepartemen = view.findViewById(R.id.daftarDepartemen);
        daftarAngkatan = view.findViewById(R.id.daftar_angkatan);

        cekSDK();

        filterAngkatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GantiWarnaBiru(daftarAngkatan,filterAngkatan);
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.isi_peta_anggota);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Anggota");
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                String s ="";
                QueryMarker(s);
            }});

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                search(s);
                return false;
            }
        });

        filterAngkatan.setOnClickListener(this);
        filterDepartemen.setOnClickListener(this);
        anggota.setOnClickListener(this);
        banksoal.setOnClickListener(this);
        lomba.setOnClickListener(this);
        home.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
        } else if (view.getId() == R.id.bank_soal) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).commit();
        }else if (view.getId() == R.id.lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).commit();
        }else if (view.getId() == R.id.home1) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).commit();
        }else if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
        }if (view.getId()==R.id.filter_departemen){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Departemen");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterDepartemen");

        }else if (view.getId()==R.id.filter_angkatan){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Angkatan");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterAngkatan");

        }
    }

    public void QueryMarker(String s){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()){
                        String localName = (String) document.get("namaLengkap");
                        GeoPoint location = (GeoPoint) document.get("posisi");
                        String angkatan = (String) document.get("angkatan");
                        String departemen = (String) document.get("departemen");
                        assert location != null;
                        LatLng localPosition = new LatLng(location.getLatitude(), location.getLongitude());

                        if (!Preference.getFilterDepartemen(getContext()).isEmpty() || !Preference.getFilterAngkatan(getContext()).isEmpty()){
                            if (Preference.getFilterDepartemen(getContext()).isEmpty()){
                                if (angkatan.startsWith(Preference.getFilterAngkatan(getContext()))){
                                    Titik(localPosition,localName);
                                }
                            } else if (Preference.getFilterAngkatan(getContext()).isEmpty()){
                                if (departemen.startsWith(Preference.getFilterDepartemen(getContext()))) {
                                    Titik(localPosition,localName);
                                }
                            } else {

                            };
                        }else {
                            Titik(localPosition,localName);
                        }
                    }
                }
            }
        });
    }

    public void Titik(LatLng geoPoint,String nama){
        LatLng localPosition = new LatLng(0,0);
        if (!geoPoint.equals(localPosition)){
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(geoPoint)
                    .title(nama)
            );
        }
    }

    public void search(final String s){
        map.clear();
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()){
                        String localName = (String) document.get("namaLengkap");
                        String angkatan = (String) document.get("angkatan");
                        String departemen = (String) document.get("departemen");
                        String localNameLower = localName.toLowerCase();
                        GeoPoint location = (GeoPoint) document.get("posisi");
                        if (localNameLower.startsWith(s.toLowerCase())){
                            LatLng localPosition = new LatLng(location.getLatitude(), location.getLongitude());
                            Titik(localPosition,localName);
                        }
                    }
                }
            }
        });
    }

    public void GantiWarnaBiru(TextView filer,View filterView){
        filer.setText(Preference.getFilterDepartemen(getContext()));
        filer.setTextColor(Color.WHITE);

        Drawable drawable1 = getResources().getDrawable(R.drawable.border_biru);
        filterView.setBackground(drawable1);

        int padding_in_dp = 7;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        filterView.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
    }

    public void cekSDK(){
        tombolAnggota.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_map_blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textAnggota.setTextColor(getActivity().getColor(R.color.deepSkyBlue));
        }else {
            textAnggota.setTextColor(ContextCompat.getColor(getContext(),R.color.deepSkyBlue));
        }
    }
}
