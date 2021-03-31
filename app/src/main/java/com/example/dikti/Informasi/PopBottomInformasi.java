package com.example.dikti.Informasi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dikti.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopBottomInformasi extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pop_up_informasi,container,false);

        final TextView judul = view.findViewById(R.id.judul_informasi);
        final TextView isi = view.findViewById(R.id.isi_informasi);
        final TextView link = view.findViewById(R.id.link_informasi);

        String timeStamp = getArguments().getString("1");

        final Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document("Informasi/"+timeStamp).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                judul.setText(documentSnapshot.getString("judul"));
                isi.setText(documentSnapshot.getString("info"));
                final String isiLink = documentSnapshot.getString("link");

                link.setText(isiLink);
                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(isiLink));
                        startActivity(intent);
                    }
                });

            }
        });

        return view;
    }
}
