package com.hamidul.stockmaintain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class Purchase extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_purchase, container, false);

        tabLayout = myView.findViewById(R.id.tabLayout);
        viewPager = myView.findViewById(R.id.viewPager);

        tabAdapter = new TabAdapter(getChildFragmentManager());

        tabAdapter.addFragment(new AddPurchase(),"Purchase Item");
        tabAdapter.addFragment(new PurchaseDetails(),"Purchase Details");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        return myView;
    }

    public class TabAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> fragmentTitle = new ArrayList<>();

        public TabAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addFragment(Fragment fragment, String title){
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
            return fragmentArrayList.size();
        }
    }




}