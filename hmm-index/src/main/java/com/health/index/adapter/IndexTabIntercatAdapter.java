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
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.FragmentActivity;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.bumptech.glide.Glide;
//import com.google.android.flexbox.FlexboxLayout;
//import com.health.index.R;
//import com.healthy.library.BuildConfig;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.base.BaseMultiItemAdapter;
//import com.healthy.library.business.SeckShareDialog;
//import com.healthy.library.businessutil.ChannelUtil;
//import com.healthy.library.businessutil.ListUtil;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.model.ActivityModel;
//import com.healthy.library.model.EnlistActivityModel;
//import com.healthy.library.model.GoodsDetail;
//import com.healthy.library.model.PostActivityList;
//import com.healthy.library.model.PostDetail;
//import com.healthy.library.model.PrizeModel;
//import com.healthy.library.routes.CityRoutes;
//import com.healthy.library.routes.IndexRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.routes.MineRoutes;
//import com.healthy.library.utils.BitmapUtils;
//import com.healthy.library.utils.DateUtils;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.JsoupCopy;
//import com.healthy.library.utils.MediaFileUtil;
//import com.healthy.library.utils.ScreenUtils;
//import com.healthy.library.utils.SpUtils;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.CollapsedTextView;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.ImageSpanCentre;
//import com.healthy.library.widget.ImageTextView;
//import com.hss01248.dialog.StyledDialog;
//import com.hss01248.dialog.interfaces.MyDialogListener;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class IndexTabIntercatAdapter extends BaseMultiItemAdapter<PostDetail> {
//    OnChatLikeClickListener onChatLikeClickListener;
//    OnChatShareClickListener onChatShareClickListener;
//    public Map<String, Bitmap> bitmapList = new HashMap<>();
//    private Drawable likeNormal;
//    private Drawable likeSelect;
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
//    public IndexTabIntercatAdapter() {
//        this(R.layout.city_item_fragment_follow);
//        addItemType(1, R.layout.city_item_fragment_follow);//1:正常帖子
//        addItemType(2, R.layout.city_item_fragment_ad);//2:新品首发 3:种草清单 4:图文搭配 5:视频搭配 6:会员专享券
//        addItemType(3, R.layout.city_item_fragment_activity);//7 投票活动 8 报名活动
//    }
//
//    private IndexTabIntercatAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        return linearLayoutHelper;
//    }
//
//    @NotNull
//    @Override
//    public BaseHolder onCreateViewHolder(@NotNull ViewGroup p0, int p1) {
//        likeNormal = p0.getContext().getResources().getDrawable(R.drawable.cityrightlike);
//        likeSelect = p0.getContext().getResources().getDrawable(R.drawable.cityrightliker);
//        return super.onCreateViewHolder(p0, p1);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder helper, int position) {
//        final PostDetail item = getDatas().get(position);
//        if (getDefItemViewType(position) == 1) {
//            buildDefaultPost(helper, item);
//        } else if (getDefItemViewType(position) == 2) {
//            buildAdPost(helper, item, position);
//        } else if (getDefItemViewType(position) == 3) {
//            buildActivityPost(helper, item);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder holder, int position, @NonNull List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position);
//        } else {
//            String payload = (String) payloads.get(0);//虽然它是个list  但是这个列表最多只有一条数据
//            if (payload.equals("fans")) {//这里表示刷新关注按钮
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
//
//    /**
//     * 错误提示弹框
//     *
//     * @param msg
//     */
//    private void isAgree(String msg) {
//        StyledDialog.init(context);
//        StyledDialog.buildIosAlert(
//                "",
//                msg,
//                new MyDialogListener() {
//                    @Override
//                    public void onFirst() {
//                    }
//
//                    @Override
//                    public void onThird() {
//                        super.onThird();
//                    }
//
//                    @Override
//                    public void onSecond() {
//
//                    }
//                }).setMsgColor(R.color.color_da1818).setBtnColor(0, R.color.colorPrimaryDark, 0)
//                .setBtnText("确定")
//                .show();
//    }
//
//    /**
//     * 活动帖子
//     *
//     * @param holder
//     * @param postDetail
//     */
//    private void buildActivityPost(final BaseHolder holder, final PostDetail postDetail) {
//        TextView name = (TextView) holder.getView(R.id.name);
//        CornerImageView ivHeader = (CornerImageView) holder.getView(R.id.ivHeader);
//        FlexboxLayout tipTitle = holder.getView(R.id.tipTitle);
//        CollapsedTextView mTipContent = holder.getView(R.id.tipContent);
//        //活动信息 布局存放（投票活动、报名活动）
//        FrameLayout mActivityInfoLayout = holder.getView(R.id.activityInfo_layout);
//
//        TextView days = holder.getView(R.id.days);
//        ImageTextView tipShare = (ImageTextView) holder.getView(R.id.tipShare);
//        ImageTextView discuss = (ImageTextView) holder.getView(R.id.discuss);
//        final ImageTextView like = (ImageTextView) holder.getView(R.id.like);
//        days.setText(postDetail.createTimeStr);
//        if (postDetail.accountNickname != null
//                && !TextUtils.isEmpty(postDetail.accountNickname)) {
//            name.setText(postDetail.accountNickname);
//        } else {
//            name.setText("");
//        }
//
//        try {
//            if (postDetail.getPostingContent() != null && postDetail.getPostingContent().contains("\n")) {//说明纯文本 那就直接用文本接收
//                mTipContent.setText(StringUtils.noEndLnString(postDetail.getPostingContent()));
//            } else {
//                mTipContent.setText(Html.fromHtml(postDetail.getPostingContent()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //发帖人头像
//        Glide.with(context).load(postDetail.createUserFaceUrl).placeholder(R.drawable.img_1_1_default2)
//                .error(R.drawable.img_avatar_default).into(ivHeader);
//
//        if (postDetail.praiseState == 0) {
//            like.setDrawable(likeNormal);
//            like.setTextColor(Color.parseColor("#444444"));
//        } else {
//            like.setTextColor(Color.parseColor("#F93F60"));
//            like.setDrawable(likeSelect);
//        }
//
//        tipTitle.removeAllViews();
//        String sharetitle = "同城圈";
//        if (postDetail.topicList != null && postDetail.topicList.size() > 0) {
//            sharetitle = "";
//            for (int i = 0; i < (postDetail.topicList.size() >= 3 ? 3 : postDetail.topicList.size()); i++) {
//                View tipchild = LayoutInflater.from(context).inflate(R.layout.city_item_tip_single, tipTitle, false);
//                TextView tipname = tipchild.findViewById(R.id.tipSmall);
//                tipname.setText(postDetail.topicList.get(i).topicName);
//                final int finalI = i;
//                tipchild.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                    /*ARouter.getInstance()
//                            .build(CityRoutes.CITY_TIP)
//                            .withString("activitytype", "全国")
//                            .withString("topicId", postDetail.topicList.get(finalI).id + "")
//                            .navigation();*/
//                    }
//                });
//                sharetitle = sharetitle + "#" + tipname.getText().toString() + "#";
//                tipTitle.addView(tipchild);
//            }
//        }
//        if (postDetail.discussNum > 0) {
//            // 展示评论数量
//            discuss.setText(postDetail.discussNum == 0 ? "  " : postDetail.discussNum + "");
//        } else {
//            // 展示评论数量
//            discuss.setText("评论");
//        }
//        if (postDetail.praiseNum > 0) {
//            //展示点赞数量
//            like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
//        } else {
//            //展示点赞数量
//            like.setText("点赞");
//        }
//
//        //报名 or 投票按钮
//        TextView mTvAction = null;
//        String mBackUrl = "";
//        /** 投票活动信息 */
//        if (postDetail.postingType == 7) {
//            mActivityInfoLayout.removeAllViews();
//            mActivityInfoLayout.setVisibility(View.GONE);
//            View mView = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_activity_prize_layout, null);
//            GridLayout gridLayout = mView.findViewById(R.id.prize_child_gridLayout);
//            TextView mActivityTitle = mView.findViewById(R.id.prize_activity_title);
//            //报名 or 投票按钮
//            mTvAction = mView.findViewById(R.id.tv_action);
//            //时间按照 状态切换展示 未报名-> 显示报名时间 报名-> 显示投票时间
//            TextView mActivityTime = mView.findViewById(R.id.prize_child_activity_time);
//            ImageView mPrizeActivityImg = mView.findViewById(R.id.prize_activity_img);
//            ActivityModel activity = postDetail.activity;
//            if (activity != null) {
//                gridLayout.setVisibility(View.VISIBLE);
//                mActivityInfoLayout.setVisibility(View.VISIBLE);
//                mTvAction.setEnabled(true);
//                mActivityTitle.setText(activity.getName());
//                String mTvActionName = "我要报名";
//                String mTime = "";
//                long mEnlistEndTime = DateUtils.str2Long(activity.getEnlistEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
//                long mEnlistStartTime = DateUtils.str2Long(activity.getEnlistStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
//                long mVotingStartTime = DateUtils.str2Long(activity.getVotingStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
//                //已报名且报名时间还没结束
//                if (!activity.getSignUpStatus() && mEnlistEndTime > System.currentTimeMillis()) {
//                    mTime = "报名时间：" + DateUtils.getDateDay(activity.getEnlistStartTime(), "yyyy-MM-dd HH:mm:ss") + " - " +
//                            DateUtils.getDateDay(activity.getEnlistEndTime(), "yyyy-MM-dd HH:mm:ss");
//                } else {
//                    mTime = "投票时间：" + DateUtils.getDateDay(activity.getVotingStartTime(), "yyyy-MM-dd HH:mm:ss") + " - " +
//                            DateUtils.getDateDay(activity.getVotingEndTime(), "yyyy-MM-dd HH:mm:ss");
//                }
//                if (activity.getStatus() == 3) {
//                    mTvActionName = "已结束";
//                    mTvAction.setEnabled(false);
//                } else {
//                    if (mEnlistStartTime < System.currentTimeMillis()) {
//                        if (mEnlistEndTime > System.currentTimeMillis()) {//报名未结束
//                            if (activity.getSignUpStatus()) {
//                                mTvAction.setEnabled(false);
//                                mTvActionName = "已报名";
//                            }
//                        } else {
//                            if (mVotingStartTime < System.currentTimeMillis()) { //已开始投票
//                                if (activity.getSignUpStatus()) {
//                                    mTvActionName = "去拉票";
//                                } else {
//                                    mTvActionName = "我要投票";
//                                }
//                            } else {
//                                mTvActionName = "投票未开始";
//                                mTvAction.setEnabled(false);
//                            }
//                        }
//                    } else {
//                        mTvActionName = "活动未开始";
//                        mTvAction.setEnabled(false);
//                    }
//                }
//                mTvAction.setText(mTvActionName);
//
//                mActivityTime.setText(mTime);
//
//                if (!ListUtil.isEmpty(activity.getPrizeList())) {
//                    mBackUrl = activity.getBackgroundUrl();
//                    if (gridLayout != null) {
//                        /** 添加入奖品*/
//                        addPrizeView(gridLayout, activity.getPrizeList());
//                    }
//                } else {
//                    if (gridLayout != null) {
//                        gridLayout.setVisibility(View.GONE);
//                    }
//                }
//                Glide.with(context).load(mBackUrl).placeholder(R.drawable.img_1_1_default)
//                        .error(R.drawable.img_1_1_default).into(mPrizeActivityImg);
//            }
//            mActivityInfoLayout.addView(mView);
//        }
//
//        /** 报名活动信息 */
//        if (postDetail.postingType == 8) {
//            mActivityInfoLayout.removeAllViews();
//            mActivityInfoLayout.setVisibility(View.GONE);
//            View mView = LayoutInflater.from(context).inflate(R.layout.city_recyclerview_enlist_prize_layout, null);
//            TextView mActivityTitle = mView.findViewById(R.id.prize_activity_title);
//            //报名 or 投票按钮
//            mTvAction = mView.findViewById(R.id.tv_action);
//            //时间按照 状态切换展示 未报名-> 显示报名时间 报名-> 显示投票时间
//            TextView mActivityTime = mView.findViewById(R.id.prize_child_activity_time);
//            ImageView mPrizeActivityImg = mView.findViewById(R.id.prize_activity_img);
//            TextView mTvAddress = mView.findViewById(R.id.prize_child_activity_address);
//            EnlistActivityModel mEnListActivity = postDetail.enlistActivity;
//            if (mEnListActivity != null) {
//                mActivityInfoLayout.setVisibility(View.VISIBLE);
//                mBackUrl = mEnListActivity.getPhotoUrl();
//                String mTvActionName;
//                long mEnlistStartTime = DateUtils.str2Long(mEnListActivity.getEnlistStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
//                long mEnlistEndTime = DateUtils.str2Long(mEnListActivity.getEnlistEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
//                long mEndTime = DateUtils.str2Long(mEnListActivity.getEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
//
//                String mTime = "";
//                if (mEnlistEndTime > System.currentTimeMillis()) { // 报名未结束显示报名时间
//                    mTime = DateUtils.getDateDay(mEnListActivity.getEnlistStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + "-" +
//                            DateUtils.getDateDay(mEnListActivity.getEnlistEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
//                    mTvAction.setVisibility(View.VISIBLE);
//                } else {//否显示活动时间切按钮隐藏
//                    mTime = DateUtils.getDateDay(mEnListActivity.getStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + "-" +
//                            DateUtils.getDateDay(mEnListActivity.getEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
//                    mTvAction.setVisibility(View.GONE);
//                }
//
//                if (mEnlistStartTime > System.currentTimeMillis()) {
//                    mTvActionName = "报名还未开始";
//                    mTvAction.setEnabled(false);
//                } else {
//                    if (mEndTime < System.currentTimeMillis() || mEnListActivity.getStatus() == 2 || mEnListActivity.getStatus() == 3) {
//                        mTvAction.setVisibility(View.VISIBLE);
//                        mTvActionName = "已结束";
//                        mTvAction.setEnabled(false);
//                    } else {
//                        mTvActionName = "我要报名";
//                        mTvAction.setEnabled(true);
//                    }
//                }
//                mActivityTime.setText("时间：" + mTime);
//                mActivityTitle.setText(mEnListActivity.getName());
//                mTvAction.setText(mTvActionName);
//                mTvAddress.setText("地址：" + mEnListActivity.activityAddress());
//                Glide.with(context).load(mBackUrl).placeholder(R.drawable.img_1_1_default)
//                        .error(R.drawable.img_1_1_default).into(mPrizeActivityImg);
//            }
//            mActivityInfoLayout.addView(mView);
//        }
//
//        if (mTvAction != null) {
//            mTvAction.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String mUrl = "";
//                    String mHost = "";
//                    String mRouter = "";
//                    /** 投票活动按钮处理 */
//                    if (postDetail.postingType == 7 && postDetail.activity != null) {
//                        ActivityModel activity = postDetail.activity;
//                        String mTvActionName = ((TextView) v).getText().toString().trim();
//                        if ("已结束".equals(mTvActionName) || "已报名".equals(mTvActionName)) return;
//
//                        if ("去拉票".equals(mTvActionName)) {
//                            if (activity.getFreezeStatus() == -1) {
//                                //弹框提示冻结
//                                isAgree(TextUtils.isEmpty(activity.getFreezeReason()) ? "投票行为存在异常，已被发起方冻结处理" : activity.getFreezeReason());
//                                return;
//                            }
//                            SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
//                            seckShareDialog.setActivityDialog(4, 0, activity);
//                            seckShareDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "voteShare");
//                            return;
//                        }
//
//                        if ("我要报名".equals(mTvActionName)) {
//                            mRouter = "voteApply.html";
//                            if (!TextUtils.isEmpty(SpUtils.getValue(context, SpKey.TEST_IP)) && !ChannelUtil.isRealRelease()) {
//                                mHost = "http://192.168.10.181:8000/";
//                            } else {
//                                mHost = BuildConfig.BASE_URL;
//                            }
//                        }
//                        if ("我要投票".equals(mTvActionName)) {
//                            mRouter = "voteList.html";
//                            if (!TextUtils.isEmpty(SpUtils.getValue(context, SpKey.TEST_IP)) && !ChannelUtil.isRealRelease()) {
//                                mHost = "http://192.168.10.181:8000/";
//                            } else {
//                                mHost = BuildConfig.BASE_URL;
//                            }
//                        }
//                        mUrl = mHost + mRouter + "?id=" + activity.getId() + "&token=" + SpUtils.getValue(context, SpKey.TOKEN);
//                        ARouter.getInstance()
//                                .build(IndexRoutes.INDEX_WEBVIEWSINGLE)
//                                .withString("url", mUrl)
//                                .withString("title", activity.getName())
//                                .withBoolean("needfindcollect", false)
//                                .navigation();
//                    }
//
//                    /** 报名活动按钮处理 */
//                    if (postDetail.postingType == 8 && postDetail.enlistActivity != null) {
//                        ARouter.getInstance()
//                                .build(MineRoutes.MINE_ENLIST_DETAILS)
//                                .withString("id", postDetail.enlistActivity.getId())
//                                .navigation();
//                        return;
//                    }
//                }
//            });
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance()
//                        .build(CityRoutes.CITY_POSTDETAIL)
//                        .withString("id", postDetail.id)
//                        .withBoolean("isshowDiscuss", false)
//                        .navigation();
//            }
//        });
//
//        //分享
//        tipShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onChatShareClickListener != null) {
//                    String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_POSTURL);
//                    String url = String.format("%s?id=%s&scheme=HMMPostDetail&postId=%s&referral_code=%s", urlPrefix, postDetail.id, postDetail.id, SpUtils.getValue(context, SpKey.GETTOKEN));
//                    try {
//                        onChatShareClickListener.chatshareclick(view, url, com.healthy.library.utils.JsoupCopy.parse(postDetail.getPostingContent()).text(), "" /*finalSharetitle*/);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        //评论
//        discuss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ARouter.getInstance()
//                        .build(CityRoutes.CITY_POSTDETAIL)
//                        .withString("id", postDetail.id + "")
//                        .withBoolean("isshowDiscuss", true)
//                        .navigation();
//            }
//        });
//
//        //点赞
//        like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                if (onChatLikeClickListener != null) {
//                    onChatLikeClickListener.chatlikeclick(view, postDetail.id + "", postDetail.praiseState == 0 ? "0" : "1");
//                    postDetail.praiseState = postDetail.praiseState == 0 ? 1 : 0;
//                    postDetail.praiseNum = postDetail.praiseNum + (postDetail.praiseState == 0 ? -1 : 1);
//                    like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
//                    if (postDetail.praiseState == 0) {
//                        like.setDrawable(likeNormal);
//                        like.setTextColor(Color.parseColor("#444444"));
//                    } else {
//                        like.setTextColor(Color.parseColor("#F93F60"));
//                        like.setDrawable(likeSelect);
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 活动帖子 奖品项内容填充
//     *
//     * @param gridLayout
//     * @param prizeList
//     */
//    private void addPrizeView(final GridLayout gridLayout, final List<PrizeModel> prizeList) {
////        if (gridLayout.getChildCount() == 0) {
//        //清除奖品内容
//        gridLayout.removeAllViews();
//
//        gridLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                gridLayout.removeAllViews();
//                int row = prizeList.size() < 3 ? 1 : 2;
//                int mForSize = prizeList.size() > 6 ? 6 : prizeList.size();
//                int needfixzzz = 3 - (mForSize % 3 == 0 ? 3 : mForSize % 3);
//                int mMargin = (int) TransformUtil.dp2px(context, 80);
//                gridLayout.setRowCount(row);
//                gridLayout.setColumnCount(3);
//                int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3);
//                for (int i = 0; i < mForSize; i++) {
//                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                    params.width = w;
//                    View itemPrizeLayout = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_activity_prize_child, gridLayout, false);
//                    final ImageView mPrizeChildImg = itemPrizeLayout.findViewById(R.id.prize_child_img);
//                    TextView mPrizeChildTitle = itemPrizeLayout.findViewById(R.id.prize_child_title);
//                    TextView mPrizeChildCount = itemPrizeLayout.findViewById(R.id.prize_child_count);
//                    PrizeModel prizeModel = prizeList.get(i);
//
//                    String mGoodsImage;
//                    String mGoodsTitle;
//                    GoodsDetail goodsDto = prizeModel.getGoodsDto();
//                    if (goodsDto == null) {
//                        mGoodsImage = prizeModel.getGoodsImage();
//                        mGoodsTitle = prizeModel.getGoodsTitle();
//                    } else {
//                        mGoodsImage = goodsDto.goodsImage;
//                        mGoodsTitle = goodsDto.goodsTitle;
//                    }
//                    Glide.with(mPrizeChildImg.getContext()).load(mGoodsImage).centerCrop()
//                            .error(R.drawable.img_1_1_default).placeholder(R.drawable.img_1_1_default)
//                            .into(mPrizeChildImg);
//                    mPrizeChildTitle.setText(mGoodsTitle);
//                    mPrizeChildCount.setText(prizeModel.getName() + " " + prizeModel.getPrizeNum() + "人");
//
//                    gridLayout.addView(itemPrizeLayout, params);
//                }
//                if (needfixzzz > 0) {
//                    for (int i = 0; i < needfixzzz; i++) {
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                        params.width = w;
//                        View itemPrizeLayout = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_activity_prize_child, gridLayout, false);
//                        final ImageView mPrizeChildImg = itemPrizeLayout.findViewById(R.id.prize_child_img);
//                        TextView mPrizeChildTitle = itemPrizeLayout.findViewById(R.id.prize_child_title);
//                        TextView mPrizeChildCount = itemPrizeLayout.findViewById(R.id.prize_child_count);
//                        itemPrizeLayout.setVisibility(View.INVISIBLE);
//                        gridLayout.addView(itemPrizeLayout, params);
//                    }
//                }
//            }
//        });
////        }
//    }
//
//
//    private void buildAdPost(BaseHolder holder, final PostDetail postDetail, final int position) {
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
//        if (postDetail.accountNickname != null && !TextUtils.isEmpty(postDetail.accountNickname)) {
//            name.setText(postDetail.accountNickname);
//        } else {
//            name.setText("");
//        }
//        tipContent.setIsExpanded(false);//设置为不展开
//        Glide.with(context).asBitmap().load(postDetail.createUserFaceUrl)
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
//            if (postDetail.getPostingContent() != null) {
//                String content = "  " + StringUtils.noEndLnString(postDetail.getPostingContent());//加个空格 后面代替图标位置
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
//                tipContent.setText(Html.fromHtml(postDetail.getPostingContent()));
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
//    private void addCouponView(final GridLayout gridLayout, BaseHolder holder, final PostDetail postDetail) {
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
//    private void addImagesView(final GridLayout gridLayout, final BaseHolder holder, final PostDetail postDetail, final List<String> idList) {
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
//                        Glide.with(context)
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
//    private void addVideoView(final GridLayout gridLayout, BaseHolder holder, final PostDetail postDetail) {
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
//                ImageView adImg = view.findViewById(R.id.adImg);
//                ImageView isVideo = (ImageView) view.findViewById(R.id.isVideo);
//                TextView imgMark = (TextView) view.findViewById(R.id.imgMark);
//                ConstraintLayout videoGoodsLayout = (ConstraintLayout) view.findViewById(R.id.videoGoodsLayout);
//                CornerImageView goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
//                TextView goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
//                isVideo.setVisibility(View.VISIBLE);
//                try {
//                    Glide.with(context)
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
//                        Glide.with(context)
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
//    private void buildDefaultPost(final BaseHolder helper, final PostDetail item) {
//        CornerImageView ivHeader;
//        TextView name;
//        TextView days;
//        TextView status;
//        FlexboxLayout tipTitle;
//        TextView tipContent;
//        GridLayout seeImgs;
//        TextView tipAddress;
//        TextView toFollow;
//        ImageTextView tipShare;
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
//        toFollow = (TextView) helper.getView(R.id.toFollow);
//        tipShare = (ImageTextView) helper.getView(R.id.tipShare);
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
//            Glide.with(context)
//                    .asBitmap()
//                    .load(R.drawable.img_avatar_default)
//                    .placeholder(R.drawable.img_1_1_default2)
//                    .error(R.drawable.img_avatar_default)
//
//                    .into(ivHeader);
//        } else {
//            Glide.with(context)
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
//                        onChatShareClickListener.chatshareclick(view, url, com.healthy.library.utils.JsoupCopy.parse(item.getPostingContent()).text(), finalSharetitle);
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
//        //days.setText(DateUtils.getClassDatePost(item.createTime));
//        days.setText(item.createTimeStr);
//        if (item.focusStatus == 0) {//关注
//            toFollow.setText("关注");
//            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
//            toFollow.setCompoundDrawablePadding(9);
//
//        } else {
//            toFollow.setText("已关注");
//            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
//            toFollow.setCompoundDrawablePadding(9);
//        }
//        toFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (moutClickListener != null) {
//                    moutClickListener.outClick("focus", item);
//                    moutClickListener.outClick("position", helper.getLayoutPosition() - 4);
//                }
//            }
//        });
//        try {
//            if (item.getPostingContent() != null && item.getPostingContent().contains("\n")) {//说明纯文本 那就直接用文本接收
//                tipContent.setText(item.getPostingContent());
//            } else {
//                tipContent.setText(JsoupCopy.parse(item.getPostingContent()).text());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (item.discussNum > 0) {
//            // 展示评论数量
//            discuss.setText(item.discussNum == 0 ? "  " : item.discussNum + "");
//        } else {
//            // 展示评论数量
//            discuss.setText("评论");
//        }
//        if (item.praiseNum > 0) {
//            //展示点赞数量
//            like.setText(item.praiseNum == 0 ? "  " : item.praiseNum + "");
//        } else {
//            //展示点赞数量
//            like.setText("点赞");
//        }
//        List<String> videsurls = new ArrayList<>();
//        if (item.videoUrls != null) {
//            for (int j = 0; j < item.videoUrls.size(); j++) {
//                PostDetail.VideoUrl videoUrl = item.videoUrls.get(j);
//                videsurls.add(videoUrl.url);
//            }
//        }
//        List<String> imgUrls = new ArrayList<>();
//        if (item.imgUrls != null) {
//            for (int j = 0; j < item.imgUrls.size(); j++) {
//                PostDetail.ImageUrl videoUrl = item.imgUrls.get(j);
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
//                                Glide.with(context)
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
//
//}