package com.healthy.library.fragment;

import android.os.Bundle;

import com.healthy.library.R;
import com.healthy.library.base.BaseFragment;

import java.util.Map;

public class EmptyLayoutFragment extends BaseFragment  {



    public static EmptyLayoutFragment newInstance(Map<String, Object> maporg) {
        EmptyLayoutFragment fragment = new EmptyLayoutFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    public static EmptyLayoutFragment newInstance(int layoutRes) {
        EmptyLayoutFragment fragment = new EmptyLayoutFragment();
        Bundle args = new Bundle();
        args.putInt("layoutRes",layoutRes);
        BaseFragment.bundleMap(null, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        if(get("layoutRes")!=null){
            return (int) get("layoutRes");
        }
        return R.layout.layout_empty_fragment_just;
    }

    @Override
    protected void findViews() {

        getData();
    }
}
