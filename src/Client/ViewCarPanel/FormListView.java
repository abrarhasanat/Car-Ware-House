package Client.ViewCarPanel;

import Server.Car;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class FormListView {
    public static ListView<Car> lv ;
    static class XCell extends ListCell<Car> {
        HBox hbox = new HBox();
        VBox Vbox = new VBox();
        Label label = new Label("(empty)");
        Label CarMake = new Label("empty");
        Label CarModel = new Label ("empty");
        Label YearMade = new Label("empty");
        Label price = new Label("empty");
        Pane pane = new Pane();
        Car lastItem;
        HBox hBox2 = new HBox();
        Image img = null ;
        ImageView imgView = new ImageView();
        Pane space = new Pane();
        public XCell() {
            super();
            hbox.setStyle("-fx-border-color: black; -fx-background-radius: 20px ; -fx-border-width: 10"  ) ;
            imgView.fitHeightProperty().bind(pane.heightProperty());
            imgView.fitWidthProperty().bind(pane.widthProperty());
            pane.setPrefSize(150 , 150) ;
            space.setPrefWidth(100);
            label.setStyle("-fx-font: bold; -fx-font-family:'Comic Sans MS'; -fx-font-size: 22px");
            YearMade.setStyle("-fx-font: bold; -fx-font-family:'Comic Sans MS'; -fx-font-size: 22px");
            CarMake.setStyle("-fx-font: bold; -fx-font-family:'Comic Sans MS'; -fx-font-size: 22px");
            CarModel.setStyle("-fx-font: bold; -fx-font-family:'Comic Sans MS'; -fx-font-size: 22px");
            price.setStyle("-fx-font: bold; -fx-font-family:'MV Boli'; -fx-font-size: 28px  ; -fx-alignment: center");
            price.setAlignment(Pos.CENTER);
            price.setTextFill(Color.RED);
            Vbox.setPrefHeight(150);
            Vbox.setPrefWidth(350);
            hBox2.setPrefHeight(150);
            hBox2.setPrefWidth(200);
            hBox2.getChildren().add(price) ;
            Vbox.getChildren().addAll(label , YearMade , CarMake , CarModel) ;
            pane.getChildren().add(imgView) ;
            hbox.getChildren().addAll( pane ,space, Vbox , hBox2);
        }
        @Override
        protected void updateItem(Car Item ,boolean empty) {
            super.updateItem(Item , empty);
            setText(null);
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = Item;
                label.setText(Item != null ? "Reg No: " +  Item.getRegistrationNumber() : "<null>");
                YearMade.setText(Item != null ?"Year Made: " + Item.getYearMade() : "<null>");
                CarMake.setText(Item != null ? "Car Make: " +Item.getCarMake() : "<null>");
                CarModel.setText(Item != null ?"Car Model: "+ Item.getCarModel() : "<null>");
                price.setText(Item != null ?  Item.getPrice() + "$" : "<null>");
                if(Item != null)  {
                    byte[] bytes = Base64.getDecoder().decode(Item.getImage()) ;
                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    try {
                        BufferedImage bimage = ImageIO.read(bis) ;
                        img = SwingFXUtils.toFXImage(bimage, null);
                        imgView.setImage(img) ;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                setGraphic(hbox);
            }
        }

    }
    public static void  FormListViewInit() {
        lv = new ListView<>(ViewCarController.carlist)   ;
        lv.setCellFactory(new Callback<ListView<Car>, ListCell<Car>>() {
            @Override
            public ListCell<Car> call(ListView<Car> param) {
                return new XCell();
            }
        });
        lv.setPrefWidth(920);
        lv.setPrefHeight(550);
    }


}
