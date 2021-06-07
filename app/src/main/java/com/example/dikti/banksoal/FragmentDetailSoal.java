package com.example.dikti.banksoal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dikti.R;

import com.example.dikti.lombaBeasiswa.Beasiswa.tambahBeasiswa.FragmentDetailBeasiswa;
import com.example.dikti.lombaBeasiswa.lomba.FragmentDetailLomba;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class FragmentDetailSoal extends Fragment {

    private ImageView download;
    private PhotoView fotoSoal;
    private String idSoal,semester,matkul,Foto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_detail_soal,container,false);

        fotoSoal = view.findViewById(R.id.gambar_soal);
        download = view.findViewById(R.id.download);
        ImageView kembali = view.findViewById(R.id.kembali);

        idSoal =getArguments().getString("1");
        matkul = getArguments().getString("2");

        final Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("FRAGMENT_ZOOM");
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        });

        if (matkul.equals("Lomba") || matkul.equals("Beasiswa")){

            Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document(matkul+"/"+idSoal).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Foto = documentSnapshot.getString("foto");
                    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).defaultDisplayImageOptions(defaultOptions).build();
                    ImageLoader.getInstance().init(config);
                    ImageLoader.getInstance().displayImage(Foto, fotoSoal);
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Foto));
                            startActivity(intent);
                        }
                    });
                }
            });


        } else {
            semester = getArguments().getString("3");

            Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document(semester+"/"+matkul+"/"+matkul+"/"+idSoal).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Foto = documentSnapshot.getString("foto");
                    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).defaultDisplayImageOptions(defaultOptions).build();
                    ImageLoader.getInstance().init(config);
                    ImageLoader.getInstance().displayImage(Foto, fotoSoal);
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Foto));
                            startActivity(intent);
                        }
                    });
                }
            });


        }



        return view;
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
