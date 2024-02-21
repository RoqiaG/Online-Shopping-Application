package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    ListView optionsPanel ;
    public ArrayList<String> options = new ArrayList<>();
    protected ArrayAdapter myadapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        optionsPanel = findViewById(R.id.admibPanel);
        options.add("Control Categories");
        options.add("Control Products");
        options.add("transactions report");
        options.add("Feedback from users");
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,options);
        optionsPanel.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();

        optionsPanel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position==0){
                    startActivity(new Intent(AdminPanel.this, controlCategories.class));
                }
                if (position==1){
                    startActivity(new Intent(AdminPanel.this, controlProduct.class));
                }
            }
        });
    }
}