package com.example.user.arcfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mehdi.sakout.fancybuttons.FancyButton;


public class MainActivity extends AppCompatActivity {

    FancyButton askButton;
    FancyButton answerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askButton = (FancyButton) findViewById(R.id.askButton);
        answerButton = (FancyButton) findViewById( R.id.answerButton);

        // goes to the ask activity
        askButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent( v.getContext(),  AscActivity.class);
                startActivity( i);
            }
        });

        // go to answer activity
        answerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent x = new Intent( v.getContext(),  AnswerActivity.class);
                startActivity( x);
            }
        });
    }

}
