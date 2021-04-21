package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.koohestantest1.adapter.EventPagerAdapter;
import com.example.koohestantest1.databinding.ActivityEventBinding;
import com.example.koohestantest1.fragments.BookmarkFragment;
import com.example.koohestantest1.fragments.FavouriteFragment;
import com.example.koohestantest1.viewModel.EventsViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

public class EventActivity extends AppCompatActivity {

    private ActivityEventBinding eventBinding;
    private EventPagerAdapter pagerAdapter;
    private List<String> tabTitles = new ArrayList<>();
    private EventsViewModel eventsViewModel;

    public static String KEY_EVENT_MODE = "event_mode";
    int eventMode = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBinding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(eventBinding.getRoot());
        eventMode = getIntent().getIntExtra(KEY_EVENT_MODE , -1);

        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);




        pagerAdapter = new EventPagerAdapter(getSupportFragmentManager(), getLifecycle());
        //set Up first tab
        pagerAdapter.add(FavouriteFragment.newInstance("علاقه مندی دیتای پاس داده شده"));
        tabTitles.add("علاقه مندی");
        //set up second tab
        pagerAdapter.add(BookmarkFragment.newInstance("بوک مارک دیتای پاس داده شده"));
        tabTitles.add("ذخیره");

        eventBinding.vpEvents.setAdapter(pagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(eventBinding.tabEvents, eventBinding.vpEvents, true, (tab, position) -> {
            // position of the current tab and that tab
            tab.setText(tabTitles.get(position));
        });

        tabLayoutMediator.attach();

        switch (eventMode){
            case 0:
                eventBinding.vpEvents.setCurrentItem(0);
                break;
            case 1:
            default:
                eventBinding.vpEvents.setCurrentItem(1);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //call for getting data from server
        eventsViewModel.callForBookmarks(BaseCodeClass.CompanyID , BaseCodeClass.userID);
        eventsViewModel.callForFavs(BaseCodeClass.CompanyID , BaseCodeClass.userID);
    }

}