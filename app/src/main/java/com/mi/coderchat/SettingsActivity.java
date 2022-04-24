package com.mi.coderchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class SettingsActivity extends AppCompatActivity {

    private Button btnUpdate,btnDelete;

    private InterstitialAd interstitial;
    private static final String AD_UNIT_ID = "ca-app-pub-9312483859588872/7219987619";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.deleteAcBtn);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                super.onAdLoaded(interstitialAd);
                interstitial = interstitialAd;
                interstitialAd.show(SettingsActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Toast.makeText(SettingsActivity.this, "Ad failed to load, please check your internet connection", Toast.LENGTH_SHORT).show();
                interstitial = null;
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdate();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deteleAccount();
            }
        });
    }

    private void deteleAccount() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:micoder.com@gmail.com?subject=" + Uri.encode("Account Deletion from 'COMECHAT'") + "&body=" + Uri.encode("Enter your reason for deleting your account('_____'),\n\nYour UserName in ComeChat('_____')\n\nYour Email in ComeChat('_____')\n\n\nIf we want to improve any features('____')"));
        intent.setData(data);
        startActivity(intent);
    }

    private void checkUpdate() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

}