package com.example.user.arcfinal;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.util.DisplayMetrics;

import java.io.StreamCorruptedException;
import java.net.Socket;

import android.widget.Chronometer;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by bayrammuradovmustafa on 4/29/16.
 */

public class Pop extends Activity {

    // variables
    long        lastPause;
    boolean     error;
    Thread      d;
    Socket      sock;
    AskClient   askClient;
    Chronometer myChrn;
    Button      stop;
    Toast       toast;
    TextView    questionID;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.popwindow);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        d = new Thread(  new Runnable () {

            @Override
            public void run()
            {
                try {
                    sock = new Socket(getIntent().getStringExtra( "SERVER_IP"), 9999);
                    askClient = new AskClient(sock);
                } catch (Exception e){}
            }
        });
        d.start();

        toast = Toast.makeText(Pop.this, null, Toast.LENGTH_SHORT);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        error = false;

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.92), (int) (height * 0.75));

        myChrn = (Chronometer) findViewById(R.id.chronometer);
        myChrn.setBase(SystemClock.elapsedRealtime());
        myChrn.start();

        stop = (Button) findViewById( R.id.stopTimer);
        questionID = (TextView) findViewById( R.id.questionId);
        questionID.setText( "ID: " + getIntent().getStringExtra( "id"));

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sock != null )
                {
                    lastPause = SystemClock.elapsedRealtime();
                    myChrn.stop();

                    Intent barActivity = new Intent (Pop.this, BarGraph.class);
                    try {
                        barActivity.putExtra("graph", askClient.getGraph( getIntent().getStringExtra( "id")));
                        sock.close();
                        askClient = null;
                    }
                    catch ( Exception e) {
                        error = true;
                        toast.setText( "Connection lost");
                        toast.show();
                        Pop.super.onBackPressed();
                    }

                    if ( !error)
                    startActivity( barActivity);
                }
                else
                {
                    toast.setText("No Internet connection");
                    toast.show();
                    Pop.super.onBackPressed();
                }
            }
        });
    }

    // overriding the backButton pressing method
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Question will be deleted, exit anyway?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            askClient.deleteQuestion(getIntent().getStringExtra("id"));
                            sock.close();
                            askClient = null;
                            Pop.super.onBackPressed();
                        } catch (Exception e) {
                            toast.setText( "Connection lost");
                            toast.show();

                            Pop.super.onBackPressed();}
                    }
                }).create().show();
    }
}
