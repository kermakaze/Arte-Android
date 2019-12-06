package com.teamarte.arte.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.teamarte.arte.App;
import com.teamarte.arte.AppConstants;
import com.teamarte.arte.R;
import com.teamarte.arte.api.data.response.ArtResponse;
import com.teamarte.arte.databinding.ActivityMainBinding;
import com.teamarte.arte.databinding.ActivityPaymentBinding;

import java.io.File;

import timber.log.Timber;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment);

        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.getSettings().setDomStorageEnabled(true);
        mBinding.webView.getSettings().setLoadWithOverviewMode(true);
        mBinding.webView.getSettings().setUseWideViewPort(true);
        mBinding.webView.loadUrl(getIntent().getStringExtra(AppConstants.PAYMENT_LINK_KEY));

        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Timber.d("url "+ url);
                if (url.startsWith("http://www.success.com/")) {

                    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                            Toast.LENGTH_LONG).show();
                    downloadFile();

                   finish();

                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }


    private void downloadFile(){
        File file=new File(getExternalFilesDir(null),"Arte Downloads");
       /*


       Create a DownloadManager.Request with all the information necessary to start the download
        */

       ArtResponse artResponse = (ArtResponse) getIntent().getSerializableExtra(AppConstants.ART_MODEL_KEY);
       Timber.d("url: " + getIntent().getStringExtra(AppConstants.IMAGE_URL_KEY));
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(getIntent().getStringExtra(AppConstants.IMAGE_URL_KEY)))
                .setTitle("Arte File-"+ artResponse.getId())// Title of the Download Notification
                .setDescription("Download Completed")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file

                // Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming n




        DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);


        long downloadID = downloadManager.enqueue(request);
    }


}
