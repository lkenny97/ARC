package com.example.user.arcfinal;

/**
 * Created by bayrammuradovmustafa on 4/29/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

//Library WAS TAKEN FROM ==>
// ==> https://github.com/PhilJay/MPAndroidChart/tree/master/MPChartLib/src/com/github/mikephil/charting
//and belongs to its rightful owner

public class BarGraph extends AppCompatActivity {

    float[] arrayData;
    int[] intArrayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

        BarChart barChart = (BarChart) findViewById(R.id.chart);

        intArrayData = getIntent().getIntArrayExtra( "graph") ;
        int numberOfChoices = intArrayData.length;
        arrayData = new float[ intArrayData.length];

        for ( int i = 0; i < intArrayData.length; i++)
        {
            arrayData[i] = intArrayData[i];
        }

        ArrayList<BarEntry> entries = new ArrayList<>();

        //adding the data from server
        for(int i =0; i < arrayData.length; i++) {
            entries.add(new BarEntry(arrayData[i], i));
        }

        BarDataSet dataset = new BarDataSet(entries, "# of responses");

        ArrayList<String> labels = new ArrayList<String>();

        for(int i =0; i < numberOfChoices; i++ ) {
            labels.add("resp - " + (i+1) );
        }

        BarData data = new BarData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //colored
        barChart.setData(data);
        barChart.animateY(5000);

    }

    // overriding the pressing back button method
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage( "Graph will be lost, exit anyway?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        BarGraph.super.onBackPressed();
                    }
                }).create().show();
    }
}// end of the class

