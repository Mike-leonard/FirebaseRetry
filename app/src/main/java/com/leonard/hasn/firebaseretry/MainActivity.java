package com.leonard.hasn.firebaseretry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

// using view.On clicklistener to work shorter
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private AutoCompleteTextView nameGatter, emailGatter, numberGatter, passGatter, confirmPassGatter;
    private TextView goSignIn;
    private Button signUpValue;


    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseDatabase firebaseDatabaseInstance;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nameGatter = (AutoCompleteTextView) findViewById(R.id.nameGatter);
        emailGatter = (AutoCompleteTextView) findViewById(R.id.emailGatter);
        numberGatter = (AutoCompleteTextView) findViewById(R.id.numberGatter);
        passGatter = (AutoCompleteTextView) findViewById(R.id.passGatter);
        confirmPassGatter = (AutoCompleteTextView) findViewById(R.id.confirmPassGatter);
        goSignIn = (TextView) findViewById(R.id.goSignIn);
        signUpValue = (Button) findViewById(R.id.signUpValue);

        signUpValue.setOnClickListener(this);
        goSignIn.setOnClickListener(this);

        // creating users
        FirebaseApp.initializeApp(this);
        firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabaseInstance.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        // If Users Login Or not
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(MainActivity.this, GoPage.class);
                    startActivity(intent);
                    finish();
                }
                return;
            }
        };


    }

    // Registering users
    private void registerUser () {

       final String emailUser = emailGatter.getText().toString().trim();
       final String passuser = passGatter.getText().toString().trim();
       final String confirmpass = confirmPassGatter.getText().toString().trim();
        String name = nameGatter.getText().toString().trim();

        if (TextUtils.isEmpty(emailUser)) {

            Toast.makeText(this, " Please Enter Email ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passuser)) {

            Toast.makeText(this, " Please Enter Password ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmpass)) {
            Toast.makeText(this, " Please Enter Confirm Password ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!confirmpass.equals(passuser)) {
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query to Check Same User Name or not
        Query name_usersQuery = FirebaseDatabase.getInstance().getReference().orderByChild("Name : ").equalTo(name);

        // Query Listener
        name_usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {

                    Toast.makeText(MainActivity.this, "choose a different username", Toast.LENGTH_SHORT).show();

                }
                else {
                    // Firebase Create Account
                    firebaseAuth.createUserWithEmailAndPassword(emailUser, passuser)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    saveUserData();
                                    Toast.makeText(MainActivity.this," Registered ", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failed to Register",Toast.LENGTH_SHORT).show();
                                }
                            });



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Saving User IInformation in Real time database
    private void saveUserData () {

        // Getting value from user Inputs
        String name = nameGatter.getText().toString().trim();
        String email = emailGatter.getText().toString().trim();
        String number = (numberGatter.getText().toString().trim());
        String password = passGatter.getText().toString().trim();

        // Sends data in a Set ..can possibly send a larger group of value information
        HashMap <String, String> dataMap = new HashMap<String, String>();
        dataMap.put("Name : ", name);
        dataMap.put("Country : ", email);
        dataMap.put("Number : ", number);
        dataMap.put("PassWord : ", password);


        // On task complte or in particular situatuion
        final String user_id = firebaseAuth.getCurrentUser().getUid();   // Get the UID

        // Creating Child By User IDs
        DatabaseReference current_user_info = FirebaseDatabase.getInstance()
                .getReference().child(user_id);

        current_user_info.setValue(dataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, " Saved ", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(MainActivity.this, user_id , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goLogInPage () {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick (View view) {
        if (view == signUpValue) {

            registerUser();
        }

        if (view == goSignIn) {

            goLogInPage();
        }
    }


}
