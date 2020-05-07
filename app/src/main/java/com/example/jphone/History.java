package com.example.jphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class History extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    private CollectionReference ref;
    private NoteAdapter adapter;
    Dialog dialog;
    ImageView homeButton, showQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final FirebaseUser user = fAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ref = db.collection("Return").document(user.getUid()).collection("pengembalian");
        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        setUpRecyclerView();
        homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

//        showQrCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImageView qrCode;
//                Button kembaliMenu;
//                dialog.setContentView(R.layout.custom_popups);
//                qrCode = (ImageView) dialog.findViewById(R.id.qrCode);
//                kembaliMenu = (Button) dialog.findViewById(R.id.kembaliMenu);
//                String qrid = user.getUid();
//                QRGEncoder qrgEncoder = new QRGEncoder(qrid, null, QRGContents.Type.TEXT, 1000);
//                try {
//                    Bitmap qrBits = qrgEncoder.getBitmap();
//                    qrCode.setImageBitmap(qrBits);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                kembaliMenu.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });

    }

    private void setUpRecyclerView() {
        Query query = ref;
        FirestoreRecyclerOptions<Note> option = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();
        adapter = new NoteAdapter(option);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
