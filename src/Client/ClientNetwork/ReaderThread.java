package Client.ClientNetwork;

import Client.ViewCarPanel.ViewCarController;
import Server.Car;
import javafx.scene.control.Alert;

public class ReaderThread extends Thread {

    private int index;

    public ReaderThread() {
        index = 0;
    }

    @Override
    public void run() {
        while (!ViewCarController.stop) {
            try {
                index = 0;
                while (true) {
                    Car car = (Car) ClientConnector.ois.readObject();
                    int x = ClientConnector.ois.readInt();
                    if (index < ViewCarController.AllCars.size()) {
                        ViewCarController.AllCars.remove(index);
                        ViewCarController.AllCars.add(index, car);
                    } else {
                        ViewCarController.AllCars.add(car);
                        ++index;
                    }
                    ++index;
                    if (x == 0) {
                        while (index < ViewCarController.AllCars.size()) {
                            ViewCarController.AllCars.remove(index);
                        }
                        break;
                    }

                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("We are Sorry");
                alert.setContentText("There are some issues that we need to fix");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }
}
