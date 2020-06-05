package com.example.jphone;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PhoneCreditTabFragment extends Fragment {

    private Button btnFirst, btnSecond, btnThird, btnTopUp;
    private EditText etTopUpAmount;
    private TextView tvphoneNumber, tvBalance;

    private long topUp = 0L;
    private long balance = 0L;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private DocumentReference userReference;

    private DecimalFormat rupiah = new DecimalFormat("Rp###,###.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View phoneCredit = inflater.inflate(R.layout.topup_phone_credit, container, false);

        btnFirst = phoneCredit.findViewById(R.id.btn20000);
        btnSecond = phoneCredit.findViewById(R.id.btn30000);
        btnThird = phoneCredit.findViewById(R.id.btn50000);
        btnTopUp = phoneCredit.findViewById(R.id.btnTopUp);
        etTopUpAmount = phoneCredit.findViewById(R.id.etTopUpAmount);
        tvphoneNumber = phoneCredit.findViewById(R.id.tvPhoneNumber);
        tvBalance = phoneCredit.findViewById(R.id.tvBalance);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        userReference = db.collection("Users").document(user.getUid());

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MM dd hh:mm:ss yyyy");
        String tanggal = formatter.format(date);
        final DocumentReference inboxTopUp = db.collection("Messages").document(user.getUid()).collection("Inbox").document(tanggal);

        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("Phone") != null && documentSnapshot.getLong("Balance") != null)
                {
                    tvphoneNumber.setText(documentSnapshot.getString("Phone"));
                    tvBalance.setText(rupiah.format(documentSnapshot.getLong("Balance")));
                    balance = documentSnapshot.getLong("Balance");
                }
            }
        });

        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topUp = 20000;
                etTopUpAmount.setText("20,000");
            }
        });

        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topUp = 30000;
                etTopUpAmount.setText("50,000");
            }
        });

        btnThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topUp = 50000;
                etTopUpAmount.setText("100,000");
            }
        });

        etTopUpAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = etTopUpAmount.getText().toString().replaceAll(",", "");
                if(!amount.equals(""))
                {
                    topUp = Integer.valueOf(amount);

                }
                else
                {
                    topUp = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        balance = documentSnapshot.getLong("Balance");
                    }
                });
                Map<String, Object> tempTopUp = new HashMap<>();
                Map<String, Object> mapper = new HashMap<>();
                mapper.put("sender","PAYUNGI");
                mapper.put("price",topUp);
                mapper.put("pesan","Berhasil melakukan Top Up sebesar : " + topUp);
                balance = balance + topUp;
                tempTopUp.put("Balance",balance);
                userReference.update(tempTopUp);
                tvBalance.setText(rupiah.format(balance));
                inboxTopUp.set(mapper);
            }
        });


        return phoneCredit;
    }

}
