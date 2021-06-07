package com.example.dikti.banksoal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterBankSoal extends FirestoreRecyclerAdapter<VariabelBankSoal, AdapterBankSoal.ViewHolder> {

    public AdapterBankSoal(@NonNull FirestoreRecyclerOptions<VariabelBankSoal> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_bank_soal,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final VariabelBankSoal variabelBankSoal) {
        viewHolder.mataKuliah.setText(variabelBankSoal.getMataKuliah());
        final String key = getSnapshots().getSnapshot(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle kirim = new Bundle();
                kirim.putString("2",key);
                kirim.putString("3",variabelBankSoal.getMataKuliah());
                kirim.putString("1",variabelBankSoal.getSemester());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragement_detail_matkul fragement_detail_matkul = new Fragement_detail_matkul();
                fragement_detail_matkul.setArguments(kirim);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.contain_all,fragement_detail_matkul,"FRAGMENT_BANK_SOAL").commit();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView mataKuliah;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mataKuliah = itemView.findViewById(R.id.matakuliah);
        }
    }

}
