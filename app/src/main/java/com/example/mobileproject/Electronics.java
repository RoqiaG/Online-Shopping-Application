package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Electronics extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    List<String> products;

    ListView electronicsList;
    Button btnAddToCart;
    public ArrayList<String> electronics = new ArrayList<>();
    protected ArrayAdapter myadapter ;
    UsersHelper helper = new UsersHelper(this);

    SQLiteDatabase db1;

    String newCategory ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronics);
        //setContentView(R.layout.activity_all_categories);

        //db1 = helper.getReadableDatabase();
       // Cursor c=db1.rawQuery("SELECT * FROM Products WHERE categoryName = Electronics",null);

       //Cursor c = db1.rawQuery("SELECT ProductName FROM Products WHERE categoryName = 'Electronics' ", null);
        try (SQLiteDatabase db1 = helper.getReadableDatabase()) {
            Cursor c = db1.rawQuery("SELECT * FROM Products WHERE categoryName = 'Electronics'", null);
            // Process the cursor
            c.moveToFirst();
            while(!c.isAfterLast())
            {
                @SuppressLint("Range") String electronic = c.getString(c.getColumnIndex("ProductName"));
                // Check if the category is not empty before adding to the list
                if (!electronic.isEmpty()) {
                    electronics.add(electronic);
                }
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,electronics);
        electronicsList = findViewById(R.id.electronicsListView);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        electronicsList.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition = electronicsList.getCheckedItemPosition();
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String selectedProduct = electronics.get(selectedItemPosition);
                    int productPrice = getProductPrice(selectedProduct); // This retrieves the product price from the database
                    addToCart(selectedProduct, productPrice);
                    Toast.makeText(Electronics.this, "Added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Electronics.this, "Please select a product", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myadapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
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




    private void addToCart(String selectedProduct, int productPrice) {
        SharedPreferences preferences = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String key = selectedProduct + "_price";
        int quantity = preferences.getInt(key, 0); // Get the current quantity
        quantity++; // Increase quantity
        editor.putInt(key, quantity);

        editor.apply();
    }

}

