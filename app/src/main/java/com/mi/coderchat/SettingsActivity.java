package com.mi.coderchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class SettingsActivity extends AppCompatActivity {

    private Button btnUpdate;

    private InterstitialAd interstitial;
    private static final String AD_UNIT_ID = "ca-app-pub-9312483859588872/7219987619";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnUpdate=findViewById(R.id.btnUpdate);

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
    }

    private void checkUpdate() {
        Toast.makeText(SettingsActivity.this,"please wait for the updates",Toast.LENGTH_SHORT).show();
    }

}