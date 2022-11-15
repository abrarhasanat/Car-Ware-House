package Server;


import java.sql.*;
import java.util.ArrayList;

public class ServerDB {
    public static Connection con = null;

    public static synchronized void ConnetToDB() throws Exception {
        con = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwarehouse?serverTimezone=UTC", "root", "");

    }

    public static synchronized void AddCarToDB(Car car) throws Exception {
        if(isThereAnyCar(car.getRegistrationNumber())) return;
        ConnetToDB();
        PreparedStatement ps = null;
        String sql = "INSERT INTO cars (reg ,year ,make ,model ,price , image) VALUES (?,?,?,?,?,?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, car.getRegistrationNumber());
        ps.setString(2, car.getYearMade());
        ps.setString(3, car.getCarMake());
        ps.setString(4, car.getCarModel());
        ps.setString(5, car.getPrice());
        ps.setString(6, car.getImage());
      if(  ps.executeUpdate()  > 0  ) System.out.println( "Successfully" ) ;
        con.close();
        ServarMain.SendUpdate();

    }

    public static synchronized void AddManufacturerToDB(String userName, String passWord) throws Exception {
        ConnetToDB();
        PreparedStatement ps = null;
        String sql = "INSERT INTO userdata (username , password) VALUES(? ,  ?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, passWord);
        ps.executeUpdate() ;
        con.close();


    }

    public static synchronized void DeleteCarFromDB(String RegNumber) throws Exception {
        ConnetToDB();
        String sql = "DELETE FROM cars WHERE reg = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, RegNumber);
        int res = ps.executeUpdate();
        if(res >0  )  System.out.println("Car deleted") ;
        con.close();
        ServarMain.SendUpdate();
        System.out.println("Update sent");
    }

    public static  synchronized boolean isThereAnyCar(String regNumber) throws Exception {
        ConnetToDB();
        String sql = "SELECT *  FROM cars WHERE reg = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet res = null;
        ps.setString(1, regNumber);
        res = ps.executeQuery();
        boolean pass = res.next();
        con.close() ;
        return pass ;
    }

    public static synchronized ArrayList<Car> getCarListFromDB() throws Exception {
        ConnetToDB();
        ArrayList<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet res = null;
        res = ps.executeQuery();
        while (res.next()) {
            Car car = new Car();
            car.setRegistrationNumber(res.getString("reg"));
            car.setYearMade(res.getString("year"));
            car.setCarMake(res.getString("make")) ;
            car.setCarModel(res.getString("model"));
            car.setPrice(res.getString("price"));
            car.setCarImage(res.getString("image"));
            cars.add(car) ;
        }
        return  cars ;


    }

    public static synchronized boolean CanILogIn(String UserName, String PassWord) throws Exception {
        ConnetToDB();
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            if (con == null) ConnetToDB();
            String sql = "SELECT * FROM userdata WHERE username=? and password=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, UserName);
            ps.setString(2, PassWord);
            res = ps.executeQuery();
            System.out.println(res);
            if (res.next()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

    public static  synchronized void EditCar(Car car) throws  Exception{
        ConnetToDB();
        PreparedStatement ps = null;
        String sql = "UPDATE  cars SET  year = ?, make = ?, model = ?, price =  ?, image = ? WHERE reg = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, car.getYearMade());
        ps.setString(2, car.getCarMake());
        ps.setString(3, car.getCarModel());
        ps.setString(4, car.getPrice());
        ps.setString(5, car.getImage());
        ps.setString(6, car.getRegistrationNumber());
        if(  ps.executeUpdate()  > 0  ) System.out.println( "Successfully" ) ;
        con.close();
        ServarMain.SendUpdate();
    }

}
