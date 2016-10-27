package allan.mydccar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class Main5Activity extends Activity {
    private LineChartView chartView;
    private  ThingSpeakChannel tsChannel;
    private ThingSpeakLineChart tsChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        tsChannel = new ThingSpeakChannel(175423,"IX7NYHNA0C135TXT");
        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
            @Override
            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {
                getActionBar().setTitle(channelName);
                getActionBar().setSubtitle("Channel " + channelId);
                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
                Toast.makeText(Main5Activity.this, lastUpdate.toString(), Toast.LENGTH_LONG).show();
            }
        });
        tsChannel.loadChannelFeed();
        chartView = (LineChartView) findViewById(R.id.chart1);
        chartView.setZoomEnabled(false);
        chartView.setValueSelectionEnabled(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        tsChart = new ThingSpeakLineChart(175423,3);
        tsChart.setNumberOfEntries(10);
        tsChart.setValueAxisLabelInterval(5);
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
        tsChart.loadChartData();

    }
    public void refresh(View v){
        refresh();
    }
    private void refresh() {
        finish();
        Intent intent = new Intent(Main5Activity.this, Main5Activity.class);
        startActivity(intent);
    }
    public void back(View v) {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}