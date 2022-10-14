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
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.google.android.flexbox.FlexboxLayout;
//import com.health.city.R;
//import com.healthy.library.base.MBaseQuickAdapter;
//import com.healthy.library.builder.ObjectIteraor;
//import com.healthy.library.businessutil.ListUtil;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.model.PostDetail;
//import com.healthy.library.routes.CityRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.utils.BitmapUtils;
//import com.healthy.library.utils.MediaFileUtil;
//import com.healthy.library.utils.SpUtils;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.ImageTextView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Li
// * @date 2019/03/25 11:03
// * @des 推荐阅读
// */
//
//public class PostMainAdapter extends MBaseQuickAdapter<PostDetail, BaseViewHolder> {
//    private int mMargin;
//    private int mCornerRadius;
//    public int mCurrentItem = 0;
//
//    public void setIsdelete(boolean isdelete) {
//        this.isdelete = isdelete;
//    }
//
//    private boolean isdelete = false;
//
//    public void setOnPostFansClickListener(OnPostMainFansClickListener onPostFansClickListener) {
//        this.onPostFansClickListener = onPostFansClickListener;
//    }
//
//    public void setOnPostLikeClickListener(OnPostMainLikeClickListener onPostLikeClickListener) {
//        this.onPostLikeClickListener = onPostLikeClickListener;
//    }
//
//    public void setOnShareClickListener(OnShareMainClickListener onShareClickListener) {
//        this.onShareClickListener = onShareClickListener;
//    }
//
//    OnShareMainClickListener onShareClickListener;
//    OnPostMainFansClickListener onPostFansClickListener;
//    OnPostMainLikeClickListener onPostLikeClickListener;
//
//    public void setOnDeleteMainClickListener(OnDeleteMainClickListener onDeleteMainClickListener) {
//        this.onDeleteMainClickListener = onDeleteMainClickListener;
//    }
//
//    OnDeleteMainClickListener onDeleteMainClickListener;
//
//    public PostMainAdapter() {
//        this(R.layout.city_item_fragment_usermain);
//    }
//
//    private PostMainAdapter(int layoutResId) {
//        super(layoutResId);
//
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, final PostDetail item) {
//
//        if (item == null) {
//            helper.getView(R.id.readlView).setVisibility(View.GONE);
//            helper.getView(R.id.noMsgCon).setVisibility(View.VISIBLE);
//            return;
//        }
//        helper.getView(R.id.readlView).setVisibility(View.VISIBLE);
//        helper.getView(R.id.noMsgCon).setVisibility(View.GONE);
//
//        helper.setGone(com.healthy.library.R.id.postingTitle, !TextUtils.isEmpty(item.postingTitle))
//                .setGone(com.healthy.library.R.id.iv_essences, !ListUtil.isEmpty(item.postingTagList))
//                .setText(com.healthy.library.R.id.postingTitle, item.postingTitle);
//        ImageView mIvEssences = helper.getView(com.healthy.library.R.id.iv_essences);
//        //精华帖按钮
//        mIvEssences.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mContext.getClass().getSimpleName().equals("EssencesActivity")) {
//                    ARouter.getInstance()
//                            .build(CityRoutes.CITY_ESSENCES)
//                            .navigation();
//                }
//            }
//        });
//        ViewGroup mPostContent = helper.getView(com.healthy.library.R.id.fl_post_content);
//        if (mCurrentItem == 0) {
//            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) mPostContent.getLayoutParams();
//            layoutParams.topMargin = layoutParams.bottomMargin;
//            mPostContent.setLayoutParams(layoutParams);
//            mCurrentItem++;
//        }
//
//        if (item == null) {
//            helper.getView(R.id.readlView).setVisibility(View.GONE);
//            helper.getView(R.id.noMsgCon).setVisibility(View.VISIBLE);
//            return;
//        } else {
//            helper.getView(R.id.readlView).setVisibility(View.VISIBLE);
//            helper.getView(R.id.noMsgCon).setVisibility(View.GONE);
//        }
//        LinearLayout cityItemFragmentFollowHead;
//        CornerImageView ivHeader;
//        TextView name;
//        TextView days;
//        TextView status;
////        final TextView toFollow;
//        LinearLayout cityItemFragmentFollowChild;
//        FlexboxLayout tipTitle;
//        TextView tipContent;
//        GridLayout seeImgs;
//        TextView tipAddress;
//        ImageTextView tipShare;
//        LinearLayout canlikediscuss;
//        ImageTextView discuss;
//        final ImageTextView like;
//        View dividerHeader;
//        View candelete;
//        View nameandstatus;
//        View iv_empty_stock;
//
//        iv_empty_stock = helper.getView(R.id.iv_empty_stock);
//        cityItemFragmentFollowHead = (LinearLayout) helper.getView(R.id.city_item_fragment_follow_head);
//        ivHeader = (CornerImageView) helper.getView(R.id.ivHeader);
//        name = (TextView) helper.getView(R.id.name);
//        days = (TextView) helper.getView(R.id.days);
//        status = (TextView) helper.getView(R.id.status);
////        toFollow = (TextView) helper.getView(R.id.toFollow);
//        cityItemFragmentFollowChild = (LinearLayout) helper.getView(R.id.city_item_fragment_follow_child);
//        tipTitle = (FlexboxLayout) helper.getView(R.id.tipTitle);
//        tipContent = (TextView) helper.getView(R.id.tipContent);
//        seeImgs = (GridLayout) helper.getView(R.id.see_imgs);
//        tipAddress = (TextView) helper.getView(R.id.tipAddress);
//        tipShare = (ImageTextView) helper.getView(R.id.tipShare);
//        canlikediscuss = (LinearLayout) helper.getView(R.id.canlikediscuss);
//        discuss = (ImageTextView) helper.getView(R.id.discuss);
//        like = (ImageTextView) helper.getView(R.id.like);
//        dividerHeader = (View) helper.getView(R.id.divider_header);
//        candelete = (View) helper.getView(R.id.candelete);
//        nameandstatus = (View) helper.getView(R.id.nameandstatus);
//
//        if (item.postingStatus == 1) {
//            iv_empty_stock.setVisibility(View.VISIBLE);
//        } else {
//            iv_empty_stock.setVisibility(View.GONE);
//
//        }
//
//        tipTitle.removeAllViews();
//        String sharetitle = "同城圈";
//        if (item.topicList != null && item.topicList.size() > 0) {
//            if (item.topicList != null && item.topicList.size() > 0) {
//                sharetitle = "";
//                for (int i = 0; i < (item.topicList.size() >= 3 ? 3 : item.topicList.size()); i++) {
//                    View tipchild = LayoutInflater.from(mContext).inflate(R.layout.city_item_tip_single, tipTitle, false);
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
//        }
//        candelete.setVisibility(View.GONE);
//        candelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onDeleteMainClickListener != null) {
//                    onDeleteMainClickListener.postdeleteclick(view, item.id + "");
//                }
//            }
//        });
//        tipAddress.setText(item.postingAddress);
//        if (TextUtils.isEmpty(item.postingAddress)) {
//            tipAddress.setVisibility(View.GONE);
//        } else {
//            tipAddress.setVisibility(View.VISIBLE);
//        }
////        final Drawable followNormal = mContext.getResources().getDrawable(R.drawable.ic_star_tofollow);
////        final Drawable followSelect = mContext.getResources().getDrawable(R.drawable.ic_star_isfollow);
//
//
//        final String finalSharetitle = sharetitle;
//        tipShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onShareClickListener != null) {
//                    String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_POSTURL);
//                    String url = String.format("%s?id=%s", urlPrefix, item.id);
//                    try {
//                        onShareClickListener.postshareclick(view, url, com.healthy.library.utils.JsoupCopy.parse(item.getPostingContent()).text(), finalSharetitle, item.id);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//        final Drawable likeNormal = mContext.getResources().getDrawable(R.drawable.cityrightlike);
//        final Drawable likeSelect = mContext.getResources().getDrawable(R.drawable.cityrightliker);
//        if (item.praiseState == 0) {
//            like.setDrawable(likeNormal);
//            like.setTextColor(Color.parseColor("#444444"));
//        } else {
//            like.setTextColor(Color.parseColor("#F93F60"));
//            like.setDrawable(likeSelect);
//        }
//
//        like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onPostLikeClickListener != null) {
//                    onPostLikeClickListener.postlikeclick(view, item.id + "", item.praiseState == 0 ? "0" : "1");
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
//                }
//            }
//        });
//
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
//        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (isdelete) {
//                    if (onDeleteMainClickListener != null) {
//                        onDeleteMainClickListener.postdeleteclick(view, item.id + "");
//                    }
//                }
//                return false;
//            }
//        });
//        try {
//            if (item.getPostingContent() != null && item.getPostingContent().contains("\n")) {//说明纯文本 那就直接用文本接收
//                tipContent.setText(StringUtils.noEndLnString(item.getPostingContent()));
//            } else {
//                tipContent.setText(Html.fromHtml(item.getPostingContent()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        discuss.setText(item.discussNum == 0 ? "评论" : item.discussNum + "");
//        like.setText(item.praiseNum == 0 ? "点赞" : item.praiseNum + "");
//        List<String> showImg = new ArrayList<>();
//        showImg.clear();
//
//        if (item.videoUrls != null) {
//            for (int j = 0; j < item.videoUrls.size(); j++) {
//                PostDetail.VideoUrl videoUrl = item.videoUrls.get(j);
//                showImg.add(videoUrl.url);
//            }
//        }
//        if (item.imgUrls != null) {
//            for (int j = 0; j < item.imgUrls.size(); j++) {
//                PostDetail.ImageUrl videoUrl = item.imgUrls.get(j);
//                showImg.add(videoUrl.url);
//            }
//        }
//
//        if (showImg.size() > 0) {
//            seeImgs.setVisibility(View.VISIBLE);
//        } else {
//
//            seeImgs.setVisibility(View.GONE);
//        }
//        addImages(mContext, seeImgs, showImg);
//    }
//
//    public Map<String, Object> picmap = new HashMap<>();
//
//    private void addImages(final Context context, final GridLayout gridLayout, final List<String> urls) {
//        if (mMargin == 0) {
//            mMargin = (int) TransformUtil.dp2px(mContext, 2);
//            mCornerRadius = (int) TransformUtil.dp2px(mContext, 3);
//        }
//        ////System.out.println("展示分格");
//        gridLayout.removeAllViews();
//        if (urls != null && urls.size() > 0) {
//            gridLayout.post(new Runnable() {
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
//                        params.height = (int) TransformUtil.dp2px(context, 100f);
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
//                        View imageparent = LayoutInflater.from(mContext).inflate(R.layout.city_item_normal_image, gridLayout, false);
//
//                        final CornerImageView imageView = imageparent.findViewById(R.id.iv_pic);
//                        imageView.setCornerRadius(mCornerRadius);
//                        final ImageView isVideo = imageparent.findViewById(R.id.isVideo);
//                        if (MediaFileUtil.isVideoFileType(url)) {
//                            isVideo.setVisibility(View.VISIBLE);
//                            imageView.setImageResource(R.drawable.img_1_1_default);
//                            if (picmap.get(url) != null) {
//                                imageView.setImageBitmap((Bitmap) picmap.get(url));
//                            } else {
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
//                                            ((Activity) mContext).runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    picmap.put(url, finalBitmap);
//                                                    imageView.setImageBitmap(finalBitmap);
//                                                }
//                                            });
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                            }
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
//            });
//        }
//
//
//    }
//
//    public interface OnPostMainFansClickListener {
//        void postfansclick(View view, String friendId, String type, String frtype);
//    }
//
//    public interface OnPostMainLikeClickListener {
//        void postlikeclick(View view, String postingId, String type);
//    }
//
//
//    public interface OnShareMainClickListener {
//        void postshareclick(View view, String url, String des, String title, String postId);
//    }
//
//    public interface OnDeleteMainClickListener {
//
//        void postdeleteclick(View view, String id);
//    }
//    @Override
//    public ObjectIteraor<PostDetail> getDuplicateObjectIterator() {//帖子去重
//
//        return o -> o.id;
//    }
//}
