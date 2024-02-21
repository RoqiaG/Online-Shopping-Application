package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AllCategories extends AppCompatActivity {
    ListView listAll ;
    public ArrayList<String> Categories = new ArrayList<>();
    protected ArrayAdapter myadapter ;
    ArrayAdapter<String> arrayAdapter;
    List<String> categories;
    UsersHelper helper = new UsersHelper(this);
    SQLiteDatabase db1;
    String newCategory ;
    Button so;
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
        UsersHelper usersHelper = new UsersHelper(this);
        categories = usersHelper.getCategories();
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
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listAll.setAdapter(arrayAdapter);
        so = findViewById(R.id.searchOptions);
        so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AllCategories.this, Search.class);
                startActivity(i);
            }
        });
        listAll.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = arrayAdapter.getItem(position);
            openNewActivity(selectedCategory);
        });
        Button addtocart=findViewById(R.id.gotocart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AllCategories.this, CartActivity.class);
                startActivity(i);
            }
        });

    }
    public void openNewActivity(String selectedCategory) {
        Intent intent;

        // Depending on the selected category, create the corresponding Intent
        switch (selectedCategory) {
            case "fashion clothing":
                intent = new Intent(AllCategories.this, Burgers.class);
                break;
            case "Electronics":
                intent = new Intent(AllCategories.this, Electronics.class);
                break;
            // Add more cases for each category as needed
            default:
                // If no specific category matches, you can provide a default behavior or show an error
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                intent = new Intent(AllCategories.this, AllCategories.class);
                break;
        }

        // Optionally, pass the selected category to the next activity if needed
        intent.putExtra("selectedCategory", selectedCategory);

        // Start the activity
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        getMenuInflater().inflate(R.menu.logoutmenu,menu);
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
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    SharedPreferences sp;
    SharedPreferences.Editor editor ;

   /* public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logoutmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }*/

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