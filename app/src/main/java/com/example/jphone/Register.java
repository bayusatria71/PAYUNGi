package com.example.jphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText registerEmail, registerPassword, registerPhone,registerDate;

    TextView loginText;
    Button registerButton;
    ImageView gambarLogo2;
    ProgressBar loadRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPhone = findViewById(R.id.registerPhone);
        registerDate = findViewById(R.id.registerDate);
        loginText = findViewById(R.id.loginText);
        registerButton = findViewById(R.id.registerButton);
        gambarLogo2 = findViewById(R.id.gambarLogo2);
        loadRegister = findViewById(R.id.loadRegister);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            //String phone = registerPhone.getText().toString().trim();

            if(TextUtils.isEmpty(email))
            {
                registerEmail.setError("Email is required!");
                return;
            }

            if(TextUtils.isEmpty(password))
            {
                registerPassword.setError("Password is required!");
                return;
            }

            if(password.length() < 8)
            {
                registerPassword.setError("Password needs to be more than 8 character");
                return;
            }

            loadRegister.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    else {
                        loadRegister.setVisibility(View.INVISIBLE);
                        Toast.makeText(Register.this,"Failed to create",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Register.class));
                        finish();
                    }
                }
            });
          }
        });

        loginText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),LoginPage.class));
                finish();
            }
        });


    }
}
