package com.example.user.arcfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Socket;

/**
 * Created by Kanan Asadov
 *            Bayram Muradov
 *            Ali Sabbagh
 */

public class AnswerActivity extends Activity {

    // constants
    final String SERVER_IP = "192.168.1.193";

    // variables
    String       qID;
    Button       go;
    Thread       d;
    Socket       sock;
    Toast        toast;
    EditText     qIdEditText;
    AnswerClient answerClient;

    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.answer_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // network thread
        d = new Thread(  new Runnable () {

            @Override
            public void run()
            {
                try {
                    sock = new Socket(SERVER_IP, 9999);
                    answerClient = new AnswerClient(sock);
                } catch (Exception e){}
            }
        });
        d.start(); // starting it

        sock = null;
        qIdEditText = (EditText) findViewById( R.id.enterIdField);
        go = (Button) findViewById( R.id.goButton);

        toast = Toast.makeText(AnswerActivity.this, null, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        // search for a question!
        go.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (sock != null) {
                    qID = qIdEditText.getText().toString();

                    try {
                        int result = answerClient.idFound(qID);
                        if (result == 1) { // if found
                            try {
                                Question q = answerClient.answerQuestion(qID);
                                Intent i = new Intent(AnswerActivity.this, QuestionAnsweringActivity.class);
                                i.putExtra("question", q);
                                i.putExtra("SERVER_IP", SERVER_IP);
                                i.putExtra("qID", qID);

                                startActivity(i);
                                try {
                                    sock.close();
                                    answerClient = null;
                                } catch (Exception e) {
                                }

                            } catch (Exception e) {
                                go.setText(e.toString());
                            }
                        } else if (result == -1) { // if not found
                            toast.setText("ID not found");
                            toast.show();
                        } else if (result == 0) { // if found but answered already
                            toast.setText("You have already answered this question");
                            toast.show();
                        }
                    } catch (Exception c) {
                        Toast tmp = Toast.makeText(AnswerActivity.this, null, Toast.LENGTH_SHORT);
                        tmp.setText( "Connection lost");
                        tmp.show();
                        AnswerActivity.super.onBackPressed();}
                }
                else{
                    toast.setText( "No internet access");
                    toast.show();
                }
            }
        });


    }
}
