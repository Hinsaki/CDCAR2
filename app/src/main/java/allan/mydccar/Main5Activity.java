package allan.mydccar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.ThingSpeakLineChart;
import com.macroyau.thingspeakandroid.model.ChannelFeed;

import java.util.Calendar;
import java.util.Date;

import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class Main5Activity extends ActionBarActivity  {
    private LineChartView chartView;
    private LineChartView chartView2;
    private LineChartView chartView3;
    private LineChartView chartView4;
    private ThingSpeakChannel tsChannel;
    private ThingSpeakLineChart tsChart;
    private ThingSpeakLineChart tsChart2;
    private ThingSpeakLineChart tsChart3;
    private ThingSpeakLineChart tsChart4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        tsChannel = new ThingSpeakChannel(173441,"GSTUCV50DKHDP1J3");
        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
            @Override
            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {
                getSupportActionBar().setTitle(channelName);
                getSupportActionBar().setSubtitle("Channel " + channelId);
                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
                Toast.makeText(Main5Activity.this, lastUpdate.toString(), Toast.LENGTH_LONG).show();
            }
        });
        tsChannel.loadChannelFeed();
        chartView = (LineChartView) findViewById(R.id.chart1);
        chartView.setZoomEnabled(false);
        chartView.setValueSelectionEnabled(true);
        chartView2 = (LineChartView) findViewById(R.id.chart2);
        chartView2.setZoomEnabled(false);
        chartView2.setValueSelectionEnabled(true);
        chartView3 = (LineChartView) findViewById(R.id.chart3);
        chartView3.setZoomEnabled(false);
        chartView3.setValueSelectionEnabled(true);
        chartView4 = (LineChartView) findViewById(R.id.chart4);
        chartView4.setZoomEnabled(false);
        chartView4.setValueSelectionEnabled(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        tsChart = new ThingSpeakLineChart(173441,1);
        tsChart.setNumberOfEntries(200);
        tsChart.setValueAxisLabelInterval(10);
        tsChart.setDateAxisLabelInterval(1);
        tsChart.useSpline(true);
        tsChart.setLineColor(Color.parseColor("#D32F2F"));
        tsChart.setAxisColor(Color.parseColor("#455a64"));
        tsChart.setChartStartDate(calendar.getTime());
        tsChart.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                chartView.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView.setCurrentViewport(initialViewport);
            }

        });
        tsChart2 = new ThingSpeakLineChart(173441,2);
        tsChart2.loadChartData();
        tsChart2.setNumberOfEntries(200);
        tsChart2.setValueAxisLabelInterval(10);
        tsChart2.setDateAxisLabelInterval(1);
        tsChart2.useSpline(true);
        tsChart2.setLineColor(Color.parseColor("#00ff2a"));
        tsChart2.setAxisColor(Color.parseColor("#455a64"));
        tsChart2.setChartStartDate(calendar.getTime());
        tsChart2.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                chartView2.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView2.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView2.setCurrentViewport(initialViewport);
            }

        });
        tsChart3 = new ThingSpeakLineChart(173441,3);
        tsChart3.loadChartData();
        tsChart3.setNumberOfEntries(200);
        tsChart3.setValueAxisLabelInterval(10);
        tsChart3.setDateAxisLabelInterval(1);
        tsChart3.useSpline(false);
        tsChart3.setLineColor(Color.parseColor("#0021ff"));
        tsChart3.setAxisColor(Color.parseColor("#455a64"));
        tsChart3.setChartStartDate(calendar.getTime());
        tsChart3.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                chartView3.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView3.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView3.setCurrentViewport(initialViewport);
            }

        });
        tsChart4 = new ThingSpeakLineChart(173441,4);
        tsChart4.loadChartData();
        tsChart4.setNumberOfEntries(200);
        tsChart4.setValueAxisLabelInterval(10);
        tsChart4.setDateAxisLabelInterval(1);
        tsChart4.useSpline(true);
        tsChart4.setLineColor(Color.parseColor("#ff006e"));
        tsChart4.setAxisColor(Color.parseColor("#455a64"));
        tsChart4.setChartStartDate(calendar.getTime());
        tsChart4.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                chartView4.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView4.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView4.setCurrentViewport(initialViewport);
            }

        });
        tsChart.loadChartData();
        tsChart2.loadChartData();
        tsChart3.loadChartData();
        tsChart4.loadChartData();

    }
    public void refresh(View v){
        refresh();
    }
    private void refresh() {
        Main5Activity.this.recreate();
    }
    public void back(View v) {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}