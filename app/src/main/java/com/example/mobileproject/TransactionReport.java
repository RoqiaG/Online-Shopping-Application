package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TransactionReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_report);

        String currentUserEmail = getUserEmailFromDatabase();

        generateTransactionReport(currentUserEmail);

    }
    @SuppressLint("Range")
    private String getUserEmailFromDatabase() {
        UsersHelper dbHelper = new UsersHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String userEmail = ""; // Default value if not found

        // Assuming you have a table named "Users" with columns "user_id" and "email"
        String query = "SELECT email FROM Users";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            userEmail = cursor.getString(cursor.getColumnIndex("email"));
        }

        cursor.close();
        db.close();

        return userEmail;
    }
    private void generateTransactionReport(String currentUserEmail) {
        // Retrieve all transactions from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Transactions", MODE_PRIVATE);

        LinearLayout transactionDetailsContainer = findViewById(R.id.transactionDetailsContainer);

        for (String key : preferences.getAll().keySet()) {
            if (key.endsWith("_user_email")) {
                String userEmail = preferences.getString(key, "");
                String productName = preferences.getString(key.replace("_user_email", "_product_name"), "");
                int quantity = preferences.getInt(key.replace("_user_email", "_quantity"), 0);
                int price = preferences.getInt(key.replace("_user_email", "_price"), 0);
                float totalprice = preferences.getFloat(key.replace("_user_email", "_total"), 0);

                // Inflate a layout for each transaction
                View transactionView = LayoutInflater.from(this).inflate(R.layout.item_transaction, null);

                // Set transaction details to TextViews
                TextView tvUserEmail = transactionView.findViewById(R.id.tvUserEmail);
                TextView tvProductName = transactionView.findViewById(R.id.tvProductName);
                TextView tvQuantity = transactionView.findViewById(R.id.tvQuantity);
                TextView tvPrice = transactionView.findViewById(R.id.tvPrice);
                TextView tvTotalPrice = transactionView.findViewById(R.id.tvTotalPrice);

                tvUserEmail.setText("User: " + userEmail);
                tvProductName.setText("Product: " + productName);
                tvQuantity.setText("Quantity: " + quantity);
                tvPrice.setText("Price: " + price);
                tvTotalPrice.setText("Total Price: " + totalprice);

                // Add the transaction view to the container
                transactionDetailsContainer.addView(transactionView);
            }
        }
    }

}