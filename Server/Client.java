/*
 * Coded By: Ali Sabbagh & Kenan Asadov
 * 21500269 & 21503382
 * Professor David Davenport
 * Last edited: 27-April-2016
 * Project group g1D
 * ARC System
 * 
 * Client Class: creates a socket and connects to server
 * on a specified ip
 */

import java.net.*;
import java.util.*;
import java.io.*;

public class Client
{
    public static void main(String args []) throws Exception
    {
        System.out.println("Client: connecting");
        Socket sock = new Socket("localhost",9999);
        Scanner scan = new Scanner(System.in);
        
        // Variables
        String step;
        String id = "";
        Question q = null;
        ObjectOutputStream oos  = new ObjectOutputStream(sock.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
        
        // Code
        do
        {
            System.out.println("Client: enter step");
            step = scan.next();
            oos.writeObject(step);
            oos.flush();
            // sending create request & information of question
            if ( step.equalsIgnoreCase("CreateQuestion") )
            {
                System.out.println( "enter Q title" );
                scan.nextLine();
                String title = scan.nextLine();;
                oos.writeObject(title);
                oos.flush();
                ArrayList<String> answers = new ArrayList<String>();
                scan.nextLine();
                answers.add(scan.nextLine());
                scan.nextLine();
                answers.add(scan.nextLine());
                scan.nextLine();
                answers.add(scan.nextLine());
                oos.writeObject(answers);
                oos.flush();
                System.out.println( "enter Q id" );
                id = scan.next();
                oos.writeObject(id);
                oos.flush();
            }
            // getting final result from server
            else if ( step.equalsIgnoreCase("GetGraph") )
            {
                System.out.println( "Enter q ID" );
                oos.writeObject(scan.next());
                oos.flush();
                int[] results = ( int[] ) ois.readObject();
                System.out.println( "Results: " + results.toString() );
                step = "end";
            }
            // getting a question from server
            else if ( step.equalsIgnoreCase("AnswerQuestion") )
            {
                id = scan.next();
                oos.writeObject(id);
                oos.flush();
                q = new Question( (String)ois.readObject(), (ArrayList<String>) ois.readObject(), id );
                System.out.println( q.toString() );
            }
            // submitting an answer to server
            else if ( step.equalsIgnoreCase("Submit") )
            {
                System.out.println( "Enter q ID" );
                scan.nextLine();
                id = scan.nextLine();
                oos.writeObject(id);
                oos.flush();
                System.out.println( "Enter answer number" );
                int answer = scan.nextInt();
                oos.writeInt(answer);
                oos.flush();
                step = "end";
            }
        } while( ! step.equalsIgnoreCase("end") );
        sock.close();
    }
    
}