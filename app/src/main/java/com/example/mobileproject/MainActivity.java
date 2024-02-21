package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor editor ;
    TextView forget_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUpBtn = (Button) findViewById(R.id.signup);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passwordEditText = (EditText) findViewById(R.id.pass);
        UsersHelper user = new UsersHelper(this);


        sp=getSharedPreferences("Data",MODE_PRIVATE);
        editor=sp.edit();
        boolean login =sp.getBoolean("islogged",false);
        if (login==true)
        {
            startActivity(new Intent(MainActivity.this,AllCategories.class));

        }

        forget_pass=findViewById(R.id.forgetpasswordTxtview);

        // UsersHelper user = new UsersHelper(this);
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), forgetPassword.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userEmail = emailEditText.getText().toString();
                String userPassword = passwordEditText.getText().toString();

                // Check if it's the admin login
                //boolean isAdmin = user.checkAdmin(userEmail, userPassword);
                //Log.d("Login", "isAdmin: " + isAdmin);
                if (isAdmin(userEmail,userPassword)) {
                    //Log.d("Login", "isAdmin: " + isAdmin);
                    Toast.makeText(getApplicationContext(), "Admin Login Success", Toast.LENGTH_SHORT).show();
                    Intent adminIntent = new Intent(MainActivity.this, AdminPanel.class);
                    startActivity(adminIntent);
                }
                else {
                    // Check regular user login
                    boolean isLogged = user.checkUser(userEmail, userPassword);
                    if (isLogged) {
                        CheckBox checked=(CheckBox) findViewById(R.id.rememberme);
                        /*Remember me*/
                        if(checked.isChecked()) {
                            editor.putString("email", userEmail);
                            editor.putString("password", userPassword);
                            editor.putBoolean("islogged", true);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, AllCategories.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, AllCategories.class);
                            startActivity(i);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Login Failed! Check your Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });

    }
    // Modify this method with actual database logic
    public boolean isAdmin(String email, String password) {
        UsersHelper usersHelper = new UsersHelper(this);
        return usersHelper.checkAdmin(email, password);
    }


}
