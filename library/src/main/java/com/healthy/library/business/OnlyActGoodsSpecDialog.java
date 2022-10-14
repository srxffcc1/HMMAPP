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
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.IncreaseDecreaseView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.util.List;
import java.util.Set;

//不进行任何请求的活动spec
public class OnlyActGoodsSpecDialog extends BaseDialogFragment {
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
    public GoodsSpecDetail goodsSpecDetail;
    public GoodsSpecDetail goodsSpecDetailNew;
    private TextView submitBtn, pinPeopleNum;
    private TextView goodsSpecMoneyLeft;
    private LinearLayout pointLL;
    private TextView pointValue;
    private TextView pointValueAdd;


    public void setMarketingType(String marketingType) {
        this.marketingType = marketingType;
    }

    public String marketingType="-1";
    public String selectSku;

    public OnlyActGoodsSpecDialog setRegimentSize(int regimentSize) {
        this.regimentSize = regimentSize;
        return this;
    }

    private int regimentSize;//为了显示几人拼的标签

    OnSpecSubmitListener onSpecSubmitListener;
    private List<GoodsDetail.GoodsChildren> goodsSpecList;
    public void setOnSpecSubmitListener(OnSpecSubmitListener onSpecSubmitListener) {
        this.onSpecSubmitListener = onSpecSubmitListener;
    }
    public void setGoodsSpecDetail(GoodsSpecDetail goodsSpecDetail) {//当什么都没有选择时显示的默认值
        this.goodsSpecDetail = goodsSpecDetail;
    }
    public OnlyActGoodsSpecDialog setSelectSku(String selectSku) {
        this.selectSku = selectSku;
        return this;
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
                        goodsSpecDetailNew.isErrorCount = true;//商品有问题 加入购物车的数量需要变动
                    }
                }
                if (goodsSpecDetailNew == null) {
                    Toast.makeText(getActivity(), "请选择商品规格", Toast.LENGTH_SHORT).show();
                    return;
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
        buildGoods(true);
        initSpec(goodsSpecList);

    }

    public void buildGoodsSkuSelect() {
        if (!TextUtils.isEmpty(selectSku)) {
            goodsSelect.setVisibility(View.VISIBLE);
            goodsSelect.setText("已选" + selectSku);
        } else {
            goodsSelect.setVisibility(View.INVISIBLE);
        }
        goodsCount.setVisibility(View.VISIBLE);
    }

    public void buildGoodsMoney() {
        //重置标签
        goodsSpecMoneyLeft.setVisibility(View.GONE);
        pointLL.setVisibility(View.GONE);
        goodsSpecMoney.setVisibility(View.VISIBLE);
        pinPeopleNum.setVisibility(View.GONE);
        pointValueAdd.setVisibility(View.GONE);
        pointValue.setVisibility(View.GONE);
        pointLL.setVisibility(View.GONE);
        if (goodsSpecDetailNew != null) {//说明有选的规格
//            goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.getPlatformPrice()));
            if("1".equals(goodsSpecDetailNew.getMarketingType())){

            }else if("2".equals(goodsSpecDetailNew.getMarketingType())){
                pinPeopleNum.setVisibility(View.VISIBLE);
                pinPeopleNum.setText(regimentSize + "人拼");
                goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.getMarketingPrice()));
            }else {
                goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetailNew.getLivePrice()));
            }

        }else {//没有选择规格则展示默认的那个价格
//            goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getPlatformPrice()));
            goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSpecDetail.getLivePrice()));
        }
    }

    String limitString = "";

    public void buildGoods(boolean isfirst) {
        goodsCount.setText("");
        goodsCount.setVisibility(View.INVISIBLE);
        submitBtn.setAlpha(0.7f);
        if (isfirst) {
            submitBtn.setText("确定");
        } else {
            submitBtn.setAlpha(1f);
            submitBtn.setText("确定");
        }
        //重置下选择器
        increaseDecrease.setMaxCount(1);
        increaseDecrease.setMinCount(1);
        {//判断规格是不是选正确了
            if (goodsSpecDetailNew != null) {
                goodsSpecDetailNew.setParent(goodsSpecDetail);
                goodsCount.setVisibility(View.VISIBLE);
                goodsCount.setText("库存" + goodsSpecDetailNew.getAvailableInventory() + "件");
                increaseDecrease.reset();
                System.out.println("设置最大最小起购");
                increaseDecrease.setMaxCount(goodsSpecDetailNew.getMarkLimitMax());
                increaseDecrease.setMinCount(goodsSpecDetailNew.getMarkLimitMin());

                if ("2".equals(goodsSpecDetail.getMarketingType())) {
                    increaseDecrease.setMaxCount(1);
                    increaseDecrease.setMinCount(1);
                }
                if(increaseDecrease.getNum()>goodsSpecDetailNew.getMarkLimitMax()){
                    increaseDecrease.setNoCount(goodsSpecDetailNew.getMarkLimitMax());
                }
                if(increaseDecrease.getNum()<goodsSpecDetailNew.getMarkLimitMin()){
                    increaseDecrease.setNoCount(goodsSpecDetailNew.getMarkLimitMin());
                }
                limitString = "";

                if ("3".equals(marketingType)) {

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

    public void initSpec(List<GoodsDetail.GoodsChildren> goodsSpecList) {
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
            specLL.addView(buildTagParent(specLLS.getContext(), goodsSpecList));
        }
    }

    private View buildTagParent(final Context mActivity, final List<GoodsDetail.GoodsChildren> goodsSpecList) {
        View tagparent = View.inflate(mActivity, R.layout.service_item_spec, null);
        TextView textView = tagparent.findViewById(R.id.title);
        textView.setVisibility(View.GONE);
        TagFlowLayout tagFlowLayout = (TagFlowLayout) tagparent.findViewById(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter<String>(new SimpleArrayListBuilder<String>().putList(goodsSpecList, new ObjectIteraor<GoodsDetail.GoodsChildren>() {
            @Override
            public String getDesObj(GoodsDetail.GoodsChildren o) {
                if(goodsSpecDetail.goodsTitle!=null){
                    String result=o.getGoodsSpecStr().replace(goodsSpecDetail.goodsTitle,"");
                    if(TextUtils.isEmpty(result)){
                        return o.getGoodsSpecStr();
                    }
                    return result;
                }
                return o.getGoodsSpecStr();
            }
        }))
        {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageTextView tag = (ImageTextView) LayoutInflater.from(mActivity).inflate(R.layout.service_item_spec_tag, parent, false);
                tag.setText(s);
                if (!TextUtils.isEmpty(selectSku)) {
                    if (selectSku.contains(s)) {
                    }
                }
                return tag;
            }
        };
        tagFlowLayout.setAdapter(tagAdapter);
        for (int i = 0; i < goodsSpecList.size(); i++) {
            if (selectSku != null && selectSku.contains(goodsSpecList.get(i).getGoodsSpecStr())) {
                tagAdapter.setSelectedList(i);
                selectSku=goodsSpecList.get(i).getGoodsSpecStr();
                goodsSpecDetailNew=new GoodsSpecDetail(goodsSpecList.get(i),marketingType);
                buildGoods(false);
            }
        }
        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet.size() > 0) {

                }

            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (!view.isPressed()) {
                    return false;
                }
                TagView tagView = (TagView) view;
                if (tagView.isChecked()) {
                    selectSku=goodsSpecList.get(position).getGoodsSpecStr();
                    goodsSpecDetailNew=new GoodsSpecDetail(goodsSpecList.get(position),marketingType);
                    buildGoods(false);
                } else {
                    goodsSpecDetailNew = null;
                    buildGoods(true);
                }
                buildGoodsSkuSelect();
                return false;
            }
        });
        return tagparent;
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

    public static OnlyActGoodsSpecDialog newInstance() {
        Bundle args = new Bundle();
        OnlyActGoodsSpecDialog fragment = new OnlyActGoodsSpecDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnSpecSubmitListener {
        void onSpecSubmit(GoodsSpecDetail goodsSpecDetail);
    }
}
