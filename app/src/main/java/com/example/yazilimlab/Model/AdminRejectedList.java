package com.example.yazilimlab.Model;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

public class AdminRejectedList extends AdminResources {
    public AdminRejectedList(Context context, RecyclerView admin_RecyclerViewIncoming, CardView admin_CardViewIncoming, LinearLayout admin_LinearLayoutIncoming, String type, String state) {
        super(context, admin_RecyclerViewIncoming, admin_CardViewIncoming, admin_LinearLayoutIncoming, type, state);
    }

    // basvuru reddet
    public void updateStateRejected(AdminAppItemInfo adminAppItemInfo) {
        DocumentReference docRef = firebaseFirestore.collection("Resources").document(adminAppItemInfo.getDocumentId());
        docRef.update("state", "3");
        System.out.println("state update");
    }
}
