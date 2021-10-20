package com.example.yazilimlab.StudentHomeFragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.Model.MyAppItemAdapter;
import com.example.yazilimlab.Model.MyAppItemInfo;
import com.example.yazilimlab.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
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

    FirebaseStorage storage;
    StorageReference storageReference;

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
                downloadFile(myAppItemInfo);
            }

            @Override
            public void onUploadClick(MyAppItemInfo myAppItemInfo, int position) {
                System.out.println("upload");
            }
        });
    }

    private String fileName;
    // imzalı yada imzasız güncel pdf indirme
    private void downloadFile(MyAppItemInfo myAppItemInfo) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        fileName = myAppItemInfo.getPetitionPath();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());

        StorageReference reference = storageReference.child(myAppItemInfo.getPetitionPath());
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadPdfFile(getActivity(), fileName, ".pdf", Environment.DIRECTORY_DOWNLOADS, url);
                        Toast.makeText(getActivity(),"Dosya indirildi",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    // basvuru pdf indirme
    public void downloadPdfFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);
    }

    // basvuru listeleme
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