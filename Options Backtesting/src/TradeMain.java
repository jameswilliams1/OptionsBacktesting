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


        try {
            calls = Analysis.ordersFromFile("C:\\Users\\User\\Downloads\\Options Data\\Call Data_CSV.csv", "call");
        } catch (IOException e) { //Catch file not found errors
            System.out.println(e);
        }
        try {
            puts = Analysis.ordersFromFile("C:\\Users\\User\\Downloads\\Options Data\\Put_Data_CSV.csv", "put");
        } catch (IOException e) {
            System.out.println(e);
        }
        orders.addAll(calls);
        orders.addAll(puts);
        Collections.sort(orders);
        Analysis.getChanges(orders, tradeList, activeTrades);

        for (int i = 0; i < tradeList.size(); i++) {
            System.out.println(tradeList.get(i));
        }

        System.out.println(tradeList.size());
    }
}