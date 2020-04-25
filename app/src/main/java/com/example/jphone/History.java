package com.example.jphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class History extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    private CollectionReference ref;
    private NoteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        FirebaseUser user = fAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ref = db.collection("Return").document(user.getUid()).collection("pengembalian");
        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        setUpRecyclerView();
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
