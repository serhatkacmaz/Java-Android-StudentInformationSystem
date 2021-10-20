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
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class DgsActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    FirebaseStorage storage;
    StorageReference storageReference;

    // hashMap
    private HashMap<String, String> resourcesAdd;

    // path
    private String transcriptPath, lessonPath;

    private ArrayList<Uri> fileUriList;
    private ArrayList<String> fileType;

    // input
    private EditText editTextDgsFaculty, editTextDgsBranch, editTextDgsNo;
    private String strEditTextDgsBranch, strEditTextDgsFaculty, strEditTextDgsNo;

    // code
    private static final int CREATE_PDF = 1;
    private static final int PICK_FILE = 1;
    // Uri
    private Uri transcriptUri, lessonUri, pdfUri;

    // image file state
    private ImageView image_Dgs_fileStateTranscript, image_Dgs_fileStateLessonFile;
    private TextView textView_Dgs_fileStateTranscript, textView_Dgs_fileStateLessonFile;

    // flag for activityResult
    private boolean flagPdf, flagFileTranscript, flagFileLesson;

    // incoming data
    private UsersData usersData;

    // init
    public void init() {
        editTextDgsBranch = (EditText) findViewById(R.id.editTextDgsBranch);
        editTextDgsFaculty = (EditText) findViewById(R.id.editTextDgsFaculty);
        editTextDgsNo = (EditText) findViewById(R.id.editTextDgsNo);

        //arrayList
        fileUriList = new ArrayList<Uri>();
        fileType = new ArrayList<String>();
        fileType.add("Transcript/");
        fileType.add("LessonContents/");

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        usersData = new UsersData();

        //file state input
        image_Dgs_fileStateTranscript = (ImageView) findViewById(R.id.image_Dgs_fileStateTranscript);
        image_Dgs_fileStateLessonFile = (ImageView) findViewById(R.id.image_Dgs_fileStateLessonFile);
        textView_Dgs_fileStateTranscript = (TextView) findViewById(R.id.textView_Dgs_fileStateTranscript);
        textView_Dgs_fileStateLessonFile = (TextView) findViewById(R.id.textView_Dgs_fileStateLessonFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dgs);
        init();
    }

    // str set input
    private void setTextString() {
        strEditTextDgsBranch = editTextDgsBranch.getText().toString();
        strEditTextDgsFaculty = editTextDgsFaculty.getText().toString();
        strEditTextDgsNo = editTextDgsNo.getText().toString();
    }

    // input bos mu kontrol
    private boolean isNotEmptyString() {
        setTextString();
        boolean result = TextUtils.isEmpty(strEditTextDgsBranch) || TextUtils.isEmpty(strEditTextDgsFaculty) || TextUtils.isEmpty(strEditTextDgsNo);

        if (result)
            return false;
        return true;
    }


    //->pdf start

    // https://github.com/LukeDaniel16/CreatePDFwithJavaOnAndroidStudio
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

    // pdf icerik olustur
    private void createPdf(Uri uri) {

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint s = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(4);
        paint.setFakeBoldText(true);

        // aktif tarih
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);

        canvas.drawText("T.C.", pageInfo.getPageWidth() / 2, 36, paint);
        canvas.drawText("KOCAELİ ÜNİVERSİTESİ", pageInfo.getPageWidth() / 2, 42, paint);
        canvas.drawText("DİKEY GEÇİŞ BAŞVURU FORMU", pageInfo.getPageWidth() / 2, 48, paint);
        paint.setTextSize(3);
        paint.setFakeBoldText(true);
        canvas.drawText("(Öğrenci İşleri Daire Başkanlığına)", pageInfo.getPageWidth() / 2, 52, paint);
        paint.setFakeBoldText(false);

        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("       Üniversiteniz " + usersData.getIncomingFaculty() + " Fakültesi " + usersData.getIncomingDepartment(), 39, 80, paint);
        canvas.drawText("Bölümüne Dikey Geçiş Sınavı ile yerleştirildim. Daha önce bitirmiş olduğum okuldaki", 39, 84, paint);
        canvas.drawText("transkriptim ve ders içerikleri ekte sunulmuştur.", 39, 88, paint);
        canvas.drawText("Gerekli ders muafiyetimin ve sınıf intibakımın yapılabilmesi için gereğini arz ederim. ", 39, 100, paint);
        canvas.drawText("Öğrenci Numarası: " + usersData.getIncomingNumber(), 39, 115, paint);
        canvas.drawText("Tarih: " + strDate, 140, 115, paint);
        canvas.drawText("Adı Soyadı: " + usersData.getIncomingName() + " " + usersData.getIncomingLastName(), 140, 125, paint);
        canvas.drawText("İmza", 146, 129, paint);
        canvas.drawText("Telefon Numarası: " + usersData.getIncomingPhone(), 39, 120, paint);
        canvas.drawText("Adres: " + usersData.getIncomingAddress(), 39, 125, paint);
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
    //https://github.com/LukeDaniel16/CreatePDFwithJavaOnAndroidStudio

    //->pdf end


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

    // transkript dosyasi seç buton
    public void selectFileTranscript(View view) {
        flagPdf = false;
        flagFileTranscript = true;
        flagFileLesson = false;
        Intent intent = new Intent();
        selectFile();
    }

    // ders listesi dosyasi seç buton
    public void selectFileLessonFile(View view) {
        flagPdf = false;
        flagFileTranscript = false;
        flagFileLesson = true;
        Intent intent = new Intent();
        selectFile();
    }

    // Dgs onayla butonu
    public void submitDGS(View view) {

        // isNotEmptyString() &&
        if (transcriptUri != null && lessonUri != null) {
            AlertDialog.Builder checkAlertDialog = new AlertDialog.Builder(DgsActivity.this);
            checkAlertDialog.setTitle("Onaylama");
            checkAlertDialog.setMessage("Başvurunuzu tamamlamak istiyor musunuz?");
            checkAlertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    initPdf("DgsBaşvuru");
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
            Toast.makeText(DgsActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
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
        resourcesAdd.put("type", "DGS");
        resourcesAdd.put("userUid", fUser.getUid());
        resourcesAdd.put("state", "0");
        resourcesAdd.put("transcriptPath", transcriptPath);
        resourcesAdd.put("lessonPath", lessonPath);
        resourcesAdd.put("date",strDate);

        firebaseFirestore.collection("Resources").document()
                .set(resourcesAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Resourcers kayıt ok");
            }
        });
    }

    // firebase save files
    private void saveFileInStorage() {
        if (fileUriList.size() == 2) {

            for (int i = 0; i < fileUriList.size(); i++) {
                String extension = getMimeType(DgsActivity.this, fileUriList.get(i));

                if (i == 0) {
                    transcriptPath = "DGS/" + extension + "/" + fileType.get(i) + adjustFormat();
                } else if (i == 1) {
                    lessonPath = "DGS/" + extension + "/" + fileType.get(i) + adjustFormat();
                }

                StorageReference reference = storageReference.child("DGS").child(extension).child(fileType.get(i) + adjustFormat());

                reference.putFile(fileUriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // path alma
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        String linkUri = uriTask.getResult().getPath();
                        System.out.println("Uri: " + String.valueOf(linkUri));
                        System.out.println("Dgs dosya Kayıt Tamam.");
                        System.out.println("----------------------");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DgsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // pdf ve dosyaları kaydet
        if (requestCode == CREATE_PDF && resultCode == RESULT_OK && data.getData() != null && flagPdf) {
            pdfUri = data.getData();
            createPdf(pdfUri);  // pdf
            saveFileInStorage();  // file save
            saveResources();
        } else if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null && flagFileTranscript) {

            // dosya transkiript seçme
            transcriptUri = data.getData();
            fileUriList.add(transcriptUri);
            image_Dgs_fileStateTranscript.setImageResource(R.drawable.yes);
            textView_Dgs_fileStateTranscript.setText("Transkript Dosyasını Değiştir");
        } else if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null && flagFileLesson) {

            // dosya lesson seçme
            lessonUri = data.getData();
            fileUriList.add(lessonUri);
            image_Dgs_fileStateLessonFile.setImageResource(R.drawable.yes);
            textView_Dgs_fileStateLessonFile.setText("Ders Listesi Dosyasını Değiştir");
        }
    }
}