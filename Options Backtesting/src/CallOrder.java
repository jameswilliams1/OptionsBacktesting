import java.text.Format;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.IllegalFormatException;
import java.util.Scanner;

/*
 * Represents a call order from the market
 */
public class CallOrder {

    //<editor-fold desc="variables">
    private final LocalDate date;
    private final LocalTime time;
    private final int strike;
    private final double close;
    private final double underlying;
    private final LocalDate expiry;
    private final double callIV;
    private final double delta;
    private final double gamma;
    private final double vega;
    private final double theta;
    private final double rho;
    //</editor-fold>

    //<editor-fold desc="constructor">
    public CallOrder(LocalDate date, LocalTime time, int strike, double close, double underlying, LocalDate expiry, double callIV, double delta, double gamma, double vega, double theta, double rho) {
        this.date = date;
        this.time = time;
        this.strike = strike;
        this.close = close;
        this.underlying = underlying;
        this.expiry = expiry;
        this.callIV = callIV;
        this.delta = delta;
        this.gamma = gamma;
        this.vega = vega;
        this.theta = theta;
        this.rho = rho;
    }
    //</editor-fold>

    //<editor-fold desc="getters">
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
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

    public double getCallIV() {
        return callIV;
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

    // Parses input string line
    public static CallOrder parseLine(String line) throws IllegalFormatException {
        Scanner s = new Scanner(line);
        s.useDelimiter(",");
        String dateString = "";
        String timeString = "";
        int strike = 0;
        double close = 0;
        double underlying = 0;
        String expiryString = "";
        double callIV = 0;
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
            underlying = s.nextDouble();
            expiryString = s.next();
            callIV = s.nextDouble();
            delta = s.nextDouble();
            gamma = s.nextDouble();
            vega = s.nextDouble();
            theta = s.nextDouble();
            rho = s.nextDouble();
        }
        s.close();
        LocalDate date;
        LocalTime time;
        LocalDate expiry;
        try{
            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M-dd-yyyy"));
        }
        catch (DateTimeParseException e){ //Date in other format
            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/dd/yyyy"));
        }
        time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
        try{
            expiry = LocalDate.parse(expiryString, DateTimeFormatter.ofPattern("M/dd/yyyy"));
        }
        catch (DateTimeParseException e) { //Date in other format
            expiry = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M-dd-yyyy"));
        }
        // Creates CallOrder from stored values
        return new CallOrder(date, time, strike, close, underlying, expiry, callIV, delta, gamma, vega, theta, rho);
    }


}
