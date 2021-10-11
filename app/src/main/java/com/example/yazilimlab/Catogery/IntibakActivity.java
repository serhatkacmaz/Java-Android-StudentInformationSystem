package com.example.yazilimlab.Catogery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

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

    private AutoCompleteTextView facultyDropDown, departmentDropDown;
    ArrayList<String> arrayListInfoClass, arrayListFaculty, arrayListDepartment;
    ArrayAdapter<String> arrayAdapterInfoClass, arrayAdapterFaculty, arrayAdapterDepartment;

    private EditText editIntibakOldSchool, editIntibakOldFaculty, editIntibakOldBranch;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;

    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intibak);
        init();

        // Fakulte ismi degistiğinde
        facultyDropDown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getDataComboBoxDepartment();
            }
        });

    }

    private void getDataComboBoxFaculty() {
        arrayListFaculty = new ArrayList<>();
        firebaseFirestore.collection("Faculties")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // System.out.println(document.getId() + " => " + document.getData());
                                arrayListFaculty.add(document.getId());
                            }
                        } else {

                        }
                    }
                });

        arrayAdapterFaculty = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListFaculty);
        facultyDropDown.setAdapter(arrayAdapterFaculty);
    }


    private void getDataComboBoxDepartment() {
        arrayListDepartment = new ArrayList<>();
        docRef = firebaseFirestore.collection("Faculties").document(facultyDropDown.getText().toString());
        docRef.get().addOnSuccessListener(IntibakActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //System.out.println(documentSnapshot.getData().values().size());
                    for (int i = 0; i < documentSnapshot.getData().values().size(); i++) {
                        //System.out.println(documentSnapshot.getData().get(String.valueOf(i)));
                        arrayListDepartment.add(String.valueOf(documentSnapshot.getData().get(String.valueOf(i))));
                    }
                }
            }
        });

        arrayAdapterDepartment = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListDepartment);
        departmentDropDown.setAdapter(arrayAdapterDepartment);
    }

    private void init() {
        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Dropdown
        facultyDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteIntibakFaculty);
        departmentDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteIntibakBranch);

        editIntibakOldSchool = (EditText) findViewById(R.id.editIntibakOldSchool);
        editIntibakOldFaculty = (EditText) findViewById(R.id.editIntibakOldFaculty);
        editIntibakOldBranch = (EditText) findViewById(R.id.editIntibakOldBranch);

        // fakulte - bolum
        getDataComboBoxFaculty();
    }
}