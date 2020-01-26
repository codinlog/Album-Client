package com.codinlog.album.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codinlog.album.bean.FragmentBean;

import java.util.ArrayList;

public class MainVPAdapter extends FragmentPagerAdapter {
    private ArrayList<FragmentBean> fragmentBeans;


    public MainVPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentBeans.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentBeans == null ? 0 : fragmentBeans.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentBeans.get(position).getTitle();
    }

    public void setList(ArrayList<FragmentBean> fragmentBeans) {
        this.fragmentBeans = fragmentBeans;
    }
}
