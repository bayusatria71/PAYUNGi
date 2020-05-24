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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryTabFragment extends Fragment {

    ArrayList<Date> borrowDate;
    ArrayList<Date> returnDate;
    ArrayList<Integer> price;
    ListView lvHistory;

    public HistoryTabFragment(ArrayList<Date> borrowDate, ArrayList<Date> returnDate, ArrayList<Integer> price)
    {
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.price = price;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View historyTab = inflater.inflate(R.layout.historytab_fragment, container, false);
        lvHistory = historyTab.findViewById(R.id.lvActivities);
        HistoryAdapter adapter = new HistoryAdapter(getContext(), borrowDate, returnDate, price);
        lvHistory.setAdapter(adapter);

        return historyTab;
    }

    private class HistoryAdapter extends ArrayAdapter<Date>
    {

        private final SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
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
