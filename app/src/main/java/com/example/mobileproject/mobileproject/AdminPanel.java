package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    ListView optionsPanel ;
    public ArrayList<String> options = new ArrayList<>();
    protected ArrayAdapter myadapter ;
    ListView feedbackListView;
    ArrayAdapter<String> feedbackAdapter;
    ArrayList<String> feedbackList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        optionsPanel = findViewById(R.id.admibPanel);
        options.add("Control Categories");
        options.add("Control Products");
        options.add("transactions report");
        options.add("Feedback from users");
        options.add("chart of best selling");
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,options);
        optionsPanel.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();

        optionsPanel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position==0){
                    startActivity(new Intent(AdminPanel.this, controlCategories.class));
                }
               else if (position==1){
                    startActivity(new Intent(AdminPanel.this, controlProduct.class));
                }
                else if (position == 2) {
                    startActivity(new Intent(AdminPanel.this, TransactionReport.class));
                }
                else if (position == 3) {
                    //displayFeedbackList();
                    startActivity(new Intent(AdminPanel.this, FeedbackList.class));

                }
                else if (position == 4) {
                    //displayFeedbackList();
                    startActivity(new Intent(AdminPanel.this,chart_best_selling.class));

                }
            }
        });



    }
   /* private void displayFeedbackList() {
        // Fetch feedback data and update the ListView
        feedbackList.clear();
        feedbackList.addAll(getFeedbackFromDatabase());
        feedbackAdapter.notifyDataSetChanged();
    }*/
   /* private ArrayList<String> getFeedbackFromDatabase() {
        ArrayList<String> feedbackData = new ArrayList<>();

        UsersHelper dbHelper = new UsersHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Feedback";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex("Productid"));
            String productName = getProductNameFromProductId(productId);
            @SuppressLint("Range") String feedbackText = cursor.getString(cursor.getColumnIndex("Feedback"));
            @SuppressLint("Range") int rating = cursor.getInt(cursor.getColumnIndex("rate"));

            String feedbackInfo = "Product: " + productName + "\nFeedback: " + feedbackText + "\nRating: " + rating;
            feedbackData.add(feedbackInfo);
        }

        cursor.close();
        db.close();

        return feedbackData;
    }
   /* @SuppressLint("Range")
    private String getProductNameFromProductId(long productId) {
        UsersHelper dbHelper = new UsersHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String productName = "Unknown"; // Default value if not found

        try {
            // Assuming you have a table named "Products" with columns "id" and "ProductName"
            String query = "SELECT ProductName FROM Products WHERE id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

            if (cursor.moveToFirst()) {
                productName = cursor.getString(cursor.getColumnIndex("ProductName"));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return productName;
    }*/
}