package com.example.yazilimlab.Catogery;

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

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class YazOkuluActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;


    private int tableRow = 3;
    // sorumlu olunan dersler
    ArrayList<String> lessonList;
    private EditText editYazOkuluLessonName, editYazOkuluLessonT, editYazOkuluLessonU, editYazOkuluLessonL, editYazOkuluLessonAKTS;
    private String strLessonName, strLessonT, strLessonU, strLessonL, strLessonAKTS;

    // yaz okulunda alıncak
    ArrayList<String> takeLessonList;
    private EditText editYazOkuluTakeFaculty, editYazOkuluTakeLessonName, editYazOkuluTakeLessonT, editYazOkuluTakeLessonU, editYazOkuluTakeLessonL, editYazOkuluTakeLessonAKTS;
    private String strTakeFaculty, strTakeLessonName, strTakeLessonT, strTakeLessonU, strTakeLessonL, strTakeLessonAKTS;

    private void init() {
        //sorumlu olunan dersler
        lessonList = new ArrayList<String>();
        editYazOkuluLessonName = (EditText) findViewById(R.id.editYazOkuluLessonName);
        editYazOkuluLessonT = (EditText) findViewById(R.id.editYazOkuluLessonT);
        editYazOkuluLessonU = (EditText) findViewById(R.id.editYazOkuluLessonU);
        editYazOkuluLessonL = (EditText) findViewById(R.id.editYazOkuluLessonL);
        editYazOkuluLessonAKTS = (EditText) findViewById(R.id.editYazOkuluLessonAKTS);

        //yaz okulunda alıncak
        takeLessonList = new ArrayList<String>();
        editYazOkuluTakeFaculty = (EditText) findViewById(R.id.editYazOkuluTakeFaculty);
        editYazOkuluTakeLessonName = (EditText) findViewById(R.id.editYazOkuluTakeLessonName);
        editYazOkuluTakeLessonT = (EditText) findViewById(R.id.editYazOkuluTakeLessonT);
        editYazOkuluTakeLessonU = (EditText) findViewById(R.id.editYazOkuluTakeLessonU);
        editYazOkuluTakeLessonL = (EditText) findViewById(R.id.editYazOkuluTakeLessonL);
        editYazOkuluTakeLessonAKTS = (EditText) findViewById(R.id.editYazOkuluTakeLessonAKTS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaz_okulu);
        init();

    }

    public void createPdf(View view) {

    }


    // sorumlu olunan dersler start
    private void setTextStringLesson() {
        strLessonName = editYazOkuluLessonName.getText().toString();
        strLessonT = editYazOkuluLessonT.getText().toString();
        strLessonU = editYazOkuluLessonU.getText().toString();
        strLessonL = editYazOkuluLessonL.getText().toString();
        strLessonAKTS = editYazOkuluLessonAKTS.getText().toString();
    }

    private boolean isNotEmptyLesson() {
        setTextStringLesson();
        boolean result = TextUtils.isEmpty(strLessonName) || TextUtils.isEmpty(strLessonT) || TextUtils.isEmpty(strLessonU) || TextUtils.isEmpty(strLessonL) || TextUtils.isEmpty(strLessonAKTS);
        System.out.println(result);
        if (result)
            return false;
        return true;
    }

    private void lessonTextDelete() {
        editYazOkuluLessonName.setText("");
        editYazOkuluLessonT.setText("");
        editYazOkuluLessonU.setText("");
        editYazOkuluLessonL.setText("");
        editYazOkuluLessonAKTS.setText("");
    }

    public void yazOkuluLessonAdd(View view) {

        if (lessonList.size() < tableRow) {
            if (isNotEmptyLesson()) {
                String temp = strLessonName + ";" + strLessonT + ";" + strLessonU + ";" + strLessonL + ";" + strLessonAKTS;
                lessonList.add(temp);

                for (int i = 0; i < lessonList.size(); i++) {
                    System.out.println(lessonList.get(i));
                }
                System.out.println("----------------------------------------------");
                lessonTextDelete();
            } else {
                Toast.makeText(YazOkuluActivity.this, "Boş alanlar var", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(YazOkuluActivity.this, String.valueOf(tableRow) + " adet ekliyebilirsin", Toast.LENGTH_SHORT).show();
        }
    }
    // sorumlu olunan dersler end


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