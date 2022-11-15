package Client.ClientInit;

import Client.ClientNetwork.ClientConnector;
import Client.Constant;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedDeque;
//1100  700

public class Controller implements Initializable {
    public double InitPaneX = 400 , InitPaneY = 300 , HiddenX = 1600 , HiddenY = 300 ;
    public static Boolean IAmManufacturer  , IamViewer   ;
    @FXML
    private TextField UserNameOfMenfacturer;

    @FXML
    private Button viewer;

    @FXML
    private Pane EnterViwerPane;

    @FXML
    private Button menufacturer;

    @FXML
    private ImageView OpeningImage;

    @FXML
    private Pane EnterManPane;

    @FXML
    private TextField UserNameOfViewer;

    @FXML
    private Pane OpeningPane;

    @FXML
    private Button EnterAsMenufacturerButton;

    @FXML
    private Button EnterAsViwerButton;

    @FXML
    private PasswordField PassWord;

    @FXML
    void GetViewer(ActionEvent event) {
        translateAnimation(0.5 , EnterViwerPane , -1600) ;
      }


    @FXML
    void BackToHome1(ActionEvent event) {
        translateAnimation(0.5 , EnterViwerPane, 1600);

    }
    @FXML
    void BackToHome2(ActionEvent event) {
        translateAnimation(0.5 , EnterManPane, 1600);

    }
    @FXML
    void GetMan(ActionEvent event) {
        translateAnimation(0.5 ,EnterManPane, -1600);
    }

    @FXML
    void EnterAsViewer(ActionEvent event) throws Exception{
        IamViewer = true ;
        try {
            ClientConnector.Connect();
            ClientConnector.oos.writeInt(Constant.ENTERASVIEWER);
            ClientConnector.oos.flush();
            Main.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/Client/ViewCarPanel/ViewCar.fxml")));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.setTitle("Connection Error");
            alert.setHeaderText("Sorry! Could not Create a Connection");
            alert.showAndWait();
            e.printStackTrace();
        }

    }

    @FXML
    void EnterAsMenufacturer(ActionEvent event) throws IOException {
        IAmManufacturer = true;
        try {
            ClientConnector.Connect()  ;
            ClientConnector.oos.writeInt(Constant.CANILOGIN);
            ClientConnector.oos.writeObject(UserNameOfMenfacturer.getText());
            ClientConnector.oos.writeObject(PassWord.getText());
            int x  = ClientConnector.ois.readInt() ;
            if(x == 1 )
                Main.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/Client/ViewCarPanel/ViewCar.fxml")));
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR) ;
                alert.setTitle("Login Error!");
                alert.setHeaderText("Username or Password is incorrect");
                alert.setContentText("Please Enter a valid username and password");
                alert.showAndWait() ;
                ClientConnector.oos.close();
                ClientConnector.ois.close();
                ClientConnector.socket.close();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.setTitle("Connection Error");
            alert.setHeaderText("Sorry! Could not Create a Connection");
            alert.showAndWait();
            e.printStackTrace();
        }


    }
    public void translateAnimation(double duration, Node node, double byX){
        IamViewer = IAmManufacturer = false ;
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(duration) , node);
        translateTransition.setByX(byX);
        translateTransition.play();

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        translateAnimation(0.5,EnterViwerPane,1600);
        translateAnimation(0.5,EnterManPane,1600);

    }



}
