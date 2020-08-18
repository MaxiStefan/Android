package com.example.proiect1;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    private static TextView textView=null;
    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_fragment1, container, false);

        View fragmentView = inflater.inflate(R.layout.fragment_fragment1, container, false);
        textView = fragmentView.findViewById(R.id.tvFragment);
        return fragmentView;
    }

    public void changeTextView(){
        if (textView!=null)
        {
            textView.setText("DAM");
            textView.setTextSize(50);
        }
    }

}
