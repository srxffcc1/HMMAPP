package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This IValueFormatter is just for convenience and simply puts a "%" sign after
 * each value. (Recommended for PieChart)
 *
 * @author Philipp Jahoda
 */
public class PercentFormatter extends ValueFormatter {

    public DecimalFormat mFormat;
    private PieChart pieChart;
    private boolean percentSignSeparated;

    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
        percentSignSeparated = true;
    }

    // Can be used to remove percent signs if the chart isn't in percent mode
    public PercentFormatter(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }

    // Can be used to remove percent signs if the chart isn't in percent mode
    public PercentFormatter(PieChart pieChart, boolean percentSignSeparated) {
        this(pieChart);
        this.percentSignSeparated = percentSignSeparated;
    }

    @Override
    public String getFormattedValue(float value) {
        NumberFormat nf = NumberFormat.getInstance();
        if (nf.format(value).equals("0")) {
            return "";
        } else {
            return nf.format(value) + (percentSignSeparated ? " %" : "%");
        }

    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // raw value, skip percent sign
            return mFormat.format(value);
        }
    }
}
