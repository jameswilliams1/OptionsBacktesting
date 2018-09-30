import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Trade {

    //<editor-fold desc="variables">
    private final String type;
    private final int quantity;
    private boolean active;
    private final LocalDate date;
    private final LocalTime time;
    private final int strike;
    private final double previousUnderlying;
    private final LocalDate expiry;
    private final double delta;
    private final double gamma;
    private final double vega;
    private final double theta;
    //</editor-fold>

    //<editor-fold desc="constructor">
    public Trade(String type, int quantity, LocalDate date, LocalTime time, int strike, double previousUnderlying, LocalDate expiry){//, double delta, double gamma, double vega, double theta) {
        this.type = type;
        this.quantity = quantity;
        this.active = true; //Open trade
        this.date = date; //Start date/time
        this.time = time;
        this.strike = strike;
        this.previousUnderlying = previousUnderlying;
        this.expiry = expiry;
        this.delta = 0;//delta;
        this.gamma = 0;//gamma;
        this.vega = 0;//vega;
        this.theta = 0;//theta;
    }
    //</editor-fold>

    //<editor-fold desc="getDate">
    public static LocalDate getExpiry(LocalDate date) {
        LocalDate expiry = date.plusMonths(1);
        expiry = expiry.withDayOfMonth(expiry.lengthOfMonth()); //Last day next month
        while (expiry.getDayOfWeek() != DayOfWeek.FRIDAY) {
            expiry = expiry.minusDays(1); //Find last Fri of month
        }
        return expiry;
    }
    //</editor-fold>

    //<editor-fold desc="round">
    public static int roundUnderlying(double number){ //Round to nearest 100 (round down for 11450 etc.)
        return new BigDecimal(number/100).setScale(0, RoundingMode.HALF_DOWN).intValue()*100;
    }
    //</editor-fold>

    public static void decreaseTrade(ArrayList<Trade> buyList, ArrayList<Trade> sellList, LocalDate date, LocalTime time, double previousUnderlying){
        int roundedUnderlying = roundUnderlying(previousUnderlying);
        LocalDate expiry = getExpiry(date);
        buyList.add(new Trade("put", 1000, date, time, roundedUnderlying-300, previousUnderlying, expiry));
        sellList.add(new Trade("put", 1000, date, time, roundedUnderlying-200, previousUnderlying, expiry));
        buyList.add(new Trade("call", 1000, date, time, roundedUnderlying+100, previousUnderlying, expiry));
        sellList.add(new Trade("call", 1200, date, time, roundedUnderlying+200, previousUnderlying, expiry));



    }


}
