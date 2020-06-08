package com.example.jphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String name, email, phoneNumber, emailVerification, passVerification;

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
                AlertDialog.Builder verification = new AlertDialog.Builder(getContext());
                verification.setTitle("Two Face Verification");
                View accountVerification = getLayoutInflater().inflate(R.layout.account_verification_layout, null);
                final EditText etEmailVerification = accountVerification.findViewById(R.id.etEmailVerification);
                final EditText etPassVerification = accountVerification.findViewById(R.id.etPasswordVerification);

                verification.setView(accountVerification);
                verification.setCancelable(true);

                verification.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        emailVerification = etEmailVerification.getText().toString().trim();
                        passVerification = etPassVerification.getText().toString().trim();

                        AuthCredential credential = EmailAuthProvider.getCredential(emailVerification, passVerification);

                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    etName.setEnabled(true);
                                    etEmail.setEnabled(true);
                                    etPhoneNumber.setEnabled(true);
                                    btnSave.setVisibility(View.VISIBLE);
                                    btnCancel.setVisibility(View.VISIBLE);
                                }
                                else {
                                    Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                verification.show();

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

                AuthCredential credential = EmailAuthProvider.getCredential(emailVerification, passVerification);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(getContext(), "Failed to save changes", Toast.LENGTH_LONG).show();
                                }
                                else {
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
                                    Toast.makeText(getContext(), "Your profile has been changed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });

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
