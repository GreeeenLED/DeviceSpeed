package com.example.atitude6430.speed;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.spec.ECField;


/**
 * A simple {@link Fragment} subclass.
 */
public class movementFragment extends Fragment {


    OnMovementListener onMovementListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            onMovementListener = (OnMovementListener)activity;
        }catch (Exception ex){}
    }
    public movementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movement, container, false);
    }

    public interface OnMovementListener{
        public void moveByCar(View view);
        public void moveByBike(View view);
        public void moveOnFoot(View view);
    }


}
