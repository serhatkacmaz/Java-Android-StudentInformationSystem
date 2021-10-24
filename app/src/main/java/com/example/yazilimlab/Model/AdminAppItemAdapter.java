package com.example.yazilimlab.Model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yazilimlab.R;

import java.util.ArrayList;

public class AdminAppItemAdapter extends RecyclerView.Adapter<AdminAppItemAdapter.ItemInfoHolder> {


    ArrayList<AdminAppItemInfo> adminAppItemInfoList;
    private Context context;

    private OnItemClickListener listener;

    public AdminAppItemAdapter(ArrayList<AdminAppItemInfo> adminAppItemInfoList, Context context) {
        this.adminAppItemInfoList = adminAppItemInfoList;
        this.context = context;
    }

    // gorunumler burda ayarlanir
    @NonNull
    @Override
    public ItemInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_admin_app_item, parent, false);
        return new AdminAppItemAdapter.ItemInfoHolder(v);
    }

    // icerigini ayarlama
    @Override
    public void onBindViewHolder(@NonNull ItemInfoHolder holder, int position) {

        AdminAppItemInfo adminAppItemInfo = adminAppItemInfoList.get(position);
        holder.adminApplicationItem_dateText.setText(adminAppItemInfo.date);
        holder.adminApplicationItem_numberText.setText(adminAppItemInfo.studentNumber);
        holder.state(adminAppItemInfo.state);
    }

    // liste boyutu
    @Override
    public int getItemCount() {
        return adminAppItemInfoList.size();
    }

    class ItemInfoHolder extends RecyclerView.ViewHolder {

        TextView adminApplicationItem_dateText, adminApplicationItem_numberText;
        ImageButton adminApplicationItem_acceptButton, adminApplicationItem_rejectButton, adminApplicationItem_fileDownloadButton;

        public ItemInfoHolder(@NonNull View itemView) {
            super(itemView);

            // input
            adminApplicationItem_dateText = (TextView) itemView.findViewById(R.id.adminApplicationItem_dateText);
            adminApplicationItem_numberText = (TextView) itemView.findViewById(R.id.adminApplicationItem_numberText);
            adminApplicationItem_acceptButton = (ImageButton) itemView.findViewById(R.id.adminApplicationItem_acceptButton);
            adminApplicationItem_rejectButton = (ImageButton) itemView.findViewById(R.id.adminApplicationItem_rejectButton);
            adminApplicationItem_fileDownloadButton = (ImageButton) itemView.findViewById(R.id.adminApplicationItem_fileDownloadButton);

            // click event

            // item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(adminAppItemInfoList.get(position), position);
                    }
                }
            });

            // accept click
            adminApplicationItem_acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onAcceptClick(adminAppItemInfoList.get(position), position);
                    }
                }
            });

            // reject click
            adminApplicationItem_rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onRejectClick(adminAppItemInfoList.get(position), position);
                    }
                }
            });

            // download click

            adminApplicationItem_fileDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onDownloadClick(adminAppItemInfoList.get(position), position);
                    }
                }
            });

        }

        public void state(String strState) {
            switch (strState) {
                case "2":
                    adminApplicationItem_acceptButton.setBackgroundColor(Color.TRANSPARENT);
                    adminApplicationItem_acceptButton.setEnabled(false);
                    adminApplicationItem_rejectButton.setBackgroundColor(Color.TRANSPARENT);
                    adminApplicationItem_rejectButton.setEnabled(false);
                    break;
                case "3":
                    adminApplicationItem_rejectButton.setBackgroundColor(Color.TRANSPARENT);
                    adminApplicationItem_rejectButton.setEnabled(false);
                    adminApplicationItem_acceptButton.setBackgroundColor(Color.TRANSPARENT);
                    adminApplicationItem_acceptButton.setEnabled(false);
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AdminAppItemInfo adminAppItemInfo, int position);

        void onAcceptClick(AdminAppItemInfo adminAppItemInfo, int position);

        void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position);

        void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position);
    }

    public void setOnItemClickListener(AdminAppItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
