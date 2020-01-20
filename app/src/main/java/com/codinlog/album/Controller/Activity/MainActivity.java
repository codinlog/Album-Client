package com.codinlog.album.Controller.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.codinlog.album.Controller.BaseActivityController;
import com.codinlog.album.Controller.Fragment.AlbumFragment;
import com.codinlog.album.Controller.Fragment.PhotoFragment;
import com.codinlog.album.Controller.Fragment.TimeFragment;
import com.codinlog.album.R;
import com.codinlog.album.adapter.ViewPagerAdapter;
import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.model.MainViewModel;

import java.util.ArrayList;
import java.util.Iterator;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MainActivity extends BaseActivityController<MainViewModel> {
    private ArrayList<FragmentBean> fragmentBeans;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void doInitVew() {
        Log.d(TAG, "doInitVew: ");
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        fragmentBeans = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    protected void doInitData() {
        Log.d(TAG, "doInitData: ");
        fragmentBeans.add(new FragmentBean(PhotoFragment.newInstance(),getString(R.string.photo)));
        fragmentBeans.add(new FragmentBean(AlbumFragment.newInstance(),getString(R.string.album)));
        fragmentBeans.add(new FragmentBean(TimeFragment.newInstance(),getString(R.string.time)));
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        viewModel.setFragmentList(fragmentBeans);
    }

    @Override
    protected void doInitListener() {
        Log.d(TAG, "doInitListener: ");
        viewModel.getFragmentList().observe(this, new Observer<ArrayList<FragmentBean>>() {
            @Override
            public void onChanged(ArrayList<FragmentBean> fragmentBeans) {
                viewPagerAdapter.setList(MainActivity.this.fragmentBeans);
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void showPermissionDialog(final ArrayList<Integer> notAllowPermissions) {
        final Iterator<Integer> iterator = notAllowPermissions.iterator();
        if(iterator.hasNext()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.notice)
                    .setMessage(iterator.next())
                    .setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            iterator.remove();
                            showPermissionDialog(notAllowPermissions);
                        }
                    }).show();
        }

    }
}
