package com.example.yazilimlab.Catogery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yazilimlab.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class YatayGecisActivity extends AppCompatActivity {

    // ComboBox for Basvuru turu
    private AutoCompleteTextView makeApplicationTypeDropDown;
    ArrayList<String> arrayListMakeApplicationType;
    ArrayAdapter<String> arrayAdapterMakeApplicationType;

    // ComboBox for Ogretim turu
    private AutoCompleteTextView educationTypeDropDown;
    ArrayList<String> arrayListEducationType;
    ArrayAdapter<String> arrayAdapterEducationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yatay_gecis);

        // ComboBox for Basvuru turu
        makeApplicationTypeDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecisMakeApplicationType);
        arrayListMakeApplicationType = new ArrayList<>();
        arrayListMakeApplicationType.add("KURUMİÇİ YATAY GEÇİŞ BAŞVURUSU");
        arrayListMakeApplicationType.add("KURUMLARARASI YATAY GEÇİŞ BAŞVURUSU");
        arrayListMakeApplicationType.add("MER. YER. PUANIYLA YATAY GEÇİŞ BAŞVURUSU");
        arrayListMakeApplicationType.add("YURT DIŞI YATAY GEÇİŞ BAŞVURUSU");
        arrayAdapterMakeApplicationType = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListMakeApplicationType);
        makeApplicationTypeDropDown.setAdapter(arrayAdapterMakeApplicationType);

        // ComboBox for Ogretim turu
        educationTypeDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecisEducationType);
        arrayListEducationType = new ArrayList<>();
        arrayListEducationType.add("I.ÖĞRETİM");
        arrayListEducationType.add("II.ÖĞRETİM");
        arrayAdapterEducationType = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListEducationType);
        educationTypeDropDown.setAdapter(arrayAdapterEducationType);


    }
}