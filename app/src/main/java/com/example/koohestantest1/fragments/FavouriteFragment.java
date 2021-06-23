package com.example.koohestantest1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koohestantest1.R;
import com.example.koohestantest1.adapter.ProductRecyclerViewAdapterV2;
import com.example.koohestantest1.databinding.FragmentFavouriteBinding;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.viewModel.EventsViewModel;

import java.util.List;

public class FavouriteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private DBViewModel dbViewModel;

    private FragmentFavouriteBinding favouriteBinding;

    private EventsViewModel eventsViewModel;

    private String TAG = FavouriteFragment.class.getSimpleName();

    private ProductRecyclerViewAdapterV2 adapterV2;

    public static FavouriteFragment newInstance(String param1) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        DBViewModel dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        //adapterV2 = new ProductRecyclerViewAdapterV2(requireContext() , false,getChildFragmentManager(),dbViewModel,getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        favouriteBinding = FragmentFavouriteBinding.inflate(inflater, container, false);
        favouriteBinding.rvFav.setAdapter(adapterV2);
        favouriteBinding.rvFav.setLayoutManager(new LinearLayoutManager(requireContext()));
        return favouriteBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * check here
         */

        /*eventsViewModel.getLiveFavs().observe(getViewLifecycleOwner(), sendProductClasses -> {
            Log.d(TAG, "onViewCreated: " + sendProductClasses.size());
            adapterV2.setData(sendProductClasses);
            setViewUpView();
        });
        eventsViewModel.getErrorFav().observe(getViewLifecycleOwner(), s -> {
            Log.d(TAG, "onViewCreated: " + s);
            setViewUpView();
        });*/

        dbViewModel.getBookmarkedProduct().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapterV2.setData(products);
                setViewUpView();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setViewUpView(){
        favouriteBinding.rvFav.setVisibility(View.VISIBLE);
        favouriteBinding.pbFavs.setVisibility(View.GONE);
    }

    private void resetView(){
        favouriteBinding.rvFav.setVisibility(View.GONE);
        favouriteBinding.pbFavs.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        resetView();
    }
}