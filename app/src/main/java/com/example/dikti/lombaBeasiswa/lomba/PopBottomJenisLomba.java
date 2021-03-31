package com.example.dikti.lombaBeasiswa.lomba;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopBottomJenisLomba extends BottomSheetDialogFragment {

    private AdapterFilterJenisLomba adapterFilter;
        private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_bottom_jenis_lomba,container,false);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        RecyclerView namaDepartemen = v.findViewById(R.id.recycle_jenis_lomba);

        FirestoreRecyclerOptions<VariabelFilterJenisLomba> options = new FirestoreRecyclerOptions.Builder<VariabelFilterJenisLomba>()
                .setQuery(firebaseFirestore.collection("JenisLomba"), VariabelFilterJenisLomba.class)
                .build();

        adapterFilter = new AdapterFilterJenisLomba(options,PopBottomJenisLomba.this);
        namaDepartemen.setLayoutManager(new GridLayoutManager(context,2));
        namaDepartemen.setAdapter(adapterFilter);
        return v;
    }

        @Override
        public void onStart(){
            super.onStart();
            adapterFilter.startListening();
        }

        @Override
        public void onStop(){
            super.onStop();
            adapterFilter.stopListening();
        }

}
