package com.health.servicecenter.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.business.GoodsSpecDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.dialog.SelectTechnicianDateDialog;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 服务预约项目详情规格弹框
 *
 * @author: long
 * @date: 2021/4/5
 */
public class AttributeSelectDataDialog extends BaseDialogFragment {

    private AlertDialog mAlertDialog;
    private View mView;
    private TextView goodsSpecMoney;
    private CornerImageView goodsImg;
    private TagFlowLayout tagFlowLayout;
    private TagAdapter<AppointmentMainItemModel.AttributeList> mTagAdapter;
    private TextView goodsSelect;
    private AppointmentMainItemModel.AttributeList attribute;
    private String price = "";
    private List<AppointmentMainItemModel.AttributeList> attributeList;
    private boolean isClicked;
    private int mUnChecked;


    public static AttributeSelectDataDialog newInstance(AppointmentMainItemModel itemModel) {
        Bundle args = new Bundle();
        AttributeSelectDataDialog fragment = new AttributeSelectDataDialog();
        args.putSerializable("itemModel", itemModel);
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //tagFlowLayout.onChanged();
            //goodsSelect.setText("");
            //price = FormatUtils.moneyKeep2Decimals(attributeList.get(0).getPrice());
            //goodsSpecMoney.setText("¥" + price);
            //attribute = null;
            isClicked = false;
            AttributeSelectDataDialog.this.dismiss();
            /*if (mOnTagClickListener != null) {
                mOnTagClickListener.onTagClick(attribute, true);
            }*/
        }
    };

    private View.OnClickListener submitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isClicked = true;
            AttributeSelectDataDialog.this.dismiss();
            /*if (mOnTagClickListener != null) {
                mOnTagClickListener.onTagClick(attribute, true);
            }*/
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AppointmentMainItemModel itemModel;
        if (mAlertDialog == null && getContext() != null) {
            if (getArguments() != null) {
                itemModel = (AppointmentMainItemModel) getArguments().getSerializable("itemModel");
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
            mView = LayoutInflater.from(getContext()).inflate(R.layout.attribute_select_dialog, null);
            mView.findViewById(R.id.close).setOnClickListener(cancelClick);
            mView.findViewById(R.id.submitBtn).setOnClickListener(submitClick);

            goodsSelect = mView.findViewById(R.id.goodsSelect);
            goodsSpecMoney = mView.findViewById(R.id.goodsSpecMoney);
            goodsImg = mView.findViewById(R.id.goodsImg);
            tagFlowLayout = (TagFlowLayout) mView.findViewById(com.healthy.library.R.id.id_flowlayout);

            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(mView)
                    .create();

            mAlertDialog.setCancelable(true);
            mAlertDialog.setCanceledOnTouchOutside(true);
            Window window = mAlertDialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(com.healthy.library.R.style.BottomDialogAnimation);
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                decorView.setBackgroundResource(com.healthy.library.R.drawable.shape_dialog);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.BOTTOM;
            }
            setData(itemModel);
        }

        return mAlertDialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnTagClickListener != null) {
            mOnTagClickListener.onTagClick(attribute, isClicked);
        }
    }

    private void setData(final AppointmentMainItemModel itemModel) {
        if (mView != null) {
            goodsSpecMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(itemModel.getPrice()));
            List<String> imgList = itemModel.getImgList();
            String url = "";
            if (!ListUtil.isEmpty(imgList)) {
                url = imgList.get(0);
            }
            com.healthy.library.businessutil.GlideCopy.with(mView.getContext())
                    .load(url)
                    .error(R.drawable.img_1_1_default2)
                    .placeholder(R.drawable.img_1_1_default2)
                    .into(goodsImg);

            attributeList = itemModel.getAttributeList();

            if (!ListUtil.isEmpty(attributeList)) {
                mUnChecked = attributeList.get(0).getId();
                goodsSelect.setText("已选“" + attributeList.get(0).getName() + "”");
                price = FormatUtils.moneyKeep2Decimals(attributeList.get(0).getPrice());
                goodsSpecMoney.setText("¥" + price);
                if (mTagAdapter == null) {
                    mTagAdapter = new TagAdapter<AppointmentMainItemModel.AttributeList>(attributeList) {
                        @Override
                        public View getView(FlowLayout parent, int position, AppointmentMainItemModel.AttributeList attributeList) {
                            ImageTextView tag = (ImageTextView) LayoutInflater.from(getContext()).inflate(com.healthy.library.R.layout.service_item_spec_tag, parent, false);
                            tag.setText(attributeList.getName());
                            return tag;
                        }

                        @Override
                        public void onSelected(int position, View view) {
                            super.onSelected(position, view);
                        }
                    };

                    tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            if (!view.isPressed()) {
                                return false;
                            }
                            String selectName = "";
                            TagView tagView = (TagView) view;
                            if (tagView.isChecked()) {
                                attribute = attributeList.get(position);
                                mUnChecked = attribute.getId();
                                price = FormatUtils.moneyKeep2Decimals(attribute.getPrice());
                                selectName = "已选“" + attributeList.get(position).getName() + "”";
                            } else {
                                attribute = null;
                                mUnChecked = attributeList.get(0).getId();
                                price = FormatUtils.moneyKeep2Decimals(attributeList.get(0).getPrice());
                                selectName = "已选“" + attributeList.get(0).getName() + "”";
                            }
                            goodsSelect.setText(selectName);
                            goodsSpecMoney.setText("¥" + price);
                            return false;
                        }
                    });
                }
                tagFlowLayout.setAdapter(mTagAdapter);
                setChecked();
                ////System.out.println("规则弹框 执行setDate方法");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setChecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        isClicked = false;
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2021-04-24 Long 处理默认选中
     */
    private void setChecked() {
        if (!ListUtil.isEmpty(attributeList) && mTagAdapter != null) {
            for (int i = 0; i < attributeList.size(); i++) {
                if (mUnChecked == attributeList.get(i).getId()) {
                    mTagAdapter.setSelectedList(i);
                    attribute = attributeList.get(i);
                }
            }
        }
    }

    private OnTagClickListener mOnTagClickListener;

    public void setOnTagClickListener(OnTagClickListener mOnTagClickListener) {
        this.mOnTagClickListener = mOnTagClickListener;
    }

    public interface OnTagClickListener {
        /**
         * 规格选择回调
         *
         * @param attribute     当前选择的规格model
         * @param isSubmitClick 是否确认点击
         */
        void onTagClick(AppointmentMainItemModel.AttributeList attribute, boolean isSubmitClick);
    }

}
