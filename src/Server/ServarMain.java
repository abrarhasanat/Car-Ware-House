package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class SockInfo {
    Socket socket;
    ObjectOutputStream oos;

    public SockInfo(Socket socket) throws IOException {
        this.socket = socket;
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

}

public class ServarMain {

    static ArrayList<SockInfo> Clientlist = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
            Socket socket = serverSocket.accept();
            SockInfo sockInfo = new SockInfo(socket);
            Clientlist.add(sockInfo);
            System.out.println(socket);
            new ClientThread(sockInfo).start();
            //  new UpdateThread(socket).start();
        }
    }

    public static void SendUpdate() throws Exception {
        ArrayList<Car> cars = ServerDB.getCarListFromDB();
        System.out.println("Car Size : " + cars.size());
         for (SockInfo client : Clientlist) {
            for (int i = 0; i < cars.size(); ++i) {
                client.oos.writeObject(cars.get(i));
                int x = (i + 1) == cars.size() ? 0 : 1;
                client.oos.writeInt(x);
            }
            client.oos.flush();
        }
    }

    public static void SendUpdate(SockInfo sockInfo) throws Exception {
        ArrayList<Car> cars = ServerDB.getCarListFromDB();
        for (int i = 0; i < cars.size(); ++i) {
            sockInfo.oos.writeObject(cars.get(i));
            int x = (i + 1) == cars.size() ? 0 : 1;
            sockInfo.oos.writeInt(x);
        }
        sockInfo.oos.flush();
    }


}
