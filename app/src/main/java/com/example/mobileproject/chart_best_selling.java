package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class chart_best_selling extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_best_selling);
        Drawable image = getResources().getDrawable(R.drawable.bestselling);

        // Get a reference to the ImageView
        ImageView imageView = findViewById(R.id.bestSellingview);

        // Set the image to the ImageView
        imageView.setImageDrawable(image);
    }
}