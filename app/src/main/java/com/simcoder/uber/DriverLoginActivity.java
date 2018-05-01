package com.simcoder.uber;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverLoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(DriverLoginActivity.this, DriverMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mLogin = (Button) findViewById(R.id.login);
        mRegistration = (Button) findViewById(R.id.registration);

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                boolean aux = false;
                if (email.equals("")) {
                    Toast.makeText(DriverLoginActivity.this, "El correo electronico es requerido", Toast.LENGTH_SHORT).show();
                }else{
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(email);
                    aux = matcher.matches();
                    if(!aux){
                        Toast.makeText(DriverLoginActivity.this, "El correo electronico es inavalido", Toast.LENGTH_SHORT).show();
                    }else{
                        if (password.equals("")) {
                            Toast.makeText(DriverLoginActivity.this, "La contraseña es requerida", Toast.LENGTH_SHORT).show();
                        }else{
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(DriverLoginActivity.this, "Error, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                                    }else{
                                        String user_id = mAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id).child("name");
                                        current_user_db.setValue(email);
                                    }
                                }
                            });

                        }

                    }




                }


            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                boolean aux = false;
                if (email.equals("")) {
                    Toast.makeText(DriverLoginActivity.this, "El correo electronico es requerido", Toast.LENGTH_SHORT).show();
                }else{
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(email);
                    aux = matcher.matches();
                    if(!aux){
                        Toast.makeText(DriverLoginActivity.this, "El correo electronico es inavalido", Toast.LENGTH_SHORT).show();
                    }else{
                        if (password.equals("")) {
                            Toast.makeText(DriverLoginActivity.this, "La contraseña es requerida", Toast.LENGTH_SHORT).show();
                        }else{
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(DriverLoginActivity.this, "Error, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
