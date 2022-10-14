package com.health.tencentlive.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.dashboardview.DashboardView;
import com.health.tencentlive.model.ResultBean;
import com.health.tencentlive.netutils.ConverUtil;
import com.health.tencentlive.netutils.SpeedListener;
import com.health.tencentlive.netutils.SpeedManager;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.net.NetUtil;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.widget.TopBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

@Route(path = TencentLiveRoutes.LIVESPEEDTEST)
public class SpeedTestActivity extends BaseActivity implements IsFitsSystemWindows {
    private TopBar topBar;
    private TextView delayNum;
    private TextView downloadNum;
    private TextView uploadNum;
    private TextView PacketLossNum;
    private TextView speedTxt;
    private TextView startSpeed;
    private DashboardView dashboardView;
    private Timer pingTimer;//ping计时器
    private long sendTimes = 0;//发送次数
    private long receiveTimes = 0;//接收次数
    private long failedTimes = 0;//失败次数
    private WeakReferenceHandler handler;
    private SpeedManager speedManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_speed_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        handler = new WeakReferenceHandler(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        delayNum = (TextView) findViewById(R.id.delayNum);
        downloadNum = (TextView) findViewById(R.id.downloadNum);
        uploadNum = (TextView) findViewById(R.id.uploadNum);
        PacketLossNum = (TextView) findViewById(R.id.PacketLossNum);
        speedTxt = (TextView) findViewById(R.id.speedTxt);
        startSpeed = (TextView) findViewById(R.id.startSpeed);
        dashboardView = (DashboardView) findViewById(R.id.dashboardView);
        dashboardView.setPercent(0);
        startSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtil.isNetworkAvalible(mContext)) {
                    showToast("当前暂无网络连接，请检查您的网络连接");
                } else {
                    dashboardView.setPercent(0);
                    startSpeed.setEnabled(false);
                    startSpeed.getBackground().setAlpha(180);
                    startSpeed.setText("正在测速中...");
                    start();
                }
            }
        });
    }

    private void startTimer() {
        pingTimer = new Timer();
        pingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //次数统计
                sendTimes++;
                Message message = handler.obtainMessage();
                message.what = 110;
                message.obj = ping();
                handler.sendMessage(message);
                handler.sendEmptyMessage(111);
            }
        }, 0, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pingTimer.cancel();
            }
        }, 10000);
    }

    @Override
    public void getData() {
        super.getData();
    }

    private static class WeakReferenceHandler extends Handler {
        private WeakReference<SpeedTestActivity> mWeakReference;

        WeakReferenceHandler(SpeedTestActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            SpeedTestActivity activity = mWeakReference.get();
            switch (msg.what) {
                case 110:
                    ResultBean resultBean = (ResultBean) msg.obj;
                    if (resultBean == null) {
                        activity.showToast("无法访问目标地址，请检查！");
                        return;
                    }
                    String s = resultBean.getContent();
                    int endIndex = s.indexOf("--- ");
                    String content = s.substring(0, endIndex).trim();
                    if (s.contains("100% packet loss")) {
                        activity.failedTimes++;
                        LogUtils.e("ping失败了");
                    } else {
                        activity.receiveTimes++;
                        String millis = content.split("=")[3].split(" ")[0];
                        activity.delayNum.setText(millis + "ms");
                        if (activity.sendTimes != 0) {
                            NumberFormat numberInstance = NumberFormat.getNumberInstance();
                            numberInstance.setMaximumFractionDigits(2);
                            double rate = ((double) activity.failedTimes / activity.sendTimes) * 100;
                            activity.PacketLossNum.setText(numberInstance.format(rate) + "%");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public ResultBean ping() {
        String url = "capi.hanmama.com";
        ResultBean resultBean = new ResultBean();
        StringBuilder builder = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec("ping -c 1 -w 1 " + url);
            InputStreamReader streamReader = new InputStreamReader(process.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            String s = builder.toString();
            if ("".equals(s)) {
                return null;
            }
            int index = s.indexOf("bytes of data.");
            resultBean.setTitle(s.substring(0, index + 14));
            resultBean.setContent(s.substring(index + 15));
            return resultBean;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
//            runtime.gc();
        }
        return null;
    }

    private void start() {

        speedManager = new SpeedManager.Builder()
                .setSpeedListener(new SpeedListener() {
                    @Override
                    public void speeding(long downSpeed, long upSpeed) {
                        String[] downResult = ConverUtil.fomartSpeed(downSpeed);
                        downloadNum.setText(downResult[0] + downResult[1]);
                        String[] upResult = ConverUtil.fomartSpeed(upSpeed);
                        uploadNum.setText(upResult[0] + upResult[1]);
                        String[] totalResult = ConverUtil.fomartSpeed(downSpeed + upSpeed);
                        double percent = 0;
                        if ("KB/S".equals(totalResult[1])) {
                            speedTxt.setText(fun2(Double.parseDouble(totalResult[0]) / 1024 * 8) + "");
                            percent = Double.parseDouble(downResult[0]) / 1024;
                        } else if ("MB/S".equals(totalResult[1])) {
                            speedTxt.setText(fun2(Double.parseDouble(totalResult[0]) * 8) + "");
                            percent = Double.parseDouble(downResult[0]);
                        } else {
                            speedTxt.setText("0");
                        }
                        LogUtils.e(downResult[0]);
                        if (percent < 1) {
                            dashboardView.setPercent((int) (30 + percent));
                        }
                        if (percent >= 1 && percent < 3) {
                            dashboardView.setPercent((int) (65 + percent));
                        }
                        if (percent >= 3 && percent < 5) {
                            dashboardView.setPercent((int) (75 + percent));
                        }
                        if (percent >= 5) {
                            dashboardView.setPercent((int) (90 + percent));
                        }
                    }

                    @Override
                    public void finishSpeed(long finalDownSpeed, long finalUpSpeed) {
                        String[] downResult = ConverUtil.fomartSpeed(finalDownSpeed);
                        downloadNum.setText(downResult[0] + downResult[1]);
                        String[] upResult = ConverUtil.fomartSpeed(finalUpSpeed);
                        uploadNum.setText(upResult[0] + upResult[1]);

                        String[] totalResult = ConverUtil.fomartSpeed(finalDownSpeed + finalUpSpeed);
                        double percent = 0;
                        if ("KB/S".equals(totalResult[1])) {
                            speedTxt.setText(fun2(Double.parseDouble(totalResult[0]) / 1024 * 8) + "");
                            percent = Double.parseDouble(downResult[0]) / 1024;
                        } else if ("MB/S".equals(totalResult[1])) {
                            speedTxt.setText(fun2(Double.parseDouble(totalResult[0]) * 8) + "");
                            percent = Double.parseDouble(downResult[0]);
                        } else {
                            speedTxt.setText("0");
                        }

                        LogUtils.e(downResult[0]);
                        if (percent < 1) {
                            dashboardView.setPercent((int) (30 + percent));
                        }
                        if (percent >= 1 && percent < 3) {
                            dashboardView.setPercent((int) (65 + percent));
                        }
                        if (percent >= 3 && percent < 5) {
                            dashboardView.setPercent((int) (75 + percent));
                        }
                        if (percent >= 5) {
                            dashboardView.setPercent((int) (90 + percent));
                        }
                        showToast("测试完成");
                        startSpeed.setEnabled(true);
                        startSpeed.getBackground().setAlpha(255);
                        startSpeed.setText("重新测速");
                    }
                })
                .setPindCmd("capi.hanmama.com")
                .setSpeedCount(5)
                .setSpeedTimeOut(5000)
                .builder();
        speedManager.startSpeed();
        startTimer();
    }

    public String fun2(double f) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }

}