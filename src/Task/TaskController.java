package Task ;
import Client.Car;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskController implements Initializable {
    int DailyCounter = 0   ;
    double total  = 30.0  ;
    ObservableList<PieChart.Data> olist ;

    @FXML
    private ProgressBar Progress;

    @FXML
    private Label DayCount;

    @FXML
    private PieChart Pie;

    @FXML
    void check(ActionEvent event) {
        ++DailyCounter ;
        DataInitialize();
    }
    @FXML
    void exit(ActionEvent event) throws Exception {
        WriteData();
        Platform.exit();
    }
    void LoadData()  throws  Exception {
        File file = new File ("src\\Task\\Data.txt") ;
        System.out.println("hi");
        System.out.println(file.length());
        ObjectInputStream  ois = new ObjectInputStream(new FileInputStream(file));
        System.out.println("hello");
        DailyCounter = ois.readInt() ;
        System.out.println("kemon aso");
        ois.close();

    }

    void WriteData() throws Exception  {
        File file = new File("src\\Task\\Data.txt");
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeInt(DailyCounter) ;
        oos.flush();

    }
    void DataInitialize()  {
        Progress.setProgress((double)(DailyCounter) / (total));
        olist.clear();
        olist.add(new PieChart.Data("Day Passed " , (double)(DailyCounter))) ;
        olist.add(new PieChart.Data("Day left" , (double)(total - DailyCounter)))  ;
        Pie.setData(olist);
        DayCount.setText("Total Day Count: " + DailyCounter );
        System.out.println(DayCount);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        olist = FXCollections.observableArrayList();
        try {

            LoadData();
            DataInitialize() ;
        }catch (Exception e ) {
            e.printStackTrace();
        }
    }
}