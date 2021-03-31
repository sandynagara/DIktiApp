package com.example.dikti.banksoal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        download.setVisibility(View.GONE);

        if (matkul.equals("lomba")){

            kembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentDetailLomba fragmentDetailLomba = new FragmentDetailLomba();
                    Bundle bundle = new Bundle();
                    bundle.putString("1",idSoal);
                    fragmentDetailLomba.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.contain_all,fragmentDetailLomba).commit();

                }
            });

            Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document("Lomba/"+idSoal).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Foto = documentSnapshot.getString("foto");
                    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).defaultDisplayImageOptions(defaultOptions).build();
                    ImageLoader.getInstance().init(config);
                    ImageLoader.getInstance().displayImage(Foto, fotoSoal);
                }
            });
        }else {
            semester = getArguments().getString("3");

            kembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Fragement_detail_matkul fragement_detail_matkul = new Fragement_detail_matkul();
                    Bundle bundle = new Bundle();
                    bundle.putString("1",semester);
                    bundle.putString("3",matkul);
                    fragement_detail_matkul.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.contain_all,fragement_detail_matkul).commit();

                }
            });

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
                            Toast.makeText(getContext(),"Masih dalam tahap Pengembangan",Toast.LENGTH_SHORT).show();
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
