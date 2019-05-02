package com.example.natethewizard.fms_server;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import model.ModelSingleton;

public class SettingsActivity extends AppCompatActivity {
    private TextView mResyncTextView;
    private TextView mLogoutTextView;
    private Spinner mSpinner;
    private ModelSingleton singleton = ModelSingleton.getINSTANCE();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSpinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.map_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMapType = (String) parent.getItemAtPosition(position);
                selectedMapType = selectedMapType.toUpperCase();

                if (selectedMapType.equals("NORMAL")) {
                    singleton.setMapType(1);
                }
                else if (selectedMapType.equals("HYBRID")) {
                    singleton.setMapType(4);
                }
                else if (selectedMapType.equals("SATELLITE")) {
                    singleton.setMapType(2);
                }
                else if (selectedMapType.equals("TERRAIN")) {
                    singleton.setMapType(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Leave blank
            }
        });

        mLogoutTextView = findViewById(R.id.logout_select);

        mLogoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton = null;
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        });

        mResyncTextView = findViewById(R.id.data_sync);

        mResyncTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Context
            }
        });
    }
}
