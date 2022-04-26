package com.mi.coderchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Patterns;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText edtEmail ,edtPassword;
    private FirebaseAuth mAuth;
	private TextView gotoRegister,forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword =findViewById(R.id.edtPasswordLogin);
        final ProgressDialog pg = new ProgressDialog(LoginActivity.this);
        pg.setTitle("Authentication");
        pg.setMessage("Please wait until authentication finishes");
        mAuth = FirebaseAuth.getInstance();
		
		forgotPassword=findViewById(R.id.forgotPassword);
		forgotPassword.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
			}
		});
		
		gotoRegister=findViewById(R.id.gotoRegister);
		gotoRegister.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
			}
		});

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(email.equals("")|| password.equals(""))
                {
                    //Toast.makeText(LoginActivity.this,"All fields are mandatory",Toast.LENGTH_SHORT).show();
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
                }
                else {
                    pg.show();
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        pg.dismiss();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        if (user.isEmailVerified())
                                        {
                                            // user is verified, so you can finish this activity or send user to activity which you want.
                                            Toast.makeText(LoginActivity.this,"Successfully Signed in",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,SplashScreen.class));
                                            finish();
                                        }
                                        else
                                        {
                                            // email is not verified, so just prompt the message to the user and restart this activity.
                                            // NOTE: don't forget to log out the user.
                                            pg.setTitle("Email Verification");
                                            pg.setMessage("Please goto your email & verify the link sent from 'noreply',\nafter verification click anywhere to close this dialog & hit the login again");
                                            pg.show();
                                            FirebaseAuth.getInstance().signOut();
                                            user.sendEmailVerification();
                                            Toast.makeText(LoginActivity.this, "Please check your email to verify your account, then come back again", Toast.LENGTH_LONG).show();
                                            //restart this activity

                                        }
                                    }
                                    else
                                    {
                                        pg.dismiss();
                                        Toast.makeText(LoginActivity.this,"error :"+task.getException().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
