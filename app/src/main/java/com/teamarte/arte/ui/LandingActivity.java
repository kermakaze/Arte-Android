package com.teamarte.arte.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.ApiServiceProvider;
import com.teamarte.arte.api.data.request.LoginRequest;
import com.teamarte.arte.api.data.response.AuthResponse;
import com.teamarte.arte.databinding.ActivityLandingBinding;
import com.teamarte.arte.ui.createprofile.CreateProfileActivity;
import com.teamarte.arte.util.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LandingActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =102 ;
    private ActivityLandingBinding mBinding;

    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_landing);


        mBinding.googleButton.setOnClickListener(this::onGoogleButtonClicked);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading.....");
    }

    private void onGoogleButtonClicked(View view){

        //Todo Implement server checking here

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                //.requestIdToken(getString(R.string.default_web_client_id))
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);


        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Timber.d("googleId: "+  account.getId());

            // Signed in successfully, show authenticated UI.

            authWithServer(account.getId());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.e( "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void authWithServer(String googleId){
         mProgressDialog.show();
        ApiServiceProvider
                .getApiService()
                .login(new LoginRequest(googleId))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                        mProgressDialog.dismiss();
                        if(response.isSuccessful()){
                            //Account exists just login

                            PrefManager.getInstance(LandingActivity.this)
                                    .saveUserId(response.body().getId());
                            startActivity(new Intent(LandingActivity.this, MainActivity.class));
                        }
                        else if(response.code() == 404){
                            //Account does not exist create it
                            Intent i = new Intent(LandingActivity.this, CreateProfileActivity.class);
                            i.putExtra(AppConstants.GOOGLE_ID_KEY, googleId);
                            startActivity(i);
                        }


                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {

                        mProgressDialog.dismiss();
                    }
                });

    }
}
