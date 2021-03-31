package com.example.dikti.lombaBeasiswa.lomba.requestTim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentPeminatLomba extends BottomSheetDialogFragment {

    private AdapterPeminatLomba adapterPeminatLomba;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pop_up_peminat,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.daftar_peminat1);

        String idRequestLomba = getArguments().getString("1");

        FirestoreRecyclerOptions<VariabelRequestLomba> options = new FirestoreRecyclerOptions.Builder<VariabelRequestLomba>()
                .setQuery(FirebaseFirestore.getInstance().collection("Request Tim/"+idRequestLomba+"/Peminat"),VariabelRequestLomba.class)
                .build();

        adapterPeminatLomba = new AdapterPeminatLomba(options);
        recyclerView.setAdapter(adapterPeminatLomba);
        adapterPeminatLomba.startListening();
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
        adapterPeminatLomba.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapterPeminatLomba.stopListening();
    }
}
