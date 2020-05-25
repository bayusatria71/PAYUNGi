package com.example.jphone;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OngoingTabFragment extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private DocumentReference ongoingReference;


    private ArrayList<Date> borrowDate = new ArrayList<>();
    private ListView lvOngoing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ongoingTab = inflater.inflate(R.layout.historytab_fragment, container, false);
        lvOngoing = ongoingTab.findViewById(R.id.lvActivities);
        loadOngoing();

        return ongoingTab;
    }

    public void loadOngoing()
    {
        FirebaseUser user = fAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ongoingReference = db.collection("Borrow").document(user.getUid());
        ongoingReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                borrowDate.add(documentSnapshot.getDate("Tanggal Peminjamans"));
                OngoingAdapter adapter = new OngoingAdapter(getContext(), borrowDate);
                lvOngoing.setAdapter(adapter);
            }
        });
    }

    private class OngoingAdapter extends ArrayAdapter<Date>
    {

        private final SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

        OngoingAdapter(Context context, ArrayList<Date> borrowDate)
        {
            super(context, 0, borrowDate);
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

            TextView tvBorrowDate = convertView.findViewById(R.id.tvBorrowDate);
            TextView tvReturnDate = convertView.findViewById(R.id.tvReturnDate);
            TextView tvPrice = convertView.findViewById(R.id.tvPrice);
            TextView staticReturnDate = convertView.findViewById(R.id.staticReturnDate);
            TextView staticPrice = convertView.findViewById(R.id.staticPrice);

            tvBorrowDate.setText(format.format(borrow));
            tvReturnDate.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            staticReturnDate.setVisibility(View.GONE);
            staticPrice.setVisibility(View.GONE);

            return convertView;
        }
    }

}
