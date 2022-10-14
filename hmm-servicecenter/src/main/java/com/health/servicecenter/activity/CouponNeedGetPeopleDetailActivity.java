package com.health.servicecenter.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.R;
import com.health.servicecenter.widget.ShopOrderPickDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.CardPackContract;
import com.healthy.library.contract.VoteCouponDetailContract;
import com.healthy.library.dialog.SingleWheelDialog;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.presenter.CardPickPresenter;
import com.healthy.library.presenter.VoteCouponDetailPresenter;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CommonInsertSection;
import com.healthy.library.widget.TopBar;

import java.util.List;

@Route(path = ServiceRoutes.SERVICE_COUPON_PEOPLE)
public class CouponNeedGetPeopleDetailActivity extends BaseActivity implements CardPackContract.View, VoteCouponDetailContract.View {
    @Autowired
    String memberCouponId;
    public SingleWheelDialog singleWheelDialog;
    private CardPickPresenter cardPickPresenter;
    private com.healthy.library.widget.TopBar topBar;
    private LinearLayout relativeLayout;
    private TextView cardMoney;
    private TextView cardName;
    private TextView cardTitle;
    private TextView cardTime;
    private com.healthy.library.widget.CommonInsertSection jobName;
    private com.healthy.library.widget.CommonInsertSection jobPhone;
    private com.healthy.library.widget.CommonInsertSection jobLoc;
    private com.healthy.library.widget.CommonInsertSection jobHistory;
    private TextView submitAddressTxt;
    private String userName;
    private String userPhone;
    private String userSex;
    private String shopId;
    private String shopName;
    private String couponId;
    private String merchantId;
    private VoteCouponDetailPresenter voteCouponDetailPresenter;
    ShopOrderPickDialog shopOrderPickDialog;
    List<ShopDetailModel> storeDetialModelList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_topic_detail;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        submitAddressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIllegal()) {
                    submit();
                }
            }
        });
        jobLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexDialog();
            }
        });
        jobHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickShop();
            }
        });
    }

    private void showSexDialog() {
        if (singleWheelDialog == null) {
            singleWheelDialog = SingleWheelDialog.newInstance(new SimpleArrayListBuilder<String>().adds("男").adds("女"),"性别");
        }
        singleWheelDialog.setOnConfirmClick(new SingleWheelDialog.OnConfirmClickListener() {
            @Override
            public void onClick(int pos, String data) {
                jobLoc.getTxtContent().setText(data);
                userSex=data;
            }
        });
        singleWheelDialog.show(getSupportFragmentManager(), "选择性别");

    }

    private void showPickShop() {
        if (shopOrderPickDialog == null) {
            shopOrderPickDialog = ShopOrderPickDialog.newInstance();
        }
        try {
            shopOrderPickDialog.setSelectId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopOrderPickDialog.show(getSupportFragmentManager(), "选择门店");
        shopOrderPickDialog.setGoodsShopList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
            @Override
            public GoodsShop getDesObj(ShopDetailModel o) {
                return new GoodsShop(o);
            }
        }));
        shopOrderPickDialog.setTitle("选择门店");
        shopOrderPickDialog.setOnDialogShopClickListener(new ShopOrderPickDialog.OnDialogShopClickListener() {
            @Override
            public void onDialogShopClick(GoodsShop goodsShop) {
                shopName=goodsShop.shopName;
                shopId=goodsShop.shopId;
                jobHistory.getTxtContent().setText(goodsShop.getShopAddressDetail()+goodsShop.shopName);
            }
        });
    }

    private void submit() {
        voteCouponDetailPresenter.save(new SimpleHashMapBuilder<String, Object>()
                .puts("userName",jobName.getEtContent().getText().toString())
                .puts("userPhone",jobPhone.getEtContent().getText().toString())
                .puts("userSex",userSex)
                .puts("shopId",shopId)
                .puts("shopName",shopName)
                .puts("couponId",couponId)
                .puts("merchantId",merchantId)
        );
    }

    public boolean checkIllegal() {
        if(TextUtils.isEmpty(shopId)){
            showToast("请选择门店");
            return false;
        }
        if(TextUtils.isEmpty(jobName.getEtContent().getText().toString())){
            showToast("请输入名字");
            return false;
        }
        if(TextUtils.isEmpty(jobPhone.getEtContent().getText().toString())){
            showToast("请输入手机");
            return false;
        }
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        merchantId= SpUtils.getValue(this, SpKey.CHOSE_MC);
        cardPickPresenter = new CardPickPresenter(this, this);
        voteCouponDetailPresenter=new VoteCouponDetailPresenter(this,this);
        cardPickPresenter.getCouponDetail(new SimpleHashMapBuilder<String, Object>().puts("memberCouponId", memberCouponId));
        voteCouponDetailPresenter.getStoreList(new SimpleHashMapBuilder<String, Object>().puts("merchantId",merchantId));
    }

    @Override
    public void onSucessGetCouponList(List<CouponInfoZ> adModels) {

    }

    @Override
    public void onSucessGetCoupon(CouponInfoZ couponInfoZ) {
        couponId=couponInfoZ.getCouponId();
        cardMoney.setText(FormatUtils.moneyKeep2Decimals(couponInfoZ.getCouponDenomination()));
        if (couponInfoZ.getCouponDenomination().length() > 4) {
            cardMoney.setTextSize(24);
        } else if (couponInfoZ.getCouponDenomination().length() > 3) {
            cardMoney.setTextSize(27);
        } else if (couponInfoZ.getCouponDenomination().length() > 2) {
            cardMoney.setTextSize(34);
        } else if (couponInfoZ.getCouponDenomination().length() > 1) {
            cardMoney.setTextSize(34);
        } else {
            cardMoney.setTextSize(34);
        }
        cardName.setText(couponInfoZ.getCouponUseName());
        cardTitle.setText(couponInfoZ.getCouponNormalName());
        if (couponInfoZ.usableStartTime != null && couponInfoZ.usableEndTime != null) {
            cardTime.setText(couponInfoZ.usableStartTime.split(" ")[0] + "-" + couponInfoZ.usableEndTime.split(" ")[0]);
        } else {
            cardTime.setText("");
        }

    }

    @Override
    public void onSucessGetInsert(Boolean hasinsert) {

    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        relativeLayout = (LinearLayout) findViewById(R.id.relativeLayout);
        cardMoney = (TextView) findViewById(R.id.cardMoney);
        cardName = (TextView) findViewById(R.id.cardName);
        cardTitle = (TextView) findViewById(R.id.cardTitle);
        cardTime = (TextView) findViewById(R.id.cardTime);
        jobName = (CommonInsertSection) findViewById(R.id.jobName);
        jobPhone = (CommonInsertSection) findViewById(R.id.jobPhone);
        jobLoc = (CommonInsertSection) findViewById(R.id.jobLoc);
        jobHistory = (CommonInsertSection) findViewById(R.id.jobHistory);
        submitAddressTxt = (TextView) findViewById(R.id.submit_address_txt);
    }

    @Override
    public void onSucessSubmit() {
        showToast("保存成功");
        finish();
    }

    @Override
    public void onGetStoreListSuccess(List<ShopDetailModel> storeDetialModelList) {
        this.storeDetialModelList=storeDetialModelList;
    }
}