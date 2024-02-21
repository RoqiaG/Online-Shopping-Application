package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class forgetPassword extends AppCompatActivity {

    boolean check = false;
    Button resetButton ;
    UsersHelper db = new UsersHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        resetButton = findViewById(R.id.resetBtn);
        EditText emailEdittxt = findViewById(R.id.editTextEmailAddress);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String email = emailEdittxt.getText().toString();
                if (db.checkEmail(email)){
                    check = true;
                    Intent intent = new Intent(getApplicationContext(),Resetpassword.class);
                    intent.putExtra("email",email);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(), "Email does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}