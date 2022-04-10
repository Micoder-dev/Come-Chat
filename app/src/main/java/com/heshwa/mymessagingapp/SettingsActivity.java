package com.heshwa.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class SettingsActivity extends AppCompatActivity {

    CircleMenu circleMenu;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        circleMenu=findViewById(R.id.circle_menu);
        relativeLayout=findViewById(R.id.relativeLayout);

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
                                Toast.makeText(SettingsActivity.this,"Home",Toast.LENGTH_SHORT).show();
                                relativeLayout.setBackgroundColor(Color.parseColor("#ecfffb"));
                                break;
                            case 1:
                                Toast.makeText(SettingsActivity.this,"Search",Toast.LENGTH_SHORT).show();
                                relativeLayout.setBackgroundColor(Color.parseColor("#96f7d2"));
                                break;
                            case 2:
                                Toast.makeText(SettingsActivity.this,"Notification",Toast.LENGTH_SHORT).show();
                                relativeLayout.setBackgroundColor(Color.parseColor("#fac4a2"));
                                break;
                            case 3:
                                Toast.makeText(SettingsActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                                relativeLayout.setBackgroundColor(Color.parseColor("#d3cde6"));
                                break;
                            case 4:
                                Toast.makeText(SettingsActivity.this,"GPS",Toast.LENGTH_SHORT).show();
                                relativeLayout.setBackgroundColor(Color.parseColor("#fff591"));
                                break;
                        }
                    }
                });

    }
}