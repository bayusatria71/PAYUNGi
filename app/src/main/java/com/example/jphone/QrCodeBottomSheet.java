package com.example.jphone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrCodeBottomSheet extends BottomSheetDialogFragment {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    ImageView qrCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View qrCodeSheet = inflater.inflate(R.layout.qr_code_sheet, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getInstance().getCurrentUser();

        qrCode = qrCodeSheet.findViewById(R.id.qrCode);
        String qrId = user.getUid();
        QRGEncoder qrgEncoder = new QRGEncoder(qrId, null, QRGContents.Type.TEXT, 1000);
        try {
            Bitmap qrBits = qrgEncoder.getBitmap();
            qrCode.setImageBitmap(qrBits);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qrCodeSheet;
    }
}
