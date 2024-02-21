package com.example.mobileproject;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UsersHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Mobile.db";
    private static final int DATABASE_VERSION =14;
   // SQLiteDatabase db = this.getWritableDatabase() ;
    public UsersHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE users " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT not null, " +
                "password TEXT not null, " +
                "birthdate TEXT)";
        db.execSQL(createTable);

        String createAdminTable = "CREATE TABLE admins " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT not null, " +
                "password TEXT not null)";
        db.execSQL(createAdminTable);
        insertAdminCredentials(db, "admin@gmail.com", "abc123");

        String createCategoriesTable = "CREATE TABLE categories " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category TEXT not null )";
        db.execSQL(createCategoriesTable);
        insertCategory(db,"fashion clothing");
        insertCategory(db,"Electronics");

        String createProductsTable = "CREATE TABLE Products " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ProductName TEXT not null ," +
                "categoryName TEXT not null ,"+
                "price INTEGER not null , "+
                "quantity INTEGER not null," +
                "barcode TEXT )";
        db.execSQL(createProductsTable);
        insetProduct(db,"Electronics","mobile",3000,3);

        String createFeedbackTable = "CREATE TABLE Feedback " +
                "(Productid INTEGER PRIMARY KEY , " +
                "ProductName TEXT ," +
                "Feedback TEXT not null ," +
                "rate INTEGER not null)";
        db.execSQL(createFeedbackTable);

        String createTableTransactions = "CREATE TABLE Transactions ("
                + "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER not null, "
                + "product_name TEXT not null, "
                + "quantity INTEGER not null, "
                + "price REAL not null, "
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(createTableTransactions);



    }

    public void insertAdminCredentials(SQLiteDatabase db, String email, String password) {
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        db.insert("admins", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists admins");
        db.execSQL("drop table if exists categories");
        db.execSQL("drop table if exists Products");
        db.execSQL("drop table if exists Feedback");
        onCreate(db);
        //db.close();
    }

    public boolean insertData(String email, String password, String birthdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        cv.put("birthdate", birthdate);
        long result = db.insert("users", null, cv);

        if (result == -1) {
            return false;
        } else return true;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public boolean checkUser(String m, String p) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{m, p})) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean checkCategories(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM categories WHERE category = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{n})) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean checkProduct(String n) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM Products WHERE ProductName = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{n})) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
    public void insetProduct(SQLiteDatabase db, String categoryName,String productName ,int price , int quantity){
            ContentValues values = new ContentValues();
            values.put("categoryName", categoryName);
            values.put("ProductName", productName);
            values.put("price", price);
            values.put("quantity", quantity);

            db.insert("Products", null, values);
          //  db.close();
    }
    public void insertCategory(SQLiteDatabase db, String categoryName) {
        ContentValues values = new ContentValues();
        values.put("category",categoryName);
        db.insert("categories", null, values);
    }
    // method to check if the user is an admin
    public boolean checkAdmin(String email, String password) {
       SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM admins WHERE email = ? AND password = ?";

        try (Cursor cursor = db.rawQuery(selectQuery, new String[]{email, password})) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public long getIdByProductName(String product) {
        long id = -1;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Products WHERE ProductName = ?", new String[]{product});

        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }

        cursor.close();
        db.close();

        return id;
    }
    public long getIdByCategoryName(String category) {
        long id = -1;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM categories WHERE category = ?", new String[]{category});

        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }

        cursor.close();
        db.close();

        return id;
    }
    public List<String> getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> categoriesList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM categories", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                categoriesList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categoriesList;
    }


}
