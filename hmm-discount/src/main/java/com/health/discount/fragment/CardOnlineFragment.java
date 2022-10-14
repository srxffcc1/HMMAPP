//package com.health.discount.fragment;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import com.health.discount.R;
//import com.healthy.library.adapter.CanReplacePageAdapter;
//import com.healthy.library.base.BaseFragment;
//
//import java.util.List;
//
//
//public class CardOnlineFragment extends BaseFragment  {
//
//
//    private List<Fragment> mFragmentList;
//    private List<String> mTitles;
//    private CanReplacePageAdapter pageAdapter;
//
//    public static CardOnlineFragment newInstance() {
//        CardOnlineFragment fragment = new CardOnlineFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_coupon_vip;
//    }
//
//    @Override
//    protected void findViews() {
//        getData();
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//    }
//}