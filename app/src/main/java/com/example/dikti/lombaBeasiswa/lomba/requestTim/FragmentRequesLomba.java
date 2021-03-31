package com.example.dikti.lombaBeasiswa.lomba.requestTim;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.lombaBeasiswa.lomba.PopBottomJenisLomba;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentRequesLomba extends Fragment {

    private AdapterRequestLomba adapterRequestLomba;
    private RecyclerView daftarRequest;
    private TextView jenisLomba, myRequest;
    private Boolean request =false;
    private FirestoreRecyclerOptions<VariabelRequestLomba> options;
    private FragmentManager fragmentManager = getFragmentManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_daftar_request, container, false);

        daftarRequest = view.findViewById(R.id.daftar_request);
        SearchView searchView = view.findViewById(R.id.search);
        jenisLomba = view.findViewById(R.id.jenis_lomba);
        myRequest = view.findViewById(R.id.filter_myRequest);

        ImageView kembali = view.findViewById(R.id.kembali);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contain_all,new fragment_lomba()).addToBackStack(null).commit();
            }
        });


        String idLomba = Objects.requireNonNull(getArguments()).getString("1");
        if (!idLomba.equals("kosong")){
            options = new FirestoreRecyclerOptions.Builder<VariabelRequestLomba>()
                    .setQuery(FirebaseFirestore.getInstance().collection("Request Tim").orderBy("namaLomba").startAt(idLomba).endAt(idLomba+"\ufaff"), VariabelRequestLomba.class)
                    .build();

            adapterRequestLomba = new AdapterRequestLomba(options);
            adapterRequestLomba.startListening();
            daftarRequest.setAdapter(adapterRequestLomba);
        }else {
            options = new FirestoreRecyclerOptions.Builder<VariabelRequestLomba>()
                    .setQuery(FirebaseFirestore.getInstance().collection("Request Tim").orderBy("deadline"), VariabelRequestLomba.class)
                    .build();

            adapterRequestLomba = new AdapterRequestLomba(options);
            adapterRequestLomba.startListening();
            daftarRequest.setAdapter(adapterRequestLomba);
        }

        jenisLomba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopBottomJenisLomba popBottomJenisLomba = new PopBottomJenisLomba();
                popBottomJenisLomba.show((Objects.requireNonNull(getFragmentManager())), "FilterJenisLomba");
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                final FragmentManager fragmentManager1 = getFragmentManager();
                fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        super.onFragmentViewDestroyed(fm, f);
                        fragmentManager1.unregisterFragmentLifecycleCallbacks(this);
                        if (Preference.getDataJenisLomba(getContext()).equals("All") || Preference.getDataJenisLomba(getContext()).isEmpty()){
                            GantiPutih(jenisLomba,"Jenis Lomba : All");
                            if (!request){
                                options = new FirestoreRecyclerOptions.Builder<VariabelRequestLomba>()
                                        .setQuery(FirebaseFirestore.getInstance().collection("Request Tim").orderBy("deadline"), VariabelRequestLomba.class)
                                        .build();
                            }else {
                                GantiRecyler("queryMyRequest",Preference.getDataUsername(getContext()));
                            }
                        }else {
                            GantiBiru(jenisLomba,Preference.getDataJenisLomba(getContext()));
                            if (!request){
                                GantiRecyler("jenisLomba",Preference.getDataJenisLomba(getContext()));
                            }else {
                                GantiRecyler("queryJenisMyRequest",Preference.getDataJenisLomba(getContext())+" "+Preference.getDataUsername(getContext()));
                            }
                        }
                    }
                }, false);
            }
        });

        myRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!request){
                    request = true;
                    GantiBiru(myRequest,"My Request");
                    if (Preference.getDataJenisLomba(getContext()).equals("All") || !Preference.getDataJenisLomba(getContext()).isEmpty()) {
                        GantiRecyler("queryMyRequest",Preference.getDataUsername(getContext()));
                    }else {
                        GantiRecyler("queryJenisMyRequest",Preference.getDataJenisLomba(getContext())+" "+Preference.getDataUsername(getContext()));
                    }
                }
                else {
                    request = false;
                    GantiPutih(myRequest,"My Request");
                    if (Preference.getDataJenisLomba(getContext()).equals("All") || !Preference.getDataJenisLomba(getContext()).isEmpty()) {
                        options = new FirestoreRecyclerOptions.Builder<VariabelRequestLomba>()
                                .setQuery(FirebaseFirestore.getInstance().collection("Request Tim").orderBy("deadline"), VariabelRequestLomba.class)
                                .build();
                        adapterRequestLomba = new AdapterRequestLomba(options);
                        adapterRequestLomba.startListening();
                        daftarRequest.setAdapter(adapterRequestLomba);
                    }else {
                        GantiRecyler("queryJenisLomba",Preference.getDataJenisLomba(getContext()));
                    }
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (Preference.getDataJenisLomba(getContext()).equals("All") || Preference.getDataJenisLomba(getContext()).isEmpty()){
                    if (!request){
                        GantiRecyler("queryNamaLomba",s);
                    }else {
                        GantiRecyler("queryMyRequest",Preference.getDataUsername(getContext())+" "+s);
                    }
                }else {
                    if (!request){
                        GantiRecyler("queryJenisLomba",Preference.getDataJenisLomba(getContext())+" "+s);
                    }else {
                        GantiRecyler("queryJenisMyRequest",Preference.getDataJenisLomba(getContext())+" "+Preference.getDataUsername(getContext())+" "+s);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (Preference.getDataJenisLomba(getContext()).equals("All") || Preference.getDataJenisLomba(getContext()).isEmpty()){
                    if (!request){
                        GantiRecyler("queryNamaLomba",s);
                    }else {
                        GantiRecyler("queryMyRequest",Preference.getDataUsername(getContext())+" "+s);
                    }
                }else {
                    if (!request){
                        GantiRecyler("queryJenisLomba",Preference.getDataJenisLomba(getContext())+" "+s);
                    }else {
                        GantiRecyler("queryJenisMyRequest",Preference.getDataJenisLomba(getContext())+" "+Preference.getDataUsername(getContext())+" "+s);
                    }
                }
                return false;
            }
        });

        return view;
    }

    private  void GantiBiru(TextView textView,String string){
        textView.setText(string);
        textView.setTextColor(Color.WHITE);
        textView.setBackground(getResources().getDrawable(R.drawable.border_biru));
        int padding_in_dp_top_bottom = 7;
        int padding_in_dp_left_right = 13;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px_top_bottom = (int) (padding_in_dp_top_bottom * scale + 0.5f);
        int padding_in_px_left_right = (int) (padding_in_dp_left_right * scale + 0.5f);
        textView.setPadding(padding_in_px_left_right,padding_in_px_top_bottom,padding_in_px_left_right,padding_in_px_top_bottom);
    }

    private  void GantiPutih(TextView textView,String string){
        textView.setText(string);
        textView.setTextColor(Color.GRAY);
        textView.setBackground(getResources().getDrawable(R.drawable.border));
        int padding_in_dp_top_bottom = 7;
        int padding_in_dp_left_right = 13;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px_top_bottom = (int) (padding_in_dp_top_bottom * scale + 0.5f);
        int padding_in_px_left_right = (int) (padding_in_dp_left_right * scale + 0.5f);
        textView.setPadding(padding_in_px_left_right,padding_in_px_top_bottom,padding_in_px_left_right,padding_in_px_top_bottom);
    }

    private void GantiRecyler(String order,String request){
        options = new FirestoreRecyclerOptions.Builder<VariabelRequestLomba>()
                .setQuery(FirebaseFirestore.getInstance().collection("Request Tim").orderBy(order).startAt(request).endAt(request + "\ufaff"), VariabelRequestLomba.class)
                .build();

        adapterRequestLomba = new AdapterRequestLomba(options);
        adapterRequestLomba.startListening();
        daftarRequest.setAdapter(adapterRequestLomba);
    }
}

