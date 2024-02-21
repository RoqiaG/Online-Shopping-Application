package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_result);

        // Start the barcode scanner
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Obtain the scanned result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            // Now, you can use the scanned barcode value to perform a search
            String scannedBarcode = result.getContents();
            // Display the result in the TextView
            displayResult(scannedBarcode);
        } else {
            Toast.makeText(this, "No Barcode Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayResult(String scannedBarcode) {
        TextView textViewResult = findViewById(R.id.textViewResult);
        textViewResult.setText("Scanned Barcode: " + scannedBarcode);
    }

    private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
}
