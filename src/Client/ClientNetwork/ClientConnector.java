package Client.ClientNetwork;

import Client.Constant;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnector {
    public static ObjectInputStream ois  ;
    public static ObjectOutputStream oos ;
    public static Socket socket;
    public static void Connect() throws Exception {
        socket = new Socket("localhost", Constant.PORT);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream()) ;
        System.out.println("Connection Created");
    }


}
