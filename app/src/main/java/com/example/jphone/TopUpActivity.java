package com.example.jphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TopUpActivity extends AppCompatActivity {

    private ViewPager2 topupContainer;
    private TabLayout topupMenu;
    private AppBarLayout titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        topupContainer = findViewById(R.id.topupContainer);
        topupMenu = findViewById(R.id.topupMenu);
        titleBar = findViewById(R.id.appBar);
        topupMenu.setSelectedTabIndicator(null);
        titleBar.setOutlineProvider(null);


        TabAdapter adapter = new TabAdapter(TopUpActivity.this);
        topupContainer.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                topupMenu, topupContainer, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0)
                {
                    tab.setText("Phone Credit");
                }
                else
                {
                    tab.setText("Other Methods");
                }
            }
        });
        tabLayoutMediator.attach();

    }

    private static class TabAdapter extends FragmentStateAdapter
    {
        TabAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new PhoneCreditTabFragment();
            }
            return new OtherMethodsTabFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
