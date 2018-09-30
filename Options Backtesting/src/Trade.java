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
    private final double IV;
    private final double delta;
    private final double gamma;
    private final double vega;
    private final double theta;
    private final double rho;
    private final double close;
    //</editor-fold>

    //<editor-fold desc="constructor">
    public Trade(String type, int quantity, LocalDateTime dateTime, int strike, double previousUnderlying, LocalDateTime expiry, double IV, double delta, double gamma, double vega, double theta, double rho, double close) {
        this.type = type;
        this.quantity = quantity;
        this.active = true; //Open trade
        this.dateTime = dateTime; //Start date/time
        this.strike = strike;
        this.previousUnderlying = previousUnderlying;
        this.expiry = expiry;
        this.IV = IV;
        this.delta = delta;
        this.gamma = gamma;
        this.vega = vega;
        this.theta = theta;
        this.rho = rho;
        this.close = close;
    }
    //</editor-fold>


    //<editor-fold desc="round">
    public static int roundUnderlying(double number) { //Round to nearest 100 (round down for 11450 etc.)
        return new BigDecimal(number / 100).setScale(0, RoundingMode.HALF_DOWN).intValue() * 100;
    }
    //</editor-fold>

    //<editor-fold desc="trades">
    //Buy orders to make when price drops by 0.5%
    public static void buyDecreaseTrade(ArrayList<Trade> buyList, ArrayList<Trade> sellList, LocalDateTime dateTime, double underlying, LocalDateTime expiry, double callIV, double callDelta, double callGamma, double callVega, double callTheta, double callRho, double callClose, double putIV, double putDelta, double putGamma, double putVega, double putTheta, double putRho, double putClose) {
        double previousUnderlying = underlying * 1.005;
        int roundedUnderlying = roundUnderlying(previousUnderlying);
        buyList.add(new Trade("put", 1000, dateTime, roundedUnderlying - 300, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
        sellList.add(new Trade("put", 1000, dateTime, roundedUnderlying - 200, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
        buyList.add(new Trade("call", 1000, dateTime, roundedUnderlying + 100, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
        sellList.add(new Trade("call", 1200, dateTime, roundedUnderlying + 200, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
    }

    //Buy orders to make when price increases by 0.5%
    public static void buyIncreaseTrade(ArrayList<Trade> buyList, ArrayList<Trade> sellList, LocalDateTime dateTime, double underlying, LocalDateTime expiry, double callIV, double callDelta, double callGamma, double callVega, double callTheta, double callRho, double callClose, double putIV, double putDelta, double putGamma, double putVega, double putTheta, double putRho, double putClose) {
        double previousUnderlying = underlying * 0.995;
        int roundedUnderlying = roundUnderlying(previousUnderlying);
        sellList.add(new Trade("put", 1200, dateTime, roundedUnderlying - 300, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
        buyList.add(new Trade("put", 1000, dateTime, roundedUnderlying - 200, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
        sellList.add(new Trade("call", 1000, dateTime, roundedUnderlying + 100, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
        buyList.add(new Trade("call", 1200, dateTime, roundedUnderlying + 200, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
    }
    //</editor-fold>


    @Override
    public String toString() {
        return "Trade{" +
                "type='" + type + '\'' +
                ", quantity=" + quantity +
                ", active=" + active +
                ", dateTime=" + dateTime +
                ", strike=" + strike +
                ", previousUnderlying=" + previousUnderlying +
                ", expiry=" + expiry +
                ", IV=" + IV +
                ", delta=" + delta +
                ", gamma=" + gamma +
                ", vega=" + vega +
                ", theta=" + theta +
                ", rho=" + rho +
                ", close=" + close +
                '}';
    }
}
