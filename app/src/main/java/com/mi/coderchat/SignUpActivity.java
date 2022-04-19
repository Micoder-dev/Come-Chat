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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.util.Patterns;

public class SignUpActivity extends AppCompatActivity {
    private Button btnSign;
    private EditText edtUserName ,edtEmail ,edtPassword,edtConfirmPassword;
    private TextView txtAlreadyHAveAccount;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSign = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        edtConfirmPassword=findViewById(R.id.edtConfirmPasswordLogin);

        edtUserName = findViewById(R.id.edtUserName);
        edtUserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        txtAlreadyHAveAccount = findViewById(R.id.txtAlreadyHaveAccount);
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        txtAlreadyHAveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        final ProgressDialog pg = new ProgressDialog(SignUpActivity.this);
        pg.setTitle("Authentication");
        pg.setMessage("Please wait until authentication finishes");


        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                final String userName ,email , password,confirmPassword;
                userName =edtUserName.getText().toString().toUpperCase().trim();
                email = edtEmail.getText().toString().trim();
                password =edtPassword.getText().toString().trim();
                confirmPassword=edtConfirmPassword.getText().toString().trim();

                if(userName.equals("") || email.equals("") || password.equals("") || confirmPassword.equals(""))
                {
					if(userName.isEmpty()){
						edtUserName.setError("Full name required");
						edtUserName.requestFocus();
						return;
					}
					if(email.isEmpty()){
						edtEmail.setError("Email required");
						edtEmail.requestFocus();
						return;
					}
					if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
						edtEmail.setError("Please enter a valid email");
						edtEmail.requestFocus();
						return;
					}

					if(password.isEmpty()){
						edtPassword.setError("Password required");
						edtPassword.requestFocus();
						return;
					}
					if(password.length() < 6){
						edtPassword.setError("Password must be greater than 6");
						edtPassword.requestFocus();
						return;
					}
					if (confirmPassword.isEmpty()){
					    edtConfirmPassword.setError("Confirm password required");
					    edtConfirmPassword.requestFocus();
					    return;
                    }
                }
                else if (!confirmPassword.equals(password)){
                    edtConfirmPassword.setError("Password doesn't match");
                    edtConfirmPassword.requestFocus();
                    return;
                }
                else if (!userName.equals("")){
                    Query usernameQuery=FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Name").equalTo(userName);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount()>0){
                                Toast.makeText(SignUpActivity.this,"Choose a different user name",Toast.LENGTH_LONG).show();
                            }else {

                                pg.show();
                                mAuth.createUserWithEmailAndPassword(email,password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful())
                                                {
                                                    userRef.child(mAuth.getCurrentUser().getUid()).child("Name").setValue(userName)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(SignUpActivity.this,
                                                                                userName + " Successfully register",Toast.LENGTH_SHORT).show();
                                                                        pg.dismiss();
                                                                        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                                                        Toast.makeText(SignUpActivity.this,
                                                                                userName + "Please Enter your registered email & password",Toast.LENGTH_SHORT).show();
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                    else
                                                                    {
                                                                        pg.dismiss();
                                                                        Toast.makeText(SignUpActivity.this,
                                                                                "error: "+task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });
                                                }
                                                else {
                                                    pg.dismiss();
                                                    Toast.makeText(SignUpActivity.this,
                                                            "error: "+task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        user= mAuth.getCurrentUser();
        if(user != null)
        {
            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
