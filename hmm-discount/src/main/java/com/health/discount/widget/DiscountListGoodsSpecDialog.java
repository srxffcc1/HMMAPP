//package com.health.discount.widget;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.healthy.library.R;
//import com.healthy.library.base.BaseDialogFragment;
//import com.healthy.library.contract.ServiceGoodsSpecContract;
//import com.healthy.library.model.GoodsSpecCell;
//import com.healthy.library.model.GoodsSpecDetail;
//import com.healthy.library.model.GoodsSpecLimit;
//import com.healthy.library.model.IntegralModel;
//import com.healthy.library.presenter.ServiceGoodsSkuPresenter;
//import com.healthy.library.builder.SimpleHashMapBuilder;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.ImageTextView;
//import com.healthy.library.widget.IncreaseDecreaseView;
//import com.zhy.view.flowlayout.FlowLayout;
//import com.zhy.view.flowlayout.TagAdapter;
//import com.zhy.view.flowlayout.TagFlowLayout;
//import com.zhy.view.flowlayout.TagView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import io.reactivex.disposables.Disposable;
//
//public class DiscountListGoodsSpecDialog extends BaseDialogFragment implements ServiceGoodsSpecContract.View {
//    View dialogview = null;
//    private ImageView goodsImg;
//    private TextView goodsSpecMoney;
//    private TextView goodsCount;
//    private TextView goodsSelect;
//    private ImageView close;
//    private LinearLayout specLL;
//    private ScrollView specLLS;
//    private TextView goodCountTitle;
//    private IncreaseDecreaseView increaseDecrease;
//    public Map<String, Object> destmap = new HashMap<>();
//    public List<GoodsSpecCell> goodsSpecList = new ArrayList<>();
//    public GoodsSpecDetail goodsSpecDetail;
//    public GoodsSpecDetail goodsSpecDetailNew;
//    private TextView submitBtn, pinPeopleNum;
//    private String regimentSize;
//    private int maxActInventorySpecial = -1;
//    private TextView goodsSpecMoneyLeft;
//    private IntegralModel vipInfo;
//    private LinearLayout pointLL;
//    private TextView pointValue;
//    private TextView pointValueAdd;
//
//    public void setMaxActLimit(int maxActLimit) {
//        this.maxActLimit = maxActLimit;
//    }
//
//    public void setMinActLimit(int minActLimit) {
//        this.minActLimit = minActLimit;
//    }
//
//    private int maxActLimit = -1;
//    private int minActLimit = -1;
//    private boolean isrightbuy;
//    private double actPrice = -1;
//    private int mode = 1;//1就是左2就是右
//
//    public void setMode(int mode) {
//        this.mode = mode;
//    }
//
//    private boolean isNtReal = false;
//
//    public void setNtReal(boolean ntReal) {
//        isNtReal = ntReal;
//    }
//
//    private String marketingType = null;
//    private String mapMarketingGoodsId = null;
//
//    public void setMarketing(String marketingType, String mapMarketingGoodsId) {
//        this.marketingType = marketingType;
//        this.mapMarketingGoodsId = mapMarketingGoodsId;
//    }
//
//    public void setSingleSelectAct(boolean singleSelectAct) {
//        isSingleSelectAct = singleSelectAct;
//    }
//
//    private boolean isSingleSelectAct = false;
//
//    public void setGoodsSpecDetail(GoodsSpecDetail goodsSpecDetail) {
//        this.goodsSpecDetail = goodsSpecDetail;
//    }
//
//    public void setGoodsId(String goodsId) {
//        this.goodsId = goodsId;
//    }
//
//    public String goodsId;
//    OnSpecSubmitListener onSpecSubmitListener;
//
//    public void setOnSpecSubmitListener(OnSpecSubmitListener onSpecSubmitListener) {
//        this.onSpecSubmitListener = onSpecSubmitListener;
//    }
//
//    public void setRegimentSize(String regimentSize, boolean isrightbuy) {
//        this.regimentSize = regimentSize;
//        this.isrightbuy = isrightbuy;
//    }
//
//    ServiceGoodsSkuPresenter serviceGoodsSkuPresenter;
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        if (getContext() == null) {
//            return super.onCreateDialog(savedInstanceState);
//        }
//        dialogview = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_spec, null);
//        serviceGoodsSkuPresenter = new ServiceGoodsSkuPresenter(getContext(), this);
//        initView(dialogview);
//        buildView(dialogview);
//        Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
//        dialog.setContentView(dialogview);
//        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
//        increaseDecrease.setOnNumChangedListener(new IncreaseDecreaseView.OnNumChangedListener() {
//            @Override
//            public void onNumChanged(int num) {
//                if (checkMapSize(destmap) > 0) {
//                    buildGoodsMoney();
//                } else {
//                    buildGoodsMoney();
//                }
//            }
//        });
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(limitString)) {
//                    Toast.makeText(getActivity(), limitString, Toast.LENGTH_SHORT).show();
//                    if (goodsSpecDetailNew != null) {
//                        goodsSpecDetailNew.isErrorCount = true;//商品有问题 加入购物车的数量需要变动
//                    }
//                }
//                if (goodsSpecDetailNew == null) {
//                    Toast.makeText(getActivity(), "请选择商品规格", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (!TextUtils.isEmpty(limitString) && mode == 2) {
//                    return;
//                }
//                if (onSpecSubmitListener != null && goodsSpecDetailNew != null && goodsSpecDetailNew.getAvailableInventory() > 0) {
//                    dismiss();
//                    onSpecSubmitListener.onSpecSubmit(goodsSpecDetailNew.setFilePath(goodsSpecDetail.filePath).setGoodsTitle(goodsSpecDetailNew.goodsTitle.replace(goodsSpecDetailNew.goodsSpecStr, "")).setCount(increaseDecrease.getNum() + ""));
//                }
//            }
//        });
//
////        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
////
////
////        builder.setView(view);
////        AlertDialog dialog = builder.create();
//
//        Window window = dialog.getWindow();
//        if (window != null) {
//            window.setWindowAnimations(R.style.BottomDialogAnimation);
//            View decorView = window.getDecorView();
//            decorView.setPadding(0, 0, 0, 0);
//            decorView.setBackgroundResource(R.drawable.shape_dialog);
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.gravity = Gravity.BOTTOM;
//        }
//
//        return dialog;
//    }
//
//    private void buildView(View dialogview) {
////        goodsSpecList.clear();
////        goodsSpecList.add(new GoodsSpecCell("含量",new String[]{"210ml","200ml"}));
////        goodsSpecList.add(new GoodsSpecCell("分类",new String[]{"红色","蓝色"}));
//        destmap.clear();
//        buildGoods(true);
//        initSpec(goodsSpecList);
//
//    }
//
//    public void buildGoodsSkuSelect() {
//        if (checkMapSize(destmap) > 0) {
//            goodsSelect.setVisibility(View.VISIBLE);
//            goodsSelect.setText("已选" + getHasSku(destmap));
//        } else {
//            goodsSelect.setVisibility(View.INVISIBLE);
//        }
//        goodsCount.setVisibility(View.VISIBLE);
//    }
//
//    public void buildGoodsMoney() {
//        goodsSpecMoneyLeft.setVisibility(View.GONE);
//        pointLL.setVisibility(View.GONE);
//        goodsSpecMoney.setVisibility(View.VISIBLE);
//        if (goodsSpecDetailNew != null && goodsSpecDetailNew.platformPrice != 0 && goodsSpecDetailNew.platformPrice != 0) {
//            goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.platformPrice));
//            if ("5".equals(goodsSpecDetailNew.marketingType)) {
//                pointLL.setVisibility(View.VISIBLE);
//                pointValueAdd.setVisibility(View.GONE);
//                if(goodsSpecDetailNew.marketingPrice==0){
//                    pointValue.setText(FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.pointsPrice));
//                    goodsSpecMoney.setVisibility(View.GONE);
//                }else {
//                    pointValueAdd.setVisibility(View.VISIBLE);
//                    goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.marketingPrice));
//                    pointValue.setText(FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.pointsPrice));
//                }
//
//            }
//            if ("4".equals(goodsSpecDetailNew.marketingType)) {
//                if (isNtReal) {
//                    goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(getRealPrice(goodsSpecDetailNew.marketingPrice)));
//                }
//            } else if ("3".equals(goodsSpecDetailNew.marketingType)) {
//                goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(getRealPrice(goodsSpecDetailNew.marketingPrice)));
//            } else if ("2".equals(goodsSpecDetailNew.marketingType)) {
//                goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(getRealPrice(goodsSpecDetailNew.marketingPrice)));
//            } else if ("1".equals(goodsSpecDetailNew.marketingType)) {
//                goodsSpecMoneyLeft.setVisibility(View.VISIBLE);
//                goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(getRealPrice(goodsSpecDetailNew.marketingPrice)));
//            } else {
//
//            }
//        } else {
//            goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.platformPrice));
//        }
//        pinPeopleNum.setVisibility(View.GONE);
//        if (regimentSize != null && isrightbuy && goodsSpecDetailNew != null && "2".equals(goodsSpecDetailNew.marketingType)) {
//            pinPeopleNum.setVisibility(View.VISIBLE);
//            pinPeopleNum.setText(regimentSize + "人拼");
//        } else {
//            if (goodsSpecDetailNew != null) {
//                if ("4".equals(goodsSpecDetailNew.marketingType) && isNtReal) {
//                    pinPeopleNum.setVisibility(View.VISIBLE);
//                    pinPeopleNum.setText("新客专享");
//                } else if ("3".equals(goodsSpecDetailNew.marketingType)) {
//                    pinPeopleNum.setVisibility(View.VISIBLE);
//                    pinPeopleNum.setText("秒杀");
//                } else if ("2".equals(goodsSpecDetailNew.marketingType)) {
//
//                } else if ("1".equals(goodsSpecDetailNew.marketingType)) {
//
//                } else {
//
//                }
//            }
//
//        }
//    }
//
//    public int getRealMinLimit() {
//        if (minActLimit == -1 || minActLimit == 0) {
//            return 1;
//        } else {
//            return minActLimit;
//        }
//    }
//
//    public int getRealMaxLimit() {
//        if (maxActLimit == -1 || maxActLimit == 0) {
//            return 99999;
//        } else {
//            return minActLimit;
//        }
//    }
//
//    public int getRealCanBuy(int maxActLimit, int nowBuy, int inventory) {//判断真实可购买
//        if (maxActLimit > 0) {//有限购
//            int realcanbuy = maxActLimit - nowBuy;
//            realcanbuy = realcanbuy > 0 ? realcanbuy : 0;
//            return realcanbuy < inventory ? realcanbuy : inventory;
//        } else {
//            return inventory;
//        }
//    }
//
//    public int getRealMax(int inventory) {//判断可选的最大值 一般是按照库存来
//        if (maxActInventorySpecial != -1 && maxActInventorySpecial != 0) {
//            return maxActInventorySpecial;
//        }
//        if (maxActLimit == -1 || maxActLimit == 0 || (goodsSpecDetailNew != null && goodsSpecDetailNew.marketingType == null)) {//不限购 那就是使用库存
//            return inventory;
//        } else {
//            return maxActLimit;
//        }
//    }
//
//    public int getRealInventory(int inventory) {//控制活动库存
//        if (maxActInventorySpecial == -1 || maxActInventorySpecial == 0) {//如果控制的库存为无意义的 针对于砍价拼团来控制 则库存使用规格显示的库存
//            return inventory;
//        } else {
//            if (maxActInventorySpecial < inventory) {
//                return maxActInventorySpecial;
//            } else {
//                return inventory;
//            }
//        }
//    }
//
//    public double getRealPrice(double price) {
//        if (actPrice == -1) {
//            return price;
//        } else {
//            return actPrice;
//        }
//    }
//
//    String limitString = "";
//
//    public void buildGoods(boolean isfirst) {
//        goodsCount.setText("");
//        goodsCount.setVisibility(View.INVISIBLE);
//        submitBtn.setAlpha(0.7f);
//        pinPeopleNum.setVisibility(View.GONE);
//        if (isfirst) {
//            submitBtn.setText("确定");
//        } else {
//            submitBtn.setAlpha(1f);
//            submitBtn.setText("确定");
//        }
//        submitBtn.setEnabled(false);
//        increaseDecrease.setMaxCount(1);
//        if (checkMapSize(destmap) == goodsSpecList.size()) {//判断是不是全选
//            if (goodsSpecDetailNew != null) {
//                pinPeopleNum.setVisibility(View.VISIBLE);
//                goodsCount.setVisibility(View.VISIBLE);
//                goodsCount.setText("库存" + 0 + "件");
//                if (goodsSpecDetailNew.availableInventory > 0) {//库存大于0
//                    submitBtn.setEnabled(true);
//                    if (!isSingleSelectAct) {//非活动
//                        increaseDecrease.setMinCount(1);
//                        //System.out.println("设置最大可选" + getRealMax(goodsSpecDetailNew.availableInventory));
//                        if ("4".equals(goodsSpecDetailNew.marketingType) && !isNtReal) {//4且不是新客 库存要使用原始商品
//                            increaseDecrease.setMaxCount(getRealMax(goodsSpecDetailNew.availableInventory));//目前不支持原始库存 需要另行
//                            increaseDecrease.setNoCount(1);
//                        } else {
//                            increaseDecrease.setMaxCount(getRealMax(goodsSpecDetailNew.availableInventory));
//                            increaseDecrease.setNoCount(1);
//                        }
//
//                    } else {
//                        if (goodsSpecDetailNew.marketingType == null) {
//                            increaseDecrease.setMaxCount(goodsSpecDetailNew.availableInventory);
//                            increaseDecrease.setNoCount(1);
//                        }
//                    }
//                    submitBtn.setAlpha(1f);
//                    submitBtn.setText("确定");
//                    if("5".equals(goodsSpecDetailNew.marketingType)){
//                        submitBtn.setText("立即兑换");
//                        if(vipInfo!=null){
//                            if(Double.parseDouble(vipInfo.SYJFTOT)<goodsSpecDetailNew.pointsPrice){
//                                submitBtn.setText("积分不足");
//                                submitBtn.setAlpha(0.7f);
//                                submitBtn.setEnabled(false);
//                            }
//                        }
//                    }
//                    increaseDecrease.setLimitMaxString(null);
//                    increaseDecrease.setLimitMinString(null);
//                    goodsCount.setText("库存" + getRealInventory(goodsSpecDetailNew.availableInventory) + "件");
//                    if (goodsSpecDetailNew.marketingType == null) {
//                        goodsCount.setText("库存" + goodsSpecDetailNew.availableInventory + "件");
//                    }
//                    goodsCount.setVisibility(View.VISIBLE);
//                    limitString = "";
//                    if ("3".equals(goodsSpecDetailNew.marketingType)) {
//
//                        increaseDecrease.setMaxCount(getRealCanBuy(maxActLimit, goodsSpecDetailNew.nowOrderBuyCount, goodsSpecDetailNew.availableInventory));//目前不支持原始库存 需要另行
//                        if (minActLimit > 0) {
//                            increaseDecrease.setLimitMinString("起购" + minActLimit + "件");
//                        }
//                        increaseDecrease.setLimitMaxString("秒杀库存不足");
//
//
//                        if (getRealMinLimit() > getRealCanBuy(maxActLimit, goodsSpecDetailNew.nowOrderBuyCount, goodsSpecDetailNew.availableInventory)) {
//                            if (getRealMinLimit() > goodsSpecDetailNew.availableInventory) {
//                                limitString = "秒杀库存不足";
//                            } else if (maxActLimit - goodsSpecDetailNew.nowOrderBuyCount <= 0) {
//                                limitString = "您已购" + goodsSpecDetailNew.nowOrderBuyCount + "件" + "该秒杀商品限购" + maxActLimit + "件";
//                            } else {
//                                limitString = "起购" + minActLimit + "件，" + "您已购" + goodsSpecDetailNew.nowOrderBuyCount + "件，" + "该秒杀商品限购" + maxActLimit + "件";
//                            }
//                            increaseDecrease.setLimitMaxString(limitString);
//                        } else {//说明满足限购了
//                            if (maxActLimit > 0) {
//                                increaseDecrease.setLimitMaxString("该秒杀商品限购" + getRealCanBuy(maxActLimit, goodsSpecDetailNew.nowOrderBuyCount, goodsSpecDetailNew.availableInventory) + "件");
//                                if (maxActLimit > goodsSpecDetailNew.availableInventory) {
//                                    increaseDecrease.setLimitMaxString("秒杀库存不足");
//                                }
//                            }
//                            if (minActLimit > 0) {
//                                increaseDecrease.setLimitMinString("起购" + minActLimit + "件");
//                            }
//                        }
//
//                        increaseDecrease.setMinCount(getRealMinLimit());
//                        increaseDecrease.setNoCount(getRealMinLimit());
//
//
//                    }
//
//
//                    if (!TextUtils.isEmpty(goodsSpecDetailNew.filePath)) {
//                        try {
//                            com.healthy.library.businessutil.GlideCopy.with(getContext())
//                                    .load(goodsSpecDetailNew.filePath)
//                                    .placeholder(R.drawable.img_1_1_default2)
//                                    .error(R.drawable.img_1_1_default)
//                                    
//                                    .into(goodsImg);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        if (!TextUtils.isEmpty(goodsSpecDetail.filePath)) {
//                            try {
//                                com.healthy.library.businessutil.GlideCopy.with(getContext())
//                                        .load(goodsSpecDetail.filePath)
//                                        .placeholder(R.drawable.img_1_1_default2)
//                                        .error(R.drawable.img_1_1_default)
//                                        
//                                        .into(goodsImg);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                } else {
//                    submitBtn.setAlpha(0.7f);
//                }
//
//
//            }
//        } else {
//            goodsSpecDetailNew = null;
//            if (goodsSpecDetail != null) {
//                if (!TextUtils.isEmpty(goodsSpecDetail.filePath)) {
//                    try {
//                        com.healthy.library.businessutil.GlideCopy.with(getContext())
//                                .load(goodsSpecDetail.filePath).placeholder(R.drawable.img_1_1_default2)
//
//                                .error(R.drawable.img_1_1_default)
//                                
//                                .into(goodsImg);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        buildGoodsMoney();
//    }
//
//    public void initSpec(List<GoodsSpecCell> goodsSpecList) {
//        this.goodsSpecList = goodsSpecList;
//        goodsSpecDetailNew = null;
//        if (specLLS != null) {
//            try {
//                goodsCount.setText("");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (goodsSpecList.size() > 6) {
//                ViewGroup.LayoutParams layoutParams = specLLS.getLayoutParams();
//                layoutParams.height = (int) TransformUtil.dp2px(specLLS.getContext(), 6 * 70f);
//                specLLS.setLayoutParams(layoutParams);
//            } else {
//                ViewGroup.LayoutParams layoutParams = specLLS.getLayoutParams();
//                layoutParams.height = (int) TransformUtil.dp2px(specLLS.getContext(), goodsSpecList.size() * 70f);
//                specLLS.setLayoutParams(layoutParams);
//            }
//            specLL.removeAllViews();
//            for (int i = 0; i < goodsSpecList.size(); i++) {
//                specLL.addView(buildTagParent(specLLS.getContext(), destmap, goodsSpecList.get(i)));
//            }
//        }
//
//    }
//
//    private View buildTagParent(final Context mActivity, final Map<String, Object> destmap, final GoodsSpecCell goodsSpecCell) {
//        View tagparent = View.inflate(mActivity, R.layout.service_item_spec, null);
//        TextView textView = tagparent.findViewById(R.id.title);
//        textView.setText(goodsSpecCell.getSpecName());
//        TagFlowLayout tagFlowLayout = (TagFlowLayout) tagparent.findViewById(R.id.id_flowlayout);
//        tagFlowLayout.setAdapter(new TagAdapter<String>(goodsSpecCell.specValue) {
//
//            @Override
//            public View getView(FlowLayout parent, int position, String s) {
//                ImageTextView tag = (ImageTextView) LayoutInflater.from(mActivity).inflate(R.layout.service_item_spec_tag, parent, false);
//                tag.setText(s);
//                return tag;
//            }
//
//        });
//        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//            @Override
//            public void onSelected(Set<Integer> selectPosSet) {
//                if (selectPosSet.size() > 0) {
//
//                }
//
//            }
//        });
//        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                TagView tagView = (TagView) view;
//                if (tagView.isChecked()) {
//                    destmap.put(goodsSpecCell.id, goodsSpecCell.specValue[position]);
//                    if (checkMapSize(destmap) == goodsSpecList.size()) {//全规格选择
//                        serviceGoodsSkuPresenter.getGoodsSkuResult(new SimpleHashMapBuilder<String, Object>()
//                                .puts("mapMarketingGoodsId", mapMarketingGoodsId)
//                                .puts("marketingType", marketingType)
//                                .puts("goodsId", goodsId)
//                                .puts("specValue", destmap));
//                    }
//                } else {
//                    destmap.put(goodsSpecCell.id, "");//取消选择
//                    goodsSpecDetailNew = null;
//                    buildGoods(true);
//                }
//                buildGoodsSkuSelect();
//                return false;
//            }
//        });
//        return tagparent;
//    }
//
//    public int checkMapSize(Map<String, Object> destmap) {
//        int result = 0;
//        for (Map.Entry<String, Object> entry : destmap.entrySet()) {
//            if (!TextUtils.isEmpty(entry.getValue().toString())) {
//                result++;
//            }
//        }
//        return result;
//
//    }
//
//    public String getHasSku(Map<String, Object> destmap) {
//        String result = "";
//        for (Map.Entry<String, Object> entry : destmap.entrySet()) {
//            if (!TextUtils.isEmpty(entry.getValue().toString())) {
//                result += " \"" + entry.getValue() + "\"";
//            }
//        }
//        return result;
//
//    }
//
//    private void initView(View view) {
//        goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
//        goodsSpecMoney = (TextView) view.findViewById(R.id.goodsSpecMoney);
//        goodsCount = (TextView) view.findViewById(R.id.goodsCount);
//        goodsSelect = (TextView) view.findViewById(R.id.goodsSelect);
//        close = (ImageView) view.findViewById(R.id.close);
//        specLL = (LinearLayout) view.findViewById(R.id.specLL);
//        goodCountTitle = (TextView) view.findViewById(R.id.goodCountTitle);
//        increaseDecrease = (IncreaseDecreaseView) view.findViewById(R.id.increase_decrease);
//        specLLS = (ScrollView) view.findViewById(R.id.specLLS);
//        submitBtn = (TextView) view.findViewById(R.id.submitBtn);
//        pinPeopleNum = (TextView) view.findViewById(R.id.pinPeopleNum);
//        goodsSpecMoneyLeft = (TextView) view.findViewById(R.id.goodsSpecMoneyLeft);
//        pointLL = (LinearLayout) view.findViewById(R.id.pointLL);
//        pointValue = (TextView) view.findViewById(R.id.pointValue);
//        pointValueAdd = (TextView) view.findViewById(R.id.pointValueAdd);
//    }
//
//    public static DiscountListGoodsSpecDialog newInstance() {
//
//        Bundle args = new Bundle();
//        DiscountListGoodsSpecDialog fragment = new DiscountListGoodsSpecDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void successGetGoodsSkuResult(List<GoodsSpecDetail> result) {
//        if (result != null && result.size() > 0) {
//            goodsSpecDetailNew = result.get(0);
//            if (exSkuList != null) {
//                if (exSkuList.size() > 0 && isSingleSelectAct) {//暂时先按照isSingleSelectAct 判断 说明存在选择的池子 则需要替换数据
//                    if (checkRealSpec(goodsSpecDetailNew, exSkuList) != null && checkRealSpec(goodsSpecDetailNew, exSkuList).availableInventory == 0) {//活动库存不足了
//                        Toast.makeText(getActivity(), "活动商品已抢完，可单独购买！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (checkRealSpec(goodsSpecDetailNew, exSkuList) == null) {
//                            Toast.makeText(getActivity(), "该规格未参加活动，可直接购买！", Toast.LENGTH_SHORT).show();
//                        } else {
//                            goodsSpecDetailNew = checkRealSpec(goodsSpecDetailNew, exSkuList);
//                        }
//                    }
//                }
//            }
//            if ("3".equals(goodsSpecDetailNew.marketingType)) {
//                serviceGoodsSkuPresenter.getGoodsLimitResult(new SimpleHashMapBuilder<String, Object>()
//                        .puts("includeNoPay", "1").putObject(
//                        new GoodsSpecLimit(
//                                "1",
//                                goodsSpecDetailNew.goodsId+"",
//                                goodsSpecDetailNew.getGoodsSpec(),
//                                goodsSpecDetailNew.mapMarketingGoodsId,
//                                goodsSpecDetailNew.marketingType,
//                                goodsSpecDetailNew.getgoodsMarketingGoodsSpec(),
//                                goodsSpecDetailNew.marketingId, "2")));
//            } else {
//                buildGoods(false);
//            }
//        } else {
//            goodsSpecDetailNew = null;
//            buildGoods(false);
//        }
//    }
//
//    private GoodsSpecDetail checkRealSpec(GoodsSpecDetail goodsSpecDetailNew, List<GoodsSpecDetail> exSkuList) {
//        if (goodsSpecDetailNew != null) {
//            for (int i = 0; i < exSkuList.size(); i++) {
//                if (goodsSpecDetailNew.getGoodsChildId().equals(exSkuList.get(i).getGoodsChildId())) {
//                    return exSkuList.get(i);
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void successGetGoodsSkuFinalResult(GoodsSpecLimit result) {
//        if (result!=null) {
//            goodsSpecDetailNew.nowOrderBuyCount = result.totalQuantity;
//        } else {
//            goodsSpecDetailNew.nowOrderBuyCount = 0;
//        }
//        buildGoods(false);
//    }
//
//
//    @Override
//    public void showLoading() {
//
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
//    public void setMaxActInventorySpecial(int maxActInventorySpecial) {
//        this.maxActInventorySpecial = maxActInventorySpecial;
//    }
//
//    public int getMaxActInventorySpecial() {
//        return maxActInventorySpecial;
//    }
//
//    public void setActPrice(double actPrice) {
//        this.actPrice = actPrice;
//    }
//
//    public double getActPrice() {
//        return actPrice;
//    }
//
//    private List<GoodsSpecDetail> exSkuList;
//
//    public void setSkuListEx(List<GoodsSpecDetail> list) {
//        for (int i = 0; i < list.size(); i++) {
//            //System.out.println("活动类型:" + list.get(i).marketingType);
//        }
//        exSkuList = list;
//    }
//
//    public void setVipInfo(IntegralModel vipInfo) {
//        this.vipInfo = vipInfo;
//    }
//
//    public IntegralModel getVipInfo() {
//        return vipInfo;
//    }
//
//    public interface OnSpecSubmitListener {
//        void onSpecSubmit(GoodsSpecDetail goodsSpecDetail);
//    }
//}
