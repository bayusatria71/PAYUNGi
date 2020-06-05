package com.example.jphone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView menuBar = findViewById(R.id.menu_bar);
        menuBar.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    switch (item.getItemId())
                    {
                        case R.id.home_menu:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.history_menu:
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.rent_button:
                            QrCodeBottomSheet qrSheet = new QrCodeBottomSheet();
                            qrSheet.show(getSupportFragmentManager(), "QR Code");
                            return true;
                        case R.id.inbox_menu:
                            selectedFragment = new InboxFragment();
                            break;
                        case R.id.account_menu:
                            selectedFragment = new AccountFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                    return true;
                }
            };

}