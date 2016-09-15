package com.example.user.arcfinal;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Created by Kanan Asadov
 *            Bayram Muradov
 *            Ali Sabbagh
 */
public abstract class Client //for ask client and answer client
{
    // properties
    Socket sock;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public Client( Socket sock) throws Exception
    {
        this.sock = sock;
        oos = new ObjectOutputStream(sock.getOutputStream());
        ois = new ObjectInputStream(sock.getInputStream());
    }

    //checks whether there is a question with specific id in the server
    public int idFound( String id ) throws Exception
    {
        oos.writeObject("checkID");
        oos.writeObject( id);
        oos.flush();
        return ois.readInt();
    }
}