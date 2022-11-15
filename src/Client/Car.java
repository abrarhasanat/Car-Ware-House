package Client;


import java.io.Serializable;

public class Car implements Serializable {
    private String registrationNumber;
    private String  yearMade;
    private String colour1, colour2, colour3;
    private String carMake;
    private String carModel;
    private String  price;
    private String Information;
    private String image  ;

    public Car() { }

    public Car(String Information) {
        this.Information = Information ;
        String[] data = Information.split(",");
        registrationNumber = data[0].toUpperCase();
        yearMade =data[1];
        colour1 = data[2].toUpperCase();
        colour2 = data[3].toUpperCase();
        colour3 = data[4].toUpperCase();
        carMake = data[5].toUpperCase();
        carModel = data[6].toUpperCase();
        price = data[7];

    }

    public Car(String [] data) {
        registrationNumber = data[0].toUpperCase();
        yearMade = data[1];
        colour1 = data[2].toUpperCase();
        colour2 = data[3].toUpperCase();
        colour3 = data[4].toUpperCase();
        carMake = data[5].toUpperCase();
        carModel = data[6].toUpperCase();
        price = data[7];

    }
    public void setRegistrationNumber(String Registration) {
        this.registrationNumber = Registration ;
    }
    public void setYearMade(String YearMade) { this.yearMade = YearMade ; }
    public void setCarMake(String carMake) { this.carMake =  carMake ; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    public void setPrice(String Price) { this.price = Price ;}
    public void setCarImage(String  image )   { this.image = image ; }

    public String toString() {
        return "Registration Number: " + registrationNumber + "\nYear Made: " + yearMade
                + "\nColours: " + colour1 + " , " + colour2 + " , " + colour3
                + "\nCar Make: " + carMake + "\nCarModel: " + carModel
                + "\nPrice: " + price + "\nImage: " + image;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public String getCarMake() {
        return carMake;
    }
    public String getCarModel() {
        return carModel;
    }
    public String getYearMade() { return yearMade ; }
    public String getPrice() { return price; }
    public String  getImage() { return  image ;}

    public String getInformation() {
        return Information;
    }
}
