package com.health.sound.activity;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.sound.R;
import com.health.sound.adapter.SoundCellAdapter;
import com.health.sound.adapter.SoundDeatilFlexListAdapter;
import com.health.sound.adapter.SoundDivideNoTopAdapter;
import com.health.sound.contract.SoundDetailContract;
import com.health.sound.model.SoundHolder;
import com.health.sound.model.SoundTrack;
import com.health.sound.presenter.SoundDetailPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.model.SoundAlbum;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
import com.yhao.floatwindow.FloatWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ezy.assist.compat.SettingsCompat;
import permison.FloatWindowManager;


@Route(path = SoundRoutes.SOUND_DETAIL)
public class SoundDeatilActivity extends BaseActivity implements IsNeedShare,IsFitsSystemWindows, OnRefreshLoadMoreListener, SoundDetailContract.View,IDataCallBack<TrackList>, SoundDeatilFlexListAdapter.OnIndexClickListener {


    @Autowired
    String audioType;

    @Autowired
    String id;
    private TopBar topBar;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private com.healthy.library.widget.CornerImageView soundEdititonIcon;
    private android.widget.TextView soundEdititonTitle;
    private android.widget.TextView soundEdititonCotent;
    private com.healthy.library.widget.ImageTextView soundEditionPlays;
    private android.widget.TextView soundEditionFrom;
    private com.healthy.library.widget.ImageTextView editionLeftTitle;
    private com.healthy.library.widget.ImageTextView editionSumCount;
    private android.widget.LinearLayout soundEditionPlayAgain;
    private View dividerStore;
    private androidx.recyclerview.widget.RecyclerView recyclerQuestion;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private List<String> emptylist1;
    private List<String> emptylist2;
    private SoundDeatilFlexListAdapter soundGridAdapter;
    private SoundDivideNoTopAdapter soundDivideAdapter;
    private SoundCellAdapter soundListAdapter;
    private androidx.constraintlayout.widget.ConstraintLayout editionLeftTitleLL;

    boolean isshowbooklist = false;
    SoundDetailPresenter soundDetailPresenter;
    boolean iscollect = false;
    private ImageTextView soundEditCollect;

    Map<String,Object> detailmap=new HashMap<>();
    Map<String,String> soundmap=new HashMap<>();

    public long page=1;
    private List<SoundTrack> soundTrackList= new ArrayList<>();
    private android.widget.ImageView soundEditionPlayAgainImg;
    private TextView soundEditionPlayAgainText;
    private boolean isplayAll=false;
    private boolean isplayClick=false;

    private Bitmap sBitmap;

    public static void initXMLY(){
        if(FrameActivityManager.instance().topActivity()!=null){
            try {
                Notification mNotification = XmNotificationCreater.getInstanse(FrameActivityManager.instance().topActivity()).initNotification(FrameActivityManager.instance().topActivity(), SoundMainActivity.class);
                XmPlayerManager.getInstance(FrameActivityManager.instance().topActivity()).setNotificationForNoCrash((int)System.currentTimeMillis() ,mNotification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sound_activity_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        XmPlayerManager.getInstance(mContext).addPlayerStatusListener(mPlayerStatusListener);

        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showShare();
            }
        });

        buildRecyclerView();
        soundDetailPresenter=new SoundDetailPresenter(mContext,this);
        editionLeftTitleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isshowbooklist = !isshowbooklist;
                chaneIndex();
            }
        });
        getData();
        if(XmPlayerManager.getInstance(mContext).getPlayerStatus() == PlayerConstants.STATE_PAUSED){
            try {
                SoundHolder.getInstance().floatName.setText("");
                FloatWindow.get().hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        soundEditCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> submap=new HashMap<>();
                submap.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                submap.put("albumsId", id + "");
                submap.put("status",iscollect?"2":"1");
                soundDetailPresenter.subAlbums(submap);
            }
        });
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.M){
            FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
        }else {
            if(!SettingsCompat.canDrawOverlays(mContext)){
                Toast.makeText(mContext,"需要打开悬浮窗权限",Toast.LENGTH_SHORT).show();
                SettingsCompat.manageDrawOverlays(mContext);
            }
        }
//        FloatWindowManager.getInstance().applyOrShowFloatWindow(this);

    }
    boolean isFish=false;
    private IXmPlayerStatusListener mPlayerStatusListener = new IXmPlayerStatusListener() {

        @Override
        public void onPlayStart() {

            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPlayPause() {
            //System.out.println("刷新播放状态1");

            buildPlayAgain(false);
            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPlayStop() {

            //System.out.println("刷新播放状态2");
            isplayAll=false;
            buildPlayAgainInit();
            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSoundPlayComplete() {
            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSoundPrepared() {

        }

        @Override
        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {

        }

        @Override
        public void onBufferingStart() {

        }

        @Override
        public void onBufferingStop() {

            //System.out.println("刷新播放状态3");
            soundListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onBufferProgress(int i) {

        }

        @Override
        public void onPlayProgress(int i, int i1) {

        }

        @Override
        public boolean onError(XmPlayerException e) {
            return false;
        }
    };
    public void chaneIndex(){
        if (isshowbooklist) {
            editionSumCount.setDrawable(R.drawable.ic_sound_up, mContext);
            soundDivideAdapter.setModel("分割线");
            soundGridAdapter.setData((ArrayList<SoundTrack>) soundTrackList);
        } else {
            editionSumCount.setDrawable(R.drawable.ic_sound_down, mContext);
            soundDivideAdapter.setModel(null);
            soundGridAdapter.setData(new ArrayList<SoundTrack>());
        }
    }

    @Override
    public void getData() {
        super.getData();
        if(page==1||page==0){
            detailmap.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            detailmap.put("ids",id+"");
            soundDetailPresenter.getSoundAlbums(detailmap);
        }
        soundmap.put(DTransferConstants.ALBUM_ID, id);
        soundmap.put(DTransferConstants.SORT, "asc");
        soundmap.put(DTransferConstants.PAGE, page+"");
        soundListAdapter.setSelectPage(page);
        CommonRequest.getInstanse().setDefaultPagesize(20);
        CommonRequest.getTracks(soundmap, this);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        soundGridAdapter = new SoundDeatilFlexListAdapter();
        delegateAdapter.addAdapter(soundGridAdapter);
        soundGridAdapter.setOnIndexClickListener(this);
        soundDivideAdapter = new SoundDivideNoTopAdapter();
        soundDivideAdapter.setModel("分割线");
        delegateAdapter.addAdapter(soundDivideAdapter);

        soundListAdapter = new SoundCellAdapter();
        soundListAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NonNull String function, @NonNull Object obj) {
                if("播放点击开始".equals(function)){
                    buildPlayAgain(true);
                    isplayAll=true;
                }
            }
        });
//        emptylist2 = new ArrayList<>();
//        emptylist2.clear();
//        for (int i = 0; i <20 ; i++) {
//            emptylist2.add(""+i);
//        }
//        soundListAdapter.setData((ArrayList<String>) emptylist2);
        delegateAdapter.addAdapter(soundListAdapter);

    }

    int verticalOffsetold = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XmPlayerManager.getInstance(mContext).removePlayerStatusListener(mPlayerStatusListener);
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        soundEdititonIcon = (CornerImageView) findViewById(R.id.soundEdititonIcon);
        soundEdititonTitle = (TextView) findViewById(R.id.soundEdititonTitle);
        soundEdititonCotent = (TextView) findViewById(R.id.soundEdititonCotent);
        soundEditionPlays = (ImageTextView) findViewById(R.id.soundEditionPlays);
        soundEditionFrom = (TextView) findViewById(R.id.soundEditionFrom);
        editionLeftTitle = (ImageTextView) findViewById(R.id.editionLeftTitle);
        editionSumCount = (ImageTextView) findViewById(R.id.editionSumCount);
        soundEditionPlayAgain = (LinearLayout) findViewById(R.id.soundEditionPlayAgain);
        dividerStore = (View) findViewById(R.id.divider_store);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
//        soundEditionBookList = (RecyclerView) findViewById(R.id.soundEditionBookList);
        editionLeftTitleLL = (ConstraintLayout) findViewById(R.id.editionLeftTitleLL);
        soundEditCollect = (ImageTextView) findViewById(R.id.soundEditCollect);
        soundEditionPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initXMLY();
                isplayClick=!isplayClick;
                if(!isplayAll){
                    isplayAll=true;
                    XmPlayerManager.getInstance(mContext).resetPlayList();
                    SoundHolder.getInstance().setSoundAlbum(soundAlbum);
                    XmPlayerManager.getInstance(mContext).playList(tracks ,0);
                    try {
                        FloatWindow.get().show();
                        System.out.println("喜马拉雅悬浮出现2");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    soundEditionPlayAgain.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            soundListAdapter.notifyDataSetChanged();
                        }
                    },500);
                }else {
                    if(isplayClick){
                        XmPlayerManager.getInstance(mContext).play();
                        try {
                            FloatWindow.get().show();
                            System.out.println("喜马拉雅悬浮出现2");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        soundEditionPlayAgain.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                soundListAdapter.notifyDataSetChanged();
                            }
                        },500);
                    }else {
                        XmPlayerManager.getInstance(mContext).pause();
                        soundEditionPlayAgain.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                soundListAdapter.notifyDataSetChanged();
                            }
                        },500);
                    }

                }
                buildPlayAgain(isplayClick);

            }
        });
        soundEditionPlayAgainImg = (ImageView) findViewById(R.id.soundEditionPlayAgainImg);
        soundEditionPlayAgainText = (TextView) findViewById(R.id.soundEditionPlayAgainText);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();

    }

    @Override
    public void successGetSoundAlbums(List<SoundAlbum> soundAlbumList) {
        if (soundAlbumList.size() > 0) {
            buildAlbumTop(soundAlbumList.get(0));
        }
    }

    @Override
    public void successGetSoundAlbumsCollectStatus(String status) {
        buidlSubAlb(status,false);

    }
    public void buidlSubAlb(String status,boolean needshow){
        if ("1".equals(status)) {
            iscollect = true;
            soundEditCollect.setText("已订阅");
            soundEditCollect.setBackgroundResource(R.drawable.shape_sound_sub_select);
            soundEditCollect.setDrawable(R.drawable.ic_sound_sub_select, mContext);
        } else {
            iscollect = false;
            soundEditCollect.setText("订阅");
            soundEditCollect.setBackgroundResource(R.drawable.shape_sound_sub);
            soundEditCollect.setDrawable(R.drawable.ic_sound_sub_normal, mContext);
        }
        if(needshow){
            Toast.makeText(mContext, "1".equals(status)?"订阅成功":"取消订阅成功",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void successSubAlbums(String status) {
        buidlSubAlb(status,true);

    }
    SoundAlbum soundAlbum;
    private void buildAlbumTop(SoundAlbum soundAlbum) {
        if (soundAlbum != null) {
            if(SoundHolder.getInstance().soundAlbum!=null){
                boolean isneedStop= true;
                try {
                    isneedStop = SoundHolder.getInstance().soundAlbum.isStop();
                } catch (Exception e) {
                    isneedStop=false;
                    e.printStackTrace();
                }
                if(soundAlbum.id==(SoundHolder.getInstance().soundAlbum.id)&&XmPlayerManager.getInstance(mContext).getPlayerStatus() != PlayerConstants.STATE_PAUSED&&!isneedStop){
                    buildPlayAgain(true);
                }
            }
            this.soundAlbum=soundAlbum;
            soundListAdapter.setSoundAlbum(soundAlbum);
            Map<String, Object> collectchekmap = new HashMap<>();
            collectchekmap.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            collectchekmap.put("albumsId", soundAlbum.id + "");
            soundDetailPresenter.getAlbumsCollectStatus(collectchekmap);
        }
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(soundAlbum.cover_url)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(soundEdititonIcon);

        com.healthy.library.businessutil.GlideCopy.with(mContext).load(soundAlbum.cover_url)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(new SimpleTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        sBitmap= DrawableUtils.drawableToBitmap(resource);
                    }
                });


        soundEdititonTitle.setText(soundAlbum.album_title);
        soundEdititonCotent.setText(soundAlbum.recommend_reason);
        soundEditionPlays.setText(ParseUtils.parseNumber(soundAlbum.play_count+"",10000,"万"));
    }

    private void buildPlayAgain(boolean b) {
        this.isplayClick=b;
        if(b){
            soundEditionPlayAgainImg.setImageResource(R.drawable.ic_sound_again_pause);
            soundEditionPlayAgainText.setText("暂停播放");
        }else {
            soundEditionPlayAgainImg.setImageResource(R.drawable.ic_sound_again_p);
            soundEditionPlayAgainText.setText("继续播放");
        }
    }

    private void buildPlayAgainInit() {
        this.isplayClick=false;
        soundEditionPlayAgainImg.setImageResource(R.drawable.ic_sound_again_p);
        soundEditionPlayAgainText.setText("全部播放");
    }

    List<Track> tracks=new ArrayList<>();
    @Override
    public void onSuccess(TrackList trackList) {
        if(page==1||page==0){
            buildIndexs(trackList.getTotalPage(),trackList.getTotalCount());
            chaneIndex();
        }
        tracks=trackList.getTracks();
        page=trackList.getCurrentPage();
        soundListAdapter.setData((ArrayList<Track>) trackList.getTracks());
    }

    private void buildIndexs(int totalPage, int totalCount) {
        boolean isendsplit=false;
        editionSumCount.setText("共"+totalCount+"集");
        soundTrackList.clear();
        if(totalCount<totalPage*20){
            isendsplit=true;
        }
        for (int i = 0; i <totalPage ; i++) {
            if(i!=0){
                if(i==totalPage-1&&isendsplit){
                    if((20*i+1)==((20*i)+totalCount%20)){
                        soundTrackList.add(new SoundTrack(i+1,(20*i+1)+""));
                    }else {
                        soundTrackList.add(new SoundTrack(i+1,(20*i+1)+"～"+((20*i)+totalCount%20)));
                    }
                }else {
                    soundTrackList.add(new SoundTrack(i+1,(20*i+1)+"～"+(20*(i+1))));
                }
            }else {
                if(i==totalPage-1&&isendsplit){
                    soundTrackList.add(new SoundTrack(i+1,(20*i+1)+"～"+((20*i)+totalCount)));
                }else {
                    soundTrackList.add(new SoundTrack(i+1,(20*i+1)+"～"+(20*(i+1))));
                }
            }

        }
//        soundGridAdapter.setData((ArrayList<SoundTrack>) soundTrackList);
    }

    @Override
    public void onError(int i, String s) {
        System.out.println("喜马拉雅报错了:"+s);
    }

    @Override
    public void onIndexClick(long page) {
        this.page=page;
        isshowbooklist = false;
        chaneIndex();
        getData();
    }

    @Override
    public String getSurl() {
        String url;
        if("1".equals(audioType)){
            String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
            url= String.format("%s?type=8&scheme=HMMBabyAudioAlbumDetail&index="+id, urlPrefix);
        }else {
            String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
            url= String.format("%s?type=8&scheme=HMMAudioAlbumDetail&index="+id, urlPrefix);
        }
        return url;
    }

    @Override
    public String getSdes() {
        return " ";
    }

    @Override
    public String getStitle() {
        String url;
        url= "宝宝最爱";
        try {
            if("1".equals(audioType)){

                url= "宝宝最爱- "+soundAlbum.album_title;
            }else {
                url= "妈妈最爱- "+soundAlbum.album_title;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public Bitmap getsBitmap() {
        return sBitmap;
    }
}
