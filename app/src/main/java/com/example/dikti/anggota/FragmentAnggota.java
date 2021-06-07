package com.example.dikti.anggota;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.dikti.Preference;
import com.example.dikti.R;
import com.example.dikti.SplashScreen;
import com.example.dikti.anggota.petaAnggota.PetaAnggota;
import com.example.dikti.anggota.tambahAnggota.FragmentTambahAnggota;
import com.example.dikti.banksoal.Fragment_Home_Bank_Soal;
import com.example.dikti.fragment_Home;
import com.example.dikti.login.FragmentLogin;
import com.example.dikti.lombaBeasiswa.lomba.AdapterLomba;
import com.example.dikti.lombaBeasiswa.lomba.fragment_lomba;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FragmentAnggota extends Fragment implements View.OnClickListener {

    private RecyclerView namaAnggota;
    private FirebaseFirestore firebaseFirestore;
    private TextView daftarDepartemen,daftarAngkatan;
    private View filterDepartemen,filterAngkatan,fillterLimit;
    private AdapterAnggota adapterAnggota;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_anggota,container,false);

        if (Preference.getDataUsername(getContext()).isEmpty()) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentLogin fragmentLogin = new FragmentLogin();
            fragmentManager.beginTransaction().replace(R.id.contain_all, fragmentLogin).commit();
        }

        filterDepartemen = view.findViewById(R.id.filter_departemen);
        filterAngkatan = view.findViewById(R.id.filter_angkatan);
        daftarDepartemen = view.findViewById(R.id.daftarDepartemen);
        daftarAngkatan = view.findViewById(R.id.daftar_angkatan);
        SearchView searchView = view.findViewById(R.id.search);
        namaAnggota = view.findViewById(R.id.daftaranggota);
        View tambahAnggota = view.findViewById(R.id.tambah_anggota);
        View toolbar = view.findViewById(R.id.toolbar);
        View anggota = toolbar.findViewById(R.id.anggota);
        View banksoal = toolbar.findViewById(R.id.bank_soal);
        ImageView tombolAnggota = toolbar.findViewById(R.id.simbol_anggota);
        TextView textAnggota = toolbar.findViewById(R.id.text_anggota);
        View lomba = toolbar.findViewById(R.id.lomba);
        View beasiswa = toolbar.findViewById(R.id.home1);
        ImageView petaAnggota = view.findViewById(R.id.peta_anggota);
        fillterLimit = view.findViewById(R.id.filter_limit);

        namaAnggota.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        firebaseFirestore = FirebaseFirestore.getInstance();
        filterDepartemen.setOnClickListener(this);
        filterAngkatan.setOnClickListener(this);
        fillterLimit.setOnClickListener(this);
        tambahAnggota.setOnClickListener(this);
        tambahAnggota.setVisibility(View.GONE);

        tombolAnggota.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_group_blue));
        textAnggota.setTextColor(ContextCompat.getColor(mContext,R.color.deepSkyBlue));

        if (Preference.getDataAs(getContext()).equals("Admin")){
            tambahAnggota.setVisibility(View.VISIBLE);
        }

        cekKondisiAngkatan();
        cekKondisiDepartemen();
        cekKondisiAll();

        if (!Preference.getFilterDepartemen(getContext()).isEmpty() || !Preference.getFilterAngkatan(mContext).isEmpty()){
            if (!Preference.getFilterAngkatan(getContext()).isEmpty() && !Preference.getFilterDepartemen(getContext()).isEmpty()){
                SearchRecyler("querycampuran",Preference.getFilterAngkatan(mContext)+" "+Preference.getFilterDepartemen(getContext()));
            } else if (Preference.getFilterDepartemen(getContext()).isEmpty()){
                SearchRecyler("queryangkatan",Preference.getFilterAngkatan(mContext));
            } else if (Preference.getFilterAngkatan(getContext()).isEmpty()){
                SearchRecyler("querydepartemen",Preference.getFilterDepartemen(mContext));
            }
        }else {
            RecleylerAwal();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               QueryAnggotaRecyler(s.toLowerCase());
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                QueryAnggotaRecyler(s.toLowerCase());
                return false;
            }
        });

        anggota.setOnClickListener(this);
        banksoal.setOnClickListener(this);
        lomba.setOnClickListener(this);
        beasiswa.setOnClickListener(this);
        petaAnggota.setOnClickListener(this);

        return view;
    }



    public void cekKondisiAngkatan(){
        if (!Preference.getFilterAngkatan(getContext()).isEmpty() && !Preference.getFilterAngkatan(getContext()).equals("All")){
            GantiWarna(daftarAngkatan,filterAngkatan,Color.WHITE,R.drawable.border_biru,Preference.getFilterAngkatan(getContext()));
        }else {
            GantiWarna(daftarAngkatan,filterAngkatan,Color.GRAY,R.drawable.border,"Angkatan : All");
        }
    }

    public void cekKondisiDepartemen(){
        if (!Preference.getFilterDepartemen(getContext()).isEmpty() && !Preference.getFilterDepartemen(getContext()).equals("All")){
            GantiWarna(daftarDepartemen,filterDepartemen,Color.WHITE,R.drawable.border_biru,Preference.getFilterDepartemen(getContext()));
        }else {
            GantiWarna(daftarDepartemen,filterDepartemen,Color.GRAY,R.drawable.border,"Departemen : All");
        }
    }

    public void GantiWarna(TextView filer,View filterView,int warna,int border,String nama){
        filer.setText(nama);
        filer.setTextColor(warna);

        Drawable drawable1 = getResources().getDrawable(border);
        filterView.setBackground(drawable1);

        int padding_in_dp = 7;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        filterView.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
    }

    public void cekKondisiAll(){
        if (Preference.getFilterDepartemen(getContext()).equals("All")){
            Preference.clearDepartemen(getContext());
        }
        if (Preference.getFilterAngkatan(getContext()).equals("All")){
            Preference.clearAngkatan(getContext());
        }
    }

    private void PindahFragment(){
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                cekKondisiAngkatan();
                cekKondisiDepartemen();
                cekKondisiAll();
                QueryAnggotaRecyler("");
            }
        }, false);
    }

    private void QueryAnggotaRecyler(String s){
        if (!Preference.getFilterDepartemen(getContext()).isEmpty() || !Preference.getFilterAngkatan(mContext).isEmpty()){
            if (!Preference.getFilterAngkatan(getContext()).isEmpty() && !Preference.getFilterDepartemen(getContext()).isEmpty()){
                SearchRecyler("querycampuran",Preference.getFilterAngkatan(mContext)+" "+Preference.getFilterDepartemen(getContext())+" "+s.toLowerCase());
            } else if (Preference.getFilterDepartemen(getContext()).isEmpty()){
                SearchRecyler("queryangkatan",Preference.getFilterAngkatan(mContext)+" "+s.toLowerCase());
            } else if (Preference.getFilterAngkatan(getContext()).isEmpty()){
                SearchRecyler("querydepartemen",Preference.getFilterDepartemen(mContext)+" "+s.toLowerCase());
            }
        }else {
            SearchRecyler("queryNama",s.toLowerCase());
        }
    }

    private void SearchRecyler(String orderBy,String request){

        FirestoreRecyclerOptions<VariabelAnggota> options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy(orderBy).startAt(request).endAt(request+"\ufaff").limit(50),VariabelAnggota.class)
                .build();

        adapterAnggota = new AdapterAnggota(options);
        adapterAnggota.startListening();
        namaAnggota.setAdapter(adapterAnggota);
    }

    private void RecleylerAwal(){
        FirestoreRecyclerOptions<VariabelAnggota> options = new FirestoreRecyclerOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy("angkatan", Query.Direction.DESCENDING).limit(50),VariabelAnggota.class)
                .build();

        adapterAnggota = new AdapterAnggota(options);
        adapterAnggota.startListening();
        namaAnggota.setAdapter(adapterAnggota);
    }

    /*
    private void QueryAnggota(String s){
        if (!Preference.getFilterDepartemen(getContext()).isEmpty() || !Preference.getFilterAngkatan(mContext).isEmpty()){
            if (!Preference.getFilterAngkatan(getContext()).isEmpty() && !Preference.getFilterDepartemen(getContext()).isEmpty()){
                SearchPaging("querycampuran",Preference.getFilterAngkatan(mContext)+" "+Preference.getFilterDepartemen(getContext())+" "+s.toLowerCase());
            } else if (Preference.getFilterDepartemen(getContext()).isEmpty()){
                SearchPaging("queryangkatan",Preference.getFilterAngkatan(mContext)+" "+s.toLowerCase());
            } else if (Preference.getFilterAngkatan(getContext()).isEmpty()){
                SearchPaging("querydepartemen",Preference.getFilterDepartemen(mContext)+" "+s.toLowerCase());
            }
        }else {
            SearchPaging("queryNama",s.toLowerCase());
        }
    }

    private void PagingAwal(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(2)
                .build();

        FirestorePagingOptions<VariabelAnggota> options = new FirestorePagingOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy("angkatan", Query.Direction.DESCENDING).limit(2),config,VariabelAnggota.class)
                .build();

        FirestorePagingAdapterIsi(options);
    }

    private void SearchPaging(String orderBy,String request){
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(2)
                .build();

        FirestorePagingOptions<VariabelAnggota> options = new FirestorePagingOptions.Builder<VariabelAnggota>()
                .setQuery(firebaseFirestore.collection("Anggota").orderBy(orderBy).startAt(request).endAt(request+"\ufaff").limit(2),config,VariabelAnggota.class)
                .build();

        FirestorePagingAdapterIsi(options);
    }

    private void FirestorePagingAdapterIsi(FirestorePagingOptions options){
        FirestorePagingAdapter firestorePagingAdapter = new FirestorePagingAdapter<VariabelAnggota,ViewHolderAnggota>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnggota holder, int i, @NonNull final VariabelAnggota variabelAnggota) {
                Glide.with(holder.fotoAnggota.getContext())
                        .load(variabelAnggota.getFoto())
                        .placeholder(R.drawable.logo_dikti)
                        .into(holder.fotoAnggota);
                holder.namaAnggota.setText(variabelAnggota.getNamaLengkap());
                holder.namaDepartemen.setText(variabelAnggota.getDepartemen());
                holder.angkatan.setText(variabelAnggota.getAngkatan());
                if (variabelAnggota.getNamaLengkap().length()<18){
                    holder.point.setText("");
                }else {
                    holder.point.setText("...");
                }
                int pos = holder.getAdapterPosition();
                final DocumentSnapshot key = getItem(pos);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        variabelAnggota.setKey(key.getId());
                        Bundle bundle = new Bundle();
                        bundle.putString("1",variabelAnggota.getKey());
                        SplashScreen activity = (SplashScreen) view.getContext();
                        Fragment_Detail_Anggota fragment_detail_anggota = new Fragment_Detail_Anggota();
                        fragment_detail_anggota.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.contain_all,fragment_detail_anggota).commit();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderAnggota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menampilkan_anggota,parent,false);
                return new ViewHolderAnggota(view);
            }
        };

        firestorePagingAdapter.startListening();
        namaAnggota.setLayoutManager(new LinearLayoutManager(getContext()));
        namaAnggota.setAdapter(firestorePagingAdapter);
    }

    class ViewHolderAnggota extends RecyclerView.ViewHolder{
        final ImageView fotoAnggota;
        final TextView namaAnggota;
        final TextView namaDepartemen;
        final TextView angkatan;
        final TextView point;

        public ViewHolderAnggota(@NonNull View itemView) {
            super(itemView);
            fotoAnggota = itemView.findViewById(R.id.foto_anggota);
            namaAnggota = itemView.findViewById(R.id.nama_anggota);
            namaDepartemen = itemView.findViewById(R.id.nama_departemen);
            angkatan = itemView.findViewById(R.id.angkatan);
            point = itemView.findViewById(R.id.point);
        }
    }
    */

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (view.getId()==R.id.filter_departemen){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Departemen");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterDepartemen");
            PindahFragment();

        }else if (view.getId()==R.id.filter_angkatan){
            PopBottomDepartemen popBottomDepartemen = new PopBottomDepartemen().dapet("Angkatan");
            popBottomDepartemen.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"FilterAngkatan");
            PindahFragment();

        }else if (view.getId()==R.id.tambah_anggota){
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentTambahAnggota()).commit();
        }
        else if (view.getId() == R.id.anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new FragmentAnggota()).commit();
        } else if (view.getId() == R.id.bank_soal) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new Fragment_Home_Bank_Soal()).commit();
        }else if (view.getId() == R.id.lomba) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_lomba()).commit();
        }else if (view.getId() == R.id.home1) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new fragment_Home()).commit();
        }else if (view.getId() == R.id.peta_anggota) {
            fragmentManager.beginTransaction().replace(R.id.contain_all, new PetaAnggota()).commit();
        }else if (view.getId() == R.id.filter_limit) {
            Toast.makeText(getContext(),"Maaf ,karena masih dalam tahap uji coba maka jumlah maksimal data yang dapat ditampilkan adalah 50 data",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
