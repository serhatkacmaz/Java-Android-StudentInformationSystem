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
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class YazOkuluActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;

    //Linear Layout for cardView
    private LinearLayout yazOkulu_linearLayoutLesson1, yazOkulu_linearLayoutLesson2, yazOkulu_linearLayoutLesson3;
    private LinearLayout yazOkulu_linearLayoutDateBetween;
    private TextInputLayout yazOkulu_linearLayoutTakeFacultyInput1, yazOkulu_linearLayoutTakeFacultyInput2, yazOkulu_linearLayoutTakeFacultyInput3;
    private LinearLayout yazOkulu_linearLayoutTakeLesson1, yazOkulu_linearLayoutTakeLesson2, yazOkulu_linearLayoutTakeLesson3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaz_okulu);

        // sorumlu dersler for cardView
        yazOkulu_linearLayoutLesson1 = (LinearLayout) findViewById(R.id.yazOkulu_linearLayoutLesson1);
        yazOkulu_linearLayoutLesson2 = (LinearLayout) findViewById(R.id.yazOkulu_linearLayoutLesson2);
        yazOkulu_linearLayoutLesson3 = (LinearLayout) findViewById(R.id.yazOkulu_linearLayoutLesson3);

        // tarih aralığı for cardView
        yazOkulu_linearLayoutDateBetween = (LinearLayout) findViewById(R.id.yazOkulu_linearLayoutDateBetween);

        //Yaz doneminde alınan dersler for cardView
        yazOkulu_linearLayoutTakeFacultyInput1 = (TextInputLayout) findViewById(R.id.yazOkulu_linearLayoutTakeFacultyInput1);
        yazOkulu_linearLayoutTakeFacultyInput2 = (TextInputLayout) findViewById(R.id.yazOkulu_linearLayoutTakeFacultyInput2);
        yazOkulu_linearLayoutTakeFacultyInput3 = (TextInputLayout) findViewById(R.id.yazOkulu_linearLayoutTakeFacultyInput3);
        yazOkulu_linearLayoutTakeLesson1 = (LinearLayout) findViewById(R.id.yazOkulu_linearLayoutTakeLesson1);
        yazOkulu_linearLayoutTakeLesson2 = (LinearLayout) findViewById(R.id.yazOkulu_linearLayoutTakeLesson2);
        yazOkulu_linearLayoutTakeLesson3 = (LinearLayout) findViewById(R.id.yazOkulu_linearLayoutTakeLesson3);
    }

    //https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=17&t=456s
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandLessons(View view) {
        int v1 = (yazOkulu_linearLayoutLesson1.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int v2 = (yazOkulu_linearLayoutLesson2.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int v3 = (yazOkulu_linearLayoutLesson3.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutLesson1, new AutoTransition());
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutLesson2, new AutoTransition());
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutLesson3, new AutoTransition());
        yazOkulu_linearLayoutLesson1.setVisibility(v1);
        yazOkulu_linearLayoutLesson2.setVisibility(v2);
        yazOkulu_linearLayoutLesson3.setVisibility(v3);
    }
    //https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=17&t=456s

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandSchoolDateBetween(View view) {
        int v = (yazOkulu_linearLayoutDateBetween.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutDateBetween, new AutoTransition());
        yazOkulu_linearLayoutDateBetween.setVisibility(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandTakeLessons(View view) {
        int v1 = (yazOkulu_linearLayoutTakeFacultyInput1.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int v2 = (yazOkulu_linearLayoutTakeFacultyInput2.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int v3 = (yazOkulu_linearLayoutTakeFacultyInput3.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int v4 = (yazOkulu_linearLayoutTakeLesson1.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int v5 = (yazOkulu_linearLayoutTakeLesson2.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int v6 = (yazOkulu_linearLayoutTakeLesson3.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutTakeFacultyInput1, new AutoTransition());
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutTakeFacultyInput2, new AutoTransition());
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutTakeFacultyInput3, new AutoTransition());
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutTakeLesson1, new AutoTransition());
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutTakeLesson2, new AutoTransition());
        TransitionManager.beginDelayedTransition(yazOkulu_linearLayoutTakeLesson3, new AutoTransition());

        yazOkulu_linearLayoutTakeFacultyInput1.setVisibility(v1);
        yazOkulu_linearLayoutTakeFacultyInput2.setVisibility(v2);
        yazOkulu_linearLayoutTakeFacultyInput3.setVisibility(v3);
        yazOkulu_linearLayoutTakeLesson1.setVisibility(v4);
        yazOkulu_linearLayoutTakeLesson2.setVisibility(v5);
        yazOkulu_linearLayoutTakeLesson3.setVisibility(v6);
    }
}