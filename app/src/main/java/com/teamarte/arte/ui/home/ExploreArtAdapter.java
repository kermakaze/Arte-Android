package com.teamarte.arte.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.data.response.ArtResponse;
import com.teamarte.arte.databinding.ActivityArtDetailBinding;
import com.teamarte.arte.databinding.AdapterExploreArtItemBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ExploreArtAdapter extends RecyclerView.Adapter<ExploreArtAdapter.ViewHolder> {
    private Context mContext;
    private List<ArtResponse> artList;



    public ExploreArtAdapter(Context context) {
        this.mContext = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_explore_art_item, parent, false);

        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArtResponse art = artList.get(position);



        String uri = "https://picsum.photos/seed/"+ System.currentTimeMillis() +"/500/900";

        Glide.with(mContext)
                .load(art.getFullResUrl())
                .placeholder(R.color.placeholder_color)
                .into(holder.binding.coverImageView);

        holder.binding.uploaderUsername.setText(art.getSellerId().getUsername());
        holder.binding.priceEditText.setText("¢" +art.getPrice());

        holder.itemView.setOnClickListener(view->{

            Intent i = new Intent(mContext,ArtDetailActivity.class);
            i.putExtra(AppConstants.ART_MODEL_KEY, art);
            mContext.startActivity(i);
        });



    }

    @Override
    public int getItemCount() {
        return artList.size();
    }


    public void setArtList(List<ArtResponse> artList) {
        this.artList = artList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AdapterExploreArtItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = AdapterExploreArtItemBinding.bind(itemView);
        }
    }
}


