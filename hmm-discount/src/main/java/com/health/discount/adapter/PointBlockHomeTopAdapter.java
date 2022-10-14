package com.health.discount.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.health.discount.R;
import com.health.discount.model.PointOption;
import com.health.discount.model.UserPoint;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PointBlockHomeTopAdapter extends BaseAdapter<IntegralModel> {


    private List<PointOption> optionList;
    private UserPoint userInfo;

    public PointBlockHomeTopAdapter() {
        this(R.layout.dis_item_activity_point_top);
    }

    public PointBlockHomeTopAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        ConstraintLayout userTopLL;
        CornerImageView userIcon;
        TextView userName;
        ImageTextView userPonitLeft;
        TextView userPonit;
        GridLayout fucLL1;
        TextView pointDetail;
        View fucLLZ = holder.itemView.findViewById(R.id.fucLLZ);
        userTopLL = (ConstraintLayout) holder.itemView.findViewById(R.id.userTopLL);
        userIcon = (CornerImageView) holder.itemView.findViewById(R.id.userIcon);
        userName = (TextView) holder.itemView.findViewById(R.id.userName);
        userPonitLeft = (ImageTextView) holder.itemView.findViewById(R.id.userPonitLeft);
        userPonit = (TextView) holder.itemView.findViewById(R.id.userPonit);
        fucLL1 = (GridLayout) holder.itemView.findViewById(R.id.fucLL1);
        pointDetail = (TextView) holder.itemView.findViewById(R.id.pointDetail);
        IntegralModel integralModel = getModel();
        if (userInfo != null) {
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(userInfo.getFaceUrl())
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default2)
                    .into(userIcon);
            userName.setText(userInfo.getNickname());
        }
        pointDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("兑换明细", "");
                }
            }
        });
        userPonit.setText("0");
        if (integralModel != null) {
            userPonit.setText(integralModel.SYJFTOT);
        }
        fucLLZ.setVisibility(View.GONE);
        if (optionList != null && optionList.size() > 0) {
            fucLLZ.setVisibility(View.VISIBLE);
        }
        addImages(context, fucLL1, optionList);
    }

    private int mMargin;

    private void addImages(final Context context, final GridLayout gridLayout, final List<PointOption> urls) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(context, 2);
        }
        gridLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                gridLayout.removeAllViews();
                if (urls != null && urls.size() > 0) {
                    int column = 6;
                    gridLayout.setColumnCount(column);
                    //gridLayout.setRowCount(row);
                    //System.out.println("选项卡列表:" + gridLayout.getWidth());
                    int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (column - 1)) * mMargin) / column;
                    for (int i = 0; i < urls.size(); i++) {
                        final PointOption pointOption = urls.get(i);

                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = w;
                        //System.out.println("选项卡列表:" + params.width);
                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
                        View imageparent = LayoutInflater.from(context).inflate(R.layout.dis_function_point, gridLayout, false);
                        TextView tvCategory;
                        TextView hotTip;
                        Space space;
                        ImageView ivCategory;
                        ConstraintLayout parentCategory;

                        parentCategory = (ConstraintLayout) imageparent.findViewById(R.id.parent_category);
                        ivCategory = (ImageView) imageparent.findViewById(R.id.iv_category);
                        space = (Space) imageparent.findViewById(R.id.space);
                        hotTip = (TextView) imageparent.findViewById(R.id.hotTip);
                        tvCategory = (TextView) imageparent.findViewById(R.id.tv_category);
                        if(pointOption.iconUrl.contains(".gif")){
                            Glide.with(context)
                                    .asGif()
                                    .apply(RequestOptions.circleCropTransform())
                                    .load(pointOption.iconUrl)
                                    .into(ivCategory);
                        } else {
                            com.healthy.library.businessutil.GlideCopy.with(context)
                                    .load(pointOption.iconUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .error(R.drawable.img_1_1_default)
                                    .placeholder(R.drawable.img_1_1_default2)
                                    .into(ivCategory);
                        }
                        tvCategory.setText(pointOption.navName);
                        imageparent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (moutClickListener != null) {
                                    if ("我能兑换".equals(pointOption.navName) || "积分抽奖".equals(pointOption.navName)) {
                                        moutClickListener.outClick(pointOption.navName, "");
                                    } else {
                                        moutClickListener.outClick("专区列表", new String[]{pointOption.themeId, pointOption.navName});
                                    }
                                }
                            }
                        });
                        if (!TextUtils.isEmpty(pointOption.themeId) || "我能兑换".equals(pointOption.navName) || "积分抽奖".equals(pointOption.navName)) {
                            gridLayout.addView(imageparent, params);
                        }
                    }
                }
            }
        }, 100);

    }

    public void setOptionList(List<PointOption> optionList) {
        this.optionList = optionList;
    }

    public List<PointOption> getOptionList() {
        return optionList;
    }

    public void setUserInfo(UserPoint userInfo) {
        this.userInfo = userInfo;
    }

    public UserPoint getUserInfo() {
        return userInfo;
    }
}
