package com.example.yazilimlab.StudentHomeFragment;

import static android.app.Activity.RESULT_OK;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.Catogery.CapActivity;
import com.example.yazilimlab.Model.MyAppItemAdapter;
import com.example.yazilimlab.Model.MyAppItemInfo;
import com.example.yazilimlab.Model.UsersData;
import com.example.yazilimlab.R;
import com.example.yazilimlab.StudentHomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyApplicationFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MyAppItemInfo> myAppItemInfoArrayList;
    MyAppItemAdapter myAppItemAdapter;

    private TextView myApplicationItem_stateText;
    private String strState;

    // firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DocumentReference docRef;

    // code
    private static final int PICK_FILE = 1;

    // upload file
    private Uri uploadFileUri;

    // dosya isimleri,yolları
    private String fileName, uploadFilePathName;

    private UsersData usersData;
    private MyAppItemInfo mInfo;

    private String getStateUpload;

    private ImageButton myApplicationItem_fileUploadButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_application, container, false);
    }

    private void init() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        myAppItemInfoArrayList = new ArrayList<MyAppItemInfo>();
        myAppItemAdapter = new MyAppItemAdapter(myAppItemInfoArrayList, getActivity());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        usersData = new UsersData();

        recyclerView.setAdapter(myAppItemAdapter);
        eventChangeListener();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myApplicationItem_stateText = (TextView) view.findViewById(R.id.myApplicationItem_stateText);
        recyclerView = (RecyclerView) view.findViewById(R.id.myApp_recyclerView);
        myApplicationItem_fileUploadButton = (ImageButton) view.findViewById(R.id.myApplicationItem_fileUploadButton);
        init();

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
                mInfo = myAppItemInfo;
                getUploadStateField(myAppItemInfo);

            }
        });
    }


    // imzalı yada imzasız güncel pdf indirme
    private void downloadFile(MyAppItemInfo myAppItemInfo) {
        fileName = myAppItemInfo.getPetitionPath();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());

        StorageReference reference = storageReference.child(myAppItemInfo.getPetitionPath());
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadPdfFile(getActivity(), fileName, ".pdf", Environment.DIRECTORY_DOWNLOADS, url);
                        Toast.makeText(getActivity(), "Dosya indirildi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    // dosya sec sayfası
    private void selectFile(MyAppItemInfo myAppItemInfo) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Dosya Seç"), PICK_FILE);
        deleteNotSignatureFile(myAppItemInfo);  // eski dosya sil
    }

    // eski dosyayı sil
    private void deleteNotSignatureFile(MyAppItemInfo myAppItemInfo) {
        mInfo = myAppItemInfo;
        StorageReference reference = storageReference.child(myAppItemInfo.getPetitionPath());

        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // dosya kayıt format isimlendirme
    private String adjustFormat() {
        String number, name, lastName;
        number = usersData.getIncomingNumber();
        name = usersData.getIncomingName();
        lastName = usersData.getIncomingLastName();

        // https://www.javatpoint.com/java-get-current-date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        String strDate = formatter.format(date);
        // https://www.javatpoint.com/java-get-current-date

        return number + "_" + name + "_" + lastName + "_" + strDate;
    }

    // imzalı dosya sisteme yükle
    private void saveUploadFile(MyAppItemInfo myAppItemInfo) {
        if (uploadFileUri != null) {
            uploadFilePathName = myAppItemInfo.getPetitionPath();
            uploadFilePathName = uploadFilePathName.substring(0, uploadFilePathName.lastIndexOf('/') + 1);
            uploadFilePathName += adjustFormat();

            System.out.println(uploadFilePathName);
            StorageReference reference = storageReference.child(uploadFilePathName);
            reference.putFile(uploadFileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("İmzalı dosya Kayıt Tamam.");
                            System.out.println("----------------------");
                            Toast.makeText(getActivity(), "İmzalı pdf yüklendi", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // field path güncelleme
    private void updateField(MyAppItemInfo myAppItemInfo) {
        DocumentReference docRef = firebaseFirestore.collection("Resources").document(myAppItemInfo.getDocumentId());
        docRef.update("petitionPath", uploadFilePathName);
        docRef.update("state", "1");
        docRef.update("uploadState", "1");
        System.out.println("upload güncellendi");
        //myAppItemInfo.setPetitionPath(uploadFilePathName);
    }

    // pdf yükleme aktif pasif
    private void getUploadStateField(MyAppItemInfo myAppItemInfo) {

        docRef = firebaseFirestore.collection("Resources").document(myAppItemInfo.getDocumentId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    System.out.println(documentSnapshot.getData().get("uploadState").toString());
                    getStateUpload = documentSnapshot.getData().get("uploadState").toString();
                    if (getStateUpload.equals("0")) {
                        selectFile(mInfo);
                    } else {
                        Toast.makeText(getActivity(), "Dosya gönderilmiş durumda", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data.getData() != null) {
            uploadFileUri = data.getData();
            saveUploadFile(mInfo);
            updateField(mInfo);  // field güncelleme
            // refresh
            myAppItemInfoArrayList.clear();
            eventChangeListener();
        }
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

                                // document ıd ekleme işlemi field olarak ekleme
                                DocumentReference docRef = firebaseFirestore.collection("Resources").document(dc.getDocument().getId());
                                docRef.update("documentId", dc.getDocument().getId());

                                System.out.println(dc.getDocument().toObject(MyAppItemInfo.class));
                                myAppItemInfoArrayList.add(dc.getDocument().toObject(MyAppItemInfo.class));
                            }
                            myAppItemAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}