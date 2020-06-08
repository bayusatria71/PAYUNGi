package com.example.jphone;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class InboxFragment extends Fragment {

    private ListView lvInbox;
    private ArrayList<Date> dateList = new ArrayList<>();
    private ArrayList<String> messageList = new ArrayList<>();
    private ArrayList<String> senderList = new ArrayList<>();
    private SimpleDateFormat fireDateFormat = new SimpleDateFormat("EEE MM dd hh:mm:ss yyyy");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inboxPage = inflater.inflate(R.layout.frag_inbox, container, false);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        lvInbox = inboxPage.findViewById(R.id.lvInbox);
        fillLists();

        return inboxPage;
    }

    private void fillLists()
    {
        db.collection("Messages").document(user.getUid()).collection("Inbox").
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot documentSnapshot : list){
                        try {
                            dateList.add(fireDateFormat.parse(documentSnapshot.getId()));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        messageList.add("Amount : " + documentSnapshot.getLong("price") + "\n" + documentSnapshot.getString("pesan"));
                        senderList.add(documentSnapshot.getString("sender"));
                    }
                    Collections.sort(dateList, Collections.<Date>reverseOrder());
                    InboxAdapter adapter = new InboxAdapter(getContext(), messageList, dateList, senderList);
                    lvInbox.setAdapter(adapter);
                }
            }
        });
    }

    private class InboxAdapter extends ArrayAdapter<String>
    {
        private ArrayList<Date> dates;
        private ArrayList<String> senders;

        InboxAdapter(Context context, ArrayList<String> messageList, ArrayList<Date> dates, ArrayList<String> senders) {
            super(context, 0, messageList);
            this.dates = dates;
            this.senders = senders;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String message = getItem(position);
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.inbox_list, parent, false);
            }

            TextView tvMessage = convertView.findViewById(R.id.tvMessage);
            TextView tvDate = convertView.findViewById(R.id.tvDate);
            TextView tvSender = convertView.findViewById(R.id.tvSender);

            tvMessage.setText(message);
            tvSender.setText(senders.get(position));
            if(dates.isEmpty()){
                tvDate.setText("Kosong");
            }
            else {
                tvDate.setText(format.format(dates.get(position)));
            }

            return convertView;
        }
    }

}