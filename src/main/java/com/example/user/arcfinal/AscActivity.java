package com.example.user.arcfinal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Kanan Asadov
 *            Bayram Muradov
 *            Ali Sabbagh
 */
public class AscActivity extends Activity {

    // constants
    final String SERVER_IP = "192.168.1.193";

    // variables
    int        idNumber;
    int        autoName;
    boolean    error;
    Button     addButton;
    Button     removeButton;
    Button     createButton;
    EditText   topic;
    Socket     sock;
    Thread     d;
    String     qID;
    Toast      toast;
    Toast      toast2;
    AskClient  askClient;

    LinearLayout        myLayout;
    ArrayList<EditText> editTexts;

    @Override
    protected void onCreate( Bundle s)
    {
        super.onCreate(s);
        setContentView(R.layout.ask_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // a network thread
        d = new Thread(  new Runnable () {
            @Override
            public void run()
            {
                try {
                    sock = new Socket(SERVER_IP, 9999);
                    askClient = new AskClient(sock);
                } catch (Exception e) {}
            }
        });
        d.start(); //starting it
        sock = null;

        // finding values by their id from XML
        topic = (EditText) findViewById( R.id.enterQuestion);
        addButton = (Button) findViewById(R.id.addAnswerButton);
        removeButton = (Button) findViewById( R.id.deleteAnswerButton);
        createButton = (Button) findViewById( R.id.createQuestion);
        myLayout = (LinearLayout) findViewById(R.id.answersLayout);
        autoName = 1;
        idNumber = -1;

        toast = Toast.makeText(AscActivity.this, null, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast2 = Toast.makeText(AscActivity.this, null, Toast.LENGTH_SHORT);
        toast2.setText( "No internet access");

        // an array list of all answers created
        editTexts = new ArrayList<EditText>();

        // add button - adds EditTexts
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( sock != null )
                    myLayout.addView(createNewEditText( "Enter an answer"), 0);
                else
                {
                    toast2.show();
                    try {
                        sock.close();
                    } catch (Exception e) { AscActivity.super.onBackPressed();}
                }
            }
        });

        // removes editTexts for answers
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( idNumber >= 0 && sock != null )
                {
                    myLayout.removeView(editTexts.get(idNumber));
                    editTexts.remove( idNumber);
                    idNumber--;
                }
            }
        });

        // creates a questions
        createButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                    if (editTexts.size() > 0) {
                        ArrayList<String> answers = new ArrayList<String>();

                        // putting all answers to the list
                        for (EditText k : editTexts) {
                            String tmp = k.getText().toString();
                            if (tmp == null || tmp.equals("")) // if no input - names them itself
                            {
                                answers.add(String.valueOf(autoName));
                                autoName++;
                            } else {
                                answers.add(k.getText().toString());
                                autoName++;
                            }
                        }
                        //setting the id and passing to the server
                        try {
                            error = false;
                            do {
                                qID = "" + (int) (Math.random() * 101);
                            } while (askClient.idFound(qID) == 1);
                            askClient.passQuestion(topic.getText().toString(), answers, qID);
                        } catch (StreamCorruptedException e) {
                            toast2.show();

                            error = true;

                            AscActivity.super.onBackPressed();
                        }
                        catch ( Exception e) { AscActivity.super.onBackPressed();}

                        try {
                            if ( !error) {
                                sock.close();
                                askClient = null;

                                Intent popUp = new Intent(AscActivity.this, Pop.class);
                                popUp.putExtra("SERVER_IP", SERVER_IP);
                                popUp.putExtra("id", qID);

                                startActivity(popUp);
                            }
                        } catch (Exception e) { AscActivity.super.onBackPressed();}

                    } else {
                        toast.setText("Add at least one answer");
                        toast.show();
                    }
            }
        });
    }
    // method to add an edit texts
    private TextView createNewEditText(String t) {
        EditText tmp = new EditText(this);

        editTexts.add(tmp);

        idNumber++;
        tmp.setId(idNumber);
        tmp.setHint(t);
        tmp.setHintTextColor(Color.WHITE);
        tmp.setTextColor( Color.WHITE);
        tmp.setGravity(Gravity.CENTER);

        return tmp;
    }
}
