/**
 * Coded By: Hammad Ali Baig & Ali Sabbagh
 * 21500266 & 21500269
 * Professor David Davenport
 * Last edited: 27-April-2016
 * Project group g1D
 * ARC System
 * 
 * Question Class: holds information of question that is sent
 * between server and clients
 */

import java.util.ArrayList;
import java.io.*;

public class Question implements Serializable 
{
    // Properties
    private String title, id;
    private ArrayList<String> answers;
    private ArrayList<String> clientsIPs; // saves the IP's of devices that answered the question 
    private int[] responses;              // saves the number each answer was selected
    
    // Constructor
    public Question ( String title, ArrayList<String> answers, String id) 
    {
        if ( title.equals(null) || title.equals(""))
            this.title = "Choose the correct answer";
        else
            this.title = title;
        this.answers = answers;
        clientsIPs = new ArrayList<String>();
        this.id = id;
        responses = new int[ answers.size()];
    }
    
    // Methods
    
    // Getters
    /////////////////////////////////////////////////////////
    public String getId () {
        return id;
    }
    
    public String getTitle () {
        return title;
    }
    
    public ArrayList<String> getAnswers () {
        return answers;
    }
    
    public ArrayList<String> getIPs () {
        return clientsIPs;
    }
    
    public int[] getCounts()
    {
        return responses;
    }
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    
    // adds one to the number the answer was selected
    public void submit ( int answerIndex )
    {
        responses[answerIndex]++;
    }
    
    // checks if an ip answerd the question before
    public boolean ipAnswerd( String ip )
    {
        for ( String str : clientsIPs )
        {
            if ( str.equals(ip) )
                return true;
        }
        return false;
    }
    
    // returns a string representation of the question object
    public String toString()
    {
        String question = title + "\n";
        for ( int i = 0; i < answers.size(); i++ )
        {
            question = question + answers.get(i) + "\n";
        }
        return question;
    }
}