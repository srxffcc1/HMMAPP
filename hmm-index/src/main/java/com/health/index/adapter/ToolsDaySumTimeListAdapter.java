package com.health.index.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.ToolsDiarySleep;
import com.healthy.library.model.ToolsSumType;
import com.healthy.library.constant.Functions;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.DateUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsDaySumTimeListAdapter extends BaseQuickAdapter<ToolsSumType, BaseViewHolder> {
    OnRecordLongClickListener onRecordLongClickListener;

    public void setOnRecordLongClickListener(OnRecordLongClickListener onRecordLongClickListener) {
        this.onRecordLongClickListener = onRecordLongClickListener;
    }

    private String childId;
    public void setChildId(String childId) {
        this.childId=childId;
    }
    public boolean checkIsLast(int postion,String date){
        List<ToolsSumType> seachs=getData();
        if(seachs.size()>postion+1){
            for (int i = postion+1; i <seachs.size() ; i++) {
                ToolsSumType item=seachs.get(i);
                String seachday=DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss");
                if(seachday.equals(date)){
                    return false;
                }
            }
        }else {
            return true;
        }
        return true;
    }
    public boolean checkIsFirst(int postion,String date){
        List<ToolsSumType> seachs=getData();
        if(postion!=0){
            for (int i = 0; i <postion ; i++) {
                ToolsSumType item=seachs.get(i);
                String seachday=DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss");
                if(seachday.equals(date)){
                    return false;
                }
            }
        }else {
            return true;
        }
        return true;
    }

    public ToolsDaySumTimeListAdapter() {
        this(R.layout.index_activity_tools_diary_sum_item_time);
    }

    public ToolsDaySumTimeListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToolsSumType item) {
         LinearLayout toolsMenuIconLL;
         TextView toolsMenuTopTime;
         LinearLayout toolsMenuIconLLTmp;
         ConstraintLayout toolsTimeSleep;
         ImageView toolsMenuSleepIcon;
         TextView toolsMenuSleepTip;
         TextView toolsMenuSleepTime;
         ImageView toolsBarSleep;
         Space tmpspace;
         TextView sleepTime;
         ConstraintLayout toolsTimeWake;
         ImageView toolsMenuWakeIcon;
         TextView toolsMenuWakeTip;
         TextView toolsMenuWakeTime;

        toolsMenuIconLL = (LinearLayout)helper.itemView. findViewById(R.id.tools_menu_iconLL);
        toolsMenuTopTime = (TextView) helper.itemView.findViewById(R.id.tools_menu_top_time);
        toolsMenuIconLLTmp = (LinearLayout) helper.itemView.findViewById(R.id.tools_menu_iconLLTmp);
        toolsTimeSleep = (ConstraintLayout) helper.itemView.findViewById(R.id.tools_time_sleep);
        toolsMenuSleepIcon = (ImageView) helper.itemView.findViewById(R.id.tools_menu_sleep_icon);
        toolsMenuSleepTip = (TextView) helper.itemView.findViewById(R.id.tools_menu_sleep_tip);
        toolsMenuSleepTime = (TextView) helper.itemView.findViewById(R.id.tools_menu_sleep_time);
        toolsBarSleep = (ImageView) helper.itemView.findViewById(R.id.tools_bar_sleep);
        tmpspace = (Space) helper.itemView.findViewById(R.id.tmpspace);
        sleepTime = (TextView) helper.itemView.findViewById(R.id.sleepTime);
        toolsTimeWake = (ConstraintLayout) helper.itemView.findViewById(R.id.tools_time_wake);
        toolsMenuWakeIcon = (ImageView)helper.itemView. findViewById(R.id.tools_menu_wake_icon);
        toolsMenuWakeTip = (TextView) helper.itemView.findViewById(R.id.tools_menu_wake_tip);
        toolsMenuWakeTime = (TextView)helper.itemView. findViewById(R.id.tools_menu_wake_time);

        sleepTime.setVisibility(View.VISIBLE);
        toolsBarSleep.setVisibility(View.VISIBLE);
        toolsTimeWake.setVisibility(View.VISIBLE);

        toolsMenuIconLLTmp.setVisibility(View.VISIBLE);
        ToolsDiarySleep toolsDiarySleep= (ToolsDiarySleep) item;
        toolsMenuTopTime.setText(DateUtils.getClassDateNoHour(DateUtils.formatLongSecAll(toolsDiarySleep.feedingDateTime, "yyyy-MM-dd HH:mm:ss")));
        if(checkIsLast(helper.getPosition(),DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss"))){
            toolsMenuIconLLTmp.setVisibility(View.GONE);
        }
        if(!checkIsFirst(helper.getPosition(),DateUtils.getDateDay(item.getTimeleft(),"yyyy-MM-dd HH:mm:ss"))){
            toolsMenuTopTime.setText("");
        }




        if (TextUtils.isEmpty(item.getRightTime())) {//说明没起来呢
            sleepTime.setVisibility(View.GONE);
            toolsBarSleep.setVisibility(View.GONE);
            toolsTimeWake.setVisibility(View.GONE);

            toolsMenuSleepIcon.setImageResource(R.drawable.tools_menu7);
            toolsMenuSleepTip.setText("睡觉");
            toolsMenuSleepTime.setText(DateUtils.getClassDateTools(DateUtils.formatLongSecAll(item.getLeftTime(), "yyyy-MM-dd HH:mm:ss")));
        } else {//起来了
            toolsMenuSleepIcon.setImageResource(R.drawable.tools_menu7);
            toolsMenuSleepTip.setText("睡觉");
            toolsMenuSleepTime.setText(DateUtils.getClassDateTools(DateUtils.formatLongSecAll(item.getLeftTime(), "yyyy-MM-dd HH:mm:ss")));

            toolsMenuWakeIcon.setImageResource(R.drawable.tools_menu8);
            toolsMenuWakeTip.setText("醒来");
            toolsMenuWakeTime.setText(DateUtils.getClassDateTools(DateUtils.formatLongSecAll(item.getRightTime(), "yyyy-MM-dd HH:mm:ss")));


            try {
                String sleeptimeString="";
                String[] timetip=DateUtils.getDistanceTimeNoDouble(item.getLeftTime(),item.getRightTime());
                if(!"0".equals(timetip[0])){
                    sleeptimeString=sleeptimeString+timetip[0]+"天";
                }
                if(!"0".equals(timetip[1])){
                    sleeptimeString=sleeptimeString+timetip[1]+"小时";
                }
                if(!"0".equals(timetip[2])){
                    sleeptimeString=sleeptimeString+timetip[2]+"分钟";
                }
                sleepTime.setText(sleeptimeString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        toolsTimeSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inittime="";
                try {
                    inittime=new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getLeftTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_SLEEP)
                        .withString("childId",childId)
                        .withBoolean("isSleep",false)
                        .withString("init", inittime)
                        .withString("recordId",item.recordId)
                        .navigation();
            }
        });
        toolsTimeWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inittime="";
                try {
                    inittime=new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getRightTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_SLEEP)
                        .withString("childId",childId)
                        .withBoolean("isSleep",true)
                        .withString("init",inittime)
                        .withString("recordId",item.recordId)
                        .navigation();
            }
        });
        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                StyledDialog.init(mContext);
                StyledDialog.buildIosAlert("", "是否删除此记录?", new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onThird() {
                        super.onThird();
                    }

                    @Override
                    public void onSecond() {
                        Map<String,Object> clearmap=new HashMap<>();
                        clearmap.put("recordId",item.recordId+"");
                        clearmap.put(Functions.FUNCTION, "9030");
                        if(onRecordLongClickListener!=null){
                            onRecordLongClickListener.onRecordLongClick(clearmap);
                        }
                    }
                }).setCancelable(true,true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("取消", "确定").show();


                return true;
            }
        });

    }
    public interface OnRecordLongClickListener{
        void onRecordLongClick(Map<String,Object> clearmap);
    }
    private void initView() {


    }
}
