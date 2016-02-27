package com.example.atitude6430.speed;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class unitsFragment extends Fragment {

    buttonClickListener buttonListener;

    public unitsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_units, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            buttonListener = (buttonClickListener) activity;
        }catch (Exception ex){

        }
    }

    public interface buttonClickListener{
        public void milesButton(View view);
        public void kilometersButton(View view);
    }


}
