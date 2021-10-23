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

public class AdminIncomingList {

    // RecyclerViewIncoming
    RecyclerView admin_RecyclerViewIncoming;
    ArrayList<AdminAppItemInfo> adminAppItemInfoIncomingArrayList;
    AdminAppItemAdapter adminAppItemIncomingAdapter;

    // firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    StorageReference storageReference;

    // CardView Extend
    private CardView admin_CardViewIncoming;
    private LinearLayout admin_LinearLayoutIncoming;

    private String type;

    public AdminIncomingList(Context context, RecyclerView admin_RecyclerViewIncoming, CardView admin_CardViewIncoming, LinearLayout admin_LinearLayoutIncoming, String type) {
        // input
        this.admin_RecyclerViewIncoming = admin_RecyclerViewIncoming;
        this.admin_CardViewIncoming = admin_CardViewIncoming;
        this.admin_LinearLayoutIncoming = admin_LinearLayoutIncoming;
        this.type = type;

        // Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // recyclerViewIncoming
        admin_RecyclerViewIncoming.setHasFixedSize(true);
        admin_RecyclerViewIncoming.setLayoutManager(new LinearLayoutManager(context));
        adminAppItemInfoIncomingArrayList = new ArrayList<AdminAppItemInfo>();
        adminAppItemIncomingAdapter = new AdminAppItemAdapter(adminAppItemInfoIncomingArrayList, context);
        admin_RecyclerViewIncoming.setAdapter(adminAppItemIncomingAdapter);
        eventChangeListenerIncoming();
    }

    // gelen başvuruları listeleme
    private void eventChangeListenerIncoming() {
        firebaseFirestore.collection("Resources").whereEqualTo("type", type)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                // gelen başvurular
                                if (dc.getDocument().getData().get("state").equals("1")) {
                                    System.out.println(dc.getDocument().toObject(AdminAppItemInfo.class));
                                    adminAppItemInfoIncomingArrayList.add(dc.getDocument().toObject(AdminAppItemInfo.class));
                                }
                            }
                            adminAppItemIncomingAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public AdminAppItemAdapter adminAppItemAdapter() {
        return adminAppItemIncomingAdapter;
    }

    public CardView admin_CardViewIncoming() {
        return admin_CardViewIncoming;
    }

    // refresh gelen basvuru
    public void refreshIncomingCardView() {
        adminAppItemInfoIncomingArrayList.clear();
        eventChangeListenerIncoming();
    }
}