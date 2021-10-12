package com.example.yazilimlab.Catogery;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yazilimlab.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class YazOkuluActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    private static final int CREATEPDF = 1;

    private int tableRow = 3;

    // yaz okulunda alıncak
    ArrayList<String> takeLessonList;
    private EditText editYazOkuluTakeFaculty, editYazOkuluTakeLessonName, editYazOkuluTakeLessonT, editYazOkuluTakeLessonU, editYazOkuluTakeLessonL, editYazOkuluTakeLessonAKTS;
    private String strTakeFaculty, strTakeLessonName, strTakeLessonT, strTakeLessonU, strTakeLessonL, strTakeLessonAKTS;

    // date
    DatePickerDialog.OnDateSetListener onDateSetListenerStart, onDateSetListenerFinish;
    private TextView editYazOkuluSchoolDateStart, editYazOkuluSchoolDateFinish;

    private void init() {

        //yaz okulunda alıncak
        takeLessonList = new ArrayList<String>();
        editYazOkuluTakeFaculty = (EditText) findViewById(R.id.editYazOkuluTakeFaculty);
        editYazOkuluTakeLessonName = (EditText) findViewById(R.id.editYazOkuluTakeLessonName);
        editYazOkuluTakeLessonT = (EditText) findViewById(R.id.editYazOkuluTakeLessonT);
        editYazOkuluTakeLessonU = (EditText) findViewById(R.id.editYazOkuluTakeLessonU);
        editYazOkuluTakeLessonL = (EditText) findViewById(R.id.editYazOkuluTakeLessonL);
        editYazOkuluTakeLessonAKTS = (EditText) findViewById(R.id.editYazOkuluTakeLessonAKTS);

        // date
        editYazOkuluSchoolDateStart = (TextView) findViewById(R.id.editYazOkuluSchoolDateStart);
        editYazOkuluSchoolDateFinish = (TextView) findViewById(R.id.editYazOkuluSchoolDateFinish);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        // date baslangıc
        editYazOkuluSchoolDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(YazOkuluActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListenerStart, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                System.out.println(date);
                editYazOkuluSchoolDateStart.setText(date);
            }
        };

        // date bitis
        editYazOkuluSchoolDateFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(YazOkuluActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListenerFinish, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListenerFinish = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                System.out.println(date);
                editYazOkuluSchoolDateFinish.setText(date);
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaz_okulu);
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

                canvas.drawText("T.C.", pageInfo.getPageWidth() / 2, 20, paint);
                canvas.drawText("KOCAELİ ÜNİVERSİTESİ", pageInfo.getPageWidth() / 2, 26, paint);
                canvas.drawText("TEKNOLOJİ FAKÜLTESİ", pageInfo.getPageWidth() / 2, 32, paint);
                canvas.drawText("BİLİŞİM SİSTEMLERİ MÜHENDİSLİĞİ BÖLÜM BAŞKANLIĞINA", pageInfo.getPageWidth() / 2, 38, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(3);
                paint.setFakeBoldText(false);

                canvas.drawText("       Teknoloji Fakültesi Bilişim Sistemleri Mühendisliği Bölümü .................................numaralı", 30, 45, paint);
                canvas.drawText("……………………………………. isimli öğrencisiyim.", 30, 50, paint);
                canvas.drawText("       20…/20… Eğitim Öğretim yılı yaz öğretimi kapsamında aşağıda bilgilerini verdiğim", 30, 55, paint);
                canvas.drawText("ders/dersleri almak istiyorum. Kontrol listesinde belirtilen adımları tamamladım. ", 30, 60, paint);
                canvas.drawText("       Gereği için arz ederim.", 30, 65, paint);
                canvas.drawText("imza", 165, 70, paint);


                //table1 row
                canvas.drawLine(30, 85, 180, 85, paint);
                canvas.drawLine(30, 90, 180, 90, paint);
                canvas.drawLine(30, 95, 180, 95, paint);
                canvas.drawLine(30, 100, 180, 100, paint);
                canvas.drawLine(30, 105, 180, 105, paint);
                canvas.drawLine(30, 110, 180, 110, paint);
                canvas.drawLine(30, 115, 180, 115, paint);
                //table1row context
                canvas.drawText("Öğrenci E-mail", 35, 89, paint);
                canvas.drawText("Öğrenci Adres", 35, 94, paint);
                canvas.drawText("Öğrenci Gsm ", 35, 99, paint);
                canvas.drawText("Öğrenci Danışmanı Adı Soyadı", 35, 104, paint);
                canvas.drawText("Yaz okulu için Başvurulan Üniversite", 35, 109, paint);
                canvas.drawText("Yaz okulu başlama-bitiş tarihleri", 35, 114, paint);
                //table1 column
                canvas.drawLine(30, 85, 30, 115, paint);
                canvas.drawLine(93, 85, 93, 115, paint);
                canvas.drawLine(180, 85, 180, 115, paint);


                canvas.drawText("   Yaz öğretimi kapsamında alınacak ders/dersler", 30, 129, paint);
                //table3 row
                canvas.drawLine(30, 130, 180, 130, paint);
                canvas.drawLine(30, 135, 180, 135, paint);
                canvas.drawLine(30, 140, 180, 140, paint);
                canvas.drawLine(30, 145, 180, 145, paint);
                canvas.drawLine(30, 150, 180, 150, paint);
                //table3 context
                canvas.drawText("Fakülte/Bölüm", 40, 134, paint);
                canvas.drawText("Dersin Adı ve Kodu", 100, 134, paint);
                canvas.drawText("T", 144, 134, paint);
                canvas.drawText("U", 154, 134, paint);
                canvas.drawText("L", 164, 134, paint);
                canvas.drawText("AKTS", 172, 134, paint);
                //table3 column
                canvas.drawLine(30, 130, 30, 150, paint);
                canvas.drawLine(70, 130, 70, 150, paint);
                canvas.drawLine(140, 130, 140, 150, paint);
                canvas.drawLine(150, 130, 150, 150, paint);
                canvas.drawLine(160, 130, 160, 150, paint);
                canvas.drawLine(170, 130, 170, 150, paint);
                canvas.drawLine(180, 130, 180, 150, paint);


                paint.setFakeBoldText(true);
                canvas.drawText("   Dilekçe Ekleri", 30, 159, paint);
                paint.setFakeBoldText(false);
                paint.setTextSize(2);
                canvas.drawText("       1-\tYaz döneminde ders almak istenilen Üniversite ve Kocaeli Üniversitesinin ilgili bölümlerinin, öğrencinin üniversiteye giriş yılındaki taban puanlarını gösteren belge ektedir.", 30, 164, paint);
                canvas.drawText("       2-\tAlınmak istenilen derslerin karşı Üniversitedeki ders saati/kredi/AKTS ve ders içeriklerini gösteren belge ektedir", 30, 167, paint);
                canvas.drawText("       3-\tBaşvurulan dönem içinde alınmış transkript ektedir.", 30, 170, paint);


                paint.setTextSize(3);
                //table4 row
                canvas.drawLine(30, 175, 180, 175, paint);
                canvas.drawLine(30, 180, 180, 180, paint);
                canvas.drawLine(30, 185, 180, 185, paint);
                canvas.drawLine(30, 190, 180, 190, paint);
                //table4 context
                canvas.drawText("Unvan, Adı, Soyadı", 82, 179, paint);
                canvas.drawText("İmza", 163, 179, paint);
                canvas.drawText("ÖĞRENCİ DANIŞMANI", 35, 184, paint);
                canvas.drawText("BÖLÜM BAŞKANI", 35, 189, paint);
                //table4 column
                canvas.drawLine(30, 175, 30, 190, paint);
                canvas.drawLine(80, 175, 80, 190, paint);
                canvas.drawLine(150, 175, 150, 190, paint);
                canvas.drawLine(180, 175, 180, 190, paint);

                paint.setTextSize(2);
                paint.setFakeBoldText(true);
                paint.setTextAlign(Paint.Align.CENTER);

                canvas.drawText("KOCAELİ ÜNİVERSİTESİ", pageInfo.getPageWidth() / 2, 200, paint);
                canvas.drawText("YAZ ÖĞRETİMİ ESASLARI", pageInfo.getPageWidth() / 2, 203, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setFakeBoldText(false);
                canvas.drawText("ESAS l- (1) Spor Bilimleri Fakültesi dışındaki tüm akademik birimlerde okuyan öğrencilerin, diğer yükseköğretim kurumlarından yaz öğretiminde ders alabilmeleri ", 30, 210, paint);
                canvas.drawText("için; Üniversiteye giriş yılı esas olmak üzere ilgili bölüm/program taban puanın en fazla 40 puan düşük olma koşulu aranır.", 30, 213, paint);
                canvas.drawText(" (3) Yaz okulunda diğer yükseköğretim kurumlarından alınacak dersler için öğrencinin kayıtlı olduğu ilgili birimin Bölüm/Anabilim Dalı veya Program ", 30, 217, paint);
                canvas.drawText("Başkanlığının onayı gerekir. ", 30, 220, paint);
                canvas.drawText("(4) Yaz okulunda Bölüm/Anabilim Dalı veya Program Başkanlığı bu konudaki değerlendirmesini; ilgili bölüm müfredatındaki benzer içeriğe veya program ", 30, 223, paint);
                canvas.drawText("yeterliliğine sahip olmakla birlikte, ders(ler)in AKTS/kedi/saat değer(ler)inden herhangi birini dikkate alarak yapar. ", 30, 226, paint);
                canvas.drawText("ESAS 2- (l) Öğrenciler, yaz öğretiminde derslerin AKTS/kredi/saat değerlerine bakılmaksızın, en çok 3 ders alabilir. ", 30, 229, paint);
                canvas.drawText("ESAS 3- (l) Güz ve/veya bahar yarıyıllarında kayıt donduran öğrenciler, kayıt dondurdukları yarıyıllara ilişkin dersleri yaz öğretiminde alamazlar.", 30, 232, paint);
                canvas.drawText("ESAS 6  (l)Yaz öğretimi kapsamında alınan ders(ler)in notu genel not ortalamasına bakılmaksızın 4'lük sistem üzerinden 2'nin altında ise öğrenci bu ders(ler)den ", 30, 235, paint);
                canvas.drawText("başarısız sayılır.", 30, 238, paint);
                canvas.drawText("ESAS 9- (l) Yaz öğretimi aynı öğretim yılına ait üçüncü bir yarıyıl değildir. Yaz öğretiminde alınan ders(ler)in notları, öğrencilerin güz ve bahar yarıyıllarındaki ", 30, 241, paint);
                canvas.drawText("yarıyıl not ortalamalarını ve yarıyıllardaki derslerin koşul durumlarını etkilemez. Ancak, öğrencinin genel not ortalaması (GNO) hesaplanmasına dahil edilir", 30, 244, paint);


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
            Toast.makeText(this, "Başarıyla Olmuş PDF", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Dosya hatası bulunamadı", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Giriş ve çıkış hatası", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bilinmeyen hata" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //pdf start
    public void createPdf(View view) {
        initPdf("YazOkuluBasvuru");
    }

    //pdf end


    // yaz okulunda alıncak start
    private void setTextStringTakeLesson() {
        strTakeFaculty = editYazOkuluTakeFaculty.getText().toString();
        strTakeLessonName = editYazOkuluTakeLessonName.getText().toString();
        strTakeLessonT = editYazOkuluTakeLessonT.getText().toString();
        strTakeLessonU = editYazOkuluTakeLessonU.getText().toString();
        strTakeLessonL = editYazOkuluTakeLessonL.getText().toString();
        strTakeLessonAKTS = editYazOkuluTakeLessonAKTS.getText().toString();
    }

    private boolean isNotEmptyTakeLesson() {
        setTextStringTakeLesson();
        boolean result = TextUtils.isEmpty(strTakeFaculty) || TextUtils.isEmpty(strTakeLessonName) || TextUtils.isEmpty(strTakeLessonT) || TextUtils.isEmpty(strTakeLessonU) || TextUtils.isEmpty(strTakeLessonL) || TextUtils.isEmpty(strTakeLessonAKTS);

        if (result)
            return false;
        return true;
    }

    private void takeLessonTextDelete() {
        editYazOkuluTakeFaculty.setText("");
        editYazOkuluTakeLessonName.setText("");
        editYazOkuluTakeLessonT.setText("");
        editYazOkuluTakeLessonU.setText("");
        editYazOkuluTakeLessonL.setText("");
        editYazOkuluTakeLessonAKTS.setText("");
    }

    public void yazOkuluTakeLessonAdd(View view) {

        if (takeLessonList.size() < tableRow) {
            if (isNotEmptyTakeLesson()) {
                setTextStringTakeLesson();
                String temp = strTakeFaculty + ";" + strTakeLessonName + ";" + strTakeLessonT + ";" + strTakeLessonU + ";" + strTakeLessonL + ";" + strTakeLessonAKTS;
                takeLessonList.add(temp);

                for (int i = 0; i < takeLessonList.size(); i++) {
                    System.out.println(takeLessonList.get(i));
                }
                System.out.println("----------------------------------------------");
                takeLessonTextDelete();
            } else {
                Toast.makeText(YazOkuluActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(YazOkuluActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }


    }
    // yaz okulunda alıncak end

}