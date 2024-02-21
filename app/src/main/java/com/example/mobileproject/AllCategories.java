package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AllCategories extends AppCompatActivity {
    ListView listAll ;
    public ArrayList<String> Categories = new ArrayList<>();
    protected ArrayAdapter myadapter ;
    UsersHelper helper = new UsersHelper(this);
    SQLiteDatabase db1;
    String newCategory ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        //setContentView(R.layout.activity_all_categories);
        listAll = findViewById(R.id.categoryList);
        sp=getSharedPreferences("Data",MODE_PRIVATE);
        editor=sp.edit();

        db1 = helper.getReadableDatabase();
        Cursor c=db1.rawQuery("SELECT * FROM categories",null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            @SuppressLint("Range") String category = c.getString(c.getColumnIndex("category"));
            // Check if the category is not empty before adding to the list
            if (!category.isEmpty()) {
                Categories.add(category);
            }
            c.moveToNext();
        }
        c.close();

        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Categories);
        listAll.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();

        listAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position==0){
                    startActivity(new Intent(AllCategories.this, Burgers.class));
                }
                else if(position ==1){
                    startActivity(new Intent(AllCategories.this, Electronics.class));

                }
            }
        });




    }
    SharedPreferences sp;
    SharedPreferences.Editor editor ;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logoutmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId()==R.id.logout)
       {
           logout();
       }
        return super.onOptionsItemSelected(item);
    }

    private  void logout()
    {
        editor.clear();
        editor.apply();
        startActivity(new Intent(AllCategories.this, MainActivity.class));
    }
}