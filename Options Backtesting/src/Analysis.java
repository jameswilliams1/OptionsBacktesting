import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Analysis {

    //<editor-fold desc="file">
    // Returns list of orders from input file
    public static ArrayList<Order> ordersFromFile(String filename, String orderType)
            throws IOException {
        ArrayList<Order> data = new ArrayList<Order>();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        int i = 0;
        while ((line = br.readLine()) != null) {
            try {
                Order order = Order.parseLine(line, orderType);
                data.add(order);
            } catch (Exception e) {
                System.out.print("Could not parse line: "); //Skip lines that don't parse
                System.out.println(line);
            }
            i++;
            if (i > 5000) {
                break;
            }
        }
        br.close();
        return data;
    }
    //</editor-fold>

    //<editor-fold desc="getExpiry">
    public static LocalDateTime getExpiry(LocalDateTime dateTime) {
        LocalDate expiryDate = dateTime.toLocalDate().plusMonths(1); //Next month
        expiryDate = expiryDate.withDayOfMonth(expiryDate.lengthOfMonth()); //Last day of month
        while (expiryDate.getDayOfWeek() != DayOfWeek.THURSDAY) {
            expiryDate = expiryDate.minusDays(1); //Find last Fri of month
        }
        LocalTime expiryTime = LocalTime.of(15, 30, 0);
        LocalDateTime expiry = LocalDateTime.of(expiryDate, expiryTime);
        return expiry;
    }
    //</editor-fold>

    //<editor-fold desc="expiryDateTime">
    public static LocalDateTime expiryDateTime(LocalDate expiryDate) {
        LocalTime expiryTime = LocalTime.of(15, 30, 0);
        return LocalDateTime.of(expiryDate, expiryTime);
    }
    //</editor-fold>

    //<editor-fold desc="getUnderlyingChangeDate">
    public static LocalDateTime getUnderlyingChangeDate(LocalDateTime expiry) {
        LocalDate changeDate = expiry.toLocalDate();
        while (changeDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            changeDate = changeDate.plusDays(1);
        }
        LocalTime changeTime = LocalTime.of(9, 15, 0);
        return LocalDateTime.of(changeDate, changeTime);
    }
    //</editor-fold>




    /**
     * Check if an order goes up or down more than 0.5% vs. reference and make trades if so.
     *
     * @param orders
     */
    public static void getChanges(ArrayList<Order> orders, ArrayList<Trade> tradeList, ArrayList<Trade> activeTrades) {
        double ref = orders.get(0).getUnderlying(); //Initially use first underlying value present as comparison reference
        double decreaseRef = ref;
        double increaseRef = ref;
        double minChange = ref * 0.005;
        double callIV = 0;
        double callDelta = 0;
        double callGamma = 0;
        double callVega = 0;
        double callTheta = 0;
        double callRho = 0;
        double callClose = 0;
        double putIV = 0;
        double putDelta = 0;
        double putGamma = 0;
        double putVega = 0;
        double putTheta = 0;
        double putRho = 0;
        double putClose = 0;
        LocalDateTime expiry = expiryDateTime(orders.get(0).getExpiry()); //Initial expiry/ref change date
        LocalDateTime change = getUnderlyingChangeDate(expiry);
        boolean ordersAllowed = false;
        LocalTime startTime = LocalTime.of(9, 20, 0);
        LocalTime endTime = LocalTime.of(15, 20, 0);

        for (int i = 0; i < orders.size(); i++) {
            String orderType = orders.get(i).getType();
            // Get underlying and date of this order
            double underlying = orders.get(i).getUnderlying();
            LocalDateTime orderDateTime = orders.get(i).getDateTime();
            if ( orderDateTime.isAfter(startTime.atDate(orderDateTime.toLocalDate())) && orderDateTime.isBefore(endTime.atDate(orderDateTime.toLocalDate()))){
                ordersAllowed = true;
            }
            else{
                ordersAllowed = false;
            }


            if(orderType.equals("put")){
                putIV = orders.get(i).getIV();
                putDelta = orders.get(i).getDelta();
                putGamma = orders.get(i).getGamma();
                putVega = orders.get(i).getVega();
                putTheta = orders.get(i).getTheta();
                putRho = orders.get(i).getRho();
                putClose = orders.get(i).getClose();
            }
            if(orderType.equals("call")){
                callIV = orders.get(i).getIV();
                callDelta = orders.get(i).getDelta();
                callGamma = orders.get(i).getGamma();
                callVega = orders.get(i).getVega();
                callTheta = orders.get(i).getTheta();
                putRho = orders.get(i).getRho();
                callClose = orders.get(i).getClose();
            }


            if(activeTrades.size()!=0 && ordersAllowed){
                for(int j=0;j<activeTrades.size();j++){

                    //Exit criteria of trades made after underlying decrease
                    if(!activeTrades.get(j).isIncrease() && activeTrades.get(j).getPreviousUnderlying()<=underlying){
                        Trade thisTrade = activeTrades.get(j);
                        String oppSide = "";
                        if(thisTrade.getSide().equals("buy")){
                            oppSide = "sell";
                        }
                        else{
                            oppSide = "buy";
                        }
                        if (thisTrade.getType().equals("put")) {
                            tradeList.add(new Trade(true, thisTrade.getType(), oppSide, thisTrade.getQuantity(), orderDateTime, thisTrade.getStrike(), thisTrade.getPreviousUnderlying(), thisTrade.getExpiry(), putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
                        } else {
                            tradeList.add(new Trade(true, thisTrade.getType(), oppSide, thisTrade.getQuantity(), orderDateTime, thisTrade.getStrike(), thisTrade.getPreviousUnderlying(), thisTrade.getExpiry(), callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
                        }
                        activeTrades.remove(j);
                        j = j-1; //Account for removing an element
                    }
                    //Exit criteria of trades made after underlying increase
                    else if(activeTrades.get(j).isIncrease() && activeTrades.get(j).getPreviousUnderlying()>=underlying){
                        Trade thisTrade = activeTrades.get(j);
                        String oppSide = "";
                        if(thisTrade.getSide().equals("buy")){
                            oppSide = "sell";
                        }
                        else{
                            oppSide = "buy";
                        }
                        if (thisTrade.getType().equals("put")) {
                            tradeList.add(new Trade(true, thisTrade.getType(), oppSide, thisTrade.getQuantity(), orderDateTime, thisTrade.getStrike(), thisTrade.getPreviousUnderlying(), thisTrade.getExpiry(), putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose));
                        } else {
                            tradeList.add(new Trade(true, thisTrade.getType(), oppSide, thisTrade.getQuantity(), orderDateTime, thisTrade.getStrike(), thisTrade.getPreviousUnderlying(), thisTrade.getExpiry(), callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose));
                        }
                        activeTrades.remove(j);
                        j = j-1; //Account for removing an element
                    }



                }
            }



            // Update variables if an order is after change date
            if (orderDateTime.isEqual(change) || orderDateTime.isAfter(change)) {
                ref = orders.get(i).getUnderlying();
                decreaseRef = ref;
                increaseRef = ref;
                minChange = ref * 0.005;
                expiry = expiryDateTime(orders.get(i).getExpiry());
                change = getUnderlyingChangeDate(expiry);
            }

            double decreaseDifference = underlying - decreaseRef;
            double increaseDifference = underlying - increaseRef;


            if (decreaseDifference <= -minChange && ordersAllowed) {
                increaseRef = ref; //Reset increaseRef when price passes ref
                Trade.buyDecreaseTrade(activeTrades, tradeList, orderDateTime, underlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose);
                decreaseRef = underlying;
            }
            if (increaseDifference >= minChange && ordersAllowed) {
                decreaseRef = ref; //Reset decreaseRef when price passes ref
                Trade.buyIncreaseTrade(activeTrades, tradeList, orderDateTime, underlying, expiry, callIV, callDelta, callGamma, callVega, callTheta, callRho, callClose, putIV, putDelta, putGamma, putVega, putTheta, putRho, putClose);
                increaseRef = underlying;
            }

        }
    }
}
