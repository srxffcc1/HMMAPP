package com.healthy.library.adapter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.healthy.library.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CornerImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PinNormalAdapter extends BaseAdapter<AssemableTeam> {

    PinOnClickListener goOnClickListener;

    private SparseArray<CountDownTimer> countDownCounters=new SparseArray<>();

    public void setGoOnClickListener(PinOnClickListener goOnClickListener) {
        this.goOnClickListener = goOnClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    public PinNormalAdapter() {
        this(R.layout.mall_item_activity_group_goods);
    }

    private PinNormalAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

        final AssemableTeam item= getDatas().get(i);
         CornerImageView groupIcon;
         TextView groupName;
         TextView groupTeamNumber;
         TextView groupTimeLimit;
         TextView groupButton;
        groupIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.groupIcon);
        groupName = (TextView) baseHolder.itemView.findViewById(R.id.groupName);
        groupTeamNumber = (TextView) baseHolder.itemView.findViewById(R.id.groupTeamNumber);
        groupTimeLimit = (TextView) baseHolder.itemView.findViewById(R.id.groupTimeLimit);
        groupButton = (TextView) baseHolder.itemView.findViewById(R.id.groupButton);
        View dividerN= baseHolder.itemView.findViewById(R.id.dividerN);
        dividerN.setVisibility(View.VISIBLE);
        if(i== getDatas().size()-1){
            dividerN.setVisibility(View.INVISIBLE);
        }
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(item.memberFaceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(groupIcon);
        groupTeamNumber.setText(SpanUtils.getBuilder(context,"还差" ).setForegroundColor(Color.parseColor("#444444"))
                .append(item.remainderNum ).setForegroundColor(Color.parseColor("#F02846"))
                .append("人成团").setForegroundColor(Color.parseColor("#444444"))
                .create());
        groupName.setText(item.memberNickName);
        if (item.isMyTeam(context)) {
            groupButton.setText("邀请好友");
        }else {
            groupButton.setText("去参团");
        }
        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goOnClickListener!=null){
                    goOnClickListener.onGoClick(v,item);
                }
            }
        });

        CountDownTimer countDownTimer = countDownCounters.get(groupTimeLimit.hashCode());
        checkTimeOut(countDownTimer,item,groupTimeLimit,true);
    }

    private void initView() {

    }

    public interface PinOnClickListener {
        void onGoClick(View v, AssemableTeam firstcoupon);
    }

    private void checkTimeOut(CountDownTimer countDownTimer, AssemableTeam url, final TextView destview, boolean needtimer) {
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
        if(needtimer){
            Date startTime= null;
            Date endTime= null;
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.regimentTime);
                endTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.realEndTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
            long desorg = startTime.getTime()+nd;
            long timer = startTime.getTime()+nd;
            if(endTime!=null&&endTime.getTime()<=timer){
                timer=endTime.getTime();
                desorg=endTime.getTime();
            }
            timer=timer-System.currentTimeMillis();

            if (timer >0) {
                //System.out.println("开始计时");
                final long finalTimer = timer;
                final long finalDesorg = desorg;
                countDownTimer = new CountDownTimer(finalTimer, 1000) {
                    public void onTick(long millisUntilFinished) {
                        String array= DateUtils.getDistanceTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg +""));
                        destview.setText("剩余"+array+"");
                    }
                    public void onFinish() {
                        destview.setText("过期");
                    }
                }.start();
                countDownCounters.put(destview.hashCode(), countDownTimer);
            } else {
                destview.setText("过期");
            }
        }

    }


}
