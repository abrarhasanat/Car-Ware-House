package Server;

import java.io.ObjectInputStream;
import java.lang.ref.Cleaner;
import java.net.Socket;

public class ClientThread extends Thread {
    public SockInfo sockInfo ;
    public ClientThread(SockInfo sockInfo) {
        this.sockInfo = sockInfo ;
        System.out.println("Everything is ok so FAR");
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(sockInfo.socket.getInputStream());
            while (true) {
                int Case = ois.readInt();
                if(Case == Constant.ENTERASVIEWER) {
                    ServarMain.SendUpdate(sockInfo) ;
                }
                if(Case == Constant.CANILOGIN){
                    String username = (String) (ois.readObject()) ;
                    String password = (String) (ois.readObject()) ;
                    if(ServerDB.CanILogIn(username, password)) {
                        sockInfo.oos.writeInt(1) ;
                        ServarMain.SendUpdate(sockInfo);
                    }
                    else {
                        sockInfo.oos.writeInt(-1);
                        ServarMain.Clientlist.remove(sockInfo);
                        sockInfo.oos.close();
                        ois.close();
                        sockInfo.socket.close();
                        break;
                    }
                }
                if (Case == Constant.ADDCAR) {
                    Car car = (Car) ois.readObject();
                    System.out.println(car);
                    Case = -1;
                    ServerDB.AddCarToDB(car);
                   // ServarMain.SendUpdate();
                }
                if (Case == Constant.BUYCAR || Case == Constant.DELETECAR) {
                    String str = (String) ois.readObject();
                    ServerDB.DeleteCarFromDB(str);
                }
                if (Case == Constant.EDITCAR) {
                    Car car = (Car) ois.readObject();
                    ServerDB.EditCar(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
