package com.example.mobileproject;// Inside the CartActivity
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class CartActivity extends AppCompatActivity {
    private LinearLayout cartContainer;
    private Button btnCalculateTotal;
    private TextView tvTotalPrice;
    UsersHelper helper = new UsersHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartContainer = findViewById(R.id.cartContainer);
        btnCalculateTotal = findViewById(R.id.btnCalculateTotal);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        displayCartItems();

        btnCalculateTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateTotalPrice();
            }
        });
    }

    private void displayCartItems() {
        SharedPreferences preferences = getSharedPreferences("Cart", MODE_PRIVATE);
        cartContainer.removeAllViews();

        for (String key : preferences.getAll().keySet()) {
            if (key.endsWith("_price")) {
                String productName = key.replace("_price", "");
                int quantity = preferences.getInt(key, 0);
                int productPrice = getProductPrice(productName);

                // Inflate cart_item.xml
                View cartItemView = getLayoutInflater().inflate(R.layout.cart_item, cartContainer, false);

                TextView tvCartItemName = cartItemView.findViewById(R.id.cartItemName);
                TextView tvCartItemPrice = cartItemView.findViewById(R.id.cartItemPrice);
                Button btnRemove = cartItemView.findViewById(R.id.btnRemove);
                Button btnIncrease = cartItemView.findViewById(R.id.btnIncrease);
                Button btnDecrease = cartItemView.findViewById(R.id.btnDecrease);
                tvCartItemName.setText(productName + " x" + quantity);
                tvCartItemPrice.setText("$" + (productPrice * quantity));

                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromCart(productName);
                        displayCartItems();
                    }
                });

                btnIncrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increaseQuantity(productName);
                        displayCartItems();
                    }
                });

                btnDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        decreaseQuantity(productName);
                        displayCartItems();
                    }
                });
                Button feedback=findViewById(R.id.feedback);
                feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int Productid = (int)helper.getIdByProductName(productName);
                        Intent feedbackIntent = new Intent(CartActivity.this, FeedbackActivity.class);
                        Intent Intent = new Intent(CartActivity.this, FeedbackList.class);
                        feedbackIntent.putExtra("PRODUCT_ID", Productid); // Pass the product ID
                        Intent.putExtra("PRODUCT_ID", Productid);

                        startActivity(feedbackIntent);
                    }
                });


                cartContainer.addView(cartItemView);
            }
        }
    }


    @SuppressLint("Range")
    private int getProductPrice(String productName) {
        UsersHelper dbHelper = new UsersHelper(this);

        // Add your logic to get the price of the selected product from the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT price FROM Products WHERE ProductName = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{productName})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("price"));
            } else {
                return 0; // Default price if product not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Default price on exception
        } finally {
            db.close();
        }
    }

    private void removeFromCart(String productName) {
        SharedPreferences preferences = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(productName + "_price");
        editor.apply();
    }

    private void increaseQuantity(String productName) {
        SharedPreferences preferences = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Get the current quantity from SharedPreferences
        int currentQuantity = preferences.getInt(productName + "_price", 0);

        // Fetch the limit for the product from the database
        int productLimit = getProductLimitFromDatabase(productName);

        // Check if the current quantity is less than the limit before increasing
        if (currentQuantity < productLimit) {
            // Increase the quantity
            currentQuantity++;
            editor.putInt(productName + "_price", currentQuantity);
            editor.apply();
            Toast.makeText(this, "Quantity increased to " + currentQuantity, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Quantity limit reached for " + productName, Toast.LENGTH_SHORT).show();
        }

    }



    private int getUserIdByEmail(String userEmail) {
        UsersHelper dbHelper = new UsersHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //int userId = 0;

        // Assuming you have a table named "Users" with columns "user_id" and "email"
        String query = "SELECT id FROM Users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});
        int columnIndex = cursor.getColumnIndex("user_id");
        if (cursor.moveToFirst()) {

            if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
                columnIndex = cursor.getInt(columnIndex);
            }
        }

        cursor.close();
        db.close();
        return columnIndex;
    }

    private void decreaseQuantity(String productName) {
        SharedPreferences preferences = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int quantity = preferences.getInt(productName + "_price", 0);
        if (quantity > 0) {
            quantity--;
            editor.putInt(productName + "_price", quantity);
            editor.apply();
        }
    }


    @SuppressLint("Range")
    public int getProductLimitFromDatabase(String productName) {
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        int limit = 0;


        String query = "SELECT quantity FROM Products WHERE ProductName = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productName});

        if (cursor.moveToFirst()) {
            limit = cursor.getInt(cursor.getColumnIndex("quantity"));
        }

        cursor.close();
        db.close();

        return limit;
    }

    // Retrieve user email from the database
    @SuppressLint("Range")
    private Set<String> getAllUserEmailsFromDatabase() {
        UsersHelper dbHelper = new UsersHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Set<String> userEmails = new HashSet<>(); // Store unique user emails

        // Assuming you have a table named "Users" with a column named "email"
        String query = "SELECT email FROM Users";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            userEmails.add(userEmail);
        }

        cursor.close();
        db.close();

        return userEmails;
    }
    private void calculateTotalPrice() {
        SharedPreferences preferences = getSharedPreferences("Cart", MODE_PRIVATE);

        // Iterate through all users to insert transactions
        Set<String> allUserEmails = getAllUserEmailsFromDatabase();
        if (!allUserEmails.isEmpty()) {
            SharedPreferences.Editor transactionEditor = getSharedPreferences("Transactions", MODE_PRIVATE).edit();

            for (String userEmail : allUserEmails) {
                float total = 0;

                for (String key : preferences.getAll().keySet()) {
                    if (key.endsWith("_price")) {
                        int quantity = preferences.getInt(key, 0);
                        String productName = key.replace("_price", "");
                        int productPrice = getProductPrice(productName);
                        float subtotal = quantity * productPrice;
                        total += subtotal;

                        // Insert transaction details into SharedPreferences
                        insertTransactionIntoSharedPreferences(transactionEditor, userEmail, productName, quantity, productPrice, subtotal);
                    }
                }

                // Update the total price TextView for each user
                updateTotalPriceTextView(total);
            }

            // Apply the changes to the transaction SharedPreferences
            transactionEditor.apply();
        } else {
            // Handle the case where no users are found
            Toast.makeText(this, "No users found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalPriceTextView(float total) {
        // Assuming tvTotalPrice is a TextView in your layout
        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
        if (tvTotalPrice != null) {
            tvTotalPrice.setText("Total Price: $" + String.format("%.2f", total));
        }
    }

    // Update the insertTransactionIntoSharedPreferences method
    private void insertTransactionIntoSharedPreferences(
            SharedPreferences.Editor editor, String userEmail, String productName, int quantity, int price, float total) {
        // Create a unique key for the transaction based on the current timestamp and user email
        String transactionKey = String.valueOf(System.currentTimeMillis()) + "_" + userEmail;

        // Store transaction details in SharedPreferences
        editor.putString(transactionKey + "_user_email", userEmail);
        editor.putString(transactionKey + "_product_name", productName);
        editor.putInt(transactionKey + "_quantity", quantity);
        editor.putInt(transactionKey + "_price", price);
        editor.putFloat(transactionKey + "_total", total);
    }
}
