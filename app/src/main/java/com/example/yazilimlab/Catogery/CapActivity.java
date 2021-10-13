package com.example.yazilimlab.Catogery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.MainActivity;
import com.example.yazilimlab.R;
import com.example.yazilimlab.RegisterActivity;
import com.example.yazilimlab.StudentHomeActivity;
import com.example.yazilimlab.StudentHomeFragment.MakeApplicationFragment;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CapActivity extends AppCompatActivity {

    // ComboBox for Ogretim turu
    private AutoCompleteTextView educationCapTypeDropDown, educationCapTypePassDropDown;
    ArrayList<String> arrayListCapEducationType;
    ArrayAdapter<String> arrayAdapterCapEducationType;


    // input
    private EditText editTextCapFaculty, editTextCapBranch;
    private String strEducationCapType, strEducationCapTypePass, strCapFaculty, strCapBranch;

    private static final int CREATEPDF = 1;

    private void init() {
        // ComboBox for Ogretim turu
        educationCapTypeDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteCapEducationType);
        educationCapTypePassDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteCapEducationTypePass);
        arrayListCapEducationType = new ArrayList<>();
        arrayListCapEducationType.add("I.Öğretim");
        arrayListCapEducationType.add("II.Öğretim");
        arrayAdapterCapEducationType = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListCapEducationType);
        educationCapTypeDropDown.setAdapter(arrayAdapterCapEducationType);
        educationCapTypePassDropDown.setAdapter(arrayAdapterCapEducationType);

        //editText
        editTextCapFaculty = (EditText) findViewById(R.id.editTextCapFaculty);
        editTextCapBranch = (EditText) findViewById(R.id.editTextCapBranch);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap);
        init();
    }

    //string degerleri atama
    private void setString() {
        strEducationCapType = educationCapTypeDropDown.getText().toString();
        strEducationCapTypePass = educationCapTypePassDropDown.getText().toString();
        strCapFaculty = editTextCapFaculty.getText().toString();
        strCapBranch = editTextCapBranch.getText().toString();
    }

    // input bos kontrolu
    private boolean isNotEmptyStrings() {
        setString();
        boolean result = TextUtils.isEmpty(strEducationCapType) || TextUtils.isEmpty(strCapFaculty) || TextUtils.isEmpty(strCapBranch) || TextUtils.isEmpty(strEducationCapTypePass);

        if (result)
            return false;
        return true;
    }

    // pdf Start
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
                canvas.drawText("TEKNOLOJİ FAKÜLTESİ", pageInfo.getPageWidth() / 2, 32, paint);
                canvas.drawText("BİLİŞİM SİSTEMLERİ MÜHENDİSLİĞİ BÖLÜM BAŞKANLIĞINA", pageInfo.getPageWidth() / 2, 38, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(3);
                paint.setFakeBoldText(false);

                canvas.drawText("       Mühendislik Fakültesi ……….……….........................……………. Bölümü " + strEducationCapType, 30, 45, paint);
                canvas.drawText("...........................................numaralı …………………………………......…. isimli öğrencisiyim.", 30, 50, paint);
                canvas.drawText("       Kocaeli Üniversitesi Ön Lisans ve Lisans Eğitim ve Öğretim Yönetmeliği’nin 43. maddesi", 30, 60, paint);
                canvas.drawText("uyarınca, " + strCapFaculty + " Fakülteniz " + strCapBranch + " Bölümü aşağıda belirtmiş", 30, 65, paint);
                canvas.drawText("olduğum " + strEducationCapTypePass + " Çift Anadal Programı (ÇAP) kapsamında öğrenim görme talebimin kabul", 30, 70, paint);
                canvas.drawText("edilmesini arz ederim.", 30, 75, paint);
                canvas.drawText("imza", 165, 80, paint);

                //table1 row
                canvas.drawLine(30, 90, 180, 90, paint);
                canvas.drawLine(30, 95, 180, 95, paint);
                canvas.drawLine(30, 100, 180, 100, paint);
                canvas.drawLine(30, 105, 180, 105, paint);
                canvas.drawLine(30, 110, 180, 110, paint);
                canvas.drawLine(30, 115, 180, 115, paint);
                canvas.drawLine(30, 130, 180, 130, paint);
                //table1 context
                canvas.drawText("Adı ve Soyadı", 33, 94, paint);
                canvas.drawText("Öğrenci No", 33, 99, paint);
                canvas.drawText("Bölümü-Sınıfı", 33, 104, paint);
                canvas.drawText("Cep Telefon No ", 33, 109, paint);
                canvas.drawText("E-posta Adresi", 33, 114, paint);
                canvas.drawText("Adresi", 33, 119, paint);
                //table1 column
                canvas.drawLine(30, 90, 30, 130, paint);
                canvas.drawLine(80, 90, 80, 130, paint);  // orta
                canvas.drawLine(180, 90, 180, 130, paint);

                paint.setFakeBoldText(true);
                canvas.drawText("Ekler", 33, 140, paint);
                paint.setFakeBoldText(false);
                canvas.drawText("EK-1\tTranskript ", 35, 144, paint);

                paint.setFakeBoldText(true);
                canvas.drawText("Çift Anadal Programı", 30, 155, paint);
                paint.setFakeBoldText(false);
                paint.setTextSize(2);
                canvas.drawText("MADDE 43 – (1) Bir bölümün öğrencileri, ön lisans/lisans öğrenimleri boyunca aynı fakülte, yüksekokul ve meslek", 30, 158, paint);
                canvas.drawText("yüksekokulu içinde veya dışında asıl bölümüne konu bakımından yakın olan başka bir lisans öğretimini aynı zamanda takip", 30, 161, paint);
                canvas.drawText("edebilir. Bununla ilgili esaslar Senato tarafından belirlenir.", 30, 164, paint);

                canvas.drawText("MADDE 6- (2) Başvuru anında anadal diploma programındaki GNO'su 4.00'lük not sisteminde en az 3.00 olan ve anadal", 30, 170, paint);
                canvas.drawText("MADDE 6- (2) Başvuru anında anadal diploma programındaki GNO'su 4.00'lük not sisteminde en az 3.00 olan ve anadal", 30, 173, paint);
                canvas.drawText("başvurabilir.", 30, 176, paint);

                canvas.drawText("c) Anadal diploma programındaki GNO'su en az 3.00 olan ancak anadal diploma programının ilgili sınıfında başarı sıralaması", 30, 182, paint);
                canvas.drawText("itibariyle en üst %20'sinde yer almayan öğrencilerden çift anadal yapılacak bölümün/programın ilgili yıldaki taban puanından az", 30, 185, paint);
                canvas.drawText("olmamak üzere puana sahip olanlar da ÇAP'a başvurabilirler.", 30, 188, paint);

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
            Toast.makeText(this, "Pdf oluşturuldu", Toast.LENGTH_LONG).show();
            startActivity(new Intent(CapActivity.this, StudentHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Dosya hatası bulunamadı", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Giriş ve çıkış hatası", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bilinmeyen hata" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void createPdf(View view) {
        if (isNotEmptyStrings()) {
            initPdf("CapBasvurusu");
        } else {
            Toast.makeText(CapActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
        }
    }
    // pdf End
}