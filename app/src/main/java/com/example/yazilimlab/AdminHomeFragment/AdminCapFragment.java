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
import com.example.yazilimlab.Model.MyAppItemAdapter;
import com.example.yazilimlab.Model.MyAppItemInfo;
import com.example.yazilimlab.Model.UsersData;
import com.example.yazilimlab.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class AdminCapFragment extends Fragment {

    // RecyclerViewIncoming
    RecyclerView admin_CapRecyclerViewIncoming;
    AdminAppItemAdapter adminAppItemIncomingAdapter;

    // RecyclerViewAccept
    RecyclerView admin_CapRecyclerViewAccept;
    AdminAppItemAdapter adminAppItemAcceptAdapter;

    // RecyclerViewRejected
    RecyclerView admin_CapRecyclerViewRejected;
    AdminAppItemAdapter adminAppItemRejectedAdapter;


    // CardView Extand
    private CardView admin_CapCardViewIncoming, admin_CapCardViewAccept, admin_CapCardViewRejected;
    private LinearLayout admin_CapLinearLayoutIncoming, admin_CapLinearLayoutAccept, admin_CapLinearLayoutRejected;

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
        return inflater.inflate(R.layout.fragment_admin_cap, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // RecyclerView
        admin_CapRecyclerViewIncoming = (RecyclerView) view.findViewById(R.id.admin_CapRecyclerViewIncoming);
        admin_CapRecyclerViewAccept = (RecyclerView) view.findViewById(R.id.admin_CapRecyclerViewAccept);
        admin_CapRecyclerViewRejected = (RecyclerView) view.findViewById(R.id.admin_CapRecyclerViewRejected);

        // CardView
        admin_CapCardViewIncoming = (CardView) view.findViewById(R.id.admin_CapCardViewIncoming);
        admin_CapCardViewAccept = (CardView) view.findViewById(R.id.admin_CapCardViewAccept);
        admin_CapCardViewRejected = (CardView) view.findViewById(R.id.admin_CapCardViewRejected);

        // LinearLayout
        admin_CapLinearLayoutIncoming = (LinearLayout) view.findViewById(R.id.admin_CapLinearLayoutIncoming);
        admin_CapLinearLayoutAccept = (LinearLayout) view.findViewById(R.id.admin_CapLinearLayoutAccept);
        admin_CapLinearLayoutRejected = (LinearLayout) view.findViewById(R.id.admin_CapLinearLayoutRejected);


        // admin IncomingList
        adminIncomingList = new AdminIncomingList(getActivity(), admin_CapRecyclerViewIncoming, admin_CapCardViewIncoming, admin_CapLinearLayoutIncoming, "Çap", "1");
        admin_CapCardViewIncoming = adminIncomingList.admin_CardViewIncoming();
        adminAppItemIncomingAdapter = adminIncomingList.adminAppItemAdapter();

        // admin AcceptList
        adminAcceptList = new AdminAcceptList(getActivity(), admin_CapRecyclerViewAccept, admin_CapCardViewAccept, admin_CapLinearLayoutAccept, "Çap", "2");
        admin_CapCardViewAccept = adminAcceptList.admin_CardViewIncoming();
        adminAppItemAcceptAdapter = adminAcceptList.adminAppItemAdapter();

        // admin RejectedList
        adminRejectedList = new AdminRejectedList(getActivity(), admin_CapRecyclerViewRejected, admin_CapCardViewRejected, admin_CapLinearLayoutRejected, "Çap", "3");
        admin_CapCardViewRejected = adminRejectedList.admin_CardViewIncoming();
        adminAppItemRejectedAdapter = adminRejectedList.adminAppItemAdapter();


        // gelen Basvurular CardView extend event
        admin_CapCardViewIncoming.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_CapRecyclerViewIncoming.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_CapLinearLayoutIncoming, new AutoTransition());
                admin_CapRecyclerViewIncoming.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });

        // onaylanan basvurular CardView extend event
        admin_CapCardViewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_CapRecyclerViewAccept.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_CapLinearLayoutAccept, new AutoTransition());
                admin_CapRecyclerViewAccept.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });

        // red edilen basvurular CardView extend event
        admin_CapCardViewRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_CapRecyclerViewRejected.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_CapLinearLayoutRejected, new AutoTransition());
                admin_CapRecyclerViewRejected.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });


        // gelen basvurlar item click
        adminAppItemIncomingAdapter.setOnItemClickListener(new AdminAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" gelen item");
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
                        Toast.makeText(getActivity(), "Dosya indiriliyor.(Dilekçe)", Toast.LENGTH_SHORT).show();
                        downloadTranscript(adminAppItemInfo);   // transkirpt
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    // transkript belgesi
    private void downloadTranscript(AdminAppItemInfo adminAppItemInfo) {

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
                        Toast.makeText(getActivity(), "Dosya indiriliyor.(Transkript)", Toast.LENGTH_SHORT).show();
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


    private void refresh() {
        adminIncomingList.refreshIncomingCardView();
        adminAcceptList.refreshIncomingCardView();
        adminRejectedList.refreshIncomingCardView();
    }
}