package com.mi.coderchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;

public class MainActivity extends AppCompatActivity {
    private TextView txtdiplayUserName;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;

    private InterstitialAd interstitial;
    private static final String AD_UNIT_ID = "ca-app-pub-9312483859588872/7219987619";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Set app volume to be half of current device volume.  MobileAds.setAppVolume(0.5f);
        MobileAds.setAppVolume(0.0f);
        //To inform the SDK that the app volume has been muted, use the setAppMuted() method:
        MobileAds.setAppMuted(true);

        txtdiplayUserName = findViewById(R.id.txtDisplayName);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                super.onAdLoaded(interstitialAd);
                interstitial = interstitialAd;
                interstitialAd.show(MainActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Toast.makeText(MainActivity.this, "Ad failed to load, please check your internet connection", Toast.LENGTH_SHORT).show();
                interstitial = null;
            }
        });

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);
		
		// Reference (or instantiate) a ViewPager instance and apply a transformer
		mViewPager.setPageTransformer(true, (ViewPager.PageTransformer) new ScaleInOutTransformer());

		//tablayout icon
		mTabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
        mTabLayout.getTabAt(1).setIcon(R.drawable.search_icon);
        mTabLayout.getTabAt(2).setIcon(R.drawable.account_pic);

        //to hide auto keyboard opens
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        userRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtdiplayUserName.setText(snapshot.child("Name").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_mainactivity,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.itmContact)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:micoder.com@gmail.com?subject=" + Uri.encode("Subject") + "&body=" + Uri.encode("//Enter your Queries here..."));
            intent.setData(data);
            startActivity(intent);
        }
		if(item.getItemId() == R.id.itmAbout)
        {
			Toast.makeText(MainActivity.this,"Loading...",Toast.LENGTH_SHORT).show();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://micoder-dev.github.io/Resume-Page/"));
            startActivity(browserIntent);
		}
		if(item.getItemId() == R.id.itmSettings)
        {
			startActivity(new Intent(MainActivity.this,SettingsActivity.class));
		}
        if(item.getItemId() == R.id.itmLogOut)
        {
            
			new AlertDialog.Builder(this)
				.setMessage("Are you sure want to Logout!")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						HashMap<String,Object> map = new HashMap<>();
						map.put("Offline",ServerValue.TIMESTAMP);
						userRef.child(mAuth.getCurrentUser().getUid()).child("Status").setValue(map);
						mAuth.signOut();
						Intent intent = new Intent(MainActivity.this,LoginActivity.class);
						startActivity(intent);
						
					}
				}).setNegativeButton("No", null)
				.show();
        }
		if(item.getItemId() == R.id.itmExit)
        {
			//when exit menu clicked alert dialog
            new AlertDialog.Builder(this)
				.setMessage("Are you sure want to exit???")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				})
				.setNegativeButton("No", null)
				.show();
		}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
		
		new AlertDialog.Builder(this)
			.setMessage("Are you sure want to exit???")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					finish();
				}
			})
			.setNegativeButton("No", null)
			.show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        HashMap<String,Object> map = new HashMap<>();
        map.put("Online",ServerValue.TIMESTAMP);
        userRef.child(mAuth.getCurrentUser().getUid()).child("Status").setValue(map);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HashMap<String,Object> map = new HashMap<>();
        map.put("Offline",ServerValue.TIMESTAMP);
        userRef.child(mAuth.getCurrentUser().getUid()).child("Status").setValue(map);
    }
}
