package com.teamarte.arte.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.data.response.ArtResponse;
import com.teamarte.arte.api.data.response.ArtSoldResponse;
import com.teamarte.arte.databinding.AdapterArtSoldItemBinding;
import com.teamarte.arte.databinding.AdapterExploreArtItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ArtSoldAdapter extends RecyclerView.Adapter<ArtSoldAdapter.ViewHolder> {
    private Context mContext;
    private List<ArtSoldResponse> artSoldResponseList;



    public ArtSoldAdapter(Context context) {
        this.mContext = context;
        this.artSoldResponseList = new ArrayList<>();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_art_sold_item, parent, false);

        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArtSoldResponse artSoldResponse = artSoldResponseList.get(position);





        Glide.with(mContext)
                .load(artSoldResponse.getArtId().getFullResUrl())
                .placeholder(R.color.placeholder_color)
                .into(holder.binding.coverImageView);

        holder.binding.priceTextView.setText("¢ "+artSoldResponse.getArtId().getPrice());





    }

    @Override
    public int getItemCount() {
        return artSoldResponseList.size();
    }


    public void setArtList(List<ArtSoldResponse> artList) {
        this.artSoldResponseList = artList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AdapterArtSoldItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = AdapterArtSoldItemBinding.bind(itemView);
        }
    }
}


