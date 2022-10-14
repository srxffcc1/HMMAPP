package com.health.mall.widget;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.mall.R;
import com.healthy.library.adapter.PinNormalAdapter;
import com.healthy.library.constant.Functions;
import com.healthy.library.contract.AppPinContract;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.model.Goods2ShopModelPin;
import com.healthy.library.presenter.AppPinPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.widget.BaseFullBottomSheetFragment;
import com.healthy.library.business.KKGroupGoodsDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;

public class PinWithShopDialog extends BaseFullBottomSheetFragment implements AppPinContract.View  {

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private ImageView imgBack;
    private RecyclerView recycler;
    AppPinPresenter appCouponPresenter;
    PinNormalAdapter pinNormalAdapter;
    public String goodsId;
    public String shopId;
    public String areaNo;
    public String lng;
    public String lat;
    public String assembleId;
    private Goods2ShopModelPin mGoodsModel;





    public PinWithShopDialog setData(Goods2ShopModelPin mGoodsModel,String assembleId,String goodsId, String shopId, String areaNo, String lng, String lat) {
        this.assembleId=assembleId;
        this.goodsId = goodsId;
        this.shopId = shopId;
        this.areaNo = areaNo;
        this.lng = lng;
        this.lat = lat;
        this.mGoodsModel=mGoodsModel;
        return this;
    }

    Map<String, Object> listmap=new HashMap<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pin_withshop_list, null);

        imgBack = (ImageView) view.findViewById(R.id.img_back);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        virtualLayoutManager = new VirtualLayoutManager(getContext());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        pinNormalAdapter=new PinNormalAdapter();
        delegateAdapter.addAdapter(pinNormalAdapter);
        pinNormalAdapter.setGoOnClickListener(new PinNormalAdapter.PinOnClickListener() {
            @Override
            public void onGoClick(View v, final AssemableTeam firstcoupon) {

                if(firstcoupon.isMyTeam(getContext())){
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_GROUPDETAIL)
                            .withString("teamNum",firstcoupon.teamNum)
                            .navigation();
                }else {
                    KKGroupGoodsDialog.newInstance().setAssemableTeam(firstcoupon)
                            .setGroupGoodsDialogClicker(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    ARouter.getInstance()
//                                            .build(MallRoutes.MALL_ORDER)
//                                            .withString("cityNo",(Integer.parseInt(areaNo) / 100 * 100)+"")
//                                            .withString("areaNo",areaNo)
//                                            .withString("lng",lng+"")
//                                            .withString("lat",lat+"")
//                                            .withString("shopId",shopId+"")
//                                            .withString("goodsId",mGoodsModel.goods.id+"")
//                                            .withString("orderType","4")
//                                            .withString("platformPrice",mGoodsModel.assembleDTO.assemblePrice+"")
//                                            .withString("assembleId",mGoodsModel.assembleDTO.id)
//                                            .withString("teamNum", firstcoupon.teamNum)
//                                            .navigation();
                                }
                            }).show(getChildFragmentManager(),"kkOkDialog");
                }
                dismiss();



            }
        });
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        init();
        appCouponPresenter=new AppPinPresenter(getContext(),this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }


    public void init(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listmap.put("assembleId",assembleId);
                listmap.put(Functions.FUNCTION, "9015");
                listmap.put("currentPage", 1+"");
                listmap.put("pageSize", "100");
                appCouponPresenter.getAppPinList(listmap);
            }
        },300);
    }



    public static PinWithShopDialog newInstance() {
        Bundle args = new Bundle();
        PinWithShopDialog fragment = new PinWithShopDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void onSucessGetAppCouponList(List<AssemableTeam> mallCouponNoList) {
        for (int i = 0; i <mallCouponNoList .size(); i++) {
            AssemableTeam item=mallCouponNoList.get(i);
            item.realEndTime=mGoodsModel.assembleDTO.endTime;
            item.regimentTimeLength=mGoodsModel.assembleDTO.regimentTimeLength;
            item.regimentSize=mGoodsModel.assembleDTO.regimentSize;
        }
        pinNormalAdapter.setData((ArrayList<AssemableTeam>) mallCouponNoList);
    }
}
