package com.example.dikti.banner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dikti.R;
import com.example.dikti.SplashScreen;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterBanner extends FirestoreRecyclerAdapter<VariabelBanner, AdapterBanner.ViewHolder> {

    public AdapterBanner(@NonNull FirestoreRecyclerOptions<VariabelBanner> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull VariabelBanner variabelBanner) {
        Glide.with(viewHolder.imageView.getContext())
                .load(variabelBanner.getFotoBanner())
                .into(viewHolder.imageView);
        final String key = getSnapshots().getSnapshot(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle kirim = new Bundle();
                kirim.putString("1",key);
                SplashScreen activity = (SplashScreen) view.getContext();
                FragmentDetailBanner fragmentDetailBanner = new FragmentDetailBanner();
                fragmentDetailBanner.setArguments(kirim);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contain_all,fragmentDetailBanner).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gambarbanner);
        }
    }

}


