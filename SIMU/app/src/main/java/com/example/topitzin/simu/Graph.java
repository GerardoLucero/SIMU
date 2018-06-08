package com.example.topitzin.simu;

import android.app.Activity;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gerardo on 20/04/2017.
 */

public class Graph {

    GraphView graph;
    DataPoint[] data;
    Activity a;

    public Graph(Activity activity, DataPoint[] d){
        a = activity;
        data = d;
        graph = (GraphView) activity.findViewById(R.id.graph);
        initGraph(graph);
    }

    public void initGraph(GraphView graph) {

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);

        series.setDrawBackground(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);


        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not nessecary
        graph.getGridLabelRenderer().setHumanRounding(false);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(a.getApplicationContext(), String.valueOf(dataPoint.getY()) , Toast.LENGTH_SHORT).show();
            }
        });

    }


}
