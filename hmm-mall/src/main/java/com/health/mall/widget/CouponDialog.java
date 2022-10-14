//package com.health.mall.widget;
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.os.Handler;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import com.google.android.material.tabs.TabLayout;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.viewpager.widget.ViewPager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.style.StyleSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.health.mall.R;
//import com.health.mall.adapter.CouponMallListAdapter;
//import com.healthy.library.interfaces.PlatformMerchantCoupon;
//import com.healthy.library.adapter.CanReplacePageAdapter;
//import com.healthy.library.fragment.TmpFragment;
//import com.healthy.library.widget.BaseFullBottomSheetFragment;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class CouponDialog extends BaseFullBottomSheetFragment {
//    private TabLayout mTabLayout;
//    private ViewPager mPager;
//    private List<String> mTitles;
//    private List<String> orgmTitles;
//    private List<Fragment> mFragmentList;
//    private CouponMallListAdapter couponMallListAdapter;
//    private CanReplacePageAdapter pageAdapter;
//    private View submitLL;
//    private int nowcouponindex = 0;//目前的选中项
//    private RecyclerView recyclerIndex;
//    private View couponLL;
//    List<PlatformMerchantCoupon> selectInfo;
//    private double nowpay;
//
//
//    public void setOnSubmitCouponListener(OnSubmitCouponListener onSubmitCouponListener) {
//        this.onSubmitCouponListener = onSubmitCouponListener;
//    }
//
//    private OnSubmitCouponListener onSubmitCouponListener;
//
//    public void setCanuselist(List<PlatformMerchantCoupon> canuselist) {
//        this.canuselist.clear();
//        this.canuselist.addAll(canuselist);
//    }
//
//    @Override
//    public int show(FragmentTransaction transaction, String tag) {
//        return super.show(transaction, tag);
//    }
//
//    public void setNocanuselist(List<PlatformMerchantCoupon> nocanuselist) {
//        this.nocanuselist = nocanuselist;
//    }
//
//    public List<PlatformMerchantCoupon> canuselist = new ArrayList<>();
//
//    public List<PlatformMerchantCoupon> nocanuselist = new ArrayList<>();
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        couponLL = LayoutInflater.from(getContext()).inflate(R.layout.dialog_coupon_list, null);
//        couponLL.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (nowcouponindex == 0) {
//                    if (onSubmitCouponListener != null) {
//
//                        onSubmitCouponListener.onClick(v, couponMallListAdapter.getSelectInfo(), couponMallListAdapter.getIndex());
//                    }
//                    dismiss();
//                }
//            }
//        });
//        recyclerIndex = couponLL.findViewById(R.id.recycler);
//        mTabLayout = couponLL.findViewById(R.id.tab);
//        mPager = couponLL.findViewById(R.id.pagers);
//        submitLL = couponLL.findViewById(R.id.submitLL);
//        View img_back = couponLL.findViewById(R.id.img_back);
//        recyclerIndex.setLayoutManager(new LinearLayoutManager(getActivity()));
//        couponMallListAdapter = new CouponMallListAdapter();
//        couponMallListAdapter.bindToRecyclerView(recyclerIndex);
//        couponMallListAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_coupon_empty, null));
//        couponMallListAdapter.setSelectInfoS(selectInfo);
//        mTitles = Arrays.asList("可用", "凑单可用");
//        orgmTitles = Arrays.asList("可用", "凑单可用");
//        mFragmentList = new ArrayList<>();
//        mFragmentList.add(new TmpFragment());
//        mFragmentList.add(new TmpFragment());
//        pageAdapter = new CanReplacePageAdapter(getChildFragmentManager(), mFragmentList, mTitles);
//        mPager.setOffscreenPageLimit(mFragmentList.size() - 1);
//        mPager.setAdapter(pageAdapter);
//        mPager.setCurrentItem(0, false);
//        submitLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab == null || tab.getText() == null) {
//                    return;
//                }
//                String trim = tab.getText().toString().trim();
//                SpannableString spStr = new SpannableString(trim);
//                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
//                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                tab.setText(spStr);
//                nowcouponindex = tab.getPosition();
//                if (nowcouponindex == 0) {
//                    submitLL.setVisibility(View.VISIBLE);
//                    couponMallListAdapter.setSelectInfoS(selectInfo);
//                    couponMallListAdapter.setNowPay(nowpay);
//                    couponMallListAdapter.setCanuse(true);
//                    couponMallListAdapter.setNewData(canuselist);
//                } else {
//                    submitLL.setVisibility(View.GONE);
//                    couponMallListAdapter.setSelectInfoS(selectInfo);
//                    couponMallListAdapter.setNowPay(nowpay);
//                    couponMallListAdapter.setCanuse(false);
//                    couponMallListAdapter.setNewData(nocanuselist);
//                }
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                if (tab == null || tab.getText() == null) {
//                    return;
//                }
//                String trim = tab.getText().toString().trim();
//                SpannableString spStr = new SpannableString(trim);
//                StyleSpan styleSpan_B = new StyleSpan(Typeface.NORMAL);
//                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                tab.setText(spStr);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//        if (pageAdapter != null) {
//            pageAdapter.setPageTitle(0, orgmTitles.get(0) + (canuselist.size() == 0 ? "" : "(" + canuselist.size() + ")"));
//            pageAdapter.setPageTitle(1, orgmTitles.get(1) + (nocanuselist.size() == 0 ? "" : "(" + nocanuselist.size() + ")"));
//        }
//        mTabLayout.setupWithViewPager(mPager);
//        if(canuselist.size()>0){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    mTabLayout.getTabAt(0).select();
//                }
//            },300);
//        }else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    mTabLayout.getTabAt(1).select();
//                }
//            },300);
//        }
//
//        return couponLL;
//
//    }
//
//    public void buildCount() {
//        if (pageAdapter != null) {
//            pageAdapter.setPageTitle(0, orgmTitles.get(0) + (canuselist.size() == 0 ? "" : "(" + canuselist.size() + ")"));
//            pageAdapter.setPageTitle(1, orgmTitles.get(1) + (nocanuselist.size() == 0 ? "" : "(" + nocanuselist.size() + ")"));
//            if(canuselist.size()>0){
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        mTabLayout.getTabAt(0).select();
//                    }
//                },300);
//            }else {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        mTabLayout.getTabAt(1).select();
//                    }
//                },300);
//            }
//        }
//
//    }
//
//    public static CouponDialog newInstance() {
//
//        Bundle args = new Bundle();
//        CouponDialog fragment = new CouponDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public void setNowPay(double doubleValue,List<PlatformMerchantCoupon> selectInfo) {
//        this.nowpay=doubleValue;
//        this.selectInfo=selectInfo;
////        if(couponMallListAdapter!=null){
////            couponMallListAdapter.setNowPay(doubleValue);
////            couponMallListAdapter.setSelectInfoS(selectInfo);
////            couponMallListAdapter.notifyDataSetChanged();
////        }
//    }
//
//    public interface OnSubmitCouponListener {
//        void onClick(View v, List<PlatformMerchantCoupon> info, int index);
//    }
//}
