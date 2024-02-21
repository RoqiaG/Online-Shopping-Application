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

public class Electronics extends AppCompatActivity {

    ListView electronicsList;
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
        electronicsList.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();

        electronicsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position==0){
                    startActivity(new Intent(Electronics.this, Burgers.class));
                }
                else if(position ==1){
                    startActivity(new Intent(Electronics.this, Electronics.class));

                }
            }
        });
//        db1.close();
    }

}