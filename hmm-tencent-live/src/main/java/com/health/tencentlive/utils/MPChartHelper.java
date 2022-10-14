package com.health.tencentlive.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


public class MPChartHelper {

    /**
     * 绘制线图，默认最多绘制三种颜色。所有线均依赖左侧y轴显示。
     *
     * @param lineChart
     * @param xAxisValue   x轴的轴
     * @param yXAxisValues y轴的值
     * @param titles       每一个数据系列的标题
     * @param lineColors   线的颜色数组。为null时取默认颜色，此时最多绘制三种颜色。
     */
    public static void setLinesChart(LineChart lineChart, final List<String> xAxisValue, List<List<Integer>> yXAxisValues,
                                     List<String> titles, List<Integer> lineColors, float max, float min, int labelCount) {
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
        lineChart.getAxisRight().setEnabled(false);
        //保证Y轴从0开始，不然会上移一点
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisRight().setAxisMinimum(0f);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        //x坐标轴设置
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value < 0) {
                    return "";
                }
                if (value < xAxisValue.size()) {
                    return xAxisValue.get((int) value);
                }
                return "";
            }
        };
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(xAxisValue.size());
        xAxis.setTextColor(Color.parseColor("#868799"));
        xAxis.setTextSize(11f);
        xAxis.setAxisLineColor(Color.parseColor("#00000000"));
        xAxis.setValueFormatter(formatter);

        //y轴设置
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setDrawLimitLinesBehindData(false);
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

//        //图例设置
//        Legend legend = lineChart.getLegend();
//        legend.setEnabled(false);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        legend.setDrawInside(false);
//        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        legend.setForm(Legend.LegendForm.LINE);
//        legend.setTextSize(12f);

        setLinesChartData(lineChart, yXAxisValues, titles, lineColors);
    }

    private static void setLinesChartData(LineChart lineChart, List<List<Integer>> yXAxisValues, List<String> titles, List<Integer> lineColors) {

        List<List<Entry>> entriesList = new ArrayList<>();
        for (int i = 0; i < yXAxisValues.size(); ++i) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int j = 0, n = yXAxisValues.get(i).size(); j < n; j++) {
                entries.add(new Entry(j, yXAxisValues.get(i).get(j)));
            }
            entriesList.add(entries);
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < entriesList.size(); ++i) {
            LineDataSet set = new LineDataSet(entriesList.get(i), titles.get(i));
            set.setColor(lineColors.get(i));
            set.setLineWidth(2.5f);
            //set.setHighLightColor(Color.parseColor("#00000000")); // 设置点击某个点时，横竖两条线的颜色
            set.setHighLightColor(lineColors.get(i)); // 设置点击某个点时，横竖两条线的颜色
            set.setDrawValues(true);//在点上显示数值 默认true
            set.setDrawCircles(true);//在点上画圆 默认true
            set.setDrawFilled(true);
            set.setDrawCircleHole(true);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setValueTextSize(9f);
            set.setValueTextColor(lineColors.get(i));
            set.setLabel(null);
            dataSets.add(set);
        }

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.getData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
    }
}
