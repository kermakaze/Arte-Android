package com.teamarte.arte.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.teamarte.arte.databinding.FragmentExploreArtBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreArtFragment extends Fragment {

    private FragmentExploreArtBinding mBinding;
    ExploreArtAdapter exploreArtAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore_art, container, false);

        setHasOptionsMenu(true);
        List<ArtResponse> artResponseList = new ArrayList<>();




        exploreArtAdapter = new ExploreArtAdapter(getContext());
        exploreArtAdapter.setArtList(artResponseList);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mBinding.recyclerView.setAdapter(exploreArtAdapter);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(layoutManager);


        loadArtItems();



        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_art, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.uploadMenu:
                ImagePicker.Companion.with(this)
                        //Crop image(Optional), Check Customization for more option
                        //Final image resolution will be less than 1080 x 1080(Optional)
                        .galleryOnly()
                        .start();

                break;
        }
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri =  data.getData();

            Intent i = new Intent(getContext(), UploadArtActivity.class);
            i.putExtra(AppConstants.IMAGE_PATH_KEY, fileUri.getPath());
            startActivity(i);
        }
    }

    private void loadArtItems(){
        mBinding.progressbar.setVisibility(View.VISIBLE);
        ApiServiceProvider
                .getApiService()
                .getArtList()
                .enqueue(new Callback<List<ArtResponse>>() {
                    @Override
                    public void onResponse(Call<List<ArtResponse>> call, Response<List<ArtResponse>> response) {
                        exploreArtAdapter.setArtList(response.body());
                        mBinding.progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<ArtResponse>> call, Throwable t) {
                        mBinding.progressbar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Fatal error occured", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}