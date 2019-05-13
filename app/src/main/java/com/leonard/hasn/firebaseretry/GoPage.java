package com.leonard.hasn.firebaseretry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class GoPage extends AppCompatActivity implements View.OnClickListener {

    private Button log_out_btn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_page);

        firebaseAuth = FirebaseAuth.getInstance();

        log_out_btn = (Button) findViewById(R.id.log_out_btn);

        log_out_btn.setOnClickListener(this);


    }

    private void LoggedOut () {
        firebaseAuth.signOut();

        Intent LogPageIntent = new Intent(GoPage.this, Login.class);
        startActivity(LogPageIntent);
        finish();

        Toast.makeText(GoPage.this, " Done ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick (View view) {

        if (view == log_out_btn) {

            LoggedOut();

        }
    }
}
