package com.example.jphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class TransferActivity extends AppCompatActivity {

    EditText etPhoneNumber, etTransferAmount, etMessage;
    Button btnTransfer;
    Integer transferAmount;
    String phoneNumber, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etMessage = findViewById(R.id.etMessage);
        etTransferAmount = findViewById(R.id.etTransferAmount);
        btnTransfer = findViewById(R.id.btnTransfer);

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = etPhoneNumber.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message = etMessage.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = etTransferAmount.getText().toString().replaceAll(",", "");
                if(!amount.equals(""))
                {
                    transferAmount = Integer.valueOf(amount);

                }
                else
                {
                    transferAmount = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
