package com.health.index.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.IndexMenu;
import com.health.index.model.IndexVideoOnline;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.ScreenUtils;

import java.util.HashMap;

public class IndexOnlineVideoItemAdapter extends BaseAdapter<IndexVideoOnline> {
    public void setTmprecyclerView(RecyclerView tmprecyclerView) {
        this.tmprecyclerView = tmprecyclerView;
    }

    RecyclerView tmprecyclerView;

//    BaseAdapter.OnOutClickListener moutClickListener;

//    public void setOutClickListener(BaseAdapter.OnOutClickListener onOutClickListener) {
//        this.moutClickListener = onOutClickListener;
//    }

    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    protected void convert(BaseViewHolder helper, IndexMenu item) {

    }

    public IndexOnlineVideoItemAdapter() {
        this(R.layout.index_mon_onlinevideo_item_rv);
    }


    private IndexOnlineVideoItemAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper(5);
    }


    @Override
    public void onBindViewHolder(@NonNull final BaseHolder baseHolder, int i) {
        baseHolder.getView(R.id.onlinevideoLL).setLayoutParams(new LinearLayout.LayoutParams((int)(ScreenUtils.getScreenWidth(context)*0.35),LinearLayout.LayoutParams.WRAP_CONTENT));
        final IndexVideoOnline firstvideo= getDatas().get(i);
        final ImageView imageView=baseHolder.itemView.findViewById(R.id.videoFlash);
        imageView.setImageResource(R.drawable.img_1_1_default);
        if(!TextUtils.isEmpty(firstvideo.photo)){
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(firstvideo.photo)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(imageView);
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    try {
                        //根据url获取缩略图
                        retriever.setDataSource(firstvideo.videoUrl, new HashMap());
                        //获得第一帧图片
                        bitmap = retriever.getFrameAtTime();
                        final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap,imageView.getWidth()+10,imageView.getHeight());
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(finalBitmap);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_VIDEOONLINELISTBLOCkDETAIL)
                        .withString("id",firstvideo.id+"")
                        .navigation();
            }
        });


    }
}
