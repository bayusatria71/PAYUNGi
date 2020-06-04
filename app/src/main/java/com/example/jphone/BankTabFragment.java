package com.example.jphone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BankTabFragment extends Fragment {

    private int[] imageLogos = {R.drawable.bni, R.drawable.bank_mandiri, R.drawable.bca, R.drawable.danamon};
    private String[] bankNames = {"BNI", "Bank Mandiri", "BCA", "Danamon"};


    private ExpandableListView expBank;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View bankMethod = inflater.inflate(R.layout.topup_bank, container, false);
        expBank = bankMethod.findViewById(R.id.expOtherMethods);

        String[] descriptions =
                {
                        "1. Insert your BNI ATM card and PIN\n\n" +
                                "2. Select the 'Other' menu\n\n" +
                                "3. Select the 'Transfer' menu\n\n" +
                                "4. Select 'Virtual Account Billing'\n\n" +
                                "5. Enter your Virtual Account Number - 42069 + Your Mobile Number\n\n" +
                                "6. Enter the nominal top-up",
                        "1. Insert your Mandiri ATM card and PIN\n\n" +
                                "2. Select the 'Pay/Buy' menu\n\n" +
                                "3. Select the 'Other' menu and select 'Other' again\n\n" +
                                "4. Select 'Multi Payment'\n\n" +
                                "5. Enter the PAYUNGi Company Code - 42069\n\n" +
                                "6. Enter your Virtual Account Number - 42069 + Your Mobile Number\n\n" +
                                "7. Enter the nominal top-up",
                        "1. Insert your ATM card and BCA PIN\n\n" +
                                "2. Select 'Other Transactions' menu\n\n" +
                                "3. Select the 'Transfer' menu\n\n" +
                                "4. Select 'Go to BCA Virtual Account' menu\n\n" +
                                "5. Enter your Virtual Account Number - 42069 + Your Mobile Number\n\n" +
                                "6. Enter the nominal top-up",
                        "1. Enter your Danamon ATM card and PIN\n\n" +
                                "2. Select the 'Payment' menu\n\n" +
                                "3. Select 'E-Commerce' menu\n\n" +
                                "4. Select 'PAYUNGi Top Up'\n\n" +
                                "5. Enter Your Mobile Number - 42069 + Your Mobile Number\n\n" +
                                "6. Enter the nominal top-up"
                };

        BankAdapter adapter = new BankAdapter(getContext(), bankNames, descriptions, imageLogos);
        expBank.setAdapter(adapter);

        expBank.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                {
                    expBank.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });


        return bankMethod;
    }

}
