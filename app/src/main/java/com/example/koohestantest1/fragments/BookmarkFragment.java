package com.example.koohestantest1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koohestantest1.R;
import com.example.koohestantest1.adapter.ProductRecyclerViewAdapterV2;
import com.example.koohestantest1.databinding.FragmentBookmarkBinding;
import com.example.koohestantest1.viewModel.EventsViewModel;


public class BookmarkFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private FragmentBookmarkBinding bookmarkBinding;

    private EventsViewModel eventsViewModel;

    private ProductRecyclerViewAdapterV2 adapterV2;

    private String TAG = BookmarkFragment.class.getSimpleName();

    public BookmarkFragment() {
        // Required empty public constructor
    }


    public static BookmarkFragment newInstance(String param1) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        adapterV2 = new ProductRecyclerViewAdapterV2(requireContext() , false,getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bookmarkBinding = FragmentBookmarkBinding.inflate(inflater, container, false);
        bookmarkBinding.rvBookmarks.setLayoutManager(new LinearLayoutManager(requireContext()));
        bookmarkBinding.rvBookmarks.setAdapter(adapterV2);

        return bookmarkBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsViewModel.getLiveBookmarks().observe(getViewLifecycleOwner(), sendProductClasses -> {
            Log.d(TAG, "onViewCreated: " + sendProductClasses.size());
            adapterV2.setData(sendProductClasses);
            setUpView();
        });

        eventsViewModel.getErrorBookmark().observe(getViewLifecycleOwner(), s -> {
            Log.d(TAG, "onViewCreated: " + s);
            setUpView();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setUpView() {
        bookmarkBinding.rvBookmarks.setVisibility(View.VISIBLE);
        bookmarkBinding.pbBookmark.setVisibility(View.GONE);
    }

    private void resetView(){
        bookmarkBinding.rvBookmarks.setVisibility(View.GONE);
        bookmarkBinding.pbBookmark.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        resetView();
    }
}