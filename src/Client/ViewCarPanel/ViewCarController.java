package Client.ViewCarPanel;

import Server.Car;
import Client.ClientInit.Controller;
import Client.ClientInit.Main;
import Client.ClientNetwork.ClientConnector;
import Client.ClientNetwork.ReaderThread;
import Client.Constant;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewCarController implements Initializable {
    private FileChooser fileChooser = new FileChooser();
    private File file;
    public static long MinPrice ;
    public static long MaxPrice ;
    public static String PreferredRegNum, PreferredCarMake , PreferredCarModel ;
    public static ObservableList<Car> carlist;
    public static int index;
    public  static boolean stop ;
    //public static  ArrayList <Car> AllCars = Collections.synchronizedList( new ArrayList<Car>()) ;
    public static CopyOnWriteArrayList<Car> AllCars ;
    private void Reset() {
        MinPrice = (long) (-1e9) ;
        MaxPrice = (long)(1e9) ;
        PreferredCarMake = null ;
        PreferredCarModel = null ;
        PreferredRegNum = null ;
        index = 0 ;
        stop = false ;
        AllCars = new CopyOnWriteArrayList<>() ;
        carlist = FXCollections.observableArrayList();
    }
    @FXML
    private TextField QCarMake;
    @FXML
    private Button BuyCarButton;
    @FXML
    private TextField newCarReg;

    @FXML
    private TextField QReg;

    @FXML
    private TextField newCarModel;

    @FXML
    private TextField QMinPrice;

    @FXML
    private TextField newCarPrice;

    @FXML
    private Button AddCarButton;

    @FXML
    private TextField QCarModel;
    @FXML
    private ImageView tmpImage;

    @FXML
    private Button EditCarButton;
    @FXML
    private Label AddOrEdit;

    @FXML
    private TextField newCarMake;

    @FXML
    private Pane CarPane;

    @FXML
    private TextField newCarYearMade;

    @FXML
    private ImageView newImage;

    @FXML
    private Pane AddCarPane;

    @FXML
    private Button DeleteCarButton;

    @FXML
    private TextField QMaxPrice;

    @FXML
    void AddCar(ActionEvent event) {
        translateAnimation(0.5, AddCarPane, -1600);
        AddOrEdit.setText("   ADD CAR");
        AddCarNowButton.setText("ADD CAR NOW");
        newCarReg.setEditable(true);
    }

    @FXML
    void DeleteCar(ActionEvent event) {
        final int selectedIdx = FormListView.lv.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            String regNumber = FormListView.lv.getSelectionModel().getSelectedItem().getRegistrationNumber();
            final int newSelectedIdx =
                    (selectedIdx == FormListView.lv.getItems().size() - 1)
                            ? selectedIdx - 1
                            : selectedIdx;

            FormListView.lv.getItems().remove(selectedIdx);
            AllCars.remove(selectedIdx);
            FormListView.lv.getSelectionModel().select(newSelectedIdx);
            try {
                ClientConnector.oos.writeInt(Constant.DELETECAR);
                ClientConnector.oos.writeObject(regNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void LogOut(ActionEvent event) throws Exception {
        stop = true ;
        ClearAllField();
        FormListView.lv.getItems().clear();
        AllCars.clear();
        ClientConnector.oos.close();
        ClientConnector.ois.close();
        ClientConnector.socket.close();
        Main.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/Client/ClientInit/sample.fxml")));

    }

    @FXML
    void newImageUpload(ActionEvent event) {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg"));
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image img = new Image(file.toURI().toString(), 150, 150, true, true);
            newImage.setImage(img);
            newImage.setPreserveRatio(false);
        } else {
            System.out.println("photo could not be uploaded");
        }
    }

    @FXML
    private Button AddCarNowButton ;
    @FXML
    void EditCar(ActionEvent event) {
        if(FormListView.lv.getSelectionModel().getSelectedIndex() == -1)  {
            Alert.AlertType alertAlertType;
            Alert Alert = new Alert(AlertType.ERROR);
            Alert.setTitle("Sorry! Could no edit Car ");
            Alert.setContentText("Please select a  car first to edit");
            Alert.showAndWait() ;
            return;
        }
        translateAnimation(0.5, AddCarPane, -1600);
        AddOrEdit.setText("   EDIT CAR") ;
        AddCarNowButton.setText("COMMIT CHANGE");
        newCarReg.setEditable(false);
        Car car = FormListView.lv.getSelectionModel().getSelectedItem();
        newCarReg.setText(car.getRegistrationNumber());
        newCarYearMade.setText(car.getYearMade());
        newCarModel.setText(car.getCarModel());
        newCarMake.setText(car.getCarMake());
        newCarPrice.setText(car.getPrice());
        byte[] bytes = Base64.getDecoder().decode(car.getImage()) ;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            BufferedImage bimage = ImageIO.read(bis) ;
            newImage.setImage(SwingFXUtils.toFXImage(bimage, null)) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void SearchCar(ActionEvent event) {
        try {
            int xx = Integer.parseInt(QMaxPrice.getText());
            MaxPrice = xx;
        } catch (Exception e) {
            MaxPrice = (int ) 1e9 ;
            e.printStackTrace();
        }
        try {
            int xx = Integer.parseInt(QMinPrice.getText());
            MinPrice = xx;
        } catch (Exception e) {
            MinPrice  =  (int) -1e9 ;
            e.printStackTrace();
        }
        try {
            if (QReg.getText() != null && !QReg.getText().trim().isEmpty())
                PreferredRegNum = QReg.getText().toUpperCase();
            else PreferredRegNum = null ;
            if (QCarModel.getText() != null && !QCarModel.getText().trim().isEmpty())
                PreferredCarModel = QCarModel.getText().toUpperCase();
            else PreferredCarModel = null ;
            if (QCarMake.getText() != null && !QCarMake.getText().trim().isEmpty())
                PreferredCarMake = QCarMake.getText().toUpperCase();
            else PreferredCarMake  = null ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void AddNewCar(ActionEvent event) throws Exception {
        Car newCar = new Car();
        newCar.setRegistrationNumber(newCarReg.getText().toUpperCase());
        newCar.setYearMade(newCarYearMade.getText());
        newCar.setCarMake(newCarMake.getText());
        newCar.setCarModel(newCarModel.getText());
        newCar.setPrice(newCarPrice.getText());
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(newImage.getImage(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        String Base64Image = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();
        newCar.setCarImage(Base64Image);
        if(newCarReg.isEditable())
        ClientConnector.oos.writeInt(Constant.ADDCAR);
        else ClientConnector.oos.writeInt(Constant.EDITCAR);
        ClientConnector.oos.writeObject(newCar);
        ClearAllField();
        ClearAllField();
        translateAnimation(0.5, AddCarPane, 1600);

    }

    @FXML
    void BaccFromAddCar(ActionEvent event) {
        ClearAllField();
        translateAnimation(0.5, AddCarPane, 1600);
    }

    private void ClearAllField() {
        newCarReg.clear();
        newCarMake.clear();
        newCarModel.clear();
        newCarPrice.clear();
        newCarYearMade.clear();
        ;
        newImage.setImage(null);
        QCarMake.clear();
        QCarModel.clear();
        QMaxPrice.clear();
        QMinPrice.clear();
        QReg.clear();
    }

    public void translateAnimation(double duration, Node node, double byX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(byX);
        translateTransition.play();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Reset() ; 
        if (Controller.IamViewer) {
            AddCarButton.setVisible(false);
            EditCarButton.setVisible(false);
            DeleteCarButton.setVisible(false);
        }
        if(Controller.IAmManufacturer) {
            BuyCarButton.setVisible(false);
        }
        ClearAllField();
        translateAnimation(0.5, AddCarPane, 1600);
        translateAnimation(0.5, AddCarPane, 1600);
        FormListView.FormListViewInit();
        CarPane.getChildren().add(FormListView.lv);
        // KeepUpdated();
        new ReaderThread().start();
        new myAnim().start();
    }

    private void KeepUpdated() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                index = 0;
                try {
                    while (true) {
                        Car car = (Car) ClientConnector.ois.readObject();
                        int x = ClientConnector.ois.readInt();
                        if (index < ViewCarController.carlist.size()) {
                            ViewCarController.carlist.remove(index);
                            ViewCarController.carlist.add(index, car);
                        } else {
                            ViewCarController.carlist.add(car);
                        }
                        ++index;
                        if (x == 0) break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    boolean CanIShow(Car car) {
        if (Integer.parseInt(car.getPrice()) > MaxPrice) return false;
        if (Integer.parseInt(car.getPrice()) < MinPrice) return false;
        if (PreferredRegNum != null && !PreferredRegNum.equals(car.getRegistrationNumber().toUpperCase())) return false;
        if (PreferredCarModel != null && !PreferredCarModel.equals(car.getCarModel().toUpperCase())) return false;
        if (PreferredCarMake != null && !PreferredCarMake.equals(car.getCarMake().toUpperCase())) return false;
        return true;
    }


    private class myAnim extends AnimationTimer {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            if (now - lastUpdate >= 1000000000) {
                dohandle();
                lastUpdate = now;
            }
        }

        private void dohandle() {
            if (stop)
                stop();
            try {
                int PreffIndex  = 0  ;
                //  synchronized (AllCars)
                {
                    int selectedindex = FormListView.lv.getSelectionModel().getSelectedIndex();
                    for (index = 0; index < AllCars.size(); ++index) {
                        if(!CanIShow(AllCars.get(index))) continue;
                        if (PreffIndex < ViewCarController.carlist.size()) {
                            ViewCarController.carlist.remove(PreffIndex);
                            ViewCarController.carlist.add(PreffIndex, AllCars.get(index));
                        } else {
                            ViewCarController.carlist.add(AllCars.get(index));
                        }
                        ++PreffIndex ;
                        FormListView.lv.getSelectionModel().select(selectedindex);
                    }
                    while (PreffIndex < carlist.size()) carlist.remove(PreffIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
