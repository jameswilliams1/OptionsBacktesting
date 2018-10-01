import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

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

        //<editor-fold desc="inputFiles">
        System.out.println("Enter full filepath for call data:");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            callData = br.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("Enter full filepath for put data:");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            putData = br.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }



        try {
            calls = Analysis.ordersFromFile("C:\\Users\\User\\Downloads\\Options Data\\Call Data_CSV.csv", "call");
            //calls = Analysis.ordersFromFile(callData, "call");
        } catch (IOException e) { //Catch file not found errors
            System.out.println(e);
        }
        try {
            puts = Analysis.ordersFromFile("C:\\Users\\User\\Downloads\\Options Data\\Put_Data_CSV.csv", "put");
            //puts = Analysis.ordersFromFile(putData, "put");
        } catch (IOException e) {
            System.out.println(e);
        }
        //</editor-fold>




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
                bw.write("Time,Type,Side,Strike,Quantity,Expiry,Close,IV,Delta,Gamma,Vega,Theta,Rho");
                bw.newLine();
                for(int k = 0; k<tradeList.size(); k++){
                    Trade trade = tradeList.get(k);
                    bw.write(trade.getDateTime() + "," + trade.getType() + "," + trade.getSide() + "," + trade.getStrike() + "," + trade.getQuantity() + "," + trade.getExpiry() + "," + trade.getClose() + "," + trade.getIV() + "," + trade.getDelta() + "," + trade.getGamma() + "," + trade.getVega() + "," + trade.getTheta() + "," + trade.getRho());
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


    }
}