package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signup = (Button) findViewById(R.id.signUpbtn);
        EditText email = (EditText) findViewById(R.id.emailsignup);
        EditText pass = (EditText) findViewById(R.id.passwordsignup);

        UsersHelper newUser = new UsersHelper(this);

        NumberPicker dayPicker = findViewById(R.id.dayPicker);
        NumberPicker monthPicker = findViewById(R.id.monthPicker);
        NumberPicker yearPicker = findViewById(R.id.yearPicker);

        // Set the range for day and month (adjust as needed)
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(1);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(1);

        // Set the range for the year (adjust as needed)
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(currentYear);
        yearPicker.setValue(2000);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of retrieving the selected date
                int day = dayPicker.getValue();
                int month = monthPicker.getValue();
                int year = yearPicker.getValue();

                String birthdate = day + "/" + month + "/" + year;

                String mail, passwd;
                mail = email.getText().toString();
                passwd = pass.getText().toString();

                if (mail.isEmpty() || passwd.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill in All Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (newUser.checkEmail(mail)) {
                        Toast.makeText(getApplicationContext(), "User Already Exists!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    boolean regSuccess = newUser.insertData(mail, passwd, birthdate);
                    if (regSuccess) {
                        Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Check your Credentials!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}