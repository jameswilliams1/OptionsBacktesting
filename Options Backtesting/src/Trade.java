import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Trade {

    //<editor-fold desc="variables">
    private final String type;
    private final int quantity;
    private boolean active;
    private final LocalDateTime dateTime;
    private final int strike;
    private final double previousUnderlying;
    private final LocalDateTime expiry;
    private final double delta;
    private final double gamma;
    private final double vega;
    private final double theta;
    //</editor-fold>

    //<editor-fold desc="constructor">
    public Trade(String type, int quantity, LocalDateTime dateTime, int strike, double previousUnderlying, LocalDateTime expiry){//, double delta, double gamma, double vega, double theta) {
        this.type = type;
        this.quantity = quantity;
        this.active = true; //Open trade
        this.dateTime = dateTime; //Start date/time
        this.strike = strike;
        this.previousUnderlying = previousUnderlying;
        this.expiry = expiry;
        this.delta = 0;//delta;
        this.gamma = 0;//gamma;
        this.vega = 0;//vega;
        this.theta = 0;//theta;
    }
    //</editor-fold>



    //<editor-fold desc="round">
    public static int roundUnderlying(double number){ //Round to nearest 100 (round down for 11450 etc.)
        return new BigDecimal(number/100).setScale(0, RoundingMode.HALF_DOWN).intValue()*100;
    }
    //</editor-fold>

    //Buy orders to make when price drops by 0.5%
    public static void buyDecreaseTrade(ArrayList<Trade> buyList, ArrayList<Trade> sellList, LocalDateTime dateTime, double previousUnderlying){
        int roundedUnderlying = roundUnderlying(previousUnderlying);
        LocalDateTime expiry = Analysis.getExpiry(dateTime);
        buyList.add(new Trade("put", 1000, dateTime, roundedUnderlying-300, previousUnderlying, expiry));
        sellList.add(new Trade("put", 1000, dateTime, roundedUnderlying-200, previousUnderlying, expiry));
        buyList.add(new Trade("call", 1000, dateTime, roundedUnderlying+100, previousUnderlying, expiry));
        sellList.add(new Trade("call", 1200, dateTime, roundedUnderlying+200, previousUnderlying, expiry));



    }


}
