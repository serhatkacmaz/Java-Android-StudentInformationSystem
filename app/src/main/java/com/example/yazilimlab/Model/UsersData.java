package com.example.yazilimlab.Model;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.yazilimlab.Catogery.CapActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UsersData {

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;

    // incoming data
    private String incomingFaculty, incomingDepartment, incomingNumber, incomingName, incomingLastName, incomingClass, incomingPhone, incomingMail, incomingAddress;

    public String getIncomingFaculty() {
        return incomingFaculty;
    }

    public String getIncomingDepartment() {
        return incomingDepartment;
    }

    public String getIncomingNumber() {
        return incomingNumber;
    }

    public String getIncomingName() {
        return incomingName;
    }

    public String getIncomingLastName() {
        return incomingLastName;
    }

    public String getIncomingClass() {
        return incomingClass;
    }

    public String getIncomingPhone() {
        return incomingPhone;
    }

    public String getIncomingMail() {
        return incomingMail;
    }

    public String getIncomingAddress() {
        return incomingAddress;
    }

    public UsersData() {
        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fUser = fAuth.getCurrentUser();
        incomingData();

        System.out.println(fUser.getUid());
    }

    private void incomingData() {

        DocumentReference docRef = firebaseFirestore.collection("Users").document(fUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                incomingFaculty = documentSnapshot.getData().get("strFaculty").toString();
                incomingDepartment = documentSnapshot.getData().get("strDepartment").toString();
                incomingNumber = documentSnapshot.getData().get("strNumber").toString();
                incomingName = documentSnapshot.getData().get("strName").toString();
                incomingLastName = documentSnapshot.getData().get("strLastName").toString();
                incomingClass = documentSnapshot.getData().get("strInfoClass").toString();
                incomingPhone = documentSnapshot.getData().get("strPhone").toString();
                incomingMail = documentSnapshot.getData().get("strMail").toString();
                incomingAddress = documentSnapshot.getData().get("strAddress").toString();
            }
        });
    }

}
