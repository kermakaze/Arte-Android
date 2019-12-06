package com.teamarte.arte.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.ApiServiceProvider;
import com.teamarte.arte.api.data.request.CreateArtRequest;
import com.teamarte.arte.api.data.response.GenericResponse;
import com.teamarte.arte.databinding.ActivityUploadArtBinding;

import java.util.Map;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UploadArtActivity extends AppCompatActivity {

    private ActivityUploadArtBinding mBinding;

    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_upload_art);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Upload Art");

        mBinding.postArtButton.setOnClickListener(this::onUploadArtButtonClicked);

        mProgressDialog = new ProgressDialog(this);


        Glide.with(this)
                .load(getIntent().getStringExtra(AppConstants.IMAGE_PATH_KEY))
                .into(mBinding.artImageView);
    }


    private void onUploadArtButtonClicked(View view){

        if(mBinding.priceEditText.getText().toString().isEmpty()){
            Toast.makeText(this, "Put in a price 0 to indicate free", Toast.LENGTH_SHORT).show();
            mBinding.priceEditText.requestFocus();
            return;
        }

        if(mBinding.descriptionEditText.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
            mBinding.descriptionEditText.requestFocus();
            return;
        }
        mProgressDialog.setMessage("Uploading 0%");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        String requestId = MediaManager.get().upload(getIntent().getStringExtra(AppConstants.IMAGE_PATH_KEY))
                .unsigned("xgggkatp")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        mProgressDialog.show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        System.out.println(bytes +"/"+ totalBytes );

                        int percent =(int) (bytes/totalBytes) * 100;
                        mProgressDialog.setMessage("Uploading "+percent+"%");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        Timber.d(resultData.toString());


                        String url = (String) resultData.get("secure_url");
                        double price = Double.parseDouble(mBinding.priceEditText.getText().toString());

                        String  description = mBinding.descriptionEditText.getText().toString();
                        uploadArtWork(new CreateArtRequest(url,price, description));

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Timber.e(error.toString());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();


    }

    private void uploadArtWork(CreateArtRequest createArtRequest){
        mProgressDialog.setMessage("Loading.....");
        ApiServiceProvider.getApiService()
                .createArtRequest(createArtRequest)
                .enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {

                        mProgressDialog.dismiss();

                        Toast.makeText(UploadArtActivity.this, "Art Uploaded", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {

                        mProgressDialog.dismiss();
                    }
                });

    }
}
