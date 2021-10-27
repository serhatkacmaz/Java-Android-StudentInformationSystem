package com.example.yazilimlab.StudentHomeFragment;

import static android.app.Activity.RESULT_OK;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yazilimlab.Catogery.CapActivity;
import com.example.yazilimlab.Model.CustomDialog;
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

    // RecyclerViewOnGoing
    RecyclerView myApp_recyclerViewOnGoing;
    ArrayList<MyAppItemInfo> myAppItemInfoOnGoingArrayList;
    MyAppItemAdapter myAppItemOnGoingAdapter;

    // RecyclerViewOnGoing
    RecyclerView myApp_recyclerViewEnd;
    ArrayList<MyAppItemInfo> myAppItemInfoEndArrayList;
    MyAppItemAdapter myAppItemEndAdapter;

    // basvuru durumu
    private String strState;

    // firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DocumentReference docRef;
    private UsersData usersData;

    // code
    private static final int PICK_FILE = 1;

    // upload file
    private Uri uploadFileUri;

    // dosya isimleri,yolları
    private String fileName, uploadFilePathName;
    private MyAppItemInfo mInfoOnGoing;
    private String getStateUpload;

    // CardView Extand
    private CardView myApp_CardViewOnGoing, myApp_CardViewEnd;
    private LinearLayout myApp_linearLayoutOnGoing, myApp_linearLayoutEnd;

    private CustomDialog customDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_application, container, false);
    }

    private void init() {
        // Firebase
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        usersData = new UsersData();


        // recyclerViewOnGoing
        myApp_recyclerViewOnGoing.setHasFixedSize(true);
        myApp_recyclerViewOnGoing.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAppItemInfoOnGoingArrayList = new ArrayList<MyAppItemInfo>();
        myAppItemOnGoingAdapter = new MyAppItemAdapter(myAppItemInfoOnGoingArrayList, getActivity());
        myApp_recyclerViewOnGoing.setAdapter(myAppItemOnGoingAdapter);
        eventChangeListenerOnGoing();


        // recyclerViewEnd
        myApp_recyclerViewEnd.setHasFixedSize(true);
        myApp_recyclerViewEnd.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAppItemInfoEndArrayList = new ArrayList<MyAppItemInfo>();
        myAppItemEndAdapter = new MyAppItemAdapter(myAppItemInfoEndArrayList, getActivity());
        myApp_recyclerViewEnd.setAdapter(myAppItemEndAdapter);
        eventChangeListenerEnd();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView
        myApp_recyclerViewOnGoing = (RecyclerView) view.findViewById(R.id.myApp_recyclerViewOnGoing);
        myApp_recyclerViewEnd = (RecyclerView) view.findViewById(R.id.myApp_recyclerViewEnd);

        // CardView
        myApp_CardViewOnGoing = (CardView) view.findViewById(R.id.myApp_CardViewOnGoing);
        myApp_CardViewEnd = (CardView) view.findViewById(R.id.myApp_CardViewEnd);

        // LinearLayout
        myApp_linearLayoutOnGoing = (LinearLayout) view.findViewById(R.id.myApp_linearLayoutOnGoing);
        myApp_linearLayoutEnd = (LinearLayout) view.findViewById(R.id.myApp_linearLayoutEnd);

        init();


        // devam eden CardView extend event
        myApp_CardViewOnGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (myApp_recyclerViewOnGoing.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(myApp_linearLayoutOnGoing, new AutoTransition());
                myApp_recyclerViewOnGoing.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });

        // biten CardView extend event
        myApp_CardViewEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (myApp_recyclerViewEnd.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(myApp_linearLayoutEnd, new AutoTransition());
                myApp_recyclerViewEnd.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });


        // devam eden item click
        myAppItemOnGoingAdapter.setOnItemClickListener(new MyAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyAppItemInfo myAppItemInfo, int position) {
                System.out.println("item devam eden");
            }

            @Override
            public void onDownloadClick(MyAppItemInfo myAppItemInfo, int position) {
                downloadFile(myAppItemInfo);
            }

            @Override
            public void onUploadClick(MyAppItemInfo myAppItemInfo, int position) {
                mInfoOnGoing = myAppItemInfo;
                getStateField(myAppItemInfo);
            }
        });

        // biten item click
        myAppItemEndAdapter.setOnItemClickListener(new MyAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyAppItemInfo myAppItemInfo, int position) {
                System.out.println("item biten");
            }

            @Override
            public void onDownloadClick(MyAppItemInfo myAppItemInfo, int position) {
                downloadFile(myAppItemInfo);
            }

            @Override
            public void onUploadClick(MyAppItemInfo myAppItemInfo, int position) {

            }
        });


    }

    // imzalı yada imzasız güncel pdf dosyasını indirme
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
                        Toast.makeText(getActivity(), "Dosya indiriliyor.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // basvuru pdf dosaysını indirme
    //https://www.youtube.com/watch?v=SmXGlv7QEO0
    public void downloadPdfFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);
    }
    //https://www.youtube.com/watch?v=SmXGlv7QEO0

    // dosya sec sayfası
    private void selectFile(MyAppItemInfo myAppItemInfo) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Dosya Seç"), PICK_FILE);
    }

    // firebase uzerinde eski dosyayı sil
    private void deleteNotSignatureFile(MyAppItemInfo myAppItemInfo) {
        StorageReference reference = storageReference.child(myAppItemInfo.getPetitionPath());

        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                saveUploadFile(mInfoOnGoing);   // kaydet
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
                            updateField(mInfoOnGoing);  // field güncelleme
                            System.out.println("İmzalı dosya Kayıt Tamam.");
                            System.out.println("----------------------");
                            Toast.makeText(getActivity(), "İmzalı pdf yüklendi.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Resources uzerinde field path güncelleme
    private void updateField(MyAppItemInfo myAppItemInfo) {
        DocumentReference docRef = firebaseFirestore.collection("Resources").document(myAppItemInfo.getDocumentId());
        docRef.update("petitionPath", uploadFilePathName);
        docRef.update("state", "1");
        System.out.println("field update");
        //myAppItemInfo.setPetitionPath(uploadFilePathName);
        customDialog.dismissDialog();
        refresh();
    }

    // pdf yüklemek için aktif pasif durumu
    private void getStateField(MyAppItemInfo myAppItemInfo) {

        docRef = firebaseFirestore.collection("Resources").document(myAppItemInfo.getDocumentId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    System.out.println(documentSnapshot.getData().get("state").toString());
                    getStateUpload = documentSnapshot.getData().get("state").toString();
                    if (getStateUpload.equals("0")) {
                        selectFile(mInfoOnGoing);
                    } else {
                        Toast.makeText(getActivity(), "Dosya gönderilmiş durumda.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // devam eden başvuruları listeleme
    private void eventChangeListenerOnGoing() {
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
                                System.out.println(dc.getDocument().getData().get("state"));

                                // imzalancak ve onay bekleyen basvurular
                                if (dc.getDocument().getData().get("state").equals("0") || dc.getDocument().getData().get("state").equals("1")) {
                                    System.out.println(dc.getDocument().toObject(MyAppItemInfo.class));
                                    myAppItemInfoOnGoingArrayList.add(dc.getDocument().toObject(MyAppItemInfo.class));
                                }
                            }
                            myAppItemOnGoingAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    // biten başvuruları listeleme
    private void eventChangeListenerEnd() {
        fUser = fAuth.getCurrentUser();
        firebaseFirestore.collection("Resources").whereEqualTo("userUid", fUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                // onaylanan ve red edilen basvurular
                                if (dc.getDocument().getData().get("state").equals("2") || dc.getDocument().getData().get("state").equals("3")) {
                                    System.out.println(dc.getDocument().toObject(MyAppItemInfo.class));
                                    myAppItemInfoEndArrayList.add(dc.getDocument().toObject(MyAppItemInfo.class));
                                }
                            }
                            myAppItemEndAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data.getData() != null) {

            customDialog=new CustomDialog(getActivity());
            customDialog.startLoadingDialog();

            uploadFileUri = data.getData();
            deleteNotSignatureFile(mInfoOnGoing);  // sistemden eski dosyayıi sil

        }
    }

    // refresh devam eden basvuru
    private void refresh() {
        myAppItemInfoOnGoingArrayList.clear();
        eventChangeListenerOnGoing();
        myAppItemInfoEndArrayList.clear();
        eventChangeListenerEnd();
    }
}