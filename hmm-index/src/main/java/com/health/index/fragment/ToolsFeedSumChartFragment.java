package com.health.index.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.ToolsDiaryContract;
import com.healthy.library.model.ToolsSumGrow;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiaryPresenter;
import com.health.index.widget.GrowSumMarkerView;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.builder.SimpleHashMapBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToolsFeedSumChartFragment extends ToolsFeedBaseFragment implements ToolsDiaryContract.View , OnChartValueSelectedListener {


    private TextView chartYUnit;
    private BarChart chart;
    private TextView chartXUnit;
    private TextView chartDes;
    List<String> xvalue = new ArrayList<>();
    ToolsDiaryPresenter toolsDiaryPresenter;
    ArrayList<BarEntry> growvalues=new ArrayList<>();
    private XAxis xAxis;
    private YAxis yAxis;

    public static ToolsFeedSumChartFragment newInstance(Map<String, Object> maporg) {
        ToolsFeedSumChartFragment fragment = new ToolsFeedSumChartFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View getBottomView() {
        return null;
    }
    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }
    private List<ToolsSumGrow> resolveListData(String obj) {
        List<ToolsSumGrow> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsSumGrow>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void onSucessGetNowDiary(String result) {
        onSucessGetNowGrow(resolveListData(result));
    }

    private void onSucessGetNowGrow(List<ToolsSumGrow> toolsGrows) {
        growvalues.clear();
        xvalue.clear();
        Collections.sort(toolsGrows, new Comparator<ToolsSumGrow>() {
            @Override
            public int compare(ToolsSumGrow o1, ToolsSumGrow o2) {
                long diff = o1.getrecordDateTime() - o2.getrecordDateTime();
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return 0; //相等为0
            }
        });
        if(toolsGrows.size()>0){
            for (int i = 0; i < toolsGrows.size(); i++) {
                growvalues.add(new BarEntry(i,toolsGrows.get(i).amount));
                xvalue.add(toolsGrows.get(i).dayOfMonth);
            }
            buildChartData(growvalues);
        }else {

            chart.setNoDataText("没有统计数据...");
        }


    }

    @Override
    public void onSucessUpdateDiary() {

    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    @Override
    public void updateGrow() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tools_bar_chart;
    }

    @Override
    protected void findViews() {
        initView();
        toolsDiaryPresenter=new ToolsDiaryPresenter(getActivity(),this);
        buildChartView();
        getData();
        if("1".equals(get("recordType"))){
            chartDes.setText("母乳/次数");
            yAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getBarLabel(BarEntry barEntry) {
                    return ((int)barEntry.getY())+"";
                }
            });
        }
        if("2".equals(get("recordType"))){
            chartDes.setText("配方奶/ml");
        }
        if("3".equals(get("recordType"))){
            chartDes.setText("辅食/次数");
            yAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getBarLabel(BarEntry barEntry) {
                    return ((int)barEntry.getY())+"";
                }
            });
        }
        if("5".equals(get("recordType"))){
            chartDes.setText("小便/次数");
            yAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getBarLabel(BarEntry barEntry) {
                    return ((int)barEntry.getY())+"";
                }
            });
        }
        if("6".equals(get("recordType"))){
            chartDes.setText("大便/次数");
            yAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getBarLabel(BarEntry barEntry) {
                    return ((int)barEntry.getY())+"";
                }
            });
        }
        if("7".equals(get("recordType"))){
            chartDes.setText("睡觉/小时");

        }
    }

    @Override
    public void getData() {
        super.getData();
//        ArrayList<BarEntry> barEntries=new ArrayList<>();
//        for (int i = 0; i <20 ; i++) {
//            barEntries.add(new BarEntry(i,i));
//        }
//        buildChartData(barEntries);
        try {
            toolsDiaryPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>()
            .puts(Functions.FUNCTION,"9031")
                    .puts("recordType",get("recordType").toString())
                    .puts("childId",get("childId").toString())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        GrowSumMarkerView mv = new GrowSumMarkerView(mContext, R.layout.custom_grow_marker_view);

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
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
//            xAxis.setSpaceMin(15f);
            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setDrawGridLines(false);
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

        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();
            chart.getAxisRight().setEnabled(false);
            chart.getAxisLeft().setEnabled(false);
            yAxis.setDrawGridLines(false);
            // horizontal grid lines
            yAxis.setAxisMinimum(0f);
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis rangex

//            if (get("fargmentType").equals("height")) {
//                yAxis.setAxisMaximum(160f);
//                yAxis.setGranularity(20f);
//                yAxis.setAxisMinimum(40f);
//            } else {
//                yAxis.setGranularity(5f);
//                yAxis.setAxisMaximum(40f);
//                yAxis.setAxisMinimum(0f);
//            }


            yAxis.setGranularityEnabled(true);
        }
    }


    private void buildChartData(ArrayList<BarEntry>... values) {
        if (chart.getData() != null) {

            chart.getData().clearValues();
            chart.invalidate();
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i <values.length; i++) {
            BarDataSet set = new BarDataSet(values[i], "");
            set.setColor(Color.parseColor("#FF8E99"));
            dataSets.add(set);
        }
        BarData data = new BarData(dataSets);
        data.setDrawValues(true);
        data.setBarWidth(0.2f);

        if("1".equals(get("recordType"))){
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return (int)value+"";
                }
            });
        }
        if("2".equals(get("recordType"))){
        }
        if("3".equals(get("recordType"))){
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return (int)value+"";
                }
            });
        }
        if("5".equals(get("recordType"))){
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return (int)value+"";
                }
            });
        }
        if("6".equals(get("recordType"))){
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return (int)value+"";
                }
            });
        }
        if("7".equals(get("recordType"))){
        }

        data.setValueTextSize(11f);
        data.setValueTextColor(Color.parseColor("#444444"));
        chart.setData(data);
        chart.invalidate();
        chart.setVisibleXRangeMinimum(3);
        chart.setVisibleXRangeMaximum(7);
        chart.moveViewToX(data.getEntryCount());

    }

    private void initView() {
        chartYUnit = (TextView) findViewById(R.id.chartYUnit);
        chart = (BarChart) findViewById(R.id.chart);
        chartXUnit = (TextView) findViewById(R.id.chartXUnit);
        chartDes = (TextView) findViewById(R.id.chartDes);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
