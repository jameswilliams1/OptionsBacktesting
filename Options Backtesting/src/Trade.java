import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a trade made based on criteria chosen
 */
public class Trade {

    //<editor-fold desc="variables">
    private final String type;
    private final int quantity;
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
    private final boolean increase;
    private final String side;
    private double exitUnderlying;
    private LocalDateTime exitTime;
    private double exitClose;
    private double tradeProfit;
    //</editor-fold>

    //<editor-fold desc="constructor">
    public Trade(boolean increase, String type, String side, int quantity, LocalDateTime dateTime, int strike, double previousUnderlying, LocalDateTime expiry, double IV, double delta, double gamma, double vega, double theta, double rho, double close) {
        this.type = type;
        this.side = side;
        this.quantity = quantity;
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
        this.increase = increase;
    }
    //</editor-fold>

    //<editor-fold desc="roundUnderlying">
    public static int roundUnderlying(double number) { //Round to nearest 100 (round down for 11450 etc.)
        return new BigDecimal(number / 100).setScale(0, RoundingMode.HALF_DOWN).intValue() * 100;
    }
    //</editor-fold>

    //<editor-fold desc="trades">
    //Buy orders to make when price drops by 0.5%
    public static void buyDecreaseTrade(ArrayList<Trade> activeTrades, ArrayList<Trade> tradeList, LocalDateTime dateTime, double underlying, LocalDateTime expiry, double callIV, double callDelta, double callGamma, double callVega, double callTheta, double callRho, HashMap<Integer, Double> callClose, double putIV, double putDelta, double putGamma, double putVega, double putTheta, double putRho, HashMap<Integer, Double> putClose) {
        double previousUnderlying = underlying * 1.005;
        int roundedUnderlying = roundUnderlying(previousUnderlying);
        //tradeList.add(new Trade(false, "put", "buy", 1000, dateTime, roundedUnderlying - 300, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
        //tradeList.add(new Trade(false, "put", "sell", 1000, dateTime, roundedUnderlying - 200, previousUnderlying, expiry, putIV, -putDelta, -putGamma, -putVega, -putTheta, -putRho, putClose));
        //tradeList.add(new Trade(false, "call", "buy", 1000, dateTime, roundedUnderlying + 100, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
        //tradeList.add(new Trade(false, "call", "sell", 1200, dateTime, roundedUnderlying + 200, previousUnderlying, expiry, callIV, -callDelta, -callGamma, -callVega, -callTheta, -callRho, callClose));
        activeTrades.add(new Trade(false, "put", "buy", 1000, dateTime, roundedUnderlying - 300, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose.get(roundedUnderlying-300)));
        activeTrades.add(new Trade(false, "put", "sell", 1000, dateTime, roundedUnderlying - 200, previousUnderlying, expiry, putIV, -putDelta, -putGamma, -putVega, -putTheta, -putRho, putClose.get(roundedUnderlying-200)));
        activeTrades.add(new Trade(false, "call", "buy", 1000, dateTime, roundedUnderlying + 100, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose.get(roundedUnderlying+100)));
        activeTrades.add(new Trade(false, "call", "sell", 1200, dateTime, roundedUnderlying + 200, previousUnderlying, expiry, callIV, -callDelta, -callGamma, -callVega, -callTheta, -callRho, callClose.get(roundedUnderlying+200)));
    }

    //Buy orders to make when price increases by 0.5%
    public static void buyIncreaseTrade(ArrayList<Trade> activeTrades, ArrayList<Trade> tradeList, LocalDateTime dateTime, double underlying, LocalDateTime expiry, double callIV, double callDelta, double callGamma, double callVega, double callTheta, double callRho, HashMap<Integer, Double> callClose, double putIV, double putDelta, double putGamma, double putVega, double putTheta, double putRho, HashMap<Integer, Double> putClose) {
        double previousUnderlying = underlying * 0.995;
        int roundedUnderlying = roundUnderlying(previousUnderlying);
        //tradeList.add(new Trade(true, "put", "sell", 1200, dateTime, roundedUnderlying - 300, previousUnderlying, expiry, putIV, -putDelta, -putGamma, -putVega, -putTheta, -putRho, putClose));
        //tradeList.add(new Trade(true, "put", "buy", 1000, dateTime, roundedUnderlying - 200, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
        //tradeList.add(new Trade(true, "call", "sell", 1000, dateTime, roundedUnderlying + 100, previousUnderlying, expiry, callIV, -callDelta, -callGamma, -callVega, -callTheta, -callRho, callClose));
        //tradeList.add(new Trade(true, "call", "buy", 1200, dateTime, roundedUnderlying + 200, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
        activeTrades.add(new Trade(true, "put", "sell", 1200, dateTime, roundedUnderlying - 300, previousUnderlying, expiry, putIV, -putDelta, -putGamma, -putVega, -putTheta, -putRho, putClose.get(roundedUnderlying-300)));
        activeTrades.add(new Trade(true, "put", "buy", 1000, dateTime, roundedUnderlying - 200, previousUnderlying, expiry, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose.get(roundedUnderlying-200)));
        activeTrades.add(new Trade(true, "call", "sell", 1000, dateTime, roundedUnderlying + 100, previousUnderlying, expiry, callIV, -callDelta, -callGamma, -callVega, -callTheta, -callRho, callClose.get(roundedUnderlying+100)));
        activeTrades.add(new Trade(true, "call", "buy", 1200, dateTime, roundedUnderlying + 200, previousUnderlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose.get(roundedUnderlying+200)));
    }
    //</editor-fold>

    //<editor-fold desc="toString">
    @Override
    public String toString() {
        return "Trade{" +
                "type='" + type + '\'' +
                ", quantity=" + quantity +
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
                ", increase=" + increase +
                ", side='" + side + '\'' +
                '}';
    }
    //</editor-fold>

    //<editor-fold desc="getters">
    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getStrike() {
        return strike;
    }

    public double getPreviousUnderlying() {
        return previousUnderlying;
    }

    public LocalDateTime getExpiry() {
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

    public double getClose() {
        return close;
    }

    public boolean isIncrease() {
        return increase;
    }

    public String getSide() {
        return side;
    }

    public double getExitUnderlying() {
        return exitUnderlying;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getExitClose() {
        return exitClose;
    }

    public double getTradeProfit() {
        return tradeProfit;
    }
    //</editor-fold>

    public void setExitUnderlying(double exitUnderlying) {
        this.exitUnderlying = exitUnderlying;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public void setExitClose(double exitClose) {
        this.exitClose = exitClose;
    }

    public void setTradeProfit(double tradeProfit) {
        this.tradeProfit = tradeProfit;
    }
}
