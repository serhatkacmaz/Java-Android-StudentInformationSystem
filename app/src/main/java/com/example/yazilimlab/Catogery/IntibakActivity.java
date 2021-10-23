package com.example.yazilimlab.Catogery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.Model.CustomDialog;
import com.example.yazilimlab.Model.UsersData;
import com.example.yazilimlab.R;
import com.example.yazilimlab.RegisterActivity;
import com.example.yazilimlab.StudentHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class IntibakActivity extends AppCompatActivity {

    //EditText
    private EditText editIntibakOldSchool, editIntibakOldFaculty, editIntibakOldBranch;
    private String strEditIntibakOldSchool, strEditIntibakOldFaculty, strEditIntibakOldBranch;

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
    private String transcriptPath, lessonPath, petitionPath;

    // Uri
    private Uri transcriptUri, lessonUri, pdfUri, petitionUri;
    // file state
    private TextView textView_intibak_fileStateTranscript, textView_intibak_fileStateLesson;
    private ImageView image_intibak_fileStateTranscript, image_intibak_fileStateLesson;

    // code
    private static final int CREATE_PDF = 1;
    private static final int PICK_FILE = 1;


    // flag for activityResult
    private boolean flagPdf, flagFileTranscript, flagFileLesson;


    private int tableRow = 16;

    // daha önce aldığım
    ArrayList<String> oldLessonList;
    private EditText editIntibakOldLesson, editIntibakOldLessonT, editIntibakOldLessonUL, editIntibakOldLessonK, editIntibakOldLessonAKTS;
    private String strOldLessonName, strOldLessonT, strOldLessonUL, strOldLessonK, strOldLessonAKTS;

    // muaf olmak istediğim
    ArrayList<String> exemptLessonList;
    private EditText editIntibakExemptLessonCode, editIntibakExemptLessonName, editIntibakExemptLessonT, editIntibakExemptLessonUL, editIntibakExemptLessonK, editIntibakExemptLessonAKTS;
    private String strExemptLessonCode, strExemptLessonName, strExemptLessonT, strExemptLessonUL, strExemptLessonK, strExemptLessonAKTS;

    // progress dialog
    private CustomDialog customDialog;

    // init
    private void init() {

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        usersData = new UsersData();

        //arrayList
        fileUriList = new ArrayList<Uri>();
        fileType = new ArrayList<String>();
        fileType.add("Transcript/");
        fileType.add("LessonContents/");
        fileType.add("Petition/");

        // file state
        textView_intibak_fileStateTranscript = (TextView) findViewById(R.id.textView_intibak_fileStateTranscript);
        textView_intibak_fileStateLesson = (TextView) findViewById(R.id.textView_intibak_fileStateLesson);
        image_intibak_fileStateTranscript = (ImageView) findViewById(R.id.image_intibak_fileStateTranscript);
        image_intibak_fileStateLesson = (ImageView) findViewById(R.id.image_intibak_fileStateLesson);


        //EditText
        editIntibakOldSchool = (EditText) findViewById(R.id.editIntibakOldSchool);
        editIntibakOldFaculty = (EditText) findViewById(R.id.editIntibakOldFaculty);
        editIntibakOldBranch = (EditText) findViewById(R.id.editIntibakOldBranch);

        // daha önce aldığım
        oldLessonList = new ArrayList<String>();
        editIntibakOldLesson = (EditText) findViewById(R.id.editIntibakOldLesson);
        editIntibakOldLessonT = (EditText) findViewById(R.id.editIntibakOldLessonT);
        editIntibakOldLessonUL = (EditText) findViewById(R.id.editIntibakOldLessonUL);
        editIntibakOldLessonK = (EditText) findViewById(R.id.editIntibakOldLessonK);
        editIntibakOldLessonAKTS = (EditText) findViewById(R.id.editIntibakOldLessonAKTS);

        // muaf olmak istediğim
        exemptLessonList = new ArrayList<String>();
        editIntibakExemptLessonCode = (EditText) findViewById(R.id.editIntibakExemptLessonCode);
        editIntibakExemptLessonName = (EditText) findViewById(R.id.editIntibakExemptLessonName);
        editIntibakExemptLessonT = (EditText) findViewById(R.id.editIntibakExemptLessonT);
        editIntibakExemptLessonUL = (EditText) findViewById(R.id.editIntibakExemptLessonUL);
        editIntibakExemptLessonK = (EditText) findViewById(R.id.editIntibakExemptLessonK);
        editIntibakExemptLessonAKTS = (EditText) findViewById(R.id.editIntibakExemptLessonAKTS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intibak);
        init();

        // text Change event
        textView_intibak_fileStateTranscript.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textView_intibak_fileStateLesson.setEnabled(true);
            }
        });
    }


    //start pdf
    //https://github.com/LukeDaniel16/CreatePDFwithJavaOnAndroidStudio
    public void initPdf(String title) {
        flagPdf = true;
        flagFileTranscript = false;
        flagFileLesson = false;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATE_PDF);
    }

    // pdf icerigi olusturma
    private void createPdf(Uri uri) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint s = new Paint();
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
        canvas.drawText(usersData.getIncomingFaculty().toUpperCase() + " DEKANLIĞINA", pageInfo.getPageWidth() / 2, 32, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(3);
        paint.setFakeBoldText(false);

        //table1 row
        canvas.drawLine(30, 40, 180, 40, paint);
        canvas.drawLine(30, 45, 180, 45, paint);
        canvas.drawLine(30, 50, 180, 50, paint);
        canvas.drawLine(30, 55, 180, 55, paint);
        canvas.drawLine(30, 60, 180, 60, paint);
        canvas.drawLine(30, 65, 180, 65, paint);
        canvas.drawLine(30, 70, 180, 70, paint);
        canvas.drawLine(30, 75, 180, 75, paint);
        //table1 context
        canvas.drawText("KİMLİK BİLGİLERİ ( Tüm alanları doldurunuz )", 33, 44, paint);
        canvas.drawText("Adı ve Soyadı", 95, 49, paint);
        canvas.drawText(usersData.getIncomingName() + " " + usersData.getIncomingLastName(), 33, 49, paint);
        canvas.drawText("Öğrenci No", 33, 54, paint);
        canvas.drawText(usersData.getIncomingNumber(), 95, 54, paint);
        canvas.drawText("Bölümü", 33, 59, paint);
        canvas.drawText(usersData.getIncomingDepartment(), 95, 59, paint);
        canvas.drawText("Telefon Numarası", 33, 64, paint);
        canvas.drawText(usersData.getIncomingPhone(), 95, 64, paint);
        canvas.drawText("E-posta Adresi", 33, 69, paint);
        canvas.drawText(usersData.getIncomingMail(), 95, 69, paint);
        canvas.drawText("Yazışma Adresi", 33, 74, paint);
        canvas.drawText(usersData.getIncomingAddress(), 95, 74, paint);
        //table1 column
        canvas.drawLine(30, 40, 30, 75, paint);
        canvas.drawLine(90, 45, 90, 75, paint);
        canvas.drawLine(180, 40, 180, 75, paint);

        paint.setTextSize(4);
        canvas.drawText("       Daha önce " + strEditIntibakOldSchool + " Üniversitesi " + strEditIntibakOldFaculty + " Fakültesi / Meslek ", 45, 85, paint);
        canvas.drawText("Yüksek Okulu " + strEditIntibakOldBranch + " Bölümünde / Programında aldığım ve ", 45, 90, paint);
        canvas.drawText("aşağıda belirttiğim ders / derslerden muaf olmak istiyorum.", 45, 95, paint);
        canvas.drawText("       Gereğinin yapılmasını arz ederim.", 45, 100, paint);
        canvas.drawText("İmza:", 150, 107, paint);


        // table arraylist Daha once aldığım ders
        int point = 145;
        for (int i = 0; i < oldLessonList.size(); i++) {
            String[] temp = oldLessonList.get(i).toString().split(";");
            point += 5;
            canvas.drawText(temp[0], 31, point, paint);   // Adı
            canvas.drawText(temp[1], 81, point, paint);   // T
            canvas.drawText(temp[2], 86, point, paint);   // U/L
            canvas.drawText(temp[3], 91, point, paint);   // K
            canvas.drawText(temp[4], 96, point, paint);   // AKTS
        }

        point = 145;
        // table arraylist Daha once aldığım ders
        for (int i = 0; i < exemptLessonList.size(); i++) {
            String[] temp = exemptLessonList.get(i).toString().split(";");
            point += 5;
            canvas.drawText(temp[0], 103, point, paint);   // Kod
            canvas.drawText(temp[1], 116, point, paint);   // Ad
            canvas.drawText(temp[2], 161, point, paint);   // T
            canvas.drawText(temp[3], 165, point, paint);   // U/L
            canvas.drawText(temp[4], 171, point, paint);   // K
            canvas.drawText(temp[4], 176, point, paint);   // AKTS
        }

        //table2 row
        canvas.drawLine(30, 120, 180, 120, paint);
        canvas.drawLine(30, 130, 180, 130, paint);
        canvas.drawLine(30, 145, 180, 145, paint);
        //16
        canvas.drawLine(30, 150, 180, 150, paint);
        canvas.drawLine(30, 155, 180, 155, paint);
        canvas.drawLine(30, 160, 180, 160, paint);
        canvas.drawLine(30, 165, 180, 165, paint);
        canvas.drawLine(30, 170, 180, 170, paint);
        canvas.drawLine(30, 175, 180, 175, paint);
        canvas.drawLine(30, 180, 180, 180, paint);
        canvas.drawLine(30, 185, 180, 185, paint);
        canvas.drawLine(30, 190, 180, 190, paint);
        canvas.drawLine(30, 195, 180, 195, paint);
        canvas.drawLine(30, 200, 180, 200, paint);
        canvas.drawLine(30, 205, 180, 205, paint);
        canvas.drawLine(30, 210, 180, 210, paint);
        canvas.drawLine(30, 215, 180, 215, paint);
        canvas.drawLine(30, 220, 180, 220, paint);
        canvas.drawLine(30, 225, 180, 225, paint);

        //table2 context
        canvas.drawText("Daha Önce Aldığım Dersin", 33, 125, paint);
        canvas.drawText("Bilişim Sistemleri Mühendisliği Bölümünde", 103, 124, paint);
        canvas.drawText("Muaf Olmak İstediğim", 103, 129, paint);
        canvas.drawText("ADI", 50, 140, paint);
        paint.setTextSize(3);
        canvas.drawText("T", 81, 138, paint);
        canvas.drawText("U", 86, 138, paint);
        canvas.drawText("L", 86, 142, paint);
        canvas.drawText("K", 91, 138, paint);
        canvas.drawText("A", 96, 132, paint);
        canvas.drawText("K", 96, 136, paint);
        canvas.drawText("T", 96, 140, paint);
        canvas.drawText("S", 96, 144, paint);
        paint.setTextSize(4);
        canvas.drawText("KOD", 103, 140, paint);
        canvas.drawText("ADI", 135, 140, paint);
        paint.setTextSize(3);
        canvas.drawText("T", 161, 138, paint);
        canvas.drawText("U", 166, 138, paint);
        canvas.drawText("L", 166, 142, paint);
        canvas.drawText("K", 171, 138, paint);
        canvas.drawText("A", 176, 132, paint);
        canvas.drawText("K", 176, 136, paint);
        canvas.drawText("T", 176, 140, paint);
        canvas.drawText("S", 176, 144, paint);
        paint.setTextSize(4);
        //table2 column
        canvas.drawLine(30, 120, 30, 225, paint);
        canvas.drawLine(80, 130, 80, 225, paint);
        canvas.drawLine(85, 130, 85, 225, paint);
        canvas.drawLine(90, 130, 90, 225, paint);
        canvas.drawLine(95, 130, 95, 225, paint);
        canvas.drawLine(100, 120, 100, 225, paint);//orta
        canvas.drawLine(115, 130, 115, 225, paint);
        canvas.drawLine(160, 130, 160, 225, paint);
        canvas.drawLine(165, 130, 165, 225, paint);
        canvas.drawLine(170, 130, 170, 225, paint);
        canvas.drawLine(175, 130, 175, 225, paint);
        canvas.drawLine(180, 120, 180, 225, paint);

        canvas.drawText("T: Teorik Ders Saati  U / L : Uygulama / Laboratuvar Saati  K : Kredi", 30, 230, paint);
        paint.setFakeBoldText(true);
        canvas.drawText("Eklenecek Belge/Belgeler:", 50, 240, paint);
        paint.setFakeBoldText(false);
        paint.setTextSize(3);
        canvas.drawText("1-\tTranskript Belgesi (Onaylı)", 55, 245, paint);
        canvas.drawText("2-\tOnaylı Ders İçerikleri", 55, 248, paint);

        pdfDocument.finishPage(page);
        setPdf(uri, pdfDocument);
    }

    // pdf kaydet
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


    // editText set str
    private void setTextString() {
        strEditIntibakOldSchool = editIntibakOldSchool.getText().toString();
        strEditIntibakOldFaculty = editIntibakOldFaculty.getText().toString();
        strEditIntibakOldBranch = editIntibakOldBranch.getText().toString();
    }

    // input bos kontrolu editText
    private boolean isNotEmptyString() {
        setTextString();
        boolean result = TextUtils.isEmpty(strEditIntibakOldSchool) || TextUtils.isEmpty(strEditIntibakOldFaculty) || TextUtils.isEmpty(strEditIntibakOldBranch)
                || oldLessonList.size() == 0 || exemptLessonList.size() == 0;
        if (result)
            return false;
        return true;
    }

    // daha önce aldığım start
    private void setTextStringOldLesson() {
        strOldLessonName = editIntibakOldLesson.getText().toString();
        strOldLessonT = editIntibakOldLessonT.getText().toString();
        strOldLessonUL = editIntibakOldLessonUL.getText().toString();
        strOldLessonK = editIntibakOldLessonK.getText().toString();
        strOldLessonAKTS = editIntibakOldLessonAKTS.getText().toString();
    }

    // bos mu
    private boolean isNotEmptyOldLesson() {
        setTextStringOldLesson();
        boolean result = TextUtils.isEmpty(strOldLessonName) || TextUtils.isEmpty(strOldLessonT) || TextUtils.isEmpty(strOldLessonUL) || TextUtils.isEmpty(strOldLessonK) || TextUtils.isEmpty(strOldLessonAKTS);

        if (result)
            return false;
        return true;
    }

    // editText delete
    private void oldLessonTextDelete() {
        editIntibakOldLesson.setText("");
        editIntibakOldLessonT.setText("");
        editIntibakOldLessonUL.setText("");
        editIntibakOldLessonK.setText("");
        editIntibakOldLessonAKTS.setText("");
    }

    // ders ekleme buton
    public void IntibakOldLessonAdd(View view) {

        if (oldLessonList.size() < tableRow) {
            if (isNotEmptyOldLesson()) {
                String temp = strOldLessonName + ";" + strOldLessonT + ";" + strOldLessonUL + ";" + strOldLessonK + ";" + strOldLessonAKTS;
                oldLessonList.add(temp);

                for (int i = 0; i < oldLessonList.size(); i++) {
                    System.out.println(oldLessonList.get(i));
                }
                System.out.println("----------------------------------------------");
                oldLessonTextDelete();
            } else {
                Toast.makeText(IntibakActivity.this, "Daha önce aldığım ders kısmında boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(IntibakActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }
    }

    // ders kaldırma buton
    public void IntibakOldLessonRemove(View view) {
        if (oldLessonList.size() > 0) {
            oldLessonList.remove(oldLessonList.size() - 1);
            Toast.makeText(IntibakActivity.this, "Son Eklenen Kaldırıldı", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(IntibakActivity.this, "Liste bos", Toast.LENGTH_SHORT).show();
        }
    }
    // daha önce aldığım end


    // muaf olmak istediğim start
    private void setTextStringExemptLesson() {
        strExemptLessonCode = editIntibakExemptLessonCode.getText().toString();
        strExemptLessonName = editIntibakExemptLessonName.getText().toString();
        strExemptLessonT = editIntibakExemptLessonT.getText().toString();
        strExemptLessonUL = editIntibakExemptLessonUL.getText().toString();
        strExemptLessonK = editIntibakExemptLessonK.getText().toString();
        strExemptLessonAKTS = editIntibakExemptLessonAKTS.getText().toString();
    }

    // bos mu
    private boolean isNotEmptyExemptLesson() {
        setTextStringExemptLesson();
        boolean result = TextUtils.isEmpty(strExemptLessonCode) || TextUtils.isEmpty(strExemptLessonName) || TextUtils.isEmpty(strExemptLessonT) || TextUtils.isEmpty(strExemptLessonUL) || TextUtils.isEmpty(strExemptLessonK) || TextUtils.isEmpty(strExemptLessonAKTS);

        if (result)
            return false;
        return true;
    }

    // EditText delete
    private void exemptLessonTextDelete() {
        editIntibakExemptLessonCode.setText("");
        editIntibakExemptLessonName.setText("");
        editIntibakExemptLessonT.setText("");
        editIntibakExemptLessonUL.setText("");
        editIntibakExemptLessonK.setText("");
        editIntibakExemptLessonAKTS.setText("");
    }

    // ders ekleme buton
    public void IntibakExemptLessonAdd(View view) {

        if (exemptLessonList.size() < tableRow) {
            if (isNotEmptyExemptLesson()) {
                String temp = strExemptLessonCode + ";" + strExemptLessonName + ";" + strExemptLessonT + ";" + strExemptLessonUL + ";" + strExemptLessonK + ";" + strExemptLessonAKTS;
                exemptLessonList.add(temp);

                for (int i = 0; i < exemptLessonList.size(); i++) {
                    System.out.println(exemptLessonList.get(i));
                }
                System.out.println("----------------------------------------------");
                exemptLessonTextDelete();
            } else {
                Toast.makeText(IntibakActivity.this, "Muaf olmak istediğim kısmında boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(IntibakActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }
    }

    // ders kaldırma buton
    public void IntibakExemptLessonRemove(View view) {
        if (exemptLessonList.size() > 0) {
            exemptLessonList.remove(exemptLessonList.size() - 1);
            Toast.makeText(IntibakActivity.this, "Son Eklenen Kaldırıldı", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(IntibakActivity.this, "Liste bos", Toast.LENGTH_SHORT).show();
        }
    }
    // muaf olmak istediğim end

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

    // dosya transkript sec
    public void selectFileTranscript(View view) {
        flagPdf = false;
        flagFileTranscript = true;
        flagFileLesson = false;
        selectFile();
    }

    // dosya onayli ders sec
    public void selectFileLesson(View view) {
        flagPdf = false;
        flagFileTranscript = false;
        flagFileLesson = true;
        selectFile();
    }

    // onayla buton
    public void submitIntibak(View view) {

        if (isNotEmptyString() && transcriptUri != null && lessonUri != null) {
            AlertDialog.Builder checkAlertDialog = new AlertDialog.Builder(IntibakActivity.this);
            checkAlertDialog.setTitle("Onaylama");
            checkAlertDialog.setMessage("Başvurunuzu tamamlamak istiyor musunuz?");
            checkAlertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    initPdf("intibak");
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
            Toast.makeText(IntibakActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
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
        resourcesAdd.put("type", "İntibak");
        resourcesAdd.put("userUid", fUser.getUid());
        resourcesAdd.put("state", "0");
        resourcesAdd.put("transcriptPath", transcriptPath);
        resourcesAdd.put("lessonPath", lessonPath);
        resourcesAdd.put("petitionPath", petitionPath);
        resourcesAdd.put("studentNumber", usersData.getIncomingNumber());
        resourcesAdd.put("date", strDate);

        firebaseFirestore.collection("Resources").document()
                .set(resourcesAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(IntibakActivity.this, "Başvuru Yapıldı\n İmzalı Belgeyi Yükleyin", Toast.LENGTH_LONG).show();
                System.out.println("Intibak basvuru kayıt tamam");
                customDialog.dismissDialog();
                startActivity(new Intent(IntibakActivity.this, StudentHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    // firebase save files
    private void saveFileInStorage() {

        fileUriList.add(2, petitionUri);
        if (fileUriList.size() == 3) {

            for (int i = 0; i < fileUriList.size(); i++) {
                String extension = getMimeType(IntibakActivity.this, fileUriList.get(i));

                if (i == 0) {
                    transcriptPath = "Intibak/" + extension + "/" + fileType.get(i) + adjustFormat();
                } else if (i == 1) {
                    lessonPath = "Intibak/" + extension + "/" + fileType.get(i) + adjustFormat();
                } else if (i == 2) {
                    petitionPath = "Intibak/" + extension + "/" + fileType.get(i) + adjustFormat();
                }

                StorageReference reference = storageReference.child("Intibak").child(extension).child(fileType.get(i) + adjustFormat());

                reference.putFile(fileUriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // path alma
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        String linkUri = uriTask.getResult().getPath();
                        System.out.println("Uri: " + String.valueOf(linkUri));
                        System.out.println("Intibak dosya Kayıt Tamam.");
                        System.out.println("----------------------");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(IntibakActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean t1 = false, t2 = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_PDF && resultCode == RESULT_OK && data.getData() != null && flagPdf) {

            customDialog = new CustomDialog(IntibakActivity.this);
            customDialog.startLoadingDialog();

            pdfUri = data.getData();
            createPdf(pdfUri); // pdf kaydet cihaz
            saveFileInStorage(); // dosyaları yukle firebase
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
            image_intibak_fileStateTranscript.setImageResource(R.drawable.yes);
            textView_intibak_fileStateTranscript.setText("Transkript Dosyasını Değiştir");
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
            image_intibak_fileStateLesson.setImageResource(R.drawable.yes);
            textView_intibak_fileStateLesson.setText("Onaylı Ders Listesi Değiştir");
        }
    }
}