//package com.health.mall.widget;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.DelegateAdapter;
//import com.alibaba.android.vlayout.VirtualLayoutManager;
//import com.health.mall.R;
//import com.health.mall.adapter.CouponNormalAdapter;
//import com.health.mall.adapter.CouponTitleAdapter;
//import com.health.mall.contract.AppCouponContract;
//import com.health.mall.presenter.AppCouponPresenter;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.model.MallCoupon;
//import com.healthy.library.routes.MallRoutes;
//import com.healthy.library.widget.BaseFullBottomSheetFragment;
//import com.hss01248.dialog.StyledDialog;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import io.reactivex.disposables.Disposable;
//
//public class CouponWithGoodsDialog extends BaseFullBottomSheetFragment implements AppCouponContract.View, CouponNormalAdapter.GoOnClickListener {
//
//    private VirtualLayoutManager virtualLayoutManager;
//    private DelegateAdapter delegateAdapter;
//    private CouponTitleAdapter couponTitleNoAdapter;
//    private CouponNormalAdapter couponNormalNoAdapter;
//    private CouponTitleAdapter couponTitleYesAdapter;
//    private CouponNormalAdapter couponNormalYesAdapter;
//    private ImageView imgBack;
//    private RecyclerView recycler;
//    AppCouponPresenter appCouponPresenter;
//
//    public String goodsId;
//    public String shopId;
//    public String memberId;
//    public String cityNo;
//    public String lng;
//    public String lat;
//    private Dialog loading;
//
//    public CouponWithGoodsDialog setData(String goodsId, String shopId, String memberId, String cityNo, String lng, String lat) {
//        this.goodsId = goodsId;
//        this.shopId = shopId;
//        this.memberId = memberId;
//        this.cityNo = cityNo;
//        this.lng = lng;
//        this.lat = lat;
//        return this;
//    }
//
//    Map<String, Object> listmap=new HashMap<>();
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_coupon_withcoupon_list, null);
//
//        imgBack = (ImageView) view.findViewById(R.id.img_back);
//        recycler = (RecyclerView) view.findViewById(R.id.recycler);
//        virtualLayoutManager = new VirtualLayoutManager(getContext());
//        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
//
//        couponTitleNoAdapter = new CouponTitleAdapter();
//        delegateAdapter.addAdapter(couponTitleNoAdapter);
//
//        couponNormalNoAdapter=new CouponNormalAdapter();
//        delegateAdapter.addAdapter(couponNormalNoAdapter);
//        couponNormalNoAdapter.setIshas(false);
//        couponNormalNoAdapter.setGoOnClickListener(this);
//
//        couponTitleYesAdapter = new CouponTitleAdapter();
//        delegateAdapter.addAdapter(couponTitleYesAdapter);
//
//        couponNormalYesAdapter=new CouponNormalAdapter();
//        delegateAdapter.addAdapter(couponNormalYesAdapter);
//        couponNormalYesAdapter.setIshas(true);
//
//        couponNormalYesAdapter.setGoOnClickListener(this);
//        recycler.setLayoutManager(virtualLayoutManager);
//        recycler.setAdapter(delegateAdapter);
//        appCouponPresenter=new AppCouponPresenter(getContext(),this);
//        init();
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        return view;
//    }
//
//
//    public void init(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                listmap.put("goodsId",goodsId);
//                listmap.put("shopId",shopId);
//                listmap.put("memberId",memberId);
//                listmap.put(Functions.FUNCTION, "8005");
//                appCouponPresenter.getAppCouponList(listmap);
//            }
//        },300);
//    }
//
//
//
//    public static CouponWithGoodsDialog newInstance() {
//        Bundle args = new Bundle();
//        CouponWithGoodsDialog fragment = new CouponWithGoodsDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onSucessGetAppCouponList(List<MallCoupon> mallCouponNoList,List<MallCoupon> mallCouponYesList) {
//        if(loading!=null){
//            StyledDialog.dismiss(loading);
//        }
//        if(mallCouponNoList.size()>0){
//
//            couponTitleNoAdapter.setModel("可领取优惠券");
//        }else {
//            couponTitleNoAdapter.setModel(null);
//        }
//        if(mallCouponYesList.size()>0){
//            couponTitleYesAdapter.setModel("已领取优惠券");
//        }else {
//            couponTitleYesAdapter.setModel(null);
//        }
//        couponNormalNoAdapter.setData((ArrayList<MallCoupon>) mallCouponNoList);
//
//
//        couponNormalYesAdapter.setData((ArrayList<MallCoupon>) mallCouponYesList);
//    }
//
//    @Override
//    public void onSucessReAppCoupon() {
//        init();
//    }
//
//    @Override
//    public void onFailReAppCoupon() {
//        if(loading!=null){
//            StyledDialog.dismiss(loading);
//        }
//    }
//
//    @Override
//    public void showLoading() {
//    }
//
//    @Override
//    public void showToast(CharSequence msg) {
//
//    }
//
//    @Override
//    public void showNetErr() {
//
//    }
//
//    @Override
//    public void onRequestStart(Disposable disposable) {
//
//    }
//
//    @Override
//    public void showContent() {
//
//    }
//
//    @Override
//    public void showEmpty() {
//
//    }
//
//    @Override
//    public void onRequestFinish() {
//
//    }
//
//    @Override
//    public void getData() {
//
//    }
//
//    @Override
//    public void showDataErr() {
//
//    }
//
//    @Override
//    public void onGoClick(View v, MallCoupon firstcoupon) {
//        StyledDialog.init(getContext());
//        loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
//        Map<String,Object> map=new HashMap<>();
//        map.put("shopId",shopId);
//        map.put("memberId",memberId+"");
//        map.put("merchantCouponId",firstcoupon.merchantCouponId+"");
//        map.put(Functions.FUNCTION, "8001");
//        appCouponPresenter.reAppCoupon(map);
//    }
//
//    @Override
//    public void onGoHasClick(View v, MallCoupon firstcoupon) {
//
//
//
//        ARouter.getInstance()
//                .build(MallRoutes.MALL_STORE_LIST2_COUPON)
//                .withString("cityCode", cityNo+"")
//                .withString("lng", lng+"")
//                .withString("lat", lat+"")
//                .withString("couponHow",firstcoupon.getRequirement())
//                .withString("endTime",firstcoupon.couponTimeEnd+"")
//                .withString("merchantCouponId",firstcoupon.merchantCouponId+"")
//                .navigation();
//    }
//}
