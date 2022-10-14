package com.health.index.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.index.R;
import com.health.index.model.IndexVideoOnline;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class IndexOnlineVideoListAdapter extends BaseAdapter<ArrayList<IndexVideoOnline>> {

    private LinearLayoutManager layoutManager;

    public void setTmprecyclerView(RecyclerView tmprecyclerView) {
        this.tmprecyclerView = tmprecyclerView;
    }

    RecyclerView tmprecyclerView;
    IndexOnlineVideoItemAdapter indexFunctionAdapter;
    @Override
    public int getItemViewType(int position) {
        return 19;
    }
    public IndexOnlineVideoListAdapter() {
        this(R.layout.index_mon_onlinevideo_list);
    }
    private IndexOnlineVideoListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }


    @Override
    public void onBindViewHolder(@NonNull final BaseHolder baseHolder, int i) {

        IndexVideoOnline firstvideo= getDatas().get(i).get(0);
        RecyclerView recycler_fun=baseHolder.getView(R.id.recycler_fun);
        layoutManager = new LinearLayoutManager(context, OrientationHelper.HORIZONTAL,false);
        LinearLayout topVideoLL=baseHolder.getView(R.id.topVideoLL);

        View topVideo=buildVideoView(topVideoLL,firstvideo);
        topVideoLL.removeAllViews();
        topVideoLL.addView(topVideo);
        recycler_fun.setLayoutManager(layoutManager);

        if(indexFunctionAdapter==null){
            indexFunctionAdapter=new IndexOnlineVideoItemAdapter();
        }
        recycler_fun.setAdapter(indexFunctionAdapter);
        indexFunctionAdapter.setData((ArrayList<IndexVideoOnline>) Utils.get2List(getDatas().get(0)));
//        indexFunctionAdapter.setOutClickListener(getMoutClickListener());

        View videoPassList=baseHolder.getView(R.id.videoPassList);
        videoPassList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                        .navigation();
            }
        });
    }

    private View buildVideoView(LinearLayout topVideoLL, final IndexVideoOnline firstvideo) {
        View view= LayoutInflater.from(context).inflate(R.layout.index_mon_onlinevideo_item,topVideoLL,false);
        final ImageView imageView=view.findViewById(R.id.videoFlash);
        imageView.setImageResource(R.drawable.img_1_1_default);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_VIDEOONLINELISTBLOCkDETAIL)
                        .withString("id",firstvideo.id+"")
                        .navigation();
            }
        });
        if(!TextUtils.isEmpty(firstvideo.photo)){
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(firstvideo.photo)
                    .placeholder(R.drawable.img_1_1_default2)
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
        View onlinevideoLL=view.findViewById(R.id.onlinevideoLL);
        return view;
    }
}
