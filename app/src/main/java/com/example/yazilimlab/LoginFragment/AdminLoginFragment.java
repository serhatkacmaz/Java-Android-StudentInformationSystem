package com.example.yazilimlab.LoginFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.AdminHomeActivity;
import com.example.yazilimlab.R;
import com.example.yazilimlab.StudentHomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;


public class AdminLoginFragment extends Fragment {

    // input
    private EditText editTextAdminLoginUserName, editTextAdminLoginPassword;
    private String strUser, strPassword;
    private Button buttonLogin;

    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;

    //beni hatirla
    private Switch switch_adminLogin_RememberMe;
    private String getUserName, getUserPassword;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean getCheckRememberMe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextAdminLoginUserName = (EditText) view.findViewById(R.id.editTextAdminLoginUserName);
        editTextAdminLoginPassword = (EditText) view.findViewById(R.id.editTextAdminLoginPassword);
        buttonLogin = (Button) view.findViewById(R.id.adminLoginButtonLogin);
        switch_adminLogin_RememberMe = (Switch) view.findViewById(R.id.switch_adminLogin_RememberMe);


        // telefon hazfızasında dosya oluşturup verileri içinde saklar
        preferences = this.getActivity().getSharedPreferences("com.example.yazilimlab", Context.MODE_PRIVATE);


        // beni hatırla için, kayıtlı verileri alma
        getUserName = preferences.getString("adminName", null);
        getUserPassword = preferences.getString("adminPassword", null);
        getCheckRememberMe = preferences.getBoolean("adminSwitch", false);


        // beni hatırla kayıtılı veriler varsa otomatik yaz
        if (getCheckRememberMe && !TextUtils.isEmpty(getUserName)) {
            editTextAdminLoginUserName.setText(getUserName);
            editTextAdminLoginPassword.setText(getUserPassword);
            switch_adminLogin_RememberMe.setChecked(true);
        }

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonLogin();
            }
        });

    }

    // beni hatırla için local olarak kayıt tutma
    private void rememberMe() {
        if (switch_adminLogin_RememberMe.isChecked()) {
            editor = preferences.edit();
            editor.putString("adminName", strUser);  //key'e göre kaydettik
            editor.putString("adminPassword", strPassword);
            editor.putBoolean("adminSwitch", true);
            editor.apply();
        } else {
            // switch kapalı ise boş kaydet
            editor = preferences.edit();
            editor.putString("adminName", null);
            editor.putString("adminPassword", null);
            editor.putBoolean("adminSwitch", false);
            editor.apply();
        }
    }

    // Giris yap
    private void setButtonLogin() {
        strUser = editTextAdminLoginUserName.getText().toString();
        strPassword = editTextAdminLoginPassword.getText().toString();

        if (!TextUtils.isEmpty(strUser) && !TextUtils.isEmpty(strPassword)) {
            rememberMe();
            fAuth.signInWithEmailAndPassword(strUser, strPassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            fUser = fAuth.getCurrentUser();
                            docRef = firebaseFirestore.collection("Users").document(fUser.getUid());
                            docRef.get().addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.getData().get("isAdmin") != null) {
                                        Intent adminIntent = new Intent(getActivity(), AdminHomeActivity.class);
                                        startActivity(adminIntent);
                                    } else {
                                        Toast.makeText(getActivity(), "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(getActivity(), "Kullanıcı adı veya şifre boş", Toast.LENGTH_SHORT).show();
        }
    }
}