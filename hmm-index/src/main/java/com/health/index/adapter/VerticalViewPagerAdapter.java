package com.health.index.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.health.index.fragment.HanMomVideoPlayerFragment;
import com.healthy.library.model.VideoListModel;

import java.util.List;


public class VerticalViewPagerAdapter extends PagerAdapter {
    private FragmentManager fragmentManager;
    private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem = null;
    private List<VideoListModel> urlList;

    public void setUrlList(List<VideoListModel> urlList) {
        this.urlList = urlList;
    }


    public VerticalViewPagerAdapter(FragmentManager fm) {
        this.fragmentManager = fm;
    }

    @Override
    public int getCount() {
        return urlList != null ? urlList.size() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = fragmentManager.beginTransaction();
        }
        HanMomVideoPlayerFragment fragment = null;
        if (urlList != null && urlList.size() > 0) {
            if (position >= urlList.size()) {
                fragment = HanMomVideoPlayerFragment.newInstance(urlList.get(position % urlList.size()).getVideoUrl(), urlList.get(position % urlList.size()).id, urlList.get(position % urlList.size()).photo);
            } else {
                fragment = HanMomVideoPlayerFragment.newInstance(urlList.get(position).getVideoUrl(), urlList.get(position).id, urlList.get(position).photo);
            }
        }
        mCurTransaction.add(container.getId(), fragment,
                makeFragmentName(container.getId(), position));
        fragment.setUserVisibleHint(false);

        return fragment;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = fragmentManager.beginTransaction();
        }
        mCurTransaction.detach((Fragment) object);
        mCurTransaction.remove((Fragment) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((Fragment) object).getView() == view;
    }

    private String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + position;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }
}
