package com.example.jphone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    String phoneNumber;

    Button btnLogOut;
    EditText etEmail, etPhoneNumber, etPassword;
    TextView tvName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View accountPage = inflater.inflate(R.layout.frag_account, container, false);
        btnLogOut = accountPage.findViewById(R.id.btnLogout);
        etEmail = accountPage.findViewById(R.id.etEmail);
        etPhoneNumber = accountPage.findViewById(R.id.etPhoneNumber);
        etPassword = accountPage.findViewById(R.id.etPassword);
        tvName = accountPage.findViewById(R.id.tvName);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getInstance().getCurrentUser();

        tvName.setText(user.getDisplayName());
        etEmail.setText(user.getEmail());
        db.collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                phoneNumber = documentSnapshot.getString("Phone");
                etPhoneNumber.setText(phoneNumber);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginPage.class));
            }
        });

        return accountPage;
    }
}