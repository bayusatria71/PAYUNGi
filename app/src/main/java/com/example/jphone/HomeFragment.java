package com.example.jphone;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 101;
    private GoogleMap mMap;
    public double longitude, latitude;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    ImageView btnTransfer, btnReward, btnTopUp;
    TextView balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homePage = inflater.inflate(R.layout.frag_home, container, false);

        balance = homePage.findViewById(R.id.balanceView);
        btnTransfer = homePage.findViewById(R.id.btnTransfer);
        btnReward = homePage.findViewById(R.id.btnReward);
        btnTopUp = homePage.findViewById(R.id.btnTopUp);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getInstance().getCurrentUser();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        countBalance();
        fetchLastLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TransferActivity.class));
            }
        });


        return homePage;
    }

    private void countBalance() {
        final DecimalFormat rupiah = new DecimalFormat("Rp###,###.00");
        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list){
                        if(d.getId().equals(user.getUid())){
                            balance.setText(rupiah.format(d.getLong("Balance")));
                            break;
                        }
                    }
                }
            }
        });
    }

    private void fetchLastLocation()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
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
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(HomeFragment.this);

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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        countBalance();
    }
}
