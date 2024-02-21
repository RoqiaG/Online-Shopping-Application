package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Resetpassword extends AppCompatActivity {

    //TextView email;
    EditText password , re_password;
    Button confirmButton;
    String pass, re_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);


        //email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.passwordReseteditTxt);
        re_password = findViewById(R.id.retypeEdittxt);
        confirmButton = findViewById(R.id.confirmedBtn);

        Intent intent =getIntent();
        String email = intent.getStringExtra("email");

        // Debugging: Print email to check if it's not null
        System.out.println("Email received: " + email);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = password.getText().toString();
                re_pass = re_password.getText().toString();
                if(pass.equals(re_pass)){
                    if(updatePassword(email,pass)==1){
                        Toast.makeText(getApplicationContext(), "successful update", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Resetpassword.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "can not update", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "password does not match", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public  int updatePassword(String email,String pass){
        UsersHelper helper = new UsersHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", pass);

        // Debugging: Print values for debugging
        System.out.println("Email: " + email);
        System.out.println("Password: " + pass);

        String whereClause = "email = ?";
        String[] whereArgs = new String[]{email};  // Pass the email directly
        int rowsUpdated = db.update("users", values, whereClause, whereArgs);
        db.close();
        return rowsUpdated;
    }
}