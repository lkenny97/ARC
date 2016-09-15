package com.example.user.arcfinal;

import java.net.*;
import java.util.*;

/**
 * Created by Kanan Asadov
 *            Bayram Muradov
 *            Ali Sabbagh
 */

public class AnswerClient extends Client
{
    // properties
    Question q;

    // constructor
    public AnswerClient( Socket socket) throws Exception
    {
        super( socket);
    }

    // methods
    public Question answerQuestion( String id ) throws Exception
    {
        oos.writeObject( "answerQuestion");
        oos.writeObject( id);

        String title = (String) ois.readObject();
        ArrayList<String> answers = (ArrayList<String>) ois.readObject();

        q = new Question(title, answers, id);
        return q;
    }

    // submitting the answer to the server
    public void submit( int i, String id ) throws Exception
    {
        oos.writeObject("submit");
        oos.writeObject(id);
        oos.writeInt(i);
        oos.flush();
    }
}