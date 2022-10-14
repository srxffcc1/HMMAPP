package com.healthy.library.utils;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Author: Li
 * Date: 2018/10/10 0010
 * Description:
 */
public class FragmentUtils {
    /**
     * show fragment（not replace）
     * @param manager   manager
     * @param tag   fragment's tag
     * @param id    protocol_content's id
     * @param showFragment  the fragment to be shown
     */
    public static void showFragment(FragmentManager manager, String tag,
                                    @IdRes int id, Fragment showFragment) {
        Fragment currentFragment=null;
        for (Fragment fragment : manager.getFragments()) {
            if (fragment.isVisible()) {
                currentFragment=fragment;
                break;
            }
        }
        Fragment tagFragment = manager.findFragmentByTag(tag);
        if (tagFragment == null) {
            if (currentFragment != null) {
                manager.beginTransaction()
                        .hide(currentFragment)
                        .add(id, showFragment, tag)
                        .commitNowAllowingStateLoss();
                currentFragment.setUserVisibleHint(false);
            } else {
                manager.beginTransaction()
                        .add(id, showFragment, tag)
                        .commitNowAllowingStateLoss();
            }
            showFragment.setUserVisibleHint(true);
        } else {
            if (currentFragment != null && currentFragment != tagFragment) {
                manager.beginTransaction()
                        .hide(currentFragment)
                        .show(tagFragment)
                        .commitNowAllowingStateLoss();
                currentFragment.setUserVisibleHint(false);
                tagFragment.setUserVisibleHint(true);
            }
        }
    }
}
