package com.alisha.roomfinderapp.rooms.normal;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores fragments for tabs
 */
public class PostPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "PostPagerAdapter";

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public PostPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {

        mFragmentList.add(fragment);
    }

}
