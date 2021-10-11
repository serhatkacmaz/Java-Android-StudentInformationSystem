package com.example.yazilimlab.StudentHomeFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yazilimlab.Catogery.IntibakActivity;
import com.example.yazilimlab.Catogery.YazOkuluActivity;
import com.example.yazilimlab.R;

public class MakeApplicationFragment extends Fragment {


    private ImageView admin_home_activity_YazOkulu,admin_home_activity_intibakBasvuru;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_application, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializings
        admin_home_activity_YazOkulu = (ImageView) view.findViewById(R.id.admin_home_activity_YazOkulu);
        admin_home_activity_intibakBasvuru = (ImageView) view.findViewById(R.id.admin_home_activity_intibakBasvuru);

        //click eventler
        admin_home_activity_YazOkulu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), YazOkuluActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        admin_home_activity_intibakBasvuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IntibakActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }
}