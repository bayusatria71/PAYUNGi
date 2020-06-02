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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class InboxFragment extends Fragment {

    private ListView lvInbox;
    private ArrayList<Date> dateList = new ArrayList<>();
    private ArrayList<String> messageList = new ArrayList<>();
    private HashMap<String, Integer> inboxMapping = new HashMap<>();
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

    private void fetchList(){

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
                            Toast.makeText(getContext(), documentSnapshot.getId(), Toast.LENGTH_SHORT).show();

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        messageList.add(documentSnapshot.getString("pesan"));
                    }
                    inboxMapping.put(messageList.get(0), R.mipmap.payungi_logo);
                    InboxAdapter adapter = new InboxAdapter(getContext(), messageList, dateList, inboxMapping);
                    lvInbox.setAdapter(adapter);
                }
            }
        });
//        try {
//            dateList.add(dateFormat.parse("11-11-2020"));
//            dateList.add(dateFormat.parse("12-11-2020"));
//            dateList.add(dateFormat.parse("13-11-2020"));
//            dateList.add(dateFormat.parse("13-11-2020"));
//            dateList.add(dateFormat.parse("13-11-2020"));
//            dateList.add(dateFormat.parse("13-11-2020"));
//            dateList.add(dateFormat.parse("13-11-2020"));
//            dateList.add(dateFormat.parse("14-11-2020"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        messageList.add("Halo");
//        messageList.add("Hello");
//        messageList.add("こんにちは");
//        messageList.add("여보세요");
//        messageList.add("你好");
//        messageList.add("Привет");
//        messageList.add("สวัสดี");
//        messageList.add("Ciao");
//
//
    }

    private class InboxAdapter extends ArrayAdapter<String>
    {
        private ArrayList<Date> dates;
        private HashMap<String, Integer> pictures;

        InboxAdapter(Context context, ArrayList<String> messageList, ArrayList<Date> dates, HashMap<String, Integer> pictures) {
            super(context, 0, messageList);
            this.dates = dates;
            this.pictures = pictures;
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
            ImageView imMessage = convertView.findViewById(R.id.imMessage);

            tvMessage.setText(message);
            if(dates.isEmpty()){
                tvDate.setText("Kosong");
            }
            else {
                tvDate.setText(format.format(dates.get(position)));
            }
            if (pictures.containsKey(message) && pictures.get(message) != null)
            {
                imMessage.setImageResource(pictures.get(message));
                imMessage.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }

//    private class InboxAdapter extends BaseExpandableListAdapter
//    {
//        private Context context;
//        private ArrayList<Date> groupList;
//        private HashMap<Date, ArrayList<String>> childList;
//
//        public InboxAdapter(Context context, ArrayList<Date> groupList, HashMap<Date, ArrayList<String>> childList)
//        {
//            this.context = context;
//            this.groupList = groupList;
//            this.childList = childList;
//        }
//
//        @Override
//        public int getGroupCount() {
//            return this.groupList.size();
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return this.childList.get(groupList.get(groupPosition)).size();
//        }
//
//        @Override
//        public Date getGroup(int groupPosition) {
//            return this.groupList.get(groupPosition);
//        }
//
//        @Override
//        public String getChild(int groupPosition, int childPosition) {
//            return this.childList.get(groupList.get(groupPosition)).get(childPosition);
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//            Date groupTitle = (Date) getGroup(groupPosition);
//            if (convertView == null)
//            {
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.inbox_grouplist, null);
//            }
//
//            TextView dateTitle = (TextView) convertView.findViewById(R.id.tvGroupTitle);
//            dateTitle.setText(format.format(groupTitle));
//
//            return convertView;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//            String childMessage = (String) getChild(groupPosition, childPosition);
//            if (convertView == null)
//            {
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.inbox_childlist, null);
//            }
//
//            TextView childText = (TextView) convertView.findViewById(R.id.tvChildText);
//            childText.setText(childMessage);
//
//            return convertView;
//        }
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return false;
//        }
//    }

}
