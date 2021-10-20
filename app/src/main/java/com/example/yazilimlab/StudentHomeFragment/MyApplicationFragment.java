package com.example.yazilimlab.StudentHomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.Model.MyAppItemAdapter;
import com.example.yazilimlab.Model.MyAppItemInfo;
import com.example.yazilimlab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyApplicationFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MyAppItemInfo> myAppItemInfoArrayList;
    MyAppItemAdapter myAppItemAdapter;

    private TextView myApplicationItem_stateText;
    private String strState;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_application, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myApplicationItem_stateText = (TextView) view.findViewById(R.id.myApplicationItem_stateText);
        recyclerView = (RecyclerView) view.findViewById(R.id.myApp_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        myAppItemInfoArrayList = new ArrayList<MyAppItemInfo>();
        myAppItemAdapter = new MyAppItemAdapter(myAppItemInfoArrayList, getActivity());

        recyclerView.setAdapter(myAppItemAdapter);
        eventChangeListener();

        myAppItemAdapter.setOnItemClickListener(new MyAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyAppItemInfo myAppItemInfo, int position) {
                System.out.println("item");
            }

            @Override
            public void onDownloadClick(MyAppItemInfo myAppItemInfo, int position) {
                System.out.println("dowland");
            }

            @Override
            public void onUploadClick(MyAppItemInfo myAppItemInfo, int position) {
                System.out.println("upload");
            }
        });
    }


    private void eventChangeListener() {
        fUser = fAuth.getCurrentUser();
        firebaseFirestore.collection("Resources").whereEqualTo("userUid", fUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                myAppItemInfoArrayList.add(dc.getDocument().toObject(MyAppItemInfo.class));
                            }

                            myAppItemAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}