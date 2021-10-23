package com.example.yazilimlab.AdminHomeFragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.yazilimlab.Model.AdminAppItemAdapter;
import com.example.yazilimlab.Model.AdminAppItemInfo;
import com.example.yazilimlab.Model.AdminIncomingList;
import com.example.yazilimlab.Model.CustomDialog;
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


public class AdminYatayGecisFragment extends Fragment {

    // RecyclerViewIncoming
    RecyclerView admin_YatayGecisRecyclerViewIncoming;
    ArrayList<AdminAppItemInfo> adminAppItemInfoIncomingArrayList;
    AdminAppItemAdapter adminAppItemIncomingAdapter;

    // CardView Extand
    private CardView admin_YatayGecisCardViewIncoming;
    private LinearLayout admin_YatayGecisLinearLayoutIncoming;

    // Admin IncomingList
    private AdminIncomingList adminIncomingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_yatay_gecis, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView
        admin_YatayGecisRecyclerViewIncoming = (RecyclerView) view.findViewById(R.id.admin_YatayGecisRecyclerViewIncoming);

        // CardView
        admin_YatayGecisCardViewIncoming = (CardView) view.findViewById(R.id.admin_YatayGecisCardViewIncoming);

        // LinearLayout
        admin_YatayGecisLinearLayoutIncoming = (LinearLayout) view.findViewById(R.id.admin_YatayGecisLinearLayoutIncoming);


        // admin IncomingList
        adminIncomingList = new AdminIncomingList(getActivity(), admin_YatayGecisRecyclerViewIncoming, admin_YatayGecisCardViewIncoming, admin_YatayGecisLinearLayoutIncoming, "Yatay Geciş");
        admin_YatayGecisCardViewIncoming = adminIncomingList.admin_CardViewIncoming();
        adminAppItemIncomingAdapter = adminIncomingList.adminAppItemAdapter();


        // gelen Basvurular CardView extend event
        admin_YatayGecisCardViewIncoming.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                adminIncomingList.refreshIncomingCardView();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_YatayGecisRecyclerViewIncoming.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_YatayGecisLinearLayoutIncoming, new AutoTransition());
                admin_YatayGecisRecyclerViewIncoming.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });

        // gelen basvurlar item click
        adminAppItemIncomingAdapter.setOnItemClickListener(new AdminAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("item");
            }

            @Override
            public void onAcceptClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("kabul");
            }

            @Override
            public void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("red");
            }

            @Override
            public void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("indirme");
            }
        });
    }
}