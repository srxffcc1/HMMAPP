package com.healthy.library.business;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.contract.ServiceGoodsSpecContract;
import com.healthy.library.model.GoodsSpecCell;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.GoodsSpecLimit;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.presenter.ServiceGoodsSkuPresenter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.IncreaseDecreaseView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.disposables.Disposable;

public class GoodsSpecDialog extends BaseDialogFragment implements ServiceGoodsSpecContract.View {
    View dialogview = null;
    private ImageView goodsImg;
    private TextView goodsSpecMoney;
    private TextView goodsCount;
    private TextView goodsSelect;
    private ImageView close;
    private LinearLayout specLL;
    private ScrollView specLLS;
    private TextView goodCountTitle;
    private IncreaseDecreaseView increaseDecrease;
    public Map<String, Object> destmap = new HashMap<>();
    public List<GoodsSpecCell> goodsSpecList = new ArrayList<>();
    public GoodsSpecDetail goodsSpecDetail;
    public GoodsSpecDetail goodsSpecDetailNew;
    private TextView submitBtn, pinPeopleNum;
    private int regimentSize;//??????????????????????????????
    private int maxActInventorySpecial = -1;
    private TextView goodsSpecMoneyLeft;
    private IntegralModel vipInfo;
    private LinearLayout pointLL;
    private TextView pointValue;
    private TextView pointValueAdd;
    ShopDetailModel newStoreDetialModelSelect;

    public GoodsSpecDialog setSelectSku(String selectSku) {
        this.selectSku = selectSku;
        return this;
    }
    private String selectSku = "";
    private int mode = 1;//1?????????2?????????
    public void setMode(int mode) {
        this.mode = mode;
    }
    private boolean isNtReal = false;
    public void setNtReal(boolean ntReal) {
        isNtReal = ntReal;
    }
    private String marketingType = null;
    private String mapMarketingGoodsId = null;
    public void setMarketing(String marketingType, String mapMarketingGoodsId) {//??????????????????
        this.marketingType = marketingType;
        this.mapMarketingGoodsId = mapMarketingGoodsId;
    }

    public void setSingleSelectAct(boolean singleSelectAct) {
        isSingleSelectAct = singleSelectAct;
    }

    private boolean isSingleSelectAct = false;
    private boolean isSet = false;
    public void setSet(boolean set) {
        isSet = set;
    }
    public void setGoodsSpecDetail(GoodsSpecDetail goodsSpecDetail) {//?????????????????????????????????????????????
        this.goodsSpecDetail = goodsSpecDetail;
    }
    public void setGoodsId(String goodsId, String shopId) {//??????goodsId???shopId
        this.goodsId = goodsId;
        this.shopId = shopId;
    }
    public String shopId;
    public String goodsId;
    OnSpecSubmitListener onSpecSubmitListener;
    public void setOnSpecSubmitListener(OnSpecSubmitListener onSpecSubmitListener) {
        this.onSpecSubmitListener = onSpecSubmitListener;
    }
    public void setRegimentSize(int regimentSize) {
        this.regimentSize = regimentSize;
    }
    ServiceGoodsSkuPresenter serviceGoodsSkuPresenter;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        dialogview = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_spec, null);
        serviceGoodsSkuPresenter = new ServiceGoodsSkuPresenter(getContext(), this);
        initView(dialogview);
        buildView(dialogview);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ??????Content?????????
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true); // ??????????????????
        increaseDecrease.setOnNumChangedListener(new IncreaseDecreaseView.OnNumChangedListener() {
            @Override
            public void onNumChanged(int num) {
                buildGoodsMoney();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(limitString)) {
                    Toast.makeText(getActivity(), limitString, Toast.LENGTH_SHORT).show();
                    if (goodsSpecDetailNew != null) {
                        goodsSpecDetailNew.isErrorCount = true;//??????????????? ????????????????????????????????????
                    }
                }
                if (goodsSpecDetailNew == null) {
                    Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(limitString) && mode == 2) {
                    return;
                }
                if(goodsSpecDetailNew != null &&goodsSpecDetailNew.getAvailableInventory() <=0){
                    Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();

                }
                if (onSpecSubmitListener != null && goodsSpecDetailNew != null && goodsSpecDetailNew.getAvailableInventory() > 0) {
                    dismiss();
                    onSpecSubmitListener.onSpecSubmit(goodsSpecDetailNew
                            .setFilePath(goodsSpecDetail.filePath)
                            .setGoodsTitle(goodsSpecDetailNew.goodsTitle.replace(goodsSpecDetailNew.goodsSpecStr, ""))
                            .setCount(increaseDecrease.getNum() + ""));
                }
            }
        });

        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
        }

        return dialog;
    }

    private void buildView(View dialogview) {
        destmap.clear();
        buildGoods(true);
        initSpec(goodsSpecList);

    }

    public void buildGoodsSkuSelect() {
        if (checkMapSize(destmap) > 0) {
            goodsSelect.setVisibility(View.VISIBLE);
            goodsSelect.setText("??????" + getHasSku(destmap));
        } else {
            goodsSelect.setVisibility(View.INVISIBLE);
        }
        goodsCount.setVisibility(View.VISIBLE);
    }

    public void buildGoodsMoney() {
        //????????????
        goodsSpecMoneyLeft.setVisibility(View.GONE);
        pointLL.setVisibility(View.GONE);
        goodsSpecMoney.setVisibility(View.VISIBLE);
        pinPeopleNum.setVisibility(View.GONE);
        pointValueAdd.setVisibility(View.GONE);
        pointValue.setVisibility(View.GONE);
        pointLL.setVisibility(View.GONE);
        if (goodsSpecDetailNew != null) {//?????????????????????
            goodsSpecMoney.setText("??" + FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.getMarketingPrice()));//?????????????????????
            if ("5".equals(goodsSpecDetailNew.marketingType)) {//??????
                pointValue.setVisibility(View.VISIBLE);
                pointLL.setVisibility(View.VISIBLE);
                pointValue.setText(FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.getPointsPrice()));
                if (goodsSpecDetailNew.getMarketingPrice() <= 0) {//???????????? ?????????????????????
                    goodsSpecMoney.setVisibility(View.GONE);
                } else {
                    pointValueAdd.setVisibility(View.VISIBLE);
                }
            }
            else if ("4".equals(goodsSpecDetailNew.marketingType)) {//???getPlatformPrice????????????????????????
                if(isNtReal){
                    pinPeopleNum.setVisibility(View.VISIBLE);
                    pinPeopleNum.setText("????????????");
                }
            }
            else if ("3".equals(goodsSpecDetailNew.marketingType)) {//???getPlatformPrice????????????????????????
                pinPeopleNum.setVisibility(View.VISIBLE);
                pinPeopleNum.setText("??????");
            }
            else if ("2".equals(goodsSpecDetailNew.marketingType)) {//???getPlatformPrice????????????????????????
                pinPeopleNum.setVisibility(View.VISIBLE);
                pinPeopleNum.setText(regimentSize + "??????");
            }
            else if ("1".equals(goodsSpecDetailNew.marketingType)) {//???getPlatformPrice????????????????????????

            }
            else if ("8".equals(goodsSpecDetailNew.marketingType)) {//???getPlatformPrice????????????????????????

            }else {
                goodsSpecMoney.setText("??" + FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.getPlatformPrice()));//?????????
            }
        }else {//????????????????????????????????????????????????
            if(goodsSpecDetail!=null){

                goodsSpecMoney.setText("??" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getPlatformPrice()));
            }
        }
    }

    String limitString = "";

    public void buildGoods(boolean isfirst) {
        goodsCount.setText("");
        goodsCount.setVisibility(View.INVISIBLE);
        submitBtn.setAlpha(0.7f);
        if (isfirst) {
            submitBtn.setText("??????");
        } else {
            submitBtn.setAlpha(1f);
            submitBtn.setText("??????");
        }
        //??????????????????
        increaseDecrease.setMaxCount(1);
        increaseDecrease.setMinCount(1);
        if (checkMapSize(destmap) == goodsSpecList.size()) {//?????????????????????????????????
            if (goodsSpecDetailNew != null) {
                goodsSpecDetailNew.setParent(goodsSpecDetail);
                goodsSpecDetailNew.setShopDetailModelSelect(goodsSpecDetail.shopDetailModelSelect);
                goodsCount.setVisibility(View.VISIBLE);
                goodsCount.setText("??????" + goodsSpecDetailNew.getAvailableInventory() + "???");
                if(!isSet){
                    increaseDecrease.reset();
                    System.out.println("????????????????????????");
                    increaseDecrease.setMaxCount(goodsSpecDetailNew.getMarkLimitMax());
                    increaseDecrease.setMinCount(goodsSpecDetailNew.getMarkLimitMin());
                }
                if(increaseDecrease.getNum()>goodsSpecDetailNew.getMarkLimitMax()){
                    increaseDecrease.setNoCount(goodsSpecDetailNew.getMarkLimitMax());
                }
                if(increaseDecrease.getNum()<goodsSpecDetailNew.getMarkLimitMin()){
                    increaseDecrease.setNoCount(goodsSpecDetailNew.getMarkLimitMin());
                }
                limitString = "";
                if ("3".equals(goodsSpecDetailNew.marketingType)) {
                    if(goodsSpecDetailNew.getMarkLimitMinOrg()>0){
                        increaseDecrease.setLimitMinString("??????" + goodsSpecDetailNew.getMarkLimitMinOrg() + "???");
                    }
                    increaseDecrease.setLimitMaxString("??????????????????");
                    if(goodsSpecDetailNew.getMarkLimitMin()>goodsSpecDetailNew.getRealCanBuy()){//??????????????????????????????????????????????????????
                        if (goodsSpecDetailNew.getMarkLimitMin() > goodsSpecDetailNew.getAvailableInventoryMark()) {
                            limitString = "??????????????????";
                        } else if (goodsSpecDetailNew.getMarkLimitMax() - goodsSpecDetailNew.nowOrderBuyCount <= 0) {
                            limitString = "?????????" + goodsSpecDetailNew.nowOrderBuyCount + "???" + "?????????????????????" + goodsSpecDetailNew.getMarkLimitMax() + "???";
                        } else {
                            limitString = "??????" + goodsSpecDetailNew.getMarkLimitMin() + "??????" + "?????????" + goodsSpecDetailNew.nowOrderBuyCount + "??????" + "?????????????????????" + goodsSpecDetailNew.getMarkLimitMax() + "???";
                        }
                        increaseDecrease.setLimitMaxString(limitString);
                    }else {
                        if (goodsSpecDetailNew.getMarkLimitMaxOrg() > 0) {//????????????0
                            increaseDecrease.setLimitMaxString("?????????????????????" + goodsSpecDetailNew.getRealCanBuy() + "???");
                            if (goodsSpecDetailNew.getMarkLimitMin() > goodsSpecDetailNew.getAvailableInventoryMark()) {
                                increaseDecrease.setLimitMaxString("??????????????????");
                            }
                        }
                        if (goodsSpecDetailNew.getMarkLimitMin() > 0) {
                            increaseDecrease.setLimitMinString("??????" + goodsSpecDetailNew.getMarkLimitMin() + "???");
                        }
                    }
                }
                if ("5".equals(goodsSpecDetailNew.marketingType)) {
                    submitBtn.setText("????????????");
                    if (vipInfo != null) {
                        if (Double.parseDouble(vipInfo.SYJFTOT) < goodsSpecDetailNew.pointsPrice) {
                            submitBtn.setText("????????????");
                            submitBtn.setAlpha(0.7f);
                        }
                    }
                }
            }else {
                goodsSpecDetailNew = null;
            }
        }
        if (goodsSpecDetail != null) {
            if (!TextUtils.isEmpty(goodsSpecDetail.filePath)) {
                try {
                    com.healthy.library.businessutil.GlideCopy.with(getContext())
                            .load(goodsSpecDetail.filePath).placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(goodsImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        buildGoodsMoney();
    }

    public void initSpec(List<GoodsSpecCell> goodsSpecList) {
        this.goodsSpecList = goodsSpecList;
        goodsSpecDetailNew = null;
        if (specLLS != null) {
            try {
                goodsCount.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (goodsSpecList.size() > 6) {
                ViewGroup.LayoutParams layoutParams = specLLS.getLayoutParams();
                layoutParams.height = (int) TransformUtil.dp2px(specLLS.getContext(), 6 * 70f);
                specLLS.setLayoutParams(layoutParams);
            } else {
                ViewGroup.LayoutParams layoutParams = specLLS.getLayoutParams();
                layoutParams.height = (int) TransformUtil.dp2px(specLLS.getContext(), goodsSpecList.size() * 70f);
                specLLS.setLayoutParams(layoutParams);
            }
            specLL.removeAllViews();
            for (int i = 0; i < goodsSpecList.size(); i++) {
                specLL.addView(buildTagParent(specLLS.getContext(), destmap, goodsSpecList.get(i)));
            }
        }

    }

    private View buildTagParent(final Context mActivity, final Map<String, Object> destmap, final GoodsSpecCell goodsSpecCell) {
        View tagparent = View.inflate(mActivity, R.layout.service_item_spec, null);
        TextView textView = tagparent.findViewById(R.id.title);
        textView.setText(goodsSpecCell.getSpecName());
        TagFlowLayout tagFlowLayout = (TagFlowLayout) tagparent.findViewById(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter<String>(goodsSpecCell.specValue) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageTextView tag = (ImageTextView) LayoutInflater.from(mActivity).inflate(R.layout.service_item_spec_tag, parent, false);
                tag.setText(s);
                if (!TextUtils.isEmpty(selectSku)) {
                    if (selectSku.contains(s)) {
                        destmap.put(goodsSpecCell.id, goodsSpecCell.specValue[position]);
                        if (checkMapSize(destmap) == goodsSpecList.size()) {//???????????????????????????????????????  ??????????????????????????????
                            serviceGoodsSkuPresenter.getGoodsSkuResult(new SimpleHashMapBuilder<String, Object>()
                                    .puts("mapMarketingGoodsId", mapMarketingGoodsId)
                                    .puts("marketingType", "4".equals(marketingType) && !isNtReal ? null : marketingType)
                                    .puts("goodsId", goodsId)
                                    .puts("shopId", shopId)
                                    .puts("specValue", destmap));
                            buildGoodsSkuSelect();
                        }
                    }
                }
                return tag;
            }
        };
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                if (!view.isPressed()) {
//                    return false;
//                }
                TagView tagView = (TagView) view;
                if (tagView.isChecked()) {
                    destmap.put(goodsSpecCell.id, goodsSpecCell.specValue[position]);
                    if (checkMapSize(destmap) == goodsSpecList.size()) {//???????????????
                        if ("4".equals(marketingType) && !isNtReal) {//???????????????????????????????????????  ??????????????????????????????
                            serviceGoodsSkuPresenter.getGoodsSkuResult(new SimpleHashMapBuilder<String, Object>()
                                    .puts("goodsId", goodsId)
                                    .puts("shopId", shopId)
                                    .puts("specValue", destmap));
                        } else {
                            serviceGoodsSkuPresenter.getGoodsSkuResult(new SimpleHashMapBuilder<String, Object>()
                                    .puts("mapMarketingGoodsId", mapMarketingGoodsId)
                                    .puts("marketingType", marketingType)
                                    .puts("goodsId", goodsId)
                                    .puts("shopId", shopId)
                                    .puts("specValue", destmap));
                        }
                    }
                } else {
                    destmap.put(goodsSpecCell.id, "");//????????????
                    goodsSpecDetailNew = null;
                    buildGoods(true);
                }
                buildGoodsSkuSelect();
                return false;
            }
        });
        tagFlowLayout.setAdapter(tagAdapter);
        for (int i = 0; i < goodsSpecCell.specValue.length; i++) {
            if (selectSku != null && selectSku.contains(goodsSpecCell.specValue[i])) {
                tagAdapter.setSelectedList(i);
            }
        }

        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet.size() > 0) {

                }

            }
        });

        return tagparent;
    }

    public int checkMapSize(Map<String, Object> destmap) {
        int result = 0;
        for (Map.Entry<String, Object> entry : destmap.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue().toString())) {
                result++;
            }
        }
        return result;
    }

    public String getHasSku(Map<String, Object> destmap) {
        String result = "";
        for (Map.Entry<String, Object> entry : destmap.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue().toString())) {
                result += " \"" + entry.getValue() + "\"";
            }
        }
        return result;

    }

    private void initView(View view) {
        goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
        goodsSpecMoney = (TextView) view.findViewById(R.id.goodsSpecMoney);
        goodsCount = (TextView) view.findViewById(R.id.goodsCount);
        goodsSelect = (TextView) view.findViewById(R.id.goodsSelect);
        close = (ImageView) view.findViewById(R.id.close);
        specLL = (LinearLayout) view.findViewById(R.id.specLL);
        goodCountTitle = (TextView) view.findViewById(R.id.goodCountTitle);
        increaseDecrease = (IncreaseDecreaseView) view.findViewById(R.id.increase_decrease);
        specLLS = (ScrollView) view.findViewById(R.id.specLLS);
        submitBtn = (TextView) view.findViewById(R.id.submitBtn);
        pinPeopleNum = (TextView) view.findViewById(R.id.pinPeopleNum);
        goodsSpecMoneyLeft = (TextView) view.findViewById(R.id.goodsSpecMoneyLeft);
        pointLL = (LinearLayout) view.findViewById(R.id.pointLL);
        pointValue = (TextView) view.findViewById(R.id.pointValue);
        pointValueAdd = (TextView) view.findViewById(R.id.pointValueAdd);
    }

    public static GoodsSpecDialog newInstance() {

        Bundle args = new Bundle();
        GoodsSpecDialog fragment = new GoodsSpecDialog();
        fragment.setArguments(args);
        return fragment;
    }
    private GoodsSpecDetail checkRealSpec(GoodsSpecDetail goodsSpecDetailNew, List<GoodsSpecDetail> exSkuList) {
        if (goodsSpecDetailNew != null) {
            for (int i = 0; i < exSkuList.size(); i++) {
                if (goodsSpecDetailNew.getGoodsChildId().equals(exSkuList.get(i).getGoodsChildId())) {
                    return exSkuList.get(i);
                }
            }
        }
        return null;
    }
    @Override
    public void successGetGoodsSkuResult(List<GoodsSpecDetail> result) {
        if (result != null && result.size() > 0) {
            goodsSpecDetailNew = result.get(0);
            if (exSkuList != null) {
                if (exSkuList.size() > 0 && isSingleSelectAct) {//???????????????isSingleSelectAct ?????? ??????????????????????????? ?????????????????????
                    if (checkRealSpec(goodsSpecDetailNew, exSkuList) != null && checkRealSpec(goodsSpecDetailNew, exSkuList).getAvailableInventoryMark() <= 0) {//?????????????????????
                        if (!"8".equals(marketingType)) {

                        }
                    } else { //??????????????????type???????????????type????????????
                        if (checkRealSpec(goodsSpecDetailNew, exSkuList) == null) {
                            Toast.makeText(getActivity(), "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            goodsSpecDetailNew = checkRealSpec(goodsSpecDetailNew, exSkuList);
                        }
                    }
                }
            }
            if ("3".equals(goodsSpecDetailNew.marketingType)) {
                serviceGoodsSkuPresenter.getGoodsLimitResult(new SimpleHashMapBuilder<String, Object>().puts("includeNoPay", "1").putObject(
                        new GoodsSpecLimit(
                                "1",
                                goodsSpecDetailNew.goodsId + "",
                                goodsSpecDetailNew.getGoodsSpec(),
                                goodsSpecDetailNew.mapMarketingGoodsId,
                                goodsSpecDetailNew.marketingType,
                                goodsSpecDetailNew.getgoodsMarketingGoodsSpec(),
                                goodsSpecDetailNew.marketingId, goodsSpecDetail.goodsType)));
            } else {
                if (marketingType != null && result.get(0).marketingType == null && (exSkuList == null || exSkuList.size() == 0)) {//
                    if (!"8".equals(marketingType)) {
                        if("4".equals(marketingType)&&result.get(0).marketingType == null&&!isNtReal){//??????????????? ??????????????????????????? ????????????????????????????????????????????????

                        }else {
                            Toast.makeText(getActivity(), "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                buildGoods(false);
            }
        }
    }

    @Override
    public void successGetGoodsSkuFinalResult(GoodsSpecLimit result) {
        try {
            if (result != null) {
                goodsSpecDetailNew.nowOrderBuyCount = result.totalQuantity;
            } else {
                goodsSpecDetailNew.nowOrderBuyCount = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        buildGoods(false);
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
    private List<GoodsSpecDetail> exSkuList;// ????????????????????????

    public void setSkuListEx(List<GoodsSpecDetail> list) {
        for (int i = 0; i < list.size(); i++) {
            //System.out.println("????????????:" + list.get(i).marketingType);
        }
        exSkuList = list;
    }

    public void setVipInfo(IntegralModel mvipInfo) {
        this.vipInfo=mvipInfo;
    }

    public interface OnSpecSubmitListener {
        void onSpecSubmit(GoodsSpecDetail goodsSpecDetail);
    }
}
