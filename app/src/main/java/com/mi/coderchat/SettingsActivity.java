package com.mi.coderchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private Button btnUpdate,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnUpdate=findViewById(R.id.btnUpdate);
        btnLogout=findViewById(R.id.btnLogout);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdate();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void checkUpdate() {
        Toast.makeText(SettingsActivity.this,"please wait for the updates",Toast.LENGTH_SHORT).show();
    }
    private void logout() {
    }

}