package Server;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class UpdateThread extends Thread {
    public Socket socket;

    public UpdateThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                ArrayList <Car> cars  = ServerDB.getCarListFromDB() ;
                for(int i  = 0 ; i < cars.size() ; ++i ) {
                    oos.writeObject(cars.get(i));
                    int x = (i + 1) == cars.size() ?  0 :  1  ;
                    oos.writeInt(x);
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
