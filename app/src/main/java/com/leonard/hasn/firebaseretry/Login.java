package com.leonard.hasn.firebaseretry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView logEmail, logPass;
    private TextView forgtPass, goSignIn;
    private Button signInValue, guestLogin;

    private CheckBox rememberChechBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPreferenceEditor;
    private boolean saveLoginState;

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
        guestLogin = (Button) findViewById(R.id.guestLogIn);

        // CheckBox implementation
        rememberChechBox = (CheckBox) findViewById(R.id.rememberChechBox);
        loginPreferences = getSharedPreferences("loginPrefsData", MODE_PRIVATE);
        loginPreferenceEditor = loginPreferences.edit();

        signInValue.setOnClickListener(this);
        goSignIn.setOnClickListener(this);
        guestLogin.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        // CheckBox Preferences
        saveLoginState = loginPreferences.getBoolean("saveLoginState", true);
        if (saveLoginState == true) {
            logEmail.setText(loginPreferences.getString("Username",null ));
            logPass.setText(loginPreferences.getString("Password", null));
        }
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

        // Remember Checked
        if (rememberChechBox.isChecked()){
            loginPreferenceEditor.putBoolean("saveLoginState", true);
            loginPreferenceEditor.putString("Username", LogInValueEmail);
            loginPreferenceEditor.putString("Password", LogInValuePass);
            loginPreferenceEditor.commit();
        }

        firebaseAuth.signInWithEmailAndPassword(LogInValueEmail, LogInValuePass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Intent intent_Go = new Intent(Login.this, GoPage.class);
                            startActivity(intent_Go);

                            Toast.makeText(Login.this, " Log In Success! ", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, " Log In Failed ", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

   private void GuestUserLogIN () {

        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Intent intent_Go = new Intent(Login.this, GoPage.class);
                    startActivity(intent_Go);
                    Toast.makeText(Login.this," Guest User ", Toast.LENGTH_SHORT).show();

                }
                else {

                    Toast.makeText(Login.this," Failed To Guest Login ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



      /*private void updateUI (FirebaseUser user) {

          if (user == null) {

              firebaseAuth.signInAnonymously()
                      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {

                              if (task.isSuccessful()) {

                                  FirebaseUser user = firebaseAuth.getCurrentUser();
                                  updateUI(user);
                              }
                              else {
                                  updateUI(null);
                              }
                          }
                      });
          }

      }*/


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

        if (view == guestLogin) {

            GuestUserLogIN();
        }


    }


    @Override
    protected void onStart () {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, GoPage.class));
        }

     /*  FirebaseUser currentUser = firebaseAuth.getCurrentUser();
       updateUI(currentUser);*/
    }
}

  /*  private void setOnClick(final Button btn, final String str){
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Do whatever you want(str can be used here)

            }
        });
    }*/