package com.example.koohestantest1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koohestantest1.R;
import com.example.koohestantest1.adapter.Adapter_recycler_FragmentTabsProfile;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.model.FieldList;
import com.example.koohestantest1.model.Item;

import java.util.List;

public class FragmentTabsProfile extends Fragment {

    Item items;
    private RecyclerView recyclerView;
   private Adapter_recycler_FragmentTabsProfile adapter;

    public FragmentTabsProfile(Item items) {
        this.items = items;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs_profile, container, false);
        recyclerView = view.findViewById(R.id.rcl_fragmentTabsProfile_recyclerView);
       for (FieldList fieldList :items.getFieldList()){
           if (fieldList.getValuType()== BaseCodeClass.variableType.Image_.getValue() || fieldList.getValuType() == BaseCodeClass.variableType.Product_.getValue()){
               recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));

           }else {
               recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

           }
       }
       recyclerView.setHasFixedSize(true);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         adapter = new Adapter_recycler_FragmentTabsProfile(getContext(),items.getFieldList());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
/*
                    Toast.makeText(getContext(), "end scroll", Toast.LENGTH_SHORT).show();
*/
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (adapter.mediaPlayer != null) {
                if (adapter.mediaPlayer.isPlaying())
                    adapter.mediaPlayer.stop();
                adapter.mediaPlayer.release();
                adapter.mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}