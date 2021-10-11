package com.example.yazilimlab.Catogery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.R;
import com.example.yazilimlab.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;

public class IntibakActivity extends AppCompatActivity {


    private EditText editIntibakOldSchool, editIntibakOldFaculty, editIntibakOldBranch;
    private TextView text_intibakActivity_title;


    // Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;

    private int tableRow = 16;

    // daha önce aldığım
    ArrayList<String> oldLessonList;
    private EditText editYazOkuluOldLesson, editYazOkuluOldLessonT, editYazOkuluOldLessonUL, editYazOkuluOldLessonK, editYazOkuluOldLessonAKTS;
    private String strOldLessonName, strOldLessonT, strOldLessonUL, strOldLessonK, strOldLessonAKTS;


    // muaf olmak istediğim
    ArrayList<String> exemptLessonList;
    private EditText editYazOkuluExemptLessonCode, editYazOkuluExemptLessonName, editYazOkuluExemptLessonT, editYazOkuluExemptLessonUL, editYazOkuluExemptLessonK, editYazOkuluExemptLessonAKTS;
    private String strExemptLessonCode, strExemptLessonName, strExemptLessonT, strExemptLessonUL, strExemptLessonK, strExemptLessonAKTS;

    private void init() {

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        editIntibakOldSchool = (EditText) findViewById(R.id.editIntibakOldSchool);
        editIntibakOldFaculty = (EditText) findViewById(R.id.editIntibakOldFaculty);
        editIntibakOldBranch = (EditText) findViewById(R.id.editIntibakOldBranch);
        text_intibakActivity_title = (TextView) findViewById(R.id.text_intibakActivity_title);

        // daha önce aldığım
        oldLessonList = new ArrayList<String>();
        editYazOkuluOldLesson = (EditText) findViewById(R.id.editYazOkuluOldLesson);
        editYazOkuluOldLessonT = (EditText) findViewById(R.id.editYazOkuluOldLessonT);
        editYazOkuluOldLessonUL = (EditText) findViewById(R.id.editYazOkuluOldLessonUL);
        editYazOkuluOldLessonK = (EditText) findViewById(R.id.editYazOkuluOldLessonK);
        editYazOkuluOldLessonAKTS = (EditText) findViewById(R.id.editYazOkuluOldLessonAKTS);

        // muaf olmak istediğim
        exemptLessonList = new ArrayList<String>();
        editYazOkuluExemptLessonCode = (EditText) findViewById(R.id.editYazOkuluExemptLessonCode);
        editYazOkuluExemptLessonName = (EditText) findViewById(R.id.editYazOkuluExemptLessonName);
        editYazOkuluExemptLessonT = (EditText) findViewById(R.id.editYazOkuluExemptLessonT);
        editYazOkuluExemptLessonUL = (EditText) findViewById(R.id.editYazOkuluExemptLessonUL);
        editYazOkuluExemptLessonK = (EditText) findViewById(R.id.editYazOkuluExemptLessonK);
        editYazOkuluExemptLessonAKTS = (EditText) findViewById(R.id.editYazOkuluExemptLessonAKTS);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intibak);
        init();
    }


    // daha önce aldığım start
    private void setTextStringOldLesson() {
        strOldLessonName = editYazOkuluOldLesson.getText().toString();
        strOldLessonT = editYazOkuluOldLessonT.getText().toString();
        strOldLessonUL = editYazOkuluOldLessonUL.getText().toString();
        strOldLessonK = editYazOkuluOldLessonK.getText().toString();
        strOldLessonAKTS = editYazOkuluOldLessonAKTS.getText().toString();
    }

    private boolean isNotEmptyOldLesson() {
        setTextStringOldLesson();
        boolean result = TextUtils.isEmpty(strOldLessonName) || TextUtils.isEmpty(strOldLessonT) || TextUtils.isEmpty(strOldLessonUL) || TextUtils.isEmpty(strOldLessonK) || TextUtils.isEmpty(strOldLessonAKTS);

        if (result)
            return false;
        return true;
    }

    private void oldLessonTextDelete() {
        editYazOkuluOldLesson.setText("");
        editYazOkuluOldLessonT.setText("");
        editYazOkuluOldLessonUL.setText("");
        editYazOkuluOldLessonK.setText("");
        editYazOkuluOldLessonAKTS.setText("");
    }

    public void yazOkuluOldLessonAdd(View view) {

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
                Toast.makeText(IntibakActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(IntibakActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }


    }
    // daha önce aldığım end


    // muaf olmak istediğim start
    private void setTextStringExemptLesson() {
        strExemptLessonCode = editYazOkuluExemptLessonCode.getText().toString();
        strExemptLessonName = editYazOkuluExemptLessonName.getText().toString();
        strExemptLessonT = editYazOkuluExemptLessonT.getText().toString();
        strExemptLessonUL = editYazOkuluExemptLessonUL.getText().toString();
        strExemptLessonK = editYazOkuluExemptLessonK.getText().toString();
        strExemptLessonAKTS = editYazOkuluExemptLessonAKTS.getText().toString();
    }

    private boolean isNotEmptyExemptLesson() {
        setTextStringExemptLesson();
        boolean result = TextUtils.isEmpty(strExemptLessonCode) || TextUtils.isEmpty(strExemptLessonName) || TextUtils.isEmpty(strExemptLessonT) || TextUtils.isEmpty(strExemptLessonUL) || TextUtils.isEmpty(strExemptLessonK) || TextUtils.isEmpty(strExemptLessonAKTS);

        if (result)
            return false;
        return true;
    }

    private void exemptLessonTextDelete() {
        editYazOkuluExemptLessonCode.setText("");
        editYazOkuluExemptLessonName.setText("");
        editYazOkuluExemptLessonT.setText("");
        editYazOkuluExemptLessonUL.setText("");
        editYazOkuluExemptLessonK.setText("");
        editYazOkuluExemptLessonAKTS.setText("");
    }

    public void yazOkuluExemptLessonAdd(View view) {

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
                Toast.makeText(IntibakActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(IntibakActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }
    }
    // muaf olmak istediğim end


    public void createPdf(View view) {

    }
}