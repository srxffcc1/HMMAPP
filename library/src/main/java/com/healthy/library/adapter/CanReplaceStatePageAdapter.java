package com.healthy.library.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class CanReplaceStatePageAdapter extends FragmentStatePagerAdapter {
    private List<String> titles;
    private List<Fragment> fragmentLists;
    private long baseId = 0;

    public CanReplaceStatePageAdapter(FragmentManager fm, List<Fragment> fragmentLists, List<String> titles){
        super(fm);
        this.fragmentLists = fragmentLists;
        this.titles=titles;
    }
    public void changeId(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && position <= titles.size()&&titles.size()>0) {
            try {
                return titles.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.getPageTitle(position);
    }
    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position);
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }

    // 动态设置我们标题的方法
    public void setPageTitle(int position, String title) {
        if (position >= 0 && position < titles.size()) {
            titles.set(position, title);
            notifyDataSetChanged();
        }
    }
}

