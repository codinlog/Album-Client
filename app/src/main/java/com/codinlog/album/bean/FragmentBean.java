package com.codinlog.album.bean;

import androidx.fragment.app.Fragment;

public class FragmentBean {
    private Fragment fragment;
    private String Title;

    public FragmentBean(Fragment fragment, String title) {
        this.fragment = fragment;
        Title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
