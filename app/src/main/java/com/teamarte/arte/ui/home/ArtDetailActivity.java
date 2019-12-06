package com.teamarte.arte.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.ApiServiceProvider;
import com.teamarte.arte.api.data.request.BuyArtRequest;
import com.teamarte.arte.api.data.response.ArtResponse;
import com.teamarte.arte.api.data.response.PaymentLinkResponse;
import com.teamarte.arte.databinding.ActivityArtDetailBinding;
import com.teamarte.arte.ui.PaymentActivity;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtDetailActivity extends AppCompatActivity {


    private ActivityArtDetailBinding mBinding;
    private ArtResponse artResponse;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_art_detail);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        artResponse = (ArtResponse) getIntent().getSerializableExtra(AppConstants.ART_MODEL_KEY);

        Glide.with(this)
                .load(artResponse.getFullResUrl())
                .placeholder(R.color.placeholder_color)
                .into(mBinding.coverImageView);


        mBinding.descriptionEditText.setText(artResponse.getDescription());
        mBinding.buyButton.setText("BUY ¢"+ artResponse.getPrice());

        mBinding.buyButton.setOnClickListener(this::onBuyButtonClicked);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);


    }


    public void onBuyButtonClicked(View view){

        mProgressDialog.setMessage("Loading......");
        mProgressDialog.show();

        ApiServiceProvider
                .getApiService()
                .buyArt(new BuyArtRequest(artResponse.getId()))
        .enqueue(new Callback<PaymentLinkResponse>() {
            @Override
            public void onResponse(Call<PaymentLinkResponse> call, Response<PaymentLinkResponse> response) {
                mProgressDialog.dismiss();

                if(!response.isSuccessful()){
                    Toast.makeText(ArtDetailActivity.this, "Error occurred", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                Intent i = new Intent(ArtDetailActivity.this, PaymentActivity.class);
                i.putExtra(AppConstants.PAYMENT_LINK_KEY, response.body().getLink());
                i.putExtra(AppConstants.IMAGE_URL_KEY, artResponse.getFullResUrl());
                i.putExtra(AppConstants.ART_MODEL_KEY, artResponse);

                startActivity(i );
            }

            @Override
            public void onFailure(Call<PaymentLinkResponse> call, Throwable t) {

                mProgressDialog.dismiss();
            }
        });


    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
