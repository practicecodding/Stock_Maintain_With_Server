package com.hamidul.stockmaintain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Sell extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    MyTabAdapter myTabAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_sell, container, false);

        tabLayout = myView.findViewById(R.id.tabLayout);
        viewPager = myView.findViewById(R.id.viewPager);

        myTabAdapter = new MyTabAdapter(getChildFragmentManager());

        myTabAdapter.addFragment(new AddSell(),"Sell Item");
        myTabAdapter.addFragment(new SellDetails(),"Sell Details");

        viewPager.setAdapter(myTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        return myView;
    }

    public class MyTabAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> fragmentTitle = new ArrayList<>();
        public MyTabAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addFragment(Fragment fragment,String title){
            fragmentArrayList.add(fragment);
            fragmentTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentTitle.size();
        }
    }

}