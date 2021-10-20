package com.example.yazilimlab.Catogery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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


    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    UsersData usersData;

    // Uri
    private Uri transcriptUri, pdfUri, petitionUri;

    // image file state
    private ImageView image_yatayGecis_fileStateTranscript;
    private TextView textView_yatayGecis_fileStateTranscript;

    // Code
    private static final int CREATE_PDF = 1;
    private static final int PICK_FILE = 1;

    // flag for activityResult
    private boolean flagPdf, flagFileTranscript;

    // hashMap
    private HashMap<String, String> resourcesAdd;

    // path
    private String transcriptPath, petitionPath;


    // EditText
    private EditText editTextYatayGecisTerm, editTextYatayGecisNoteGrade, editTextYatayGecisYear, editTextYatayGecisScore, editTextYatayGecisEnglish;
    private EditText editTextYatayGecisFaculty, editTextYatayGecisBranch, editTextYatayGecisScore2;
    private String strTerm, strNoteGrade, strYear, strScore, strScore2, strEnglish, strFaculty, strBranch;
    private String strMakeApplicationType, strEducationType, strDisciplineType, strEducationType2, strScoreTypeDropDown1, strScoreTypeDropDown2;

    // visibility
    private EditText editTextYatayGecisNo;
    private String strNo;

    // ArrayList
    private ArrayList<Uri> fileUriList;
    private ArrayList<String> fileType;

    private TextInputLayout editTextYatayGecisNoWrap;


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
        fileType.add("Petition/");


        // file state
        image_yatayGecis_fileStateTranscript = (ImageView) findViewById(R.id.image_yatayGecis_fileStateTranscript);
        textView_yatayGecis_fileStateTranscript = (TextView) findViewById(R.id.textView_yatayGecis_fileStateTranscript);


        //kurum içi
        editTextYatayGecisNoWrap = (TextInputLayout) findViewById(R.id.editTextYatayGecisNoWrap);
        editTextYatayGecisNo = (EditText) findViewById(R.id.editTextYatayGecisNo);

        //EditText
        editTextYatayGecisTerm = (EditText) findViewById(R.id.editTextYatayGecisTerm);
        editTextYatayGecisNoteGrade = (EditText) findViewById(R.id.editTextYatayGecisNoteGrade);
        editTextYatayGecisYear = (EditText) findViewById(R.id.editTextYatayGecisYear);
        editTextYatayGecisScore = (EditText) findViewById(R.id.editTextYatayGecisScore);
        editTextYatayGecisEnglish = (EditText) findViewById(R.id.editTextYatayGecisEnglish);
        editTextYatayGecisFaculty = (EditText) findViewById(R.id.editTextYatayGecisFaculty);
        editTextYatayGecisBranch = (EditText) findViewById(R.id.editTextYatayGecisBranch);
        editTextYatayGecisScore2 = (EditText) findViewById(R.id.editTextYatayGecisScore2);


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
                //System.out.println(makeApplicationTypeDropDown.getText().toString());
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

    // string degerleri atama
    private void setString() {
        // editText
        strTerm = editTextYatayGecisTerm.getText().toString();
        strNoteGrade = editTextYatayGecisNoteGrade.getText().toString();
        strYear = editTextYatayGecisYear.getText().toString();
        strScore = editTextYatayGecisScore.getText().toString();
        strScore2 = editTextYatayGecisScore2.getText().toString();
        strEnglish = editTextYatayGecisEnglish.getText().toString();
        strFaculty = editTextYatayGecisFaculty.getText().toString();
        strBranch = editTextYatayGecisBranch.getText().toString();
        // combBox
        strMakeApplicationType = makeApplicationTypeDropDown.getText().toString();
        strEducationType = educationTypeDropDown.getText().toString();
        strDisciplineType = disciplineTypeDropDown.getText().toString();
        strEducationType2 = educationTypeDropDown2.getText().toString();
        strScoreTypeDropDown1 = scoreTypeDropDown1.getText().toString();
        strScoreTypeDropDown2 = scoreTypeDropDown2.getText().toString();

        //kurum ici
        strNo = editTextYatayGecisNo.getText().toString();

    }

    // input bos kontrolu
    private boolean isNotEmptyStrings() {
        setString();
        boolean result = TextUtils.isEmpty(strTerm) || TextUtils.isEmpty(strNoteGrade) || TextUtils.isEmpty(strYear) || TextUtils.isEmpty(strScore) || TextUtils.isEmpty(strScore2) || TextUtils.isEmpty(strFaculty) || TextUtils.isEmpty(strBranch)
                || TextUtils.isEmpty(strMakeApplicationType) || TextUtils.isEmpty(strEducationType) || TextUtils.isEmpty(strDisciplineType) || TextUtils.isEmpty(strEducationType2) || TextUtils.isEmpty(strScoreTypeDropDown1) || TextUtils.isEmpty(strScoreTypeDropDown2);
        if (makeApplicationTypeDropDown.getText().toString().equals("KURUMİÇİ YATAY GEÇİŞ BAŞVURUSU") && result == true) {
            result = TextUtils.isEmpty(strNo);
        }

        if (result)
            return false;
        return true;
    }

    // pdf start
    //https://github.com/LukeDaniel16/CreatePDFwithJavaOnAndroidStudio
    public void initPdf(String title) {
        flagPdf = true;
        flagFileTranscript = false;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATE_PDF);
    }

    // pdf icerik hazırla
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
        canvas.drawText("YATAY GEÇİŞ BAŞVURU FORMU", pageInfo.getPageWidth() / 2, 32, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(3);
        paint.setFakeBoldText(false);

        // text1
        paint.setFakeBoldText(true);
        canvas.drawText("I- BAŞVURU TÜRÜ", 30, 45, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(strMakeApplicationType, 30, 50, paint);

        //text2
        paint.setFakeBoldText(true);
        canvas.drawText("II- KİŞİSEL BİLGİLER", 30, 60, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("ADI SOYADI: " + usersData.getIncomingName() + usersData.getIncomingLastName(), 30, 65, paint);
        canvas.drawText("T.C. KİMLİK NO: " + usersData.getIncomingIdentity(), 30, 70, paint);
        canvas.drawText("DOĞUM TARİHİ: " + usersData.getIncomingBirthday(), 120, 70, paint);
        canvas.drawText("E-POSTA ADRESİ: " + usersData.getIncomingMail(), 30, 75, paint);
        canvas.drawText("TELEFON (GSM): " + usersData.getIncomingPhone(), 30, 80, paint);
        canvas.drawText("TEBLİGAT ADRES: " + usersData.getIncomingAddress(), 30, 85, paint);

        //text3
        paint.setFakeBoldText(true);
        canvas.drawText("III- ÖĞRENİMİNE İLİŞKİN BİLGİLER", 30, 95, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("HALEN KAYITLI OLDUĞU ÜNİVERSİTE: Kocaeli Üniversitesi", 30, 100, paint);
        canvas.drawText("HALEN KAYITLI OLDUĞU FAKÜLTE / YÜKSEKOKUL: " + usersData.getIncomingFaculty(), 30, 105, paint);
        canvas.drawText("HALEN KAYITLI OLDUĞU BÖLÜM / PROGRAM: " + usersData.getIncomingDepartment(), 30, 110, paint);
        canvas.drawText("ÖĞRETİM TÜRÜ:  " + strEducationType, 30, 115, paint);
        canvas.drawText("SINIF/ YARIYIL:    " + strTerm, 120, 115, paint);
        canvas.drawText("DİSİPLİN CEZASI ALIP ALMADIĞI: " + strDisciplineType, 30, 120, paint);
        canvas.drawText("GENEL AKADEMİK BAŞARI NOT ORTALAMASI:  " + strNoteGrade, 30, 125, paint);
        canvas.drawText("ÖĞRENCİ NUMARASI (KOCAELİ  ÜNİVERSİTESİ ÖĞRENCİLERİ İÇİN): " + strNo, 30, 130, paint);
        canvas.drawText("HALEN KAYITLI OLDUĞU YÜKSEKÖĞRETİM KURUMUNA YERLEŞTİRİLDİĞİ YIL:   " + strYear, 30, 135, paint);
        canvas.drawText("HALEN KAYITLI OLUNAN PROGRAMA YERLEŞTİRMEDE KULLANILAN PUAN TÜRÜ VE PUANI: " + strScoreTypeDropDown1 + " / " + strScore, 30, 140, paint);
        canvas.drawText("ZORUNLU HAZIRLIK SINIFI BULUNAN PROGRAMLARA BAŞVURAN ADAYLAR İÇİN YABANCI DİL PUANI:  " + strEnglish, 30, 145, paint);

        //text4
        paint.setFakeBoldText(true);
        canvas.drawText("IV – ADAYIN BAŞVURDUĞU YÜKSEKÖĞRETİM PROGRAMINA İLİŞKİN BİLGİLER", 30, 160, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("FAKÜLTE / YÜKSEKOKUL/MYO. ADI: " + strFaculty, 30, 165, paint);
        canvas.drawText("BÖLÜM / PROGRAM ADI:   " + strBranch, 30, 170, paint);
        canvas.drawText("ÖĞRETİM TÜRÜ:  " + strScoreTypeDropDown2, 30, 175, paint);
        canvas.drawText("BAŞVURULAN PROGRAMIN HALEN KAYITLI OLUNAN PROGRAMA YERLEŞTİRME YAPILDIĞI PUAN TÜRÜ VE PUANI: " + strScoreTypeDropDown2 + " / " + strScore2, 30, 180, paint);

        //text5
        canvas.drawText("Beyan ettiğim bilgilerin veya belgelerin gerçeğe aykırı olması veya daha önce yatay geçiş yapmış olmam ", 30, 190, paint);
        canvas.drawText("halinde hakkımda cezai işlemlerin yürütüleceğini ve kaydım yapılmış olsa dahi silineceğini bildiğimi kabul ", 30, 195, paint);
        canvas.drawText("ediyorum.", 30, 200, paint);

        canvas.drawText("Adayın Adı Soyadı: " + usersData.getIncomingName() + " " + usersData.getIncomingLastName(), 130, 205, paint);
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

    // pdf kaydet
    private void setPdf(Uri uri, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(Objects.requireNonNull(getContentResolver().openOutputStream(uri)));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "Pdf oluşturuldu.\n", Toast.LENGTH_LONG).show();
            petitionUri = uri;
            startActivity(new Intent(YatayGecisActivity.this, StudentHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Dosya hatası bulunamadı", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Giriş ve çıkış hatası", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Bilinmeyen hata" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //https://github.com/LukeDaniel16/CreatePDFwithJavaOnAndroidStudio
    // pdf end


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


    // transkript dosya sec
    public void selectFileTranscript(View view) {
        flagPdf = false;
        flagFileTranscript = true;
        selectFile();
    }


    // onayla buton
    public void submitYatayGecis(View view) {

        if (isNotEmptyStrings() && transcriptUri != null) {
            AlertDialog.Builder checkAlertDialog = new AlertDialog.Builder(YatayGecisActivity.this);
            checkAlertDialog.setTitle("Onaylama");
            checkAlertDialog.setMessage("Başvurunuzu tamamlamak istiyor musunuz?");
            checkAlertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    initPdf("YatayGecisBasvurusu");
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
            Toast.makeText(YatayGecisActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
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
        resourcesAdd.put("type", "Yatay Geciş");
        resourcesAdd.put("userUid", fUser.getUid());
        resourcesAdd.put("state", "0");
        resourcesAdd.put("transcriptPath", transcriptPath);
        resourcesAdd.put("petitionPath", petitionPath);
        resourcesAdd.put("date", strDate);

        firebaseFirestore.collection("Resources").document()
                .set(resourcesAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Resourcers kayıt ok");
            }
        });
    }


    // transkript firebase save
    private void saveTranscriptFileInStorage() {

        fileUriList.add(1, petitionUri);
        if (fileUriList.size() == 2) {

            for (int i = 0; i < fileUriList.size(); i++) {
                String extension = getMimeType(YatayGecisActivity.this, fileUriList.get(i));

                if (i == 0) {
                    transcriptPath = "YatayGecis/" + extension + "/" + fileType.get(i) + adjustFormat();
                } else if (i == 1) {
                    petitionPath = "YatayGecis/" + extension + "/" + fileType.get(i) + adjustFormat();
                }

                StorageReference reference = storageReference.child("YatayGecis").child(extension).child(fileType.get(i) + adjustFormat());

                reference.putFile(fileUriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // path alma
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        String linkUri = uriTask.getResult().getPath();
                        System.out.println("Uri: " + String.valueOf(linkUri));
                        System.out.println("YatayGecis dosya Kayıt Tamam.");
                        System.out.println("----------------------");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(YatayGecisActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean t1 = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_PDF && resultCode == RESULT_OK && data.getData() != null && flagPdf) {
            pdfUri = data.getData();
            createPdf(pdfUri); // pdf kaydet cihaz
            saveTranscriptFileInStorage();
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
            image_yatayGecis_fileStateTranscript.setImageResource(R.drawable.yes);
            textView_yatayGecis_fileStateTranscript.setText("Transkript Dosyasını Değiştir");
        }
    }
}