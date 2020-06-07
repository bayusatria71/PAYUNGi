package com.example.jphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText registerEmail, registerPassword, registerPhone,registerDate, registerName;

    TextView loginText;
    Button registerButton;
    ImageView gambarLogo2;
    ProgressBar loadRegister;
    String date,phone;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    final static int balance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPhone = findViewById(R.id.registerPhone);
        registerDate = findViewById(R.id.registerDate);
        loginText = findViewById(R.id.loginText);
        registerButton = findViewById(R.id.registerButton);
        gambarLogo2 = findViewById(R.id.imagePayungi);
        loadRegister = findViewById(R.id.loadRegister);
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            Toast.makeText(this, "Logged in as "+ mAuth.getCurrentUser(),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
            final String email = registerEmail.getText().toString().trim();
            final String password = registerPassword.getText().toString().trim();
            phone = registerPhone.getText().toString().trim();
            date = registerDate.getText().toString().trim();

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
                        DocumentReference documentReference = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                        Map<String,Object> user = new HashMap<>();
                        user.put("Email",email);
                        user.put("Phone",phone);
                        user.put("Birth Date",date);
                        user.put("Balance", balance);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "User Created: "+ mAuth.getCurrentUser().getUid());
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        });
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(registerName.getText().toString().trim()).build();
                        currentUser.updateProfile(profileUpdates);

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
