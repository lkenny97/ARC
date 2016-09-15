package com.example.user.arcfinal;

import java.net.Socket;
import java.util.*;

/**
 * Created by Kanan Asadov
 *            Bayram Muradov
 *            Ali Sabbagh
 */
public class AskClient extends Client
{
    ArrayList<String> answers;

    // constructor
    public AskClient(Socket socket) throws Exception
    {
        super( socket );
    }

    // methods

    //passes the question to the server by sending its data
    public void passQuestion( String topic, ArrayList<String> answers, String qID) throws Exception
    {
        try {
            oos.writeObject("CreateQuestion");
            oos.writeObject( topic);
            oos.writeObject( answers);
            oos.writeObject( qID);
            oos.flush();
        } catch (Exception e) {}
    }

    // gets an array of answers from the server
    public int[] getGraph( String id) throws Exception
    {
        oos.writeObject( "getGraph");
        oos.writeObject( id);
        oos.flush();
        return (int[]) ois.readObject();
    }

    // deletes a question from the server
    public void deleteQuestion( String id) throws Exception
    {
        oos.writeObject( "deleteQuestion");
        oos.writeObject( id);
        oos.flush();
    }
}