package com.teamarte.arte.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.teamarte.arte.R;
import com.teamarte.arte.api.ApiServiceProvider;
import com.teamarte.arte.api.data.response.ProfileResponse;
import com.teamarte.arte.databinding.ActivityMainBinding;
import com.teamarte.arte.ui.home.ArtSoldFragment;
import com.teamarte.arte.ui.home.ExploreArtFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle drawerToggle;
    private ActivityMainBinding mBinding;
    private ExploreArtFragment exploreArtFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        drawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerToggle.syncState();

        //Initalize Fragments
        exploreArtFragment = new ExploreArtFragment();


        switchFragment(exploreArtFragment, "Explore Art");


        mBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.exploreArtMenu:
                        switchFragment(new ExploreArtFragment(), "Explore Art");

                        mBinding.drawerLayout.closeDrawers();
                        return true;

                    case R.id.artSoldMenu:
                        switchFragment(new ArtSoldFragment(), "Art Sold");
                        mBinding.drawerLayout.closeDrawers();
                        return true;
                    case R.id.signOutMenu:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Are you sure you want to Sign Out?")

                                .setPositiveButton("Yes, SIGN OUT!", (dialog, which) -> {

                                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestEmail()
                                            .requestProfile()

                                            .build();
                                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                                    googleSignInClient.signOut()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Timber.d("Signed out from google successfully");
                                                    Toast.makeText(MainActivity.this,
                                                            "Signed out",
                                                            Toast.LENGTH_SHORT)
                                                            .show();



                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Timber.e("Failed to sign out of google id");
                                                }
                                            });

                                    finish();
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialogBuilder.show();



                        return true;

            }
                return false;
        }
    });

        loadProfileData();
}

    private void switchFragment(Fragment newFragment, String title) {
        getSupportActionBar().setTitle(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrameLayout, newFragment)
                .commit();
    }


    private void loadProfileData(){
        ApiServiceProvider.getApiService()
                .getProfile()
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        if(response.isSuccessful()){
                            View headerView= mBinding.navigationView.getHeaderView(0);

                            Glide.with(MainActivity.this)
                                    .load(response.body().getProfilePhotoUrl())
                                    .placeholder(R.color.placeholder_color)
                                    .into((ImageView) headerView.findViewById(R.id.avatarImageView));
                            ((TextView)headerView.findViewById(R.id.usernamTextView)).setText("@" + response.body().getUsername());
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {

                    }
                });
    }
}
