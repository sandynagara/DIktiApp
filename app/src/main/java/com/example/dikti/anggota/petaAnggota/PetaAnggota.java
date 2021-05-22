package com.example.dikti.anggota.petaAnggota;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.SplashScreen;
import com.example.dikti.anggota.FragmentAnggota;
import com.example.dikti.anggota.Fragment_Detail_Anggota;
import com.example.dikti.anggota.PopBottomDepartemen;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.fragment_Home;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.getSystemService;

public class PetaAnggota extends Fragment implements View.OnClickListener {

    private GoogleMap map;
    private FirebaseFirestore db;
    private ImageView anggota,tombolAnggota;
    private View toolbar,home,lomba,banksoal,filterDepartemen,filterAngkatan;
    private TextView textAnggota,daftarDepartemen,daftarAngkatan;
    private SearchView searchView;
    private View view1;
    private CollectionReference collectionReference;
    private  SupportMapFragment supportMapFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view1 != null) {
            ViewGroup parent = (ViewGroup) view1.getParent();
            if (parent != null)
                parent.removeView(view1);
        }
        try {
            view1 = inflater.inflate(R.layout.fragment_peta_anggota, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        anggota = view1.findViewById(R.id.anggota);
        toolbar = view1.findViewById(R.id.toolbar);
        home = toolbar.findViewById(R.id.home1);
        banksoal = toolbar.findViewById(R.id.bank_soal);
        tombolAnggota = toolbar.findViewById(R.id.simbol_anggota);
        textAnggota = toolbar.findViewById(R.id.text_anggota);
        lomba = toolbar.findViewById(R.id.lomba);
        searchView = view1.findViewById(R.id.search);
        filterDepartemen = view1.findViewById(R.id.filter_departemen);
        filterAngkatan = view1.findViewById(R.id.filter_angkatan);
        daftarDepartemen = view1.findViewById(R.id.daftarDepartemen);
        daftarAngkatan = view1.findViewById(R.id.daftar_angkatan);

        cekSDK();

        cekKondisiAngkatan();
        cekKondisiDepartemen();

        supportMapFragment = (SupportMapFragment)
               getChildFragmentManager().findFragmentById(R.id.isi_peta_anggota1);

        db = FirebaseFirestore.getInstance();
        String s ="";
        QueryMarker(s,"1");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                map.clear();
                QueryMarker(s,"2");
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                map.clear();
                QueryMarker(s,"2");
                return false;
            }
        });

        filterAngkatan.setOnClickListener(this);
        filterDepartemen.setOnClickListener(this);
        anggota.setOnClickListener(this);
        banksoal.setOnClickListener(this);
        lomba.setOnClickListener(this);
        home.setOnClickListener(this);
        return view1;
    }

    private void PindahFragment(){
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                cekKondisiAngkatan();
                cekKondisiDepartemen();
                cekKondisiAll();
                map.clear();
                String s = "";
                QueryMarker(s,"2");
            }
        }, false);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
            onDestroy();
        } else if (view.getId() == R.id.bank_soal) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).commit();
        }else if (view.getId() == R.id.lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).commit();
        }else if (view.getId() == R.id.home1) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).commit();
        }else if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
            onDestroy();
        }if (view.getId()==R.id.filter_departemen){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Departemen");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterDepartemen");
            PindahFragment();
        }else if (view.getId()==R.id.filter_angkatan){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Angkatan");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterAngkatan");
            PindahFragment();
        }
    }

    public void QueryMarker(final String s, final String kode){
        collectionReference = db.collection("Anggota");
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int n = 1;
                            for (DocumentSnapshot document : task.getResult()){
                                String id = document.getId();
                                String localName = (String) document.get("namaLengkap");
                                GeoPoint location = (GeoPoint) document.get("posisi");
                                String angkatan = (String) document.get("queryangkatan");
                                String foto = (String) document.get("foto");
                                String departemen = (String) document.get("querydepartemen");
                                String campuran = (String) document.get("querycampuran");
                                assert location != null;
                                LatLng localPosition = new LatLng(location.getLatitude(), location.getLongitude());

                                if (!Preference.getFilterDepartemen(getContext()).isEmpty() || !Preference.getFilterAngkatan(getContext()).isEmpty()){
                                     if (!Preference.getFilterAngkatan(getContext()).isEmpty() && !Preference.getFilterDepartemen(getContext()).isEmpty()){
                                        if (campuran.startsWith(Preference.getFilterAngkatan(getContext())+" "+Preference.getFilterDepartemen(getContext())+" "+s.toLowerCase())){
                                            Titik(localPosition,localName,foto,id);
                                            n++;
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(localPosition,14));
                                        }
                                    } else if (Preference.getFilterDepartemen(getContext()).isEmpty()){
                                        if (angkatan.startsWith(Preference.getFilterAngkatan(getContext())+" "+s.toLowerCase())){
                                            Titik(localPosition,localName,foto,id);
                                            n++;
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(localPosition,14));
                                        }
                                    } else if (Preference.getFilterAngkatan(getContext()).isEmpty()){
                                        if (departemen.startsWith(Preference.getFilterDepartemen(getContext())+" "+s.toLowerCase())) {
                                            Titik(localPosition,localName,foto,id);
                                            n++;
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(localPosition,14));
                                        }
                                    }

                                }else {
                                    if (kode.equals("1")){
                                            LatLng localPosition1 = new LatLng(-7.771273, 110.376072);
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(localPosition1,14));
                                            Titik(localPosition,localName,foto,id);
                                            n++;
                                        }
                                    else {
                                        if (localName.toLowerCase().startsWith(s.toLowerCase())) {
                                            Titik(localPosition,localName,foto,id);
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(localPosition,14));
                                            n++;
                                        }
                                    }
                                }
                                if (n>50){
                                    break;
                                }
                            }
                        }
                    }
                });
            }});
    }

    public void Titik(LatLng geoPoint,String nama,String foto,String id){
        LatLng localPosition = new LatLng(0,0);
        if (!geoPoint.equals(localPosition)){
            Marker marker = map.addMarker(new MarkerOptions().snippet(id)
                    .position(geoPoint).title(nama)
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(foto))));
        }
        PopUpDetail();
    }

    public void PopUpDetail(){
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final String id = marker.getSnippet();
                final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                dialog.setContentView(R.layout.pop_up_peta);
                final CircleImageView circleImageView = dialog.findViewById(R.id.foto_anggota);
                final TextView nama = dialog.findViewById(R.id.nama_anggota);
                final TextView nim = dialog.findViewById(R.id.nim);
                final TextView departemen = dialog.findViewById(R.id.departemen);
                TextView detail = dialog.findViewById(R.id.detail);
                Task<DocumentSnapshot> task = db.collection("Anggota").document(id).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String namaAnggota = documentSnapshot.getString("namaLengkap");
                                assert namaAnggota != null;
                                assert nama != null;
                                if (namaAnggota.length()>=27){
                                    nama.setText(documentSnapshot.getString("namaLengkap")+" ...");
                                }else {
                                    nama.setText(documentSnapshot.getString("namaLengkap"));
                                }
                                nim.setText(documentSnapshot.getString("nim"));
                                departemen.setText(documentSnapshot.getString("departemen"));
                                Glide.with(getContext())
                                        .load(documentSnapshot.getString("foto"))
                                        .placeholder(R.drawable.logo_dikti)
                                        .into(circleImageView);
                            }
                        });

                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putString("1",id);
                        Fragment_Detail_Anggota fragment_detail_anggota = new Fragment_Detail_Anggota();
                        fragment_detail_anggota.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.contain_all,fragment_detail_anggota).commit();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    public void cekKondisiAngkatan(){
        if (!Preference.getFilterAngkatan(getContext()).isEmpty() && !Preference.getFilterAngkatan(getContext()).equals("All")){
            GantiWarna(daftarAngkatan,filterAngkatan,Color.WHITE,R.drawable.border_biru,Preference.getFilterAngkatan(getContext()));
        }else {
            GantiWarna(daftarAngkatan,filterAngkatan,Color.GRAY,R.drawable.border,"Angkatan : All");
        }
    }

    public void cekKondisiDepartemen(){
        if (!Preference.getFilterDepartemen(getContext()).isEmpty() && !Preference.getFilterDepartemen(getContext()).equals("All")){
            GantiWarna(daftarDepartemen,filterDepartemen,Color.WHITE,R.drawable.border_biru,Preference.getFilterDepartemen(getContext()));
        }else {
            GantiWarna(daftarDepartemen,filterDepartemen,Color.GRAY,R.drawable.border,"Departemen : All");
        }
    }

    public void cekKondisiAll(){
        if (Preference.getFilterDepartemen(getContext()).equals("All")){
            Preference.clearDepartemen(getContext());
        }
        if (Preference.getFilterAngkatan(getContext()).equals("All")){
            Preference.clearAngkatan(getContext());
        }
    }

    public void GantiWarna(TextView filer,View filterView,int warna,int border,String nama){
        filer.setText(nama);
        filer.setTextColor(warna);

        Drawable drawable1 = getResources().getDrawable(border);
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

    private Bitmap getMarkerBitmapFromView(String url) {

        View customMarkerView = LayoutInflater.from(getContext()).inflate(R.layout.custom_marker, null);
        CircleImageView markerImageView = (CircleImageView) customMarkerView.findViewById(R.id.profile_image);
        Glide.with(getContext()).load(url).placeholder(R.drawable.logo_dikti).into(markerImageView);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
}
