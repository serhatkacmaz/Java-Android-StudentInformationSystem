package com.example.yazilimlab.Catogery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.yazilimlab.R;

import java.util.ArrayList;

public class CapActivity extends AppCompatActivity {

    // ComboBox for Ogretim turu
    private AutoCompleteTextView educationCapTypeDropDown;
    ArrayList<String> arrayListCapEducationType;
    ArrayAdapter<String> arrayAdapterCapEducationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap);
        // ComboBox for Ogretim turu
        educationCapTypeDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteCapEducationType);
        arrayListCapEducationType = new ArrayList<>();
        arrayListCapEducationType.add("I.ÖĞRETİM");
        arrayListCapEducationType.add("II.ÖĞRETİM");
        arrayAdapterCapEducationType = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListCapEducationType);
        educationCapTypeDropDown.setAdapter(arrayAdapterCapEducationType);
    }
}