package com.example.jphone;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    private CollectionReference historyReference, ongoingReference;
//    private NoteAdapter adapter;

    private ArrayList<Date> borrowDate = new ArrayList<>();
    private ArrayList<Date> returnDate = new ArrayList<>();
    private ArrayList<Integer> price = new ArrayList<>();

    private TabLayout tabMenu;
    private AppBarLayout titleBar;
    private ViewPager tabContainer;
    private TabAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View historyFragment = inflater.inflate(R.layout.frag_history, container, false);

        tabContainer = historyFragment.findViewById(R.id.tabContainer);
        tabMenu = historyFragment.findViewById(R.id.tabMenu);
        titleBar = historyFragment.findViewById(R.id.appBar);
        adapter = new TabAdapter(getFragmentManager());

        loadHistory();

//        Toast.makeText(getContext(), user.getUid(), Toast.LENGTH_SHORT).show();
//        setUpRecyclerView(activityPage);

        return historyFragment;
    }

    public void loadHistory()
    {
        FirebaseUser user = fAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        historyReference = db.collection("Return").document(user.getUid()).collection("pengembalian");
        historyReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    Note note = documentSnapshot.toObject(Note.class);

                    borrowDate.add(note.getTanggalPeminjaman());
                    returnDate.add(note.getTanggalDikembalikan());
                    price.add(note.getPrice());
                }

                adapter.addFragment(new OngoingTabFragment(borrowDate, returnDate, price), "Ongoing");
                adapter.addFragment(new HistoryTabFragment(borrowDate, returnDate, price), "History");
                titleBar.setOutlineProvider(null);
                tabContainer.setAdapter(adapter);
                tabMenu.setupWithViewPager(tabContainer);
                tabMenu.setSelectedTabIndicator(null);
            }
        });
    }

    private class TabAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title)
        {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }

//    private void setUpRecyclerView(View activityPage) {
//        Query query = ref;
//        FirestoreRecyclerOptions<Note> option = new FirestoreRecyclerOptions.Builder<Note>()
//                .setQuery(query,Note.class)
//                .build();
//        adapter = new NoteAdapter(option);
//        RecyclerView recyclerView = activityPage.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

}
