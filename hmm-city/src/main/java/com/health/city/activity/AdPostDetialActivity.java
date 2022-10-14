package com.health.city.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.city.R;
import com.health.city.adapter.AdPostDetialGoodsAdapter;
import com.health.city.adapter.ItemAdapter;
import com.health.city.contract.AdPostDetialContract;
import com.health.city.presenter.AdPostDetialPresenter;
import com.health.city.snaprecycleview.GravityPageChangeListener;
import com.health.city.snaprecycleview.GravitySnapHelper;
import com.health.city.snaprecycleview.PageIndicatorHelper;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.PostImgDetial;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.widget.CornerImageView;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

@Route(path = CityRoutes.CITY_ADPOSTDETIAL)
public class AdPostDetialActivity extends BaseActivity implements IsFitsSystemWindows,
        AdPostDetialContract.View, SeekBar.OnSeekBarChangeListener, DataStatisticsContract.View {

    @Autowired
    String id;//帖子Id
    @Autowired
    String type;//帖子类型
    @Autowired
    String createUserFaceUrl; //创建人头像
    @Autowired
    String accountNickname;//创建人昵称
    @Autowired
    List<String> idList;//图片/视频的id集合
    @Autowired
    int pos;//外面点击图片的position

    private ConstraintLayout activityView;
    private View viewHeaderBg;
    private CornerImageView imgLogo;
    private TextView nickName;
    private ImageView imgClose;
    private TextView imgIndicator;
    private RecyclerView imgBanner;
    private RecyclerView goodsRecycle;
    private TXCloudVideoView videoView;
    private ConstraintLayout bottomPlayerLayout;
    private ImageView livePlayer;
    private TextView startTime;
    private SeekBar seekBar;
    private TextView endTime;
    private ImageView livePlayerImg;
    private TXVodPlayer mVodPlayer;
    private List<PostImgDetial> detialList = new ArrayList<>();
    private AdPostDetialGoodsAdapter adPostDetialGoodsAdapter;
    private AdPostDetialPresenter adPostDetialPresenter;
    private DataStatisticsPresenter dataStatisticsPresenter;

    private int totalSecond = 0;//视频总时长  毫秒数
    private int currentSecond = 0;//当前播放时长  秒数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ad_post_detial;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        adPostDetialPresenter = new AdPostDetialPresenter(this, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(this, this);
        if (accountNickname != null && !TextUtils.isEmpty(accountNickname)) {
            nickName.setText(accountNickname);
        } else {
            nickName.setText("");
        }
        com.healthy.library.businessutil.GlideCopy.with(mContext).asBitmap().load(createUserFaceUrl)
                .placeholder(R.drawable.img_1_1_default2).error(R.drawable.img_avatar_default)
                .into(imgLogo);
        if (type.equals("5")) {//回放点播视频
            livePlayer.setVisibility(View.VISIBLE);
            //创建 player 对象
            mVodPlayer = new TXVodPlayer(this);
            //关联 player 对象与界面 view
            mVodPlayer.setPlayerView(videoView);
            mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        } else {
            livePlayer.setVisibility(View.GONE);
        }
        if (idList != null && idList.size() > 0) {
            getData();
        }
        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", id).puts("sourceType", 2).puts("type", 2));
    }

    @Override
    public void getData() {
        super.getData();
        detialList.clear();
        for (int i = 0; i < idList.size(); i++) {
            detialList.add(new PostImgDetial());
        }
        for (int i = 0; i < idList.size(); i++) {
            adPostDetialPresenter.getImgDetial(new SimpleHashMapBuilder<String, Object>().puts("id", idList.get(i)), detialList.get(i));
        }
    }

    private void configCenterRecyclerView(int row, int column) {
        imgBanner.setHasFixedSize(true);
        imgIndicator.setText((pos + 1) + "/" + idList.size());
        setAdapter(pos);

        //setLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, row,
                LinearLayoutManager.HORIZONTAL, false);
        imgBanner.setLayoutManager(gridLayoutManager);
        //setAdapter
        ItemAdapter adapter = new ItemAdapter();
        adapter.setData((ArrayList) detialList);
        imgBanner.setAdapter(adapter);

        //attachToRecyclerView
        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.setColumn(column);
        snapHelper.attachToRecyclerView(imgBanner);
        snapHelper.setCanPageScroll(true);

        imgBanner.scrollToPosition(pos);//初始化  滑动到指定位置
        //加入Indicator监听
        PageIndicatorHelper pageIndicatorHelper = new PageIndicatorHelper();
        pageIndicatorHelper.setPageColumn(column);
        pageIndicatorHelper.setRecyclerView(imgBanner);
        pageIndicatorHelper.setOnPageChangeListener(new GravityPageChangeListener() {
            @Override
            public void onPageSelected(int position, int currentPage, int totalPage) {
                imgIndicator.setText((position + 1) + "/" + idList.size());
                setAdapter(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setAdapter(int position) {
        adPostDetialGoodsAdapter = new AdPostDetialGoodsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        goodsRecycle.setLayoutManager(layoutManager);
        goodsRecycle.setAdapter(adPostDetialGoodsAdapter);
        adPostDetialGoodsAdapter.setData((ArrayList) detialList.get(position).postingGoodsList);
        adPostDetialGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void findViews() {
        super.findViews();
        activityView = (ConstraintLayout) findViewById(R.id.activityView);
        viewHeaderBg = (View) findViewById(R.id.view_header_bg);
        imgLogo = (CornerImageView) findViewById(R.id.imgLogo);
        nickName = (TextView) findViewById(R.id.nickName);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgIndicator = (TextView) findViewById(R.id.imgIndicator);
        imgBanner = (RecyclerView) findViewById(R.id.img_banner);
        goodsRecycle = (RecyclerView) findViewById(R.id.goodsRecycle);
        videoView = (TXCloudVideoView) findViewById(R.id.video_view);
        bottomPlayerLayout = (ConstraintLayout) findViewById(R.id.bottomPlayerLayout);
        livePlayer = (ImageView) findViewById(R.id.livePlayer);
        startTime = (TextView) findViewById(R.id.startTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        endTime = (TextView) findViewById(R.id.endTime);
        livePlayerImg = (ImageView) findViewById(R.id.livePlayerImg);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        livePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVodPlayer.isPlaying()) {//正在播放
                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                    mVodPlayer.pause();
                } else {
                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                    mVodPlayer.resume();
                }
            }
        });
        activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("5")) {//回放点播视频
                    if (mVodPlayer.isPlaying()) {//正在播放
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
                    } else {
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
                    }
                    livePlayerImg.setVisibility(View.VISIBLE);
                    if (livePlayerImg.getVisibility() == View.VISIBLE) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                livePlayerImg.setVisibility(View.GONE);
                            }
                        }, 4000);

                    }
                }
            }
        });
        livePlayerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVodPlayer != null) {
                    if (mVodPlayer.isPlaying()) {//正在播放
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
                        mVodPlayer.pause();
                    } else {
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
                        mVodPlayer.resume();
                    }
                    if (livePlayerImg.getVisibility() == View.VISIBLE) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                livePlayerImg.setVisibility(View.GONE);
                            }
                        }, 4000);

                    }
                }
            }
        });
    }

    @Override
    public void onSuccessGetDetial(PostImgDetial postImgDetial) {
//        if (postImgDetial != null) {
//            detialList.add(postImgDetial);
//        }
        if (detialList.size() == idList.size()) {
            if (type.equals("5")) {
                setAdapter(0);
                bottomPlayerLayout.setVisibility(View.VISIBLE);
                imgIndicator.setVisibility(View.GONE);
                mVodPlayer.startPlay(postImgDetial.url);
                mVodPlayer.setVodListener(new ITXVodPlayListener() {
                    @Override
                    public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
                        totalSecond = bundle.getInt("EVT_PLAY_DURATION_MS");//视频总时长  秒数
                        currentSecond = bundle.getInt("EVT_PLAY_PROGRESS");//当前播放进度  秒数
                        LogUtils.e("视频总时长：" + totalSecond);
                        LogUtils.e("当前播放到：" + currentSecond);
                        if (totalSecond > 0 && currentSecond > 0) {
                            setProgress();
                        }
                        if (i == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                            int width = bundle.getInt("EVT_PARAM1");
                            int height = bundle.getInt("EVT_PARAM2");
                            setInsideModel(width, height);
                        }
                    }

                    @Override
                    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

                    }
                });
            } else {
                imgIndicator.setVisibility(View.VISIBLE);
                bottomPlayerLayout.setVisibility(View.GONE);
                if (detialList.get(pos).postingGoodsList != null) {
                    configCenterRecyclerView(1, 1);
                }
            }
        }
    }

    private void setInsideModel(int width, int height) {
        // 设置填充模式
        if (height > 0) {
            double size = (double) width / height;
            DisplayMetrics outMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            double mPixelsSize = (double) outMetrics.widthPixels / outMetrics.heightPixels;
            if (size > 1.0 || size > mPixelsSize) {
                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            } else {
                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            }
        } else {
            mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        }
    }

    private void setProgress() {
        seekBar.setMax(totalSecond / 1000);
        endTime.setText(FormatRunTime(totalSecond / 1000));
        seekBar.setProgress(currentSecond);
        startTime.setText(FormatRunTime(currentSecond));
        if (mVodPlayer != null && !mVodPlayer.isPlaying()) {//说明进度条走完了
            livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
            livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
            mVodPlayer.pause();
        } else {
            livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
            livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
            if (mVodPlayer != null) {
                mVodPlayer.resume();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (type.equals("5")) {//回放点播视频
            if (mVodPlayer != null) {
                mVodPlayer.stopPlay(true);
                mVodPlayer = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type.equals("5")) {//回放点播视频
            if (mVodPlayer != null) {
                mVodPlayer.resume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (type.equals("5")) {//回放点播视频
            if (mVodPlayer != null) {
                mVodPlayer.pause();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBar.setProgress(seekBar.getProgress());
        startTime.setText(FormatRunTime(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mVodPlayer != null) {
            mVodPlayer.seek(seekBar.getProgress());
            mVodPlayer.resume();//恢复播放
        }
    }

    public String FormatRunTime(long runTime) {
        if (runTime <= 0) {
            return "00:00";
        }
        long hour = 0;
        long minute = 0;
        if (runTime > 3600) {
            hour = runTime / 3600;
        }
        if (runTime > 60) {
            minute = (runTime % 3600) / 60;
        }
        long second = runTime % 60;

        if (hour <= 0) {
            return unitTimeFormat(minute) + ":" +
                    unitTimeFormat(second);

        } else {
            return unitTimeFormat(hour) + ":" + unitTimeFormat(minute) + ":" +
                    unitTimeFormat(second);
        }

    }

    private String unitTimeFormat(long number) {
        return String.format("%02d", number);
    }
}