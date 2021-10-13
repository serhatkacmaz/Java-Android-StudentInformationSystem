package com.example.yazilimlab.Catogery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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
    private static final int CREATEPDF = 1;

    private void init() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yatay_gecis);
        init();

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
    }

    //pdf start
    public void initPdf(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATEPDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATEPDF) {
            if (data.getData() != null) {


                Uri uri = data.getData();


                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint s = new Paint();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(4);
                paint.setFakeBoldText(true);

                canvas.drawText("T.C.", pageInfo.getPageWidth() / 2, 20, paint);
                canvas.drawText("KOCAELİ ÜNİVERSİTESİ", pageInfo.getPageWidth() / 2, 26, paint);
                canvas.drawText("YATAY GEÇİŞ BAŞVURU FORMU", pageInfo.getPageWidth() / 2, 32, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(3);
                paint.setFakeBoldText(false);

                // text1
                paint.setFakeBoldText(true);
                canvas.drawText("I- BAŞVURU TÜRÜ", 30, 40, paint);
                paint.setFakeBoldText(false);
                canvas.drawText("KURUMİÇİ YATAY GEÇİŞ BAŞVURUSU", 30, 45, paint);
                canvas.drawText("MER. YER. PUANIYLA YATAY GEÇİŞ BAŞVURUSU", 30, 50, paint);
                canvas.drawText("KURUMLARARASI YATAY GEÇİŞ BAŞVURUSU ", 125, 45, paint);
                canvas.drawText("YURT DIŞI YATAY GEÇİŞ BAŞVURUSU", 125, 50, paint);

                //text2
                paint.setFakeBoldText(true);
                canvas.drawText("II- KİŞİSEL BİLGİLER", 30, 60, paint);
                paint.setFakeBoldText(false);
                canvas.drawText("ADI SOYADI:", 30, 65, paint);
                canvas.drawText("T.C. KİMLİK NO:", 30, 70, paint);
                canvas.drawText("DOĞUM TARİHİ:", 120, 70, paint);
                canvas.drawText("E-POSTA ADRESİ:", 30, 75, paint);
                canvas.drawText("TELEFON (GSM):", 30, 80, paint);
                canvas.drawText("TELEFON (EV/İŞ):", 120, 80, paint);
                canvas.drawText("DOĞUM TARİHİ:", 120, 70, paint);
                canvas.drawText("TEBLİGAT ADRES:", 30, 85, paint);

                //text3
                paint.setFakeBoldText(true);
                canvas.drawText("III- ÖĞRENİMİNE İLİŞKİN BİLGİLER", 30, 95, paint);
                paint.setFakeBoldText(false);
                canvas.drawText("HALEN KAYITLI OLDUĞU ÜNİVERSİTE:", 30, 100, paint);
                canvas.drawText("HALEN KAYITLI OLDUĞU FAKÜLTE / YÜKSEKOKUL:", 30, 105, paint);
                canvas.drawText("HALEN KAYITLI OLDUĞU BÖLÜM / PROGRAM:", 30, 110, paint);
                canvas.drawText("ÖĞRETİM TÜRÜ:", 30, 115, paint);
                canvas.drawText("SINIF/ YARIYIL:", 120, 115, paint);
                canvas.drawText("DİSİPLİN CEZASI ALIP ALMADIĞI:", 30, 120, paint);
                canvas.drawText("GENEL AKADEMİK BAŞARI NOT ORTALAMASI:", 30, 125, paint);
                canvas.drawText("ÖĞRENCİ NUMARASI (KOCAELİ  ÜNİVERSİTESİ ÖĞRENCİLERİ İÇİN):", 30, 130, paint);
                canvas.drawText("HALEN KAYITLI OLDUĞU YÜKSEKÖĞRETİM KURUMUNA YERLEŞTİRİLDİĞİ YIL:", 30, 135, paint);
                canvas.drawText("HALEN KAYITLI OLUNAN PROGRAMA YERLEŞTİRMEDE KULLANILAN PUAN TÜRÜ VE PUANI:", 30, 140, paint);
                canvas.drawText("ZORUNLU HAZIRLIK SINIFI BULUNAN PROGRAMLARA BAŞVURAN ADAYLAR İÇİN YABANCI DİL PUANI VE SINAV TÜRÜ:", 30, 145, paint);

                //text4
                paint.setFakeBoldText(true);
                canvas.drawText("IV – ADAYIN BAŞVURDUĞU YÜKSEKÖĞRETİM PROGRAMINA İLİŞKİN BİLGİLER", 30, 160, paint);
                paint.setFakeBoldText(false);
                canvas.drawText("FAKÜLTE / YÜKSEKOKUL/MYO. ADI:", 30, 165, paint);
                canvas.drawText("BÖLÜM / PROGRAM ADI:", 30, 170, paint);
                canvas.drawText("ÖĞRETİM TÜRÜ:", 30, 175, paint);
                canvas.drawText("BAŞVURULAN PROGRAMIN HALEN KAYITLI OLUNAN PROGRAMA YERLEŞTİRME YAPILDIĞI PUAN TÜRÜ VE PUANI:", 30, 180, paint);

                //text5
                canvas.drawText("Beyan ettiğim bilgilerin veya belgelerin gerçeğe aykırı olması veya daha önce yatay geçiş yapmış olmam ", 30, 190, paint);
                canvas.drawText("halinde hakkımda cezai işlemlerin yürütüleceğini ve kaydım yapılmış olsa dahi silineceğini bildiğimi kabul ", 30, 195, paint);
                canvas.drawText("ediyorum.", 30, 200, paint);

                canvas.drawText("Tarih:", 30, 200, paint);
                canvas.drawText("Adayın Adı Soyadı:", 130, 205, paint);
                canvas.drawText("İmzası:", 140, 210, paint);

                canvas.drawText("BU BÖLÜM ÜNİVERSİTE YETKİLİ BİRİMLERİNCE DOLDURULACAKTIR.", 30, 220, paint);
                canvas.drawText("BAŞVURUSU UYGUN DEĞİLDİR:", 30, 230, paint);
                canvas.drawText("BAŞVURUSU UYGUNDUR:", 120, 235, paint);

                paint.setFakeBoldText(true);
                canvas.drawText("Başvuruyu Alan Görevlinin:", 120, 245, paint);
                canvas.drawText("Adı Soyadı:", 120, 255, paint);
                canvas.drawText("Unvanı:", 120, 260, paint);
                canvas.drawText("İmza:", 120, 265, paint);
                canvas.drawText("Tarih:", 120, 270, paint);
                pdfDocument.finishPage(page);
                setPdf(uri, pdfDocument);
            }
        }
    }

    private void setPdf(Uri uri, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(Objects.requireNonNull(getContentResolver().openOutputStream(uri)));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "Pdf oluşturuldu.\n", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Dosya hatası bulunamadı", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Giriş ve çıkış hatası", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bilinmeyen hata" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void createPdf(View view) {
        initPdf("YatayGecisBasvurusu");
    }

    //pdf end
}