package com.example.yazilimlab;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.yazilimlab.Model.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    // combo box for InfoClass
    private AutoCompleteTextView infoClassDropDown, facultyDropDown, departmentDropDown;
    ArrayList<String> arrayListInfoClass, arrayListFaculty, arrayListDepartment;
    ArrayAdapter<String> arrayAdapterInfoClass, arrayAdapterFaculty, arrayAdapterDepartment;

    private EditText editTextRegisterNumber, editTextRegisterMail, editTextRegisterName, editTextRegisterLastName, editTextRegisterPhone, editTextRegisterIdentity,
            editTextRegisterAddress, editTextRegisterBirthday, editTextRegisterUniversity,
            editTextRegisterPassword, autoCompleteRegisterInfoClass;

    private UserRegister userRegister;
    public String strNumber, strMail, strName, strLastName, strPhone, strIdentity, strAddress, strInfoClass, strBirthday, strUniversity, strFaculty, strDepartment, strPassword;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;


    //img
    private ImageView imageRegisterProfile;
    private final int IMG_REQUEST_ID = 10;
    private Uri imgUri;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        docRef.get().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<DocumentSnapshot>() {
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

        //DropDown
        infoClassDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteRegisterInfoClass);
        facultyDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteRegisterFaculty);
        departmentDropDown = (AutoCompleteTextView) findViewById(R.id.autoCompleteRegisterDep);

        editTextRegisterNumber = (EditText) findViewById(R.id.editTextRegisterNumber);
        editTextRegisterMail = (EditText) findViewById(R.id.editTextRegisterMail);
        editTextRegisterName = (EditText) findViewById(R.id.editTextRegisterName);
        editTextRegisterLastName = (EditText) findViewById(R.id.editTextRegisterLastName);
        editTextRegisterPhone = (EditText) findViewById(R.id.editTextRegisterPhone);
        editTextRegisterIdentity = (EditText) findViewById(R.id.editTextRegisterIdentity);
        editTextRegisterAddress = (EditText) findViewById(R.id.editTextRegisterAddress);
        editTextRegisterBirthday = (EditText) findViewById(R.id.editTextRegisterBirthday);
        editTextRegisterUniversity = (EditText) findViewById(R.id.editTextRegisterUniversity);
        editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
        imageRegisterProfile = (ImageView) findViewById(R.id.imageRegisterProfile);
        autoCompleteRegisterInfoClass = (AutoCompleteTextView) findViewById(R.id.autoCompleteRegisterInfoClass);


        // ComboBox
        arrayListInfoClass = new ArrayList<>();
        arrayListInfoClass.add("1.Sınıf");
        arrayListInfoClass.add("2.Sınıf");
        arrayListInfoClass.add("3.Sınıf");
        arrayListInfoClass.add("4.Sınıf");
        arrayListInfoClass.add("Hazırlık");
        arrayAdapterInfoClass = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayListInfoClass);
        infoClassDropDown.setAdapter(arrayAdapterInfoClass);

        // fakulte - bolum
        getDataComboBoxFaculty();

        //img
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    //img choice start
    // https://www.youtube.com/watch?v=_uW3yRhy0MU start
    public void buttonSelectPhoto(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Profil Seç"), IMG_REQUEST_ID);
    }

    private String adjustFormat() {
        String number, name, lastName;
        number = editTextRegisterNumber.getText().toString();
        name = editTextRegisterName.getText().toString();
        lastName = editTextRegisterLastName.getText().toString();

        // https://www.javatpoint.com/java-get-current-date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        String strDate= formatter.format(date);
        // https://www.javatpoint.com/java-get-current-date

        return number + "_" + name + "_" + lastName + "_" + strDate;
    }

    private void saveInFirebase() {
        if (imgUri != null) {
            try {

                StorageReference reference = storageReference.child("picture/" + adjustFormat());
                reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RegisterActivity.this, "Tamamlandı", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST_ID && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageRegisterProfile.setImageBitmap(bitmapImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // https://www.youtube.com/watch?v=_uW3yRhy0MU end
    //img choice end


    //save start
    public void buttonRegister(View v) {
        incomingData();
        boolean flag = isNotEmpty();

        if (flag) {
            fAuth.createUserWithEmailAndPassword(strMail, strPassword)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fUser = fAuth.getCurrentUser();
                                if (fUser != null) {
                                    setData(fUser.getUid());
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(RegisterActivity.this, "Eksik veriler var", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(String Uid) {
        userRegister = new UserRegister(strNumber, strMail, strName, strLastName, strPhone, strAddress, strInfoClass, strAddress, strBirthday, strUniversity, strFaculty, strDepartment, strPassword);

        firebaseFirestore.collection("Users").document(Uid)
                .set(userRegister).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    saveInFirebase();
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void incomingData() {
        strNumber = editTextRegisterNumber.getText().toString();
        strMail = editTextRegisterMail.getText().toString();
        strName = editTextRegisterName.getText().toString();
        strLastName = editTextRegisterLastName.getText().toString();
        strPhone = editTextRegisterPhone.getText().toString();
        strIdentity = editTextRegisterIdentity.getText().toString();
        strAddress = editTextRegisterAddress.getText().toString();
        strInfoClass = infoClassDropDown.getText().toString();
        strBirthday = editTextRegisterBirthday.getText().toString();
        strUniversity = editTextRegisterUniversity.getText().toString();
        strFaculty = facultyDropDown.getText().toString();
        strDepartment = departmentDropDown.getText().toString();
        strPassword = editTextRegisterPassword.getText().toString();
    }

    private boolean isNotEmpty() {
        boolean result = TextUtils.isEmpty(strNumber) || TextUtils.isEmpty(strMail) || TextUtils.isEmpty(strName) || TextUtils.isEmpty(strLastName) || TextUtils.isEmpty(strPhone) || TextUtils.isEmpty(strIdentity) ||
                TextUtils.isEmpty(strAddress) || TextUtils.isEmpty(strInfoClass) || TextUtils.isEmpty(strBirthday) || TextUtils.isEmpty(strUniversity) ||
                TextUtils.isEmpty(strFaculty) || TextUtils.isEmpty(strDepartment) || TextUtils.isEmpty(strPassword) || imgUri == null;

        if (result)
            return false;
        return true;
    }
    // save end
}