import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
            if (i > 20) {
                break;
            }
        }
        br.close();
        return data;
    }
    //</editor-fold>

    public static void checkPrice(ArrayList<Order> orders){
        double initialUnderlying = orders.get(0).getUnderlying();
    }

}
