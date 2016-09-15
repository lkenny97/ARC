package com.example.user.arcfinal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Kanan Asadov
 *            Bayram Muradov
 *            Ali Sabbagh
 */

public class QuestionAnsweringActivity extends Activity {

    int          idNumber;
    int          answer;
    boolean      error;
    Button       submit;
    RadioGroup   radioGroup;
    AnswerClient answerClient;
    Question     question;
    TextView     topic;
    Thread       d;
    Socket       sock;
    Toast        toast;

    ArrayList<RadioButton> radioButtons;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.question_answering_activity);

        toast = Toast.makeText(QuestionAnsweringActivity.this, null, Toast.LENGTH_SHORT);

        d = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sock = new Socket(getIntent().getStringExtra("SERVER_IP"), 9999);
                    answerClient = new AnswerClient(sock);
                } catch (Exception e) {
                    toast.setText( "No internet access");
                    toast.show();

                    QuestionAnsweringActivity.super.onBackPressed();
                }
            }
        });
        d.start();

        question = (Question) getIntent().getSerializableExtra("question");
        idNumber = 0;
        error = false;

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        submit = (Button) findViewById(R.id.submitAnswerButton);
        topic = (TextView) findViewById(R.id.questionTopic);
        radioButtons = new ArrayList<>();

        topic.setText(question.getTitle());

        // adding radio buttons to the group
        while (radioButtons.size() < question.getAnswers().size()) {
            RadioButton tmp = new RadioButton(this);
            tmp.setTag(idNumber);
            tmp.setHighlightColor( Color.WHITE);
            radioButtons.add(tmp);
            tmp.setText(question.getAnswers().get(idNumber));
            tmp.setTextColor(Color.WHITE);
            tmp.setTextSize( 18);
            idNumber++;
            tmp.setId(idNumber);
            tmp.setGravity(Gravity.CENTER);
            radioGroup.addView(tmp);
        }

        // submits the answer
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sock != null) {
                    if (radioGroup.getCheckedRadioButtonId() != -1) {

                        answer = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                        try {
                            answerClient.submit(answer, getIntent().getStringExtra("qID"));
                        } catch (Exception e) {
                            error = true;
                            toast.setText( "Connection lost");
                            toast.show();

                            QuestionAnsweringActivity.super.onBackPressed();
                        }

                        try {
                            sock.close();
                            answerClient = null;
                        } catch (Exception e) {
                            toast.setText( "No internet access");
                            toast.show();

                            QuestionAnsweringActivity.super.onBackPressed();
                        }

                        if ( !error) {
                            toast.setText("Answer submitted");
                            toast.show();

                            QuestionAnsweringActivity.super.onBackPressed();
                        }
                    } else {
                        CharSequence text = "Choose an answer";

                        toast.setText(text);
                        toast.show();
                    }
                }
                else {
                    toast.setText( "No internet connection");
                    toast.show();
                }
            }
        });
    }
}