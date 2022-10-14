package com.health.tencentlive.utils;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DynamicLineChartManager {

    private LineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();

    //一条曲线
    public DynamicLineChartManager(LineChart mLineChart, String name, int color) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initLineChart();
        initLineDataSet(name, color);
    }

    //多条曲线
    public DynamicLineChartManager(LineChart mLineChart, List<String> names, List<Integer> colors) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initLineChart();
        initLineDataSet(names, colors);
    }

    /**
     * 初始化LineChar
     */
    private void initLineChart() {
        lineChart.setNoDataText("暂无用户数据");
        lineChart.setEnabled(false);
        lineChart.setGridBackgroundColor(Color.parseColor("#00000000"));
        lineChart.setDrawGridBackground(true);//如果启用，chart绘图区后面的背景矩形将绘制
        lineChart.setDragEnabled(true);//启用/禁用拖动（平移）图表
        lineChart.setScaleEnabled(true);//启用/禁用缩放图表上的两个轴
        lineChart.setDrawBorders(false);//启用/禁用绘制图表边框（chart周围的线）
        lineChart.getAxisLeft().setDrawAxisLine(false);//去掉左边线
        lineChart.getXAxis().setGridColor(Color.parseColor("#00000000"));
        lineChart.getLegend().setEnabled(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        lineChart.getAxisRight().setEnabled(false);
//        //折线图例 标签 设置
//        Legend legend = lineChart.getLegend();
//        legend.setForm(Legend.LegendForm.SQUARE);
//        legend.setTextSize(11f);
//        //显示位置
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        legend.setDrawInside(false);

        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
    }

    /**
     * 初始化折线(一条线)
     *
     * @param name
     * @param color
     */
    private void initLineDataSet(String name, int color) {

        lineDataSet = new LineDataSet(null, name);
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setCircleRadius(1.5f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setHighLightColor(color);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置曲线填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //添加一个空的 LineData
        lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 初始化折线（多条线）
     *
     * @param names
     * @param colors
     */
    private void initLineDataSet(List<String> names, List<Integer> colors) {

        for (int i = 0; i < names.size(); i++) {
            LineDataSet lineDataSet = new LineDataSet(null, names.get(i));
            lineDataSet.setColor(colors.get(i));
            lineDataSet.setLineWidth(2.5f);
            lineDataSet.setHighLightColor(Color.parseColor("#00000000")); // 设置点击某个点时，横竖两条线的颜色
            lineDataSet.setDrawValues(false);//在点上显示数值 默认true
            lineDataSet.setDrawCircles(false);//在点上画圆 默认true
            lineDataSet.setDrawFilled(false);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSet.setValueTextSize(0f);
            lineDataSets.add(lineDataSet);

        }
        //添加一个空的 LineData
        lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 动态添加数据（一条折线图）
     *
     * @param number
     */
    public void addEntry(int number) {

        //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);
        Entry entry = new Entry(lineDataSet.getEntryCount(), number);
        lineData.addEntry(entry, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
        lineChart.setVisibleXRangeMaximum(10);
        //移到某个位置
        lineChart.moveViewToX(lineData.getEntryCount() - 5);
    }

    /**
     * 动态添加数据（多条折线图）
     *
     * @param numbers
     */
    public void addEntry(List<Integer> numbers) {
        if (numbers == null) {
            return;
        }
        if (lineDataSets.get(0).getEntryCount() == 0) {
            lineData = new LineData(lineDataSets);
            lineChart.setData(lineData);
        }
        for (int i = 0; i < numbers.size(); i++) {
            Entry entry = new Entry(lineDataSets.get(i).getEntryCount(), numbers.get(i));
            lineData.addEntry(entry, i);
            //System.out.println(entry.getX() + "  " + entry.getY());
            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(3);
        }
    }

    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setAxisLineWidth(1);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawTopYLabelEntry(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setTextColor(Color.parseColor("#868799"));
        leftAxis.setTextSize(11f);
        leftAxis.setAxisLineColor(Color.parseColor("#00000000"));
        leftAxis.setLabelCount(labelCount, false);
        rightAxis.setEnabled(false);
        lineChart.invalidate();
    }

    /**
     * 设置X轴值
     *
     * @param data
     */
    public void setXAxis(final List<String> data) {
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(data.size());
        xAxis.setTextColor(Color.parseColor("#868799"));
        xAxis.setTextSize(11f);
        xAxis.setAxisLineColor(Color.parseColor("#00000000"));
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value < 0) {
                    return "";
                }
                if (value < data.size()) {
                    return data.get((int) value);
                }
                return "";
            }
        };
        xAxis.setValueFormatter(formatter);
        lineChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }
}
