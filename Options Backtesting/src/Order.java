import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

/*
 * Represents a call/put order from the market
 */
public class Order implements Comparable<Order> {

    //<editor-fold desc="variables">
    private final LocalDateTime dateTime;
    private final int strike;
    private final double close;
    private final double underlying;
    private final LocalDate expiry;
    private final double IV;
    private final double delta;
    private final double gamma;
    private final double vega;
    private final double theta;
    private final double rho;
    private final String type;
    //</editor-fold>

    //<editor-fold desc="constructor">
    public Order(LocalDateTime dateTime, int strike, double close, double underlying, LocalDate expiry, double IV, double delta, double gamma, double vega, double theta, double rho, String type) {
        this.dateTime = dateTime;
        this.strike = strike;
        this.close = close;
        this.underlying = underlying;
        this.expiry = expiry;
        this.IV = IV;
        this.delta = delta;
        this.gamma = gamma;
        this.vega = vega;
        this.theta = theta;
        this.rho = rho;
        this.type = type;
    }
    //</editor-fold>

    //<editor-fold desc="getters">
    public LocalDateTime getDateTime() {
        return dateTime;
    }


    public int getStrike() {
        return strike;
    }

    public double getClose() {
        return close;
    }

    public double getUnderlying() {
        return underlying;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public double getIV() {
        return IV;
    }

    public double getDelta() {
        return delta;
    }

    public double getGamma() {
        return gamma;
    }

    public double getVega() {
        return vega;
    }

    public double getTheta() {
        return theta;
    }

    public double getRho() {
        return rho;
    }
    //</editor-fold>

    //<editor-fold desc="toString">
    @Override
    public String toString() {
        return type + "{" +
                "date=" + dateTime +
                ", strike=" + strike +
                ", close=" + close +
                ", underlying=" + underlying +
                ", expiry=" + expiry +
                ", IV=" + IV +
                ", delta=" + delta +
                ", gamma=" + gamma +
                ", vega=" + vega +
                ", theta=" + theta +
                ", rho=" + rho +
                '}';
    }
    //</editor-fold>

    // Parses input string line
    public static Order parseLine(String line, String orderType) {
        Scanner s = new Scanner(line);
        s.useDelimiter(",|%,"); //Use , delimiter and ignore % sign on IV
        String dateString = "";
        String timeString = "";
        int strike = 0;
        double close = 0;
        double underlying = 0;
        String expiryString = "";
        double IV = 0;
        double delta = 0;
        double gamma = 0;
        double vega = 0;
        double theta = 0;
        double rho = 0;
        // Iterates while next token present
        while (s.hasNext()) {
            // Updates each value with next token
            dateString = s.next();
            timeString = s.next();
            strike = s.nextInt();
            close = s.nextDouble();
            if (orderType.equals("call")) { //Call/put different column position
                underlying = s.nextDouble();
                expiryString = s.next();
            }
            if (orderType.equals("put")) {
                expiryString = s.next();
                underlying = s.nextDouble();
            }
            IV = s.nextDouble();
            delta = s.nextDouble();
            gamma = s.nextDouble();
            vega = s.nextDouble();
            theta = s.nextDouble();
            rho = s.nextDouble();
        }
        if (orderType.equals("call")) {
            IV = IV / 100; //Ensures both call/put IV are decimal
        }
        s.close();
        LocalDate date;
        LocalTime time = LocalTime.of(0, 0, 0); //Initialise to 0
        LocalDate expiry;
        try {
            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M-dd-yyyy"));
        } catch (DateTimeParseException e) { //Date in other format
            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/dd/yyyy"));
        }
        if (orderType.equals("call")) {
            time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        if (orderType.equals("put")) {
            time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH));
        }
        try {
            expiry = LocalDate.parse(expiryString, DateTimeFormatter.ofPattern("M/dd/yyyy"));
        } catch (DateTimeParseException e) { //Date in other format
            expiry = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M-dd-yyyy"));
        }
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        // Creates Order from stored values in standard format
        return new Order(dateTime, strike, close, underlying, expiry, IV, delta, gamma, vega, theta, rho, orderType);
    }

    @Override
    public int compareTo(Order o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
