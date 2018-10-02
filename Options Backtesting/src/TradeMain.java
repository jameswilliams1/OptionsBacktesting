import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TradeMain {


    public static void main(String[] args) {
        ArrayList<Order> calls = new ArrayList<>();
        ArrayList<Order> puts = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<Trade> tradeList = new ArrayList<>();
        ArrayList<Trade> activeTrades = new ArrayList<>();
        String dir = "";
        String callData = "";
        String putData = "";
        dir = System.getProperty("user.home") + "/Options_Backtest_Output/";
        File folder = new File(dir);
        folder.mkdir();
        ArrayList<String> profitList = new ArrayList<>();

        //<editor-fold desc="inputFiles">
        System.out.println("Enter full filepath for call data:");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            callData = br.readLine();
            //callData = "C:\\Users\\User\\Downloads\\Options Data\\call.csv";
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("Enter full filepath for put data:");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            putData = br.readLine();
            //putData = "C:\\Users\\User\\Downloads\\Options Data\\put.csv";
        } catch (IOException e) {
            System.out.println(e);
        }



        try {
            Analysis.ordersFromFile(callData, "call", orders);
        } catch (IOException e) { //Catch file not found errors
            System.out.println(e);
        }
        try {
            Analysis.ordersFromFile(putData, "put", orders);
        } catch (IOException e) {
            System.out.println(e);
        }
        //</editor-fold>

        Collections.sort(orders);



        if (calls.size() != 0 && puts.size() != 0) {
            orders.addAll(calls);
            orders.addAll(puts);
            Collections.sort(orders);
        }
        if(orders.size()!=0){
            try {
                Analysis.getChanges(orders, tradeList, activeTrades, dir);
                File tradeLogFile = new File(dir + "trades.csv");
                FileWriter fw = new FileWriter(tradeLogFile);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("Enter Time,Enter Underlying,Type,Side,Strike,Quantity,Expiry,Enter Close,Exit Close,Exit Time,Exit Underlying,Profit,IV,Delta,Gamma,Vega,Theta,Rho");
                bw.newLine();
                for(int k = 0; k<tradeList.size(); k++){
                    Trade trade = tradeList.get(k);
                    bw.write(trade.getDateTime() + "," + trade.getPreviousUnderlying()/1.005 + "," + trade.getType() + "," + trade.getSide() + "," + trade.getStrike() + "," + trade.getQuantity() + "," + trade.getExpiry() + "," + trade.getClose() + "," + trade.getExitClose() + "," + trade.getExitTime() + "," + trade.getExitUnderlying() + "," + trade.getTradeProfit()  + "," +  trade.getIV() + "," + trade.getDelta() + "," + trade.getGamma() + "," + trade.getVega() + "," + trade.getTheta() + "," + trade.getRho());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        else{
            System.out.println("There was a problem processing the order files, check the file paths and try again.");
        }
        System.out.println("Backtesting complete.");
        System.out.println("Output saved to " + dir);

    }
}