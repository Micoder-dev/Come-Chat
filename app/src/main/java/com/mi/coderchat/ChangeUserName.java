package com.mi.coderchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangeUserName extends AppCompatActivity {

    private EditText edtChangeName;
    private Button btnChangeName;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);

        edtChangeName=findViewById(R.id.edtChangeName);
        edtChangeName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btnChangeName=findViewById(R.id.btnChangeName);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        final ProgressDialog pg = new ProgressDialog(ChangeUserName.this);
        pg.setTitle("Checking");
        pg.setMessage("Please wait until updation finishes");

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newName;
                newName = edtChangeName.getText().toString().toUpperCase().trim();

                if (newName.equals("")) {
                    if (newName.isEmpty()) {
                        edtChangeName.setError("New name required");
                        edtChangeName.requestFocus();
                        return;
                    }
                }else if (!newName.equals("")) {
                    Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Name").equalTo(newName);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount()>0){
                                Toast.makeText(ChangeUserName.this,"User Name Already in use, please choose a different user name",Toast.LENGTH_LONG).show();
                                edtChangeName.setError("Name Already in use");
                                edtChangeName.requestFocus();
                                return;
                            }else {
                                pg.show();
                                userRef.child(mAuth.getCurrentUser().getUid()).child("Name").setValue(newName)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(ChangeUserName.this,
                                                            newName + " Successfully Updated",Toast.LENGTH_SHORT).show();
                                                    pg.dismiss();
                                                    Intent intent = new Intent(ChangeUserName.this,MainActivity.class);
                                                    Toast.makeText(ChangeUserName.this,
                                                            newName + " Happy Hunting",Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    pg.dismiss();
                                                    Toast.makeText(ChangeUserName.this,
                                                            "error: "+task.getException().toString(),Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ChangeUserName.this,
                                    "error: check your internet connection or contact developer",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}