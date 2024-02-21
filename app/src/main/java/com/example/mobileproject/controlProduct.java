package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class controlProduct extends AppCompatActivity {


    private EditText categoryNameEditText;
    private EditText productNameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;

    private Button addProductButton;
    private Button editProductButton;
    private Button deleteProductButton;
    private Button checkProductButton;

    boolean check = false;
    int oldProductid;
    UsersHelper helper = new UsersHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_product);

        addProductButton = findViewById(R.id.addProductBtn);
        categoryNameEditText = findViewById(R.id.Productcategoryedittxt);
        productNameEditText = findViewById(R.id.edittxtproductName);
        priceEditText = findViewById(R.id.edittxtprice);
        quantityEditText = findViewById(R.id.quantityedittxt);



        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameEditText.getText().toString().trim();
                String categoryName = categoryNameEditText.getText().toString().trim();

                int price =  Integer.valueOf(priceEditText.getText().toString().trim());
                int quantity =  Integer.valueOf(priceEditText.getText().toString().trim());

                addToDb(categoryName,productName,price ,quantity);
                Toast.makeText(getApplicationContext(), "product added: " + productName, Toast.LENGTH_SHORT).show();
                categoryNameEditText.setText("");
                productNameEditText.setText("");
                priceEditText.setText("");
                quantityEditText.setText("");
            }
        });

        deleteProductButton = findViewById(R.id.deleteProductBtn);
        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = productNameEditText.getText().toString().trim();

                deleteProduct(productName);
                Toast.makeText(getApplicationContext(), "Product deleted: " + productName, Toast.LENGTH_SHORT).show();
                productNameEditText.setText("");
            }
        });

        checkProductButton = findViewById(R.id.checkProduct);
        checkProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = productNameEditText.getText().toString().trim();

                if( helper.checkProduct(productName)){
                    check =true; // true state
                     oldProductid = (int)helper.getIdByProductName(productName);
                }else{
                    Toast.makeText(getApplicationContext(), "no product with this name", Toast.LENGTH_SHORT).show();

                }
            }
        });

        editProductButton = findViewById(R.id.editProductBtn);
        editProductButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String productName =""; //= productNameEditText.getText().toString().trim();
                String categoryName  ="";// = categoryNameEditText.getText().toString().trim();

                int price =0;// =  Integer.valueOf(priceEditText.getText().toString().trim());
                int quantity =0;//  Integer.valueOf(quantityEditText.getText().toString().trim());

                ContentValues values = new ContentValues();

                if (!TextUtils.isEmpty(categoryNameEditText.getText().toString())) {
                    categoryName=categoryNameEditText.getText().toString().trim();
                    values.put("categoryName", categoryName);
                }
                if (!TextUtils.isEmpty(productNameEditText.getText().toString())) {
                    productName=productNameEditText.getText().toString().trim();
                    values.put("ProductName", productName);
                }
                if (!TextUtils.isEmpty(priceEditText.getText().toString())) {
                    price =  Integer.valueOf(priceEditText.getText().toString().trim());
                   if (price>0)values.put("price", price);
                }
                if (!TextUtils.isEmpty(quantityEditText.getText().toString())) {
                    quantity =  Integer.valueOf(quantityEditText.getText().toString().trim());
                    if (quantity>0)values.put("quantity", quantity);
                }

               if(check==true) {
                   int rowsUpdated=  updateProduct(values ,oldProductid);

                   if (rowsUpdated > 0) {

                   // The row was updated
                   Toast.makeText(getApplicationContext(), "successful update", Toast.LENGTH_SHORT).show();
                   categoryNameEditText.setText("");
                   productNameEditText.setText("");
                   priceEditText.setText("");
                   quantityEditText.setText("");
                   check =false;
               }else {
                   // The row was not updated
                   Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_SHORT).show();
               }
               }else {
                   Toast.makeText(getApplicationContext(), "check if the product exist first ", Toast.LENGTH_SHORT).show();

               }


            }
        });

    }

    public void addToDb(String categoryName,String productName ,int price , int quantity){
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db2 = helper.getWritableDatabase() ;
        if(helper.checkProduct(productName)==false) {
            ContentValues values = new ContentValues();
            values.put("categoryName", categoryName);
            values.put("ProductName", productName);
            values.put("price", price);
            values.put("quantity", quantity);

            db2.insert("Products", null, values);
        }
        else{
            Toast.makeText(getApplicationContext(), "Product" + productName +"already exist", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteProduct(String name) {
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(helper.checkProduct(name)) {
            db.delete("Products", "productName" + " = ?", new String[]{name});
            db.close();
        }
        else{
            Toast.makeText(getApplicationContext(), "Product " + name +" not existed", Toast.LENGTH_SHORT).show();
        }
    }

    public int updateProduct( ContentValues values, int oldId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();

        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(oldId)};
        int rowsUpdated = db.update("Products", values, whereClause, whereArgs);
        db.close();
        return rowsUpdated;
    }
}