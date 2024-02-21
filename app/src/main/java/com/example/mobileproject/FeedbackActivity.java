package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {
    private long productId; // Assuming you pass the product ID to this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Retrieve the product ID passed from the previous activity
        Intent intent = getIntent();

        productId = intent.getLongExtra("PRODUCT_ID", -1);
        System.out.println(productId);
        // Initialize UI elements
        EditText feedbackEditText = findViewById(R.id.feedbackEditText);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        // Retrieve and display existing feedback if available
        retrieveAndDisplayFeedback(productId, feedbackEditText, ratingBar);

        // Set a click listener for the submit button
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get feedback text and rating
                String feedbackText = feedbackEditText.getText().toString();
                int rating = (int) ratingBar.getRating();

                // Insert or update feedback in the database
                insertOrUpdateFeedback(productId, feedbackText, rating);

                // Optionally, you can notify the user that feedback has been submitted.
                Toast.makeText(FeedbackActivity.this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Add this method to retrieve and display feedback
    private void retrieveAndDisplayFeedback(long productId, EditText feedbackEditText, RatingBar ratingBar) {
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();


        String query = "SELECT * FROM Feedback WHERE Productid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String feedbackText = cursor.getString(cursor.getColumnIndex("Feedback"));
            @SuppressLint("Range") int rating = cursor.getInt(cursor.getColumnIndex("rate"));

            feedbackEditText.setText(feedbackText);
            ratingBar.setRating(rating);
        } else {

            ratingBar.setRating(0);
            Toast.makeText(this, "No feedback available for this product.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }


    // Add this method to insert or update feedback in the database
    private void insertOrUpdateFeedback(long productId, String feedbackText, int rating) {
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Productid", productId);
        values.put("Feedback", feedbackText);
        values.put("rate", rating);

        // Use SQLiteDatabase insertWithOnConflict to handle both insert and update scenarios
        long result = db.insertWithOnConflict("Feedback", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }




}







