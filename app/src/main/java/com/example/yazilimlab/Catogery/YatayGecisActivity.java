package com.example.yazilimlab.Catogery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yazilimlab.R;
import com.google.android.material.textfield.TextInputLayout;

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

    // ComboBox for Disiplin durumu
    private AutoCompleteTextView disciplineTypeDropDown;
    ArrayList<String> arrayListDiscipline;
    ArrayAdapter<String> arrayAdapterDiscipline;

    // ComboBox for Ogretim turu2
    private AutoCompleteTextView educationTypeDropDown2;
    ArrayList<String> arrayListEducationType2;
    ArrayAdapter<String> arrayAdapterEducationType2;


    // ComboBox for puan türü1
    private AutoCompleteTextView scoreTypeDropDown1;
    ArrayList<String> arrayListScoreType1;
    ArrayAdapter<String> arrayAdapterScoreType1;

    // ComboBox for puan türü2
    private AutoCompleteTextView scoreTypeDropDown2;
    ArrayList<String> arrayListScoreType2;
    ArrayAdapter<String> arrayAdapterScoreType2;

    private TextInputLayout editTextYatayGecisNoWrap;
    private int visibility = 1;
    private int inVisibility = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yatay_gecis);

        editTextYatayGecisNoWrap = (TextInputLayout) findViewById(R.id.editTextYatayGecisNoWrap);
        // ComboBox for Basvuru turu
        makeApplicationTypeDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecisMakeApplicationType);
        arrayListMakeApplicationType = new ArrayList<>();
        arrayListMakeApplicationType.add("KURUMİÇİ YATAY GEÇİŞ BAŞVURUSU");
        arrayListMakeApplicationType.add("KURUMLARARASI YATAY GEÇİŞ BAŞVURUSU");
        arrayListMakeApplicationType.add("MER. YER. PUANIYLA YATAY GEÇİŞ BAŞVURUSU");
        arrayListMakeApplicationType.add("YURT DIŞI YATAY GEÇİŞ BAŞVURUSU");
        arrayAdapterMakeApplicationType = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListMakeApplicationType);
        makeApplicationTypeDropDown.setAdapter(arrayAdapterMakeApplicationType);

        // ComboBox for Basvuru turu click event
        makeApplicationTypeDropDown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println(makeApplicationTypeDropDown.getText().toString());
                if (makeApplicationTypeDropDown.getText().toString().equals("KURUMİÇİ YATAY GEÇİŞ BAŞVURUSU")) {
                    System.out.println("visible");
                    editTextYatayGecisNoWrap.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("invvisibl");
                    editTextYatayGecisNoWrap.setVisibility(View.GONE);
                }
            }
        });


        // ComboBox for Ogretim turu
        educationTypeDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecisEducationType);
        arrayListEducationType = new ArrayList<>();
        arrayListEducationType.add("I.ÖĞRETİM");
        arrayListEducationType.add("II.ÖĞRETİM");
        arrayAdapterEducationType = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListEducationType);
        educationTypeDropDown.setAdapter(arrayAdapterEducationType);

        // ComboBox for Disiplin durumu
        disciplineTypeDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecisDiscipline);
        arrayListDiscipline = new ArrayList<>();
        arrayListDiscipline.add("DİSİPLİN CEZASI ALDIM");
        arrayListDiscipline.add("DİSİPLİN CEZASI ALMADIM");
        arrayAdapterDiscipline = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListDiscipline);
        disciplineTypeDropDown.setAdapter(arrayAdapterDiscipline);

        // ComboBox for Ogretim turu 2
        educationTypeDropDown2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecisEducationType2);
        arrayListEducationType2 = new ArrayList<>();
        arrayListEducationType2.add("I.ÖĞRETİM");
        arrayListEducationType2.add("II.ÖĞRETİM");
        arrayAdapterEducationType2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListEducationType2);
        educationTypeDropDown2.setAdapter(arrayAdapterEducationType2);

        // ComboBox for puan türü1
        scoreTypeDropDown1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecispScoreType1);
        arrayListScoreType1 = new ArrayList<>();
        arrayListScoreType1.add("SAYISAL");
        arrayListScoreType1.add("SÖZEL");
        arrayListScoreType1.add("YABANCI DİL");
        arrayListScoreType1.add("EŞİT AĞIRLIK");
        arrayListScoreType1.add("TYT");
        arrayListScoreType1.add("ALES SÖZEL");
        arrayListScoreType1.add("ALES SAYISAL");
        arrayListScoreType1.add("ALES EŞİT AĞIRLIK");
        arrayAdapterScoreType1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListScoreType1);
        scoreTypeDropDown1.setAdapter(arrayAdapterScoreType1);

        // ComboBox for puan türü2
        scoreTypeDropDown2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteYatayGecispScoreType2);
        arrayListScoreType2 = new ArrayList<>();
        arrayListScoreType2.add("SAYISAL");
        arrayListScoreType2.add("SÖZEL");
        arrayListScoreType2.add("YABANCI DİL");
        arrayListScoreType2.add("EŞİT AĞIRLIK");
        arrayListScoreType2.add("TYT");
        arrayListScoreType2.add("ALES SÖZEL");
        arrayListScoreType2.add("ALES SAYISAL");
        arrayListScoreType2.add("ALES EŞİT AĞIRLIK");
        arrayAdapterScoreType2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListScoreType2);
        scoreTypeDropDown2.setAdapter(arrayAdapterScoreType2);

    }
}