package com.example.dikti.lombaBeasiswa.lomba.requestTim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.lombaBeasiswa.lomba.FragmentDetailLomba;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdapterRequestLomba extends FirestoreRecyclerAdapter<VariabelRequestLomba, AdapterRequestLomba.ViewHolder> {

    String namaPeminat,foto,angkatan;


    public AdapterRequestLomba(@NonNull FirestoreRecyclerOptions<VariabelRequestLomba> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull final VariabelRequestLomba variabelRequestLomba) {
        Glide.with(viewHolder.gambarLomba.getContext())
                .load(variabelRequestLomba.getFoto())
                .into(viewHolder.gambarLomba);
        viewHolder.jumlahAnggota.setText(Long.toString(variabelRequestLomba.getJumlahAnggota()));
        viewHolder.deadline.setText(variabelRequestLomba.getDeadlineTanggal() +" "+variabelRequestLomba.getDeadlineBulan()+" "+ variabelRequestLomba.getDeadlineTahun());
        viewHolder.isiJenisLomba.setText(variabelRequestLomba.getJenisLomba());
        viewHolder.namaLomba.setText(variabelRequestLomba.getNamaLomba());
        viewHolder.syaratAnggota.setText(variabelRequestLomba.getSyaratAnggota());
        viewHolder.namaPengirim.setText(variabelRequestLomba.getNamaPengirim());
        viewHolder.angkatan.setText(variabelRequestLomba.getAngkatan());

        final String idLomba = variabelRequestLomba.getIdLomba();

        viewHolder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!viewHolder.meluas){
                    viewHolder.meluas = true;
                    viewHolder.dropdown.setImageResource(R.drawable.ic_baseline_arrow_drop_up_blue);
                    viewHolder.isiLomba.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                }
                else {
                    viewHolder.meluas=false;
                    float scale = view.getContext().getResources().getDisplayMetrics().density;
                    int pixels = (int) (145 * scale + 0.5f);
                    viewHolder.dropdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_blue);
                    viewHolder.isiLomba.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pixels));
                }
            }
        });

        final FragmentManager fragmentManager = ((AppCompatActivity) viewHolder.itemView.getContext()).getSupportFragmentManager();

        viewHolder.cancel.setVisibility(View.GONE);
        viewHolder.pilihan1.setVisibility(View.GONE);
        viewHolder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentDetailLomba fragmentDetailLomba = new FragmentDetailLomba();
                Bundle bundle = new Bundle();
                bundle.putString("1",idLomba);
                fragmentDetailLomba.setArguments(bundle);
                fragmentManager.beginTransaction().add(R.id.contain_all,fragmentDetailLomba,"FRAGMENT_LOMBA").commit();
            }
        });

        Task<DocumentSnapshot> documentReference = FirebaseFirestore.getInstance().collection("Request Tim").document(variabelRequestLomba.getNamaLomba()+" "+variabelRequestLomba.getNamaPengirim())
                .collection("Peminat").document(Preference.getDataUsername(viewHolder.itemView.getContext())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            viewHolder.pilihan1.setVisibility(View.INVISIBLE);
                            viewHolder.cancel.setVisibility(View.VISIBLE);
                            viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Request Tim").document(variabelRequestLomba.getNamaLomba()+" "+variabelRequestLomba.getNamaPengirim())
                                            .collection("Peminat").document(Preference.getDataUsername(viewHolder.itemView.getContext()));
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            viewHolder.pilihan1.setVisibility(View.VISIBLE);
                                            viewHolder.cancel.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });
                        }
                        else {
                            viewHolder.pilihan1.setVisibility(View.VISIBLE);
                        }
                    }
                });



        if (!Preference.getDataUsername(viewHolder.itemView.getContext()).equals(variabelRequestLomba.getIdPengirim())){
            viewHolder.pilihan1.setVisibility(View.VISIBLE);
            viewHolder.edit.setVisibility(View.GONE);
            viewHolder.bergabung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Request Tim")
                            .document(variabelRequestLomba.getNamaLomba()+" "+variabelRequestLomba.getNamaPengirim())
                            .collection("Peminat")
                            .document(Preference.getDataUsername(view.getContext()));

                    Task<DocumentSnapshot> documentReferencePeminat = FirebaseFirestore.getInstance().collection("Anggota")
                            .document(Preference.getDataUsername(view.getContext())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    namaPeminat = documentSnapshot.getString("namaLengkap");
                                    foto = documentSnapshot.getString("foto");
                                    angkatan = documentSnapshot.getString("angkatan");
                                    HashMap<String,Object> map =new HashMap<>();
                                    map.put("namaLengkap",namaPeminat);
                                    map.put("foto",foto);
                                    map.put("angkatan",angkatan);
                                    map.put("idPeminat",Preference.getDataUsername(view.getContext()));
                                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(viewHolder.itemView.getContext(),"Perminataan Berhasil dibuat",Toast.LENGTH_SHORT);
                                            viewHolder.cancel.setVisibility(View.VISIBLE);
                                            viewHolder.pilihan1.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
            });
        }
        else {
            viewHolder.pilihan1.setVisibility(View.VISIBLE);
            viewHolder.bergabung.setText("Daftar Peminat");
            viewHolder.bergabung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("1",variabelRequestLomba.getNamaLomba()+" "+variabelRequestLomba.getNamaPengirim());
                    FragmentPeminatLomba fragmentPeminatLomba = new FragmentPeminatLomba();
                    fragmentPeminatLomba.setArguments(bundle);
                    fragmentPeminatLomba.show(fragmentManager,"Daftar Peminat");
                }
            });

            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("1","1");
                    bundle.putString("2",variabelRequestLomba.getNamaLomba()+" "+variabelRequestLomba.getNamaPengirim());
                    RequestTim requestTim = new RequestTim();
                    requestTim.setArguments(bundle);
                    requestTim.show(fragmentManager,"Edit Request Tim");
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampilan_request_tim,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView gambarLomba,dropdown,edit,detail;
        TextView namaLomba,bergabung,jumlahAnggota,syaratAnggota,isiJenisLomba,namaPengirim,deadline,cancel,angkatan;
        View isiLomba,pilihan1;
        Boolean meluas = false;

           public ViewHolder(@NonNull View itemView) {
               super(itemView);
               gambarLomba=itemView.findViewById(R.id.gambarlomba);
               namaLomba = itemView.findViewById(R.id.nama_lomba);
               bergabung = itemView.findViewById(R.id.requestTim);
               deadline = itemView.findViewById(R.id.deadline);
               jumlahAnggota = itemView.findViewById(R.id.jumlah_anggota_text);
               syaratAnggota = itemView.findViewById(R.id.syarat_anggota_text);
               isiJenisLomba = itemView.findViewById(R.id.isi_jenis_lomba);
               dropdown = itemView.findViewById(R.id.dropdown_lomba);
               namaPengirim = itemView.findViewById(R.id.nama_pengirim);
               isiLomba = itemView.findViewById(R.id.isi_lomba);
               pilihan1 = itemView.findViewById(R.id.pilihan1);
               edit = itemView.findViewById(R.id.edit);
               cancel = itemView.findViewById(R.id.cancel);
               angkatan = itemView.findViewById(R.id.angkatan);
               detail = itemView.findViewById(R.id.detail);
           }
       }
}
