package com.health.city.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.city.R;
import com.healthy.library.model.Fans;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.widget.CornerImageView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class FansCardAdapter extends BaseQuickAdapter<Fans, BaseViewHolder> {



    public void setOnFansClickListener(OnFansClickListener onFansClickListener) {
        this.onFansClickListener = onFansClickListener;
    }

    private OnFansClickListener onFansClickListener;

    public FansCardAdapter() {
        this(R.layout.city_item_fragment_nofollow_card);
    }

    private FansCardAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Fans item) {
        CornerImageView ivHeader;
        TextView name;
        TextView status;
        final TextView toFollow;
        View nameandstatus;
         TextView fansCount;
         TextView likeCount;
        fansCount = (TextView) helper.getView(R.id.fansCount);
        likeCount = (TextView) helper.getView(R.id.likeCount);
        ivHeader = (CornerImageView) helper.getView(R.id.ivHeader);
        name = (TextView) helper.getView(R.id.name);
        status = (TextView) helper.getView(R.id.status);
        toFollow = (TextView) helper.getView(R.id.toFollow);

        nameandstatus = (View) helper.getView(R.id.nameandstatus);

        fansCount.setText("粉丝"+item.friendNum);

        likeCount.setText("点赞"+item.praiseNum);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_FANDETAIL)
                        .withString("memberId", item.friendId + "")
                        .withString("searchMemberType", item.friendType + "")
                        .navigation();
            }
        });

//        if(item.currentStatus!=null){
//            if(item.currentStatus.contains("备孕")){//备孕
//                status.setBackgroundResource(R.drawable.shape_city_nofollow_red);
//            }else if(item.currentStatus.contains("宝")||item.currentStatus.contains("产后")){//宝宝出身
//                status.setBackgroundResource(R.drawable.shape_city_nofollow_yellow);
//            }else {//孕
//                status.setBackgroundResource(R.drawable.shape_city_nofollow_green);
//            }
//        }
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(item.faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)

                .into(ivHeader);
        name.setText(item.nickName);
        status.setText(item.currentStatus);
//        if (TextUtils.isEmpty(item.currentStatus)) {
//            status.setVisibility(View.GONE);
//        } else {
//            status.setVisibility(View.VISIBLE);
//        }
        if (item.focusStatus == 0) {
            toFollow.setText("关注");
            toFollow.setTextColor(Color.parseColor("#ff29bda9"));
            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
        } else {
            toFollow.setText("已关注");
            toFollow.setTextColor(Color.parseColor("#9596A4"));
            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_gray);
        }
//        if (item.friendId != null && item.friendId.equals(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {
//
//            toFollow.setVisibility(View.GONE);
//        } else {
//            toFollow.setVisibility(View.VISIBLE);
//        }
        toFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onFansClickListener != null) {


                    if (item.focusStatus != 0) {
                        StyledDialog.init(mContext);
                        StyledDialog.buildIosAlert("", "确认取消关注吗?", new MyDialogListener() {
                            @Override
                            public void onFirst() {

                            }

                            @Override
                            public void onThird() {
                                super.onThird();
                            }

                            @Override
                            public void onSecond() {
                                onFansClickListener.click(view, item.friendId, item.focusStatus == 0 ? "0" : "1", item.friendType + "");
                                item.focusStatus = item.focusStatus == 0 ? 1 : 0;
                                if (item.focusStatus == 0) {
                                    toFollow.setText("关注");
                                    toFollow.setTextColor(Color.parseColor("#ff29bda9"));
                                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
                                } else {
                                    toFollow.setText("已关注");
                                    toFollow.setTextColor(Color.parseColor("#9596A4"));
                                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_gray);
                                }


                            }
                        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
                    } else {
                        onFansClickListener.click(view, item.friendId, item.focusStatus == 0 ? "0" : "1", item.friendType + "");
                        item.focusStatus = item.focusStatus == 0 ? 1 : 0;
                        if (item.focusStatus == 0) {
                            toFollow.setText("关注");
                            toFollow.setTextColor(Color.parseColor("#ff29bda9"));
                            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
                        } else {
                            toFollow.setText("已关注");
                            toFollow.setTextColor(Color.parseColor("#9596A4"));
                            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_gray);
                        }
                    }


                }
            }
        });

    }

    private void initView() {

    }

    public interface OnFansClickListener {
        void click(View view, String friendId, String type, String frtype);
    }
}
