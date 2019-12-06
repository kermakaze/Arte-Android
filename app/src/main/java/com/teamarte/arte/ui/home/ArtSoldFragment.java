package com.teamarte.arte.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.ApiServiceProvider;
import com.teamarte.arte.api.data.response.ArtResponse;
import com.teamarte.arte.api.data.response.ArtSoldResponse;
import com.teamarte.arte.databinding.FragmentExploreArtBinding;
import com.teamarte.arte.util.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArtSoldFragment extends Fragment {

    private FragmentExploreArtBinding mBinding;

    ArtSoldAdapter artSoldAdapter;



    public ArtSoldFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore_art, container, false);

        setHasOptionsMenu(true);





        artSoldAdapter = new ArtSoldAdapter(getContext());


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mBinding.recyclerView.setAdapter(artSoldAdapter);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(layoutManager);


        loadArtSoldItems();



        return mBinding.getRoot();
    }


    private void loadArtSoldItems(){
        mBinding.progressbar.setVisibility(View.VISIBLE);
        ApiServiceProvider
                .getApiService()
                .getMyArtSold()
                .enqueue(new Callback<List<ArtSoldResponse>>() {
                    @Override
                    public void onResponse(Call<List<ArtSoldResponse>> call, Response<List<ArtSoldResponse>> response) {
                        List<ArtSoldResponse> artSoldResponseList = new ArrayList<>();
                        if(response.isSuccessful()){
                            for(ArtSoldResponse r: response.body()){
                                if(r.getArtId().getSellerId().equals(PrefManager.getInstance(getContext()).getUserId())){
                                    artSoldResponseList.add(r);
                                }
                            }
                            artSoldAdapter.setArtList(artSoldResponseList);
                            if(artSoldResponseList.size() == 0){
                                Toast.makeText(getContext(),"No art sold", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"Error", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        mBinding.progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<ArtSoldResponse>> call, Throwable t) {
                        mBinding.progressbar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Fatal error occured", Toast.LENGTH_SHORT).show();

                    }
                });
    }



}
