package com.leonard.hasn.firebaseretry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView logEmail, logPass;
    private TextView forgtPass, goSignIn;
    private Button signInValue;
    private CheckBox rememberChechBox;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseauthListenEr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        logEmail = (AutoCompleteTextView) findViewById(R.id.logEmail);
        logPass = (AutoCompleteTextView) findViewById(R.id.logPass);
        forgtPass = (TextView) findViewById(R.id.forgtPass);
        goSignIn = (TextView) findViewById(R.id.goSignIn);
        signInValue = (Button) findViewById(R.id.signInValue);
        rememberChechBox = (CheckBox) findViewById(R.id.rememberChechBox);

        signInValue.setOnClickListener(this);
        goSignIn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void LogInApp () {

        String LogInValueEmail = logEmail.getText().toString().trim();
        String LogInValuePass = logPass.getText().toString().trim();

        if (TextUtils.isEmpty(LogInValueEmail)) {
            Toast.makeText(this, " Enter Email Address ",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(LogInValuePass)) {
            Toast.makeText(this, " Enter Your Password !", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(LogInValueEmail, LogInValuePass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Intent intent_Go = new Intent(Login.this, GoPage.class);
                            startActivity(intent_Go);
                            finish();
                            Toast.makeText(Login.this, " Log In Success! ", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, " Log In Failed ", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void goSignUpPage () {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick (View view) {
        if (view == signInValue) {

            LogInApp();
        }
        if (view == goSignIn) {

            goSignUpPage();
        }


    }
}
