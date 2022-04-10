package com.heshwa.mymessagingapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.Button;
import android.content.Intent;
import java.util.HashMap;
import com.google.firebase.database.ServerValue;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;


public class ProfileFragment extends Fragment {
    private TextView txtProfile;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

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

        circleMenu=view.findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.menu_icon,R.drawable.cancel_icon)
                .addSubMenu(Color.parseColor("#88bef5"),R.drawable.home_icon)
                .addSubMenu(Color.parseColor("#83e85a"),R.drawable.search_icon)
                .addSubMenu(Color.parseColor("#FF4832"),R.drawable.notification_icon)
                .addSubMenu(Color.parseColor("#ba53de"),R.drawable.settings_icon)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.gps_icon)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        switch (index){
                            case 0:
                                Toast.makeText(getActivity(),"Home",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getActivity(),"Search",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getActivity(),"Notification",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getActivity(),"Settings",Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(getActivity(),"GPS",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

        return view;
    }
}
