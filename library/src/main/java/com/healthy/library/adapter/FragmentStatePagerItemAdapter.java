package com.healthy.library.adapter;


import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.healthy.library.base.BaseFragment;

import java.util.List;

/**
 * FragmentStatePagerAdapter适配器
 *
 * @author luoyang
 * @version [AndroidLibrary, 2018-3-6]
 */
public class FragmentStatePagerItemAdapter extends FragmentStatePagerAdapter {
    FragmentManager fm;
    private List<Fragment> fragments;
    private List<String> titles;
    public OnPageClickListener onPageClickListener;

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }

    public FragmentStatePagerItemAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
        this.titles = titles;
    }

    public FragmentStatePagerItemAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments, null);
    }

    public void setDataSource(List<Fragment> fragments, List<String> titles) {
        this.fragments = fragments;
        this.titles = titles;
    }

    public void setDataSource(List<Fragment> fragments) {
        setDataSource(fragments, null);
    }

    @Override
    public Fragment getItem(final int position) {
        if(fragments.get(position) instanceof BaseFragment){
            BaseFragment baseFragment= (BaseFragment) fragments.get(position);
            if(onPageClickListener!=null){
                baseFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onPageClickListener!=null){
                            onPageClickListener.onPageClick(position);
                        }else {
//                            //System.out.println("点击时间");
                        }
                    }
                });
            }

        }

        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null) {
            return null;
        }
        return titles.get(position);
    }
    public interface OnPageClickListener{
        void onPageClick(int index);
    }
}
