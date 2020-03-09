package com.example.jphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.WriteAbortedException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
    Button generateButton, scanButton;
    ImageView gambar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        generateButton = findViewById(R.id.generateButton);
        scanButton = findViewById(R.id.scanButton);
        gambar = findViewById(R.id.gambar);
        mAuth = FirebaseAuth.getInstance();

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                String qrid = user.getEmail();
                QRGEncoder qrgEncoder = new QRGEncoder(qrid, null, QRGContents.Type.TEXT, 500);
                  try {
                      Bitmap qrBits = qrgEncoder.getBitmap();
                      gambar.setImageBitmap(qrBits);
                  }
                  catch (Exception e)
                  {
                      e.printStackTrace();
                  }
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View y) {
            mAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginPage.class));
            finish();
            }
        });
    }

}
