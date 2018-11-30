package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peterjester.inventory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FIREBASE_USER = "FIREBASE_USER";
    private TextView userNameView = null;
    private TextView passwordView = null;

    private Button signUpButton = null;
    private Button loginButton = null;

    private FirebaseAuth mAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        userNameView = (EditText) findViewById(R.id.userTextView);
        passwordView = (EditText) findViewById(R.id.passwordView);

        signUpButton = (Button) findViewById(R.id.signupButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        String email = userNameView.getText().toString();
        String password = passwordView.getText().toString();

        switch (v.getId()){
            case R.id.loginButton: signIn(email, password); break;
            case R.id.signupButton: signUp(email, password); break;
        }
    }

    private void signIn(String email, String password){

        // Tries to sign in a user with the given email address and password.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If sign is sucessful, update UI with the signed-in user's information
                            Log.d(FIREBASE_USER,String.valueOf(R.string.sign_in_success));
                            // Get the current user from Firebase
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Pass the user info as paramater to the next activity.
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(FIREBASE_USER, user);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", String.valueOf(R.string.sign_in_fail), task.getException());
                            String msgFailed =  R.string.auth_failed + task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, // context
                                    String.valueOf(R.string.auth_failed) + task.getException().getMessage(), // Message to be displayed
                                    Toast.LENGTH_SHORT).show(); // length for the toast
                        }
                    }
                });
    }

    private void signUp(String email, String password){


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // If sign in sucessfull, display a message to the user.
                    Log.d(FIREBASE_USER,String.valueOf(R.string.sign_up_success));
                    // Get user information.
                    FirebaseUser user = mAuth.getCurrentUser();
                    String msg = "User: "+user.getEmail()+" created successfully";
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                }else{
                    // TODO
                    // If sign in fails, display a message to the user.
                    Log.d(FIREBASE_USER, String.valueOf(R.string.sign_up_fail), task.getException());
                    String message =  R.string.auth_failed + task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, // context
                            message, // Message to be displayed
                            Toast.LENGTH_SHORT).show(); // length for the toast

                }
            }
        });
    }




}

