package com.example.yazilimlab.StudentHomeFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yazilimlab.Catogery.IntibakActivity;
import com.example.yazilimlab.Catogery.YazOkuluActivity;
import com.example.yazilimlab.MainActivity;
import com.example.yazilimlab.R;
import com.example.yazilimlab.RegisterActivity;

import java.sql.SQLOutput;

public class MakeApplicationFragment extends Fragment {


    private CardView cardView_makeApp_YazOkul,cardView_makeApp_YatayGecis,cardView_makeApp_DGS,cardView_makeApp_CAP,cardView_makeApp_Intibak;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_application, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView_makeApp_YazOkul=(CardView) view.findViewById(R.id.cardView_makeApp_YazOkul);
        cardView_makeApp_YatayGecis=(CardView) view.findViewById(R.id.cardView_makeApp_YatayGecis);
        cardView_makeApp_DGS=(CardView) view.findViewById(R.id.cardView_makeApp_DGS);
        cardView_makeApp_CAP=(CardView) view.findViewById(R.id.cardView_makeApp_CAP);
        cardView_makeApp_Intibak=(CardView) view.findViewById(R.id.cardView_makeApp_Intibak);


        // click events
        cardView_makeApp_YazOkul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), YazOkuluActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        cardView_makeApp_YatayGecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), YazOkuluActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        cardView_makeApp_DGS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), YazOkuluActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        cardView_makeApp_CAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), YazOkuluActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        cardView_makeApp_Intibak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IntibakActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }
}