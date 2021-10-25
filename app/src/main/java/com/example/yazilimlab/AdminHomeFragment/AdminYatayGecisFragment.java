package com.example.yazilimlab.AdminHomeFragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yazilimlab.Model.AdminAcceptList;
import com.example.yazilimlab.Model.AdminAppItemAdapter;
import com.example.yazilimlab.Model.AdminAppItemInfo;
import com.example.yazilimlab.Model.AdminIncomingList;
import com.example.yazilimlab.Model.AdminRejectedList;
import com.example.yazilimlab.Model.CustomDialog;
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

import java.util.ArrayList;


public class AdminYatayGecisFragment extends Fragment {

    // RecyclerViewIncoming
    RecyclerView admin_YatayGecisRecyclerViewIncoming;
    AdminAppItemAdapter adminAppItemIncomingAdapter;

    // RecyclerViewAccept
    RecyclerView admin_YatayGecisRecyclerViewAccept;
    AdminAppItemAdapter adminAppItemAcceptAdapter;

    // RecyclerViewRejected
    RecyclerView admin_YatayGecisRecyclerViewRejected;
    AdminAppItemAdapter adminAppItemRejectedAdapter;

    // CardView Extand
    private CardView admin_YatayGecisCardViewIncoming, admin_YatayGecisCardViewAccept, admin_YatayGecisCardViewRejected;
    private LinearLayout admin_YatayGecisLinearLayoutIncoming, admin_YatayGecisLinearLayoutAccept, admin_YatayGecisLinearLayoutRejected;

    // Admin IncomingList
    private AdminIncomingList adminIncomingList;

    // Admin AcceptList
    private AdminAcceptList adminAcceptList;

    // Admin RejectedList
    private AdminRejectedList adminRejectedList;

    // Storage
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_yatay_gecis, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        // RecyclerView
        admin_YatayGecisRecyclerViewIncoming = (RecyclerView) view.findViewById(R.id.admin_YatayGecisRecyclerViewIncoming);
        admin_YatayGecisRecyclerViewAccept = (RecyclerView) view.findViewById(R.id.admin_YatayGecisRecyclerViewAccept);
        admin_YatayGecisRecyclerViewRejected = (RecyclerView) view.findViewById(R.id.admin_YatayGecisRecyclerViewRejected);

        // CardView
        admin_YatayGecisCardViewIncoming = (CardView) view.findViewById(R.id.admin_YatayGecisCardViewIncoming);
        admin_YatayGecisCardViewAccept = (CardView) view.findViewById(R.id.admin_YatayGecisCardViewAccept);
        admin_YatayGecisCardViewRejected = (CardView) view.findViewById(R.id.admin_YatayGecisCardViewRejected);

        // LinearLayout
        admin_YatayGecisLinearLayoutIncoming = (LinearLayout) view.findViewById(R.id.admin_YatayGecisLinearLayoutIncoming);
        admin_YatayGecisLinearLayoutAccept = (LinearLayout) view.findViewById(R.id.admin_YatayGecisLinearLayoutAccept);
        admin_YatayGecisLinearLayoutRejected = (LinearLayout) view.findViewById(R.id.admin_YatayGecisLinearLayoutRejected);


        // admin IncomingList
        adminIncomingList = new AdminIncomingList(getActivity(), admin_YatayGecisRecyclerViewIncoming, admin_YatayGecisCardViewIncoming, admin_YatayGecisLinearLayoutIncoming, "Yatay Geciş", "1");
        admin_YatayGecisCardViewIncoming = adminIncomingList.admin_CardViewIncoming();
        adminAppItemIncomingAdapter = adminIncomingList.adminAppItemAdapter();

        // admin AcceptList
        adminAcceptList = new AdminAcceptList(getActivity(), admin_YatayGecisRecyclerViewAccept, admin_YatayGecisCardViewAccept, admin_YatayGecisLinearLayoutAccept, "Yatay Geciş", "2");
        admin_YatayGecisCardViewAccept = adminAcceptList.admin_CardViewIncoming();
        adminAppItemAcceptAdapter = adminAcceptList.adminAppItemAdapter();

        // admin RejectedList
        adminRejectedList = new AdminRejectedList(getActivity(), admin_YatayGecisRecyclerViewRejected, admin_YatayGecisCardViewRejected, admin_YatayGecisLinearLayoutRejected, "Yatay Geciş", "3");
        admin_YatayGecisCardViewRejected = adminRejectedList.admin_CardViewIncoming();
        adminAppItemRejectedAdapter = adminRejectedList.adminAppItemAdapter();


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

        // onaylanan basvurular CardView extend event
        admin_YatayGecisCardViewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_YatayGecisRecyclerViewAccept.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_YatayGecisLinearLayoutAccept, new AutoTransition());
                admin_YatayGecisRecyclerViewAccept.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });

        // red edilen basvurular CardView extend event
        admin_YatayGecisCardViewRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_YatayGecisRecyclerViewRejected.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_YatayGecisLinearLayoutRejected, new AutoTransition());
                admin_YatayGecisRecyclerViewRejected.setVisibility(v);
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
                adminAcceptList.updateStateAccept(adminAppItemInfo);
                refresh();
            }

            @Override
            public void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position) {
                adminRejectedList.updateStateRejected(adminAppItemInfo);
                refresh();
            }

            @Override
            public void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position) {
                downloadFile(adminAppItemInfo);
            }
        });

        // onaylanan basvurlar item click
        adminAppItemAcceptAdapter.setOnItemClickListener(new AdminAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" onaylanan item");
            }

            @Override
            public void onAcceptClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" onaylanan kabul");
            }

            @Override
            public void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" onaylanan red");
            }

            @Override
            public void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position) {
                downloadFile(adminAppItemInfo);
            }
        });

        // red edilen basvurlar item click
        adminAppItemRejectedAdapter.setOnItemClickListener(new AdminAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" red edilen item");
            }

            @Override
            public void onAcceptClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" red edilen kabul");
            }

            @Override
            public void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" red edilen red");
            }

            @Override
            public void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position) {
                downloadFile(adminAppItemInfo);
            }
        });
    }

    private String fileName, extension;

    // dosyaları inidirme
    private void downloadFile(AdminAppItemInfo adminAppItemInfo) {
        // dosya adı
        fileName = adminAppItemInfo.getPetitionPath();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());

        // imzalı dilekçe
        StorageReference referencePetition = storageReference.child(adminAppItemInfo.getPetitionPath());
        referencePetition.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadPdfFile(getActivity(), fileName, ".pdf", Environment.DIRECTORY_DOWNLOADS, url);
                        Toast.makeText(getActivity(), "Dosya indiriliyor(dilekce)", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // transkript belgesi

        // dosya adı
        fileName = adminAppItemInfo.getTranscriptPath();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());

        // uzantı
        extension = adminAppItemInfo.getTranscriptPath();
        extension = (extension.substring(extension.indexOf('/') + 1));
        extension = extension.substring(0, extension.indexOf('/'));

        StorageReference referenceTranscript = storageReference.child(adminAppItemInfo.getTranscriptPath());
        referenceTranscript.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadPdfFile(getActivity(), fileName, "." + extension, Environment.DIRECTORY_DOWNLOADS, url);
                        Toast.makeText(getActivity(), "Dosya indiriliyor(transkript)", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // basvuru pdf dosaysını indirme
    public void downloadPdfFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);
    }



    private void refresh() {
        adminIncomingList.refreshIncomingCardView();
        adminAcceptList.refreshIncomingCardView();
        adminRejectedList.refreshIncomingCardView();
    }
}