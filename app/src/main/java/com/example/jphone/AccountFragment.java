package com.example.jphone;

import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String name, email, phoneNumber;

    private Button btnLogOut, btnSave, btnCancel;
    private EditText etEmail, etPhoneNumber, etName;
    private TextView tvChangeProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View accountPage = inflater.inflate(R.layout.frag_account, container, false);
        btnLogOut = accountPage.findViewById(R.id.btnLogout);
        btnSave = accountPage.findViewById(R.id.btnSave);
        btnCancel = accountPage.findViewById(R.id.btnCancel);
        etEmail = accountPage.findViewById(R.id.etEmail);
        etPhoneNumber = accountPage.findViewById(R.id.etPhoneNumber);
        etName = accountPage.findViewById(R.id.tvName);
        tvChangeProfile = accountPage.findViewById(R.id.tvChangeProfile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getInstance().getCurrentUser();

        btnCancel.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etPhoneNumber.setEnabled(false);

        if(user.getDisplayName()!= null){
            etName.setText(user.getDisplayName());
            name = user.getDisplayName();
        }
        else{
            etName.setText("Error");
        }

        etEmail.setText(user.getEmail());
        email = user.getEmail();
        db.collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                phoneNumber = documentSnapshot.getString("Phone");
                etPhoneNumber.setText(phoneNumber);
            }
        });


        tvChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setEnabled(true);
                etEmail.setEnabled(true);
                etPhoneNumber.setEnabled(true);
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText(name);
                etPhoneNumber.setText(phoneNumber);
                etEmail.setText(email);

                etName.setEnabled(false);
                etEmail.setEnabled(false);
                etPhoneNumber.setEnabled(false);
                btnCancel.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                phoneNumber = etPhoneNumber.getText().toString().trim();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(etName.getText().toString().trim()).build();
                user.updateProfile(profileUpdates);
                DocumentReference setProfile = db.collection("Users").document(user.getUid());
                Map<String, Object> profileUpdate = new HashMap<>();
                profileUpdate.put("Email", etEmail.getText().toString().trim());
                profileUpdate.put("Phone", etPhoneNumber.getText().toString().trim());
                setProfile.update(profileUpdate);

                etName.setEnabled(false);
                etEmail.setEnabled(false);
                etPhoneNumber.setEnabled(false);
                btnCancel.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginPage.class));
                getActivity().finish();
            }
        });

        return accountPage;
    }
}
