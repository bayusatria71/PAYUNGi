package com.example.jphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {

    EditText etPhoneNumber, etTransferAmount, etMessage;
    Button btnTransfer;
    int transferAmount;
    String phoneNumber, message;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    long balanceKembali = 0l;
    String phoneNumberUser;
    final static String PESAN_PAYUNGI = "Transaksi Berhasil diselesaikan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etMessage = findViewById(R.id.etMessage);
        etTransferAmount = findViewById(R.id.etTransferAmount);
        btnTransfer = findViewById(R.id.btnTransfer);

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = etPhoneNumber.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message = etMessage.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = etTransferAmount.getText().toString().replaceAll(",", "");
                if(!amount.equals(""))
                {
                    transferAmount = Integer.valueOf(amount);

                }
                else
                {
                    transferAmount = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentChecker : list){
                                if(documentChecker.getId().equals(user.getUid())){
                                    balanceKembali = (long) documentChecker.get("Balance");
                                    phoneNumberUser = (String) documentChecker.get("Phone");

                                }
                            }
                            for (DocumentSnapshot documentSnapshot : list) {
                                if (documentSnapshot.get("Phone").equals(phoneNumber)) {
                                    Date date = Calendar.getInstance().getTime();
                                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MM dd hh:mm:ss yyyy");
                                    String tanggal = formatter.format(date);
                                    DocumentReference inboxMessage = db.collection("Messages").document(documentSnapshot.getId()).collection("Inbox").document(tanggal);
                                    DocumentReference setVal = db.collection("Users").document(documentSnapshot.getId());
                                    DocumentReference minusVal = db.collection("Users").document(user.getUid());
                                    DocumentReference inboxPengirim = db.collection("Messages").document(user.getUid()).collection("Inbox").document(tanggal);
                                    Map<String, Object> mapper = new HashMap<>();
                                    Map<String, Object> mapperKedua = new HashMap<>();
                                    mapper.put("sender",phoneNumberUser);
                                    mapperKedua.put("price",transferAmount);
                                    mapperKedua.put("sender","PAYUNGI");
                                    mapper.put("price", transferAmount);
                                    mapperKedua.put("pesan",PESAN_PAYUNGI+"\nTarget: " + phoneNumber +"\nAmmount: " + transferAmount);
                                    if (!message.isEmpty()) {
                                        mapper.put("pesan", message);
                                    } else {
                                        mapper.put("pesan", "No message to be shown");
                                    }

                                    Map<String, Object> value = new HashMap<>();
                                    long balanceSementara = (long) documentSnapshot.get("Balance");
                                    if(balanceKembali < transferAmount){
                                        Toast.makeText(TransferActivity.this, "Uang terlalu dikit", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    else {
                                        balanceSementara = balanceSementara + Long.valueOf(transferAmount);
                                        balanceKembali = balanceKembali - Long.valueOf(transferAmount);
                                        value.put("Balance", balanceSementara);
                                        setVal.update(value);
                                        value.put("Balance",balanceKembali);
                                        minusVal.update(value);
                                    }


                                    inboxMessage.set(mapper).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(TransferActivity.this, "Berhasil dikirim", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    inboxPengirim.set(mapperKedua).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(TransferActivity.this, "Transaksi diselesaikan", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        });
    }

}
