package com.wordpress.gistschool.raji.farmchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int noOfTabs;
    public PagerAdapter(FragmentManager fm,int noOfTabs) {
        super( fm );
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFarmerFragment homeFragment = new HomeFarmerFragment();
                return homeFragment;
            case 1:
                whoToChatUpFragment friendFragment = new whoToChatUpFragment();
                return friendFragment;
            case 2:
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                return dashBoardFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
