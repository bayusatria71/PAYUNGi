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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;

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

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getInstance().getCurrentUser();

        tvName.setText(user.getDisplayName());
        etEmail.setText(user.getEmail());
        etPhoneNumber.setText(user.getPhoneNumber());

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
