package com.example.jphone;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryTabFragment extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private CollectionReference historyReference;

    private ArrayList<Date> borrowDate = new ArrayList<>();
    private ArrayList<Date> returnDate = new ArrayList<>();
    private ArrayList<Integer> price = new ArrayList<>();
    private ListView lvHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View historyTab = inflater.inflate(R.layout.historytab_fragment, container, false);
        lvHistory = historyTab.findViewById(R.id.lvActivities);
        loadHistory();

        return historyTab;
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
                HistoryAdapter adapter = new HistoryAdapter(getContext(), borrowDate, returnDate, price);
                lvHistory.setAdapter(adapter);
            }
        });
    }

    private class HistoryAdapter extends ArrayAdapter<Date>
    {

        private final SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        private ArrayList<Date> returnDate;
        private ArrayList<Integer> price;

        HistoryAdapter(Context context, ArrayList<Date> borrowDate, ArrayList<Date> returnDate, ArrayList<Integer> price)
        {
            super(context, 0, borrowDate);
            this.returnDate = returnDate;
            this.price = price;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date borrow = getItem(position);
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.history_list, parent, false);
            }

            DecimalFormat rupiahFormat = new DecimalFormat("Rp###,###.00");

            TextView tvBorrowDate = convertView.findViewById(R.id.tvBorrowDate);
            TextView tvReturnDate = convertView.findViewById(R.id.tvReturnDate);
            TextView tvPrice = convertView.findViewById(R.id.tvPrice);

            tvBorrowDate.setText(format.format(borrow));
            tvReturnDate.setText(format.format(returnDate.get(position)));
            tvPrice.setText(rupiahFormat.format(price.get(position)));

            return convertView;
        }
    }
}
