package Task ;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;


public class TaskMain extends Application {
    public static Stage stage = null;
    private double xoffset ;
    private double yoffset ;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Task/taskXml.fxml"));
        Parent pane= loader.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        this.stage = primaryStage;
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
