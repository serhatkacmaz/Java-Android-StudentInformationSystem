package com.example.yazilimlab.Catogery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yazilimlab.Model.CustomDialog;
import com.example.yazilimlab.Model.UsersData;
import com.example.yazilimlab.R;
import com.example.yazilimlab.StudentHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class YazOkuluActivity extends AppCompatActivity {

    private ArrayList<Uri> fileUriList;
    private ArrayList<String> fileType;

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    UsersData usersData;


    // hashMap
    private HashMap<String, String> resourcesAdd;

    // path
    private String transcriptPath, lessonPath, subScorePath, petitionPath;

    // Uri
    private Uri transcriptUri, lessonUri, subScoreUri, pdfUri, petitionUri;

    // image file state
    private ImageView image_yazOkul_fileStateTranscript, image_yazOkul_fileStateLesson, image_yazOkul_fileStateSubScore;
    private TextView textView_yazOkul_fileStateTranscript, textView_yazOkul_fileStateLesson, textView_yazOkul_fileStateSubScore;

    // code
    private static final int CREATE_PDF = 1;
    private static final int PICK_FILE = 1;

    // flag for activityResult
    private boolean flagPdf, flagFileTranscript, flagFileLesson, flagFileSubScore;


    private int tableRow = 3;   // tabloya eklenecek sınır

    //EditText
    private EditText editYazOkuluTeacherName, editYazOkuluToSchool;
    private String strTeacherName, strToSchool;

    // yaz okulunda alıncak
    ArrayList<String> takeLessonList;
    private EditText editYazOkuluTakeFaculty, editYazOkuluTakeLessonName, editYazOkuluTakeLessonT, editYazOkuluTakeLessonU, editYazOkuluTakeLessonL, editYazOkuluTakeLessonAKTS;
    private String strTakeFaculty, strTakeLessonName, strTakeLessonT, strTakeLessonU, strTakeLessonL, strTakeLessonAKTS;

    // date
    DatePickerDialog.OnDateSetListener onDateSetListenerStart, onDateSetListenerFinish;
    private TextView editYazOkuluSchoolDateStart, editYazOkuluSchoolDateFinish;
    private String strSchoolDateStart, strSchoolDateFinish;

    // progress dialog
    private CustomDialog customDialog;

    private void init() {

        // file state
        image_yazOkul_fileStateTranscript = (ImageView) findViewById(R.id.image_yazOkul_fileStateTranscript);
        image_yazOkul_fileStateLesson = (ImageView) findViewById(R.id.image_yazOkul_fileStateLesson);
        image_yazOkul_fileStateSubScore = (ImageView) findViewById(R.id.image_yazOkul_fileStateSubScore);
        textView_yazOkul_fileStateTranscript = (TextView) findViewById(R.id.textView_yazOkul_fileStateTranscript);
        textView_yazOkul_fileStateLesson = (TextView) findViewById(R.id.textView_yazOkul_fileStateLesson);
        textView_yazOkul_fileStateSubScore = (TextView) findViewById(R.id.textView_yazOkul_fileStateSubScore);

        //arrayList
        fileUriList = new ArrayList<Uri>();
        fileType = new ArrayList<String>();
        fileType.add("Transcript/");
        fileType.add("LessonContents/");
        fileType.add("SubScore/");
        fileType.add("Petition/");


        //yaz okulunda alıncak
        takeLessonList = new ArrayList<String>();
        editYazOkuluTakeFaculty = (EditText) findViewById(R.id.editYazOkuluTakeFaculty);
        editYazOkuluTakeLessonName = (EditText) findViewById(R.id.editYazOkuluTakeLessonName);
        editYazOkuluTakeLessonT = (EditText) findViewById(R.id.editYazOkuluTakeLessonT);
        editYazOkuluTakeLessonU = (EditText) findViewById(R.id.editYazOkuluTakeLessonU);
        editYazOkuluTakeLessonL = (EditText) findViewById(R.id.editYazOkuluTakeLessonL);
        editYazOkuluTakeLessonAKTS = (EditText) findViewById(R.id.editYazOkuluTakeLessonAKTS);

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        usersData = new UsersData();


        // date
        editYazOkuluSchoolDateStart = (TextView) findViewById(R.id.editYazOkuluSchoolDateStart);
        editYazOkuluSchoolDateFinish = (TextView) findViewById(R.id.editYazOkuluSchoolDateFinish);

        //EditText
        editYazOkuluTeacherName = (EditText) findViewById(R.id.editYazOkuluTeacherName);
        editYazOkuluToSchool = (EditText) findViewById(R.id.editYazOkuluToSchool);

        // aktif tarih alma
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        // date baslangıc tarihi
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
                //System.out.println(date);
                editYazOkuluSchoolDateStart.setText(date);
            }
        };

        // date bitis tarihi
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
                //System.out.println(date);
                editYazOkuluSchoolDateFinish.setText(date);
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaz_okulu);
        init();

        // text change events

        // transkript text
        textView_yazOkul_fileStateTranscript.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textView_yazOkul_fileStateLesson.setEnabled(true);
            }
        });

        // ders listesi text
        textView_yazOkul_fileStateLesson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textView_yazOkul_fileStateSubScore.setEnabled(true);
            }
        });
    }

    // editText set str
    private void setTextString() {
        strTakeFaculty = editYazOkuluTakeFaculty.getText().toString();
        strTakeLessonName = editYazOkuluTakeLessonName.getText().toString();
        strTakeLessonT = editYazOkuluTakeLessonT.getText().toString();
        strTakeLessonU = editYazOkuluTakeLessonU.getText().toString();
        strTakeLessonL = editYazOkuluTakeLessonL.getText().toString();
        strTakeLessonAKTS = editYazOkuluTakeLessonAKTS.getText().toString();
        //EditText,TextView
        strTeacherName = editYazOkuluTeacherName.getText().toString();
        strToSchool = editYazOkuluToSchool.getText().toString();
        strSchoolDateStart = editYazOkuluSchoolDateStart.getText().toString();
        strSchoolDateFinish = editYazOkuluSchoolDateFinish.getText().toString();
    }

    // input EditText bos kontrolu
    private boolean isNotEmptyStrings() {
        setTextString();
        boolean result = TextUtils.isEmpty(strTeacherName) || TextUtils.isEmpty(strToSchool) || TextUtils.isEmpty(strSchoolDateStart) || TextUtils.isEmpty(strSchoolDateFinish)
                || takeLessonList.size() == 0;
        if (result)
            return false;
        return true;
    }


    // yaz okulunda alıncak start
    private boolean isNotEmptyTakeLesson() {
        setTextString();
        boolean result = TextUtils.isEmpty(strTakeFaculty) || TextUtils.isEmpty(strTakeLessonName) || TextUtils.isEmpty(strTakeLessonT) || TextUtils.isEmpty(strTakeLessonU) || TextUtils.isEmpty(strTakeLessonL) || TextUtils.isEmpty(strTakeLessonAKTS);

        if (result)
            return false;
        return true;
    }

    // editText temizleme
    private void takeLessonTextDelete() {
        editYazOkuluTakeFaculty.setText("");
        editYazOkuluTakeLessonName.setText("");
        editYazOkuluTakeLessonT.setText("");
        editYazOkuluTakeLessonU.setText("");
        editYazOkuluTakeLessonL.setText("");
        editYazOkuluTakeLessonAKTS.setText("");
    }

    // listeye ders ekleme
    public void yazOkuluTakeLessonAdd(View view) {

        if (takeLessonList.size() < tableRow) {
            if (isNotEmptyTakeLesson()) {
                String temp = strTakeFaculty + ";" + strTakeLessonName + ";" + strTakeLessonT + ";" + strTakeLessonU + ";" + strTakeLessonL + ";" + strTakeLessonAKTS;
                takeLessonList.add(temp);

                for (int i = 0; i < takeLessonList.size(); i++) {
                    System.out.println(takeLessonList.get(i));
                }
                System.out.println("----------------------------------------------");
                takeLessonTextDelete();
            } else {
                Toast.makeText(YazOkuluActivity.this, "Ders eklemede boş alanlar var ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(YazOkuluActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }
    }

    // listeden ders kaldirma
    public void yazOkuluTakeLessonRemove(View view) {
        if (takeLessonList.size() > 0) {
            takeLessonList.remove(takeLessonList.size() - 1);
            Toast.makeText(YazOkuluActivity.this, "Son Eklenen Kaldırıldı", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(YazOkuluActivity.this, "Liste bos", Toast.LENGTH_SHORT).show();
        }
    }
    // yaz okulunda alıncak end


    //pdf start
    //https://github.com/LukeDaniel16/CreatePDFwithJavaOnAndroidStudio
    public void initPdf(String title) {
        flagPdf = true;
        flagFileTranscript = false;
        flagFileLesson = false;
        flagFileSubScore = false;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATE_PDF);
    }

    // pdf içerik olustur
    private void createPdf(Uri uri) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        paint.setLinearText(true);
        paint.setTypeface(Typeface.MONOSPACE);
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(4);
        paint.setFakeBoldText(false);

        // aktif tarih
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);

        canvas.drawText(strDate, 190, 12, paint);
        paint.setFakeBoldText(true);
        canvas.drawText("T.C.", pageInfo.getPageWidth() / 2, 20, paint);
        canvas.drawText("KOCAELİ ÜNİVERSİTESİ", pageInfo.getPageWidth() / 2, 26, paint);
        canvas.drawText(usersData.getIncomingFaculty().toUpperCase(), pageInfo.getPageWidth() / 2, 32, paint);
        canvas.drawText(usersData.getIncomingDepartment().toUpperCase() + " BÖLÜM BAŞKANLIĞINA", pageInfo.getPageWidth() / 2, 38, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(3);
        paint.setFakeBoldText(false);



        TextPaint mTextPaint=new TextPaint();
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setTextSize(3);
        String mText="\t" + usersData.getIncomingFaculty() + " " + usersData.getIncomingDepartment() + " " + usersData.getIncomingNumber() + " numaralı "+usersData.getIncomingName() + " " + usersData.getIncomingLastName() + " isimli öğrencisiyim.\n\tYaz öğretimi kapsamında aşağıda bilgilerini verdiğim ders/dersleri almak istiyorum. Kontrol listesinde belirtilen adımları tamamladım.\n\tGereği için arz ederim. ";
        StaticLayout mTextLayout = new StaticLayout(mText, mTextPaint, canvas.getWidth()-60, Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        canvas.save();
// calculate x and y position where your text will be placed
        int textX = 30;
        int textY = 45;
        canvas.translate(textX, textY);
        mTextLayout.draw(canvas);
        canvas.restore();

        canvas.drawText("imza:_____________", 142, 75, paint);


        //table1 row
        canvas.drawLine(30, 85, 180, 85, paint);
        canvas.drawLine(30, 90, 180, 90, paint);
        canvas.drawLine(30, 100, 180, 100, paint);
        canvas.drawLine(30, 105, 180, 105, paint);
        canvas.drawLine(30, 110, 180, 110, paint);
        canvas.drawLine(30, 115, 180, 115, paint);
        canvas.drawLine(30, 120, 180, 120, paint);
        //table1row context
        canvas.drawText("Öğrenci E-mail", 35, 89, paint);
        canvas.drawText(usersData.getIncomingMail(), 95, 89, paint);
        canvas.drawText("Öğrenci Adres", 35, 94, paint);


        String mAdressText=usersData.getIncomingAddress();
        StaticLayout mAdressTextLayout = new StaticLayout(mAdressText, mTextPaint, canvas.getWidth()-120, Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        canvas.save();
// calculate x and y position where your text will be placed
        int textAX = 95;
        int textAY = 90;
        canvas.translate(textAX, textAY);
        mAdressTextLayout.draw(canvas);
        canvas.restore();






        canvas.drawText("Öğrenci Gsm ", 35, 104, paint);
        canvas.drawText(usersData.getIncomingPhone(), 95, 104, paint);
        canvas.drawText("Öğrenci Danışmanı Adı Soyadı", 35, 109, paint);
        canvas.drawText(strTeacherName, 95, 109, paint);
        canvas.drawText("Başvurulan Üniversite", 35, 114, paint);
        canvas.drawText(strToSchool, 95, 114, paint);
        canvas.drawText("Başlama-Bitiş Tarihleri", 35, 119, paint);
        canvas.drawText(strSchoolDateStart + " - " + strSchoolDateFinish, 95, 119, paint);
        //table1 column
        canvas.drawLine(30, 85, 30, 120, paint);
        canvas.drawLine(93, 85, 93, 120, paint); //orts
        canvas.drawLine(180, 85, 180, 120, paint);


        canvas.drawText("\tYaz öğretimi kapsamında alınacak ders/dersler", 30, 129, paint);
        //table3 row
        canvas.drawLine(30, 130, 180, 130, paint);
        canvas.drawLine(30, 135, 180, 135, paint);
        canvas.drawLine(30, 140, 180, 140, paint);
        canvas.drawLine(30, 145, 180, 145, paint);
        canvas.drawLine(30, 150, 180, 150, paint);

        // table arraylist
        int point = 134;
        for (int i = 0; i < takeLessonList.size(); i++) {
            String[] temp = takeLessonList.get(i).toString().split(";");
            point += 5;
            canvas.drawText(temp[0], 31, point, paint);   // fakulte/-bolum
            canvas.drawText(temp[1], 71, point, paint);   // Dersin Adı ve Kodu
            canvas.drawText(temp[2], 144, point, paint);   // T
            canvas.drawText(temp[3], 154, point, paint);   // U
            canvas.drawText(temp[4], 164, point, paint);   // L
            canvas.drawText(temp[5], 174, point, paint);   // AKTS
        }

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
        canvas.drawText("Dilekçe Ekleri", 30, 159, paint);
        paint.setFakeBoldText(false);
        paint.setTextSize(2);

        TextPaint mFooterTextPaint=new TextPaint();
        mFooterTextPaint.setTypeface(Typeface.MONOSPACE);
        mFooterTextPaint.setTextSize(2);
        String mFooterText="1-Yaz döneminde ders almak istenilen Üniversite ve Kocaeli Üniversitesinin ilgili bölümlerinin, öğrencinin üniversiteye giriş yılındaki taban puanlarını gösteren belge ektedir." +
                "\n2-Alınmak istenilen derslerin karşı Üniversitedeki ders saati/kredi/AKTS ve ders içeriklerini gösteren belge ektedir." +
                "\n3-Başvurulan dönem içinde alınmış transkript ektedir.";

        StaticLayout mFooterTextLayout = new StaticLayout(mFooterText, mFooterTextPaint, canvas.getWidth()-60, Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        canvas.save();
// calculate x and y position where your text will be placed
        int textFX = 30;
        int textFY = 160;
        canvas.translate(textFX, textFY);
        mFooterTextLayout.draw(canvas);
        canvas.restore();

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

        TextPaint mFooterEsasTextPaint=new TextPaint();
        mFooterEsasTextPaint.setTypeface(Typeface.MONOSPACE);
        mFooterEsasTextPaint.setTextSize(2);
        String mFooterEsasText="\nESAS l- (1) Spor Bilimleri Fakültesi dışındaki tüm akademik birimlerde okuyan öğrencilerin, diğer yükseköğretim kurumlarından yaz öğretiminde ders alabilmeleri için; Üniversiteye giriş yılı esas olmak üzere ilgili bölüm/program taban puanın en fazla 40 puan düşük olma koşulu aranır." +
                "\n(3) Yaz okulunda diğer yükseköğretim kurumlarından alınacak dersler için öğrencinin kayıtlı olduğu ilgili birimin Bölüm/Anabilim Dalı veya Program Başkanlığının onayı gerekir. " +
                "\n(4) Yaz okulunda Bölüm/Anabilim Dalı veya Program Başkanlığı bu konudaki değerlendirmesini; ilgili bölüm müfredatındaki benzer içeriğe veya program yeterliliğine sahip olmakla birlikte, ders(ler)in AKTS/kedi/saat değer(ler)inden herhangi birini dikkate alarak yapar." +
                "\nESAS 2- (l) Öğrenciler, yaz öğretiminde derslerin AKTS/kredi/saat değerlerine bakılmaksızın, en çok 3 ders alabilir. " +
                "\nESAS 3- (l) Güz ve/veya bahar yarıyıllarında kayıt donduran öğrenciler, kayıt dondurdukları yarıyıllara ilişkin dersleri yaz öğretiminde alamazlar." +
                "\nESAS 6  (l)Yaz öğretimi kapsamında alınan ders(ler)in notu genel not ortalamasına bakılmaksızın 4'lük sistem üzerinden 2'nin altında ise öğrenci bu ders(ler)den başarısız sayılır." +
                "\nESAS 9- (l) Yaz öğretimi aynı öğretim yılına ait üçüncü bir yarıyıl değildir. Yaz öğretiminde alınan ders(ler)in notları, öğrencilerin güz ve bahar yarıyıllarındaki yarıyıl not ortalamalarını ve yarıyıllardaki derslerin koşul durumlarını etkilemez. Ancak, öğrencinin genel not ortalaması (GNO) hesaplanmasına dahil edilir. ";


        StaticLayout mFooterEsasTextLayout = new StaticLayout(mFooterEsasText, mFooterEsasTextPaint, canvas.getWidth()-60, Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        canvas.save();
// calculate x and y position where your text will be placed
        int textFEX = 30;
        int textFEY = 210;
        canvas.translate(textFEX, textFEY);
        mFooterEsasTextLayout.draw(canvas);
        canvas.restore();


        pdfDocument.finishPage(page);
        setPdf(uri, pdfDocument);
    }

    // pdfi kaydet
    private void setPdf(Uri uri, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(Objects.requireNonNull(getContentResolver().openOutputStream(uri)));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            petitionUri = uri;
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Dosya hatası bulunamadı", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Giriş ve çıkış hatası", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bilinmeyen hata" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //https://github.com/LukeDaniel16/CreatePDFwithJavaOnAndroidStudio
    //pdf end


    //https://stackoverflow.com/questions/9758151/get-the-file-extension-from-images-picked-from-gallery-or-camera-as-string
    public String getMimeType(Context context, Uri uri) {
        //seçilen dosya uzantısı tespit etme
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }
    //https://stackoverflow.com/questions/9758151/get-the-file-extension-from-images-picked-from-gallery-or-camera-as-string


    // dosya kayıt format isimlendirme
    private String adjustFormat() {
        String number, name, lastName;
        number = usersData.getIncomingNumber();
        name = usersData.getIncomingName();
        lastName = usersData.getIncomingLastName();

        // https://www.javatpoint.com/java-get-current-date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        String strDate = formatter.format(date);
        // https://www.javatpoint.com/java-get-current-date

        return number + "_" + name + "_" + lastName + "_" + strDate;
    }

    // dosya sec sayfası
    private void selectFile() {
        Intent intent = new Intent();
        //https://stackoverflow.com/questions/1698050/multiple-mime-types-in-android
        intent.setType("*/*");
        String[] mimetypes = {"application/msword", "application/pdf", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        //https://stackoverflow.com/questions/1698050/multiple-mime-types-in-android
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Dosya Seç"), PICK_FILE);
    }

    // transkript sec buton
    public void selectFileTranscript(View view) {
        flagPdf = false;
        flagFileTranscript = true;
        flagFileLesson = false;
        flagFileSubScore = false;
        selectFile();
    }

    // ders icerik sec buton
    public void selectFileLesson(View view) {
        flagPdf = false;
        flagFileTranscript = false;
        flagFileLesson = true;
        flagFileSubScore = false;
        selectFile();
    }

    // taban puan sec buton
    public void selectFileSubScore(View view) {
        flagPdf = false;
        flagFileTranscript = false;
        flagFileLesson = false;
        flagFileSubScore = true;
        selectFile();
    }

    // onayla butonu
    public void submitYazOkul(View view) {
        if (isNotEmptyStrings() && transcriptUri != null && lessonUri != null && subScoreUri != null) {
            AlertDialog.Builder checkAlertDialog = new AlertDialog.Builder(YazOkuluActivity.this);
            checkAlertDialog.setTitle("Onaylama");
            checkAlertDialog.setMessage("Başvurunuzu tamamlamak istiyor musunuz?");
            checkAlertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    initPdf("YazOkuluBasvuru");
                }
            });
            checkAlertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.out.println("Hayır Bastın");
                }
            });
            checkAlertDialog.create().show();
        } else {
            Toast.makeText(YazOkuluActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
        }
    }

    // basvurular firebase save
    private void saveResources() {

        // https://www.javatpoint.com/java-get-current-date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        // https://www.javatpoint.com/java-get-current-date

        fUser = fAuth.getCurrentUser();
        resourcesAdd = new HashMap<String, String>();
        resourcesAdd.put("type", "Yaz Okulu");
        resourcesAdd.put("userUid", fUser.getUid());
        resourcesAdd.put("state", "0");
        resourcesAdd.put("transcriptPath", transcriptPath);
        resourcesAdd.put("lessonPath", lessonPath);
        resourcesAdd.put("subScorePath", subScorePath);
        resourcesAdd.put("petitionPath", petitionPath);
        resourcesAdd.put("studentNumber", usersData.getIncomingNumber());
        resourcesAdd.put("date", strDate);

        firebaseFirestore.collection("Resources").document()
                .set(resourcesAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(YazOkuluActivity.this, "Başvuru Yapıldı\n İmzalı Belgeyi Yükleyin", Toast.LENGTH_LONG).show();
                System.out.println("YazOkulu basvuru kayıt tamam");
                customDialog.dismissDialog();
                startActivity(new Intent(YazOkuluActivity.this, StudentHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    // firebase save files
    private void saveFileInStorage() {

        fileUriList.add(3, petitionUri);
        if (fileUriList.size() == 4) {

            for (int i = 0; i < fileUriList.size(); i++) {
                String extension = getMimeType(YazOkuluActivity.this, fileUriList.get(i));

                if (i == 0) {
                    transcriptPath = "YazOkul/" + extension + "/" + fileType.get(i) + adjustFormat();
                } else if (i == 1) {
                    lessonPath = "YazOkul/" + extension + "/" + fileType.get(i) + adjustFormat();
                } else if (i == 2) {
                    subScorePath = "YazOkul/" + extension + "/" + fileType.get(i) + adjustFormat();
                } else if (i == 3) {
                    petitionPath = "YazOkul/" + extension + "/" + fileType.get(i) + adjustFormat();
                }

                StorageReference reference = storageReference.child("YazOkul").child(extension).child(fileType.get(i) + adjustFormat());

                reference.putFile(fileUriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // path alma
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        String linkUri = uriTask.getResult().getPath();
                        System.out.println("Uri: " + String.valueOf(linkUri));
                        System.out.println("YazOkul dosya Kayıt Tamam.");
                        System.out.println("----------------------");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(YazOkuluActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    private boolean t1 = false, t2 = false, t3 = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_PDF && resultCode == RESULT_OK && data.getData() != null && flagPdf) {

            customDialog = new CustomDialog(YazOkuluActivity.this);
            customDialog.startLoadingDialog();

            pdfUri = data.getData();
            createPdf(pdfUri); // pdf olustur kaydet
            saveFileInStorage(); // dosyalari kaydet
            saveResources();
        } else if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null && flagFileTranscript) {
            // dosya transkiript
            transcriptUri = data.getData();

            if (fileUriList.size() > 0 && t1) {
                fileUriList.remove(0);
                fileUriList.add(0, transcriptUri);
            } else {
                fileUriList.add(0, transcriptUri);
                t1 = true;
            }


            //System.out.println(getMimeType(CapActivity.this,transcriptUri));
            image_yazOkul_fileStateTranscript.setImageResource(R.drawable.yes);
            textView_yazOkul_fileStateTranscript.setText("Transkript Dosyasını Değiştir");
        } else if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null && flagFileLesson) {
            // dosya ders icerik
            lessonUri = data.getData();

            if (fileUriList.size() > 0 && t2) {
                fileUriList.remove(1);
                fileUriList.add(1, lessonUri);
            } else {
                fileUriList.add(1, lessonUri);
                t2 = true;
            }


            //System.out.println(getMimeType(CapActivity.this,transcriptUri));
            image_yazOkul_fileStateLesson.setImageResource(R.drawable.yes);
            textView_yazOkul_fileStateLesson.setText("Ders Listesi Değiştir");
        } else if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null && flagFileSubScore) {
            // dosya taban puan
            subScoreUri = data.getData();

            if (fileUriList.size() > 0 && t3) {
                fileUriList.remove(2);
                fileUriList.add(2, subScoreUri);
            } else {
                fileUriList.add(2, subScoreUri);
                t3 = true;
            }


            //System.out.println(getMimeType(CapActivity.this,transcriptUri));
            image_yazOkul_fileStateSubScore.setImageResource(R.drawable.yes);
            textView_yazOkul_fileStateSubScore.setText("Taban Puan Değiştir");
        }
    }
}