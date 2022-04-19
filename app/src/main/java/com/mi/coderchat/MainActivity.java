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
	
	//private ImageView profileImg;
	//private Uri imageUri;
	//private FirebaseStorage storage;
	//private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtdiplayUserName = findViewById(R.id.txtDisplayName);

        //profileImg=findViewById(R.id.profileImg);
        //storage=FirebaseStorage.getInstance();
        //storageReference=storage.getReference();


        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);
		
		//imgView2=findViewById(R.id.imgView2);
		
		//String imageUri = "https://i.imgur.com/tGbaZCY.jpg";
		//Picasso.with(this).load(imageUri).into(imgView2);
		
		// Reference (or instantiate) a ViewPager instance and apply a transformer
		mViewPager.setPageTransformer(true, (ViewPager.PageTransformer) new ScaleInOutTransformer());

		//tablayout icon
		mTabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
        mTabLayout.getTabAt(1).setIcon(R.drawable.search_icon);
        mTabLayout.getTabAt(2).setIcon(R.drawable.account_pic);

        //to hide auto keyboard opens
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/*
		//profile image
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
		 */

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
    /*
    private void choosePicture() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profileImg.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey= UUID.randomUUID().toString();
        StorageReference riversRef=storageReference.child("images/"+randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progressPercent=(100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Progress: "+(int) progressPercent + "%");
            }
        });
    }

     */

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
