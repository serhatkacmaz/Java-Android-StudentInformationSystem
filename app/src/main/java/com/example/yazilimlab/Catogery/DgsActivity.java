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
import android.widget.EditText;
import android.widget.Toast;

import com.example.yazilimlab.R;
import com.example.yazilimlab.StudentHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class DgsActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    private static final int CREATEPDF = 1;

    private EditText editTextDgsFaculty,editTextDgsBranch,editTextDgsNo;
    private String strEditTextDgsBranch,strEditTextDgsFaculty,strEditTextDgsNo;

    public void init(){
        editTextDgsBranch = (EditText) findViewById(R.id.editTextDgsBranch);
        editTextDgsFaculty = (EditText) findViewById(R.id.editTextDgsFaculty);
        editTextDgsNo = (EditText) findViewById(R.id.editTextDgsNo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dgs);
        init();
    }
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

                canvas.drawText("T.C.", pageInfo.getPageWidth() / 2, 36, paint);
                canvas.drawText("KOCAELİ ÜNİVERSİTESİ", pageInfo.getPageWidth() / 2, 42, paint);
                canvas.drawText("DİKEY GEÇİŞ BAŞVURU FORMU", pageInfo.getPageWidth() / 2, 48, paint);
                paint.setTextSize(3);
                paint.setFakeBoldText(true);
                canvas.drawText("(Öğrenci İşleri Daire Başkanlığına)", pageInfo.getPageWidth() / 2, 52, paint);
                paint.setFakeBoldText(false);

                paint.setTextAlign(Paint.Align.LEFT);

                canvas.drawText("       Üniversiteniz " + strEditTextDgsFaculty + " Fakültesi " + strEditTextDgsBranch, 39, 80, paint);
                canvas.drawText("Bölümüne Dikey Geçiş Sınavı ile yerleştirildim. Daha önce bitirmiş olduğum okuldaki", 39, 84, paint);
                canvas.drawText("transkriptim ve ders içerikleri ekte sunulmuştur.", 39, 88, paint);
                canvas.drawText("Gerekli ders muafiyetimin ve sınıf intibakımın yapılabilmesi için gereğini arz ederim. ", 39, 100, paint);
                canvas.drawText("Öğrenci Numarası: " + strEditTextDgsNo, 39, 115, paint);
                canvas.drawText("Tarih: ../../....", 140, 115, paint);
                canvas.drawText("Adı Soyadı", 140, 125, paint);
                canvas.drawText("İmza", 146, 129, paint);
                canvas.drawText("Telefon Numarası: ...........", 39, 120, paint);
                canvas.drawText("Adres: ...........", 39, 125, paint);
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
            Toast.makeText(this, "PDF oluşturuldu.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(DgsActivity.this, StudentHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Dosya hatası bulunamadı", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Giriş ve çıkış hatası", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bilinmeyen hata" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    //pdf start
    public void createDgsPdf(View view) {

        if (isNotEmptyString()) {
            initPdf("DgsBaşvuru");
        } else {
            Toast.makeText(DgsActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
        }
    }

    //pdf end


    // veriler
    private void setTextString() {
        strEditTextDgsBranch = editTextDgsBranch.getText().toString();
        strEditTextDgsFaculty = editTextDgsFaculty.getText().toString();
        strEditTextDgsNo = editTextDgsNo.getText().toString();
    }
    private boolean isNotEmptyString() {
        setTextString();
        boolean result = TextUtils.isEmpty(strEditTextDgsBranch) || TextUtils.isEmpty(strEditTextDgsFaculty) || TextUtils.isEmpty(strEditTextDgsNo);

        if (result)
            return false;
        return true;
    }
}