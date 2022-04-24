package com.mi.coderchat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import android.widget.Toast;


public class ProfileFragment extends Fragment {
    private TextView txtProfile;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private ImageView image_profile;

    CircleMenu circleMenu;


    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtProfile = view.findViewById(R.id.txtProfileName);
		
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        userRef.child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("Name"))
                {
                    txtProfile.setText(snapshot.child("Name").getValue().toString());
					
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image_profile=view.findViewById(R.id.image_profile);
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Will be updated soon, or contact the 'developer' for more info!",Toast.LENGTH_LONG).show();
            }
        });

        circleMenu=view.findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#FF69B4"),R.drawable.menu_icon,R.drawable.cancel_icon)
                .addSubMenu(Color.parseColor("#FF69B4"),R.drawable.home_icon)
                .addSubMenu(Color.parseColor("#83e85a"),R.drawable.email_contact)
                .addSubMenu(Color.parseColor("#ba53de"),R.drawable.exit_icon)
                .addSubMenu(Color.parseColor("#FF4832"),R.drawable.settings_icon)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.info_icon)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        switch (index){
                            case 0:
                                Toast.makeText(getActivity(),"Home",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(),MainActivity.class));
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri data = Uri.parse("mailto:micoder.com@gmail.com?subject=" + Uri.encode("Subject") + "&body=" + Uri.encode("//Enter your Queries here..."));
                                intent.setData(data);
                                startActivity(intent);
                                break;
                            case 2:
                                new AlertDialog.Builder(getContext())
                                        .setMessage("Are you sure want to exit???")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                getActivity().finish();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                break;
                            case 3:
                                Toast.makeText(getActivity(),"Settings",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(),SettingsActivity.class));
                                break;
                            case 4:
                                Toast.makeText(getContext(),"Loading...",Toast.LENGTH_SHORT).show();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://micoder-dev.github.io/Resume-Page/"));
                                startActivity(browserIntent);
                                break;
                        }
                    }
                });

        return view;
    }
}
