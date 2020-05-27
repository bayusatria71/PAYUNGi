package com.example.jphone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrCodeBottomSheet extends BottomSheetDialogFragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private DocumentReference userData;

    private ImageView qrCode;
    private TextView tvUId, tvPhoneNumber;

    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View qrCodeSheet = inflater.inflate(R.layout.qr_code_sheet, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getInstance().getCurrentUser();

        qrCode = qrCodeSheet.findViewById(R.id.qrCode);
        tvUId = qrCodeSheet.findViewById(R.id.tvUId);
        tvPhoneNumber = qrCodeSheet.findViewById(R.id.tvPhoneNumber);

        String qrId = user.getUid();
        getPhoneNumber(qrId);
        QRGEncoder qrgEncoder = new QRGEncoder(qrId, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap qrBits = qrgEncoder.getBitmap();
            qrCode.setImageBitmap(qrBits);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qrCodeSheet;
    }

    public void getPhoneNumber(String Uid)
    {
        userData = db.collection("Users").document(Uid);
        userData.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                phoneNumber = documentSnapshot.getString("Phone");
                tvPhoneNumber.setText(phoneNumber);
            }
        });
    }
}
