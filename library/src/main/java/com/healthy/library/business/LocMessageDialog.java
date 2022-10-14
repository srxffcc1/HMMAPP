package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.adapter.LocMessageAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.LocVipContract;
import com.healthy.library.message.DialogDissmiss;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.LocVip;
import com.healthy.library.presenter.LocVipPresenter;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.hss01248.dialog.StyledDialog;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.disposables.Disposable;

public class LocMessageDialog extends BaseDialogFragment implements LocVipContract.View, TextView.OnEditorActionListener {
    private ConstraintLayout dialogBg;
    private RecyclerView contentRecycler;
    private ImageView dialogCloseButton;
    private ImageView passButton;
    private ImageView iconMessageDialog;
    private TextView titleMessageDialog;
    private TextView contentMessageDialog;
    private Space space;
    private TextView buttonMessageDialog;
    private MessageOkClickListener messageOkClickListener;
    private String merchantId;

    public int messageTopRes;
    public String messageCenterText;
    public String messageBottomText;
    private LinearLayout contentMessageLL;
    private LinearLayout contentMessagePassLL;
    private LocVipPresenter locVipPresenter;

    private ConstraintLayout topView;
    private TextView currentShopTxt;
    private TextView locRefreshTxt;
    private TextView locShopName;
    private TextView locDistance;
    private TextView locShopAddress;
    private RecyclerView locRecycle;
    private LocMessageAdapter locMessageAdapter;
    private Dialog loading;
    private ConstraintLayout seachLLZ;
    private LinearLayout seachLL;
    private EditText serarchKeyWord;
    private ImageView clearEdit;

    public LocMessageDialog setMessageTopRes(int messageTopRes, String messageCenterText, String messageBottomText) {
        this.messageTopRes = messageTopRes;
        this.messageCenterText = messageCenterText;
        this.messageBottomText = messageBottomText;
        return this;
    }

    public LocMessageDialog setMessage(String messageCenterText, String messageBottomText) {
        this.messageCenterText = messageCenterText;
        this.messageBottomText = messageBottomText;
        return this;
    }

    public LocMessageDialog setMessageOkClickListener(MessageOkClickListener messageOkClickListener) {
        this.messageOkClickListener = messageOkClickListener;
        return this;
    }

    public LocMessageDialog setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LocMessageDialog newInstance() {

        Bundle args = new Bundle();
        LocMessageDialog fragment = new LocMessageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_loc_change, null);
        builder.setView(view);
        locVipPresenter = new LocVipPresenter(getActivity(), this);
        displayDialog(view);
        //监听back事件
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    EventBus.getDefault().post(new DialogDissmiss());
                }
                return false;
            }
        });
        return builder.create();
    }

    private void displayDialog(View view) {
        StyledDialog.init(getContext());
        loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
        dialogBg = (ConstraintLayout) view.findViewById(R.id.dialog_bg);
        iconMessageDialog = (ImageView) view.findViewById(R.id.iconMessageDialog);
        titleMessageDialog = (TextView) view.findViewById(R.id.titleMessageDialog);
        contentMessageDialog = (TextView) view.findViewById(R.id.contentMessageDialog);
        space = (Space) view.findViewById(R.id.space);
        buttonMessageDialog = (TextView) view.findViewById(R.id.buttonMessageDialog);
        contentMessageLL = (LinearLayout) view.findViewById(R.id.contentMessageLL);
        contentMessagePassLL = (LinearLayout) view.findViewById(R.id.contentMessagePassLL);

        topView = (ConstraintLayout) view.findViewById(R.id.topView);
        currentShopTxt = (TextView) view.findViewById(R.id.currentShopTxt);
        locRefreshTxt = (TextView) view.findViewById(R.id.locRefreshTxt);
        locShopName = (TextView) view.findViewById(R.id.locShopName);
        locDistance = (TextView) view.findViewById(R.id.locDistance);
        locShopAddress = (TextView) view.findViewById(R.id.locShopAddress);
        locRecycle = (RecyclerView) view.findViewById(R.id.locRecycle);
        seachLLZ = (ConstraintLayout) view.findViewById(R.id.seachLLZ);
        seachLL = (LinearLayout) view.findViewById(R.id.seachLL);
        serarchKeyWord = (EditText) view.findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) view.findViewById(R.id.clearEdit);
        contentMessagePassLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageOkClickListener != null) {
                    messageOkClickListener.onMessageOkClick(v);
                }

                dismiss();
            }
        });
        locMessageAdapter = new LocMessageAdapter();
        if (SpUtils.getValue(getActivity(), SpKey.OPERATIONSTATUS, false)) {//运营模式
            seachLLZ.setVisibility(View.VISIBLE);
        }

        serarchKeyWord.setOnEditorActionListener(this);
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
                filterLoc(serarchKeyWord.getText().toString());
            }
        });
        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEdit.setVisibility(View.VISIBLE);
                    if (s.toString().length() > 1) {
                        filterLoc(serarchKeyWord.getText().toString());
                    }
                } else {
                    clearEdit.setVisibility(View.GONE);
                    filterLoc(serarchKeyWord.getText().toString());
                }
            }
        });
        getData();
    }


    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);
    }

    private void initView() {


    }

    List<LocVip.Local.MerchantsShop> localShopList = new ArrayList<>();

    @Override
    public void onSucessGetLocVip(LocVip locVip) {
        localList.clear();
        localShopList.clear();
        if (getActivity() == null) {
            dismissAllowingStateLoss();
            return;
        }
        if (locVip != null) {
            if (SpUtils.getValue(getActivity(), SpKey.OPERATIONSTATUS, false)) {//运营模式
                seachLLZ.setVisibility(View.VISIBLE);
                showToast("因数据过多已为您开启辅助视图");
            }
            if (!TextUtils.isEmpty(SpUtils.getValue(getActivity(), SpKey.CHOSE_SHOP))) {
                if (!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(locVip.getAllMerchantShopWithMe(), new ObjectIteraor<LocVip.Local.MerchantsShop>() {
                    @Override
                    public String getDesObj(LocVip.Local.MerchantsShop o) {
                        return o.shopId;
                    }
                }), SpUtils.getValue(getActivity(), SpKey.CHOSE_SHOP))) {//加判下历史选择是不是当前可选
                    Log.i("SDT", "出现历史问题");
                    AMapLocation aMapLocation = LocUtil.getLocationBean(getActivity(), SpKey.LOC_CHOSE);
                    if (aMapLocation == null) {
                        aMapLocation = new AMapLocation("location");
                    }
                    aMapLocation.setAdCode(locVip.getAllMerchantWithMe().get(0).getNearShop().district);//默认市级加5 得到模糊区县
                    aMapLocation.setProvince(locVip.getAllMerchantWithMe().get(0).getNearShop().provinceName);
                    aMapLocation.setCity(locVip.getAllMerchantWithMe().get(0).getNearShop().cityName);
                    aMapLocation.setDistrict(locVip.getAllMerchantWithMe().get(0).getNearShop().districtName);
                    LocUtil.setNowShop(locVip.getAllMerchantShopWithMe().get(0));
                    LocUtil.storeLocationChose(getActivity(), aMapLocation);
                    EventBus.getDefault().post(new UpdateUserLocationMsg());
                    EventBus.getDefault().post(new DialogDissmiss());

                }
            }else {
                if (TextUtils.isEmpty(SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP))) {
                    if(locVip!=null){
                        List<LocVip.Local.MerchantsShop> shopList = locVip.getAllMerchantShopWithMe();
                        //System.out.println("默认选一个:" + shopList.get(0).getAddressDetails());
                        AMapLocation aMapLocation = LocUtil.getLocationBean(LibApplication.getAppContext(), SpKey.LOC_CHOSE);
                        if (aMapLocation == null) {
                            aMapLocation = new AMapLocation("location");
                        }
                        aMapLocation.setAdCode(locVip.getAllMerchantWithMe().get(0).getNearShop().district);//默认市级加5 得到模糊区县
                        aMapLocation.setProvince(locVip.getAllMerchantWithMe().get(0).getNearShop().provinceName);
                        aMapLocation.setCity(locVip.getAllMerchantWithMe().get(0).getNearShop().cityName);
                        aMapLocation.setDistrict(locVip.getAllMerchantWithMe().get(0).getNearShop().districtName);
                        LocUtil.storeLocationChose(LibApplication.getAppContext(), aMapLocation);
                        LocUtil.setNowShop(shopList.get(0));
                        EventBus.getDefault().post(new UpdateUserLocationMsg());
                    }
                }
            }
            List<LocVip.Local> tmplocalList = new ArrayList<>();
            tmplocalList.addAll(locVip.getAllMerchantWithMe());
            if (SpUtils.getValue(getActivity(), SpKey.OPERATIONSTATUS, false)) {//运营模式
                if (LocUtil.getCityNo(getActivity(), SpKey.LOC_CHOSE) != null && SpUtils.getValue(getActivity(), SpKey.CHOSE_MC) != null) {//有选的电了
                    for (int i = 0; i < tmplocalList.size(); i++) {
                        if (tmplocalList.get(i).city.equals(LocUtil.getCityNo(getActivity(), SpKey.LOC_CHOSE)) &&
                                tmplocalList.get(i).merchantId.equals(SpUtils.getValue(getActivity(), SpKey.CHOSE_MC))) {
                            localList.add(0, tmplocalList.get(i));
                            tmplocalList.remove(i);
                        } else if (tmplocalList.get(i).merchantId.equals(SpUtils.getValue(getActivity(), SpKey.CHOSE_MC))) {
                            localList.add(tmplocalList.get(i));
                            tmplocalList.remove(i);
                        }
                    }
                    localList.addAll(tmplocalList);
                } else {
                    localList = tmplocalList;
                }
            } else {
                localList = tmplocalList;
            }

            buildTopView(locVip);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StyledDialog.dismiss(loading);
                }
            }, 500);
            buildLocView();
        } else {
            try {
                EventBus.getDefault().post(new DialogDissmiss());
                getDialog().dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    List<LocVip.Local> localList = new ArrayList<>();

    public void filterLoc(String shopName) {
        if (TextUtils.isEmpty(shopName)) {
            shopName = null;
        }
        List<LocVip.Local.MerchantsShop> result = new ArrayList<>();
        for (int i = 0; i < localShopList.size(); i++) {
            if (shopName == null) {
                result.add(localShopList.get(i));
            } else {
                if (shopName != null && localShopList.get(i).toString().contains(shopName)) {
                    result.add(localShopList.get(i));
                }
            }
        }
        try {
            locMessageAdapter.setData((ArrayList) result);
            locMessageAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildLocView() {
        for (int i = 0; i < localList.size(); i++) {
            if (localList.get(i).getMerchantsMap() != null) {
                for (int j = 0; j < localList.get(i).getMerchantsMap().size(); j++) {
                    localList.get(i).getMerchantsMap().get(j).partnerName = localList.get(i).partnerName;
                }
                localShopList.addAll(localList.get(i).getMerchantsMap());
            }
        }

        Map<String, LocVip.Local.MerchantsShop> clearMap = new HashMap<>();
        for (int i = 0; i < localShopList.size(); i++) {
            if (!localShopList.get(i).shopId.equals(SpUtils.getValue(getActivity(), SpKey.CHOSE_SHOP))) {
                clearMap.put(localShopList.get(i).shopId, localShopList.get(i));
            }
        }
        if (!SpUtils.getValue(getActivity(), SpKey.OPERATIONSTATUS, false)) {
            localShopList.clear();
            Set<Map.Entry<String, LocVip.Local.MerchantsShop>> set = clearMap.entrySet();
            // 遍历键值对对象的集合，得到每一个键值对对象
            for (Map.Entry<String, LocVip.Local.MerchantsShop> me : set) {
                localShopList.add(me.getValue());
            }
            Collections.sort(localShopList);
        } else {
            for (int i = 0; i < localShopList.size(); i++) {//运营模式的去重 直接就这样好了
                if (localShopList.get(i).shopId.equals(SpUtils.getValue(getActivity(), SpKey.CHOSE_SHOP))) {
                    localShopList.remove(i);
                }
            }
        }
        if (localShopList.size() > 0) {
            locRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
            locRecycle.setAdapter(locMessageAdapter);
            locMessageAdapter.setData((ArrayList) localShopList);
            locMessageAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
                @Override
                public void outClick(@NotNull String function, @NotNull Object obj) {
                    if ("click".equals(function)) {
                        final LocVip.Local.MerchantsShop merchantsShop = (LocVip.Local.MerchantsShop) obj;
                        if (merchantsShop != null) {
                            AMapLocation aMapLocation = LocUtil.getLocationBean(getActivity(), SpKey.LOC_CHOSE);
                            if (aMapLocation == null) {
                                aMapLocation = new AMapLocation("location");
                            }
                            aMapLocation.setAdCode(merchantsShop.district);//默认市级加5 得到模糊区县
                            aMapLocation.setProvince(merchantsShop.provinceName);
                            aMapLocation.setCity(merchantsShop.cityName);
                            aMapLocation.setDistrict(merchantsShop.districtName);
                            LocUtil.setNowShop(merchantsShop);
                            LocUtil.storeLocationChose(getActivity(), aMapLocation);
                            EventBus.getDefault().post(new UpdateUserLocationMsg());
                            EventBus.getDefault().post(new DialogDissmiss());
                            dismiss();
                        }
                    }
                }
            });
        }
//        for (int i = 0; i < localShopList.size(); i++) {
//            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_item_loc_change, contentMessageLL, false);
//            final LocVip.Local.MerchantsShop local = localShopList.get(i);
//            TextView textView = (TextView) view;
//            textView.setText(local.getCityName() + local.getPartnerName());
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AMapLocation aMapLocation = LocUtil.getLocationBean(getActivity(), SpKey.LOC_CHOSE);
//                    aMapLocation.setAdCode(local.getDistrict());//默认市级加5 得到模糊区县
//                    aMapLocation.setProvince(local.provinceName);
//                    aMapLocation.setCity(local.getCityName());
//                    aMapLocation.setDistrict(local.districtName);
//                    SpUtils.store(getActivity(), SpKey.CHOSE_MC, local.merchantId);
//                    SpUtils.store(getActivity(), SpKey.CHOSE_SHOP, local.shopId);
//                    SpUtils.store(getActivity(), SpKey.CHOSE_SHOPNAME, local.shopName);
//                    LocUtil.storeLocationChose(getActivity(), aMapLocation);
//                    EventBus.getDefault().post(new UpdateUserLocationMsg());
//                    if (messageOkClickListener != null) {
//                        messageOkClickListener.onMessageOkClick(v);
//                    }
//                    dismiss();
//                }
//            });
//            contentMessageLL.addView(view);
//        }

    }

    private void buildTopView(LocVip locVip) {
        String shopId = SpUtils.getValue(getContext(), SpKey.CHOSE_SHOP);
        String merchantId = SpUtils.getValue(getContext(), SpKey.CHOSE_MC);
        if (shopId == null) {
            topView.setVisibility(View.GONE);
        } else {
            topView.setVisibility(View.VISIBLE);
            if (SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPNAME) != null) {
                locShopName.setText(SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPNAME));
            }
            if (SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPADDRESS) != null) {
                locShopAddress.setText(SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPADDRESS));
            }
            if (SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPDISTANCE) != null && !TextUtils.isEmpty(SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPDISTANCE))) {
                String shopDistanceDes = "";
                if (Double.parseDouble(SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPDISTANCE)) >= 1000) {
                    shopDistanceDes = String.format("%.1f", (double) Integer.parseInt(SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPDISTANCE)) / 1000) + "km";
                } else {
                    shopDistanceDes = FormatUtils.moneyKeep2Decimals(SpUtils.getValue(getContext(), SpKey.CHOSE_SHOPDISTANCE)) + "m";
                }
                locDistance.setText("距您" + shopDistanceDes);
            }

//            if (locVip.local.getMerchantsMap() != null && locVip.local.getMerchantsMap().size() > 0) {
//                topView.setVisibility(View.VISIBLE);
//                for (int i = 0; i < locVip.local.getMerchantsMap().size(); i++) {
//                    if (shopId.equals(locVip.local.getMerchantsMap().get(i).shopId) && merchantId.equals(locVip.local.getMerchantsMap().get(i).merchantId)) {
//                        LocVip.Local.MerchantsShop merchantsShop = locVip.local.getMerchantsMap().get(i);
//                        locShopName.setText(merchantsShop.getCityName() + merchantsShop.shopName);
//                        locShopAddress.setText(merchantsShop.getAddressDetails());
//                        String shopDistanceDes = "";
//                        if (merchantsShop.distance >= 1000) {
//                            shopDistanceDes = String.format("%.1f", (double) merchantsShop.distance / 1000) + "km";
//                        } else {
//                            shopDistanceDes = merchantsShop.distance + "m";
//                        }
//                        locDistance.setText("距您" + shopDistanceDes);
//                    }
//                }
//            } else {
//                if (locVip.options != null && locVip.options.get(0).getMerchantsMap().size() == 0) {
//
//                }
//
//            }

            topView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DialogDissmiss());
                    dismiss();
                }
            });
            //刷新定位点击
            locRefreshTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Toast.makeText(getContext(), "刷新定位成功", Toast.LENGTH_LONG).show();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, 300);
                    LocUtil.clearNowShop();//先不改
                    loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
                    getData();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(getContext(), "刷新定位成功,正在为您选择最近的门店", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                }
            });
        }
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StyledDialog.dismiss(loading);
            }
        }, 500);
    }

    @Override
    public void getData() {
        locVipPresenter.getLocVip(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    public void showDataErr() {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
//                return false;
//            }
            hideSoftKeyBoard(v);
            filterLoc(serarchKeyWord.getText().toString());
        }
        return false;
    }

    public interface MessageOkClickListener {
        public void onMessageOkClick(View view);
    }
}
