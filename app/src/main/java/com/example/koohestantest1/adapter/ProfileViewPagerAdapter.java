package com.example.koohestantest1.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;

    public ProfileViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentList = new ArrayList<>();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);

    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void addTab(Fragment fragment) {
        fragmentList.add(fragment);
    }

}
