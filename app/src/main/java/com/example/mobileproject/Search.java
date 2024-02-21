package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    //--
    UsersHelper usersHelper;
    List<String> categories = new ArrayList<String>();


    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String[] results;

    Button voice;
    Button barcode;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = findViewById(R.id.listView);
        voice = (Button) findViewById(R.id.voice);
        barcode = (Button) findViewById(R.id.barcode);

        // Initialize usersHelper and retrieve categories
        usersHelper = new UsersHelper(this);
        categories = usersHelper.getCategories();

        // Convert List<String> to String[]
        results = categories.toArray(new String[0]);

        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_customtest, results);
        listView.setAdapter(arrayAdapter);
        TextView t = findViewById(R.id.listCustomText);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = arrayAdapter.getItem(position);
            openNewActivity(selectedCategory);
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceRecognition();
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, BarcodeScannerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startVoiceRecognition() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");

            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Search.this, "Speech recognition not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String query = result.get(0);

                // Check if the query exists in the categories list
                boolean isFound = false;
                for (String category : categories) {
                    if (category.toLowerCase().contains(query.toLowerCase())) {
                        isFound = true;
                        break;
                    }
                }

                if (isFound) {
                    arrayAdapter.getFilter().filter(query);
                } else {
                    // Show the toast if the query is not found
                    Toast.makeText(this, "Error 404: Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void openNewActivity(String selectedCategory) {
        Intent intent;

        // Depending on the selected category, create the corresponding Intent
        switch (selectedCategory) {
            case "fashion clothing":
                intent = new Intent(Search.this, Burgers.class);
                break;
            case "Electronics":
                intent = new Intent(Search.this, Electronics.class);
                break;
            // Add more cases for each category as needed
            default:
                // If no specific category matches, you can provide a default behavior or show an error
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                intent = new Intent(Search.this, AllCategories.class);
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

    //-----------Barcode-----------
    /*private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }*/

    private void handleBarcodeResult(String scannedBarcode) {
        // Implement logic to search for products using the scanned barcode
        // You may use the scanned barcode to query your database or perform any other relevant actions
        // Update the categories list or show a toast message based on the result
        // For now, let's display a toast message with the scanned barcode
        Toast.makeText(this, "Scanned Barcode: " + scannedBarcode, Toast.LENGTH_SHORT).show();
    }

}