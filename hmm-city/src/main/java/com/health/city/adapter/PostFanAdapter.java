//package com.health.city.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.media.MediaMetadataRetriever;
//import android.text.Html;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.AppCompatTextView;
//import androidx.constraintlayout.widget.Barrier;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
//import com.example.lib_ShapeView.view.ShapeTextView;
//import com.google.android.flexbox.FlexboxLayout;
//import com.health.city.R;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.businessutil.ListUtil;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.model.Fans;
//import com.healthy.library.model.PostDetail;
//import com.healthy.library.routes.CityRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.utils.BitmapUtils;
//import com.healthy.library.utils.MediaFileUtil;
//import com.healthy.library.utils.ScreenUtils;
//import com.healthy.library.utils.SpUtils;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.ImageTextView;
//import com.hss01248.dialog.StyledDialog;
//import com.hss01248.dialog.interfaces.MyDialogListener;
//import com.lihang.ShadowLayout;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author Li
// * @date 2019/03/25 11:03
// * @des 推荐阅读
// */
//
//public class PostFanAdapter extends BaseAdapter<PostDetail> {
//    private int mMargin;
//    private int mCornerRadius;
//    private boolean isdelete = false;
//
//    public void setTotalcount(int totalcount) {
//        this.totalcount = totalcount;
//    }
//
//    private int totalcount = 0;
//
//
//    private List<Fans> fans = new ArrayList<>();
//
//    public void setFans(List<Fans> fans) {
//        this.fans = fans;
//    }
//
//    public void setFragmentType(String fragmentType) {
//        this.fragmentType = fragmentType;
//    }
//
//    private String fragmentType;
//
//    public void setOnPostFansClickListener(OnPostFansClickListener onPostFansClickListener) {
//        this.onPostFansClickListener = onPostFansClickListener;
//    }
//
//    public void setOnPostLikeClickListener(OnPostLikeClickListener onPostLikeClickListener) {
//        this.onPostLikeClickListener = onPostLikeClickListener;
//    }
//
//    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
//        this.onShareClickListener = onShareClickListener;
//    }
//
//    public void setOnPostFansChangeListener(OnPostFansChangeListener onPostFansChangeListener) {
//        this.onPostFansChangeListener = onPostFansChangeListener;
//    }
//
//    public void setOnPostFansAllListener(OnPostFansAllListener onPostFansAllListener) {
//        this.onPostFansAllListener = onPostFansAllListener;
//    }
//
//    OnPostFansAllListener onPostFansAllListener;
//    OnPostFansChangeListener onPostFansChangeListener;
//
//    OnShareClickListener onShareClickListener;
//    OnPostFansClickListener onPostFansClickListener;
//    OnPostLikeClickListener onPostLikeClickListener;
//
//    public PostFanAdapter() {
//        this(R.layout.city_item_fragment_follow_fan);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        return linearLayoutHelper;
//    }
//
//    private PostFanAdapter(int layoutResId) {
//        super(layoutResId);
//
//    }
//
//    private int getFansPos() {
//        if (totalcount >= 5) {
//            return 4;
//        } else {
//            return totalcount;
//        }
//    }
//
//    LinearLayout fansLLGlobal;
//
//    @Override
//    public void onBindViewHolder(@NonNull final BaseHolder helper, int position) {
//
//        final LinearLayout cityItemFragmentFollowHead;
//        final CornerImageView ivHeader;
//        final ConstraintLayout nameandstatus;
//        final TextView name;
//        final TextView days;
//        final TextView status;
////        final TextView toFollow;
//        final LinearLayout cityItemFragmentFollowChild;
//        final ImageView ivEmptyStock;
//        final FlexboxLayout tipTitle;
//        final ImageView candelete;
//        final Barrier tipBar;
//        final TextView tipContent;
//        final TextView toFollow;
//        final GridLayout seeImgs;
//        final TextView tipAddress;
//        final ImageTextView tipShare;
//        final LinearLayout canlikediscuss;
//        final ImageTextView discuss;
//        final ImageTextView like;
//        final View dividerHeader;
//        final View dividerEnd;
//        final ViewGroup mFansAllLL;
//        final ShadowLayout mShadowLayout;
//        final AppCompatTextView fansTitleLL;
//        final TextView fansTitle;
//        final LinearLayout fansLL;
//        final View fansLLDiver;
//        final LinearLayout llIndexRefresh;
//        final AppCompatTextView llIndexAllFan;
//
//
//        cityItemFragmentFollowHead = (LinearLayout) helper.itemView.findViewById(R.id.city_item_fragment_follow_head);
//        ivHeader = (CornerImageView) helper.itemView.findViewById(R.id.ivHeader);
//        nameandstatus = (ConstraintLayout) helper.itemView.findViewById(R.id.nameandstatus);
//        name = (TextView) helper.itemView.findViewById(R.id.name);
//        days = (TextView) helper.itemView.findViewById(R.id.days);
//        status = (TextView) helper.itemView.findViewById(R.id.status);
////        toFollow = (TextView) helper.itemView.findViewById(R.id.toFollow);
//        cityItemFragmentFollowChild = (LinearLayout) helper.itemView.findViewById(R.id.city_item_fragment_follow_child);
//        ivEmptyStock = (ImageView) helper.itemView.findViewById(R.id.iv_empty_stock);
//        tipTitle = (FlexboxLayout) helper.itemView.findViewById(R.id.tipTitle);
//        candelete = (ImageView) helper.itemView.findViewById(R.id.candelete);
//        tipBar = (Barrier) helper.itemView.findViewById(R.id.tipBar);
//        tipContent = (TextView) helper.itemView.findViewById(R.id.tipContent);
//        seeImgs = (GridLayout) helper.itemView.findViewById(R.id.see_imgs);
//        tipAddress = (TextView) helper.itemView.findViewById(R.id.tipAddress);
//        tipShare = (ImageTextView) helper.itemView.findViewById(R.id.tipShare);
//        canlikediscuss = (LinearLayout) helper.itemView.findViewById(R.id.canlikediscuss);
//        discuss = (ImageTextView) helper.itemView.findViewById(R.id.discuss);
//        like = (ImageTextView) helper.itemView.findViewById(R.id.like);
//        dividerHeader = (View) helper.itemView.findViewById(R.id.divider_header);
//        mFansAllLL = helper.itemView.findViewById(R.id.mFansAllLL);
//        mShadowLayout = (ShadowLayout) helper.itemView.findViewById(R.id.mShadowLayout);
//        fansTitleLL = helper.itemView.findViewById(R.id.fansTitle);
//        fansTitle = (TextView) helper.itemView.findViewById(R.id.fansTitle);
//        toFollow = (TextView) helper.itemView.findViewById(R.id.toFollow);
//        fansLL = (LinearLayout) helper.itemView.findViewById(R.id.fansLL);
//        fansLLDiver = (View) helper.itemView.findViewById(R.id.fansLLDiver);
//        llIndexRefresh = (LinearLayout) helper.itemView.findViewById(R.id.ll_index_refresh);
//        llIndexAllFan = helper.itemView.findViewById(R.id.ll_index_all_fan);
//        //dividerEnd = (View) helper.itemView.findViewById(R.id.divider_end);
//        //dividerEnd.setVisibility(View.VISIBLE);
//        cityItemFragmentFollowHead.setVisibility(View.VISIBLE);
//        cityItemFragmentFollowChild.setVisibility(View.VISIBLE);
//        dividerHeader.setVisibility(View.VISIBLE);
//        final PostDetail item = getDatas().get(position);
//
//        helper.setVisible(R.id.postingTitle, !TextUtils.isEmpty(item.postingTitle))
//                .setVisible(R.id.iv_essences, !ListUtil.isEmpty(item.postingTagList))
//                .setText(R.id.postingTitle, item.postingTitle);
//
//        ImageView mIvEssences = helper.getView(R.id.iv_essences);
//        //精华帖按钮
//        mIvEssences.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!context.getClass().getSimpleName().equals("EssencesActivity")) {
//                    ARouter.getInstance()
//                            .build(CityRoutes.CITY_ESSENCES)
//                            .navigation();
//                }
//            }
//        });
//
//        boolean empty = TextUtils.isEmpty(item.badgeId);
//        helper.setVisible(R.id.iv_user_badge, !empty)
//                .setVisible(R.id.stv_user_badgeName, !empty)
//                .setText(R.id.stv_user_badgeName, item.badgeName);
//        ImageView mUserBadge = helper.getView(R.id.iv_user_badge);
//        ShapeTextView mUserBadgeName = helper.getView(R.id.stv_user_badgeName);
//        if (!empty) {
//            if (item.badgeId != mUserBadgeName.getTag()) {
//                mUserBadgeName.setTag(item.badgeId);
//                ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
//                if (0 == item.badgeType) {
//                    mUserBadge.setImageResource(com.healthy.library.R.drawable.icon_user_certification);
//                    shapeDrawableBuilder
//                            .setSolidColor(0)
//                            .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
//                            .intoBackground();
//                }
//                if (1 == item.badgeType) {
//                    mUserBadge.setImageResource(com.healthy.library.R.drawable.icon_user_official_certification);
//                    shapeDrawableBuilder
//                            .clearGradientColors();
//                    shapeDrawableBuilder
//                            .setSolidColor(Color.parseColor("#3889FD")).intoBackground();
//                }
//            }
//        }
//
//        fansLLGlobal = fansLL;
//        if (item.isnull) {//说明没有啥了 直接推荐吧
//            //System.out.println("直接推荐");
//            cityItemFragmentFollowHead.setVisibility(View.GONE);
//            cityItemFragmentFollowChild.setVisibility(View.GONE);
//            dividerHeader.setVisibility(View.GONE);
//            //dividerEnd.setVisibility(View.GONE);
//        } else {
////            if (item.memberState != null) {
////                if (item.memberState.contains("备孕")) {//备孕
////                    status.setBackgroundResource(R.drawable.shape_city_nofollow_red);
////                } else if (item.memberState.contains("宝") || item.memberState.contains("产后")) {//宝宝出身
////                    status.setBackgroundResource(R.drawable.shape_city_nofollow_yellow);
////                } else {//孕
////                    status.setBackgroundResource(R.drawable.shape_city_nofollow_green);
////                }
////            }
//            String nickname = "";
//            if (item.anonymousStatus == 1) {
//                nickname = "匿名用户";
//            } else if (TextUtils.isEmpty(item.accountNickname)) {
//
//                nickname = "用户已注销";
//            } else {
//                nickname = item.accountNickname;
//            }
//            name.setText(nickname);
//
//
//            if (item.anonymousStatus == 1) {
//                com.healthy.library.businessutil.GlideCopy.with(context)
//                        .asBitmap()
//                        .load(R.drawable.img_avatar_default)
//                        .placeholder(R.drawable.img_1_1_default2)
//                        .error(R.drawable.img_avatar_default)
//
//                        .into(ivHeader);
//            } else {
//                com.healthy.library.businessutil.GlideCopy.with(context)
//                        .asBitmap()
//                        .load(item.createUserFaceUrl)
//                        .placeholder(R.drawable.img_1_1_default2)
//                        .error(R.drawable.img_avatar_default)
//
//                        .into(ivHeader);
//            }
//            if (item.focusStatus == 0) {//关注
//                toFollow.setText("关注");
//                toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
//                toFollow.setCompoundDrawablePadding(9);
//
//            } else {
//                toFollow.setText("已关注");
//                toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
//                toFollow.setCompoundDrawablePadding(9);
//            }
//            toFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onPostFansClickListener != null && moutClickListener != null) {
//                        moutClickListener.outClick("position", helper.getPosition() - 1);
//                        moutClickListener.outClick("focus", item);
////                        onPostFansClickListener.postfansclick(v, item.memberId, item.focusStatus == 0 ? "0" : "1", item.createSource + "");
//                    }
//                }
//            });
//            if (TextUtils.isEmpty(item.memberState)) {
//                status.setVisibility(View.GONE);
//            } else {
//                status.setText(item.memberState.replace("育儿期", ""));
//                status.setVisibility(View.VISIBLE);
//            }
//            tipTitle.removeAllViews();
//            String sharetitle = "同城圈";
//            if (item.topicList != null && item.topicList.size() > 0) {
//                sharetitle = "";
//                for (int i = 0; i < (item.topicList.size() >= 3 ? 3 : item.topicList.size()); i++) {
//                    View tipchild = LayoutInflater.from(context).inflate(R.layout.city_item_tip_single, tipTitle, false);
//                    TextView tipname = tipchild.findViewById(R.id.tipSmall);
//                    tipname.setText(item.topicList.get(i).topicName);
//                    final int finalI = i;
//                    tipchild.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ARouter.getInstance()
//                                    .build(CityRoutes.CITY_TIP)
//                                    .withString("activitytype", "全国")
//                                    .withString("topicId", item.topicList.get(finalI).id + "")
//                                    .navigation();
//                        }
//                    });
//                    sharetitle = sharetitle + "#" + item.topicList.get(i).topicName + "#";
//                    tipTitle.addView(tipchild);
//                }
//
//            }
//            candelete.setVisibility(isdelete ? View.VISIBLE : View.GONE);
//            tipAddress.setText(item.postingAddress);
//            if (TextUtils.isEmpty(item.postingAddress)) {
//                tipAddress.setVisibility(View.GONE);
//            } else {
//                tipAddress.setVisibility(View.VISIBLE);
//            }
//            tipAddress.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//
////            final Drawable followNormal = context.getResources().getDrawable(R.drawable.ic_star_tofollow);
////            final Drawable followSelect = context.getResources().getDrawable(R.drawable.ic_star_isfollow);
////
////
////            if (item.focusStatus == 0) {
////                toFollow.setText("关注");
////                toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal, null, null, null);
////                toFollow.setCompoundDrawablePadding(9);
//////            toFollow.setTextColor(Color.parseColor("#ff29bda9"));
//////            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
////            } else {
////                toFollow.setText("已关注");
////                toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect, null, null, null);
////                toFollow.setCompoundDrawablePadding(9);
//////            toFollow.setTextColor(Color.parseColor("#9596A4"));
//////            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_no);
////            }
//            final Drawable likeNormal = context.getResources().getDrawable(R.drawable.cityrightlike);
//            final Drawable likeSelect = context.getResources().getDrawable(R.drawable.cityrightliker);
//            if (item.praiseState == 0) {
//                like.setDrawable(likeNormal);
//                like.setTextColor(Color.parseColor("#444444"));
//            } else {
//                like.setTextColor(Color.parseColor("#F93F60"));
//                like.setDrawable(likeSelect);
//            }
//
//            final String finalNickname = nickname;
////            toFollow.setVisibility(View.VISIBLE);
////            if (item.memberId.equals(new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {
////
////                toFollow.setVisibility(View.GONE);
////            }
////            if (item.anonymousStatus == 1) {
////                toFollow.setVisibility(View.GONE);
////            }
////            if (finalNickname.equals("用户已注销") || item.anonymousStatus == 1) {
////                toFollow.setVisibility(View.GONE);
////            }
//            ivHeader.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (item.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)) {
//                        ARouter.getInstance()
//                                .build(CityRoutes.CITY_FANDETAIL)
//                                .withString("searchMemberType", item.createSource + "")
//                                .withString("memberId", item.memberId + "")
//                                .navigation();
//                    }
//
//                }
//            });
//            nameandstatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (item.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)) {
//                        ARouter.getInstance()
//                                .build(CityRoutes.CITY_FANDETAIL)
//                                .withString("searchMemberType", item.createSource + "")
//                                .withString("memberId", item.memberId + "")
//                                .navigation();
//                    }
//
//                }
//            });
//
//            final String finalSharetitle = sharetitle;
//            tipShare.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onShareClickListener != null) {
//                        String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_POSTURL);
//                        String url = String.format("%s?id=%s&scheme=HMMPostDetail&postId=%s", urlPrefix, item.id, item.id);
//                        try {
//                            onShareClickListener.postshareclick(view, url, com.healthy.library.utils.JsoupCopy.parse(item.getPostingContent()).text(), finalSharetitle,item.id);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            });
//
//            if (item.praiseState == 0) {
//
//            } else {
//
//            }
//            like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View view) {
//                    if (onPostLikeClickListener != null) {
//                        onPostLikeClickListener.postlikeclick(view, item.id + "", item.praiseState == 0 ? "0" : "1");
//                        item.praiseState = item.praiseState == 0 ? 1 : 0;
//                        item.praiseNum = item.praiseNum + (item.praiseState == 0 ? -1 : 1);
//                        like.setText(item.praiseNum == 0 ? "  " : item.praiseNum + "");
//                        if (item.praiseState == 0) {
//                            like.setDrawable(likeNormal);
//                            like.setTextColor(Color.parseColor("#444444"));
//                        } else {
//                            like.setTextColor(Color.parseColor("#F93F60"));
//                            like.setDrawable(likeSelect);
//                        }
//
//                    }
//                }
//            });
//
////            toFollow.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(final View view) {
////                    if (onPostFansClickListener != null) {
////                        if (item.focusStatus != 0) {
////                            StyledDialog.init(context);
////                            StyledDialog.buildIosAlert("", "确认取消关注吗?", new MyDialogListener() {
////                                @Override
////                                public void onFirst() {
////
////                                }
////
////                                @Override
////                                public void onThird() {
////                                    super.onThird();
////                                }
////
////                                @Override
////                                public void onSecond() {
////                                    onPostFansClickListener.postfansclick(view, item.memberId, item.focusStatus == 0 ? "0" : "1", item.createSource + "");
////                                    item.focusStatus = item.focusStatus == 0 ? 1 : 0;
////                                    if (item.focusStatus == 0) {
////                                        toFollow.setText("关注");
////                                        toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal, null, null, null);
////                                        toFollow.setCompoundDrawablePadding(9);
////                                    } else {
////                                        toFollow.setText("已关注");
////                                        toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect, null, null, null);
////                                        toFollow.setCompoundDrawablePadding(9);
////                                    }
////
////
////                                }
////                            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
////                        } else {
////                            onPostFansClickListener.postfansclick(view, item.memberId, item.focusStatus == 0 ? "0" : "1", item.createSource + "");
////                            item.focusStatus = item.focusStatus == 0 ? 1 : 0;
////                            if (item.focusStatus == 0) {
////                                toFollow.setText("关注");
////                                toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal, null, null, null);
////                                toFollow.setCompoundDrawablePadding(9);
////                            } else {
////                                toFollow.setText("已关注");
////                                toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect, null, null, null);
////                                toFollow.setCompoundDrawablePadding(9);
////                            }
////                        }
////
////
////                    }
////                }
////            });
//            helper.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ARouter.getInstance()
//                            .build(CityRoutes.CITY_POSTDETAIL)
//                            .withString("id", item.id + "")
//                            .withBoolean("isshowDiscuss", false)
//                            .navigation();
//                }
//            });
//            discuss.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ARouter.getInstance()
//                            .build(CityRoutes.CITY_POSTDETAIL)
//                            .withString("id", item.id + "")
//                            .withBoolean("isshowDiscuss", true)
//                            .navigation();
//                }
//            });
//            //days.setText(DateUtils.getClassDatePost(item.createTime));
//            days.setText(item.createTimeStr);
//            try {
//                if (item.getPostingContent() != null && item.getPostingContent().contains("\n")) {//说明纯文本 那就直接用文本接收
//                    tipContent.setText(StringUtils.noEndLnString(item.getPostingContent()));
//                } else {
//                    tipContent.setText(Html.fromHtml(item.getPostingContent()));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (item.discussNum > 0) {
//                // 展示评论数量
//                discuss.setText(item.discussNum == 0 ? "  " : item.discussNum + "");
//            } else {
//                // 展示评论数量
//                discuss.setText("评论");
//            }
//            if (item.praiseNum > 0) {
//                //展示点赞数量
//                like.setText(item.praiseNum == 0 ? "  " : item.praiseNum + "");
//            } else {
//                //展示点赞数量
//                like.setText("点赞");
//            }
//            List<String> showImg = new ArrayList<>();
//            showImg.clear();
//
//            if (item.videoUrls != null) {
//                for (int j = 0; j < item.videoUrls.size(); j++) {
//                    PostDetail.VideoUrl videoUrl = item.videoUrls.get(j);
//                    showImg.add(videoUrl.url);
//                }
//            }
//            if (item.imgUrls != null) {
//                for (int j = 0; j < item.imgUrls.size(); j++) {
//                    PostDetail.ImageUrl videoUrl = item.imgUrls.get(j);
//                    showImg.add(videoUrl.url);
//                }
//            }
//
//            if (showImg.size() > 0) {
//                seeImgs.setVisibility(View.VISIBLE);
//            } else {
//
//                seeImgs.setVisibility(View.GONE);
//            }
//            addImages(context, seeImgs, showImg);
//        }
//        mFansAllLL.setVisibility(View.GONE);
//        if (getFansPos() == (helper.getPosition())) {
//            mFansAllLL.setVisibility(View.VISIBLE);
//            addFans(context, fansLLGlobal, fans);
//            fansTitleLL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ARouter.getInstance().build(CityRoutes.CITY_FANRECLIST)
//                            .navigation();
//                }
//            });
//
//
//            llIndexRefresh.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onPostFansChangeListener != null) {
//                        onPostFansChangeListener.postfanschange(v);
//                    }
//                }
//            });
//            llIndexAllFan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onPostFansAllListener != null) {
//                        onPostFansAllListener.postfansall(v);
//                    }
//                }
//            });
//        }
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder holder, int position, @NonNull List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position);
//        } else {
//            String payload = (String) payloads.get(0);//虽然它是个list  但是这个列表最多只有一条数据
//            if (payload.equals("coupon")) {
//
//            } else {//这里表示刷新关注按钮
//                PostDetail postDetail = getDatas().get(position);
//                if (postDetail.focusStatus == 0) {//关注
//                    ((TextView) holder.itemView.findViewById(R.id.toFollow)).setText("关注");
//                    ((TextView) holder.itemView.findViewById(R.id.toFollow)).setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
//                    ((TextView) holder.itemView.findViewById(R.id.toFollow)).setCompoundDrawablePadding(9);
//
//                } else {
//                    ((TextView) holder.itemView.findViewById(R.id.toFollow)).setText("已关注");
//                    ((TextView) holder.itemView.findViewById(R.id.toFollow)).setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
//                    ((TextView) holder.itemView.findViewById(R.id.toFollow)).setCompoundDrawablePadding(9);
//                }
//            }
//        }
//    }
//
//    public void buildFans() {
//        if (fansLLGlobal != null) {
//            addFans(context, fansLLGlobal, fans);
//        }
//    }
//
//    private void addFans(final Context context, final LinearLayout gridLayout, final List<Fans> urls) {
//        gridLayout.removeAllViews();
//        if (urls != null && urls.size() > 0) {
//            for (int i = 0; i < urls.size(); i++) {
//
//                final Fans item = urls.get(i);
//                View imageparent = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_nofollow_focus, gridLayout, false);
//                CornerImageView ivHeader;
//                TextView name;
//                TextView status;
//                final TextView toFollow;
//                View nameandstatus;
//                ivHeader = (CornerImageView) imageparent.findViewById(R.id.ivHeader);
//                name = (TextView) imageparent.findViewById(R.id.name);
//                status = (TextView) imageparent.findViewById(R.id.status);
//                toFollow = (TextView) imageparent.findViewById(R.id.toFollow);
//
//                nameandstatus = (View) imageparent.findViewById(R.id.nameandstatus);
//
//                ivHeader.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ARouter.getInstance()
//                                .build(CityRoutes.CITY_FANDETAIL)
//                                .withString("memberId", item.friendId + "")
//                                .withString("searchMemberType", item.friendType + "")
//                                .navigation();
//                    }
//                });
//                nameandstatus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ARouter.getInstance()
//                                .build(CityRoutes.CITY_FANDETAIL)
//                                .withString("memberId", item.friendId + "")
//                                .withString("searchMemberType", item.friendType + "")
//                                .navigation();
//                    }
//                });
//
////                if(item.currentStatus!=null){
////                    if(item.currentStatus.contains("备孕")){//备孕
////                        status.setBackgroundResource(R.drawable.shape_city_nofollow_red);
////                    }else if(item.currentStatus.contains("宝")||item.currentStatus.contains("产后")){//宝宝出身
////                        status.setBackgroundResource(R.drawable.shape_city_nofollow_yellow);
////                    }else {//孕
////                        status.setBackgroundResource(R.drawable.shape_city_nofollow_green);
////                    }
////                }
//                com.healthy.library.businessutil.GlideCopy.with(context)
//                        .asBitmap()
//                        .load(item.faceUrl)
//                        .placeholder(R.drawable.img_1_1_default2)
//                        .error(R.drawable.img_avatar_default)
//
//                        .into(ivHeader);
//                name.setText(item.nickName);
//                status.setText(item.currentStatus);
//                if (TextUtils.isEmpty(item.currentStatus)) {
//                    status.setVisibility(View.GONE);
//                } else {
//                    status.setVisibility(View.VISIBLE);
//                }
//                if (item.focusStatus == 0) {
//                    toFollow.setText("关注");
//                    toFollow.setTextColor(Color.parseColor("#ff29bda9"));
//                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
//                } else {
//                    toFollow.setText("已关注");
//                    toFollow.setTextColor(Color.parseColor("#9596A4"));
//                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_no);
//                }
//
//                if (item.friendId != null && item.friendId.equals(new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {
//
//                    toFollow.setVisibility(View.GONE);
//                } else {
//                    toFollow.setVisibility(View.VISIBLE);
//                }
//                toFollow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View view) {
//                        if (onPostFansClickListener != null) {
//
//
//                            if (item.focusStatus != 0) {
//                                StyledDialog.init(context);
//                                StyledDialog.buildIosAlert("", "确认取消关注吗?", new MyDialogListener() {
//                                    @Override
//                                    public void onFirst() {
//
//                                    }
//
//                                    @Override
//                                    public void onThird() {
//                                        super.onThird();
//                                    }
//
//                                    @Override
//                                    public void onSecond() {
//                                        onPostFansClickListener.postfansclick(view, item.friendId, item.focusStatus == 0 ? "0" : "1", item.friendType + "");
//                                        item.focusStatus = item.focusStatus == 0 ? 1 : 0;
//                                        if (item.focusStatus == 0) {
//                                            toFollow.setText("关注");
//                                            toFollow.setTextColor(Color.parseColor("#ff29bda9"));
//                                            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
//                                        } else {
//                                            toFollow.setText("已关注");
//                                            toFollow.setTextColor(Color.parseColor("#9596A4"));
//                                            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_no);
//                                        }
//
//
//                                    }
//                                }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
//                            } else {
//                                onPostFansClickListener.postfansclick(view, item.friendId, item.focusStatus == 0 ? "0" : "1", item.friendType + "");
//                                item.focusStatus = item.focusStatus == 0 ? 1 : 0;
//                                if (item.focusStatus == 0) {
//                                    toFollow.setText("关注");
//                                    toFollow.setTextColor(Color.parseColor("#ff29bda9"));
//                                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
//                                } else {
//                                    toFollow.setText("已关注");
//                                    toFollow.setTextColor(Color.parseColor("#9596A4"));
//                                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_no);
//                                }
//                            }
//
//
//                        }
//                    }
//                });
//                gridLayout.addView(imageparent);
//            }
//
//        }
//    }
//
//    private void addImages(final Context context, final GridLayout gridLayout, final List<String> urls) {
//        if (mMargin == 0) {
//            mMargin = (int) TransformUtil.dp2px(context, 2);
//            mCornerRadius = (int) TransformUtil.dp2px(context, 5);
//        }
//        //System.out.println("展示分格");
//        gridLayout.removeAllViews();
//        if (urls != null && urls.size() > 0) {
//            /*gridLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {*/
//                    int row = 3;
//                    if (urls.size() == 1) {
//                        row = 1;
//                    }
//
//                    gridLayout.removeAllViews();
//                    gridLayout.setRowCount(row);
//                    //int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
//                    final int w = (ScreenUtils.getScreenWidth(context) - (int) TransformUtil.dp2px(context, 60) - (2 + 2 * (row - 1)) * mMargin) / row;
//                    int needsize = urls.size();
//                    if (urls.size() >= 3) {
//                        needsize = 3;
//                    }
//                    for (int i = 0; i < needsize; i++) {
//                        final String url = urls.get(i);
//                        final int pos = i;
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                        params.width = w;
//                        params.height = (int) TransformUtil.dp2px(context, 100f);
//                        if (row == 3) {
//                            params.height = (int) TransformUtil.dp2px(context, 110f);
//                        } else if (row == 2) {
//                            params.height = (int) TransformUtil.dp2px(context, 170f);
//                        } else {
//                            params.width = (int) TransformUtil.dp2px(context, 220f);
//                            params.height = (int) TransformUtil.dp2px(context, 220f);
//                        }
//                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
//
//                        View imageparent = LayoutInflater.from(context).inflate(R.layout.city_item_normal_image, gridLayout, false);
//
//                        final CornerImageView imageView = imageparent.findViewById(R.id.iv_pic);
//                        imageView.setCornerRadius(mCornerRadius);
//                        final ImageView isVideo = imageparent.findViewById(R.id.isVideo);
//                        if (MediaFileUtil.isVideoFileType(url)) {
//                            isVideo.setVisibility(View.VISIBLE);
//                            imageView.setImageResource(R.drawable.img_1_1_default);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Bitmap bitmap = null;
//                                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                                    try {
//                                        //根据url获取缩略图
//                                        retriever.setDataSource(url, new HashMap());
//                                        //获得第一帧图片
//                                        bitmap = retriever.getFrameAtTime();
//                                        final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap, imageView.getWidth() + 10, imageView.getHeight());
//                                        ((Activity) context).runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                imageView.setImageBitmap(finalBitmap);
//                                            }
//                                        });
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//                        } else {
//                            isVideo.setVisibility(View.GONE);
//                            com.healthy.library.businessutil.GlideCopy.with(context)
//                                    .load(url)
//                                    .placeholder(R.drawable.img_1_1_default2)
//                                    .error(R.drawable.img_1_1_default)
//
//                                    .into(imageView);
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
//        /*   }, 500);
//        }*/
//    }
//
//    private void initView() {
//
//    }
//
//    public interface OnPostFansAllListener {
//        void postfansall(View view);
//    }
//
//    public interface OnPostFansChangeListener {
//        void postfanschange(View view);
//    }
//
//    public interface OnPostFansClickListener {
//        void postfansclick(View view, String friendId, String type, String frtype);
//    }
//
//    public interface OnPostLikeClickListener {
//        void postlikeclick(View view, String postingId, String type);
//    }
//
//
//    public interface OnShareClickListener {
//        void postshareclick(View view, String url, String des, String title,String postId);
//    }
//}
