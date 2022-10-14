package com.healthy.library.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.google.android.flexbox.FlexboxLayout;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.dialog.PkVotingDialog;
import com.healthy.library.model.ActivityModel;
import com.healthy.library.model.EnlistActivityModel;
import com.healthy.library.model.Fans;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.PkVotingActivityModel;
import com.healthy.library.model.PostActivityList;
import com.healthy.library.model.PostDetail;
import com.healthy.library.model.PrizeModel;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.JsoupCopy;
import com.healthy.library.utils.MMiniPass;
import com.healthy.library.utils.MediaFileUtil;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CollapsedTextView;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageSpanCentre;
import com.healthy.library.widget.ImageTextView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PostAdapter extends BaseMultiItemAdapter<PostDetail> implements DataStatisticsContract.View {
    private int mMargin;
    private int mCornerRadius;
    private boolean isDelete = false;
    //0 普通同城圈帖子列表 1 个人主页帖子
    private int postViewType = 0;
    public boolean isShowContent = false;
    private Drawable likeNormal;
    private Drawable likeSelect;
    private Drawable pkItemStyle;
    public Map<String, Bitmap> bitmapList = new HashMap<>();
    public List<QiYeWeXin> qiYeWeXins;


    public void setQiYeWeXins(List<QiYeWeXin> qiYeWeXins) {
        this.qiYeWeXins = qiYeWeXins;
    }

    public void setPostViewType(int postViewType) {
        this.postViewType = postViewType;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 设置首位帖子是否展示圆角
     */
    private boolean isTopRadius = false;
    /**
     * 设置帖子样式是否是搜索全部
     */
    private boolean isSearchAll = false;

    public SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();

    public void setTopRadius(boolean isTopRadius) {
        this.isTopRadius = isTopRadius;
    }

    public void setSearchAll(boolean isSearchAll) {
        this.isSearchAll = isSearchAll;
    }

    public void setFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
    }

    private String fragmentType;

    public void setOnPostFansClickListener(OnPostFansClickListener onPostFansClickListener) {
        this.onPostFansClickListener = onPostFansClickListener;
    }

    public void setOnPostLikeClickListener(OnPostLikeClickListener onPostLikeClickListener) {
        this.onPostLikeClickListener = onPostLikeClickListener;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public void setOnDeleteMainClickListener(OnDeleteMainClickListener onDeleteMainClickListener) {
        this.onDeleteMainClickListener = onDeleteMainClickListener;
    }

    public void setOnPostFansChangeListener(OnPostFansChangeListener onPostFansChangeListener) {
        this.onPostFansChangeListener = onPostFansChangeListener;
    }

    public void setOnPostFansAllListener(OnPostFansAllListener onPostFansAllListener) {
        this.onPostFansAllListener = onPostFansAllListener;
    }

    OnPostFansAllListener onPostFansAllListener;
    OnPostFansChangeListener onPostFansChangeListener;

    OnShareClickListener onShareClickListener;
    OnPostFansClickListener onPostFansClickListener;
    OnPostLikeClickListener onPostLikeClickListener;
    OnDeleteMainClickListener onDeleteMainClickListener;

    public PostAdapter() {
        this(R.layout.city_item_fragment_follow);
        addItemType(0, R.layout.city_item_fragment_follow_fan);//-1，主要展示关注的
        addItemType(1, R.layout.city_item_fragment_follow);//1:正常帖子
        addItemType(2, R.layout.city_item_fragment_ad);//2:新品首发 3:种草清单 4:图文搭配 5:视频搭配 6:会员专享券
        addItemType(3, R.layout.city_item_fragment_activity);//7 投票活动 8 报名活动
        addItemType(4, R.layout.city_item_fragment_pkvoting_activity);//9 投票PK活动
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return linearLayoutHelper;
    }

    private PostAdapter(int layoutResId) {
        super(layoutResId);

    }


    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    @NotNull
    @Override
    public BaseHolder onCreateViewHolder(@NotNull ViewGroup p0, int p1) {
        likeNormal = p0.getContext().getResources().getDrawable(R.drawable.cityrightlike);
        likeSelect = p0.getContext().getResources().getDrawable(R.drawable.cityrightliker);
        pkItemStyle = p0.getContext().getResources().getDrawable(R.drawable.shape_pk_time_style);
        return super.onCreateViewHolder(p0, p1);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final PostDetail postDetail = getDatas().get(position);
        if (getDefItemViewType(position) == 0) {
            buildFansPost(holder, postDetail);
        } else if (getDefItemViewType(position) == 1) {
            buildDefaultPost(holder, postDetail, position);
        } else if (getDefItemViewType(position) == 2) {
            buildAdPost(holder, postDetail, position);
        } else if (getDefItemViewType(position) == 3) {
            buildActivityPost(holder, postDetail, position);
        } else if (getDefItemViewType(position) == 4) {
            buildPkVotingPost(holder, postDetail, position);
        }
    }

    /**
     * 错误提示弹框
     *
     * @param msg
     */
    private void isAgree(String msg) {
        StyledDialog.init(context);
        StyledDialog.buildIosAlert(
                        "",
                        msg,
                        new MyDialogListener() {
                            @Override
                            public void onFirst() {
                            }

                            @Override
                            public void onThird() {
                                super.onThird();
                            }

                            @Override
                            public void onSecond() {

                            }
                        }).setMsgColor(R.color.color_da1818).setBtnColor(0, R.color.colorPrimaryDark, 0)
                .setBtnText("确定")
                .show();
    }

    /**
     * 关注数据
     *
     * @param holder
     * @param postDetail
     */
    private void buildFansPost(final BaseHolder holder, final PostDetail postDetail) {
        LinearLayout fansLL = (LinearLayout) holder.getView(R.id.fansLL);
        addFans(context, fansLL, postDetail.fans);
        LinearLayout llIndexRefresh = (LinearLayout) holder.getView(R.id.ll_index_refresh);
        TextView llIndexAllFan = holder.getView(R.id.ll_index_all_fan);
        TextView fansTitleLL = holder.getView(R.id.fansTitle);

        fansTitleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(CityRoutes.CITY_FANRECLIST)
                        .navigation();
            }
        });

        llIndexRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostFansChangeListener != null) {
                    onPostFansChangeListener.postfanschange(v);
                }
            }
        });

        llIndexAllFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostFansAllListener != null) {
                    onPostFansAllListener.postfansall(v);
                }
            }
        });
        LinearLayout recommandQWX = (LinearLayout) holder.getView(R.id.recommandQWX);
        buildQWXList(recommandQWX, qiYeWeXins);
    }

    public void buildQWXList(LinearLayout linearLayout, List<QiYeWeXin> qiYeWeXinWorkers) {
        linearLayout.removeAllViews();
        if (ListUtil.isEmpty(qiYeWeXinWorkers)) {
            return;
        }
        for (int i = 0; i < qiYeWeXinWorkers.size(); i++) {
            final QiYeWeXin qiYeWeXinWorker = qiYeWeXinWorkers.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.item_qwx_recommand_layout, linearLayout, false);
            TextView groupnName;
            TextView groupnSecondName;
            ShapeTextView groupnButtom;
            LinearLayout pinTitleLfet;
            LinearLayout pinHeadIconLL;
            TextView pinMenber;
            groupnName = (TextView) view.findViewById(R.id.groupnName);
            groupnSecondName = (TextView) view.findViewById(R.id.groupnSecondName);
            groupnButtom = (ShapeTextView) view.findViewById(R.id.groupnButtom);
            pinTitleLfet = (LinearLayout) view.findViewById(R.id.pinTitleLfet);
            pinHeadIconLL = (LinearLayout) view.findViewById(R.id.pinHeadIconLL);
            pinMenber = (TextView) view.findViewById(R.id.pinMenber);
            groupnName.setText(qiYeWeXinWorker.groupName);
            groupnSecondName.setText(qiYeWeXinWorker.groupIntroduce);
            List<String> mUrlList = qiYeWeXinWorker.faceUrlList;
            groupnButtom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MMiniPass.passMini("gh_f9b4fbd9d3b8", String.format("pages/mine/servicer/jionGroup?merchantId=%s&shopId=%s&workcode=%s&type=%s&groupId=%s"
                            , SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC)
                            , SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP)
                            , ""
                            , "3"
                            , qiYeWeXinWorker.id
                    ));
                }
            });
            if (!ListUtil.isEmpty(mUrlList)) {
                pinHeadIconLL.removeAllViews();
                int forSize = 3;
                if (forSize > mUrlList.size()) {//避免数组越界
                    forSize = mUrlList.size();
                }
                for (int j = 0; j < forSize; j++) {
                    CornerImageView cornerImageView = new CornerImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TransformUtil.dp2px(context, 16),
                            (int) TransformUtil.dp2px(context, 16));
                    cornerImageView.setIsCircle(true);
                    if (j > 0) {
                        layoutParams.leftMargin = -10;
                    }
                    cornerImageView.setLayoutParams(layoutParams);
                    GlideCopy.with(cornerImageView.getContext())
                            .load(mUrlList.get(j))
                            .error(R.drawable.img_avatar_default)
                            .into(cornerImageView);
                    pinHeadIconLL.addView(cornerImageView);
                }
            }
            linearLayout.addView(view);
        }
    }

    /**
     * 普通帖子
     *
     * @param holder
     * @param postDetail
     */
    private void buildDefaultPost(final BaseHolder holder, final PostDetail postDetail, final int position) {

        holder.setVisible(R.id.city_item_fragment_follow_head, postViewType == 0);
        holder.setVisible(R.id.heightView, isSearchAll);
        ViewGroup mPostContent = holder.getView(R.id.fl_post_content);
        Drawable mBackground = context.getResources().getDrawable(R.drawable.shape_chose);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) mPostContent.getLayoutParams();
        if (postDetail.isFirst) {
            if (isTopRadius) {
                mBackground = context.getResources().getDrawable(R.drawable.shape_chose_bottom_radius);
            } else {
                layoutParams.topMargin = layoutParams.bottomMargin;
            }
        } else {
            layoutParams.topMargin = 0;
        }
        if (isSearchAll) {
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = 0;
            if (position == getDatas().size() - 1) {
                holder.setVisible(R.id.heightView, false);
            }
        }
        mPostContent.setLayoutParams(layoutParams);
        mPostContent.setBackground(mBackground);

        boolean empty = TextUtils.isEmpty(postDetail.badgeId);
        holder.setVisible(R.id.iv_user_badge, !empty)
                .setVisible(R.id.stv_user_badgeName, !empty)
                .setText(R.id.stv_user_badgeName, postDetail.badgeName);
        ImageView mUserBadge = holder.getView(R.id.iv_user_badge);
        ShapeTextView mUserBadgeName = holder.getView(R.id.stv_user_badgeName);
        if (!empty) {
            if (postDetail.badgeId != mUserBadgeName.getTag()) {
                mUserBadgeName.setTag(postDetail.badgeId);
                ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
                if (0 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_certification);
                    shapeDrawableBuilder
                            .setSolidColor(0)
                            .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
                            .intoBackground();
                }
                if (1 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_official_certification);
                    shapeDrawableBuilder
                            .clearGradientColors();
                    shapeDrawableBuilder
                            .setSolidColor(Color.parseColor("#3889FD")).intoBackground();
                }
            }
        }

        CornerImageView ivHeader = (CornerImageView) holder.getView(R.id.ivHeader);
        TextView name = (TextView) holder.getView(R.id.name);
        TextView days = (TextView) holder.getView(R.id.days);
        TextView status = (TextView) holder.getView(R.id.status);
        FlexboxLayout tipTitle = (FlexboxLayout) holder.getView(R.id.tipTitle);
        CollapsedTextView mTipContent = (CollapsedTextView) holder.getView(R.id.tipContent);
        GridLayout seeImgs = (GridLayout) holder.getView(R.id.see_imgs);
        TextView tipAddress = (TextView) holder.getView(R.id.tipAddress);
        TextView toFollow = (TextView) holder.getView(R.id.toFollow);
        ImageTextView tipShare = (ImageTextView) holder.getView(R.id.tipShare);
        ImageTextView discuss = (ImageTextView) holder.getView(R.id.discuss);
        final ImageTextView like = (ImageTextView) holder.getView(R.id.like);
        View candelete = (View) holder.getView(R.id.candelete);
        View nameandstatus = (View) holder.getView(R.id.nameandstatus);

        mTipContent.setIsExpanded(false);
        mTipContent.setExpandedClick(new CollapsedTextView.ExpandedClick() {
            @Override
            public void click(boolean mIsExpanded) {
                postDetail.setShow(mIsExpanded);
            }
        });
        holder.setVisible(R.id.postingTitle, !TextUtils.isEmpty(postDetail.postingTitle))
                .setVisible(R.id.iv_essences, !ListUtil.isEmpty(postDetail.postingTagList))
                .setText(R.id.postingTitle, postDetail.postingTitle);
        ImageView mIvEssences = holder.getView(R.id.iv_essences);
        //精华帖按钮
        mIvEssences.setOnClickListener(v -> {
            if (!context.getClass().getSimpleName().equals("EssencesActivity")) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_ESSENCES)
                        .navigation();
            }
        });
        if (postDetail.memberId != null && postDetail.memberId.equals(new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {
            toFollow.setVisibility(View.GONE);
        } else {
            toFollow.setVisibility(View.VISIBLE);
        }
        String nickname = "";
        if (postDetail.anonymousStatus == 1) {
            nickname = "匿名用户";
        } else if (TextUtils.isEmpty(postDetail.accountNickname)) {
            nickname = "用户已注销";
        } else {
            nickname = postDetail.accountNickname;
        }
        name.setText(nickname);
        if (postDetail.anonymousStatus == 1) {
            GlideCopy.with(context)
                    .asBitmap()
                    .load(R.drawable.img_avatar_default)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)

                    .into(ivHeader);
        } else {
            GlideCopy.with(context)
                    .asBitmap()
                    .load(postDetail.createUserFaceUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)

                    .into(ivHeader);
        }
        if (TextUtils.isEmpty(postDetail.memberState)) {
            status.setVisibility(View.GONE);
        } else {
            status.setText(postDetail.memberState.replace("育儿期", ""));
            status.setVisibility(View.VISIBLE);
        }
        tipTitle.removeAllViews();
        tipTitle.setVisibility(View.GONE);
        String sharetitle = "同城圈";
        if (postDetail.topicList != null && postDetail.topicList.size() > 0) {
            tipTitle.setVisibility(View.VISIBLE);
            sharetitle = "";
            for (int i = 0; i < (postDetail.topicList.size() >= 3 ? 3 : postDetail.topicList.size()); i++) {
                View tipchild = LayoutInflater.from(context).inflate(R.layout.city_item_tip_single, tipTitle, false);
                TextView tipname = tipchild.findViewById(R.id.tipSmall);
                tipname.setText(postDetail.topicList.get(i).topicName);
//                tipname.setText("测试");
                final int finalI = i;
                tipchild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_TIP)
                                .withString("activitytype", "全国")
                                .withString("topicId", postDetail.topicList.get(finalI).id + "")
                                .navigation();
                    }
                });
                sharetitle = sharetitle + "#" + postDetail.topicList.get(i).topicName + "#";
                tipTitle.addView(tipchild);
            }
        }
        //candelete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
        tipAddress.setText(postDetail.postingAddress);
        if (TextUtils.isEmpty(postDetail.postingAddress)) {
            tipAddress.setVisibility(View.GONE);
        } else {
            tipAddress.setVisibility(View.VISIBLE);
        }
        if (postDetail.praiseState == 0) {
            like.setDrawable(likeNormal);
            like.setTextColor(Color.parseColor("#444444"));
        } else {
            like.setTextColor(Color.parseColor("#F93F60"));
            like.setDrawable(likeSelect);
        }
        final String finalNickname = nickname;
        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postDetail.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)
                        && (postDetail.postingType == 0 || postDetail.postingType == 1)) {
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType", postDetail.createSource + "")
                            .withString("memberId", postDetail.memberId + "")
                            .navigation();
                }
            }
        });
        nameandstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postDetail.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)
                        && (postDetail.postingType == 0 || postDetail.postingType == 1)) {
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType", postDetail.createSource + "")
                            .withString("memberId", postDetail.memberId + "")
                            .navigation();
                }

            }
        });
        candelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteMainClickListener != null) {
                    onDeleteMainClickListener.postdeleteclick(v, postDetail.id);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isDelete) {
                    if (onDeleteMainClickListener != null) {
                        onDeleteMainClickListener.postdeleteclick(view, postDetail.id);
                    }
                }
                return true;
            }
        });
        final String finalSharetitle = sharetitle;
        tipShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShareClickListener != null) {
                    String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_POSTURL);
                    String url = String.format("%s?id=%s&scheme=HMMPostDetail&postId=%s&referral_code=%s", urlPrefix, postDetail.id, postDetail.id, SpUtils.getValue(context, SpKey.GETTOKEN));
                    try {
                        onShareClickListener.postshareclick(view, url, JsoupCopy.parse(postDetail.getPostingContent()).text(), finalSharetitle, postDetail.id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onPostLikeClickListener != null) {
                    onPostLikeClickListener.postlikeclick(view, postDetail.id + "", postDetail.praiseState == 0 ? "0" : "1");
                    postDetail.praiseState = postDetail.praiseState == 0 ? 1 : 0;
                    postDetail.praiseNum = postDetail.praiseNum + (postDetail.praiseState == 0 ? -1 : 1);
                    like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
                    if (postDetail.praiseState == 0) {
                        like.setDrawable(likeNormal);
                        like.setTextColor(Color.parseColor("#444444"));
                    } else {
                        like.setTextColor(Color.parseColor("#F93F60"));
                        like.setDrawable(likeSelect);
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTDETAIL)
                        .withString("id", postDetail.id + "")
                        .withBoolean("isshowDiscuss", false)
                        .navigation();
            }
        });
        mTipContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTDETAIL)
                        .withString("id", postDetail.id)
                        .withBoolean("isshowDiscuss", false)
                        .navigation();
            }
        });
        discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTDETAIL)
                        .withString("id", postDetail.id + "")
                        .withBoolean("isshowDiscuss", true)
                        .navigation();
            }
        });
        //days.setText(DateUtils.getClassDatePost(postDetail.createTime));
        days.setText(postDetail.createTimeStr);
        if (postDetail.focusStatus == 0) {//关注
            toFollow.setText("关注");
            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
            toFollow.setCompoundDrawablePadding(9);

        } else {
            toFollow.setText("已关注");
            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
            toFollow.setCompoundDrawablePadding(9);
        }
        toFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPostFansClickListener != null) {
                    if (postDetail.focusStatus != 0) {
                        StyledDialog.init(context);
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
                                onPostFansClickListener.postfansclick(view, postDetail.memberId, postDetail.focusStatus == 0 ? "0" : "1", postDetail.createSource + "");
                                postDetail.focusStatus = postDetail.focusStatus == 0 ? 1 : 0;
                                if (postDetail.focusStatus == 0) {
                                    toFollow.setText("关注");
                                    toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
                                } else {
                                    toFollow.setText("已关注");
                                    toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
                                }
                                toFollow.setCompoundDrawablePadding(9);
                            }
                        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
                    } else {
                        onPostFansClickListener.postfansclick(view, postDetail.memberId, postDetail.focusStatus == 0 ? "0" : "1", postDetail.createSource + "");
                        postDetail.focusStatus = postDetail.focusStatus == 0 ? 1 : 0;
                        if (postDetail.focusStatus == 0) {
                            toFollow.setText("关注");
                            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
                        } else {
                            toFollow.setText("已关注");
                            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
                        }
                        toFollow.setCompoundDrawablePadding(9);
                    }
                }
            }
        });
        try {
            if (TextUtils.isEmpty(postDetail.getPostingContent()) && postDetail.getPostingContent().contains("\n")) {//说明纯文本 那就直接用文本接收
                mTipContent.setText(StringUtils.noEndLnString(postDetail.getPostingContent()));
            } else {
                if (!TextUtils.isEmpty(postDetail.postingRichContent)
                        && !postDetail.postingRichContent.contains("<img")
                        && !postDetail.postingRichContent.contains("<video")
                        && !postDetail.postingRichContent.contains("<iframe")) {
                    mTipContent.setText(Html.fromHtml(postDetail.postingRichContent));
                } else {
                    mTipContent.setText(postDetail.getPostingContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (postDetail.discussNum > 0) {
            // 展示评论数量
            discuss.setText(postDetail.discussNum == 0 ? "  " : postDetail.discussNum + "");
        } else {
            // 展示评论数量
            discuss.setText("评论");
        }
        if (postDetail.praiseNum > 0) {
            //展示点赞数量
            like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
        } else {
            //展示点赞数量
            like.setText("点赞");
        }

        /*if (postDetail.mViewGroup != null && postDetail.mViewGroup.getChildCount() > 0){
            return;
        }*/
        List<String> showImg = new ArrayList<>();
        showImg.clear();
        if (postDetail.videoUrls != null) {
            for (int j = 0; j < postDetail.videoUrls.size(); j++) {
                PostDetail.VideoUrl videoUrl = postDetail.videoUrls.get(j);
                showImg.add(videoUrl.url);
            }
        }
        if (postDetail.imgUrls != null) {
            for (int j = 0; j < postDetail.imgUrls.size(); j++) {
                PostDetail.ImageUrl videoUrl = postDetail.imgUrls.get(j);
                showImg.add(videoUrl.url);
            }
        }
        if (showImg.size() > 0) {
            seeImgs.setVisibility(View.VISIBLE);
        } else {
            seeImgs.setVisibility(View.GONE);
        }
        addImages(context, seeImgs, showImg, holder);
        //postDetail.mViewGroup = seeImgs;
    }

    /**
     * 活动帖子 种草 等等
     *
     * @param holder
     * @param postDetail
     * @param position
     */
    private void buildAdPost(BaseHolder holder, final PostDetail postDetail, final int position) {
        ViewGroup mPostContent = holder.getView(R.id.fl_post_content);
        holder.setVisible(R.id.heightView, isSearchAll);
        Drawable mBackground = context.getResources().getDrawable(R.drawable.shape_chose);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) mPostContent.getLayoutParams();
        if (postDetail.isFirst) {
            if (isTopRadius) {
                mBackground = context.getResources().getDrawable(R.drawable.shape_chose_bottom_radius);
            } else {
                layoutParams.topMargin = layoutParams.bottomMargin;
            }
        } else {
            layoutParams.topMargin = 0;
        }
        if (isSearchAll) {
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = 0;
            if (position == getDatas().size() - 1) {
                holder.setVisible(R.id.heightView, false);
            }
        }
        mPostContent.setLayoutParams(layoutParams);
        mPostContent.setBackground(mBackground);
        boolean empty = TextUtils.isEmpty(postDetail.badgeId);
        holder.setVisible(R.id.iv_user_badge, !empty)
                .setVisible(R.id.stv_user_badgeName, !empty)
                .setText(R.id.stv_user_badgeName, postDetail.badgeName);
        ImageView mUserBadge = holder.getView(R.id.iv_user_badge);
        ShapeTextView mUserBadgeName = holder.getView(R.id.stv_user_badgeName);
        if (!empty) {
            if (postDetail.badgeId != mUserBadgeName.getTag()) {
                mUserBadgeName.setTag(postDetail.badgeId);
                ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
                if (0 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_certification);
                    shapeDrawableBuilder
                            .setSolidColor(0)
                            .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
                            .intoBackground();
                }
                if (1 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_official_certification);
                    shapeDrawableBuilder
                            .clearGradientColors();
                    shapeDrawableBuilder
                            .setSolidColor(Color.parseColor("#3889FD")).intoBackground();
                }
            }
        }

        LinearLayout grassHead = (LinearLayout) holder.getView(R.id.grass_head);
        CornerImageView ivHeader = (CornerImageView) holder.getView(R.id.ivHeader);
        TextView name = (TextView) holder.getView(R.id.name);
        TextView days = (TextView) holder.getView(R.id.days);
        ImageView submit = (ImageView) holder.getView(R.id.submit);
        TextView mark = (TextView) holder.getView(R.id.mark);
        LinearLayout followChild = (LinearLayout) holder.getView(R.id.follow_child);
        View heightView = (View) holder.getView(R.id.heightView);
        CollapsedTextView tipContent = (CollapsedTextView) holder.getView(R.id.tipContent);
        GridLayout gridLayout = (GridLayout) holder.getView(R.id.see_imgs);

        holder.setVisible(R.id.postingTitle, !TextUtils.isEmpty(postDetail.postingTitle))
                .setVisible(R.id.iv_essences, !ListUtil.isEmpty(postDetail.postingTagList))
                .setText(R.id.postingTitle, postDetail.postingTitle);
        ImageView mIvEssences = holder.getView(R.id.iv_essences);
        //精华帖按钮
        mIvEssences.setOnClickListener(v -> {
            if (!context.getClass().getSimpleName().equals("EssencesActivity")) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_ESSENCES)
                        .navigation();
            }
        });
        if (postDetail.accountNickname != null && !TextUtils.isEmpty(postDetail.accountNickname)) {
            name.setText(postDetail.accountNickname);
        } else {
            name.setText("");
        }
        tipContent.setIsExpanded(false);//设置为不展开
        GlideCopy.with(context).asBitmap().load(postDetail.createUserFaceUrl)
                .placeholder(R.drawable.img_1_1_default2).error(R.drawable.img_avatar_default)
                .into(ivHeader);
        days.setText(postDetail.createTimeStr);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("submit", postDetail.id);
                }
            }
        });
        try {
            if (postDetail.getPostingContent() != null) {
                String content = "  " + StringUtils.noEndLnString(postDetail.getPostingContent());//加个空格 后面代替图标位置
                Drawable drawable = null;
                if (postDetail.postingType == 2) {
                    drawable = context.getResources().getDrawable(R.drawable.post_new_icon);
                } else if (postDetail.postingType == 3) {
                    drawable = context.getResources().getDrawable(R.drawable.post_grass_icon);
                } else if (postDetail.postingType == 4) {
                    drawable = context.getResources().getDrawable(R.drawable.post_collocation_icon);
                } else if (postDetail.postingType == 5) {
                    drawable = context.getResources().getDrawable(R.drawable.post_collocation_icon);
                } else if (postDetail.postingType == 6) {
                    drawable = context.getResources().getDrawable(R.drawable.post_coupon_icon);
                }
                SpannableString spannableString = new SpannableString(content);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpanCentre imageSpan = new ImageSpanCentre(drawable, ImageSpanCentre.CENTRE);
                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tipContent.setText(spannableString);
            } else {
                tipContent.setText(Html.fromHtml(postDetail.getPostingContent()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if (postDetail.mViewGroup != null && postDetail.mViewGroup.getChildCount() != 0) return;
        gridLayout.removeAllViews();
        if (postDetail.postingType == 5 && postDetail.videoUrls != null && postDetail.videoUrls.size() > 0) {
            addVideoView(gridLayout, holder, postDetail);//视频搭配
            final List<String> idList = new ArrayList<>();
            for (int i = 0; i < postDetail.videoUrls.size(); i++) {
                idList.add(postDetail.videoUrls.get(i).id);
            }
            gridLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(CityRoutes.CITY_ADPOSTDETIAL)
                            .withString("id", postDetail.id)
                            .withString("type", postDetail.postingType + "")
                            .withString("createUserFaceUrl", postDetail.createUserFaceUrl)
                            .withString("accountNickname", postDetail.accountNickname)
                            .withObject("idList", idList)
                            .navigation();
                }
            });
        } else if (postDetail.postingType == 4 || postDetail.postingType == 3 || postDetail.postingType == 2) {
            if (postDetail.imgUrls != null && postDetail.imgUrls.size() > 0) {
                final List<String> idList = new ArrayList<>();
                for (int i = 0; i < postDetail.imgUrls.size(); i++) {
                    idList.add(postDetail.imgUrls.get(i).id);
                }
                addImagesView(gridLayout, holder, postDetail, idList);//图文搭配
                gridLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build(CityRoutes.CITY_ADPOSTDETIAL)
                                .withString("id", postDetail.id)
                                .withString("type", postDetail.postingType + "")
                                .withString("createUserFaceUrl", postDetail.createUserFaceUrl)
                                .withString("accountNickname", postDetail.accountNickname)
                                .withObject("idList", idList)
                                .withInt("pos", 0)
                                .navigation();
                    }
                });
            }
        } else if (postDetail.postingType == 6) {
            if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {
                addCouponView(gridLayout, holder, postDetail);//优惠券
                gridLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moutClickListener != null) {
                            moutClickListener.outClick("coupon", postDetail.id);
                            //moutClickListener.outClick("position", position);
                        }
                    }
                });
            }
        } else {
            gridLayout.removeAllViews();
        }
        //postDetail.mViewGroup = gridLayout;
    }

    /**
     * 活动帖子
     *
     * @param holder
     * @param postDetail
     */
    private void buildActivityPost(final BaseHolder holder, final PostDetail postDetail, int position) {
        ViewGroup mPostContent = holder.getView(R.id.fl_post_content);
        holder.setVisible(R.id.heightView, isSearchAll);
        Drawable mBackground = context.getResources().getDrawable(R.drawable.shape_chose);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) mPostContent.getLayoutParams();
        if (postDetail.isFirst) {
            if (isTopRadius) {
                mBackground = context.getResources().getDrawable(R.drawable.shape_chose_bottom_radius);
            } else {
                layoutParams.topMargin = layoutParams.bottomMargin;
            }
        } else {
            layoutParams.topMargin = 0;
        }
        if (isSearchAll) {
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = 0;
            if (position == getDatas().size() - 1) {
                holder.setVisible(R.id.heightView, false);
            }
        }
        mPostContent.setLayoutParams(layoutParams);
        mPostContent.setBackground(mBackground);
        boolean empty = TextUtils.isEmpty(postDetail.badgeId);
        holder.setVisible(R.id.iv_user_badge, !empty)
                .setVisible(R.id.stv_user_badgeName, !empty)
                .setText(R.id.stv_user_badgeName, postDetail.badgeName);
        ImageView mUserBadge = holder.getView(R.id.iv_user_badge);
        ShapeTextView mUserBadgeName = holder.getView(R.id.stv_user_badgeName);
        if (!empty) {
            if (postDetail.badgeId != mUserBadgeName.getTag()) {
                mUserBadgeName.setTag(postDetail.badgeId);
                ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
                if (0 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_certification);
                    shapeDrawableBuilder
                            .setSolidColor(0)
                            .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
                            .intoBackground();
                }
                if (1 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_official_certification);
                    shapeDrawableBuilder
                            .clearGradientColors();
                    shapeDrawableBuilder
                            .setSolidColor(Color.parseColor("#3889FD")).intoBackground();
                }
            }
        }

        TextView name = (TextView) holder.getView(R.id.name);
        CornerImageView ivHeader = (CornerImageView) holder.getView(R.id.ivHeader);
        FlexboxLayout tipTitle = holder.getView(R.id.tipTitle);
        CollapsedTextView mTipContent = holder.getView(R.id.tipContent);
        mTipContent.setIsExpanded(false);//默认不展开
        mTipContent.setCurrentLine(0);
        //活动信息 布局存放（投票活动、报名活动）
        FrameLayout mActivityInfoLayout = holder.getView(R.id.activityInfo_layout);
        ImageTextView tipShare = (ImageTextView) holder.getView(R.id.tipShare);
        ImageTextView discuss = (ImageTextView) holder.getView(R.id.discuss);
        final ImageTextView like = (ImageTextView) holder.getView(R.id.like);

        holder.setVisible(R.id.postingTitle, !TextUtils.isEmpty(postDetail.postingTitle))
                .setVisible(R.id.iv_essences, !ListUtil.isEmpty(postDetail.postingTagList))
                .setText(R.id.postingTitle, postDetail.postingTitle);
        ImageView mIvEssences = holder.getView(R.id.iv_essences);
        //精华帖按钮
        mIvEssences.setOnClickListener(v -> {
            if (!context.getClass().getSimpleName().equals("EssencesActivity")) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_ESSENCES)
                        .navigation();
            }
        });

        TextView days = holder.getView(R.id.days);
        days.setText(postDetail.createTimeStr);
        if (postDetail.accountNickname != null
                && !TextUtils.isEmpty(postDetail.accountNickname)) {
            name.setText(postDetail.accountNickname);
        } else {
            name.setText("");
        }

        try {
            if (postDetail.getPostingContent() != null && postDetail.getPostingContent().contains("\n")) {//说明纯文本 那就直接用文本接收
                mTipContent.setText(StringUtils.noEndLnString(postDetail.getPostingContent()));
            } else {
                if (!TextUtils.isEmpty(postDetail.postingRichContent)) {
                    mTipContent.setText(Html.fromHtml(postDetail.postingRichContent));
                } else {
                    mTipContent.setText(Html.fromHtml(postDetail.getPostingContent()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //发帖人头像
        Glide.with(context).load(postDetail.createUserFaceUrl).placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default).into(ivHeader);

        if (postDetail.praiseState == 0) {
            like.setDrawable(likeNormal);
            like.setTextColor(Color.parseColor("#444444"));
        } else {
            like.setTextColor(Color.parseColor("#F93F60"));
            like.setDrawable(likeSelect);
        }

        tipTitle.removeAllViews();
        tipTitle.setVisibility(View.GONE);
        if (postDetail.topicList != null && postDetail.topicList.size() > 0) {
            tipTitle.setVisibility(View.VISIBLE);
            for (int i = 0; i < (postDetail.topicList.size() >= 3 ? 3 : postDetail.topicList.size()); i++) {
                View tipchild = LayoutInflater.from(context).inflate(R.layout.city_item_tip_single, tipTitle, false);
                TextView tipname = tipchild.findViewById(R.id.tipSmall);
                tipname.setText(postDetail.topicList.get(i).topicName);
                final int finalI = i;
                tipchild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    /*ARouter.getInstance()
                            .build(CityRoutes.CITY_TIP)
                            .withString("activitytype", "全国")
                            .withString("topicId", postDetail.topicList.get(finalI).id + "")
                            .navigation();*/
                    }
                });
                tipTitle.addView(tipchild);
            }
        }

        if (postDetail.discussNum > 0) {
            // 展示评论数量
            discuss.setText(postDetail.discussNum == 0 ? "  " : postDetail.discussNum + "");
        } else {
            // 展示评论数量
            discuss.setText("评论");
        }
        if (postDetail.praiseNum > 0) {
            //展示点赞数量
            like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
        } else {
            //展示点赞数量
            like.setText("点赞");
        }

        //报名 or 投票按钮
        TextView mTvAction = null;
        String mBackUrl = "";
        /** 投票活动信息 */
        if (postDetail.postingType == 7) {
            mActivityInfoLayout.removeAllViews();
            mActivityInfoLayout.setVisibility(View.GONE);
            View mView = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_activity_prize_layout, null);
            GridLayout gridLayout = mView.findViewById(R.id.prize_child_gridLayout);
            TextView mActivityTitle = mView.findViewById(R.id.prize_activity_title);
            //报名 or 投票按钮
            mTvAction = mView.findViewById(R.id.tv_action);
            //时间按照 状态切换展示 未报名-> 显示报名时间 报名-> 显示投票时间
            TextView mActivityTime = mView.findViewById(R.id.prize_child_activity_time);
            ImageView mPrizeActivityImg = mView.findViewById(R.id.prize_activity_img);
            ActivityModel activity = postDetail.activity;
            if (activity != null) {
                gridLayout.setVisibility(View.VISIBLE);
                mActivityInfoLayout.setVisibility(View.VISIBLE);
                mTvAction.setEnabled(true);
                mActivityTitle.setText(activity.getName());
                String mTvActionName = "我要报名";
                String mTime = "";
                long mEnlistEndTime = DateUtils.str2Long(activity.getEnlistEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                long mEnlistStartTime = DateUtils.str2Long(activity.getEnlistStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                long mVotingStartTime = DateUtils.str2Long(activity.getVotingStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                //已报名且报名时间还没结束
                if (!activity.getSignUpStatus() && mEnlistEndTime > System.currentTimeMillis()) {
                    mTime = "报名时间：" + DateUtils.getDateDay(activity.getEnlistStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + " - " +
                            DateUtils.getDateDay(activity.getEnlistEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
                } else {
                    mTime = "投票时间：" + DateUtils.getDateDay(activity.getVotingStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + " - " +
                            DateUtils.getDateDay(activity.getVotingEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
                }
                if (activity.getStatus() == 3) {
                    mTvActionName = "已结束";
                    mTvAction.setEnabled(false);
                } else {
                    if (mEnlistStartTime < System.currentTimeMillis()) {
                        if (mEnlistEndTime > System.currentTimeMillis()) {//报名未结束
                            if (activity.getSignUpStatus()) {
                                mTvAction.setEnabled(false);
                                mTvActionName = "已报名";
                            }
                        } else {
                            if (mVotingStartTime < System.currentTimeMillis()) { //已开始投票
                                if (activity.getSignUpStatus()) {
                                    mTvActionName = "去拉票";
                                } else {
                                    mTvActionName = "我要投票";
                                }
                            } else {
                                mTvActionName = "投票未开始";
                                mTvAction.setEnabled(false);
                            }
                        }
                    } else {
                        mTvActionName = "活动未开始";
                        mTvAction.setEnabled(false);
                    }
                }
                mTvAction.setText(mTvActionName);
                mActivityTime.setText(mTime);
                if (!ListUtil.isEmpty(activity.getPrizeList())) {
                    if (!TextUtils.isEmpty(activity.getBackgroundUrl())) {
                        if (activity.getBackgroundUrl().contains(",")) {
                            String[] split = activity.getBackgroundUrl().split(",");
                            mBackUrl = split[0];
                        } else {
                            mBackUrl = activity.getBackgroundUrl();
                        }
                    }
                    /** 添加入奖品*/
                    addPrizeView(gridLayout, activity.getPrizeList());
                } else {
                    gridLayout.setVisibility(View.GONE);
                }
                Glide.with(context).load(mBackUrl).placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default).into(mPrizeActivityImg);
            }
            mActivityInfoLayout.addView(mView);
        }

        /** 报名活动信息 */
        if (postDetail.postingType == 8) {
            mActivityInfoLayout.removeAllViews();
            mActivityInfoLayout.setVisibility(View.GONE);
            View mView = LayoutInflater.from(context).inflate(R.layout.city_recyclerview_enlist_prize_layout, null);
            TextView mActivityTitle = mView.findViewById(R.id.prize_activity_title);
            //报名 or 投票按钮
            mTvAction = mView.findViewById(R.id.tv_action);
            //时间按照 状态切换展示 未报名-> 显示报名时间 报名-> 显示投票时间
            TextView mActivityTime = mView.findViewById(R.id.prize_child_activity_time);
            ImageView mPrizeActivityImg = mView.findViewById(R.id.prize_activity_img);
            TextView mTvAddress = mView.findViewById(R.id.prize_child_activity_address);
            EnlistActivityModel mEnListActivity = postDetail.enlistActivity;
            if (mEnListActivity != null) {
                mActivityInfoLayout.setVisibility(View.VISIBLE);
                mBackUrl = mEnListActivity.getPhotoUrl();

                String mTvActionName;
                long mEnlistStartTime = DateUtils.str2Long(mEnListActivity.getEnlistStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                long mEnlistEndTime = DateUtils.str2Long(mEnListActivity.getEnlistEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                long mEndTime = DateUtils.str2Long(mEnListActivity.getEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);

                String mTime = "";
                if (mEnlistEndTime > System.currentTimeMillis()) { // 报名未结束显示报名时间
                    mTime = DateUtils.getDateDay(mEnListActivity.getEnlistStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + "-" +
                            DateUtils.getDateDay(mEnListActivity.getEnlistEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
                    mTvAction.setVisibility(View.VISIBLE);
                } else {//否显示活动时间切按钮隐藏
                    mTime = DateUtils.getDateDay(mEnListActivity.getStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + "-" +
                            DateUtils.getDateDay(mEnListActivity.getEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
                    mTvAction.setVisibility(View.GONE);
                }

                if (mEnlistStartTime > System.currentTimeMillis()) {
                    mTvActionName = "报名还未开始";
                    mTvAction.setEnabled(false);
                } else {
                    if (mEndTime < System.currentTimeMillis() || mEnListActivity.getStatus() == 2 || mEnListActivity.getStatus() == 3) {
                        mTvAction.setVisibility(View.VISIBLE);
                        mTvActionName = "已结束";
                        mTvAction.setEnabled(false);
                    } else {
                        mTvActionName = "我要报名";
                        mTvAction.setEnabled(true);
                    }
                }
                mActivityTime.setText("时间：" + mTime);
                mActivityTitle.setText(mEnListActivity.getName());
                mTvAction.setText(mTvActionName);
                mTvAddress.setText("地址：" + mEnListActivity.activityAddress());
                Glide.with(context).load(mBackUrl).placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default).into(mPrizeActivityImg);
            }
            mActivityInfoLayout.addView(mView);
        }

        mTipContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTDETAIL)
                        .withString("id", postDetail.id)
                        .withBoolean("isshowDiscuss", false)
                        .navigation();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTDETAIL)
                        .withString("id", postDetail.id)
                        .withBoolean("isshowDiscuss", false)
                        .navigation();
            }
        });

        if (mTvAction != null) {
            DataStatisticsPresenter dataStatisticsPresenter = new DataStatisticsPresenter(context, this);
            mTvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mUrl = "";
                    String mHost = "";
                    String mRouter = "";

                    /** 投票活动按钮处理 */
                    if (postDetail.postingType == 7 && postDetail.activity != null) {
                        ActivityModel activity = postDetail.activity;
                        String mTvActionName = ((TextView) v).getText().toString().trim();
                        if ("已结束".equals(mTvActionName) || "已报名".equals(mTvActionName)) return;

                        if ("去拉票".equals(mTvActionName)) {
                            if (activity.getFreezeStatus() == -1) {
                                //弹框提示冻结
                                isAgree(TextUtils.isEmpty(activity.getFreezeReason()) ? "投票行为存在异常，已被发起方冻结处理" : activity.getFreezeReason());
                                return;
                            }
                            SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
                            seckShareDialog.setActivityDialog(4, 0, activity);
                            seckShareDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "voteShare");
                            return;
                        }

                        if ("我要报名".equals(mTvActionName)) {
                            mHost = "http://192.168.10.181:8000/voteApply.html";
                            if (!TextUtils.isEmpty(SpUtils.getValue(context, UrlKeys.H5_VOTE_APPLY_URL))) {
                                mHost = SpUtils.getValue(context, UrlKeys.H5_VOTE_APPLY_URL);
                            }
                        }
                        if ("我要投票".equals(mTvActionName)) {
                            mHost = "http://192.168.10.181:8000/voteList.html";
                            if (!TextUtils.isEmpty(SpUtils.getValue(context, UrlKeys.H5_VOTE_LIST_URL))) {
                                mHost = SpUtils.getValue(context, UrlKeys.H5_VOTE_LIST_URL);
                            }
                        }
                        mUrl = mHost + "?id=" + activity.getId() + "&token=" + SpUtils.getValue(context, SpKey.TOKEN);
                        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", postDetail.id).puts("sourceType", 2).puts("type", 1));
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                                .withString("url", mUrl)
                                .withString("title", activity.getName())
                                .withBoolean("needfindcollect", false)
                                .navigation();
                    }

                    /** 报名活动按钮处理 */
                    if (postDetail.postingType == 8 && postDetail.enlistActivity != null) {
                        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", postDetail.id).puts("sourceType", 2).puts("type", 1));
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_ENLIST_DETAILS)
                                .withString("id", postDetail.enlistActivity.getId())
                                .navigation();
                        return;
                    }
                }
            });
        }
        String sharetitle = "";
        if (TextUtils.isEmpty(postDetail.postingTitle)) {
            sharetitle = "同城圈";
        } else {
            sharetitle = postDetail.postingTitle;
        }
        String finalSharetitle = sharetitle;
        //分享
        tipShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShareClickListener != null) {
                    String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_POSTURL);
                    String url = String.format("%s?id=%s&scheme=HMMPostDetail&postId=%s&referral_code=%s", urlPrefix, postDetail.id, postDetail.id, SpUtils.getValue(context, SpKey.GETTOKEN));
                    try {
                        onShareClickListener.postshareclick(view, url, JsoupCopy.parse(postDetail.getPostingContent()).text(), finalSharetitle, postDetail.id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //评论
        discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTDETAIL)
                        .withString("id", postDetail.id + "")
                        .withBoolean("isshowDiscuss", true)
                        .navigation();
            }
        });

        //点赞
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onPostLikeClickListener != null) {
                    onPostLikeClickListener.postlikeclick(view, postDetail.id + "", postDetail.praiseState == 0 ? "0" : "1");
                    postDetail.praiseState = postDetail.praiseState == 0 ? 1 : 0;
                    postDetail.praiseNum = postDetail.praiseNum + (postDetail.praiseState == 0 ? -1 : 1);
                    like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
                    if (postDetail.praiseState == 0) {
                        like.setDrawable(likeNormal);
                        like.setTextColor(Color.parseColor("#444444"));
                    } else {
                        like.setTextColor(Color.parseColor("#F93F60"));
                        like.setDrawable(likeSelect);
                    }
                }
            }
        });
    }

    /**
     * PK投票帖子
     *
     * @param holder
     * @param postDetail
     */
    private void buildPkVotingPost(final BaseHolder holder, final PostDetail postDetail, int position) {
        holder.setVisible(R.id.city_item_fragment_follow_head, postViewType == 0);
        holder.setVisible(R.id.heightView, isSearchAll);

        ViewGroup mPostContent = holder.getView(R.id.fl_post_content);
        Drawable mBackground = context.getResources().getDrawable(R.drawable.shape_chose);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) mPostContent.getLayoutParams();
        if (postDetail.isFirst) {
            if (isTopRadius) {
                mBackground = context.getResources().getDrawable(R.drawable.shape_chose_bottom_radius);
            } else {
                layoutParams.topMargin = layoutParams.bottomMargin;
            }
        } else {
            layoutParams.topMargin = 0;
        }
        if (isSearchAll) {
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = 0;
            if (position == getDatas().size() - 1) {
                holder.setVisible(R.id.heightView, false);
            }
        }
        mPostContent.setLayoutParams(layoutParams);
        mPostContent.setBackground(mBackground);
        /*---------------- 身份账号凸显 START ----------------*/
        boolean empty = TextUtils.isEmpty(postDetail.badgeId);
        holder.setVisible(R.id.iv_user_badge, !empty)
                .setVisible(R.id.stv_user_badgeName, !empty)
                .setText(R.id.stv_user_badgeName, postDetail.badgeName);
        ImageView mUserBadge = holder.getView(R.id.iv_user_badge);
        ShapeTextView mUserBadgeName = holder.getView(R.id.stv_user_badgeName);
        if (!empty) {
            if (postDetail.badgeId != mUserBadgeName.getTag()) {
                mUserBadgeName.setTag(postDetail.badgeId);
                ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
                if (0 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_certification);
                    shapeDrawableBuilder
                            .setSolidColor(0)
                            .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
                            .intoBackground();
                }
                if (1 == postDetail.badgeType) {
                    mUserBadge.setImageResource(R.drawable.icon_user_official_certification);
                    shapeDrawableBuilder
                            .clearGradientColors();
                    shapeDrawableBuilder
                            .setSolidColor(Color.parseColor("#3889FD")).intoBackground();
                }
            }
        }
        TextView name = (TextView) holder.getView(R.id.name);
        TextView days = (TextView) holder.getView(R.id.days);
        TextView status = (TextView) holder.getView(R.id.status);
        FlexboxLayout tipTitle = (FlexboxLayout) holder.getView(R.id.tipTitle);
        CornerImageView ivHeader = (CornerImageView) holder.getView(R.id.ivHeader);
        ViewGroup nameandstatus = holder.getView(R.id.nameandstatus);
        CollapsedTextView mTipContent = holder.getView(R.id.tipContent);
        mTipContent.setIsExpanded(false);//默认不展开
        mTipContent.setCurrentLine(0);
        ImageTextView tipShare = (ImageTextView) holder.getView(R.id.tipShare);
        ImageTextView discuss = (ImageTextView) holder.getView(R.id.discuss);
        final ImageTextView like = (ImageTextView) holder.getView(R.id.like);
        ViewGroup mPkVotingPopularity = holder.getView(R.id.ll_pkVoting_popularity);
        ViewGroup mFlPkVoting = holder.getView(R.id.fl_pkVoting);
        TextView mPkAction = holder.getView(R.id.pkVoting_action);
        TextView toFollow = (TextView) holder.getView(R.id.toFollow);
        /*---------------- 身份账号凸显 START ----------------*/

        int mVisibility = View.GONE;
        PkVotingActivityModel pkActivity = postDetail.pkActivity;
        if (pkActivity != null) {
            mVisibility = View.VISIBLE;
            mPkAction.setVisibility("1".equals(pkActivity.getStatus()) ? View.VISIBLE : View.GONE);
            buildPkVotingChild(holder, pkActivity);
        } else {
            mPkAction.setVisibility(mVisibility);
        }
        mPkVotingPopularity.setVisibility(mVisibility);
        mFlPkVoting.setVisibility(mVisibility);
        /*---------------- PK投票信息填充 END ----------------*/
        String nickname = "";
        if (postDetail.anonymousStatus == 1) {
            nickname = "匿名用户";
        } else if (TextUtils.isEmpty(postDetail.accountNickname)) {
            nickname = "用户已注销";
        } else {
            nickname = postDetail.accountNickname;
        }
        name.setText(nickname);
        status.setText(postDetail.createTimeStr);
        tipTitle.removeAllViews();
        tipTitle.setVisibility(View.GONE);
        if (!ListUtil.isEmpty(postDetail.topicList)) {
            tipTitle.setVisibility(View.VISIBLE);
            for (int i = 0; i < (postDetail.topicList.size() >= 3 ? 3 : postDetail.topicList.size()); i++) {
                View tipchild = LayoutInflater.from(context).inflate(R.layout.city_item_tip_single, tipTitle, false);
                TextView tipname = tipchild.findViewById(R.id.tipSmall);
                tipname.setText(postDetail.topicList.get(i).topicName);
//                tipname.setText("测试");
                final int finalI = i;
                tipchild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_TIP)
                                .withString("activitytype", "全国")
                                .withString("topicId", postDetail.topicList.get(finalI).id + "")
                                .navigation();
                    }
                });
                tipTitle.addView(tipchild);
            }
        }
        //发帖人头像
        Glide.with(context).load(postDetail.createUserFaceUrl).placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default).into(ivHeader);

        holder.setVisible(R.id.lineView, !ListUtil.isEmpty(postDetail.topicList))
                .setVisible(R.id.postingTitle, !TextUtils.isEmpty(postDetail.postingTitle))
                .setVisible(R.id.iv_essences, !ListUtil.isEmpty(postDetail.postingTagList))
                .setText(R.id.postingTitle, postDetail.postingTitle);
        ImageView mIvEssences = holder.getView(R.id.iv_essences);
        //精华帖按钮
        mIvEssences.setOnClickListener(v -> {
            if (!context.getClass().getSimpleName().equals("EssencesActivity")) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_ESSENCES)
                        .navigation();
            }
        });

        try {
            if (postDetail.getPostingContent() != null && postDetail.getPostingContent().contains("\n")) {//说明纯文本 那就直接用文本接收
                mTipContent.setText(StringUtils.noEndLnString(postDetail.getPostingContent()));
            } else {
                if (!TextUtils.isEmpty(postDetail.postingRichContent)) {
                    mTipContent.setText(Html.fromHtml(postDetail.postingRichContent));
                } else {
                    mTipContent.setText(Html.fromHtml(postDetail.getPostingContent()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (postDetail.discussNum > 0) {
            // 展示评论数量
            discuss.setText(postDetail.discussNum == 0 ? "  " : postDetail.discussNum + "");
        } else {
            // 展示评论数量
            discuss.setText("评论");
        }
        if (postDetail.praiseNum > 0) {
            //展示点赞数量
            like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
        } else {
            //展示点赞数量
            like.setText("点赞");
        }

        if (postDetail.praiseState == 0) {
            like.setDrawable(likeNormal);
            like.setTextColor(Color.parseColor("#444444"));
        } else {
            like.setTextColor(Color.parseColor("#F93F60"));
            like.setDrawable(likeSelect);
        }

        if (postDetail.focusStatus == 0) {//关注
            toFollow.setText("关注");
            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
            toFollow.setCompoundDrawablePadding(9);

        } else {
            toFollow.setText("已关注");
            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
            toFollow.setCompoundDrawablePadding(9);
        }
        toFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPostFansClickListener != null) {
                    if (postDetail.focusStatus != 0) {
                        StyledDialog.init(context);
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
                                onPostFansClickListener.postfansclick(view, postDetail.memberId, postDetail.focusStatus == 0 ? "0" : "1", postDetail.createSource + "");
                                postDetail.focusStatus = postDetail.focusStatus == 0 ? 1 : 0;
                                if (postDetail.focusStatus == 0) {
                                    toFollow.setText("关注");
                                    toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
                                } else {
                                    toFollow.setText("已关注");
                                    toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
                                }
                                toFollow.setCompoundDrawablePadding(9);
                            }
                        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
                    } else {
                        onPostFansClickListener.postfansclick(view, postDetail.memberId, postDetail.focusStatus == 0 ? "0" : "1", postDetail.createSource + "");
                        postDetail.focusStatus = postDetail.focusStatus == 0 ? 1 : 0;
                        if (postDetail.focusStatus == 0) {
                            toFollow.setText("关注");
                            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_tofollow), null, null, null);
                        } else {
                            toFollow.setText("已关注");
                            toFollow.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_star_isfollow), null, null, null);
                        }
                        toFollow.setCompoundDrawablePadding(9);
                    }
                }
            }
        });
        final String finalNickname = nickname;
        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postDetail.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)) {
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType", postDetail.createSource + "")
                            .withString("memberId", postDetail.memberId)
                            .navigation();
                }
            }
        });
        nameandstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postDetail.anonymousStatus != 1 && !"用户已注销".equals(finalNickname)) {
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType", postDetail.createSource + "")
                            .withString("memberId", postDetail.memberId)
                            .navigation();
                }

            }
        });

        mTipContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pkActivity == null) return;
                ARouter.getInstance()
                        .build(CityRoutes.CITY_PK_VOTING_DETAIL)
                        .withString("id", postDetail.id)
                        .navigation();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pkActivity == null) return;
                ARouter.getInstance()
                        .build(CityRoutes.CITY_PK_VOTING_DETAIL)
                        .withString("id", postDetail.id)
                        .navigation();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isDelete) {
                    if (onDeleteMainClickListener != null) {
                        onDeleteMainClickListener.postdeleteclick(v, postDetail.id);
                    }
                }
                return true;
            }
        });

        //分享
        tipShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context == null || postDetail == null) {
                    return;
                }
                if (moutClickListener != null) {
                    moutClickListener.outClick("sharePk", postDetail.id);
                }
                PkVotingDialog
                        .Companion.newInstance()
                        .setMerchantShopId(SpUtils.getValue(context, SpKey.CHOSE_MC), SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .setActivityObj(postDetail)
                        .show(((FragmentActivity) context).getSupportFragmentManager(), "pkVotingShareDialog");
            }
        });

        //评论
        discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pkActivity == null) return;
                ARouter.getInstance()
                        .build(CityRoutes.CITY_PK_VOTING_DETAIL)
                        .withString("id", postDetail.id)
                        .withBoolean("isShowDiscuss", true)
                        .navigation();
            }
        });

        //点赞
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onPostLikeClickListener != null) {
                    onPostLikeClickListener.postlikeclick(view, postDetail.id + "", postDetail.praiseState == 0 ? "0" : "1");
                    postDetail.praiseState = postDetail.praiseState == 0 ? 1 : 0;
                    postDetail.praiseNum = postDetail.praiseNum + (postDetail.praiseState == 0 ? -1 : 1);
                    like.setText(postDetail.praiseNum == 0 ? "  " : postDetail.praiseNum + "");
                    if (postDetail.praiseState == 0) {
                        like.setDrawable(likeNormal);
                        like.setTextColor(Color.parseColor("#444444"));
                    } else {
                        like.setTextColor(Color.parseColor("#F93F60"));
                        like.setDrawable(likeSelect);
                    }
                }
            }
        });
    }

    /**
     * PK 活动数据填充
     *
     * @param holder
     * @param activityModel
     */
    private void buildPkVotingChild(final BaseHolder holder, final PkVotingActivityModel activityModel) {

        //人气
        TextView mPopularity = holder.getView(R.id.pkVoting_popularity);
        //倒计时
        final AppCompatTextView mPkVotingTime = holder.getView(R.id.pkVoting_time);
        mPkVotingTime.setBackgroundColor(Color.TRANSPARENT);
        //activityModel.setTimeView(mPkVotingTime);
        ViewGroup mViewGroup = holder.getView(R.id.tabs);
        RelativeLayout mPkTitleLayout = holder.getView(R.id.pkVoting_title_layout);
        //正方
        ImageView mPkVotingSquare = holder.getView(R.id.iv_pkVoting_square);
        TextView mPkVotingSquareName = holder.getView(R.id.tv_pkVoting_square_name);
        //反方
        ImageView mPkVotingConSide = holder.getView(R.id.iv_pkVoting_conSide);
        TextView mPkVotingConSideName = holder.getView(R.id.tv_pkVoting_conSide_name);
        //只有纯文字时文字对应的pk图标
        ImageView mPkVotingTextVs = holder.getView(R.id.iv_pkVoting_text_vs);

        mPopularity.setText(ParseUtils.parseNumber(activityModel.getInitialPopularity(), 10000, "万") + "人气");
        mPkVotingSquareName.setText(activityModel.getSquareTitle());
        mPkVotingConSideName.setText(activityModel.getConSideTitle());

        long mActivityEndTime = DateUtils.str2Long(activityModel.getActivityEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);

        long time = (mActivityEndTime - System.currentTimeMillis()) / 1000;//转换为秒
        checkTimeOut(holder, mPkVotingTime, time);
        if ("1".equals(activityModel.getStatus()) && mActivityEndTime > System.currentTimeMillis()) {
            mPkVotingTime.setTypeface(Typeface.DEFAULT_BOLD);
            holder.setVisible(R.id.pkVoting_time_title, true);
            CountDownTimer countDownTimer = countDownCounters.get(mPkVotingTime.hashCode());
            if (time > 0) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer.start();
            } else {
                countDownTimer.cancel();
            }
        } else {
            countDownCounters.get(mPkVotingTime.hashCode()).cancel();
            mPkVotingTime.setText("活动已结束");
            mPkVotingTime.setTypeface(Typeface.DEFAULT);
            mPkVotingTime.setBackground(pkItemStyle);
            holder.setVisible(R.id.pkVoting_time_title, false);
        }

        mPkVotingSquareName.post(new Runnable() {
            @Override
            public void run() {
                if (mPkVotingSquareName.getLineCount() == 1) {
                    mPkVotingSquareName.setGravity(Gravity.CENTER);
                } else {
                    mPkVotingSquareName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                }
                if (mPkVotingConSideName.getLineCount() == 1) {
                    mPkVotingConSideName.setGravity(Gravity.CENTER);
                } else {
                    mPkVotingConSideName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                }
            }
        });
        ConstraintLayout.LayoutParams mLayoutParams = (ConstraintLayout.LayoutParams) mPkTitleLayout.getLayoutParams();

        if (TextUtils.isEmpty(activityModel.getSquareImgUrl()) || TextUtils.isEmpty(activityModel.getConSideImgUrl())) {
            mViewGroup.setVisibility(View.GONE);
            mPkVotingTextVs.setVisibility(View.VISIBLE);
            mLayoutParams.topMargin = (int) TransformUtil.dp2px(context, 36f);
        } else {
            mViewGroup.setVisibility(View.VISIBLE);
            mPkVotingTextVs.setVisibility(View.GONE);
            mLayoutParams.topMargin = 0;
            GlideCopy.with(context)
                    .load(activityModel.getSquareImgUrl())
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default)

                    .into(mPkVotingSquare);

            GlideCopy.with(context)
                    .load(activityModel.getConSideImgUrl())
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default)

                    .into(mPkVotingConSide);
        }
        mPkTitleLayout.setLayoutParams(mLayoutParams);
    }

    private void addFans(final Context context, final LinearLayout gridLayout, final List<Fans> urls) {
        gridLayout.removeAllViews();
        if (urls != null && urls.size() > 0) {
            for (int i = 0; i < urls.size(); i++) {

                final Fans item = urls.get(i);
                View imageparent = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_nofollow_focus, gridLayout, false);
                CornerImageView ivHeader;
                TextView name;
                TextView status;
                final TextView toFollow;
                View nameandstatus;
                ivHeader = (CornerImageView) imageparent.findViewById(R.id.ivHeader);
                name = (TextView) imageparent.findViewById(R.id.name);
                status = (TextView) imageparent.findViewById(R.id.status);
                toFollow = (TextView) imageparent.findViewById(R.id.toFollow);

                nameandstatus = (View) imageparent.findViewById(R.id.nameandstatus);

                ivHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_FANDETAIL)
                                .withString("memberId", item.friendId + "")
                                .withString("searchMemberType", item.friendType + "")
                                .navigation();
                    }
                });
                nameandstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_FANDETAIL)
                                .withString("memberId", item.friendId + "")
                                .withString("searchMemberType", item.friendType + "")
                                .navigation();
                    }
                });

                GlideCopy.with(context)
                        .asBitmap()
                        .load(item.faceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)

                        .into(ivHeader);
                name.setText(item.nickName);
                status.setText(item.currentStatus);
                if (TextUtils.isEmpty(item.currentStatus)) {
                    status.setVisibility(View.GONE);
                } else {
                    status.setVisibility(View.VISIBLE);
                }
                if (item.focusStatus == 0) {
                    toFollow.setText("关注");
                    toFollow.setTextColor(Color.parseColor("#ff29bda9"));
                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
                } else {
                    toFollow.setText("已关注");
                    toFollow.setTextColor(Color.parseColor("#9596A4"));
                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_no);
                }

                if (item.friendId != null && item.friendId.equals(new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {

                    toFollow.setVisibility(View.GONE);
                } else {
                    toFollow.setVisibility(View.VISIBLE);
                }
                toFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (onPostFansClickListener != null) {
                            if (item.focusStatus != 0) {
                                StyledDialog.init(context);
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
                                        onPostFansClickListener.postfansclick(view, item.friendId, item.focusStatus == 0 ? "0" : "1", item.friendType + "");
                                        item.focusStatus = item.focusStatus == 0 ? 1 : 0;
                                        if (item.focusStatus == 0) {
                                            toFollow.setText("关注");
                                            toFollow.setTextColor(Color.parseColor("#ff29bda9"));
                                            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
                                        } else {
                                            toFollow.setText("已关注");
                                            toFollow.setTextColor(Color.parseColor("#9596A4"));
                                            toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_no);
                                        }


                                    }
                                }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
                            } else {
                                onPostFansClickListener.postfansclick(view, item.friendId, item.focusStatus == 0 ? "0" : "1", item.friendType + "");
                                item.focusStatus = item.focusStatus == 0 ? 1 : 0;
                                if (item.focusStatus == 0) {
                                    toFollow.setText("关注");
                                    toFollow.setTextColor(Color.parseColor("#ff29bda9"));
                                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click);
                                } else {
                                    toFollow.setText("已关注");
                                    toFollow.setTextColor(Color.parseColor("#9596A4"));
                                    toFollow.setBackgroundResource(R.drawable.shape_city_nofollow_click_no);
                                }
                            }


                        }
                    }
                });
                gridLayout.addView(imageparent);
            }

        }
    }

    /**
     * 活动帖子 奖品项内容填充
     *
     * @param gridLayout
     * @param prizeList
     */
    private void addPrizeView(final GridLayout gridLayout, final List<PrizeModel> prizeList) {
//        if (gridLayout.getChildCount() == 0) {
        //清除奖品内容
        gridLayout.removeAllViews();

      /*  gridLayout.post(new Runnable() {
            @Override
            public void run() {*/
        int row = prizeList.size() < 3 ? 1 : 2;
        int mForSize = prizeList.size() > 6 ? 6 : prizeList.size();
        int needfixzzz = 3 - (mForSize % 3 == 0 ? 3 : mForSize % 3);
        int mMargin = (int) TransformUtil.dp2px(context, 98);
        gridLayout.setRowCount(row);
        gridLayout.setColumnCount(3);
        int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3);
        for (int i = 0; i < mForSize; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View itemPrizeLayout = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_activity_prize_child, gridLayout, false);
            CardView mCardView = itemPrizeLayout.findViewById(R.id.cardView);
            final ImageView mPrizeChildImg = itemPrizeLayout.findViewById(R.id.prize_child_img);
            TextView mPrizeChildTitle = itemPrizeLayout.findViewById(R.id.prize_child_title);
            TextView mPrizeChildCount = itemPrizeLayout.findViewById(R.id.prize_child_count);
            PrizeModel prizeModel = prizeList.get(i);
            String mGoodsImage;
            String mGoodsTitle;
            GoodsDetail goodsDto = prizeModel.getGoodsDto();
            if (goodsDto == null) {
                mGoodsImage = prizeModel.getGoodsImage();
                mGoodsTitle = prizeModel.getGoodsTitle();
            } else {
                mGoodsImage = goodsDto.goodsImage;
                mGoodsTitle = goodsDto.goodsTitle;
            }
            Glide.with(mPrizeChildImg.getContext()).load(mGoodsImage).centerCrop()
                    .error(R.drawable.img_1_1_default).placeholder(R.drawable.img_1_1_default)
                    .into(mPrizeChildImg);
            mPrizeChildTitle.setText(mGoodsTitle);
            mPrizeChildCount.setText(prizeModel.getName() + " " + prizeModel.getPrizeNum() + "人");
            ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
            layoutParams.width = (int) (w - TransformUtil.dp2px(context, 4f));
            mCardView.setLayoutParams(layoutParams);
            gridLayout.addView(itemPrizeLayout, params);
        }
                /*if (needfixzzz > 0) {
                    for (int i = 0; i < needfixzzz; i++) {
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = w;
                        View itemPrizeLayout = LayoutInflater.from(context).inflate(R.layout.city_item_fragment_activity_prize_child, gridLayout, false);
                        final ImageView mPrizeChildImg = itemPrizeLayout.findViewById(R.id.prize_child_img);
                        TextView mPrizeChildTitle = itemPrizeLayout.findViewById(R.id.prize_child_title);
                        TextView mPrizeChildCount = itemPrizeLayout.findViewById(R.id.prize_child_count);
                        itemPrizeLayout.setVisibility(View.INVISIBLE);
                        gridLayout.addView(itemPrizeLayout, params);
                    }
                }*/
         /*   }
        });*/
//        }
    }

    private void checkTimeOut(BaseHolder holder, TextView mPkVotingTime, long time) {
        CountDownTimer countDownTimer = countDownCounters.get(mPkVotingTime.hashCode());
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(time <= 0 ? 0 : (long) time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisUntilFinished = millisUntilFinished / 1000;
                int day = (int) millisUntilFinished / (24 * 60 * 60);
                int hours = (int) (millisUntilFinished / (60 * 60) - day * 24);
                int minutes = (((int) millisUntilFinished / 60) - day * 24 * 60 - hours * 60);
                int seconds = ((int) millisUntilFinished - day * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);
                String mTimeText;
                if (day > 0) {
                    mTimeText = String.format("%02d", Math.max(0, day)) + ":"
                            + String.format("%02d", Math.max(0, hours)) + ":"
                            + String.format("%02d", Math.max(0, minutes)) + ":"
                            + String.format("%02d", Math.max(0, seconds));
                } else {
                    mTimeText = String.format("%02d", Math.max(0, hours)) + ":"
                            + String.format("%02d", Math.max(0, minutes)) + ":"
                            + String.format("%02d", Math.max(0, seconds));
                }
                mPkVotingTime.setText(mTimeText);
                mPkVotingTime.setBackgroundColor(Color.TRANSPARENT);
                holder.setVisible(R.id.pkVoting_time_title, true);
            }

            @Override
            public void onFinish() {
                //剩余支付时间结束后进行相应逻辑处理
                mPkVotingTime.setText("活动已结束");
                mPkVotingTime.setTypeface(Typeface.DEFAULT);
                mPkVotingTime.setBackground(pkItemStyle);
                holder.setVisible(R.id.pkVoting_time_title, false);
            }
        };
        countDownCounters.put(mPkVotingTime.hashCode(), countDownTimer);
    }

    private void addCouponView(final GridLayout gridLayout, BaseHolder holder, final PostDetail postDetail) {
        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                gridLayout.removeAllViews();
                int row = 1;
                int needSize = postDetail.postActivityList.get(0).activityCoupon.size();
                if (needSize < 3) {
                    needSize = 2;
                    row = 1;
                } else {
                    needSize = 4;
                    row = 2;
                }
                gridLayout.setRowCount(row);
                int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight()) / 2;
                for (int i = 0; i < needSize; i++) {
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = w;
                    params.height = (int) TransformUtil.dp2px(context, 72f);
                    View view = LayoutInflater.from(context).inflate(R.layout.city_item_ad_coupon_layout, gridLayout, false);
                    ConstraintLayout couponLayout = (ConstraintLayout) view.findViewById(R.id.couponLayout);
                    TextView couponMoney = (TextView) view.findViewById(R.id.couponMoney);
                    TextView couponType = (TextView) view.findViewById(R.id.couponType);
                    TextView receive = (TextView) view.findViewById(R.id.receive);
                    ConstraintLayout moreLayout = (ConstraintLayout) view.findViewById(R.id.moreLayout);
                    ImageView moreImg = (ImageView) view.findViewById(R.id.moreImg);
                    TextView moreTitle = (TextView) view.findViewById(R.id.moreTitle);
                    if (i != needSize - 1 && postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {
                        PostActivityList.ActivityCoupon activityCoupon = null;
                        try {
                            activityCoupon = postDetail.postActivityList.get(0).activityCoupon.get(i);
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                        if (activityCoupon == null) {
                            return;
                        }
                        moreLayout.setVisibility(View.GONE);
                        couponLayout.setVisibility(View.VISIBLE);
                        if (activityCoupon != null) {
                            if (activityCoupon.isCanReceive()) {
                                couponLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_city_coupon_item_bg));
                                receive.setBackground(context.getResources().getDrawable(R.drawable.shape_city_item_coupon_receive));
                                couponMoney.setTextColor(Color.parseColor("#F08957"));
                                couponType.setTextColor(Color.parseColor("#EE9551"));
                                receive.setTextColor(Color.parseColor("#ffee9551"));
                                receive.setText("领取");
                            } else {
                                couponLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_city_coupon_item_invalid));
                                receive.setBackground(context.getResources().getDrawable(R.drawable.shape_city_item_coupon_invalid));
                                couponMoney.setTextColor(Color.parseColor("#999999"));
                                couponType.setTextColor(Color.parseColor("#999999"));
                                receive.setTextColor(Color.parseColor("#999999"));
                                if (activityCoupon.couponQuantity <= 0) {
                                    receive.setText("已领完");
                                } else {
                                    receive.setText("已领取");
                                }
                            }
                            if (FormatUtils.moneyKeep2Decimals(activityCoupon.denomination).length() >= 4) {
                                SpannableString spannableString = new SpannableString(FormatUtils.moneyKeep2Decimals(activityCoupon.denomination) + "元");
                                spannableString.setSpan(new AbsoluteSizeSpan(28, true), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableString.setSpan(new AbsoluteSizeSpan(12, true), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
                                spannableString.setSpan(new StyleSpan(Typeface.NORMAL), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
                                couponMoney.setText(spannableString);
                            } else {
                                SpannableString spannableString = new SpannableString(FormatUtils.moneyKeep2Decimals(activityCoupon.denomination) + "元");
                                spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableString.setSpan(new AbsoluteSizeSpan(12, true), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
                                spannableString.setSpan(new StyleSpan(Typeface.NORMAL), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
                                couponMoney.setText(spannableString);
                            }
                            couponType.setText(activityCoupon.getRequirement());
                        }
                    } else {
                        moreLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_city_coupon_item_bg));
                        moreLayout.setVisibility(View.VISIBLE);
                        couponLayout.setVisibility(View.GONE);
                    }
                    gridLayout.addView(view, params);
                }
            }
        });
    }

    private void addImagesView(final GridLayout gridLayout, final BaseHolder holder, final PostDetail postDetail, final List<String> idList) {
        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                gridLayout.removeAllViews();
                int row = 2;
                int needSize = postDetail.imgUrls.size();
                if (postDetail.imgUrls.size() == 1) {
                    needSize = 1;
                    row = 1;
                } else if (postDetail.imgUrls.size() == 3 || postDetail.imgUrls.size() == 2) {
                    needSize = 2;
                    row = 1;
                } else if (postDetail.imgUrls.size() > 3) {
                    needSize = 4;
                    row = 2;
                }
                gridLayout.setRowCount(row);
                int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight()) / 2;
                for (int i = 0; i < needSize; i++) {
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = w;
                    params.height = (int) TransformUtil.dp2px(context, 160f);
                    View view = LayoutInflater.from(context).inflate(R.layout.city_item_ad_image_layout, gridLayout, false);
                    ImageView adImg = view.findViewById(R.id.adImg);
                    ImageView isVideo = (ImageView) view.findViewById(R.id.isVideo);
                    TextView imgMark = (TextView) view.findViewById(R.id.imgMark);
                    ConstraintLayout videoGoodsLayout = (ConstraintLayout) view.findViewById(R.id.videoGoodsLayout);
                    CornerImageView goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
                    TextView goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
                    isVideo.setVisibility(View.GONE);
                    videoGoodsLayout.setVisibility(View.GONE);
                    try {
                        if (postDetail.imgUrls.get(i).annexUrl != null && !TextUtils.isEmpty(postDetail.imgUrls.get(i).url)) {
                            GlideCopy.with(context)
                                    .load(postDetail.imgUrls.get(i).annexUrl)
                                    .placeholder(R.drawable.img_1_1_default2)
                                    .error(R.drawable.img_1_1_default)

                                    .into(adImg);
                        } else {
                            GlideCopy.with(context)
                                    .load(postDetail.imgUrls.get(i).url)
                                    .placeholder(R.drawable.img_1_1_default2)
                                    .error(R.drawable.img_1_1_default)

                                    .into(adImg);
                        }

                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                    if (postDetail.imgUrls.size() == 3) {
                        if (i == 1) {
                            imgMark.setVisibility(View.VISIBLE);
                            imgMark.setText(postDetail.imgUrls.size() + "图");
                        } else {
                            imgMark.setVisibility(View.GONE);
                        }
                    } else if (postDetail.imgUrls.size() > 4) {
                        if (i == 3) {
                            imgMark.setVisibility(View.VISIBLE);
                            imgMark.setText(postDetail.imgUrls.size() + "图");
                        } else {
                            imgMark.setVisibility(View.GONE);
                        }
                    } else {
                        imgMark.setVisibility(View.GONE);
                    }
                    final int position = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance().build(CityRoutes.CITY_ADPOSTDETIAL)
                                    .withString("id", postDetail.id)
                                    .withString("type", postDetail.postingType + "")
                                    .withString("createUserFaceUrl", postDetail.createUserFaceUrl)
                                    .withString("accountNickname", postDetail.accountNickname)
                                    .withObject("idList", idList)
                                    .withInt("pos", position)
                                    .navigation();
                        }
                    });
                    gridLayout.addView(view, params);
                }
            }
        });
    }

    private void addVideoView(final GridLayout gridLayout, BaseHolder holder, final PostDetail postDetail) {
        /*gridLayout.post(new Runnable() {
            @Override
            public void run() {*/
        gridLayout.removeAllViews();
        int row = 1;
        gridLayout.setRowCount(row);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = (int) TransformUtil.dp2px(context, 327f);
        params.setMargins(mMargin, mMargin, mMargin, mMargin);
        View view = LayoutInflater.from(context).inflate(R.layout.city_item_ad_image_layout, gridLayout, false);
        ImageView adImg = view.findViewById(R.id.adImg);
        ImageView isVideo = (ImageView) view.findViewById(R.id.isVideo);
        TextView imgMark = (TextView) view.findViewById(R.id.imgMark);
        ConstraintLayout videoGoodsLayout = (ConstraintLayout) view.findViewById(R.id.videoGoodsLayout);
        CornerImageView goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
        TextView goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
        isVideo.setVisibility(View.VISIBLE);
        try {
            GlideCopy.with(context)
                    .load(postDetail.videoFramePicture)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(adImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {
            if (postDetail.postActivityList.get(0).newGoodsDTO != null) {
                videoGoodsLayout.setVisibility(View.VISIBLE);
                GlideCopy.with(context)
                        .load(postDetail.postActivityList.get(0).newGoodsDTO.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(goodsImg);
                goodsMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(postDetail.postActivityList.get(0).newGoodsDTO.platformPrice));
            } else {
                videoGoodsLayout.setVisibility(View.GONE);
            }
        } else {
            videoGoodsLayout.setVisibility(View.GONE);
        }
        gridLayout.addView(view, params);
          /*  }
        });*/
    }

    private void addImages(final Context context, final GridLayout gridLayout,
                           final List<String> urls, final BaseHolder holder) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(context, 2);
            mCornerRadius = (int) TransformUtil.dp2px(context, 5);
        }
        //System.out.println("展示分格");
        gridLayout.removeAllViews();
        if (urls != null && urls.size() > 0) {
          /*  gridLayout.postDelayed(new Runnable() {
                @Override
                public void run() {*/
            int row = 3;
            if (urls.size() == 1) {
                row = 1;
            }
            if (urls.size() == 2) {
                row = 2;
            }
            gridLayout.setRowCount(row);
            //int mWidth = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 40);
            //final int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
            //final int w = (ScreenUtils.getScreenWidth(context) - mWidth - (2 + 2 * (row - 1)) * mMargin) / row;
            final int w = (ScreenUtils.getScreenWidth(context) - (int) TransformUtil.dp2px(context, 60) - (2 + 2 * (row - 1)) * mMargin) / row;
            int needsize = urls.size();
            if (urls.size() >= 3) {
                needsize = 3;
            }
            for (int i = 0; i < needsize; i++) {
                final String url = urls.get(i);
                final int pos = i;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                if (row == 3) {
                    params.height = (int) TransformUtil.dp2px(context, 110f);
                } else if (row == 2) {
                    params.height = (int) TransformUtil.dp2px(context, 170f);
                } else {
                    params.width = (int) TransformUtil.dp2px(context, 220f);
                    params.height = (int) TransformUtil.dp2px(context, 220f);
                }
                params.setMargins(mMargin, mMargin, mMargin, mMargin);

                View imageparent = LayoutInflater.from(context).inflate(R.layout.city_item_normal_image, gridLayout, false);

                final CornerImageView imageView = imageparent.findViewById(R.id.iv_pic);
                imageView.setCornerRadius(mCornerRadius);
                final ImageView isVideo = imageparent.findViewById(R.id.isVideo);
                if (MediaFileUtil.isVideoFileType(url)) {
                    isVideo.setVisibility(View.VISIBLE);
                    isVideo.setVisibility(View.VISIBLE);
                    if (bitmapList.get(url) != null) {
                        imageView.setImageBitmap(bitmapList.get(url));
                        //System.out.println("已经有数据了");
                    } else {
                        imageView.setImageResource(R.drawable.img_1_1_default2);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = null;
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                try {
                                    //根据url获取缩略图
                                    retriever.setDataSource(url, new HashMap());
                                    //获得第一帧图片
                                    bitmap = retriever.getFrameAtTime();
                                    final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap, imageView.getWidth() + 10, imageView.getHeight());
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageView.setImageBitmap(finalBitmap);
                                            bitmapList.put(url, finalBitmap);
                                        }
                                    });
                                } catch (Exception e) {
//                                            e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } else {
                    isVideo.setVisibility(View.GONE);
                    try {
                        GlideCopy.with(context)
                                .load(url)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.img_1_1_default)

                                .into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                gridLayout.addView(imageparent, params);
                final String[] array = urls.toArray(new String[urls.size()]);
                /*final String[] array = new String[urls.size()];
                for (int j = 0; j < array.length; j++) {
                    array[j] = urls.get(j);
                }*/
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MediaFileUtil.isVideoFileType(url)) {
                            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                    .withCharSequenceArray("urls", array)
                                    .withInt("pos", pos)
                                    .navigation();
                        } else {
                            ARouter.getInstance()
                                    .build(LibraryRoutes.LIBRARY_VIDEOPLAYER)
                                    .withString("videoUrl", url)
                                    .navigation();
                        }

                    }
                });
            }
        }
         /*   }, 500);
        }*/
    }

    @Override
    public ObjectIteraor<PostDetail> getDuplicateObjectIterator() {//帖子去重
        return o -> o.id;
    }

    private void initView() {

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

    public interface OnPostFansClickListener {
        void postfansclick(View view, String friendId, String type, String frtype);
    }

    public interface OnPostLikeClickListener {
        void postlikeclick(View view, String postingId, String type);
    }

    public interface OnShareClickListener {
        void postshareclick(View view, String url, String des, String title, String postId);
    }

    /**
     * 个人主页帖子删除
     */
    public interface OnDeleteMainClickListener {
        void postdeleteclick(View view, String id);
    }

    /**
     * 关注页面 全部关注按钮
     */
    public interface OnPostFansAllListener {
        void postfansall(View view);
    }

    /**
     * 关注页面 更新数据
     */
    public interface OnPostFansChangeListener {
        void postfanschange(View view);
    }
}