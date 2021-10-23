package com.example.yazilimlab.Model;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yazilimlab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminIncomingList extends AdminResources {

    public AdminIncomingList(Context context, RecyclerView admin_RecyclerViewIncoming, CardView admin_CardViewIncoming, LinearLayout admin_LinearLayoutIncoming, String type, String state) {
        super(context, admin_RecyclerViewIncoming, admin_CardViewIncoming, admin_LinearLayoutIncoming, type, state);
    }
}