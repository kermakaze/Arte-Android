package com.teamarte.arte.ui.createprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.ApiService;
import com.teamarte.arte.api.ApiServiceProvider;
import com.teamarte.arte.api.data.request.RegisterRequest;
import com.teamarte.arte.api.data.response.AuthResponse;
import com.teamarte.arte.databinding.ActivityCreateProfileBinding;
import com.teamarte.arte.ui.MainActivity;
import com.teamarte.arte.ui.home.UploadArtActivity;
import com.teamarte.arte.util.PrefManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CreateProfileActivity extends AppCompatActivity {

    private ActivityCreateProfileBinding mBinding;
    private ProgressDialog mProgressDialog;
    private String photoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_profile);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create an account");

        mBinding.chooseImageLayout.setOnClickListener(this::onChooseImageLayoutClicked);
        mBinding.createAccountButton.setOnClickListener(this::onCreateAccountButtonClicked);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

    }

    private void onChooseImageLayoutClicked(View  view){
        ImagePicker.Companion.with(this)
                //Crop image(Optional), Check Customization for more option
                //Final image resolution will be less than 1080 x 1080(Optional)
                .galleryOnly()
                .start();
    }

    private void onCreateAccountButtonClicked(View view){

        if(mBinding.avatarImageView.getDrawable() == null){
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = mBinding.usernameEditText.getText().toString().trim();

        if(username.isEmpty()){
            mBinding.usernameEditText.requestFocus();
            Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.show();
        mProgressDialog.setMessage("Uploading 0%");

        String requestId = MediaManager.get().upload(photoPath)
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
                        Toast.makeText(CreateProfileActivity.this, "Finished uploading", Toast.LENGTH_SHORT).show();

                        String imageUrl = (String) resultData.get("secure_url");
                        createProfile(new RegisterRequest(username,getIntent().getStringExtra(AppConstants.GOOGLE_ID_KEY),imageUrl));

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();
    }


    private  void createProfile(RegisterRequest registerRequest){
        mProgressDialog.setMessage("Loading.....");
        ApiServiceProvider
                .getApiService()
                .register(registerRequest)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(CreateProfileActivity.this,
                                    "Success", Toast.LENGTH_SHORT)
                                    .show();

                            Timber.d(response.body().getId());

                            PrefManager.getInstance(CreateProfileActivity.this)
                                    .saveUserId(response.body().getId());

                            Intent i = new Intent(CreateProfileActivity.this, MainActivity.class);
                            startActivity(i);

                        }
                        else {
                            Toast.makeText(CreateProfileActivity.this, "Username must be unique", Toast.LENGTH_SHORT).show();
                        }

                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        mProgressDialog.dismiss();
                        Toast.makeText(CreateProfileActivity.this, "Fatal Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri =  data.getData();

            photoPath = fileUri.getPath();

            Glide.with(this)
                    .load(fileUri.getPath())
                    .into(mBinding.avatarImageView);
        }
    }
}
