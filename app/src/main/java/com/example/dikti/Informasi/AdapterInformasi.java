package com.example.dikti.Informasi;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.SplashScreen;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdapterInformasi extends FirestoreRecyclerAdapter<VariabelInformasi, AdapterInformasi.ViewHolder> {

    BottomSheetDialog dialog;
    private final Context context;


    public AdapterInformasi(@NonNull FirestoreRecyclerOptions<VariabelInformasi> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull VariabelInformasi variabelInformasi) {
        final FragmentManager fragmentManager = ((AppCompatActivity) viewHolder.itemView.getContext()).getSupportFragmentManager();
        viewHolder.judul.setText(variabelInformasi.getJudul());
        viewHolder.isiInformasi.setText(variabelInformasi.getInfo());
        final String key = getSnapshots().getSnapshot(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUp(key,viewHolder);
            }
        });
    }

    private void PopUp(final String Time, final ViewHolder viewHolder){
        Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().document("Informasi/" + Time).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                dialog =new BottomSheetDialog(context);
                dialog.setContentView(R.layout.pop_up_informasi);
                TextView judulInformasi = dialog.findViewById(R.id.judul_informasi);
                TextView isiDeskripsi = dialog.findViewById(R.id.isi_informasi);
                TextView link = dialog.findViewById(R.id.link_informasi);
                ImageView edit = dialog.findViewById(R.id.edit_informasi);
                edit.setVisibility(View.INVISIBLE);
                judulInformasi.setText(documentSnapshot.getString("judul"));
                isiDeskripsi.setText(documentSnapshot.getString("info"));
                link.setText(documentSnapshot.getString("link"));
                if (Preference.getDataAs(viewHolder.itemView.getContext()).equals("Admin")){
                    edit.setVisibility(View.VISIBLE);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putString("1",Time);
                            SplashScreen activity = unwrap(view.getContext());
                            TambahInformasi tambahInformasi = new TambahInformasi();
                            tambahInformasi.setArguments(bundle);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.contain_all,tambahInformasi).addToBackStack(null).commit();
                            dialog.dismiss();
                        }
                    });
                }
                if (documentSnapshot.getString("link").equals("-")){
                    link.setVisibility(View.GONE);
                }else {
                    link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentSnapshot.getString("link")));
                            context.startActivity(intent);
                        }
                    });
                }
                dialog.show();
            }

        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampilan_informasi,parent,false);
        return new ViewHolder(view);
    }

    private static SplashScreen unwrap(Context context) {
        while (!(context instanceof SplashScreen) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (SplashScreen) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView judul;
        final TextView isiInformasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            isiInformasi = itemView.findViewById(R.id.informasi);
        }

    }
}
