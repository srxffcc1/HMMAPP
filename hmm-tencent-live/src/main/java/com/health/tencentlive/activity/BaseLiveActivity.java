package com.health.tencentlive.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.R;
import com.health.tencentlive.model.PushModel;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.model.LiveRoomDecoration;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.net.NetUtil;
import com.healthy.library.widget.ImageSpanCentre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BaseLiveActivity extends BaseActivity {
    public int navigationBarPx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //设置贴图
    public void setLiveRoomMapping(LiveRoomDecoration result, LinearLayout mappingLeftLin, LinearLayout mappingRgihtLin) {
        mappingLeftLin.removeAllViews();
        mappingRgihtLin.removeAllViews();
        if (result != null) {
            if (result.roomDecorationType == 1) {
                if (result.iconDtoList != null && result.iconDtoList.size() > 0) {
                    if (result.position == 1) {
                        for (int i = 0; i < result.iconDtoList.size(); i++) {
                            LiveRoomDecoration.IconDtoListBean listBean = result.iconDtoList.get(i);
                            View view = getLayoutInflater().inflate(R.layout.item_live_room_mapping_layout, mappingLeftLin, false);
                            ImageView mappingImg = view.findViewById(R.id.mappingImg);
                            if (isFinishing()) {
                                return;
                            }
                            com.healthy.library.businessutil.GlideCopy.with(mContext)
                                    .load(listBean.iconPath)
                                    .error(R.drawable.img_1_1_default)
                                    .placeholder(R.drawable.img_1_1_default)

                                    .into(mappingImg);
                            mappingLeftLin.addView(view);
                        }
                    } else if (result.position == 3) {
                        for (int i = 0; i < result.iconDtoList.size(); i++) {
                            LiveRoomDecoration.IconDtoListBean listBean = result.iconDtoList.get(i);
                            View view = getLayoutInflater().inflate(R.layout.item_live_room_mapping_layout, mappingRgihtLin, false);
                            ImageView mappingImg = view.findViewById(R.id.mappingImg);
                            if (isFinishing()) {
                                return;
                            }
                            com.healthy.library.businessutil.GlideCopy.with(mContext)
                                    .load(listBean.iconPath)
                                    .error(R.drawable.img_1_1_default)
                                    .placeholder(R.drawable.img_1_1_default)

                                    .into(mappingImg);
                            mappingRgihtLin.addView(view);
                        }
                    }

                }
            }
        }
    }

    //设置置顶消息
    public void refreshTips(String CmdPara, final TextView liveTipsTxt, final LinearLayout topTipsLayout) {
        if (CmdPara != null) {
            topTipsLayout.setVisibility(View.VISIBLE);
            liveTipsTxt.setVisibility(View.VISIBLE);
            try {
                int showSecond = new JSONObject(CmdPara).getInt("showSecond");
                String content = new JSONObject(CmdPara).getString("content").replace("\n", "");
                if (content != null) {
                    SpannableString spanString = new SpannableString("  " + content);
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.live_room_tips_icon);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    ImageSpanCentre imageSpan = new ImageSpanCentre(drawable, ImageSpanCentre.CENTRE);
                    spanString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    liveTipsTxt.setText(spanString);
                }
                if (showSecond > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            liveTipsTxt.setVisibility(View.GONE);
                            topTipsLayout.setVisibility(View.GONE);
                        }
                    }, showSecond * 1000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            topTipsLayout.setVisibility(View.GONE);
            liveTipsTxt.setVisibility(View.GONE);
        }
    }

    public List<LiveVideoGoods> sortList(List<LiveVideoGoods> data) {
        Collections.sort(data, new Comparator<LiveVideoGoods>() {
            @Override
            public int compare(LiveVideoGoods o1, LiveVideoGoods o2) {
                //根据ranking升序
                return o1.ranking - o2.ranking;
            }
        });
        return data;
    }

    public int getInventoryByGoodsId(String goodsId, String goodsChildId, List<PushModel> data, String liveActivityId) {
        int inventory = 0;
        for (int i = 0; i < data.size(); i++) {
            if (goodsChildId != null && data.get(i).e != null) {
                if (data.get(i).e.equals(goodsChildId) && data.get(i).d.equals(liveActivityId)) {
                    inventory = data.get(i).b;
                }
            } else {
                if (data.get(i).a.equals(goodsId) && data.get(i).d.equals(liveActivityId)) {
                    inventory = data.get(i).b;
                }
            }
        }
        return inventory;
    }

    public int getRankingByGoodsId(String goodsId, String goodsChildId, List<PushModel> data, String liveActivityId) {
        int ranking = 0;
        for (int i = 0; i < data.size(); i++) {
            if (goodsChildId != null && data.get(i).e != null) {
                if (data.get(i).e.equals(goodsChildId) && data.get(i).d.equals(liveActivityId)) {
                    ranking = data.get(i).c;
                }
            } else {
                if (data.get(i).a.equals(goodsId) && data.get(i).d.equals(liveActivityId)) {
                    ranking = data.get(i).c;
                }
            }
        }
        return ranking;
    }

    public List<PushModel> resolveData(String obj) {
        List<PushModel> result = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<PushModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List getRandomNumList() {
        int nums = 3;
        int start = 0;
        int end = 15;
        List list = new ArrayList();
        Random r = new Random();
        while (list.size() != nums) {
            int num = r.nextInt(end - start) + start;
            if (!list.contains(num)) {
                list.add(num);
            }
        }
        return list;
    }

    public void checkNetWorkStatusAndAudio() {
        if (!NetUtil.isNetworkAvalible(mContext)) {
            showToast("当前暂无网络连接，请检查你的网络设置");
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
                    if (current == 0) {
                        if (mActivity != null && !mActivity.isFinishing()) {
                            showToast("当前处于静音状态");
                        }
                    }
                }
            }, 5000);
        }
        if (NetUtil.IsMobileNetConnect(mContext)) {
            showToast("当前正在使用流量播放，请注意流量消耗");
        }
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    public void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

    public String unitTimeFormat(long number) {
        return String.format("%02d", number);
    }

    public String getTime(String beginTime, String endTime) {
        //时间处理类
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = 0;
        long createTime = 0;
        try {
            createTime = df.parse(beginTime).getTime();
            currentTime = df.parse(endTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = (currentTime - createTime) / 1000;
        if (diff > 0) {
            return FormatRunTime(diff);
        } else {
            return "0";
        }
    }

    //将时间戳转换为时间

    public static String stampToTime(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(s) * 1000L));
    }

}