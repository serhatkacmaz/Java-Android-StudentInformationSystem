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


public class AdminIntibakFragment extends Fragment {

    // RecyclerViewIncoming
    RecyclerView admin_IntibakRecyclerViewIncoming;
    ArrayList<AdminAppItemInfo> adminAppItemInfoIncomingArrayList;
    AdminAppItemAdapter adminAppItemIncomingAdapter;

    // CardView Extand
    private CardView admin_IntibakCardViewIncoming;
    private LinearLayout admin_IntibakLinearLayoutIncoming;

    // Admin IncomingList
    private AdminIncomingList adminIncomingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_intibak, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView
        admin_IntibakRecyclerViewIncoming = (RecyclerView) view.findViewById(R.id.admin_IntibakRecyclerViewIncoming);

        // CardView
        admin_IntibakCardViewIncoming = (CardView) view.findViewById(R.id.admin_IntibakCardViewIncoming);

        // LinearLayout
        admin_IntibakLinearLayoutIncoming = (LinearLayout) view.findViewById(R.id.admin_IntibakLinearLayoutIncoming);


        // admin IncomingList
        adminIncomingList = new AdminIncomingList(getActivity(), admin_IntibakRecyclerViewIncoming, admin_IntibakCardViewIncoming, admin_IntibakLinearLayoutIncoming, "İntibak","1");
        admin_IntibakCardViewIncoming = adminIncomingList.admin_CardViewIncoming();
        adminAppItemIncomingAdapter = adminIncomingList.adminAppItemAdapter();


        // gelen Basvurular CardView extend event
        admin_IntibakCardViewIncoming.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                adminIncomingList.refreshIncomingCardView();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_IntibakRecyclerViewIncoming.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_IntibakLinearLayoutIncoming, new AutoTransition());
                admin_IntibakRecyclerViewIncoming.setVisibility(v);
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