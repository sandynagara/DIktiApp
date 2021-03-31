package com.example.dikti.anggota;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopBottomDepartemen extends BottomSheetDialogFragment {

    private DocumentReference databaseReference;
    private AdapterFilterDepartemen adapterFilterDepartemen;
    private AdapterFilterAngkatan adapterFilterAngkatan;
    private Context context;
    private String filter;

    public PopBottomDepartemen dapet(String msg) {
        PopBottomDepartemen fragment = new PopBottomDepartemen();

        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_bottom_departemen,container,false);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        RecyclerView namaDepartemen = v.findViewById(R.id.recycle_departemen);
        TextView judul = v.findViewById(R.id.nama_departemen);

        namaDepartemen.setLayoutManager(new GridLayoutManager(context,2));

        filter = getArguments().getString("msg");

        FirestoreRecyclerOptions options;
        if (filter == "Angkatan"){
            judul.setText(filter);
            options = new FirestoreRecyclerOptions.Builder<VariabelFilterDepartemen>()
                    .setQuery(firebaseFirestore.collection("Angkatan"),VariabelFilterDepartemen.class)
                    .setLifecycleOwner(this)
                    .build();

            adapterFilterAngkatan = new AdapterFilterAngkatan(options,PopBottomDepartemen.this);
            adapterFilterAngkatan.startListening();
            namaDepartemen.setAdapter(adapterFilterAngkatan);
        } else {
            options = new FirestoreRecyclerOptions.Builder<VariabelFilterDepartemen>()
                    .setQuery(firebaseFirestore.collection("Departemen"),VariabelFilterDepartemen.class)
                    .setLifecycleOwner(this)
                    .build();

            adapterFilterDepartemen = new AdapterFilterDepartemen(options,PopBottomDepartemen.this);
            adapterFilterDepartemen.startListening();
            namaDepartemen.setAdapter(adapterFilterDepartemen);
        }
        return v;
    }

    @Override
    public void onStop(){
        super.onStop();
        if (filter == "Angkatan"){
            adapterFilterAngkatan.stopListening();
        }else {
            adapterFilterDepartemen.stopListening();
        }
    }
}
