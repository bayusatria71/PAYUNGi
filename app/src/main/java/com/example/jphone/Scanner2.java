package com.example.jphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import org.w3c.dom.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner2 extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    ImageView cameraView;
    Button scanBarcode,backButton;
    AlertDialog waitingDialog;
    FirebaseVisionImage image;
    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;
    String rawValue;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    boolean status;
    long balances;
    private int count = 0;
    private long val;
    Date date;
    Date databasedate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner2);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        cameraView = findViewById(R.id.imageView2);
        scanBarcode = findViewById(R.id.scanBarcode2);
        backButton  = findViewById(R.id.backButton2);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE,
                                FirebaseVisionBarcode.FORMAT_PDF417
                        ).build();
        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }



    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            cameraView.setImageBitmap(imageBitmap);
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
            detector = FirebaseVision.getInstance()
                    .getVisionBarcodeDetector(options);


            Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                            long balancesSuccess;
                            for (FirebaseVisionBarcode barcode: barcodes) {
                                rawValue = barcode.getRawValue();
                            }
                            db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(!queryDocumentSnapshots.isEmpty()){
                                        List<DocumentSnapshot> lists = queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot f : lists){
                                            if(f.getId().equals(rawValue)){
                                                balances = (long) f.get("Balance");
                                                add();
                                                break;
                                            }
                                        }
                                    }
                                }

                                private void add() {
                                    db.collection("Borrow").get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if(!queryDocumentSnapshots.isEmpty()){
                                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                        for(DocumentSnapshot d : list){
                                                            if(d.getId().equals(rawValue)){
                                                                DocumentReference documentReference2 = db.collection("Return").document(rawValue);
                                                                Map<String, Object> map = new HashMap<>();
                                                                date = Calendar.getInstance().getTime();
                                                                databasedate = d.getDate("Tanggal Peminjamans");
                                                                long price = Math.abs(databasedate.getTime() - date.getTime());
                                                                long diff = (price / (60000));
                                                                val = 10000 + (diff/10)*10000;
                                                                if(balances < val){
                                                                    String kurang = "Balance anda : " + balances + "\nBiaya yang diperlukan : " + val + "\nSilahkan Topup sesuai biaya yang dibutuhkan";
                                                                    ExampleDialog exmaple = new ExampleDialog("Fund Required",kurang);
                                                                    exmaple.show(getSupportFragmentManager(),"Contoh");
                                                                    break;
                                                                }
                                                                long pass = balances - val;
                                                                setBalance(pass);
                                                                map.put("tanggalPeminjaman",databasedate);
                                                                map.put("tanggalDikembalikan", date);
                                                                map.put("price", val);
                                                                DocumentReference documentReferenceKid = documentReference2.collection("pengembalian").document(databasedate.toString());
                                                                documentReferenceKid.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        printData("berhasil");
                                                                    }
                                                                });
                                                                db.collection("Borrow").document(rawValue).delete();
                                                                break;
                                                            } else {
                                                                printData("Tidak ada yang sama");
                                                            }
                                                        }
                                                    } else {
                                                        printData("tidak ada peminjaman");
                                                    }
                                                }

                                                private void openDialog() {

                                                }


                                                private void setBalance(long pass) {
                                                    DocumentReference setVal = db.collection("Users").document(rawValue);
                                                    Map<String, Object> value = new HashMap<>();
                                                    value.put("Balance",pass);
                                                    setVal.update(value);
                                                }
                                            });
                                }
                            });


                            }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    });
        }
    }

    private void printData(String value) {
        Toast.makeText(this, value,Toast.LENGTH_SHORT).show();
    }
}
