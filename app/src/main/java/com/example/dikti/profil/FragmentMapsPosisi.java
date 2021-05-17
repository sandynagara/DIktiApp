package com.example.dikti.profil;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.anggota.FragmentAnggota;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMapsPosisi extends Fragment {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    private TextView lintang,bujur;
    private View view;
    Task<Location> task;
    String foto;
    ImageView posisi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_maps_posisi, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        lintang = view.findViewById(R.id.lintang);
        bujur = view.findViewById(R.id.bujur);
        posisi = view.findViewById(R.id.posisi);

        ImageView kembali = view.findViewById(R.id.kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentProfil()).commit();
            }
        });

        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.isi_peta_anggota);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        Toast.makeText(getContext(),"Drag a Marker First",Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            getCurrentLocation(task);
        }else {
            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            getCurrentLocation(task);
        }

        return view;
    }

    private void getCurrentLocation(Task<Location> task) {
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            getFoto();
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(foto)));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                            dragMarker(googleMap,markerOptions);
                        }
                    });
                }
            }
        }
        );
    }

    private void getFoto(){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Anggota").document(Preference.getDataUsername(getContext()));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                foto = (String) documentSnapshot.get("foto");
            }
        });
    }

    private void dragMarker(GoogleMap googleMap,MarkerOptions markerOptions){
        googleMap.addMarker(markerOptions).setDraggable(true);
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                LatLng latLngFix = marker.getPosition();
                lintang.setText(Double.toString(latLngFix.latitude));
                bujur.setText(Double.toString(latLngFix.longitude));
                inputGeopoint(marker,latLngFix);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                LatLng latLngFix = marker.getPosition();
                lintang.setText(Double.toString(latLngFix.latitude));
                bujur.setText(Double.toString(latLngFix.longitude));
                inputGeopoint(marker,latLngFix);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latLngFix = marker.getPosition();
                lintang.setText(Double.toString(latLngFix.latitude));
                bujur.setText(Double.toString(latLngFix.longitude));
                inputGeopoint(marker,latLngFix);
            }
        });

    }

    private void inputGeopoint(final Marker marker, final LatLng latLng){
        posisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeoPoint geoPoint = new GeoPoint(latLng.latitude,latLng.longitude);
                DocumentReference isiData= FirebaseFirestore.getInstance().collection("Anggota").document(Preference.getDataUsername(getContext()));
                Map<String,Object> map = new HashMap<>();
                map.put("posisi",geoPoint);
                isiData.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Update Berhasil",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Update Gagal",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 4 ){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation(task);
            }
        }
    }
}
