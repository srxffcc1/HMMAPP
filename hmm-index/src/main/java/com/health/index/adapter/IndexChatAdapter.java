//package com.health.index.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.media.MediaMetadataRetriever;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.bumptech.glide.Glide;
//import com.health.index.R;
//import com.health.index.model.IndexAllChat;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.routes.CityRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.utils.BitmapUtils;
//import com.healthy.library.utils.JsoupCopy;
//import com.healthy.library.utils.MediaFileUtil;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.CornerImageView;
//import com.ksyun.player.now.activity.VodDisplayActivity;
//import com.ksyun.player.now.bean.VodBean;
//import com.ksyun.player.now.model.FloatingPlayer;
//import com.ksyun.player.now.utils.Ids;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class IndexChatAdapter extends BaseAdapter<IndexAllChat> {
//    OnChatLikeClickListener onChatLikeClickListener;
//
//    public void setOnChatLikeClickListener(OnChatLikeClickListener onChatLikeClickListener) {
//        this.onChatLikeClickListener = onChatLikeClickListener;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 1;
//    }
//
//    public IndexChatAdapter() {
//        this(R.layout.index_mon_chat);
//    }
//    private IndexChatAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        LinearLayoutHelper linearLayoutHelper=new LinearLayoutHelper();
////        linearLayoutHelper.setBgColor(Color.parseColor("#F5F5F9"));
////        linearLayoutHelper.setMarginBottom((int)TransformUtil.dp2px(context, 15f));
//        return linearLayoutHelper;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
//        final IndexAllChat indexAllChat= getDatas().get(i);
////        try {
////            baseHolder.setText(R.id.see_content, com.healthy.library.utils.JsoupCopy.parse(indexAllChat.posting.postingContent).text());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        try {
//            if(indexAllChat.posting.postingContent!=null&&indexAllChat.posting.postingContent.contains("\n")){//说明纯文本 那就直接用文本接收
//                baseHolder.setText(R.id.see_content, indexAllChat.posting.postingContent);
//            }else {
//                baseHolder.setText(R.id.see_content, JsoupCopy.parse(indexAllChat.posting.postingContent).text());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        baseHolder.setText(R.id.see_title,indexAllChat.topicName);
//        baseHolder.setText(R.id.likecount,indexAllChat.partInNum+"人正在参与");
//        baseHolder.setText(R.id.discuss,indexAllChat.posting.discussNum+"");
//        baseHolder.setText(R.id.like,indexAllChat.posting.praiseNum==0?"  ":indexAllChat.posting.praiseNum+"");
//
//        TextView seeT=baseHolder.getView(R.id.see_title);
//
//        TextView discuss=baseHolder.getView(R.id.discuss);
//        final TextView like=baseHolder.getView(R.id.like);
//
//        final Drawable likeNormal = context.getResources().getDrawable(R.drawable.ic_like);
//        final Drawable likeSelect = context.getResources().getDrawable(R.drawable.ic_like_red);
//
//        if(indexAllChat.posting.praiseState==0){
//            like.setCompoundDrawablesWithIntrinsicBounds(likeNormal,null,null,null);
//            like.setCompoundDrawablePadding(5);
//        }else {
//            like.setCompoundDrawablesWithIntrinsicBounds(likeSelect,null,null,null);
//            like.setCompoundDrawablePadding(5);
//        }
//        ImageView head_icon1=baseHolder.getView(R.id.head_icon1);
//        ImageView head_icon2=baseHolder.getView(R.id.head_icon2);
//        ImageView head_icon3=baseHolder.getView(R.id.head_icon3);
//        ImageView head_icon4=baseHolder.getView(R.id.head_icon4);
//        head_icon4.setVisibility(View.GONE);
//        head_icon3.setVisibility(View.GONE);
//        head_icon2.setVisibility(View.GONE);
//        head_icon1.setVisibility(View.GONE);
//        if(indexAllChat.posting.praiseMemberList!=null){
//            for (int j = 0; j <indexAllChat.posting.praiseMemberList.size() ; j++) {
//                IndexAllChat.PraiseMember praiseMember=indexAllChat.posting.praiseMemberList.get(j);
//                if(j==0){
//                    head_icon4.setVisibility(View.VISIBLE);
//                    com.healthy.library.businessutil.GlideCopy.with(context)
//                            .load(praiseMember.faceUrl)
//                            .placeholder(R.drawable.img_1_1_default2)
//                            .error(R.drawable.ic_sex_women)
//                            
//                            .into(head_icon4);
//                }
//                if(j==1){
//                    head_icon3.setVisibility(View.VISIBLE);
//                    com.healthy.library.businessutil.GlideCopy.with(context)
//                            .load(praiseMember.faceUrl)
//                            .placeholder(R.drawable.img_1_1_default2)
//                            .error(R.drawable.ic_sex_women)
//                            
//                            .into(head_icon3);
//                }
//                if(j==2){
//                    head_icon2.setVisibility(View.VISIBLE);
//                    com.healthy.library.businessutil.GlideCopy.with(context)
//                            .load(praiseMember.faceUrl)
//                            .placeholder(R.drawable.img_1_1_default2)
//                            .error(R.drawable.ic_sex_women)
//                            
//                            .into(head_icon2);
//                }
//                if(j==3){
//                    head_icon1.setVisibility(View.VISIBLE);
//                    com.healthy.library.businessutil.GlideCopy.with(context)
//                            .load(praiseMember.faceUrl)
//                            .placeholder(R.drawable.img_1_1_default2)
//                            .error(R.drawable.ic_sex_women)
//                            
//                            .into(head_icon1);
//                }
//            }
//        }
//
//
//
//        Drawable chatRed = context.getResources().getDrawable(R.drawable.ic_post_red);
//        Drawable chatYellow = context.getResources().getDrawable(R.drawable.ic_post_yellow);
//        Drawable chatGreen = context.getResources().getDrawable(R.drawable.ic_post_green);
//        Drawable chatBlue = context.getResources().getDrawable(R.drawable.ic_post_blue);
//        if(i==0){
//            seeT.setTextColor(Color.parseColor("#E26376"));
//            seeT.setCompoundDrawablesWithIntrinsicBounds(chatRed,null,null,null);
//            seeT.setCompoundDrawablePadding(8);
//            seeT.setBackgroundResource(R.drawable.shape_index_chat_title_red);
//        }
//        if(i==1){
//            seeT.setTextColor(Color.parseColor("#C49469"));
//            seeT.setCompoundDrawablesWithIntrinsicBounds(chatYellow,null,null,null);
//            seeT.setCompoundDrawablePadding(8);
//            seeT.setBackgroundResource(R.drawable.shape_index_chat_title_yellow);
//        }
//        if(i==2){
//            seeT.setTextColor(Color.parseColor("#64BEB7"));
//            seeT.setCompoundDrawablesWithIntrinsicBounds(chatGreen,null,null,null);
//            seeT.setCompoundDrawablePadding(8);
//            seeT.setBackgroundResource(R.drawable.shape_index_chat_title_green);
//        }
//        if(i==3){
//            seeT.setTextColor(Color.parseColor("#7591D1"));
//            seeT.setCompoundDrawablesWithIntrinsicBounds(chatBlue,null,null,null);
//            seeT.setCompoundDrawablePadding(8);
//            seeT.setBackgroundResource(R.drawable.shape_index_chat_title_blue);
//        }
//
//        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ARouter.getInstance()
//                        .build(CityRoutes.CITY_POSTDETAIL)
//                        .withString("id",indexAllChat.posting.id+"")
//                        .withBoolean("isshowDiscuss",false)
//                        .navigation();
//            }
//        });
//        discuss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ARouter.getInstance()
//                        .build(CityRoutes.CITY_POSTDETAIL)
//                        .withString("id",indexAllChat.posting.id+"")
//                        .withBoolean("isshowDiscuss",true)
//                        .navigation();
//            }
//        });
////        seeT.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                ARouter.getInstance()
////                        .build(CityRoutes.CITY_POSTDETAIL)
////                        .withString("id",indexAllChat.posting.id+"")
////                        .withBoolean("isshowDiscuss",false)
////                        .navigation();
////            }
////        });
//        like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(onChatLikeClickListener!=null){
//                    onChatLikeClickListener.chatlikeclick(view,indexAllChat.posting.id+"",indexAllChat.posting.praiseState==0?"0":"1");
//                    indexAllChat.posting.praiseState=indexAllChat.posting.praiseState==0?1:0;
//                    indexAllChat.posting.praiseNum=indexAllChat.posting.praiseNum+(indexAllChat.posting.praiseState==0?-1:1);
//                    like.setText(indexAllChat.posting.praiseNum==0?"  ":indexAllChat.posting.praiseNum+"");
//                    if(indexAllChat.posting.praiseState==0){
//                        like.setCompoundDrawablesWithIntrinsicBounds(likeNormal,null,null,null);
//                        like.setCompoundDrawablePadding(5);
//                    }else {
//                        like.setCompoundDrawablesWithIntrinsicBounds(likeSelect,null,null,null);
//                        like.setCompoundDrawablePadding(5);
//                    }
//
//                }
//            }
//        });
//
//
//        GridLayout gridLayout=baseHolder.getView(R.id.see_imgs);
//        if(indexAllChat.posting!=null){
//            List<String> videsurls=new ArrayList<>();
//            if(indexAllChat.posting.videoUrls!=null){
//                for (int j = 0; j <indexAllChat.posting.videoUrls.size() ; j++) {
//                    IndexAllChat.VideoUrl videoUrl=indexAllChat.posting.videoUrls.get(j);
//                    videsurls.add(videoUrl.url);
//                }
//            }
//            List<String> imgUrls=new ArrayList<>();
//            if(indexAllChat.posting.imgUrls!=null){
//                for (int j = 0; j <indexAllChat.posting.imgUrls.size() ; j++) {
//                    IndexAllChat.ImageUrl videoUrl=indexAllChat.posting.imgUrls.get(j);
//                    videsurls.add(videoUrl.url);
//                }
//            }
//            List<String> showImg=new ArrayList<>();
//            showImg.clear();
//            try {
//                showImg.addAll(videsurls);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                showImg.addAll(imgUrls);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            addImages(context,gridLayout,showImg);
//        }
//
//
//
//    }
//    private int mMargin;
//    private int mCornerRadius;
//    private void addImages(final Context context, final GridLayout gridLayout, final List<String> urls) {
//        if (mMargin == 0) {
//            mMargin = (int) TransformUtil.dp2px(context, 2);
//            mCornerRadius = (int) TransformUtil.dp2px(context, 3);
//        }
//        //System.out.println("展示分格:"+urls.size());
//        gridLayout.removeAllViews();
//        if(urls!=null&&urls.size()>0){
//            gridLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    int row = 3;
//                    if(urls.size()==1){
//                        row=1;
//                    }
//                    if(urls.size()==2){
//                        row=2;
//                    }
//                    gridLayout.removeAllViews();
//                    gridLayout.setRowCount(row);
//                    int w = (gridLayout.getWidth()-gridLayout.getPaddingLeft()-gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
//                    for (int i = 0; i < urls.size(); i++) {
//                        final String url = urls.get(i);
//                        final int pos = i;
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                        params.width = w;
//                        params.height = (int)TransformUtil.dp2px(context, 100f);
//                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
//
//                        View imageparent= LayoutInflater.from(context).inflate(R.layout.index_item_image,gridLayout,false);
//
//                        final CornerImageView imageView = imageparent.findViewById(R.id.iv_pic);
//                        imageView.setCornerRadius(mCornerRadius);
//                        final ImageView isVideo = imageparent.findViewById(R.id.isVideo);
//                        if(MediaFileUtil.isVideoFileType(url)){
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
//                                        final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap,imageView.getWidth()+10,imageView.getHeight());
//                                        ((Activity)context).runOnUiThread(new Runnable() {
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
//                        }else {
//                            isVideo.setVisibility(View.GONE);
//
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
//                        final String[] array=new String[urls.size()];
//                        for (int j = 0; j <array.length ; j++) {
//                            array[j]=urls.get(j);
//                        }
//                        imageView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if(!MediaFileUtil.isVideoFileType(url)){
//                                    ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
//                                            .withCharSequenceArray("urls", array)
//                                            .withInt("pos", pos)
//                                            .navigation();
//                                }else {
//
//                                    List<VodBean.DataBean.DetailBean> videoList=new ArrayList<>();
//                                    String scanResult=url;
//                                    FloatingPlayer.getInstance().destroy();
//                                    Intent intent2 = new Intent(context, VodDisplayActivity.class);
//                                    intent2.putExtra(Ids.PLAY_ID, -1);
//                                    Ids.playingId=-1;
//                                    intent2.putExtra(Ids.VIDEO_LIST, (Serializable)videoList);
//                                    intent2.putExtra(Ids.PLAY_URL, scanResult);
//                                    context.startActivity(intent2);
//
////                                    mContext.startActivity(new Intent(mContext, TextureVideoActivity.class)
////                                            .putExtra("path",url));
//                                }
//
//                            }
//                        });
//                    }
//                }
//            },500);
//        }
//
//
//    }
//    public interface OnChatLikeClickListener{
//        void chatlikeclick(View view,String postingId,String type);
//    }
//}
