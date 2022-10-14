//package com.health.mall.widget;
//
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.alibaba.android.vlayout.DelegateAdapter;
//import com.alibaba.android.vlayout.VirtualLayoutManager;
//import com.health.mall.R;
//import com.health.mall.adapter.CouponGiveAdapter;
//import com.health.mall.adapter.CouponGivedAdapter;
//import com.health.mall.adapter.CouponTitleAdapter;
//import com.healthy.library.model.PlatformCouponInfo;
//import com.healthy.library.widget.BaseFullBottomSheetFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CouponGiveDialog extends BaseFullBottomSheetFragment {
//    private View couponLL;
//    private ImageView imgBack;
//    private RecyclerView recycler;
//    private VirtualLayoutManager virtualLayoutManager;
//    private DelegateAdapter delegateAdapter;
//    private CouponTitleAdapter couponTitleAdapter;
//    private CouponGiveAdapter couponGiveAdapter;
//    private CouponTitleAdapter couponTitleGivedAdapter;
//    private CouponGivedAdapter couponGivedAdapter;
//    private List<PlatformCouponInfo> platformCouponInfosOrg=new ArrayList<>();
//
//
//    @Override
//    public int show(FragmentTransaction transaction, String tag) {
//        return super.show(transaction, tag);
//    }
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        couponLL = LayoutInflater.from(getContext()).inflate(R.layout.dialog_coupon_give_list, null);
//        imgBack = (ImageView) couponLL.findViewById(R.id.img_back);
//        recycler = (RecyclerView) couponLL.findViewById(R.id.recycler);
//        virtualLayoutManager = new VirtualLayoutManager(getContext());
//        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
//        couponTitleAdapter=new CouponTitleAdapter();
//        couponGiveAdapter=new CouponGiveAdapter();
//        couponTitleGivedAdapter=new CouponTitleAdapter();
//        couponGivedAdapter=new CouponGivedAdapter();
//
//        return couponLL;
//
//    }
//
//    public static CouponGiveDialog newInstance() {
//
//        Bundle args = new Bundle();
//        CouponGiveDialog fragment = new CouponGiveDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private void initView() {
//
//    }
//
//    public interface OnSubmitCouponListener {
//        void onClick(View v, PlatformCouponInfo info, int index);
//    }
//}
