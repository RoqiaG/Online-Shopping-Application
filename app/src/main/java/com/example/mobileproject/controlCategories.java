package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class controlCategories extends AppCompatActivity {
    private ListView categories;
    public ArrayList<String> CategoriesIn ;
    private EditText categoryNameEditText;
    int isExist =0;
    int oldid;
    private Button addCategoryButton;
    private Button editCategoryButton;
    private Button deleteCategoryButton;
    private Button CheckCategoryButton;
    ListView listAll ;
    UsersHelper helper = new UsersHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_categories);
        addCategoryButton = findViewById(R.id.addCategoryBtn);
        categoryNameEditText = findViewById(R.id.categoryName);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameEditText.getText().toString().trim();
                addToDb(categoryName);
                Toast.makeText(getApplicationContext(), "Category added: " + categoryName, Toast.LENGTH_SHORT).show();
                categoryNameEditText.setText("");
            }
        });

        deleteCategoryButton = findViewById(R.id.deleteCategoryBtn);
        categoryNameEditText = findViewById(R.id.categoryName);
        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = categoryNameEditText.getText().toString().trim();
                deleteCategory(categoryName);
                Toast.makeText(getApplicationContext(), "Category deleted: " + categoryName, Toast.LENGTH_SHORT).show();
                categoryNameEditText.setText("");
            }
        });

        categoryNameEditText = findViewById(R.id.categoryName);
        editCategoryButton = findViewById(R.id.editCategoryBtn);
        CheckCategoryButton=findViewById(R.id.checkBtn);
        CheckCategoryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String categoryName = categoryNameEditText.getText().toString().trim();
                if( helper.checkCategories(categoryName)){
                    isExist = 1; // true state
                    oldid= (int) helper.getIdByCategoryName(categoryName);
                    Toast.makeText(getApplicationContext(), "it exist you can edit it", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "no category with this name", Toast.LENGTH_SHORT).show();

                }
            }
        });
        editCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExist ==1 ) {
                    String categoryName = categoryNameEditText.getText().toString().trim();
                    // categoryNameEditText.setText("");
                    int rows= updateCategory(categoryName ,oldid);
                    if(rows > 0) {
                        Toast.makeText(getApplicationContext(), "update new Name: " + categoryName, Toast.LENGTH_SHORT).show();
                        categoryNameEditText.setText("");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "check if the category exist first ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void addToDb(String categoryName){
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db2 = helper.getWritableDatabase() ;
        if(helper.checkCategories(categoryName)==false) {
            ContentValues values = new ContentValues();
            values.put("category", categoryName);
            db2.insert("categories", null, values);
        }
        else{
            Toast.makeText(getApplicationContext(), "Category " + categoryName+"already exist", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteCategory(String name) {
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(helper.checkCategories(name)) {
            db.delete("categories", "category" + " = ?", new String[]{name});
            db.close();
        }
        else{
            Toast.makeText(getApplicationContext(), "Category " + name +" not existed", Toast.LENGTH_SHORT).show();
        }
    }
    // Check if the category exists
    private void checkCategory(String categoryName) {
        UsersHelper helper = new UsersHelper(this);
        if (helper.checkCategories(categoryName)) {
            Toast.makeText(getApplicationContext(), "Category " + categoryName + " exists", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Category " + categoryName + " does not exist", Toast.LENGTH_SHORT).show();
        }
    }
    public int updateCategory(String categoryName , int idcategory){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", categoryName);
        String whereClause = "id = ?";
        String[] whereArgs = new String[] { String.valueOf(idcategory) };
        int rowsUpdated = db.update("categories", contentValues, whereClause, whereArgs);
        db.close();
        return rowsUpdated;
    }
}