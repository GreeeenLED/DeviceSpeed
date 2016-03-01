package com.example.atitude6430.speed;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class resultsFragment extends Fragment {


    private List<String> resultsList;
    private ArrayAdapter<String> resultsAdapter;
    SharedPreferences preferences;

    ListView resultListView;
    public resultsFragment() {
        // Required empty public constructor
    }

    View view;
    ArrayList<String> displayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_results, container, false);
        resultListView = (ListView)view.findViewById(R.id.listViewResults);
        String moveType = getArguments().getString("pref");
        preferences = getActivity().getSharedPreferences(moveType, Context.MODE_PRIVATE);
        ShowResults();
        adapter = new ArrayAdapter<String>(getActivity(),R.layout.row_layout,displayList);
        resultListView.setAdapter(adapter);
        return view;
    }

    public void ShowResults(){//to bedzie kolejny fragment wkladany do kontenera !!!!!!
        int i =0;
        Map<String,?> keys = sortResults(); //preferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            i++;
            if (i<11){
                displayList.add(" " + i + "   " + entry.getKey().toString() + "   " + entry.getValue() + " " + "KM/H");
            }
        }
      }

    public Map<String,?> sortResults(){
        Map<String, Float> unsortMap = (Map<String, Float>) preferences.getAll();

        List<Map.Entry<String,Float>> list = new LinkedList<Map.Entry<String,Float>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> lhs, Map.Entry<String, Float> rhs) {
                return rhs.getValue().compareTo(lhs.getValue());
            }
        });
        Map<String, Object> sortedMap = new LinkedHashMap<>();
        for (Iterator<Map.Entry<String,Float>> it = list.iterator(); it.hasNext();){
            Map.Entry<String,?>entry = it.next();
            sortedMap.put(entry.getKey().toLowerCase(),entry.getValue());
        }
        Map<String,?> map = sortedMap;
        for (Map.Entry<String,?>entry: map.entrySet()){
            Log.d("posortowane", " " + entry.getKey().toString() + " " + entry.getValue().toString());
        }
        return map;
    }
    OnResultsClosed onResultsClosed;
    public interface OnResultsClosed{
        public void ExitResults(View view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onResultsClosed = (OnResultsClosed)activity;
    }
}
