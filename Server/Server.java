/*
 * Coded By: Ali Sabbagh & Kenan Asadov
 * 21500269 & 21503382
 * Professor David Davenport
 * Last edited: 27-April-2016
 * Project group g1D
 * ARC System
 * 
 * Server Class: creates a server socket & listens to every
 * connection from client socket
 */

import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.*;


public class Server
{
    // arraylist that will save all questions asked until deleting
    static ArrayList<Question> questions = new ArrayList<Question>();
    
    public static void main(String args []) throws Exception
    {
        ServerSocket s = new ServerSocket(9999);
        // runs infinitely listening for clients requests
        while(true)
        {
            System.out.println("Server: waiting for a client");
            Socket sock = s.accept();
            // creating thread to hold client socket
            ServerThread st = new ServerThread(sock);
            System.out.println("connected to a client");
            st.start();
        }
    }
    
    // returns the question that has that specific ID
    public static Question getQuestion( String id )
    {
        if ( questions.size() > 0 )
        {
            for ( Question q : questions )
            {
                if ( q.getId().equals( id) )
                {
                    return q;
                }
            }
        }
        return null;
    }
    
    // inner class ServerThread
    public static class ServerThread extends Thread
    {
        // properties
        Socket sock;
        String clientIP;
        String title;
        ArrayList<String> answers;
        String questionID = "";
        String step;
        Question q = null;
        boolean teacher;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        
        // constructor
        public ServerThread( Socket sock)
        {
            this.sock = sock;
        }
        
        // run method
        public void run()
        {
            try
            {
                // saving ip of the client
                clientIP = sock.getRemoteSocketAddress().toString();
                int colonIndex = clientIP.indexOf(":");
                clientIP = clientIP.substring(1, colonIndex);
                
                oos = new ObjectOutputStream(sock.getOutputStream());
                ois = new ObjectInputStream(sock.getInputStream());
                do
                {
                    step =  (String) ois.readObject();
                    // creating question
                    if ( step.equalsIgnoreCase("CreateQuestion") )
                    {
                        // getting question information from client
                        teacher = true;
                        title = (String) ois.readObject();
                        answers = (ArrayList<String>) ois.readObject();
                        questionID = (String) ois.readObject();
                        System.out.println(questionID);
                        // creating and saving in the arraylist
                        q = new Question(title, answers, questionID);
                        questions.add(q);
                        System.out.println("Server: question created");
                        step = "end";
                    }
                    // deleting question specified by ID
                    else if ( step.equalsIgnoreCase("DeleteQuestion") )
                    {
                        questionID = (String) ois.readObject();
                        questions.remove(getQuestion(questionID));
                        System.out.println("question deleted 1");
                        step = "end";
                    }
                    // sending results to client
                    else if ( step.equalsIgnoreCase("GetGraph") )
                    {
                        questionID = (String) ois.readObject();
                        q = getQuestion(questionID);
                        int[] counts = q.getCounts();
                        oos.writeObject(counts);
                        oos.flush();
                        questions.remove(q);
                        step = "end";
                    }
                    // sending a question information to client
                    else if ( step.equalsIgnoreCase("AnswerQuestion") )
                    {
                        questionID = (String) ois.readObject();
                        q = getQuestion(questionID);
                        oos.writeObject(q.getTitle());
                        oos.flush();
                        oos.writeObject(q.getAnswers());
                        oos.flush();
                        step = "end";
                    }
                    // submitting client's answer to question
                    else if ( step.equalsIgnoreCase("Submit") )
                    {
                        questionID = (String) ois.readObject();
                        int answer = ois.readInt();
                        q = getQuestion(questionID);
                        q.getIPs().add(clientIP);
                        q.submit(answer);
                        step = "end";
                    }
                    // checking if given ID exists &&
                    // if client answerd the question before
                    else if ( step.equalsIgnoreCase("checkID") )
                    {
                        String checkID = (String) ois.readObject();
                        q = getQuestion( checkID );
                        if ( q == null )
                        {
                            oos.writeInt( -1 );
                            oos.flush();
                        }
                        else if ( q != null && q.ipAnswerd(clientIP) ) 
                        {
                            oos.writeInt( 0 );
                            oos.flush();
                        }
                        else
                        {
                            oos.writeInt( 1 );
                            oos.flush();
                        }
                    } 
                } while( ! step.equalsIgnoreCase("end") );
                sock.close();
            }
            catch ( Exception x )
            {
                if ( teacher )
                {
                    questions.remove(q);
                    System.out.println("Question deleted 2");
                }
                x.printStackTrace();
            }
        }
    }
}