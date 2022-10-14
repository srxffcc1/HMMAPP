package com.health.index.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.health.index.R;
import com.health.index.contract.ToolsGrowContract;
import com.healthy.library.model.ToolsGrow;
import com.healthy.library.model.ToolsGrowBase;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsGrowPresenter;
import com.health.index.widget.GrowMarkerView;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartHWFragment extends BaseFragment implements OnChartValueSelectedListener, ToolsGrowContract.View {
    private TextView chartYUnit;
    private LineChart chart;
    private TextView chartXUnit;
    private TextView chartDes;
    ToolsGrowPresenter toolsGrowPresenter;

    List<String> xvalue = new ArrayList<>();
    ArrayList<Entry> growvalues = new ArrayList<>();
    ArrayList<Entry> growvaluesMax = new ArrayList<>();
    ArrayList<Entry> growvaluesMin = new ArrayList<>();

    Map<Integer, ToolsGrowBase> baseGrowmap = new HashMap<>();
    private LinearLayout emptyGrow;

    public ChartHWFragment setChildId(String childId) {
        getBasemap().put("childId", childId);
        //System.out.println("设置child"+childId);
        return this;

    }

    public ChartHWFragment setBirthday(String birthday) {
        getBasemap().put("birthday", birthday);
        return this;
    }

    public ChartHWFragment refresh() {
        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tools_chart;
    }

    @Override
    protected void findViews() {
        initView();
        toolsGrowPresenter = new ToolsGrowPresenter(getActivity(), this);

        buildChartView();
//        buildChartData(null);
        getData();
        if ("height".equals(get("fargmentType"))) {
            chartYUnit.setText("cm");
        } else {
            chartYUnit.setText("kg");
        }
        chartXUnit.setText("");
//        chartDes.setText("年龄（月）");
    }

    @Override
    public void getData() {
        super.getData();
        //System.out.println("fragment:base");
        if(get("childId")!=null){
            toolsGrowPresenter.getNowBaseGrow(new SimpleHashMapBuilder<String, Object>()
                    .puts("type", "height".equals(get("fargmentType")) ? "1" : "2")
                    .puts("childId", get("childId").toString()));
        }



    }


    private void buildChartData(ArrayList<Entry> values, ArrayList<Entry> basevaluesMax, ArrayList<Entry> basevaluesMin) {

//        ArrayList<Entry> values = new ArrayList<>();
//        for (int i = 0; i < 120; i++) {
//            float val = (float) (Math.random() * 100) - 30;
//            values.add(new Entry(i, val));
//        }
        if (chart.getData() != null) {

            chart.getData().clearValues();
            chart.invalidate();
        }
        LineDataSet set;
        // create a dataset and give it a type
        ILineDataSet dataSets = getiLineDataSetsNoDash(values);

        ILineDataSet dataSetsMax = getiLineDataSets(basevaluesMax);

        ILineDataSet dataSetsMin = getiLineDataSets(basevaluesMin);

        // create a data object with the data sets
        LineData data = new LineData(dataSets,dataSetsMax,dataSetsMin);

        // set data
        chart.setData(data);
        chart.setVisibleXRangeMinimum(3);
        chart.setVisibleXRangeMaximum(5);
        chart.moveViewToX(data.getEntryCount());

    }

    @NotNull
    private ILineDataSet getiLineDataSets(ArrayList<Entry> values) {
        LineDataSet set;
        set = new LineDataSet(values, "DataSet 1");

        set.setDrawIcons(false);

        // draw dashed line
//        set.enableDashedLine(10f, 5f, 0f);

        // black lines and points

        set.setColor(Color.parseColor("#FFC276"));
        set.setCircleColor(Color.parseColor("#FFC276"));

        // line thickness and point size
        set.setLineWidth(1f);
        set.setCircleRadius(2f);
        set.setHighLightColor(Color.parseColor("#FFC276"));

        // draw points as solid circles
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        // customize legend entry
        set.setFormLineWidth(1f);
//        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);

        // text size of values
        set.setValueTextSize(9f);

        // draw selection line as dashed
        set.enableDashedHighlightLine(10f, 5f, 0f);
        set.setDrawValues(false);
        // set the filled area
        set.setDrawFilled(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
//        if (Utils.getSDKInt() >= 18) {
//            // drawables only supported on api level 18 and above
//            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_red);
//            set.setFillDrawable(drawable);
//        } else {
//            set.setFillColor(Color.BLACK);
//        }

//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set); // add the data sets
        return set;
    }

    @NotNull
    private ILineDataSet getiLineDataSetsNoDash(ArrayList<Entry> values) {
        LineDataSet set;
        set = new LineDataSet(values, "DataSet 1");

        set.setDrawIcons(false);

        // draw dashed line
//        set.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        set.setColor(Color.parseColor("#4DE4B6"));
        set.setCircleColor(Color.parseColor("#4DE4B6"));

        // line thickness and point size
        set.setLineWidth(1f);
        set.setCircleRadius(2f);
        set.setHighLightColor(Color.parseColor("#4DE4B6"));

        // draw points as solid circles
        set.setDrawCircleHole(false);
        set.setDrawValues(false);

        // customize legend entry
        set.setFormLineWidth(1f);
//            set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);

        // text size of values
        set.setValueTextSize(9f);

        // draw selection line as dashed
//            set.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        set.setDrawFilled(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
//        if (Utils.getSDKInt() >= 18) {
//            // drawables only supported on api level 18 and above
//            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_red);
//            set.setFillDrawable(drawable);
//        } else {
//            set.setFillColor(Color.BLACK);
//        }

//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set); // add the data sets
        return set;
    }

    private void buildChartView() {
        chart.setBackgroundColor(Color.WHITE);

        chart.setNoDataText("读取宝宝数据中...");

        // disable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // set listeners
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);

        // create marker to display box when values are selected
        GrowMarkerView mv = new GrowMarkerView(mContext, R.layout.custom_grow_marker_view);

        // Set the marker to the chart
        mv.setChartView(chart);
        chart.setMarker(mv);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setExtraRightOffset(25f);
        chart.setExtraLeftOffset(2f);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true);
//        chart.setScaleXEnabled(true);
//        chart.setScaleYEnabled(false);


//        //设置右侧y轴来显示出x轴
//        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setDrawAxisLine(true);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setTextColor(Color.TRANSPARENT);
//        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        rightAxis.setXOffset(15f);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
//            xAxis.setSpaceMin(15f);
            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
//            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (xvalue.size() > 0 && value >= 0) {
                        try {
                            return xvalue.get((int) value);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return   "";
                        }
                    } else {

                        return   "";
                    }
                }
            });
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();
            chart.getAxisRight().setEnabled(false);
            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis rangex

            if ("height".equals(get("fargmentType"))) {
                yAxis.setAxisMaximum(160f);
                yAxis.setGranularity(20f);
                yAxis.setAxisMinimum(40f);
            } else {
                yAxis.setGranularity(5f);
                yAxis.setAxisMaximum(40f);
                yAxis.setAxisMinimum(0f);
            }


            yAxis.setGranularityEnabled(true);
        }
    }

    public static ChartHWFragment newInstance(Map<String, Object> maporg) {
        ChartHWFragment fragment = new ChartHWFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        chartYUnit = (TextView) findViewById(R.id.chartYUnit);
        chart = (LineChart) findViewById(R.id.chart);
        chartXUnit = (TextView) findViewById(R.id.chartXUnit);
        chartDes = (TextView) findViewById(R.id.chartDes);
        emptyGrow = (LinearLayout) findViewById(R.id.emptyGrow);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void onSucessGetNowGrow(List<ToolsGrow> toolsGrows) {
        growvalues.clear();
        growvaluesMax.clear();
        growvaluesMin.clear();
        xvalue.clear();
        Collections.sort(toolsGrows, new Comparator<ToolsGrow>() {
            @Override
            public int compare(ToolsGrow o1, ToolsGrow o2) {
                long diff = o1.getrecordDateTime() - o2.getrecordDateTime();
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });
        for (int i = 0; i < toolsGrows.size(); i++) {
            ToolsGrow toolsGrow = toolsGrows.get(i);
            String[] arraydate=toolsGrow.recordDate.split("-");
            xvalue.add(arraydate[1]+"-"+arraydate[2]);
            //System.out.println("注入的value"+toolsGrow.recordDate);
            Date birthday = null;
            Date growday = null;
            try {
                birthday = new SimpleDateFormat("yyyy-MM-dd").parse(get("birthday").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                growday = new SimpleDateFormat("yyyy-MM-dd").parse(toolsGrow.recordDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int thisage = DateUtils.getAgeMonth(birthday, growday);
            try {
                growvaluesMax.add(new Entry(i, (float) baseGrowmap.get(thisage).positiveOneSd,"+2sd:"+ baseGrowmap.get(thisage).positiveOneSd));
                growvaluesMin.add(new Entry(i, (float) baseGrowmap.get(thisage).negativeOneSd,"-2sd:"+ baseGrowmap.get(thisage).negativeOneSd));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ("height".equals(get("fargmentType"))) {
                growvalues.add(new Entry(i, (float)toolsGrow.height));
            } else {
                growvalues.add(new Entry(i,  (float)toolsGrow.weight));
            }
        }
        if (growvalues.size() > 0) {
            buildChartData(growvalues, growvaluesMax, growvaluesMin);
        }

    }

    @Override
    public void onSucessGetNowBaseGrow(List<ToolsGrowBase> toolsGrowBases) {
        //System.out.println("fragment:base");
        if(toolsGrowBases!=null){
            if (toolsGrowBases.size() > 0) {
                emptyGrow.setVisibility(View.GONE);
                for (int i = 0; i < toolsGrowBases.size(); i++) {
                    ToolsGrowBase toolsGrowBase = toolsGrowBases.get(i);
                    baseGrowmap.put(toolsGrowBase.monthOfAge, toolsGrowBase);
                }
                toolsGrowPresenter.getNowGrow(new SimpleHashMapBuilder<String, Object>().puts("childId", get("childId")));
            } else {
                emptyGrow.setVisibility(View.VISIBLE);
            }
        }else {

            emptyGrow.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onSucessAddGrow() {

    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }
}
