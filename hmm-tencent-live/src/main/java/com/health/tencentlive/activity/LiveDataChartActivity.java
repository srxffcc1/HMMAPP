package com.health.tencentlive.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.LiveChartContract;
import com.health.tencentlive.model.LineChartModel;
import com.health.tencentlive.model.RoundChartModel;
import com.health.tencentlive.presenter.LiveChartPresenter;
import com.health.tencentlive.utils.DynamicLineChartManager;
import com.health.tencentlive.utils.MPChartHelper;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.LiveChartDateDialog;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.TopBar;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


@Route(path = TencentLiveRoutes.LIVEDATACHART)
public class LiveDataChartActivity extends BaseActivity implements OnChartValueSelectedListener, IsFitsSystemWindows
        , LiveChartContract.View {

    private TopBar topBar;
    private PieChart pieChartRound;
    private LineChart lineChart;
    private DynamicLineChartManager dynamicLineChartManager;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<String> quarters = new ArrayList<>();
    private TextView lookTotalNum;
    private TextView lookLiveTotalNum;
    private TextView lookVideoTotalNum;
    private ConstraintLayout left1;
    private TextView liveCount;
    private TextView liveCountPre;
    private View heightView;
    private ConstraintLayout right1;
    private LinearLayout bottomLiner;
    private TextView liveTimeCount;
    private TextView liveTimeCountPre;
    private ConstraintLayout left2;
    private TextView liveFanceNum;
    private TextView liveFanceNumPre;
    private ConstraintLayout right2;
    private TextView liveMoney;
    private TextView liveMoneyPre;
    private TextView LineSelectYear;
    private TextView userNum;

    private LiveChartDateDialog liveChartWheelDialog;
    private LiveChartPresenter liveChartPresenter;

    private List<String> xAxisValues = new ArrayList<>();//x轴图例集合
    private List<String> title = new ArrayList<>();//标题
    private List<List<Integer>> yAxisValues = new ArrayList<>();//Y轴数据集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    @Autowired
    String anchormanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_chart;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        liveChartPresenter = new LiveChartPresenter(this, this);
        initChartRound();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        liveChartPresenter.getYearData(new SimpleHashMapBuilder<String, Object>().puts("followedId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("year", Calendar.getInstance().get(Calendar.YEAR) + ""));
        liveChartPresenter.getLiveData(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId));
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        pieChartRound = (PieChart) findViewById(R.id.pieChartRound);
        lookTotalNum = (TextView) findViewById(R.id.lookTotalNum);
        lookLiveTotalNum = (TextView) findViewById(R.id.lookLiveTotalNum);
        lookVideoTotalNum = (TextView) findViewById(R.id.lookVideoTotalNum);
        left1 = (ConstraintLayout) findViewById(R.id.left1);
        liveCount = (TextView) findViewById(R.id.liveCount);
        liveCountPre = (TextView) findViewById(R.id.liveCountPre);
        heightView = (View) findViewById(R.id.heightView);
        right1 = (ConstraintLayout) findViewById(R.id.right1);
        liveTimeCount = (TextView) findViewById(R.id.liveTimeCount);
        liveTimeCountPre = (TextView) findViewById(R.id.liveTimeCountPre);
        left2 = (ConstraintLayout) findViewById(R.id.left2);
        liveFanceNum = (TextView) findViewById(R.id.liveFanceNum);
        liveFanceNumPre = (TextView) findViewById(R.id.liveFanceNumPre);
        right2 = (ConstraintLayout) findViewById(R.id.right2);
        bottomLiner = (LinearLayout) findViewById(R.id.bottomLiner);
        liveMoney = (TextView) findViewById(R.id.liveMoney);
        liveMoneyPre = (TextView) findViewById(R.id.liveMoneyPre);
        LineSelectYear = (TextView) findViewById(R.id.LineSelectYear);
        userNum = (TextView) findViewById(R.id.userNum);
        lineChart = (LineChart) findViewById(R.id.LineChart);
        LineSelectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBornDate(v);
            }
        });
        LineSelectYear.setText(Calendar.getInstance().get(Calendar.YEAR) + "年");
        lineChart.setNoDataText("暂无用户数据");
    }

    public void chooseBornDate(View view) {
        if (liveChartWheelDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration = currentMill - 10 * 12 * 31 * 24 * 60 * 60 * 1000L;//10年
            liveChartWheelDialog = LiveChartDateDialog.newInstance(currentMill, duration, currentMill, "选择年份");
            liveChartWheelDialog.setOnConfirmClick(new LiveChartDateDialog.OnSelectConfirm() {
                @Override
                public void onClick(String year) {
                    LineSelectYear.setText(year + "年");
                    liveChartPresenter.getYearData(new SimpleHashMapBuilder<String, Object>().puts("followedId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                            .puts("year", year));
                }
            });
        }
        liveChartWheelDialog.show(getSupportFragmentManager(), "bornDate");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void initChartRound() {
        pieChartRound.setUsePercentValues(true);
        pieChartRound.setNoDataText("暂无观看数据");
        pieChartRound.getDescription().setEnabled(false);
        pieChartRound.setExtraOffsets(0, 0, 0, 0);
        pieChartRound.setDragDecelerationFrictionCoef(0.95f);
        pieChartRound.setDrawHoleEnabled(true);
        pieChartRound.setHoleColor(Color.WHITE);
        pieChartRound.setTransparentCircleColor(Color.parseColor("#00000000"));
        pieChartRound.setTransparentCircleAlpha(0);
        pieChartRound.setHoleRadius(58f);
        pieChartRound.setTransparentCircleRadius(61f);
        pieChartRound.setDrawCenterText(false);
        pieChartRound.setRotationAngle(0);
        pieChartRound.setTouchEnabled(false);
        // enable rotation of the chart by touch
        pieChartRound.setRotationEnabled(true);
        pieChartRound.setHighlightPerTapEnabled(true);
        pieChartRound.setDrawEntryLabels(false);
        // add a selection listener
        pieChartRound.setOnChartValueSelectedListener(this);
        pieChartRound.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);
        Legend l = pieChartRound.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(15f);
        l.setTextSize(9f);
        l.setYEntrySpace(2f);
        l.setYOffset(2f);
        // entry label styling
        pieChartRound.setEntryLabelColor(Color.WHITE);
        pieChartRound.setEntryLabelTextSize(12f);

    }

    private void setChartRoundData(RoundChartModel roundChartModel) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (roundChartModel.liveTimesWatchedCount > 0) {
            float value = percentage(roundChartModel.liveTimesWatchedCount, roundChartModel.liveTimesWatchedCount + roundChartModel.liveTimesWatchedReplayCount);
            if (value > 0) {
                entries.add(new PieEntry(value, "直播"));
            }
        }
        if (roundChartModel.liveTimesWatchedReplayCount > 0) {
            float value = percentage(roundChartModel.liveTimesWatchedReplayCount, roundChartModel.liveTimesWatchedCount + roundChartModel.liveTimesWatchedReplayCount);
            if (value > 0) {
                entries.add(new PieEntry(value, "回看"));
            } else {
                entries.add(new PieEntry(0, "回看"));
            }
        }
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 0));
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#534BF5"));
        colors.add(Color.parseColor("#FFB001"));
        //colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChartRound));
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);
        pieChartRound.setData(data);
        // undo all highlights
        pieChartRound.highlightValues(null);
        pieChartRound.invalidate();
    }

//    private void initLineChart(List<Integer> list) {
//        //折线颜色
//        colour.add(Color.parseColor("#00A0E9"));
//        colour.add(Color.parseColor("#5EFF8F"));
//        colour.add(Color.parseColor("#945FEB"));
//        dynamicLineChartManager = new DynamicLineChartManager(lineChart, names, colour);
//        dynamicLineChartManager.setXAxis(quarters);
//        dynamicLineChartManager.setYAxis(getMaxNum(Collections.max(list)), 0, 5);
//        dynamicLineChartManager.setDescription("");
//
//    }

    @Override
    public void onSuccessGetLiveData(RoundChartModel result) {
        if (result != null) {
            setChartRoundData(result);
            lookTotalNum.setText(result.liveTimesWatchedReplayCount + result.liveTimesWatchedCount + "");
            lookLiveTotalNum.setText(result.liveTimesWatchedCount + "");
            lookVideoTotalNum.setText(result.liveTimesWatchedReplayCount + "");
            liveCount.setText(result.courseCount + "");
            liveFanceNum.setText(result.fansCount + "");
            liveTimeCount.setText(FormatRunTime(result.liveTimeCount));
        }
    }

    @Override
    public void onSuccessGetYearData(final LineChartModel model) {
        lineChart.clear();
        if (model != null && model.statisticsList != null) {
            xAxisValues.clear();
            title.clear();
            yAxisValues.clear();
            List<Integer> yAxisValues1 = new ArrayList<>();
            List<Integer> yAxisValues2 = new ArrayList<>();
            List<Integer> yAxisValues3 = new ArrayList<>();
            List<Integer> listMax = new ArrayList<>();
            for (int i = 0; i < model.statisticsList.size(); i++) {
                title.add(model.statisticsList.get(i).name);
                if (model.statisticsList.get(i).maxCount > 0) {
                    listMax.add(model.statisticsList.get(i).maxCount);
                }
                for (int j = 0; j < model.statisticsList.get(i).monthDataList.size(); j++) {
                    if (model.statisticsList.get(i).maxCount > 0) {
                        if (i == 0) {
                            yAxisValues1.add(model.statisticsList.get(i).monthDataList.get(j).count);
                        } else if (i == 1) {
                            yAxisValues2.add(model.statisticsList.get(i).monthDataList.get(j).count);
                        } else if (i == 2) {
                            yAxisValues3.add(model.statisticsList.get(i).monthDataList.get(j).count);
                        }
                    }
                }
            }
            for (int i = 0; i < model.statisticsList.get(0).monthDataList.size(); i++) {
                xAxisValues.add(model.statisticsList.get(0).monthDataList.get(i).monthName);
            }
            yAxisValues.add(yAxisValues1);
            yAxisValues.add(yAxisValues2);
            yAxisValues.add(yAxisValues3);
            //折线颜色
            colour.add(Color.parseColor("#00A0E9"));
            colour.add(Color.parseColor("#5EFF8F"));
            colour.add(Color.parseColor("#945FEB"));

            if (title.size() == 0 || listMax.size() == 0) {//这里表示全部数据都是0
                userNum.setVisibility(View.GONE);
                bottomLiner.setVisibility(View.GONE);
                lineChart.setNoDataText("暂无用户数据");
                return;
            } else {
                bottomLiner.setVisibility(View.VISIBLE);
                userNum.setVisibility(View.VISIBLE);
            }
            MPChartHelper.setLinesChart(lineChart, xAxisValues, yAxisValues, title, colour, getMaxNum(Collections.max(listMax)), 0, 5);

        } else {
            userNum.setVisibility(View.GONE);
            bottomLiner.setVisibility(View.GONE);
            lineChart.setNoDataText("暂无用户数据");
        }

    }

    private float percentage(int data, int total) {
        BigDecimal b = new BigDecimal(data);
        String result = b.divide(new BigDecimal(total), 3, BigDecimal.ROUND_HALF_UP).toString();

        DecimalFormat df = new DecimalFormat("0.###");
//        String result = new BigDecimal(data).divide(new BigDecimal(total), RoundingMode.UP).toString();
        return Float.parseFloat(df.format(new BigDecimal(result)));
    }


    public String FormatRunTime(long runTime) {
        if (runTime <= 0) {
            return "00:00:00";
        }

        long hour = runTime / 3600;
        long minute = (runTime % 3600) / 60;
        long second = runTime % 60;

        return unitTimeFormat(hour) + ":" + unitTimeFormat(minute) + ":" +
                unitTimeFormat(second);
    }

    private String unitTimeFormat(long number) {
        return String.format("%02d", number);
    }

    private int getMaxNum(int number) {
        if (number > 0 && number < 5000) {
            return 5000;
        }
        if (number > 5000 && number < 10000) {
            return 10000;
        }
        if (number > 10000 && number < 50000) {
            return 50000;
        }
        if (number > 50000 && number < 100000) {
            return 100000;
        }
        return number;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lineChart.clear();
        lineChart.removeAllViews();
        pieChartRound.clear();
        pieChartRound.removeAllViews();
    }
}
