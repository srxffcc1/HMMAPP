package com.healthy.library.business;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.healthy.library.contract.ShopContract;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.model.PopListInfo;
import com.healthy.library.presenter.ShopPresenterCopy;
import com.healthy.library.utils.FormatUtils;
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

import io.reactivex.rxjava3.disposables.Disposable;


public class DisGoodsSpecDialog extends BaseDialogFragment implements ShopContract.View {
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
    public List<PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean> goodsSpecList = new ArrayList<>();
    public PopDetailInfo.GoodsDTOListBean goodsDTOListBean;
    private PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean goodsSpecCell;
    private TextView submitBtn, pinPeopleNum;
    private TextView goodsSpecMoneyLeft;
    private LinearLayout pointLL;
    private TextView pointValue;
    private TextView pointValueAdd;
    ShopPresenterCopy shopPresenterCopy;


    private PopListInfo popListInfo = null;

    public void setPopListInfo(PopListInfo popListInfo) {
        this.popListInfo = popListInfo;
    }

    public void setSingleSelectAct(boolean singleSelectAct) {
        isSingleSelectAct = singleSelectAct;
    }

    private boolean isSingleSelectAct = false;

    public void setGoodsDetail(PopDetailInfo.GoodsDTOListBean goodsDTOListBean) {
        this.goodsDTOListBean = goodsDTOListBean;
    }

    OnSpecSubmitListener onSpecSubmitListener;

    public void setOnSpecSubmitListener(OnSpecSubmitListener onSpecSubmitListener) {
        this.onSpecSubmitListener = onSpecSubmitListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        dialogview = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_spec, null);
        initView(dialogview);
        buildView(dialogview);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        increaseDecrease.setOnNumChangedListener(new IncreaseDecreaseView.OnNumChangedListener() {
            @Override
            public void onNumChanged(int num) {
                if (checkMapSize(destmap) > 0) {
                    buildGoodsMoney();
                } else {
                    buildGoodsMoney();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        shopPresenterCopy = new ShopPresenterCopy(getActivity(), this);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsSpecCell == null) {
                    Toast.makeText(getActivity(), "请选择商品规格", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onSpecSubmitListener != null && goodsSpecCell != null && goodsSpecCell.getAvailableInventory() > 0) {
                    dismiss();
                    onSpecSubmitListener.onSpecSubmit(goodsSpecCell.setCount(increaseDecrease.getNum() + ""));
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
            goodsSelect.setText("已选" + getHasSku(destmap));
        } else {
            goodsSelect.setVisibility(View.INVISIBLE);
        }
        goodsCount.setVisibility(View.VISIBLE);
    }

    public void buildGoodsMoney() {
        goodsSpecMoneyLeft.setVisibility(View.GONE);
        pointLL.setVisibility(View.GONE);
        goodsSpecMoney.setVisibility(View.VISIBLE);
        if (goodsSpecCell != null && goodsSpecCell.platformPrice != 0) {
            goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecCell.platformPrice));
        } else {
            if (goodsDTOListBean.platformPrice != 0) {
                goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsDTOListBean.platformPrice));
            } else {
                goodsSpecMoney.setText("¥" + 0);
            }
        }
        pinPeopleNum.setVisibility(View.GONE);
        if (popListInfo != null) {
            if (popListInfo.PopLabelName != null && !TextUtils.isEmpty(popListInfo.PopLabelName)) {
                pinPeopleNum.setVisibility(View.VISIBLE);
                pinPeopleNum.setText(popListInfo.PopLabelName);
            } else {
                pinPeopleNum.setVisibility(View.GONE);
                pinPeopleNum.setText("");
            }
        }
    }

    public void buildGoods(boolean isfirst) {
        goodsCount.setText("");
        goodsCount.setVisibility(View.INVISIBLE);
        submitBtn.setAlpha(0.7f);
        pinPeopleNum.setVisibility(View.GONE);
        if (isfirst) {
            submitBtn.setText("确定");
        } else {
            submitBtn.setAlpha(1f);
            submitBtn.setText("确定");
        }
//        submitBtn.setEnabled(false);
        increaseDecrease.setMaxCount(1);
        if (checkMapSize(destmap) > 0) {//判断是不是全选
            if (goodsSpecCell != null) {
                pinPeopleNum.setVisibility(View.VISIBLE);
                goodsCount.setVisibility(View.VISIBLE);
                goodsCount.setText("库存" + 0 + "件");
                if (goodsSpecCell.getAvailableInventory() > 0) {//库存大于0
//                    submitBtn.setEnabled(true);
                    if (isSingleSelectAct) {
                        increaseDecrease.setMinCount(1);
                        increaseDecrease.setMaxCount(goodsSpecCell.getAvailableInventory());
                        increaseDecrease.setNoCount(1);
                    } else {
                        increaseDecrease.setMaxCount(goodsSpecCell.getAvailableInventory());
                        increaseDecrease.setNoCount(1);
                    }
                    submitBtn.setAlpha(1f);
                    submitBtn.setText("确定");
                    increaseDecrease.setLimitMaxString(null);
                    increaseDecrease.setLimitMinString(null);
                    goodsCount.setText("库存" + goodsSpecCell.getAvailableInventory() + "件");
                    goodsCount.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(goodsDTOListBean.headImage)) {
                        try {
                            com.healthy.library.businessutil.GlideCopy.with(getContext())
                                    .load(goodsDTOListBean.headImage)
                                    .placeholder(R.drawable.img_1_1_default2)
                                    .error(R.drawable.img_1_1_default)

                                    .into(goodsImg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    submitBtn.setAlpha(0.7f);
                }
            }
        } else {
            if (goodsDTOListBean != null) {
                if (!TextUtils.isEmpty(goodsDTOListBean.headImage)) {
                    try {
                        com.healthy.library.businessutil.GlideCopy.with(getContext())
                                .load(goodsDTOListBean.headImage).placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.img_1_1_default)

                                .into(goodsImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        buildGoodsMoney();
    }

    public void initSpec(List<PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean> goodsSpecList) {
        this.goodsSpecList = goodsSpecList;
        if (specLLS != null) {
            try {
                goodsSpecCell=null;
                goodsCount.setText("");
                goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecList.get(0).platformPrice));
            } catch (Exception e) {
                e.printStackTrace();
            }
            specLL.removeAllViews();
            specLL.addView(buildTagParent(specLLS.getContext(), destmap, goodsSpecList));
        }

    }

    private View buildTagParent(final Context mActivity, final Map<String, Object> destmap, final List<PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean> goodsSpecList) {
        View tagparent = View.inflate(mActivity, R.layout.service_item_spec, null);
        TextView textView = tagparent.findViewById(R.id.title);
        textView.setText("活动规格");
        TagFlowLayout tagFlowLayout = (TagFlowLayout) tagparent.findViewById(R.id.id_flowlayout);
        tagFlowLayout.setMaxSelectCount(1);
        tagFlowLayout.setAdapter(new TagAdapter<PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean>(goodsSpecList) {

            @Override
            public View getView(FlowLayout parent, int position, PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean disGoodsSpecCell) {
                ImageTextView tag = (ImageTextView) LayoutInflater.from(mActivity).inflate(R.layout.service_item_spec_tag, parent, false);
                tag.setText(disGoodsSpecCell.getGoodsSpecStr());
                return tag;
            }

        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                TagView tagView = (TagView) view;
                destmap.clear();
                goodsSpecCell = goodsSpecList.get(position);
                if (tagView.isChecked()) {
                    destmap.put(goodsSpecList.get(position).goodsChildId, goodsSpecList.get(position).getGoodsSpecStr());
                    if (checkMapSize(destmap) > 0) {//全规格选择
                        buildGoods(true);
                    }
                } else {
                    buildGoods(true);
                }
                buildGoodsSkuSelect();
                return false;
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

    public static DisGoodsSpecDialog newInstance() {

        Bundle args = new Bundle();
        DisGoodsSpecDialog fragment = new DisGoodsSpecDialog();
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
    public void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel) {

    }

    public interface OnSpecSubmitListener {
        void onSpecSubmit(PopDetailInfo.GoodsDTOListBean.GoodsChildrenBean goodsSpecCell);
    }
}
