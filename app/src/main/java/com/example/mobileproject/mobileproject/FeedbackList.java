// FeedbackList.java
package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FeedbackList extends AppCompatActivity {

    ListView feedbackListView;
    ArrayAdapter<String> feedbackAdapter;
    ArrayList<String> feedbackList;
    TextView titleTextView;
    private long productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        titleTextView = findViewById(R.id.textViewFeedbackListTitle);
        feedbackListView = findViewById(R.id.feedbackListView);
        feedbackList = getFeedbackFromDatabase();
        feedbackAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feedbackList);
        feedbackListView.setAdapter(feedbackAdapter);
    }

    private ArrayList<String> getFeedbackFromDatabase() {
        ArrayList<String> feedbackData = new ArrayList<>();
        // Retrieve the product ID passed from the previous activity
        Intent intent = getIntent();
        productId = intent.getIntExtra("PRODUCT_ID", -1);
        UsersHelper dbHelper = new UsersHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Feedback";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("ProductName"));
            @SuppressLint("Range") String feedbackText = cursor.getString(cursor.getColumnIndex("Feedback"));
            @SuppressLint("Range") int rating = cursor.getInt(cursor.getColumnIndex("rate"));

            String feedbackInfo = "Product: " + productId  + "\nFeedback: " + feedbackText + "\nRating: " + rating;
            feedbackData.add(feedbackInfo);
        }

        cursor.close();
        db.close();

        return feedbackData;
    }
}
