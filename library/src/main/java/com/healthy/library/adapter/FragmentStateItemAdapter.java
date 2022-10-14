package com.healthy.library.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.healthy.library.widget.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author long
 * @description
 * @date 2021/6/22
 */
public class FragmentStateItemAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public FragmentStateItemAdapter(@NonNull @NotNull FragmentActivity fragmentActivity,
                                    List<Fragment> mFragments) {
        super(fragmentActivity);
        this.mFragments = mFragments;
    }

    public FragmentStateItemAdapter(@NonNull @NotNull Fragment fragmentActivity,
                                    List<Fragment> mFragments) {
        super(fragmentActivity);
        this.mFragments = mFragments;
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

}
