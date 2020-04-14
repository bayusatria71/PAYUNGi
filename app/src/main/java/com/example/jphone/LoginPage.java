package com.example.jphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView accountText;
    Button loginButton;
    EditText inputEmail,inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



    mAuth = FirebaseAuth.getInstance();
    accountText = findViewById(R.id.accountText);
    loginButton = findViewById(R.id.loginButton);
    inputEmail = findViewById(R.id.inputEmail);
    inputPassword = findViewById(R.id.inputPassword);

    accountText.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),Register.class));
            finish();
        }
    });

    loginButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View y){
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(LoginPage.this , "Berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }

                }
            });
        }
    });

    }
}
