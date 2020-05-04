package com.example.jphone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    Button returnButton, logoutButton,scanButton;
    TextView balance;
    ImageView gambar, historyButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    private GoogleMap mMap;
    private int balances;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    public double longitude,latitude;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

        }

        // generateButton = findViewById(R.id.generateButton);
        balance = findViewById(R.id.balanceView);
        logoutButton = findViewById(R.id.logoutButton);
        gambar = findViewById(R.id.gambar);
        historyButton = findViewById(R.id.historyButton);
        scanButton = findViewById(R.id.scanButton);
        returnButton = findViewById(R.id.returnButton);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user = mAuth.getInstance().getCurrentUser();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        countBalance();
        fetchLastLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        String qrid = user.getUid();
        QRGEncoder qrgEncoder = new QRGEncoder(qrid, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap qrBits = qrgEncoder.getBitmap();
            gambar.setImageBitmap(qrBits);
        } catch (Exception e) {
            e.printStackTrace();
        }

 /*       generateButton.setOnClickListener(new View.OnClickListener() {
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
        });*/
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View y) {
                mAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Scanner.class));
                finish();
            }
        });

        returnButton.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Scanner2.class));
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),History.class));
            }
        });
    }

    private void countBalance() {
        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list){
                        if(d.getId().equals(user.getUid())){
                           balance.setText("Rp. " + d.getLong("Balance"));
                           break;
                        }
                    }
                }
            }
        });
    }

    private void fetchLastLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {
                    currentLocation = location;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MainActivity.this);

                }
                else{
                    latitude = -6.363155;
                    longitude = 106.825454;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

}