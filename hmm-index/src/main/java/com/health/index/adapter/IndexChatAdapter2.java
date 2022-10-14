//package com.health.index.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.media.MediaMetadataRetriever;
//import android.text.Html;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.style.AbsoluteSizeSpan;
//import android.text.style.StyleSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.google.android.flexbox.FlexboxLayout;
//import com.health.index.R;
//import com.health.index.model.IndexAllChat2;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.base.BaseMultiItemAdapter;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.model.PostActivityList;
//import com.healthy.library.routes.CityRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.utils.BitmapUtils;
//import com.healthy.library.utils.DateUtils;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.JsoupCopy;
//import com.healthy.library.utils.MediaFileUtil;
//import com.healthy.library.utils.SpUtils;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.CollapsedTextView;
//import com.healthy.library.widget.CornerFitCenterImageView;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.ImageSpanCentre;
//import com.healthy.library.widget.ImageTextView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class IndexChatAdapter2 extends BaseMultiItemAdapter<IndexAllChat2> {
//    OnChatLikeClickListener onChatLikeClickListener;
//    OnChatShareClickListener onChatShareClickListener;
//    public Map<String, Bitmap> bitmapList = new HashMap<>();
//
//    public void setOnChatLikeClickListener(OnChatLikeClickListener onChatLikeClickListener) {
//        this.onChatLikeClickListener = onChatLikeClickListener;
//    }
//
//    public void setOnChatShareClickListener(OnChatShareClickListener onChatShareClickListener) {
//        this.onChatShareClickListener = onChatShareClickListener;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return getDefItemViewType(position);
//    }
//
//    public IndexChatAdapter2() {
//        this(R.layout.city_item_fragment_follow);
//        addItemType(1, R.layout.city_item_fragment_follow);//1:正常帖子
//        addItemType(2, R.layout.city_item_fragment_ad);//2:新品首发 3:种草清单 4:图文搭配 5:视频搭配 6:会员专享券
//    }
//
//    private IndexChatAdapter2(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        return linearLayoutHelper;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder helper, int position) {
//        final IndexAllChat2 item = getDatas().get(position);
//        if (getDefItemViewType(position) == 1) {
//            buildDefaultPost(helper, item);
//        } else if (getDefItemViewType(position) == 2) {
//            buildAdPost(helper, item, position);
//        }
//    }
//
//    private void buildAdPost(BaseHolder holder, final IndexAllChat2 postDetail, final int position) {
//        LinearLayout grassHead = (LinearLayout) holder.getView(R.id.grass_head);
//        CornerImageView ivHeader = (CornerImageView) holder.getView(R.id.ivHeader);
//        TextView name = (TextView) holder.getView(R.id.name);
//        TextView days = (TextView) holder.getView(R.id.days);
//        ImageView submit = (ImageView) holder.getView(R.id.submit);
//        TextView mark = (TextView) holder.getView(R.id.mark);
//        LinearLayout followChild = (LinearLayout) holder.getView(R.id.follow_child);
//        View heightView = (View) holder.getView(R.id.heightView);
//        CollapsedTextView tipContent = (CollapsedTextView) holder.getView(R.id.tipContent);
//        GridLayout gridLayout = (GridLayout) holder.getView(R.id.see_imgs);
//        View dividerHeader = (View) holder.getView(R.id.divider_header);
//        if (postDetail.accountNickname != null && !TextUtils.isEmpty(postDetail.accountNickname)) {
//            name.setText(postDetail.accountNickname);
//        } else {
//            name.setText("");
//        }
//        tipContent.setIsExpanded(false);//设置为不展开
//        com.healthy.library.businessutil.GlideCopy.with(context).asBitmap().load(postDetail.createUserFaceUrl)
//                .placeholder(R.drawable.img_1_1_default2).error(R.drawable.img_avatar_default)
//                .into(ivHeader);
//        days.setText(postDetail.createTimeStr);
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (moutClickListener != null) {
//                    moutClickListener.outClick("submit", postDetail.id);
//                }
//            }
//        });
//        try {
//            if (postDetail.postingContent != null) {
//                String content = "  " + StringUtils.noEndLnString(postDetail.postingContent);//加个空格 后面代替图标位置
//                Drawable drawable = null;
//                if (postDetail.postingType == 2) {
//                    drawable = context.getResources().getDrawable(R.drawable.post_new_icon);
//                } else if (postDetail.postingType == 3) {
//                    drawable = context.getResources().getDrawable(R.drawable.post_grass_icon);
//                } else if (postDetail.postingType == 4) {
//                    drawable = context.getResources().getDrawable(R.drawable.post_collocation_icon);
//                } else if (postDetail.postingType == 5) {
//                    drawable = context.getResources().getDrawable(R.drawable.post_collocation_icon);
//                } else if (postDetail.postingType == 6) {
//                    drawable = context.getResources().getDrawable(R.drawable.post_coupon_icon);
//                }
//                SpannableString spannableString = new SpannableString(content);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                ImageSpanCentre imageSpan = new ImageSpanCentre(drawable, ImageSpanCentre.CENTRE);
//                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                tipContent.setText(spannableString);
//            } else {
//                tipContent.setText(Html.fromHtml(postDetail.postingContent));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        gridLayout.removeAllViews();
//        if (postDetail.postingType == 5 && postDetail.videoUrls != null && postDetail.videoUrls.size() > 0) {
//            addVideoView(gridLayout, holder, postDetail);//视频搭配
//            final List<String> idList = new ArrayList<>();
//            for (int i = 0; i < postDetail.videoUrls.size(); i++) {
//                idList.add(postDetail.videoUrls.get(i).id);
//            }
//            gridLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ARouter.getInstance().build(CityRoutes.CITY_ADPOSTDETIAL)
//                            .withString("id", postDetail.id)
//                            .withString("type", postDetail.postingType + "")
//                            .withString("createUserFaceUrl", postDetail.createUserFaceUrl)
//                            .withString("accountNickname", postDetail.accountNickname)
//                            .withObject("idList", idList)
//                            .navigation();
//                }
//            });
//        } else if (postDetail.postingType == 4 || postDetail.postingType == 3 || postDetail.postingType == 2) {
//            if (postDetail.imgUrls != null && postDetail.imgUrls.size() > 0) {
//                final List<String> idList = new ArrayList<>();
//                for (int i = 0; i < postDetail.imgUrls.size(); i++) {
//                    idList.add(postDetail.imgUrls.get(i).id);
//                }
//                addImagesView(gridLayout, holder, postDetail, idList);//图文搭配
//                gridLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ARouter.getInstance().build(CityRoutes.CITY_ADPOSTDETIAL)
//                                .withString("id", postDetail.id)
//                                .withString("type", postDetail.postingType + "")
//                                .withString("createUserFaceUrl", postDetail.createUserFaceUrl)
//                                .withString("accountNickname", postDetail.accountNickname)
//                                .withObject("idList", idList)
//                                .withInt("pos", 0)
//                                .navigation();
//                    }
//                });
//            }
//        } else if (postDetail.postingType == 6) {
//            if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {
//                addCouponView(gridLayout, holder, postDetail);//优惠券
//                gridLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (moutClickListener != null) {
//                            moutClickListener.outClick("coupon", postDetail.id);
//                            moutClickListener.outClick("position", position);
//                        }
//                    }
//                });
//            }
//        } else {
//            gridLayout.removeAllViews();
//        }
//    }
//
//    private void addCouponView(final GridLayout gridLayout, BaseHolder holder, final IndexAllChat2 postDetail) {
//        gridLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                gridLayout.removeAllViews();
//                int row = 1;
//                int needSize = postDetail.postActivityList.get(0).activityCoupon.size();
//                if (needSize < 3) {
//                    needSize = 2;
//                    row = 1;
//                } else {
//                    needSize = 4;
//                    row = 2;
//                }
//                gridLayout.setRowCount(row);
//                int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight()) / 2;
//                for (int i = 0; i < needSize; i++) {
//                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                    params.width = w;
//                    params.height = (int) TransformUtil.dp2px(context, 72f);
//                    View view = LayoutInflater.from(context).inflate(R.layout.city_item_ad_coupon_layout, gridLayout, false);
//                    ConstraintLayout couponLayout = (ConstraintLayout) view.findViewById(R.id.couponLayout);
//                    TextView couponMoney = (TextView) view.findViewById(R.id.couponMoney);
//                    TextView couponType = (TextView) view.findViewById(R.id.couponType);
//                    TextView receive = (TextView) view.findViewById(R.id.receive);
//                    ConstraintLayout moreLayout = (ConstraintLayout) view.findViewById(R.id.moreLayout);
//                    ImageView moreImg = (ImageView) view.findViewById(R.id.moreImg);
//                    TextView moreTitle = (TextView) view.findViewById(R.id.moreTitle);
//                    if (i != needSize - 1) {
//                        PostActivityList.ActivityCoupon activityCoupon = postDetail.postActivityList.get(0).activityCoupon.get(i);
//                        moreLayout.setVisibility(View.GONE);
//                        couponLayout.setVisibility(View.VISIBLE);
//                        if (activityCoupon != null) {
//                            if (activityCoupon.isCanReceive()) {
//                                couponLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_city_coupon_item_bg));
//                                receive.setBackground(context.getResources().getDrawable(R.drawable.shape_city_item_coupon_receive));
//                                couponMoney.setTextColor(Color.parseColor("#F08957"));
//                                couponType.setTextColor(Color.parseColor("#EE9551"));
//                                receive.setTextColor(Color.parseColor("#ffee9551"));
//                                receive.setText("领取");
//                            } else {
//                                couponLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_city_coupon_item_invalid));
//                                receive.setBackground(context.getResources().getDrawable(R.drawable.shape_city_item_coupon_invalid));
//                                couponMoney.setTextColor(Color.parseColor("#999999"));
//                                couponType.setTextColor(Color.parseColor("#999999"));
//                                receive.setTextColor(Color.parseColor("#999999"));
//                                if (activityCoupon.couponQuantity <= 0) {
//                                    receive.setText("已领完");
//                                } else {
//                                    receive.setText("已领取");
//                                }
//                            }
//                            if (FormatUtils.moneyKeep2Decimals(activityCoupon.denomination).length() >= 4) {
//                                SpannableString spannableString = new SpannableString(FormatUtils.moneyKeep2Decimals(activityCoupon.denomination) + "元");
//                                spannableString.setSpan(new AbsoluteSizeSpan(28, true), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                spannableString.setSpan(new AbsoluteSizeSpan(12, true), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
//                                spannableString.setSpan(new StyleSpan(Typeface.NORMAL), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
//                                couponMoney.setText(spannableString);
//                            } else {
//                                SpannableString spannableString = new SpannableString(FormatUtils.moneyKeep2Decimals(activityCoupon.denomination) + "元");
//                                spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                spannableString.setSpan(new AbsoluteSizeSpan(12, true), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
//                                spannableString.setSpan(new StyleSpan(Typeface.NORMAL), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
//                                couponMoney.setText(spannableString);
//                            }
//                            couponType.setText(activityCoupon.getRequirement());
//                        }
//                    } else {
//                        moreLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_city_coupon_item_bg));
//                        moreLayout.setVisibility(View.VISIBLE);
//                        couponLayout.setVisibility(View.GONE);
//                    }
//                    gridLayout.addView(view, params);
//                }
//            }
//        });
//    }
//
//    private void addImagesView(final GridLayout gridLayout, final BaseHolder holder, final IndexAllChat2 postDetail, final List<String> idList) {
//        gridLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                gridLayout.removeAllViews();
//                int row = 2;
//                int needSize = postDetail.imgUrls.size();
//                if (postDetail.imgUrls.size() == 1) {
//                    needSize = 1;
//                    row = 1;
//                } else if (postDetail.imgUrls.size() == 3 || postDetail.imgUrls.size() == 2) {
//                    needSize = 2;
//                    row = 1;
//                } else if (postDetail.imgUrls.size() > 3) {
//                    needSize = 4;
//                    row = 2;
//                }
//                gridLayout.setRowCount(row);
//                int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight()) / 2;
//                for (int i = 0; i < needSize; i++) {
//                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                    params.width = w;
//                    params.height = (int) TransformUtil.dp2px(context, 160f);
//                    View view = LayoutInflater.from(context).inflate(R.layout.city_item_ad_image_layout, gridLayout, false);
//                    ImageView adImg =  view.findViewById(R.id.adImg);
//                    ImageView isVideo = (ImageView) view.findViewById(R.id.isVideo);
//                    TextView imgMark = (TextView) view.findViewById(R.id.imgMark);
//                    ConstraintLayout videoGoodsLayout = (ConstraintLayout) view.findViewById(R.id.videoGoodsLayout);
//                    CornerImageView goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
//                    TextView goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
//                    isVideo.setVisibility(View.GONE);
//                    videoGoodsLayout.setVisibility(View.GONE);
//                    try {
//                        com.healthy.library.businessutil.GlideCopy.with(context)
//                                .load(postDetail.imgUrls.get(i).url)
//                                .placeholder(R.drawable.img_1_1_default2)
//                                .error(R.drawable.img_1_1_default)
//
//                                .into(adImg);
//                    } catch (
//                            Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (postDetail.imgUrls.size() == 3) {
//                        if (i == 1) {
//                            imgMark.setVisibility(View.VISIBLE);
//                            imgMark.setText(postDetail.imgUrls.size() + "图");
//                        } else {
//                            imgMark.setVisibility(View.GONE);
//                        }
//                    } else if (postDetail.imgUrls.size() > 4) {
//                        if (i == 3) {
//                            imgMark.setVisibility(View.VISIBLE);
//                            imgMark.setText(postDetail.imgUrls.size() + "图");
//                        } else {
//                            imgMark.setVisibility(View.GONE);
//                        }
//                    } else {
//                        imgMark.setVisibility(View.GONE);
//                    }
//                    final int position = i;
//                    view.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ARouter.getInstance().build(CityRoutes.CITY_ADPOSTDETIAL)
//                                    .withString("id", postDetail.id)
//                                    .withString("type", postDetail.postingType + "")
//                                    .withString("createUserFaceUrl", postDetail.createUserFaceUrl)
//                                    .withString("accountNickname", postDetail.accountNickname)
//                                    .withObject("idList", idList)
//                                    .withInt("pos", position)
//                                    .navigation();
//                        }
//                    });
//                    gridLayout.addView(view, params);
//                }
//            }
//        });
//    }
//
//    private void addVideoView(final GridLayout gridLayout, BaseHolder holder, final IndexAllChat2 postDetail) {
//        gridLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                gridLayout.removeAllViews();
//                int row = 1;
//                gridLayout.setRowCount(row);
//                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.height = (int) TransformUtil.dp2px(context, 327f);
//                params.setMargins(mMargin, mMargin, mMargin, mMargin);
//                View view = LayoutInflater.from(context).inflate(R.layout.city_item_ad_image_layout, gridLayout, false);
//                ImageView adImg =  view.findViewById(R.id.adImg);
//                ImageView isVideo = (ImageView) view.findViewById(R.id.isVideo);
//                TextView imgMark = (TextView) view.findViewById(R.id.imgMark);
//                ConstraintLayout videoGoodsLayout = (ConstraintLayout) view.findViewById(R.id.videoGoodsLayout);
//                CornerImageView goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
//                TextView goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
//                isVideo.setVisibility(View.VISIBLE);
//                try {
//                    com.healthy.library.businessutil.GlideCopy.with(context)
//                            .load(postDetail.videoFramePicture)
//                            .placeholder(R.drawable.img_1_1_default2)
//                            .error(R.drawable.img_1_1_default)
//
//                            .into(adImg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {
//                    if (postDetail.postActivityList.get(0).newGoodsDTO != null) {
//                        videoGoodsLayout.setVisibility(View.VISIBLE);
//                        com.healthy.library.businessutil.GlideCopy.with(context)
//                                .load(postDetail.postActivityList.get(0).newGoodsDTO.headImage)
//                                .placeholder(R.drawable.img_1_1_default2)
//                                .error(R.drawable.img_1_1_default)
//
//                                .into(goodsImg);
//                        goodsMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(postDetail.postActivityList.get(0).newGoodsDTO.platformPrice));
//                    } else {
//                        videoGoodsLayout.setVisibility(View.GONE);
//                    }
//                } else {
//                    videoGoodsLayout.setVisibility(View.GONE);
//                }
//                gridLayout.addView(view, params);
//            }
//        });
//    }
//
//    private void buildDefaultPost(BaseHolder helper, final IndexAllChat2 item) {
//        CornerImageView ivHeader;
//        TextView name;
//        TextView days;
//        TextView status;
//        FlexboxLayout tipTitle;
//        TextView tipContent;
//        GridLayout seeImgs;
//        TextView tipAddress;
//        ImageView tipShare;
//        ImageTextView discuss;
//        final ImageTextView like;
//        View candelete;
//        View nameandstatus;
//        ivHeader = (CornerImageView) helper.getView(R.id.ivHeader);
//        name = (TextView) helper.getView(R.id.name);
//        days = (TextView) helper.getView(R.id.days);
//        status = (TextView) helper.getView(R.id.status);
//        tipTitle = (FlexboxLayout) helper.getView(R.id.tipTitle);
//        tipContent = (TextView) helper.getView(R.id.tipContent);
//        seeImgs = (GridLayout) helper.getView(R.id.see_imgs);
//        tipAddress = (TextView) helper.getView(R.id.tipAddress);
//        tipShare = (ImageView) helper.getView(R.id.tipShare);
//        discuss = (ImageTextView) helper.getView(R.id.discuss);
//        like = (ImageTextView) helper.getView(R.id.like);
//        candelete = (View) helper.getView(R.id.candelete);
//        nameandstatus = (View) helper.getView(R.id.nameandstatus);
//        String nickname = "";
//        if (item.anonymousStatus == 1) {
//            nickname = "匿名用户";
//        } else if (TextUtils.isEmpty(item.accountNickname)) {
//
//            nickname = "用户已注销";
//        } else {
//            nickname = item.accountNickname;
//        }
//        name.setText(nickname);
//        if (item.anonymousStatus == 1) {
//            com.healthy.library.businessutil.GlideCopy.with(context)
//                    .asBitmap()
//                    .load(R.drawable.img_avatar_default)
//                    .placeholder(R.drawable.img_1_1_default2)
//                    .error(R.drawable.img_avatar_default)
//
//                    .into(ivHeader);
//        } else {
//            com.healthy.library.businessutil.GlideCopy.with(context)
//                    .asBitmap()
//                    .load(item.createUserFaceUrl)
//                    .placeholder(R.drawable.img_1_1_default2)
//                    .error(R.drawable.img_avatar_default)
//
//                    .into(ivHeader);
//        }
//        if (TextUtils.isEmpty(item.memberState)) {
//            status.setVisibility(View.GONE);
//        } else {
//            status.setText(item.memberState.replace("育儿期", ""));
//            status.setVisibility(View.VISIBLE);
//        }
//        tipTitle.removeAllViews();
//        String sharetitle = "同城圈";
//        if (item.topicList != null && item.topicList.size() > 0) {
//            sharetitle = "";
//            for (int i = 0; i < (item.topicList.size() >= 3 ? 3 : item.topicList.size()); i++) {
//                View tipchild = LayoutInflater.from(context).inflate(R.layout.city_item_tip_single, tipTitle, false);
//                TextView tipname = tipchild.findViewById(R.id.tipSmall);
//                tipname.setText(item.topicList.get(i).topicName);
//                final int finalI = i;
//                tipchild.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ARouter.getInstance()
//                                .build(CityRoutes.CITY_TIP)
//                                .withString("activitytype", "全国")
//                                .withString("topicId", item.topicList.get(finalI).id + "")
//                                .navigation();
//                    }
//                });
//                sharetitle = sharetitle + "#" + item.topicList.get(i).topicName + "#";
//                tipTitle.addView(tipchild);
//            }
//
//        }
//        candelete.setVisibility(View.GONE);
//        tipAddress.setText(item.postingAddress);
//        if (TextUtils.isEmpty(item.postingAddress)) {
//            tipAddress.setVisibility(View.GONE);
//        } else {
//            tipAddress.setVisibility(View.VISIBLE);
//        }
//        final Drawable likeNormal = context.getResources().getDrawable(R.drawable.cityrightlike);
//        final Drawable likeSelect = context.getResources().getDrawable(R.drawable.cityrightliker);
//        if (item.praiseState == 0) {
//            like.setDrawable(likeNormal);
//            like.setTextColor(Color.parseColor("#444444"));
//        } else {
//            like.setTextColor(Color.parseColor("#F93F60"));
//            like.setDrawable(likeSelect);
//        }
//        final String finalNickname = nickname;
//        ivHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (item.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)) {
//                    ARouter.getInstance()
//                            .build(CityRoutes.CITY_FANDETAIL)
//                            .withString("searchMemberType", item.createSource + "")
//                            .withString("memberId", item.memberId + "")
//                            .navigation();
//                }
//
//            }
//        });
//        nameandstatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (item.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)) {
//                    ARouter.getInstance()
//                            .build(CityRoutes.CITY_FANDETAIL)
//                            .withString("searchMemberType", item.createSource + "")
//                            .withString("memberId", item.memberId + "")
//                            .navigation();
//                }
//
//            }
//        });
//        final String finalSharetitle = sharetitle;
//        tipShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onChatShareClickListener != null) {
//                    String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_POSTURL);
//                    String url = String.format("%s?id=%s&scheme=HMMPostDetail&postId=%s&referral_code=%s", urlPrefix, item.id, item.id, SpUtils.getValue(context, SpKey.GETTOKEN));
//                    try {
//                        onChatShareClickListener.chatshareclick(view, url, com.healthy.library.utils.JsoupCopy.parse(item.postingContent).text(), finalSharetitle);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//        like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                if (onChatLikeClickListener != null) {
//                    onChatLikeClickListener.chatlikeclick(view, item.id + "", item.praiseState == 0 ? "0" : "1");
//                    item.praiseState = item.praiseState == 0 ? 1 : 0;
//                    item.praiseNum = item.praiseNum + (item.praiseState == 0 ? -1 : 1);
//                    like.setText(item.praiseNum == 0 ? "  " : item.praiseNum + "");
//                    if (item.praiseState == 0) {
//                        like.setDrawable(likeNormal);
//                        like.setTextColor(Color.parseColor("#444444"));
//                    } else {
//                        like.setTextColor(Color.parseColor("#F93F60"));
//                        like.setDrawable(likeSelect);
//                    }
//
//                }
//            }
//        });
//        helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ARouter.getInstance()
//                        .build(CityRoutes.CITY_POSTDETAIL)
//                        .withString("id", item.id + "")
//                        .withBoolean("isshowDiscuss", false)
//                        .navigation();
//            }
//        });
//        discuss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ARouter.getInstance()
//                        .build(CityRoutes.CITY_POSTDETAIL)
//                        .withString("id", item.id + "")
//                        .withBoolean("isshowDiscuss", true)
//                        .navigation();
//            }
//        });
//        days.setText(DateUtils.getClassDatePost(item.createTime));
//        try {
//            if (item.postingContent != null && item.postingContent.contains("\n")) {//说明纯文本 那就直接用文本接收
//                tipContent.setText(item.postingContent);
//            } else {
//                tipContent.setText(JsoupCopy.parse(item.postingContent).text());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        discuss.setText(item.discussNum == 0 ? "  " : item.discussNum + "");
//        like.setText(item.praiseNum == 0 ? "  " : item.praiseNum + "");
//        List<String> videsurls = new ArrayList<>();
//        if (item.videoUrls != null) {
//            for (int j = 0; j < item.videoUrls.size(); j++) {
//                IndexAllChat2.ImgUrl videoUrl = item.videoUrls.get(j);
//                videsurls.add(videoUrl.url);
//            }
//        }
//        List<String> imgUrls = new ArrayList<>();
//        if (item.imgUrls != null) {
//            for (int j = 0; j < item.imgUrls.size(); j++) {
//                IndexAllChat2.ImgUrl videoUrl = item.imgUrls.get(j);
//                videsurls.add(videoUrl.url);
//            }
//        }
//        List<String> showImg = new ArrayList<>();
//        showImg.clear();
//        try {
//            showImg.addAll(videsurls);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            showImg.addAll(imgUrls);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (showImg.size() > 0) {
//            seeImgs.setVisibility(View.VISIBLE);
//        } else {
//
//            seeImgs.setVisibility(View.GONE);
//        }
//        addImages(context, seeImgs, showImg);
//    }
//
//    private int mMargin;
//    private int mCornerRadius;
//
//    private void addImages(final Context context, final GridLayout gridLayout, final List<String> urls) {
//        if (mMargin == 0) {
//            mMargin = (int) TransformUtil.dp2px(context, 2);
//            mCornerRadius = (int) TransformUtil.dp2px(context, 5);
//        }
//        //System.out.println("展示分格");
//        gridLayout.removeAllViews();
//        if (urls != null && urls.size() > 0) {
//            gridLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    int row = 3;
//                    if (urls.size() == 1) {
//                        row = 1;
//                    }
//                    if (urls.size() == 2) {
//                        row = 2;
//                    }
//                    gridLayout.removeAllViews();
//                    gridLayout.setRowCount(row);
//                    int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
//                    int needsize = urls.size();
//                    if (urls.size() >= 3) {
//                        needsize = 3;
//                    }
//                    for (int i = 0; i < needsize; i++) {
//                        final String url = urls.get(i);
//                        final int pos = i;
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                        params.width = w;
//                        if (row == 3) {
//
//                            params.height = (int) TransformUtil.dp2px(context, 110f);
//                        } else if (row == 2) {
//                            params.height = (int) TransformUtil.dp2px(context, 170f);
//                        } else {
//                            params.width = (int) TransformUtil.dp2px(context, 220f);
//                            params.height = (int) TransformUtil.dp2px(context, 220f);
//                        }
//                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
//
//                        View imageparent = LayoutInflater.from(context).inflate(R.layout.index_item_image, gridLayout, false);
//
//                        final CornerImageView imageView = imageparent.findViewById(R.id.iv_pic);
//                        imageView.setCornerRadius(mCornerRadius);
//                        final ImageView isVideo = imageparent.findViewById(R.id.isVideo);
//
//                        if (MediaFileUtil.isVideoFileType(url)) {
//                            isVideo.setVisibility(View.VISIBLE);
//                            if (bitmapList.get(url) != null) {
//                                imageView.setImageBitmap(bitmapList.get(url));
//                                //System.out.println("已经有数据了首页");
//                            } else {
//                                imageView.setImageResource(R.drawable.img_1_1_default2);
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Bitmap bitmap = null;
//                                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                                        try {
//                                            //根据url获取缩略图
//                                            retriever.setDataSource(url, new HashMap());
//                                            //获得第一帧图片
//                                            bitmap = retriever.getFrameAtTime();
//                                            final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap, imageView.getWidth() + 10, imageView.getHeight());
//                                            ((Activity) context).runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    imageView.setImageBitmap(finalBitmap);
//                                                    bitmapList.put(url, finalBitmap);
//                                                }
//                                            });
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                            }
//
//                        } else {
//                            isVideo.setVisibility(View.GONE);
//                            try {
//                                com.healthy.library.businessutil.GlideCopy.with(context)
//                                        .load(url)
//                                        .placeholder(R.drawable.img_1_1_default2)
//                                        .error(R.drawable.img_1_1_default)
//
//                                        .into(imageView);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        gridLayout.addView(imageparent, params);
//                        final String[] array = new String[urls.size()];
//                        for (int j = 0; j < array.length; j++) {
//                            array[j] = urls.get(j);
//                        }
//                        imageView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (!MediaFileUtil.isVideoFileType(url)) {
//                                    ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
//                                            .withCharSequenceArray("urls", array)
//                                            .withInt("pos", pos)
//                                            .navigation();
//                                } else {
//                                    ARouter.getInstance()
//                                            .build(LibraryRoutes.LIBRARY_VIDEOPLAYER)
//                                            .withString("videoUrl", url)
//                                            .navigation();
//                                }
//
//                            }
//                        });
//                    }
//                }
//            }, 500);
//        }
//
//
//    }
//
//
//    public interface OnChatLikeClickListener {
//        void chatlikeclick(View view, String postingId, String type);
//    }
//
//    public interface OnChatShareClickListener {
//        void chatshareclick(View view, String url, String des, String title);
//    }
//}
