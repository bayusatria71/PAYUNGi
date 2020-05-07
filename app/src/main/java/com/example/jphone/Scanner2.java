package com.example.jphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner2 extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
//    ImageView cameraView;
    SurfaceView cameraView;
    Button scanBarcode,backButton;
    AlertDialog waitingDialog;
    FirebaseVisionImage image;
    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;
    String rawValue;
    private boolean firstDetected = true;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    boolean status;
    long balances;
    private int count = 0;
    private long val;
    Date date;
    Date databasedate;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    SparseArray<Barcode> qrCodes;



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

        cameraView = findViewById(R.id.surfaceViewReturn);
        scanBarcode = findViewById(R.id.scanBarcode2);
        backButton  = findViewById(R.id.backButton2);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        scanBarcode.setVisibility(View.INVISIBLE);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(1280,720).build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                                         @Override
                                         public void release() {

                                         }

                                         @Override
                                         public void receiveDetections(Detector.Detections<Barcode> detections) {
                                             qrCodes = detections.getDetectedItems();
                                             if(qrCodes.size() != 0 && firstDetected) {
                                                 firstDetected = false;
                                                 db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                     @Override
                                                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                         if(!queryDocumentSnapshots.isEmpty()){
                                                             List<DocumentSnapshot> lists = queryDocumentSnapshots.getDocuments();
                                                             for(DocumentSnapshot f : lists){
                                                                 if(f.getId().equals(qrCodes.valueAt(0).displayValue)){
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
                                                                                 if(d.getId().equals(qrCodes.valueAt(0).displayValue)){
                                                                                     DocumentReference documentReference2 = db.collection("Return").document(qrCodes.valueAt(0).displayValue);
                                                                                     Map<String, Object> map = new HashMap<>();
                                                                                     date = Calendar.getInstance().getTime();
                                                                                     databasedate = d.getDate("Tanggal Peminjamans");
                                                                                     long price = Math.abs(databasedate.getTime() - date.getTime());
                                                                                     long diff = (price / (60000));
                                                                                     val = 10000 + (diff/10)*10000;
                                                                                     if(balances < val){
                                                                                         String kurang = "Balance anda : " + balances + "\nBiaya yang diperlukan : " + val + "\nSilahkan Topup sesuai biaya yang dibutuhkan";
                                                                                         ExampleDialog example = new ExampleDialog("Fund Required",kurang);
                                                                                         example.show(getSupportFragmentManager(),"Contoh");
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
                                                                                             scanBarcode.setVisibility(View.VISIBLE);
                                                                                         }
                                                                                     });
                                                                                     db.collection("Borrow").document(qrCodes.valueAt(0).displayValue).delete();
                                                                                     break;
                                                                                 }
                                                                             }
                                                                             scanBarcode.setVisibility(View.VISIBLE);
                                                                         } else {
                                                                             printData("tidak ada peminjaman");
                                                                         }
                                                                     }


                                                                     private void setBalance(long pass) {
                                                                         DocumentReference setVal = db.collection("Users").document(qrCodes.valueAt(0).displayValue);
                                                                         Map<String, Object> value = new HashMap<>();
                                                                         value.put("Balance",pass);
                                                                         setVal.update(value);
                                                                     }
                                                                 });
                                                     }
                                                 });
                                                }
                                             }
                                         });
//        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
//                        .setBarcodeFormats(
//                                FirebaseVisionBarcode.FORMAT_QR_CODE,
//                                FirebaseVisionBarcode.FORMAT_PDF417
//                        ).build();
        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode.setVisibility(View.INVISIBLE);
                firstDetected = true;
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



//    static final int REQUEST_IMAGE_CAPTURE = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode,resultCode,data);
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            cameraView.setImageBitmap(imageBitmap);
//            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
//            detector = FirebaseVision.getInstance()
//                    .getVisionBarcodeDetector(options);
//
//
//            Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
//                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
//                        @Override
//                        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
//                            for (FirebaseVisionBarcode barcode: barcodes) {
//                                rawValue = barcode.getRawValue();
//                            }
//
//
//                            }
//                    })
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                            finish();
//                        }
//                    });
//        }


    private void printData(String value) {
        Toast.makeText(this, value,Toast.LENGTH_SHORT).show();
    }
}


